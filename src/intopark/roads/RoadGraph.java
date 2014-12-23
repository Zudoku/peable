/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package intopark.roads;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import intopark.UtilityMethods;
import intopark.roads.events.UpdateRoadEvent;
import intopark.util.MapPosition;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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
    private DirectedGraph<Walkable,DefaultEdge> roadMap= new DefaultDirectedGraph<>(DefaultEdge.class);
    //VARIABLES

    @Inject
    public RoadGraph() {

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
                    UpdateRoadEvent uREvent = new UpdateRoadEvent(road,connected);
                    uREvent.setFirsTimeUpdated(road.isFirstTimeUpdated());
                    eventBus.post(uREvent);
                    if(road.isFirstTimeUpdated()){
                        road.setFirstTimeUpdated(false);
                    }
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
        boolean addedEdge = false;
        if(!roadMap.containsEdge(walk1, walk2)){
            roadMap.addEdge(walk1, walk2);
            addedEdge = true;
            logger.log(Level.FINEST,"Walkables {0} and {1} connected via edge ", new Object[]{walk1.position.getVector(),walk2.position.getVector()});
        }
        if(!roadMap.containsEdge(walk2, walk1)){
            roadMap.addEdge(walk2, walk1);
            addedEdge = true;
            logger.log(Level.FINEST,"Walkables {0} and {1} connected via edge ", new Object[]{walk2.position.getVector(),walk1.position.getVector()});
        }
        if(!addedEdge){
            logger.log(Level.FINEST,"Walkables {0} and {1} already connected: only updating.", new Object[]{walk1.position.getVector(),walk2.position.getVector()});
        }
        walk1.setNeedsUpdate(true);
        walk2.setNeedsUpdate(true);
    }
    public void deleteWalkable(Walkable walkable){
        List<Walkable> walkablesThatNeedUpdateAfterDelete = new ArrayList<>();
        /* Look up all the edges connected to walkable. */
        Set<DefaultEdge>edgesDelete=new HashSet<>(roadMap.outgoingEdgesOf(walkable));
        edgesDelete.addAll(roadMap.incomingEdgesOf(walkable));
        for(DefaultEdge edge:edgesDelete){
            Walkable connectedWalkable = UtilityMethods.getTheOtherEndOfEdge(this, edge, walkable);
            if(connectedWalkable!=null){
                walkablesThatNeedUpdateAfterDelete.add(connectedWalkable);
            }else{
                logger.log(Level.WARNING,"Couldn't find the walkable that need to be updated.");
            }
        }
        logger.log(Level.FINEST,"Deleting {0} edges.",edgesDelete.size());
        /* Delete them */
        roadMap.removeAllEdges(edgesDelete);
        /* Delete also the vertex */
        roadMap.removeVertex(walkable);
        for(Walkable needUpdate:walkablesThatNeedUpdateAfterDelete){
            needUpdate.setNeedsUpdate(true);
        }
        logger.log(Level.FINEST,"Deleted Walkable ({0}) from roadGraph.",walkable.getClass().toString());
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
