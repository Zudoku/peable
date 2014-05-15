/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package intopark.roads;

import intopark.roads.events.CreateRoadEvent;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import intopark.GUI.events.UpdateMoneyTextBarEvent;
import intopark.GUI.events.UpdateRoadDirectionEvent;
import intopark.Gamestate;
import intopark.UtilityMethods;
import intopark.inputhandler.MouseContainer;
import intopark.inputhandler.NeedMouse;
import intopark.roads.events.CreateBuildingEnteranceEvent;
import intopark.roads.events.UpdateRoadEvent;
import intopark.util.Direction;
import intopark.terrain.MapContainer;
import intopark.util.MapPosition;
import intopark.terrain.ParkHandler;
import intopark.terrain.decoration.RotationEvent;

/**
 *
 * @author arska
 */
@Singleton
public class RoadMaker implements NeedMouse{
    //LOGGER
    private static final Logger logger = Logger.getLogger(RoadMaker.class.getName());
    //MISC
    //DEPENDENCIES
    @Inject private Roadgraph roadGraph;
    private Node roadNode;
    @Inject public RoadFactory roadF;
    private MapContainer map;
    @Inject
    private ParkHandler parkHandler;
    private final EventBus eventBus;
    //OWNS
    private List<Spatial> roadSpatials = new ArrayList<>();
    //VARIABLES
    private Direction direction = Direction.NORTH;
    private int slope = 0; //0= flat 1=up 2=down
    private RoadMakerStatus status = RoadMakerStatus.MANUAL;
    private MapPosition startingPosition;
    private MapPosition endingPosition;
    private boolean queroad = false;
    private boolean change = true;
    private int ID = 1;
    /**
     * This Class is the Main control which does everything regarding to roadSpatials including 
     * loading them and calculating where to move them
     * @param assetManager This is needed to load the road Spatial
     * @param rootNode This is used to attach the roadSpatials to the world
     * @param map This is used to access the space where the roadSpatials are going to be
     * @param eventBus This is used to send events to other components
     */
    @Inject
    public RoadMaker(Node rootNode,EventBus eventBus) {
        roadNode=new Node("roadNode");
        rootNode.attachChild(roadNode);
        this.eventBus = eventBus;
        eventBus.register(this);
        this.map = map;
        roadF = new RoadFactory();
    }
    public void update(){
        roadGraph.update();
    }
    public boolean buildAutomaticRoad(){
        if(startingPosition==null||endingPosition==null){
            logger.log(Level.FINE, "STARTING OR ENDING POS NULL");
            return false;
        }
        if(startingPosition.isSameMainCoords(endingPosition)){
            logger.log(Level.FINE, "SAME POS");
            return false;
        }
        boolean  build=true;
        int counter=0;
        while(build){
            counter++;
            direction=endingPosition.getDirection(startingPosition.getX(), startingPosition.getZ());
            if(startingPosition.getY()<endingPosition.getY()){
                slope=1;
            }else if(startingPosition.getY()>endingPosition.getY()){
                slope=2;
            }else{
                slope=0;
            }
            manualBuildRoad();
            if(startingPosition.isSameMainCoords(endingPosition)){
                build=false;
            }
            if(counter>100){
                build=false;
                logger.log(Level.WARNING,"ROAD TOOL LOOPED 100 TIMES, QUITTING");
            }
        }
        logger.log(Level.FINE, "building done");
        
        startingPosition=null;
        endingPosition=null;
        return true;
        
    }
    /**
     * This calculates next position for road. It calculates it based on direction.
     * @return Position where the next road is going to be
     */
    public MapPosition calcNextManualRoadPosition() {
        MapPosition roadPos = new MapPosition(startingPosition);
        switch (direction) {
            case NORTH:
                roadPos.setX(roadPos.getX() + 1);
                break;

            case SOUTH:
                roadPos.setX(roadPos.getX() - 1);
                break;

            case EAST:
                roadPos.setZ(roadPos.getZ() + 1);
                break;

            case WEST:
                roadPos.setZ(roadPos.getZ() - 1);
                break;
        }
        roadPos.setOffSetY(0.1f);
        return roadPos;
    }
    /**
     * 
     */
    public void manualBuildRoad() {
        if (status == RoadMakerStatus.CHOOSING) {
            logger.log(Level.WARNING,"Yo,yo we cant build while in choosing-mode. This should not happen ever");
            return;
        }
        
        MapPosition constructedPosition=new MapPosition(calcNextManualRoadPosition());
        Direction roadDir=direction;
        RoadHill angle=RoadHill.FLAT;
        int skin=1;
        if(slope!=0){
            angle=RoadHill.UP;
        }
        if(slope==2){ //DOWNHILL
            constructedPosition.setY(constructedPosition.getY()-1);
            roadDir=roadDir.getOpposite();
        }
        Road road = new Road(constructedPosition,angle,ID,skin,queroad,roadDir);
        eventBus.post(new CreateRoadEvent(road));
        /**
         * Calculate a new starting position based on where you placed your road
         */
        switch (slope) {
            case 0:
                startingPosition=new MapPosition(constructedPosition);
                startingPosition.setOffSetY(startingPosition.getOffSetY()-0.1f);
                break;

            case 1:
                startingPosition=new MapPosition(constructedPosition);
                startingPosition.setOffSetY(startingPosition.getOffSetY()-0.1f);
                startingPosition.setY(startingPosition.getY()+1);
                break;

            default:
                startingPosition=new MapPosition(constructedPosition);
                startingPosition.setOffSetY(startingPosition.getOffSetY()-0.1f);
                break;

        }
    }
    @Subscribe
    public void listenCreateRoadEvent(CreateRoadEvent event){
        int ROADPRICE=10;
        if(event.getRoad()==null){
            /* FAILED */
            return;
        }
        if (!parkHandler.getParkWallet().canAfford(ROADPRICE)) {
            logger.log(Level.FINEST,"You cant afford building a road.");
            return;
        }
        MapPosition pos=event.getRoad().getPosition();
        if(!roadGraph.isThereRoom(pos)){
            logger.log(Level.FINEST,"Already Walkable on the way. Can't build here.");
            return;
        }
        if(!parkHandler.testForEmptyTile(pos.getX(), pos.getY(),pos.getZ())){
            logger.log(Level.FINEST,"Already something on the way. Can't build here.");
            return;
        }
        try{
            roadGraph.addWalkable(event.getRoad());
        }catch(IllegalArgumentException ex){
            logger.log(Level.SEVERE, "ILLEGAL ARGUMENTS: {0}",ex);
            return;
        }
        parkHandler.getParkWallet().remove(10);
        eventBus.post(new UpdateMoneyTextBarEvent());
        ID++;
        logger.log(Level.FINEST,"Road added succesfully to {0}",event.getRoad().getVector3f());
    }

    @Subscribe
    public void listenCreateBuildingEnteranceEvent(CreateBuildingEnteranceEvent event){
        if(event.getEnterance()==null){
            return;
        }
        MapPosition pos=event.getEnterance().getPosition();
        if(!roadGraph.isThereRoom(pos)){
            logger.log(Level.FINEST,"Already Walkable on the way. Can't build here.");
            return;
        }
        try{
            roadGraph.addWalkable(event.getEnterance());
        }catch(IllegalArgumentException ex){
            logger.log(Level.SEVERE, "ILLEGAL ARGUMENTS: {0}",ex);
            return;
        }
        logger.log(Level.FINEST,"BuildingEnterance added succesfully to {0}",event.getEnterance().getPosition().getVector());
        
    }
    @Subscribe
    public void listenUpdateRoadEvent(UpdateRoadEvent event){
        int id = event.getExistingRoad().getID();
        Spatial oldRoad = null;
        for(Spatial s:roadSpatials){
            int value=s.getUserData("ID");
            if(value==id){
                oldRoad=s;
                break;
            }
        }
        if(oldRoad==null){
            /* FAILED CANT FIND ROAD WITH THAT ID   */
            logger.log(Level.FINER, "Unable to find roadSpatial with ID {0}",id);
        }else{
            /* Delete old road */
            roadNode.detachChild(oldRoad);//FROM GRAPH
            roadSpatials.remove(oldRoad);//FROM LIST
            logger.log(Level.FINEST,"Deleted old roadSpatial with ID {0}",id);
        }
        /* Get new road */
        Spatial newRoad=roadF.roadToSpatial(event.getExistingRoad(),event.getConnected());
        /* Add new road  */
        roadNode.attachChild(newRoad);
        roadSpatials.add(newRoad);
        event.getExistingRoad().setNeedsUpdate(false);
    }

    @Subscribe
    public void listenRotateEvent(RotationEvent event) {
        if (event.getWho() == 1) {
            if (event.getValue() == 1) {
                turnRight();
            } else {
                turnLeft();
            }
        }
    }

    public void turnLeft() {
        if (direction == Direction.NORTH) {
            direction = Direction.WEST;
            eventBus.post(new UpdateRoadDirectionEvent(direction));
            return;
        }
        if (direction == Direction.WEST) {
            direction = Direction.SOUTH;
            eventBus.post(new UpdateRoadDirectionEvent(direction));
            return;
        }
        if (direction == Direction.SOUTH) {
            direction = Direction.EAST;
            eventBus.post(new UpdateRoadDirectionEvent(direction));
            return;
        }
        if (direction == Direction.EAST) {
            direction = Direction.NORTH;
            eventBus.post(new UpdateRoadDirectionEvent(direction));
        }
    }

    public void turnRight() {
        if (direction == Direction.NORTH) {
            direction = Direction.EAST;
            eventBus.post(new UpdateRoadDirectionEvent(direction));
            return;
        }
        if (direction == Direction.EAST) {
            direction = Direction.SOUTH;
            eventBus.post(new UpdateRoadDirectionEvent(direction));
            return;
        }
        if (direction == Direction.SOUTH) {
            direction = Direction.WEST;
            eventBus.post(new UpdateRoadDirectionEvent(direction));
            return;
        }
        if (direction == Direction.WEST) {
            direction = Direction.NORTH;
            eventBus.post(new UpdateRoadDirectionEvent(direction));
        }
    }

    public RoadMakerStatus getStatus() {
        return status;
    }

    public Roadgraph getRoadGraph() {
        return roadGraph;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }
    public void setStatus(RoadMakerStatus status) {
        this.status = status;
    }

    public int getHill() {
        return slope;
    }

    public void setHill(int hill) {
        this.slope = hill;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public boolean isQueroad() {
        return queroad;
    }

    public void setQueroad(boolean queroad) {
        this.queroad = queroad;
    }
    public void setStartingPosition(Vector3f pos) {
        Vector3f vector= UtilityMethods.roundVector(pos);
        startingPosition =new MapPosition(vector);
    }

    public void setEndingPosition(Vector3f pos) {
        Vector3f vector= UtilityMethods.roundVector(pos);
        endingPosition =new MapPosition(vector);
    }

    public MapPosition getEndingPosition() {
        return endingPosition;
    }

    public MapPosition getStartingPosition() {
        return startingPosition;
    }

    @Override
    public void onClick(MouseContainer container) {
        if (status == RoadMakerStatus.CHOOSING) {
            setStartingPosition(container.getResults().getClosestCollision().getContactPoint());
            setStatus(RoadMakerStatus.MANUAL);
            Gamestate.ingameHUD.updateClickingIndicator();
            logger.log(Level.FINEST, "Updated Roads starting position");
        } else if (status == RoadMakerStatus.AUTOMATIC) {
            
            if (container.isLeftClick()) {
                CollisionResult result = null;
                for (CollisionResult r : container.getResults()) {
                    if (UtilityMethods.findUserDataType(r.getGeometry().getParent(), "Terrain")) {
                        result = r;
                        break;
                    }
                }
                if (result != null) {
                    Vector3f location = result.getContactPoint();
                    if (startingPosition == null) {
                        setStartingPosition(location);
                    } else {
                        setEndingPosition(location);
                        buildAutomaticRoad();
                    }
                }
            }
            
        } else {
            logger.log(Level.FINER, "Tried clicking while in road mode and not choosing start position, not doing anything");
        }
    }

    @Override
    public void onDrag(MouseContainer container) {
        
    }

    @Override
    public void onDragRelease(MouseContainer container) {
        
    }
}
