/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.npc;

import com.google.inject.Inject;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import mygame.Main;
import mygame.inputhandler.UserInput;
import mygame.npc.inventory.Item;
import mygame.npc.inventory.RideType;
import mygame.npc.inventory.StatManager;
import mygame.npc.inventory.Wallet;
import mygame.ride.BasicRide;
import mygame.ride.RideManager;
import mygame.shops.BasicShop;
import mygame.shops.ShopManager;
import mygame.terrain.Direction;
import mygame.terrain.MapContainer;
import mygame.terrain.ParkHandler;
import mygame.terrain.RoadMaker;

/**
 *
 * @author arska
 */
public class Guest extends BasicNPC {
    public Wallet wallet;
    private int guestnum;
    Direction moving = Direction.UP;
    private int x;
    private int y;
    private int z;
    private  transient Random r;
    private  transient GuestWalkingStates walkState = GuestWalkingStates.WALK;
    
    @Inject private transient ParkHandler currentPark;
    @Inject private transient ShopManager shopManager;
    @Inject private transient RideManager rideManager;
    @Inject private transient RoadMaker roadMaker;
    @Inject private transient MapContainer map;
    private transient List<NPCAction> actions = new ArrayList<NPCAction>();
    private List<Item> inventory = new ArrayList<Item>();
    public StatManager stats = new StatManager();
    public  transient boolean active = true;
    public transient  Spatial currentQueRoad;
    public  transient long joinedRide;

    public Guest(Wallet wallet, int guestNum, Direction moving, int x1, int y1, int z1, StatManager stats, Spatial geom, String name) {
        super(name, geom);
        Main.injector.injectMembers(this);
        initXYZ(x1, y1, z1);
        this.moving = moving;
        this.stats = stats;
        this.wallet = wallet;
        this.guestnum = guestNum;
        r = new Random();
        super.getGeometry().setLocalTranslation(x, y, z);
    }

    public Guest(String name, float money, int guestNum, Spatial geom) {
        super(name, geom);
        Main.injector.injectMembers(this);
        this.wallet = new Wallet(money);
        this.guestnum = guestNum;
        r = new Random();
        stats.randomize();

    }

    public void deleteActions() {
        actions.clear();
    }

    public boolean isEmptyActions() {
        if (actions.isEmpty()) {
            return true;
        }
        return false;
    }

    @Override
    public void update() {
        if (actions.size() < 1 && active) {

            calcMovePoints();
        }
        if (walkState == GuestWalkingStates.WALK) {
            if (map.getMap()[x][y][z] == null) {
                return;
            }
            if (actions.isEmpty() == true) {
                return;
            }
            super.move(actions.get(0), actions);
        }
        stats.update();

    }

    public void calcMovePoints() {
        //0 p 1 e 2 i 3 l
        int suunta = r.nextInt(4);
        ActionType actiontype = ActionType.NOTHING;
        if (r.nextInt(100) == 5) {
            actiontype = ActionType.CONSUME;
        }
        if (suunta == 0) {
            if (map.getMap()[x + 1][y][z] != null) {
                if (moving == Direction.DOWN) {
                    int pass = r.nextInt(10);
                    if (pass != 1) {
                        return;
                    }
                }
                Spatial temp = map.getMap()[x + 1][y][z];
                if (temp.getUserData("type").equals("road")) {
                    if (temp.getUserData("roadHill").equals("upHill") || temp.getUserData("roadHill").equals("downHill")) {
                        if (temp.getUserData("direction").equals("UP") || temp.getUserData("direction").equals("DOWN")) {
                            if (temp == map.getMap()[x + 1][y - 1][z]) {
                                actions.add(new NPCAction(new Vector3f(x + 0.5f, y + 0.1f, z), actiontype, this));
                                actions.add(new NPCAction(new Vector3f(x + 1f, y - 0.9f, z), actiontype, this));
                                x = x + 1;
                                y = y - 1;
                                moving = Direction.UP;
                            }
                            if (temp == map.getMap()[x + 1][y + 1][z]) {
                                actions.add(new NPCAction(new Vector3f(x + 0.5f, y + 0.1f, z), actiontype, this));
                                actions.add(new NPCAction(new Vector3f(x + 1f, y + 1.1f, z), actiontype, this));
                                x = x + 1;
                                y = y + 1;
                                moving = Direction.UP;
                            }

                        }


                    }

                    if (temp.getUserData("roadHill").equals("flat")) {
                        actions.add(new NPCAction(new Vector3f(x + 1f, y + 0.1f, z), actiontype, this));
                        x = x + 1;
                        moving = Direction.UP;
                    }

                }
                if (temp.getUserData("type").equals("queroad")) {
                    handleQueRoadFound(temp);

                }

                if (temp.getUserData("type").equals("shop")) {
                    BasicShop foundshop = shopManager.isthereshop(x + 1, y, z);
                    if (foundshop != null) {
                        NPCAction buy = new NPCAction(new Vector3f(x + 0.7f, y + 0.1f, z), ActionType.BUY, foundshop, this);
                        actions.add(buy);
                        actions.add(new NPCAction(new Vector3f(x, y + 0.1f, z), ActionType.NOTHING, this));
                        moving = Direction.UP;
                    }
                }

            }

        }
        if (suunta == 1) {
            if (map.getMap()[x - 1][y][z] != null) {
                Spatial temp = map.getMap()[x - 1][y][z];
                if (moving == Direction.UP) {
                    int pass = r.nextInt(10);
                    if (pass != 1) {
                        return;
                    }
                }
                if (temp.getUserData("type").equals("road")) {
                    if (temp.getUserData("roadHill").equals("upHill") || temp.getUserData("roadHill").equals("downHill")) {
                        if (temp.getUserData("direction").equals("UP") || temp.getUserData("direction").equals("DOWN")) {
                            if (temp == map.getMap()[x - 1][y - 1][z]) {
                                actions.add(new NPCAction(new Vector3f(x - 0.5f, y + 0.1f, z), actiontype, this));
                                actions.add(new NPCAction(new Vector3f(x - 1f, y - 0.9f, z), actiontype, this));
                                x = x - 1;
                                y = y - 1;
                                moving = Direction.DOWN;
                            }
                            if (temp == map.getMap()[x - 1][y + 1][z]) {
                                actions.add(new NPCAction(new Vector3f(x - 0.5f, y + 0.1f, z), actiontype, this));
                                actions.add(new NPCAction(new Vector3f(x - 1f, y + 1.1f, z), actiontype, this));
                                x = x - 1;
                                y = y + 1;
                                moving = Direction.DOWN;
                            }
                        }
                    }
                    if (temp.getUserData("roadHill").equals("flat")) {
                        actions.add(new NPCAction(new Vector3f(x - 1, y + 0.1f, z), actiontype, this));
                        x = x - 1;
                        moving = Direction.DOWN;
                    }

                }
                if (temp.getUserData("type").equals("queroad")) {
                    handleQueRoadFound(temp);
                }

                if (temp.getUserData("type").equals("shop")) {
                    BasicShop foundshop = shopManager.isthereshop(x - 1, y, z);
                    if (foundshop != null) {
                        NPCAction buy = new NPCAction(new Vector3f(x - 0.7f, y + 0.1f, z), ActionType.BUY, foundshop, this);
                        actions.add(buy);
                        actions.add(new NPCAction(new Vector3f(x, y + 0.1f, z), ActionType.NOTHING, this));
                        moving = Direction.DOWN;
                    }
                }

            }
        }
        if (suunta == 2) {
            if (map.getMap()[x][y][z + 1] != null) {
                Spatial temp = map.getMap()[x][y][z + 1];
                if (moving == Direction.LEFT) {
                    int pass = r.nextInt(10);
                    if (pass != 1) {
                        return;
                    }
                }
                if (temp.getUserData("type").equals("road")) {
                    if (temp.getUserData("roadHill").equals("upHill") || temp.getUserData("roadHill").equals("downHill")) {
                        if (temp.getUserData("direction").equals("LEFT") || temp.getUserData("direction").equals("RIGHT")) {
                            if (temp == map.getMap()[x][y - 1][z + 1]) {
                                actions.add(new NPCAction(new Vector3f(x, y + 0.1f, z + 0.5f), actiontype, this));
                                actions.add(new NPCAction(new Vector3f(x, y - 0.9f, z + 1f), actiontype, this));
                                z = z + 1;
                                y = y - 1;
                                moving = Direction.RIGHT;
                            }
                            if (temp == map.getMap()[x][y + 1][z + 1]) {
                                actions.add(new NPCAction(new Vector3f(x, y + 0.1f, z + 0.5f), actiontype, this));
                                actions.add(new NPCAction(new Vector3f(x, y + 1.1f, z + 1f), actiontype, this));
                                z = z + 1;
                                y = y + 1;
                                moving = Direction.RIGHT;
                            }
                        }
                    }
                    if (temp.getUserData("roadHill").equals("flat")) {
                        actions.add(new NPCAction(new Vector3f(x, y + 0.1f, z + 1), actiontype, this));
                        z = z + 1;
                        moving = Direction.RIGHT;
                    }

                }
                if (temp.getUserData("type").equals("queroad")) {
                    handleQueRoadFound(temp);
                }

                if (temp.getUserData("type").equals("shop")) {
                    BasicShop foundshop = shopManager.isthereshop(x, y, z + 1);
                    if (foundshop != null) {
                        NPCAction buy = new NPCAction(new Vector3f(x, y + 0.1f, z + 0.7f), ActionType.BUY, foundshop, this);
                        actions.add(buy);
                        actions.add(new NPCAction(new Vector3f(x, y + 0.1f, z), ActionType.NOTHING, this));
                        moving = Direction.RIGHT;
                    }
                }

            }

        }
        if (suunta == 3) {
            if (map.getMap()[x][y][z - 1] != null) {

                Spatial temp = map.getMap()[x][y][z - 1];
                if (moving == Direction.RIGHT) {
                    int pass = r.nextInt(10);
                    if (pass != 1) {
                        return;
                    }
                }
                if (temp.getUserData("type").equals("road")) {
                    if (temp.getUserData("roadHill").equals("upHill") || temp.getUserData("roadHill").equals("downHill")) {
                        if (temp.getUserData("direction").equals("LEFT") || temp.getUserData("direction").equals("RIGHT")) {
                            if (temp == map.getMap()[x][y - 1][z - 1]) {
                                actions.add(new NPCAction(new Vector3f(x, y + 0.1f, z - 0.5f), actiontype, this));
                                actions.add(new NPCAction(new Vector3f(x, y - 0.9f, z - 1), actiontype, this));
                                z = z - 1;
                                y = y - 1;
                                moving = Direction.LEFT;
                            }
                            if (temp == map.getMap()[x][y + 1][z - 1]) {
                                actions.add(new NPCAction(new Vector3f(x, y + 0.1f, z - 0.5f), actiontype, this));
                                actions.add(new NPCAction(new Vector3f(x, y + 1.1f, z - 1), actiontype, this));
                                z = z - 1;
                                y = y + 1;
                                moving = Direction.LEFT;
                            }
                        }
                    }
                    if (temp.getUserData("roadHill").equals("flat")) {
                        actions.add(new NPCAction(new Vector3f(x, y + 0.1f, z - 1), actiontype, this));
                        z = z - 1;
                        moving = Direction.LEFT;
                    }

                }
                if (temp.getUserData("type").equals("queroad")) {
                    handleQueRoadFound(temp);
                }

                if (temp.getUserData("type").equals("shop")) {
                    BasicShop foundshop = shopManager.isthereshop(x, y, z - 1);
                    if (foundshop != null) {
                        NPCAction buy = new NPCAction(new Vector3f(x, y + 0.1f, z - 0.7f), ActionType.BUY, foundshop, this);
                        actions.add(buy);
                        actions.add(new NPCAction(new Vector3f(x, y + 0.1f, z), ActionType.NOTHING, this));
                        moving = Direction.LEFT;
                    }
                }

            }
        }
    }

    public ArrayList<Spatial> getlinkedqueroads(Spatial firstroad) {
        ArrayList<Spatial> list = new ArrayList<Spatial>();
        Spatial nextroad = firstroad;
        boolean end = false;
        int max = 0;
        while (end) {
            max++;
            boolean connected = nextroad.getUserData("connected");
            if (connected == true) {
                if (nextroad.getUserData("connectedEnterance") != null) {
                    end = true;
                }
                list.add(nextroad);
                nextroad = nextroad.getUserData("connected2");
            } else {
                end = true;
            }
            if (max > 100) {
                end = true;
                logger.log(Level.FINE,"Getting linked queroads looped for 100 times. Quitting...");
            }
        }

        return list;

    }

    public void initXYZ(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
        
    }

    public int getGuestNum() {
        return guestnum;
    }

    public GuestWalkingStates getWalkingState() {
        return walkState;
    }

    public void callToQueRoad(NPCAction action) {
        actions.add(action);
    }

    public void handleQueRoadFound(Spatial temp) {
        boolean found = false;
        logger.log(Level.FINEST,"Testing the found queroad");
        int fRideID = 0;
        Spatial trueroad = null;
        BasicRide foundRide = null;
        for (BasicRide s : rideManager.rides) {
            if (s.enterance != null) {
                if (s.enterance.connectedRoad != null) {
                    ArrayList<Spatial> a = roadMaker.getlinkedqueroads(s.enterance.connectedRoad);
                    trueroad = temp.getUserData("queconnect1");
                    if (a.contains(trueroad)) {
                        logger.log(Level.FINEST,"Found the ride witch the road is connected to");
                        found = true;
                        fRideID = s.getRideID();
                        break;
                    }
                    logger.log(Level.FINEST,"Didnt find the ride witch the road is connected to");
                }

            }
        }

        if (found) {
            for (BasicRide a : rideManager.rides) {
                if (a.getRideID() == fRideID) {
                    foundRide = a;
                }
            }
            // If the guest wants to go to the ride
            if (doIWantToGoThere(foundRide)) {
                if (foundRide.tryToQueGuest(this)) {
                    if (wallet.canAfford(foundRide.getPrice())) {
                        logger.log(Level.FINEST,"Accepted the ride and now in que");
                        active = false;
                        this.currentQueRoad = trueroad;
                    }
                }
            }
        }

    }

    public boolean doIWantToGoThere(BasicRide ride) {
        /**
         * happyness+laitteen hyvyys+preferredride +40>100**
         */
        int h = stats.happyness / 5; //0-20
        int e = ride.getExitement();   //0-80
        int p = 0;                //0-20
        switch (stats.preferredRide) {
            case LOW:
                if (ride.rideType == RideType.LOW) {
                    p = 20;
                }
                if (ride.rideType == RideType.MEDIUM) {
                    p = 10;
                }
                break;

            case MEDIUM:
                if (ride.rideType == RideType.MEDIUM) {
                    p = 20;
                }
                if (ride.rideType == RideType.LOW) {
                    p = 10;
                }
                if (ride.rideType == RideType.HIGH) {
                    p = 10;
                }

                break;

            case HIGH:
                if (ride.rideType == RideType.MEDIUM) {
                    p = 10;
                }
                if (ride.rideType == RideType.CRAZY) {
                    p = 5;
                }
                if (ride.rideType == RideType.HIGH) {
                    p = 20;
                }
                break;

            case CRAZY:
                if (ride.rideType == RideType.CRAZY) {
                    p = 20;
                }
                if (ride.rideType == RideType.NAUSEA) {
                    p = 10;
                }

                break;

            case NAUSEA:
                if (ride.rideType == RideType.NAUSEA) {
                    p = 20;
                }
                if (ride.rideType == RideType.CRAZY) {
                    p = 10;
                }

                break;

        }
        //happyness+exitement+preference+40
        int rating = h + e + p + 40;
        logger.log(Level.FINEST,"Guest got rating of {0}",rating);
        if (rating > 100) {
            logger.log(Level.FINEST,"Guest is ready to accept the ride");
            return true;
        }
        return false;
    }

    public Direction getmoveDirection() {
        return moving;
    }

    /**
     * Use only when accessing save data!
     *
     * @return
     */
    public int getX() {
        return x;
    }

    /**
     * Use only when accessing save data!
     *
     * @return
     */
    public int getY() {
        return y;
    }

    /**
     * Use only when accessing save data!
     *
     * @return
     */
    public int getZ() {
        return z;
    }

    public void setInventory(List<Item> inventory) {
        this.inventory = inventory;
    }

    public List<Item> getInventory() {
        return inventory;
    }
    
}
