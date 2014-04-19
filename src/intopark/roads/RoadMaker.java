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
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import intopark.GUI.events.UpdateMoneyTextBarEvent;
import intopark.GUI.events.UpdateRoadDirectionEvent;
import intopark.UtilityMethods;
import intopark.roads.events.UpdateRoadEvent;
import intopark.terrain.Direction;
import intopark.terrain.MapContainer;
import intopark.terrain.MapPosition;
import intopark.terrain.ParkHandler;
import intopark.terrain.decoration.RotationEvent;

/**
 *
 * @author arska
 */
@Singleton
public class RoadMaker {
    //LOGGER
    private static final Logger logger = Logger.getLogger(RoadMaker.class.getName());
    //MISC
    //DEPENDENCIES
    @Inject private Roadgraph roadGraph;
    private Node roadNode;
    public RoadFactory roadF;
    private MapContainer map;
    @Inject
    private ParkHandler parkHandler;
    private final EventBus eventBus;
    //OWNS
    private List<Spatial> roadSpatials = new ArrayList<>();
    //VARIABLES
    private Direction direction = Direction.NORTH;
    private RoadHill slope = RoadHill.FLAT;
    private RoadMakerStatus status = RoadMakerStatus.BUILDING;
    private MapPosition startingPosition;
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
    /**
     * This calculates next position for road. It calculates it based on direction.
     * @return Position where the next road is going to be
     */
    public MapPosition calcNextRoadPosition() {
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
        roadPos.setOffSetY(roadPos.getY() + 0.1f);
        return roadPos;
    }
    /**
     * 
     */
    public void buildRoad() {
        if (status != RoadMakerStatus.BUILDING) {
            logger.log(Level.WARNING,"Yo,yo we cant build while not in building-mode. This should not happen ever");
            return;
        }
        
        MapPosition constructedPosition=new MapPosition(calcNextRoadPosition());
        Road road = new Road(constructedPosition, slope, ID,1, queroad, direction);
        eventBus.post(new CreateRoadEvent(road));
        /**
         * Calculate a new starting position based on where you placed your road
         */
        switch (slope) {
            case FLAT:
                startingPosition=new MapPosition(constructedPosition);
                startingPosition.setOffSetY(startingPosition.getOffSetY()-0.1f);
                break;

            case UP:
                startingPosition=new MapPosition(constructedPosition);
                startingPosition.setOffSetY(startingPosition.getOffSetY()-0.1f);
                startingPosition.setY(startingPosition.getY()+1);
                break;

            case DOWN:
                startingPosition=new MapPosition(constructedPosition);
                startingPosition.setOffSetY(startingPosition.getOffSetY()-0.1f);
                startingPosition.setY(startingPosition.getY()-1);
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
    }

    public void startingPosition(Vector3f pos) {
        Vector3f vector= UtilityMethods.roundVector(pos);
        startingPosition =new MapPosition(vector);
        status = RoadMakerStatus.BUILDING;
    }
    @Subscribe
    public void listenUpdateRoadEvent(UpdateRoadEvent event){
        int id = event.getExistingRoad().getID();
        Spatial oldRoad = null;
        for(Spatial s:roadSpatials){
            if(s.getUserData("ID")==id){
                oldRoad=s;
                break;
            }
        }
        if(oldRoad==null){
            /* FAILED CANT FIND ROAD WITH THAT ID   */
        }else{
            /* Delete old road */
        
            roadNode.detachChild(oldRoad);
        }
        /* Get new road */
        Spatial newRoad=roadF.roadToSpatial(event.getExistingRoad(),event.getConnected());
        /* Add new road  */
        roadNode.attachChild(newRoad);
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

    public void setStatus(RoadMakerStatus status) {
        this.status = status;
    }

    public RoadHill getHill() {
        return slope;
    }

    public void setHill(RoadHill hill) {
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
    
}
