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
                    /* TODO: NOT ROAD */
                }

            }
        }
    }
    private void connectWalkables(Walkable walk1,Walkable walk2){
        roadMap.addEdge(walk1, walk2);
        roadMap.addEdge(walk2, walk1);
        walk1.setNeedsUpdate(true);
        walk2.setNeedsUpdate(true);
    }
    public void deleteRoad(Road road){
        /* Look up all the edges connected to road. */
        Set<DefaultEdge>edgesDelete=roadMap.outgoingEdgesOf(road);
        edgesDelete.addAll(roadMap.incomingEdgesOf(road));
        /* Delete them */
        roadMap.removeAllEdges(edgesDelete);
        /* Delete also the vertex */
        roadMap.removeVertex(road);
    }

    DirectedGraph<Walkable, DefaultEdge> getRoadMap() {
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
    
    
}
