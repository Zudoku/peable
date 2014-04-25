/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package intopark.npc;

import com.google.inject.Inject;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import intopark.Main;
import static intopark.npc.BasicNPC.logger;
import intopark.npc.inventory.Item;
import intopark.npc.inventory.RideType;
import intopark.npc.inventory.StatManager;
import intopark.npc.inventory.Wallet;
import intopark.ride.BasicRide;
import intopark.roads.Road;
import intopark.roads.Roadgraph;
import intopark.roads.Walkable;
import intopark.terrain.Direction;
import intopark.terrain.MapContainer;
import intopark.terrain.MapPosition;
import intopark.terrain.ParkHandler;
import java.util.Arrays;
import org.jgraph.graph.DefaultEdge;
import org.jgrapht.DirectedGraph;

/**
 *
 * @author arska
 */
public class Guest extends BasicNPC {
    //LOGGER 
    //DEPENDENCIES
    private  transient Random r;
    @Inject private transient ParkHandler parkHandler;
    @Inject private transient MapContainer map;
    //OWNS
    private List<Item> inventory = new ArrayList<>(); // What does the guest carry
    private StatManager stats = new StatManager(); //Emotions and feelings, thoughts
    private Wallet wallet; //Money situation
    //VARIABLES
    private int x;
    private int y;
    private int z;
    private transient GuestWalkingStates walkState = GuestWalkingStates.WALK; //Whether guest is walking or not
    private transient List<NPCAction> actions = new ArrayList<>(); //Npcs actions. Determine where the guest moves and what does he do.
    private int guestnum; //Unique ID for guests
    private Direction moving = Direction.NORTH; //What direction guest is moving.
    private transient boolean active = true; //Is guest active AKA is he on ride? is he allowed to move
    private transient  Spatial currentQueRoad;
    private  transient long joinedRide;

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
        super.getGeometry().setUserData("type","guest");
        super.getGeometry().setUserData("guestnum",guestNum);
    }
    @Deprecated
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
            if (actions.isEmpty()) {
                return;
            }
            super.move(actions.get(0), actions);
        }
        stats.update();

    }
    /**
     * We calculate our next Action (WHERE GUEST MOVES)
     */
    public void calcMovePoints() {
        boolean haveTarget=false;//WHEN I WANT TO EXPAND TO PATHFINDING
        boolean needTarget=false;
        if(needTarget){
            if(!haveTarget){
                //PATHFINDING SHIZ
                haveTarget=true;
            }
        }
        if(haveTarget){
            /* NEED TO MOVE TO TARGET */
        }else{
            /*CAN WONDER OFF FREELY*/
            Roadgraph roadGraph=parkHandler.getRoadGraph();
            Walkable current=roadGraph.getWalkable(x, y, z);
            if(current==null){
                /*NOT ON ROAD. CAN'T MOVE*/
                return;
            }
            DirectedGraph<Walkable,DefaultEdge>roadMap=roadGraph.getRoadMap();
            List<Walkable>possibilities=new ArrayList<>();
            for(DefaultEdge edge:roadMap.outgoingEdgesOf(current)){
                Walkable possi=roadMap.getEdgeTarget(edge);
                possibilities.add(possi);
            }
            if(possibilities.isEmpty()){
                return;
            }
            int seed=0;
            if(possibilities.size()>1){
                seed=r.nextInt(possibilities.size());
            }
            Walkable target=possibilities.get(seed);
            if(target instanceof Road){
                Direction roadDirection=new MapPosition(x, y, z).getDirection(target.getPosition().getX(),target.getPosition().getZ());
                if(moving.isOpposite(roadDirection)){
                    if(5!=r.nextInt(10)){
                        return;
                    }
                }
                actions.add(getSimpleAction(target.getPosition().getVector()));
                moving=roadDirection;
                x=target.getPosition().getX();
                y=target.getPosition().getY();
                z=target.getPosition().getZ();
            }
            
            
            
        }
    }
    private NPCAction getSimpleAction(Vector3f pos){
        NPCAction action=new NPCAction(pos, ActionType.NOTHING, this);
        return action;
    }
    public void initXYZ(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    public int getGuestNum() {
        return guestnum;
    }
    public void callToQueRoad(NPCAction action) {
        actions.add(action);
    }
    public void handleQueRoadFound(Spatial temp) {
    }
    public boolean doIWantToGoThere(BasicRide ride) {
        //TODO: REWORK
        
        /**
         * happyness+laitteen hyvyys+preferredride +40>100**
         */
        int h = stats.happyness / 5; //0-20
        int e = ride.getExitement();   //0-80
        int p = 0;                //0-20
        switch (stats.preferredRide) {
            case LOW:
                if (ride.getRideType() == RideType.LOW) {
                    p = 20;
                }
                if (ride.getRideType() == RideType.MEDIUM) {
                    p = 10;
                }
                break;

            case MEDIUM:
                if (ride.getRideType() == RideType.MEDIUM) {
                    p = 20;
                }
                if (ride.getRideType() == RideType.LOW) {
                    p = 10;
                }
                if (ride.getRideType() == RideType.HIGH) {
                    p = 10;
                }

                break;

            case HIGH:
                if (ride.getRideType() == RideType.MEDIUM) {
                    p = 10;
                }
                if (ride.getRideType() == RideType.CRAZY) {
                    p = 5;
                }
                if (ride.getRideType() == RideType.HIGH) {
                    p = 20;
                }
                break;

            case CRAZY:
                if (ride.getRideType() == RideType.CRAZY) {
                    p = 20;
                }
                if (ride.getRideType() == RideType.NAUSEA) {
                    p = 10;
                }

                break;

            case NAUSEA:
                if (ride.getRideType() == RideType.NAUSEA) {
                    p = 20;
                }
                if (ride.getRideType() == RideType.CRAZY) {
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

    /**
     * GETTERS AND SETTERS
     */
    
    public GuestWalkingStates getWalkState() {
        return walkState;
    }

    
    public StatManager getStats() {
        return stats;
    }
    public void setActive(boolean active) {
        this.active = active;
    }
    public long getJoinedRide() {
        return joinedRide;
    }
    public void setJoinedRide(long joinedRide) {
        this.joinedRide = joinedRide;
    }
    public Wallet getWallet() {
        return wallet;
    }
    public Direction getMoving() {
        return moving;
    }
    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
    public int getZ() {
        return z;
    }

    public void setInventory(List<Item> inventory) {
        this.inventory = inventory;
    }
    public List<Item> getInventory() {
        return inventory;
    }

    public void setMoving(Direction moving) {
        this.moving = moving;
    }
    
}
