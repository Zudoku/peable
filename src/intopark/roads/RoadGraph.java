/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package intopark.roads;

import com.google.common.eventbus.EventBus;
import com.google.inject.Singleton;
import intopark.roads.events.UpdateRoadEvent;
import intopark.util.MapPosition;
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
public class RoadGraph {
    //LOGGER
    private transient static final Logger logger = Logger.getLogger(RoadGraph.class.getName());
    //DEPENDENCIES
    private transient EventBus eventBus;
    //OWNS
    private DirectedGraph<Walkable,DefaultEdge> roadMap;
    //VARIABLES

    public RoadGraph() {
        roadMap = new DefaultDirectedGraph<>(DefaultEdge.class);
        //TODO: PROPERLY DOCUMENT THIS CLASS.
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
                    /* DO SOMETHING ...*/
                    enterance.setNeedsUpdate(false);
                }
            }
        }
    }
    public void addWalkable(Walkable walkable){
        if(walkable==null){
            throw new IllegalArgumentException("Cannot add null walkable to graph.");
        }
        roadMap.addVertex(walkable);
        if(walkable instanceof Road){
            Road road=(Road)walkable;
            road.setNeedsUpdate(true);
        }
        for (Walkable r : roadMap.vertexSet()) {
            if (canWalkablesConnect(walkable, r)) {
                connectWalkables(walkable, r);
            }
        }
    }
    private boolean canWalkablesConnect(Walkable walk1,Walkable walk2){
        if(walk1 instanceof Road){
            if(walk2 instanceof Road){
                return canRoadConnectWalkable((Road)walk1,walk2) && canRoadConnectWalkable((Road)walk2,walk1);
            }
            else if(walk2 instanceof BuildingEnterance){
                return canRoadConnectWalkable((Road)walk1,walk2) && enteranceAcceptRoad((BuildingEnterance)walk2,(Road)walk1);
            }
        }else if(walk1 instanceof BuildingEnterance){
            if(walk2 instanceof Road){
                return enteranceAcceptRoad((BuildingEnterance)walk1,(Road)walk2) && canRoadConnectWalkable((Road)walk2, walk1);
            }else if(walk2 instanceof BuildingEnterance){
                return false;
            }
        }
        /* unknown type*/
        return false;
    }
    private boolean canRoadConnectWalkable(Road road,Walkable walkable){
            /* If road and walkable can physically connect */
            if (road.canConnect(walkable)) {
                /* If road is normal road */
                if (!road.getQueRoad()) {
                    /* No restrictions on how many edges it can make. */
                    return true;
                }else{
                    /* If not connected to 2 vertexes */
                    if ((roadMap.edgesOf(road).size() < 4)) {
                        /* Can connect */
                        return true;
                    }else{
                        return false;
                    }
                }
            }else{
                /* Cant connect physically */
                return false;
            }
    }
    private boolean enteranceAcceptRoad(BuildingEnterance enterance,Road road){
        if (enterance.getBuildingType() == BuildingEnterance.SHOP) {
            if (road.getQueRoad()) {
                /* dont accept queroads */
                return false;
            } else {
                /* accept normal roads */
                return true;
            }
        } else if (enterance.getBuildingType() == BuildingEnterance.RIDE) {
            if (road.getQueRoad()) {
                /* accept queroads */
                return true;
            } else {
                /* dont accept normal roads */
                return false;
            }
        } else {
            /* unknown type*/
            return false;
        }
    }

    private void connectWalkables(Walkable walk1,Walkable walk2){
        //TODO make it that it checks for both edges being there.
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
            /* Ignore building enterances */
            if(walkable instanceof BuildingEnterance){
                continue;
            }
            if(position.isSameMainCoords(walkable.position)){
                return false;
            }
        }
        return true;
    }

    public void setEventBus(EventBus eventBus) {
        this.eventBus = eventBus;
    }
    public Walkable getWalkable(int x,int y,int z,boolean tryignoreBE){
        Walkable walkable=null;
        BuildingEnterance ent = null;
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
            if(tryignoreBE && test instanceof BuildingEnterance){
                ent=(BuildingEnterance)test;
                continue;
            }
            walkable=test;
            break;
        }
        if(walkable==null && ent!=null){
            walkable=ent;
        }
        return walkable;
    }


}
