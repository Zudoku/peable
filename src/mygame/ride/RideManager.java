/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.ride;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.jme3.asset.AssetManager;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import java.util.ArrayList;
import mygame.Gamestate;
import mygame.Main;
import mygame.inputhandler.ClickingHandler;
import mygame.inputhandler.ClickingModes;
import mygame.shops.BasicBuildables;
import mygame.shops.HolomodelDrawer;
import mygame.shops.ShopManager;
import mygame.terrain.Direction;
import mygame.terrain.MapContainer;
import mygame.terrain.ParkHandler;
import mygame.terrain.RoadMaker;

/**
 *
 * @author arska
 */
@Singleton
public class RideManager {

    public ArrayList<BasicRide> rides = new ArrayList<BasicRide>();
    RideFactory rideFactory;
    private AssetManager assetManager;
    public Node rideNode;
    public Node rootNode;
    int rideID;
    int enterancecount = 0;
    private final HolomodelDrawer holoDrawer;
    private final ParkHandler parkHandler;
    private final RoadMaker roadMaker;
    private final ClickingHandler clickingHandler;
    private final MapContainer map;
    
    @Inject
    public RideManager(AssetManager assetManager, Node rootNode,RideFactory rideFactory,HolomodelDrawer holoDrawer,ParkHandler parkHandler,
    RoadMaker roadMaker,ClickingHandler clickingHandler,MapContainer map) {
        this.rideFactory =rideFactory;
        this.assetManager = assetManager;
        this.rootNode = rootNode;
        this.holoDrawer=holoDrawer;
        this.parkHandler=parkHandler;
        this.roadMaker=roadMaker;
        this.clickingHandler=clickingHandler;
        this.map=map;
        
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

            case NULL:
                System.out.println("You just tried to buy null ride!");
                break;
        }
        if(!parkHandler.getParkWallet().canAfford(boughtride.constructionmoney)){
           return; 
        }
        int tx = (int) loc.x;
        int ty = (int) loc.y;
        int tz = (int) loc.z;
        
        boughtride.setRideID(rideID);
        boughtride.getGeometry().setUserData("rideID", rideID);
        boughtride.getGeometry().setUserData("type", "ride");
        rides.add(boughtride);
        rideNode.attachChild(boughtride.getGeometry());
        parkHandler.getParkWallet().remove(boughtride.constructionmoney);
        Gamestate.ingameHUD.updateMoneytextbar();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                map.getMap()[tx + i - 1][ty][tz + j - 1] = boughtride.getGeometry();
            }
        }
        rideID++;
        resetRidedata();
    }

    private void resetRidedata() {

        
        clickingHandler.clickMode = ClickingModes.RIDE;

    }

    @Deprecated
    public BasicRide isthereRide(int x, int y, int z) {
        BasicRide b = null;
        if (rides.isEmpty() == false) {
            for (BasicRide p : rides) {
                int tx = (int) holoDrawer.pyorista(p.getPosition()).x;
                int ty = (int) holoDrawer.pyorista(p.getPosition()).y;
                int tz = (int) holoDrawer.pyorista(p.getPosition()).z;
                if (tx == x && ty == y && tz == z) {
                    b = p;
                    System.out.println("RIDE IS LOCATED!");
                    return b;

                }
            }
        }
        return b;
    }

    public void placeEnterance(Vector3f pos) {

        int x = (int) (pos.x - 0.4999f + 1);
        int y = (int) (pos.y - 0.4999f + 1);
        int z = (int) (pos.z - 0.4999f + 1);


        boolean enterancetype = true;
        if (enterancecount == 0) {
            enterancetype = false;
        }

        if (map.getMap()[x][y][z] != null) {
            return;
        }
        if (map.getMap()[x + 1][y][z] != null) {
            Spatial s = map.getMap()[x + 1][y][z];
            if (!s.getUserData("type").equals("ride")) {
                return;
            }
            int rideidArvo = s.getUserData("rideID");
            if (rideidArvo == rideID - 1) {
                placeEnterancetrue(enterancetype, x, y, z,Direction.DOWN);
                return;
            }
        }
        if (map.getMap()[x - 1][y][z] != null) {
            Spatial s = map.getMap()[x - 1][y][z];
            if (!s.getUserData("type").equals("ride")) {
                return;
            }
            int rideidArvo = s.getUserData("rideID");
            if (rideidArvo == rideID - 1) {
                placeEnterancetrue(enterancetype, x, y, z,Direction.UP);
                return;
            }
        }
        if (map.getMap()[x][y][z + 1] != null) {
            Spatial s = map.getMap()[x][y][z + 1];
            if (!s.getUserData("type").equals("ride")) {
                return;
            }
            int rideidArvo = s.getUserData("rideID");
            if (rideidArvo == rideID - 1) {
                placeEnterancetrue(enterancetype, x, y, z,Direction.RIGHT);
                return;
            }
        }
        if (map.getMap()[x][y][z - 1] != null) {
            Spatial s = map.getMap()[x][y][z - 1];
            if (!s.getUserData("type").equals("ride")) {
                return;
            }
            int rideidArvo = s.getUserData("rideID");
            if (rideidArvo == rideID - 1) {
                placeEnterancetrue(enterancetype, x, y, z,Direction.LEFT);
            }
        }

    }

    public void updateRideQueues() {
        for (BasicRide ride : rides) {
            ride.updateQueLine();
            ride.updateRide();
        }
    }

    private void placeEnterancetrue(boolean enterancetype, int x, int y, int z,Direction suunta) {
        Enterance e = new Enterance(enterancetype, new Vector3f(x, y, z), suunta, assetManager);
        e.connectedRide = rides.get(rideID - 2);
        e.object.setUserData("type", "enterance");
        map.getMap()[x][y][z] = e.object;
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
    public void setRideID(int rideID){
        this.rideID=rideID;
    }
}
