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
import mygame.inputhandler.ClickingModes;
import mygame.shops.BasicBuildables;
import mygame.terrain.Direction;

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
    @Inject
    public RideManager(AssetManager assetManager, Node rootNode,RideFactory rideFactory) {
        this.rideFactory =rideFactory;
        this.assetManager = assetManager;
        this.rootNode = rootNode;
        rideNode = new Node("rideNode");
        rootNode.attachChild(rideNode);



    }

    public void buy(Direction facing, BasicBuildables selectedBuilding) {
        Vector3f loc = Main.gamestate.holoDrawer.pyorista(Main.gamestate.holoDrawer.getLocation());
        BasicRide boughtride = null;



        switch (selectedBuilding) {
            case CHESSCENTER:
                boughtride = rideFactory.chessCenter(loc, facing);

                break;

            case NULL:
                System.out.println("You just tried to buy null ride!");
                break;
        }
        if(!Main.currentPark.getParkWallet().canAfford(boughtride.constructionmoney)){
           return; 
        }
        int tx = (int) loc.x;
        int ty = (int) loc.y;
        int tz = (int) loc.z;
        Spatial[][][] map = Main.gamestate.roadMaker.map;
        boughtride.setRideID(rideID);
        boughtride.getGeometry().setUserData("rideID", rideID);
        boughtride.getGeometry().setUserData("type", "ride");
        rides.add(boughtride);
        rideNode.attachChild(boughtride.getGeometry());
        Main.currentPark.getParkWallet().remove(boughtride.constructionmoney);
        Gamestate.ingameHUD.updateMoneytextbar();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                map[tx + i - 1][ty][tz + j - 1] = boughtride.getGeometry();
            }
        }
        rideID++;
        resetRidedata();
    }

    public void resetRidedata() {

        Main.gamestate.shopManager.resetShopdata();
        Main.gamestate.clickingHandler.clickMode = ClickingModes.RIDE;

    }

    @Deprecated
    public BasicRide isthereRide(int x, int y, int z) {
        BasicRide b = null;
        if (rides.isEmpty() == false) {
            for (BasicRide p : rides) {
                int tx = (int) Main.gamestate.holoDrawer.pyorista(p.getPosition()).x;
                int ty = (int) Main.gamestate.holoDrawer.pyorista(p.getPosition()).y;
                int tz = (int) Main.gamestate.holoDrawer.pyorista(p.getPosition()).z;
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

        Spatial[][][] map = Main.gamestate.roadMaker.map;

        boolean enterancetype = true;
        if (enterancecount == 0) {
            enterancetype = false;
        }

        if (map[x][y][z] != null) {
            return;
        }
        if (map[x + 1][y][z] != null) {
            Spatial s = map[x + 1][y][z];
            if (!s.getUserData("type").equals("ride")) {
                return;
            }
            int rideidArvo = s.getUserData("rideID");
            if (rideidArvo == rideID - 1) {
                placeEnterancetrue(enterancetype, x, y, z, map,Direction.DOWN);
                return;
            }
        }
        if (map[x - 1][y][z] != null) {
            Spatial s = map[x - 1][y][z];
            if (!s.getUserData("type").equals("ride")) {
                return;
            }
            int rideidArvo = s.getUserData("rideID");
            if (rideidArvo == rideID - 1) {
                placeEnterancetrue(enterancetype, x, y, z, map,Direction.UP);
                return;
            }
        }
        if (map[x][y][z + 1] != null) {
            Spatial s = map[x][y][z + 1];
            if (!s.getUserData("type").equals("ride")) {
                return;
            }
            int rideidArvo = s.getUserData("rideID");
            if (rideidArvo == rideID - 1) {
                placeEnterancetrue(enterancetype, x, y, z, map,Direction.RIGHT);
                return;
            }
        }
        if (map[x][y][z - 1] != null) {
            Spatial s = map[x][y][z - 1];
            if (!s.getUserData("type").equals("ride")) {
                return;
            }
            int rideidArvo = s.getUserData("rideID");
            if (rideidArvo == rideID - 1) {
                placeEnterancetrue(enterancetype, x, y, z, map,Direction.LEFT);
            }
        }

    }

    public void updateRideQueues() {
        for (BasicRide ride : rides) {
            ride.updateQueLine();
            ride.updateRide();
        }
    }

    private void placeEnterancetrue(boolean enterancetype, int x, int y, int z, Spatial[][][] map,Direction suunta) {
        Enterance e = new Enterance(enterancetype, new Vector3f(x, y, z), suunta, assetManager);
        e.connectedRide = rides.get(rideID - 2);
        e.object.setUserData("type", "enterance");
        map[x][y][z] = e.object;
        if (enterancetype == false) {
            rides.get(rideID - 2).enterance = e;
        } else {
            rides.get(rideID - 2).exit = e;
        }

        rideNode.attachChild(e.object);
        enterancecount++;
        if (enterancecount > 1) {
            enterancecount = 0;
            Main.gamestate.clickingHandler.clickMode = ClickingModes.NOTHING;
        }
    }
    public void setRideID(int rideID){
        this.rideID=rideID;
    }
}
