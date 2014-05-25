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
import intopark.GUI.events.UpdateMoneyTextBarEvent;
import intopark.Gamestate;
import intopark.UtilityMethods;
import intopark.inputhandler.ClickingModes;
import intopark.inputhandler.MouseContainer;
import intopark.inputhandler.NeedMouse;
import intopark.inputhandler.SetClickModeEvent;
import intopark.shops.BasicBuildables;
import intopark.shops.HolomodelDrawer;
import intopark.terrain.events.AddObjectToMapEvent;
import intopark.util.Direction;
import intopark.terrain.ParkHandler;

/**
 *
 * @author arska
 */
@Singleton
public class RideManager implements NeedMouse{
    
    private static final Logger logger = Logger.getLogger(RideManager.class.getName());
    //DEPENDENCIES
    private RideFactory rideFactory;
    //VARIABLES
    public List<BasicRide> rides = new ArrayList<>();
    private Node rideNode;
    private int rideID;
    private int enterancecount = 0;
    private final HolomodelDrawer holoDrawer;
    private final ParkHandler parkHandler;

    private final EventBus eventBus;

    @Inject
    public RideManager(Node rootNode, HolomodelDrawer holoDrawer, ParkHandler parkHandler,
        EventBus eventBus) {
        this.rideFactory = new RideFactory();
        this.holoDrawer = holoDrawer;
        this.parkHandler = parkHandler;
        this.eventBus = eventBus;
        eventBus.register(this);
        rideNode = new Node("rideNode");
        rootNode.attachChild(rideNode);
        //TODO: MAKE ALL ID'S UNIQUE ACROSS EVERYTHING.
    }

    public void buy(Direction direction, BasicBuildables selectedBuilding) {
        Vector3f loc = UtilityMethods.roundVector(holoDrawer.getLocation());

        BasicRide boughtride = null;



        switch (selectedBuilding) {
            case CHESSCENTER:
                boughtride = rideFactory.chessCenter(loc, direction);

                break;

            case ARCHERYRANGE:
                boughtride=rideFactory.archeryRange(loc, direction);
                break;

            case BLENDER:
                boughtride=rideFactory.blender(loc, direction);
                break;

            case HAUNTEDHOUSE:
                boughtride=rideFactory.hauntedHouse(loc, direction);
                break;

            case PIRATESHIP:
                boughtride=rideFactory.pirateShip(loc, direction);
                break;

            case ROTOR:
                boughtride=rideFactory.rotor(loc, direction);
                break;

            case NULL:
                logger.log(Level.WARNING,"Could not identify your ride!");
                break;
        }
        if (!parkHandler.getParkWallet().canAfford(boughtride.getConstructionmoney())) {
            return;
        }
        int tx = (int) loc.x;
        int ty = (int) loc.y;
        int tz = (int) loc.z;

        boughtride.setRideID(rideID);
        boughtride.setAllSpatialsUserData("rideID",rideID);
        boughtride.setAllSpatialsUserData("type", "ride");
        rides.add(boughtride);
        boughtride.attachToNode(rideNode);
        parkHandler.getParkWallet().remove(boughtride.getConstructionmoney());
        eventBus.post(new UpdateMoneyTextBarEvent());
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                
                eventBus.post(new AddObjectToMapEvent(tx+i-1, ty, tz+j-1, boughtride.getGeometry()));
                logger.log(Level.WARNING, "{0} {1} {2}",new Object[]{tx+i-1,ty,tz+j-1});
            }
        }
        rideID++;
        resetRidedata();
    }

    private void resetRidedata() {
        eventBus.post(new SetClickModeEvent(ClickingModes.RIDE));
    }



    public void placeEnterance(Vector3f pos) {

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
            if (s.getUserData("rideID")==null) {
                return;
            }
            int rideidArvo = s.getUserData("rideID");
            if (rideidArvo == rideID - 1) {
                placeEnterancetrue(enterancetype, x, y, z, Direction.SOUTH);
                return;
            }
        }
        if (!parkHandler.testForEmptyTile(x-1, y, z)) {
            Spatial s =parkHandler.getSpatialAt(x-1, y, z);
            if (s.getUserData("rideID")==null) {
                return;
            }
            int rideidArvo = s.getUserData("rideID");
            if (rideidArvo == rideID - 1) {
                placeEnterancetrue(enterancetype, x, y, z, Direction.NORTH);
                return;
            }
        }
        if (!parkHandler.testForEmptyTile(x, y, z+1)) {
            Spatial s = parkHandler.getSpatialAt(x, y, z+1);
            if (s.getUserData("rideID")==null) {
                return;
            }
            int rideidArvo = s.getUserData("rideID");
            if (rideidArvo == rideID - 1) {
                placeEnterancetrue(enterancetype, x, y, z, Direction.EAST);
                return;
            }
        }
        if (!parkHandler.testForEmptyTile(x, y, z-1)) {
            Spatial s =parkHandler.getSpatialAt(x, y, z-1);
            if (s.getUserData("rideID")==null) {
                return;
            }
            int rideidArvo = s.getUserData("rideID");
            if (rideidArvo == rideID - 1) {
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

    public void setRideID(int rideID) {
        this.rideID = rideID;
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
