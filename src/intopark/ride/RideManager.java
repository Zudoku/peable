/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package intopark.ride;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import com.google.inject.Singleton;
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
    //VARIABLES
    public List<BasicRide> rides = new ArrayList<>();
    private Node rideNode;
    private int enterancecount = 0;
    

    

    @Inject
    public RideManager(Node rootNode, HolomodelDrawer holoDrawer, ParkHandler parkHandler,
        EventBus eventBus,Identifier identifier) {
        this.rideFactory = new RideFactory();
        this.holoDrawer = holoDrawer;
        this.parkHandler = parkHandler;
        this.eventBus = eventBus;
        this.identifier=identifier;
        eventBus.register(this);
        rideNode = new Node("rideNode");
        rootNode.attachChild(rideNode);
        //TODO: MAKE ALL ID'S UNIQUE ACROSS EVERYTHING.
    }

    public void buy(Direction direction, BasicBuildables selectedBuilding) {
        MapPosition loc = new MapPosition(UtilityMethods.roundVector(holoDrawer.getLocation()));

        
        int ID = identifier.reserveID();
        CreateRideEvent event=new CreateRideEvent(loc,selectedBuilding , direction," ",ID,0,65,20,true,20, null,null);

        resetRidedata();
    }

    private void resetRidedata() {
        eventBus.post(new SetClickModeEvent(ClickingModes.RIDE));
    }



    public void placeEnterance(Vector3f pos) {
        //TODO: test meaby roundvector??
        int x = (int) (pos.x - 0.4999f + 1);
        int y = (int) (pos.y - 0.4999f + 1);
        int z = (int) (pos.z - 0.4999f + 1);


        boolean enterancetype = true;
        if (enterancecount == 0) {
            enterancetype = false;
        }
        
        if (!parkHandler.testForEmptyTile(x, y, z)) {
            return;
        }
        if (!parkHandler.testForEmptyTile(x+1, y, z)) {
            Spatial s = parkHandler.getSpatialAt(x+1, y, z);
            if (s.getUserData("ID")==null) {
                return;
            }
            int ID = s.getUserData("ID");
            if(identifier.getObjectWithID(ID) instanceof BasicRide){
                placeEnterancetrue(enterancetype, x, y, z, Direction.SOUTH);
            }

        }
        if (!parkHandler.testForEmptyTile(x-1, y, z)) {
            Spatial s =parkHandler.getSpatialAt(x-1, y, z);
            if (s.getUserData("ID")==null) {
                return;
            }
            int ID = s.getUserData("ID");
            if(identifier.getObjectWithID(ID) instanceof BasicRide){
                placeEnterancetrue(enterancetype, x, y, z, Direction.NORTH);
                return;
            }
        }
        if (!parkHandler.testForEmptyTile(x, y, z+1)) {
            Spatial s = parkHandler.getSpatialAt(x, y, z+1);
            if (s.getUserData("ID")==null) {
                return;
            }
            int ID = s.getUserData("ID");
            if(identifier.getObjectWithID(ID) instanceof BasicRide){
                placeEnterancetrue(enterancetype, x, y, z, Direction.EAST);
                return;
            }
        }
        if (!parkHandler.testForEmptyTile(x, y, z-1)) {
            Spatial s =parkHandler.getSpatialAt(x, y, z-1);
            if (s.getUserData("ID")==null) {
                return;
            }
            int ID = s.getUserData("ID");
            if(identifier.getObjectWithID(ID) instanceof BasicRide){
                placeEnterancetrue(enterancetype, x, y, z, Direction.WEST);
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

    private void placeEnterancetrue(boolean enterancetype, int x, int y, int z, Direction suunta) {
        if(!parkHandler.testForEmptyTile(x,y,z)){
            logger.log(Level.FINE, "Enterance build cancelled because {0} {1} {2} was not empty", new Object[]{x, y, z});
            return;
        }
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

    @Override
    public void onClick(MouseContainer container) {
        placeEnterance(container.getResults().getClosestCollision().getContactPoint());
        Gamestate.ingameHUD.updateClickingIndicator();
    }

    @Override
    public void onDrag(MouseContainer container) {
        
    }

    @Override
    public void onDragRelease(MouseContainer container) {
        
    }
}
