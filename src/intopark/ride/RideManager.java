/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package intopark.ride;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.jme3.light.Light;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import intopark.Gamestate;
import intopark.UtilityMethods;
import intopark.inout.Identifier;
import intopark.inputhandler.ClickingModes;
import intopark.inputhandler.MouseContainer;
import intopark.inputhandler.NeedMouse;
import intopark.inputhandler.SetClickModeEvent;
import intopark.shops.BasicBuildables;
import intopark.shops.HolomodelDrawer;
import intopark.terrain.MapContainer;
import intopark.util.Direction;
import intopark.terrain.ParkHandler;
import intopark.terrain.events.AddToBlockingMapEvent;
import intopark.util.MapPosition;

/**
 *
 * @author arska
 */
@Singleton
public class RideManager implements NeedMouse{

    private static final Logger logger = Logger.getLogger(RideManager.class.getName());
    //DEPENDENCIES
    private RideFactory rideFactory;
    private Identifier identifier;
    private final EventBus eventBus;
    private final HolomodelDrawer holoDrawer;
    private final ParkHandler parkHandler;
    private MapContainer mapContainer;
    //VARIABLES
    public List<BasicRide> rides = new ArrayList<>();
    private Node rideNode;
    private int enterancecount = 0;

    @Inject
    public RideManager(Node rootNode, HolomodelDrawer holoDrawer, ParkHandler parkHandler,
        EventBus eventBus,Identifier identifier,MapContainer mapContainer) {
        this.rideFactory = new RideFactory();
        this.holoDrawer = holoDrawer;
        this.parkHandler = parkHandler;
        this.eventBus = eventBus;
        this.identifier=identifier;
        this.mapContainer=mapContainer;
        eventBus.register(this);
        rideNode = new Node("rideNode");
        rootNode.attachChild(rideNode);
    }

    public void buildNewRide(Direction direction, BasicBuildables selectedBuilding) {
        MapPosition position = new MapPosition(UtilityMethods.roundVector(holoDrawer.getLocation()));
        int ID = identifier.reserveID();
        CreateRideEvent event=new CreateRideEvent(position,selectedBuilding,direction,ID);
        eventBus.post(event);
        enterancecount=0;
    }
    public void UserClickOnEnteranceMode(Vector3f pos) {
        Vector3f roundVector=UtilityMethods.roundVector(pos);
        MapPosition mapPosition =new MapPosition(roundVector);
        int x=mapPosition.getX();
        int y=mapPosition.getY();
        int z=mapPosition.getZ();

        boolean enterancetype = true;
        if (enterancecount == 0) {
            enterancetype = false;
        }
        int foundID=mapContainer.checkIfBlocking(new int[]{x,y,z});
        if(foundID>-2){ //Blocks
            if(foundID==-1){
                logger.log(Level.FINER, "Can't place enterance here, Something is blocking it.");
                return;
            }else{
                logger.log(Level.FINER, "Can't place enterance here, {0} is blocking it.","a");
                return;
            }
        }
        //Check for each direction if there is a ride it can attach to.
        for(int suunta = 0; suunta < 4 ;suunta++){
            Direction direction= Direction.intToDirection(suunta);
            MapPosition offset = direction.directiontoPosition();
            int[]coords = new int[]{(x+offset.getX()),y,(z+offset.getZ())};
            int code=mapContainer.checkIfBlocking(coords);
            if(code > 0){
                Object foundObject = identifier.getObjectWithID(code);
                if(foundObject instanceof BasicRide){
                    BasicRide foundRide= (BasicRide) foundObject;
                    boolean built = TryToBuildEnterance(enterancetype, x, y, z, direction,foundRide); //might be direction.getOpposite()
                    if(built){
                        return;
                    }
                }
            }
        }

    }
    public void updateRide() {
        for (BasicRide ride : rides) {
            ride.runAnim();
            ride.updateQueLine();
            ride.updateRide();
            ride.update();
        }
    }
    private boolean TryToBuildEnterance(boolean exit, int x, int y, int z, Direction suunta,BasicRide ride) {
        //Check if the block is occupied.
        if(mapContainer.checkIfBlocking(new int[]{x,y,z})!=-2){
            logger.log(Level.FINER, "Can't build enterance because there is something on the way.");
            return false;
        }
        //Check if ride already has enterance.
        if(exit){
            if(ride.getExit()!=null){
                logger.log(Level.FINER, "Ride {0} already has an exit.",ride.toString());
                return false;
            }
        }else{
            if(ride.getEnterance()!= null){
                logger.log(Level.FINER, "Ride {0} already has an enterance");
                return false;
            }
        }
        buildEnterance(exit, new MapPosition(x, y, z), suunta, ride);
        return true;
    }
    public void buildEnterance(boolean exit,MapPosition position,Direction direction,BasicRide connectedRide){
        int ID = identifier.reserveID();
        Enterance enterance = new Enterance(exit,position,direction,ID,eventBus);
        enterance.setConnectedRide(connectedRide);
        if(exit){
            connectedRide.setExit(enterance);
        }else{
            connectedRide.setEnterance(enterance);
        }
        attachToRideNode(enterance.getObject());
        identifier.addOldObject(enterance.getID(),ID);
        AddToBlockingMapEvent event = new AddToBlockingMapEvent(ID, new int[]{position.getX(),position.getY(),position.getZ()});
        eventBus.post(event);
        enterancecount++;
        if(enterancecount>=2){
            enterancecount=0;
            SetClickModeEvent clickmodeEvent= new SetClickModeEvent(ClickingModes.NOTHING);
            eventBus.post(clickmodeEvent);
        }
    }
    public void attachToRideNode(Object object){
        if(object instanceof Spatial){
            logger.log(Level.FINEST,"New Spatial added to rideNode");
            rideNode.attachChild((Spatial)object);
        }
        if(object instanceof Light){
            logger.log(Level.FINEST,"New Light added to rideNode");
            rideNode.addLight((Light)object);
        }
        if(object instanceof BasicRide){
            logger.log(Level.FINEST,"New Ride added to rideNode");
            BasicRide ride=(BasicRide)object;
            for(Spatial spatial:ride.getAllSpatialsFromRide(false)){
                rideNode.attachChild(spatial);
            }
        }
    }
    /**
     * This is a temporary fix so that rides reserve space. It always reserves 3x3 area around the ride. TODO: REMAKE
     * @param ride
     */
    public void reserveSpace(BasicRide ride){
        int ID = ride.getID();
        MapPosition position = ride.getPosition();
        for(int x=-1;x<2;x++){
            for(int z=-1;z<2;z++){
                int x1=position.getX()+x;
                int y1=position.getY();
                int z1=position.getZ()+z;
                int[] coords=new int[]{x1,y1,z1};
                AddToBlockingMapEvent event=new AddToBlockingMapEvent(ID, coords);
                eventBus.post(event);
            }
        }
    }

    @Override
    public void onClick(MouseContainer container) {
        UserClickOnEnteranceMode(container.getResults().getClosestCollision().getContactPoint());
        Gamestate.ingameHUD.updateClickingIndicator();
    }

    @Override
    public void onDrag(MouseContainer container) {

    }

    @Override
    public void onDragRelease(MouseContainer container) {

    }
}
