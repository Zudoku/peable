/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.ride;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.jme3.asset.AssetManager;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import mygame.GUI.UpdateMoneyTextBarEvent;
import mygame.inputhandler.ClickingHandler;
import mygame.inputhandler.ClickingModes;
import mygame.shops.BasicBuildables;
import mygame.shops.HolomodelDrawer;
import mygame.terrain.events.AddObjectToMapEvent;
import mygame.terrain.Direction;
import mygame.terrain.ParkHandler;

/**
 *
 * @author arska
 */
@Singleton
public class RideManager {
    private static final Logger logger = Logger.getLogger(RideManager.class.getName());
    public ArrayList<BasicRide> rides = new ArrayList<BasicRide>();
    RideFactory rideFactory;
    public Node rideNode;
    public Node rootNode;
    int rideID;
    int enterancecount = 0;
    private final HolomodelDrawer holoDrawer;
    private final ParkHandler parkHandler;
    
    private final ClickingHandler clickingHandler;
    
    private final EventBus eventBus;
    private final AssetManager assetManager;

    @Inject
    public RideManager(AssetManager assetManager, Node rootNode, HolomodelDrawer holoDrawer, ParkHandler parkHandler,
                ClickingHandler clickingHandler, EventBus eventBus) {
        this.rideFactory = new RideFactory(assetManager,rootNode);
        this.assetManager = assetManager;
        this.rootNode = rootNode;
        this.holoDrawer = holoDrawer;
        this.parkHandler = parkHandler;
        this.clickingHandler = clickingHandler;
        this.eventBus = eventBus;

        rideNode = new Node("rideNode");
        rootNode.attachChild(rideNode);
    }

    public void buy(Direction facing, BasicBuildables selectedBuilding) {
        Vector3f loc = holoDrawer.pyorista(holoDrawer.getLocation());

        BasicRide boughtride = null;



        switch (selectedBuilding) {
            case CHESSCENTER:
                boughtride = rideFactory.chessCenter(loc, facing);

                break;

            case ARCHERYRANGE:
                boughtride=rideFactory.archeryRange(loc, facing);
                break;

            case BLENDER:
                boughtride=rideFactory.blender(loc, facing);
                break;

            case HAUNTEDHOUSE:
                boughtride=rideFactory.hauntedHouse(loc, facing);
                break;

            case PIRATESHIP:
                boughtride=rideFactory.pirateShip(loc, facing);
                break;

            case ROTOR:
                boughtride=rideFactory.rotor(loc, facing);
                break;

            case NULL:
                logger.log(Level.WARNING,"Could not identify your ride!");
                break;
        }
        if (!parkHandler.getParkWallet().canAfford(boughtride.constructionmoney)) {
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
        parkHandler.getParkWallet().remove(boughtride.constructionmoney);
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


        clickingHandler.clickMode = ClickingModes.RIDE;

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
                placeEnterancetrue(enterancetype, x, y, z, Direction.DOWN);
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
                placeEnterancetrue(enterancetype, x, y, z, Direction.UP);
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
                placeEnterancetrue(enterancetype, x, y, z, Direction.RIGHT);
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
                placeEnterancetrue(enterancetype, x, y, z, Direction.LEFT);
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
        Enterance e = new Enterance(enterancetype, new Vector3f(x, y, z), suunta, assetManager);
        e.connectedRide = rides.get(rideID - 2);
        e.object.setUserData("type", "enterance");
        e.object.setUserData("rideID", e.connectedRide.getRideID());
        
        eventBus.post(new AddObjectToMapEvent(x, y, z, e.object));
        if (enterancetype == false) {
            rides.get(rideID - 2).enterance = e;
        } else {
            rides.get(rideID - 2).exit = e;
        }

        rideNode.attachChild(e.object);
        enterancecount++;
        if (enterancecount > 1) {
            enterancecount = 0;
            clickingHandler.clickMode = ClickingModes.NOTHING;
        }
    }

    public void setRideID(int rideID) {
        this.rideID = rideID;
    }
}
