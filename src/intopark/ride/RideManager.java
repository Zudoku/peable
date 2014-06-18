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
import intopark.inputhandler.MouseContainer;
import intopark.inputhandler.NeedMouse;
import intopark.shops.BasicBuildables;
import intopark.shops.HolomodelDrawer;
import intopark.terrain.MapContainer;
import intopark.util.Direction;
import intopark.terrain.ParkHandler;
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

    public void buy(Direction direction, BasicBuildables selectedBuilding) {
        MapPosition loc = new MapPosition(UtilityMethods.roundVector(holoDrawer.getLocation()));
        int ID = identifier.reserveID();
        CreateRideEvent event=new CreateRideEvent(loc,selectedBuilding,direction,"",ID,-1,-1,-1,true,20, null,null);
        eventBus.post(event);
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
                logger.log(Level.FINER, "Can't place enterance here, {0} is blocking it.",identifier.getObjectWithID(foundID).toString());
                return;
            }
        }
        for(int suunta = 0; suunta < 4 ;suunta++){
            Direction direction= Direction.intToDirection(suunta);
            MapPosition offset = direction.directiontoPosition();
            int[]coords = new int[]{(x+offset.getX()),y,(z+offset.getZ())};
            int code=mapContainer.checkIfBlocking(coords);
            if(code > 0){
                Object foundObject = identifier.getObjectWithID(code);
                if(foundObject instanceof BasicRide){
                    BasicRide foundRide= (BasicRide) foundObject;
                    boolean built = TryToBuildEnterance(enterancetype, (x+offset.getX()), y, (z+offset.getZ()), direction); //might be direction.getOpposite()
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
    private boolean TryToBuildEnterance(boolean enterancetype, int x, int y, int z, Direction suunta) {
        if(!parkHandler.testForEmptyTile(x,y,z)){
            logger.log(Level.FINE, "Enterance build cancelled because {0} {1} {2} was not empty", new Object[]{x, y, z});

        }
        return false;
        //TODO: test if ride needs enterance...

        //Enterance e = new Enterance(enterancetype, new Vector3f(x, y, z), suunta, assetManager);
        //e.connectedRide = rides.get(rideID - 2);
        //e.object.setUserData("type", "enterance");
        //e.object.setUserData("rideID", e.connectedRide.getRideID());
//
//        eventBus.post(new AddObjectToMapEvent(x, y, z, e.object));
//        if (enterancetype == false) {
//            rides.get(rideID - 2).enterance = e;
//        } else {
//            rides.get(rideID - 2).exit = e;
//        }
//
//        rideNode.attachChild(e.object);
//        enterancecount++;
//        if (enterancecount > 1) {
//            enterancecount = 0;
//            eventBus.post(new SetClickModeEvent(ClickingModes.NOTHING));
//        }
    }
    public void buildEnterance(){

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
