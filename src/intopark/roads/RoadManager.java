/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package intopark.roads;

import com.google.common.collect.ImmutableMap;
import intopark.roads.events.CreateRoadEvent;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.jme3.collision.CollisionResult;
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
import intopark.inout.Identifier;
import intopark.input.mouse.MouseContainer;
import intopark.input.mouse.NeedMouse;
import intopark.roads.events.CreateBuildingEnteranceEvent;
import intopark.roads.events.DeleteRoadEvent;
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
public class RoadManager implements NeedMouse{
    private static final Logger logger = Logger.getLogger(RoadManager.class.getName());
    //DEPENDENCIES
    @Inject private RoadGraph roadGraph;
    private Node roadNode;
    @Inject public RoadFactory roadF;
    private MapContainer map;
    @Inject private ParkHandler parkHandler;
    private final EventBus eventBus;
    private Identifier identifier;
    //OWNS
    private List<Spatial> roadSpatials = new ArrayList<>();
    private List<Road> walkablesThatNeedToBePaid = new ArrayList<>();
    private List<Integer> roadIDsToBeDeleted = new ArrayList<>();
    //VARIABLES
    private Direction direction = Direction.NORTH;
    private int slope = 0; //0= flat 1=up 2=down
    private RoadManagerStatus status = RoadManagerStatus.MANUAL;
    private MapPosition startingPosition;
    private MapPosition endingPosition;
    private boolean queroad = false;
    private boolean draggingMode = true;
    private MapPosition lastDragPosition;

    private static int MAX_AUTOMATIC_ROAD_BUILD_CONTER = 1000;

    /**
     * This Class is the Main control which does everything regarding to roadSpatials including
     * loading them and calculating where to move them
     * @param assetManager This is needed to load the road Spatial
     * @param rootNode This is used to attach the roadSpatials to the world
     * @param map This is used to access the space where the roadSpatials are going to be
     * @param eventBus This is used to send events to other components
     */
    @Inject
    public RoadManager(Node rootNode,EventBus eventBus,Identifier identifier) {
        roadNode=new Node("roadNode");
        rootNode.attachChild(roadNode);
        this.eventBus = eventBus;
        eventBus.register(this);
        this.identifier=identifier;
        roadF = new RoadFactory();
    }
    public void update(){
        roadGraph.update();
        tryToDeleteRoads();
    }
    /**
     * Tries to delete road Spatials that are marked to being deleted from roadNode.
     */
    private void tryToDeleteRoads(){
        List<Integer> succesfullyDeleted = new ArrayList<>();
        for(int id : roadIDsToBeDeleted){
            Spatial oldRoad = UtilityMethods.findSpatialWithID(roadSpatials, id);
            if(oldRoad != null){
                roadNode.detachChild(oldRoad);
                if(!roadNode.hasChild(oldRoad)){
                    roadSpatials.remove(oldRoad);
                    succesfullyDeleted.add((Integer)id);
                }
            }else{
                logger.log(Level.WARNING, "Spatial with ID {0} couldnt be found in roadSpatials",id);
            }
        }
        for(int id:succesfullyDeleted){
            roadIDsToBeDeleted.remove((Integer)id);
        }
    }
    /**
     * This should be called after automatic Road building.
     * If draggingMode is false, will reset variables that store the MapPositions from start to finish
     * @param valueToReturn Pass a boolean to return. Useful for one-liners.
     * @return valueToReturn
     */
    private boolean automaticRoadBuildCleanup(boolean valueToReturn){
        if (!draggingMode) {
            startingPosition = null;
            endingPosition = null;
            lastDragPosition = null;
        }
        return valueToReturn;
    }

    public void buildAutomaticRoadWrapper(MapPosition sourcePosition,MapPosition destinationPosition){
        buildAutomaticRoad(sourcePosition, destinationPosition);
    }

    /**
     * Builds Road starting from the sourcePosition to destinationPosition.
     * Road will be built on sourcePosition always. Source can be the same as destination.
     * Does a cleanup after every call.
     * @param sourcePosition Where the road starts.
     * @param destinationPosition Where the road ends.
     * @return true if roads were built correctly. False if error was caught.
     */
    public boolean buildAutomaticRoad(MapPosition sourcePosition,MapPosition destinationPosition){
        //Make copies so we dont modify the original
        MapPosition sourcePos = new MapPosition(sourcePosition);
        MapPosition destinationPos = new MapPosition(destinationPosition);
        if(sourcePosition==null||destinationPosition==null){
            logger.log(Level.WARNING, "SourcePosition or destinationPosition is null. Exiting buildAutomaticRoad.");
            return automaticRoadBuildCleanup(false);
        }

        //Build a road at startingposition
        direction= Direction.NORTH;
        slope=0;
        sourcePos.setX(sourcePos.getX()-1);
        sourcePos = manualBuildRoad(sourcePos,false);
        if(sourcePos.isSameMainCoords(destinationPos)){
            logger.log(Level.FINE, "Road tool was used to build a single piece of road. Skipping the automatic road-building loop.");
            return automaticRoadBuildCleanup(true);
        }
        boolean build=true;
        int roadsBuiltCounter=0;
        while(build){
            roadsBuiltCounter++;
            direction=destinationPosition.getDirection(sourcePos.getX(), sourcePos.getZ());
            if(sourcePos.getY()<destinationPos.getY()){
                slope=1;
            }else if(sourcePos.getY()>destinationPos.getY()){
                slope=2;
            }else{
                slope=0;
            }
            sourcePos = manualBuildRoad(sourcePos,false);
            if(sourcePos.isSameMainCoords(destinationPos)){
                build=false;
            }
            if(roadsBuiltCounter>MAX_AUTOMATIC_ROAD_BUILD_CONTER){
                build=false;
                logger.log(Level.WARNING,"Road tool looped {0} times. Something might have gone wrong, or you either built a too long road.",MAX_AUTOMATIC_ROAD_BUILD_CONTER);
            }
        }
        logger.log(Level.FINE, "Finished automatic road building. Built {0} roads.",roadsBuiltCounter);
        return automaticRoadBuildCleanup(true);

    }
    /**
     * This calculates next position for road. It calculates it based on direction.
     * @return Position where the next road is going to be
     */
    public MapPosition calcNextManualRoadPosition(MapPosition sourcePosition) {
        MapPosition roadPos = new MapPosition(sourcePosition);
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

    public void manualBuildRoadWrapper(MapPosition sourcePosition,boolean setPositionForward){
        draggingMode = false;
        removeUnpaidRoads();
        manualBuildRoad(sourcePosition, setPositionForward);
        updateManualRoadDragging();
    }

    /**
     * Builds one road forward based on the sourcePosition given and variables in this class.
     * @param sourcePosition Where the road will start.
     * @return the position of the built road. null if error was caught.
     */
    public MapPosition manualBuildRoad(MapPosition sourcePosition,boolean setPositionForward) {
        MapPosition sourcePos = new MapPosition(sourcePosition);

        if (status == RoadManagerStatus.CHOOSING) {
            logger.log(Level.WARNING,"Can't build while in choosing-mode.");
            return null;
        }

        MapPosition constructedPosition=new MapPosition(calcNextManualRoadPosition(sourcePos));
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
        int ID=identifier.reserveID();
        Road road = new Road(constructedPosition,angle,ID,skin,queroad,roadDir);
        eventBus.post(new CreateRoadEvent(road));
        /**
         * Calculate a new starting position based on where you placed your road
         */
        switch (slope) {
            case 0:
                sourcePos=new MapPosition(constructedPosition);
                sourcePos.setOffSetY(startingPosition.getOffSetY()-0.1f);
                break;

            case 1:
                sourcePos=new MapPosition(constructedPosition);
                sourcePos.setOffSetY(startingPosition.getOffSetY()-0.1f);
                sourcePos.setY(startingPosition.getY()+1);
                break;

            default:
                sourcePos=new MapPosition(constructedPosition);
                sourcePos.setOffSetY(startingPosition.getOffSetY()-0.1f);
                break;

        }
        if(setPositionForward){
            startingPosition = sourcePos;
        }
        return sourcePos;
    }
    /**
     * Called when road dragging tool changes position (in the preview of where the road is going to be built)
     * @param newDragPos new DestinationPosition for the preview tool.
     */
    private void dragRoadChangedPosition(MapPosition newDragPos){
        logger.log(Level.FINER, "Building unpaid roads from {1} to {0}",new Object[]{newDragPos.toString(),startingPosition});
        removeUnpaidRoads();
        logger.log(Level.FINER, "Unpaid old roads deleted.");
        buildAutomaticRoad(startingPosition,newDragPos);
        logger.log(Level.FINER, "New unpaid roads built.");
        lastDragPosition=newDragPos;
        logger.log(Level.FINER, "DraggingPosition has been changed has been changed to {0}",newDragPos.toString());
    }
    /**
     * Removes all the preview roads generated by the road dragging tool preview.
     */
    private void removeUnpaidRoads(){
        for(Road unpaidRoad : walkablesThatNeedToBePaid){
            roadGraph.deleteWalkable(unpaidRoad);
            identifier.removeObjectWithID(unpaidRoad.getID());
            roadIDsToBeDeleted.add(unpaidRoad.getID());
        }
        walkablesThatNeedToBePaid.clear();
        logger.log(Level.FINER, "Unpaid roads list cleared.");
    }


    /**
     * This method listens for CreateRoadEvents and hadles roads being created.
     * @param event
     */
    @Subscribe
    public void listenCreateRoadEvent(CreateRoadEvent event){
        int ROADPRICE=10;
        if(event.getRoad()==null){
            logger.log(Level.WARNING,"Can't create null road.");
            return;
        }
        if (!parkHandler.getParkWallet().canAfford(ROADPRICE) && !draggingMode) {
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
            identifier.addOldObject(event.getRoad().getID(),event.getRoad());
            if(draggingMode){
                walkablesThatNeedToBePaid.add(event.getRoad());
                logger.log(Level.FINEST,"Unpaid road added succesfully to {0}",event.getRoad().getVector3f());
                return;
            }
        }catch(IllegalArgumentException ex){
            logger.log(Level.SEVERE, "ILLEGAL ARGUMENTS: {0}",ex);
            return;
        }
        parkHandler.getParkWallet().remove(10);
        eventBus.post(new UpdateMoneyTextBarEvent());
        logger.log(Level.FINEST,"Road added succesfully to {0}",event.getRoad().getVector3f());
    }

    /**
     * This method listens for CreateBuildingEnteranceEvents and hadles building enterances being created.
     * @param event
     */
    @Subscribe
    public void listenCreateBuildingEnteranceEvent(CreateBuildingEnteranceEvent event){
        if(event.getEnterance()==null){
            return;
        }
        MapPosition pos=event.getEnterance().getPosition();
        if(!roadGraph.isThereRoom(pos)){
            logger.log(Level.FINEST,"Already Walkable on the way. Might not be able to connect.");
        }
        try{
            roadGraph.addWalkable(event.getEnterance());
        }catch(IllegalArgumentException ex){
            logger.log(Level.SEVERE, "ILLEGAL ARGUMENTS: {0}",ex);
            return;
        }
        logger.log(Level.FINEST,"BuildingEnterance added succesfully to {0}",event.getEnterance().getPosition().getVector());

    }
    /**
     * This method listens for UpdateRoadEvents and hadles roads being updated.
     * The road's spatial will get deleted and a new one will be added to match the updated state of the road.
     * @param event
     */
    @Subscribe
    public void listenUpdateRoadEvent(UpdateRoadEvent event){
        if (!event.isFirsTimeUpdated()) {
            eventBus.post(new DeleteRoadEvent(event.getExistingRoad(),true));
        }
        /* Get new road */
        Spatial newRoad=roadF.roadToSpatial(event.getExistingRoad(),event.getConnected());
        /* Add new road  */
        roadNode.attachChild(newRoad);
        roadSpatials.add(newRoad);
        event.getExistingRoad().setNeedsUpdate(false);
    }
    @Subscribe
    public void listenDeleteRoadEvent(DeleteRoadEvent event){
        int id = event.getRoad().getID();
        Spatial oldRoad = UtilityMethods.findSpatialWithID(roadSpatials, id);
        if (oldRoad == null) {
            /* FAILED CANT FIND ROAD WITH THAT ID   */
            logger.log(Level.WARNING, "Unable to find roadSpatial with ID {0}", id);
        } else {
            if(event.isDeleteNow()){
                /* Delete old road */
                roadNode.detachChild(oldRoad);//FROM GRAPH
                if (roadNode.hasChild(oldRoad)) {
                    logger.log(Level.FINEST, "FFFFFFFFFFFFF {0}", id);
                }
                roadSpatials.remove(oldRoad);//FROM LIST
            }else{
                roadIDsToBeDeleted.add(id);
            }


            logger.log(Level.FINEST, "Deleted old roadSpatial with ID {0}", id);
        }
    }

    /**
     * Listening on if the manual road tool should rotate
     * @param event
     */
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
    /**
     * Turn the manual road tool left.
     */
    public void turnLeft() {
        direction = direction.turnLeft();
        eventBus.post(new UpdateRoadDirectionEvent(direction));
        updateManualRoadDragging();
    }
    /**
     * Turn the manual road tool right.
     */
    public void turnRight() {
        direction = direction.turnRight();
        eventBus.post(new UpdateRoadDirectionEvent(direction));
        updateManualRoadDragging();
    }

    public RoadManagerStatus getStatus() {
        return status;
    }

    public RoadGraph getRoadGraph() {
        return roadGraph;
    }
    public void setStatus(RoadManagerStatus status) {
        this.status = status;
    }

    public int getHill() {
        return slope;
    }

    public void setHill(int hill) {
        this.slope = hill;
        updateManualRoadDragging();
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
        updateManualRoadDragging();
    }

    public boolean isQueroad() {
        return queroad;
    }

    public void setQueroad(boolean queroad) {
        this.queroad = queroad;
        updateManualRoadDragging();
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
        switch(status){
            case CHOOSING:
                if(container.isLeftClick()){
                    setStartingPosition(container.getResults().getClosestCollision().getContactPoint());
                    setStatus(RoadManagerStatus.MANUAL);
                    Gamestate.ingameHUD.updateClickingIndicator();
                    logger.log(Level.FINEST, "Updated Roads starting position");
                }else{

                }
                break;

            case AUTOMATIC:
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
                            draggingMode = true;
                        } else {
                            setEndingPosition(location);
                            draggingMode = false;
                            removeUnpaidRoads();
                            buildAutomaticRoad(startingPosition, endingPosition);
                        }
                    }
                } else {
                    if(draggingMode){
                        draggingMode = false;
                        automaticRoadBuildCleanup(true);
                        removeUnpaidRoads();
                    }else{
                        handleRoadRightClick(container);
                    }
                }
                break;

            case MANUAL:
                if(container.isLeftClick()){

                }else{
                    handleRoadRightClick(container);
                }
                break;
        }
    }
    private void handleRoadRightClick(MouseContainer container) {
        CollisionResult result = container.getResults().getClosestCollision();
        if (result != null) {
            Spatial resultRoad = tryToFindRoadSpatial(result.getGeometry());
            if (resultRoad != null) {
                int ID = (int) resultRoad.getUserData("ID");
                Road foundRoad = null;
                if (identifier.getObjectWithID(ID) instanceof Road) {
                    foundRoad = (Road) identifier.getObjectWithID(ID);
                }
                if (foundRoad == null) {
                    return;
                }
                roadGraph.deleteWalkable(foundRoad);
                identifier.removeObjectWithID(foundRoad.getID());
                eventBus.post(new DeleteRoadEvent(foundRoad, false));
            }

        }

    }
    //recursive function done with ifs because why not... X_X
    private Spatial tryToFindRoadSpatial(Spatial child){
        Spatial returnedSpatial = null;
        if(child.getUserData("type")!=null){
            if(child.getUserData("type").equals("road")){
                returnedSpatial = child;
                return returnedSpatial;
            }else {
                return returnedSpatial;
            }
        }else{
            Spatial parent = child.getParent();
            if (parent.getUserData("type") != null) {
                if (parent.getUserData("type").equals("road")) {
                    returnedSpatial = parent;
                    return returnedSpatial;
                } else {
                    return returnedSpatial;
                }
            } else {
                Spatial ancestor = parent.getParent();
                if (ancestor.getUserData("type") != null) {
                    if (ancestor.getUserData("type").equals("road")) {
                        returnedSpatial = ancestor;
                        return returnedSpatial;
                    } else {
                        return returnedSpatial;
                    }
                } else {
                    return returnedSpatial;
                }
            }
        }
    }

    public void updateManualRoadDragging(){
        if(status != RoadManagerStatus.MANUAL){
            return;
        }
        if(startingPosition == null){
            return;
        }
        draggingMode = true;
        removeUnpaidRoads();
        manualBuildRoad(startingPosition,false);
        draggingMode = false;
    }

    @Override
    public void onDrag(MouseContainer container) {

    }

    @Override
    public void onDragRelease(MouseContainer container) {

    }

    @Override
    public void onCursorHover(MouseContainer container) {
        if (status == RoadManagerStatus.AUTOMATIC && startingPosition != null && draggingMode){
            CollisionResult result = null;
            for (CollisionResult r : container.getResults()) {
                if (UtilityMethods.findUserDataType(r.getGeometry().getParent(), "Terrain")) {
                    result = r;
                    break;
                }
            }
            if (result != null) {
                MapPosition dragPos = new MapPosition(UtilityMethods.roundVector(result.getContactPoint()));
                if (lastDragPosition != null) {
                    if (!dragPos.isSameMainCoords(lastDragPosition)) {
                        dragRoadChangedPosition(dragPos);
                    }
                } else {
                    dragRoadChangedPosition(dragPos);
                }

            }
        }
    }
}
