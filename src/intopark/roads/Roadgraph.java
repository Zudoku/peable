/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package intopark.roads;

import com.google.common.eventbus.EventBus;
import com.google.inject.Singleton;
import intopark.roads.events.UpdateRoadEvent;
import intopark.terrain.MapPosition;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jgraph.graph.DefaultEdge;
import org.jgrapht.DirectedGraph;
import org.jgrapht.graph.DefaultDirectedGraph;

/**
 *
 * @author arska
 */
@Singleton
public class Roadgraph {
    //LOGGER
    private transient static final Logger logger = Logger.getLogger(Roadgraph.class.getName());
    //DEPENDENCIES
    private transient EventBus eventBus;
    //OWNS
    private DirectedGraph<Walkable,DefaultEdge> roadMap;
    //VARIABLES
    
    public Roadgraph() {
        roadMap = new DefaultDirectedGraph<>(DefaultEdge.class);

    }
    public void update(){
        for(Walkable walkable:roadMap.vertexSet()){
            if(walkable.isNeedsUpdate()){
                if(walkable instanceof Road){
                    Road road=(Road)walkable;
                    
                    /* We need to know where the connected vertexes are. N-S-E-W */
                    boolean[] connected=new boolean[]{false,false,false,false};
                    for(DefaultEdge e:roadMap.incomingEdgesOf(road)){
                        Walkable source=(Walkable)roadMap.getEdgeSource(e);
                        if(source.position.getX()>road.position.getX()){
                            connected[0]=true;
                        }
                        if(source.position.getX()<road.position.getX()){
                            connected[1]=true;
                        }
                        if(source.position.getZ()>road.position.getZ()){
                            connected[2]=true;
                        }
                        if(source.position.getZ()<road.position.getZ()){
                            connected[3]=true;
                        }
                    }
                    logger.log(Level.FINEST,"Road {0} needs updating!",road.getID());
                    eventBus.post(new UpdateRoadEvent(road,connected));
                    
                }else if(walkable instanceof BuildingEnterance){
                    BuildingEnterance enterance=(BuildingEnterance)walkable;
                    /* DO SOMETHING */
                    enterance.setNeedsUpdate(false);
                }
            }
        }
    }
    public void addWalkable(Walkable walkable){
        if(walkable==null){
            throw new IllegalArgumentException("Cannot add null walkable to graph.");
        }
        if (walkable instanceof Road) {
            Road road = (Road) walkable;
            roadMap.addVertex(road);
            road.setNeedsUpdate(true);
            for (Walkable r : roadMap.vertexSet()) {
                if (r instanceof Road) {
                    Road road2 = (Road) r;
                    /* If road2 and road can physically connect */
                    if (road2.canConnect(road)) {
                        /* If road is normal road */
                        if (!road.getQueRoad()) {
                            /* And road2 is normal road*/
                            if (!road2.getQueRoad()) {
                                /* Connect them */
                                connectWalkables(road2, road);
                            } /* And road is queRoad*/ 
                            else {
                                /* If road2 isn't connected to two vertixes already (2 edges between two vertixes. One for each direction)*/
                                if (!(roadMap.edgesOf(road2).size() > 4)) {
                                    /* Then connect them */
                                    connectWalkables(road2, road);
                                }
                            }
                        } /* road is queRoad */ 
                        else {
                            /* If road can connect since it's queRoad */
                            if (!(roadMap.edgesOf(road).size() > 4)) {
                                /* And road2 is normal road*/
                                if (!road2.getQueRoad()) {
                                    /* Connect them */
                                    connectWalkables(road2, road);
                                } else {
                                    /* If road2 isn't connected to two vertixes already (2 edges between two vertixes. One for each direction)*/
                                    if (!(roadMap.edgesOf(road2).size() > 4)) {
                                        /* Then connect them */
                                        connectWalkables(road2, road);
                                    }
                                }
                            } 
                            else {
                                /* road already has 2 edges */
                                
                            }

                        }
                    } else {
                        /* CANT CONNECT PHYSICALLY */
                    }
                }
                else {
                    /* TODO: ROAD TO OTHER WALKABLE(BUILDING) */
                }

            }
        }
    }
    private void connectWalkables(Walkable walk1,Walkable walk2){
        if(!roadMap.containsEdge(walk1, walk2)){
            roadMap.addEdge(walk1, walk2);
            roadMap.addEdge(walk2, walk1);
            logger.log(Level.FINEST,"Walkables {0} and {1} connected via edge ", new Object[]{walk1.position.getVector(),walk2.position.getVector()});
        }else{
            logger.log(Level.FINEST,"Walkables {0} and {1} already connected: only updating.", new Object[]{walk1.position.getVector(),walk2.position.getVector()});
        }
        
        walk1.setNeedsUpdate(true);
        walk2.setNeedsUpdate(true);
    }
    public void deleteWalkable(Walkable walkable){
        /* Look up all the edges connected to walkable. */
        Set<DefaultEdge>edgesDelete=roadMap.outgoingEdgesOf(walkable);
        edgesDelete.addAll(roadMap.incomingEdgesOf(walkable));
        logger.log(Level.FINEST,"Deleting {0} edges.",edgesDelete.size());
        /* Delete them */
        roadMap.removeAllEdges(edgesDelete);
        /* Delete also the vertex */
        roadMap.removeVertex(walkable);
        String type=" ";
        if(walkable instanceof Road){
            type="road";
        }
        if(walkable instanceof BuildingEnterance){
            type="building enterance";
        }
        logger.log(Level.FINEST,"Deleted Walkable ({0}) from the graph.",type);
    }
    
    public DirectedGraph<Walkable, DefaultEdge> getRoadMap() {
        return roadMap;
    }
    public boolean isThereRoom(MapPosition position){
        for(Walkable walkable:roadMap.vertexSet()){
            if(position.isSameMainCoords(walkable.position)){
                return false;
            }
        }
        return true;
    }

    public void setEventBus(EventBus eventBus) {
        this.eventBus = eventBus;
    }
    public Walkable getWalkable(int x,int y,int z){
        Walkable walkable=null;
        for(Walkable test:roadMap.vertexSet()){
            if(test.position.getX()!=x){
                continue;
            }
            if(test.position.getY()!=y){
                continue;
            }
            if(test.position.getZ()!=z){
                continue;
            }
            walkable=test;
            break;
        }
        return walkable;
    }
    
    
}
