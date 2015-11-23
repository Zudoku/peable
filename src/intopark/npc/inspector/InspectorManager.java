/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package intopark.npc.inspector;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Spatial;
import intopark.UtilityMethods;
import intopark.inout.Identifier;
import intopark.npc.ActionType;
import intopark.npc.NPCAction;
import intopark.npc.NPCManager;
import intopark.roads.RoadGraph;
import intopark.roads.Walkable;
import intopark.util.MapPosition;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jgraph.graph.DefaultEdge;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.DijkstraShortestPath;

/**
 *
 * @author arska
 */
@Singleton
public class InspectorManager {
    private transient static final Logger logger = Logger.getLogger(InspectorManager.class.getName());
    private transient EventBus eventBus;
    private List<Inspector> inspectorsOnWork = new ArrayList<>();
    private transient Identifier identifier;
    private MapPosition spawnPoint = new MapPosition(6, 6, 0);
    private transient NPCManager nPCManager;
    private transient RoadGraph roadGraph;

    @Inject
    public InspectorManager(EventBus eventBus, Identifier identifier,RoadGraph roadGraph) {
        this.eventBus = eventBus;
        this.identifier = identifier;
        //this.spawnPoint = new MapPosition(6, 6, 0); //FIXME
        this.roadGraph = roadGraph;
        eventBus.register(this);
    }
    public void update(){
        updatePaths();
    }
    private void updatePaths(){
        for(Inspector inspector:inspectorsOnWork){
            if(inspector.getActions().isEmpty()){
                pathFind(inspector);
            }
        }
    }
    public void pathFind(Inspector ins){
        Walkable standing = roadGraph.getWalkable(ins.getPosition().getX(), ins.getPosition().getY(), ins.getPosition().getZ(), true);
        if(standing == null){
            logger.log(Level.FINER, "Inspector {0} can't find path because it's not on a road.");
        }
        else {
            if (!ins.isInspected()) {
                DijkstraShortestPath pathfind = new DijkstraShortestPath(roadGraph.getRoadMap(), standing, ins.getTargetRide().getEnterance().getEnteranceWalkable());
                GraphPath path = pathfind.getPath();
                if (path == null) {
                    logger.log(Level.FINEST, "Inspector {0} can't find path because there is no valid path to the destination.");
                    return;
                }
                MapPosition lastPosition = null;
                for (Object e : path.getEdgeList()) {
                    DefaultEdge edge = (DefaultEdge) e;
                    Walkable point = roadGraph.getRoadMap().getEdgeTarget(edge);
                    ins.getActions().add(new NPCAction(point.getPosition(), ActionType.NOTHING, ins, true));
                    lastPosition = point.getPosition();
                }
                ins.getActions().add(new NPCAction(lastPosition, ActionType.INSPECT,ins,false));
            } else {
                //Try to get spawnpoint
                Walkable spawnWalkable = roadGraph.getWalkable(spawnPoint.getX(), spawnPoint.getY(), spawnPoint.getZ(), true);
                if (spawnWalkable == null) {
                    //PANIC
                    logger.log(Level.FINE, "Inspector {0} panics. He/she will now disappear and exit the park magically because he/she can't find spawn.",ins);
                    ins.getActions().add(new NPCAction(standing.getPosition(), ActionType.EXIT, ins, false));
                }
                if(moveAlongPath(ins, standing, spawnWalkable)){
                    //ADD EXIT ACTION
                    logger.log(Level.FINEST, "Inspector {0} found a way to the exit and will exit the park once he/she gets there.",ins);
                    ins.getActions().add(new NPCAction(spawnPoint, ActionType.EXIT, ins, false));
                }else{
                    //PANIC
                    logger.log(Level.FINE, "Inspector {0} panics. He/she will now disappear and exit the park magically because he/she can't find path to spawn.",ins);
                    ins.getActions().add(new NPCAction(standing.getPosition(), ActionType.EXIT, ins, false));
                }

            }
        }

    }
    private boolean moveAlongPath(Inspector ins,Walkable start,Walkable stop){
        DijkstraShortestPath pathfind = new DijkstraShortestPath(roadGraph.getRoadMap(), start,stop);
        GraphPath path = pathfind.getPath();
        if (path == null) {
            logger.log(Level.FINEST, "Inspector {0} can't find path because there is no valid path to the destination.");
            return false;
        }
        for (Object e : path.getEdgeList()) {
            DefaultEdge edge = (DefaultEdge) e;
            Walkable point = roadGraph.getRoadMap().getEdgeTarget(edge);
            ins.getActions().add(new NPCAction(point.getPosition(), ActionType.NOTHING, ins, true));
        }
        return true;
    }
    @Subscribe
    public void listenInviteInspectorEvent(InviteInspectorEvent event){
        //Create new Inspector
        Spatial model = UtilityMethods.getDebugBoxSpatial(0.2f, ColorRGBA.randomColor());//UtilityMethods.loadModel(LoadPaths.inspector); //Load up 3D-model
        int ID = identifier.reserveID();
        Inspector newInspector = new Inspector("Test_Inspector",model,ID,10,new MapPosition(spawnPoint),event.getRideToInspect(),event.isPaid()?event.getPaidAmount():0f);
        CreateInspectorEvent inspectorEvent = new CreateInspectorEvent(newInspector);
        eventBus.post(inspectorEvent);
    }
    @Subscribe
    public void listenCreateInspectorEvent(CreateInspectorEvent event){
        Inspector inspector = event.getInspector();
        nPCManager.attachToNPCNode(inspector.getGeometry());
        identifier.addOldObject(inspector.getID(), inspector);
        inspectorsOnWork.add(inspector);
        nPCManager.getNpcs().add(inspector);
        logger.log(Level.FINEST, "Inspector {0}, initialized!",inspector.getName());
    }

    public void setnPCManager(NPCManager nPCManager) {
        this.nPCManager = nPCManager;
    }

    public List<Inspector> getInspectorsOnWork() {
        return inspectorsOnWork;
    }




}
