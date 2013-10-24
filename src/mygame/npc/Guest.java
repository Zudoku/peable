/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.npc;

import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import java.util.ArrayList;
import java.util.Random;
import mygame.Main;
import mygame.npc.inventory.Item;
import mygame.npc.inventory.StatManager;
import mygame.npc.inventory.Wallet;
import mygame.shops.BasicShop;
import mygame.terrain.Direction;

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
    private Random r;
    private GuestWalkingStates walkState = GuestWalkingStates.WALK;
    Spatial[][][] roads;
    ArrayList<NPCAction> actions = new ArrayList<NPCAction>();
    public ArrayList<Item> inventory = new ArrayList<Item>();
    public StatManager stats = new StatManager();

    public Guest(String name, float money, int guestNum, Spatial geom) {
        super(name, geom);

        this.wallet = new Wallet(money);
        this.guestnum = guestNum;
        r = new Random();
        stats.randomize();

    }

    @Override
    public void update() {
        if (actions.size() < 1) {

            calcMovePoints();
        }
        if (walkState == GuestWalkingStates.WALK) {
            if (roads[x][y][z] == null) {
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
        if (r.nextInt(10) == 5) {
            actiontype = ActionType.CONSUME;
        }
        if (suunta == 0) {
            if (roads[x + 1][y][z] != null) {
                if(moving==Direction.DOWN){
                    int pass = r.nextInt(10);
                    if(pass!=1){
                        return;
                    }
                }
                Spatial temp = roads[x + 1][y][z];
                if (temp.getUserData("type").equals("road")) {
                    if (temp.getUserData("roadHill").equals("uphill")) {
                        actions.add(new NPCAction(new Vector3f(x + 0.5f, y + 0.1f, z), actiontype, this));
                        actions.add(new NPCAction(new Vector3f(x + 1f, y + 1.1f, z), actiontype, this));
                        x = x + 1;
                        y = y + 1;
                        moving= Direction.UP;
                    }
                    if (temp.getUserData("roadHill").equals("downhill")) {
                        actions.add(new NPCAction(new Vector3f(x + 0.5f, y + 0.1f, z), actiontype, this));
                        actions.add(new NPCAction(new Vector3f(x + 1f, y - 0.9f, z), actiontype, this));
                        x = x + 1;
                        y = y - 1;
                        moving= Direction.UP;
                    }
                    if (temp.getUserData("roadHill").equals("flat")) {
                        actions.add(new NPCAction(new Vector3f(x + 1f, y + 0.1f, z), actiontype, this));
                        x = x + 1;
                        moving= Direction.UP;
                    }

                }
                if (temp.getUserData("type").equals("queroad")) {
                }

                if (temp.getUserData("type").equals("shop")) {
                    BasicShop foundshop = Main.shopManager.isthereshop(x + 1, y, z);
                    if (foundshop != null) {
                        NPCAction buy = new NPCAction(new Vector3f(x + 0.7f, y + 0.1f, z), ActionType.BUY, foundshop, this);
                        actions.add(buy);
                        actions.add(new NPCAction(new Vector3f(x, y + 0.1f, z), ActionType.NOTHING, this));
                        moving= Direction.UP;
                    }
                }

            }

        }
        if (suunta == 1) {
            if (roads[x - 1][y][z] != null) {
                Spatial temp = roads[x - 1][y][z];
                if(moving==Direction.UP){
                    int pass = r.nextInt(10);
                    if(pass!=1){
                        return;
                    }
                }
                if (temp.getUserData("type").equals("road")) {
                    if (temp.getUserData("roadHill").equals("uphill")) {
                        actions.add(new NPCAction(new Vector3f(x - 0.5f, y + 0.1f, z), actiontype, this));
                        actions.add(new NPCAction(new Vector3f(x - 1f, y + 1.1f, z), actiontype, this));
                        x = x - 1;
                        y = y + 1;
                        moving= Direction.DOWN;
                    }
                    if (temp.getUserData("roadHill").equals("downhill")) {
                        actions.add(new NPCAction(new Vector3f(x - 0.5f, y + 0.1f, z), actiontype, this));
                        actions.add(new NPCAction(new Vector3f(x - 1f, y - 0.9f, z), actiontype, this));
                        x = x - 1;
                        y = y - 1;
                        moving= Direction.DOWN;
                    }
                    if (temp.getUserData("roadHill").equals("flat")) {
                        actions.add(new NPCAction(new Vector3f(x - 1, y + 0.1f, z), actiontype, this));
                        x = x - 1;
                        moving= Direction.DOWN;
                    }

                }
                if (temp.getUserData("type").equals("queroad")) {
                }

                if (temp.getUserData("type").equals("shop")) {
                    BasicShop foundshop = Main.shopManager.isthereshop(x - 1, y, z);
                    if (foundshop != null) {
                        NPCAction buy = new NPCAction(new Vector3f(x - 0.7f, y + 0.1f, z), ActionType.BUY, foundshop, this);
                        actions.add(buy);
                        actions.add(new NPCAction(new Vector3f(x, y + 0.1f, z), ActionType.NOTHING, this));
                        moving= Direction.DOWN;
                    }
                }

            }
        }
        if (suunta == 2) {
            if (roads[x][y][z + 1] != null) {
                Spatial temp = roads[x][y][z + 1];
                if(moving==Direction.LEFT){
                    int pass = r.nextInt(10);
                    if(pass!=1){
                        return;
                    }
                }
                if (temp.getUserData("type").equals("road")) {
                    if (temp.getUserData("roadHill").equals("uphill")) {
                        actions.add(new NPCAction(new Vector3f(x, y + 0.1f, z + 0.5f), actiontype, this));
                        actions.add(new NPCAction(new Vector3f(x, y + 1.1f, z + 1f), actiontype, this));
                        z = z + 1;
                        y = y + 1;
                        moving= Direction.RIGHT;
                    }
                    if (temp.getUserData("roadHill").equals("downhill")) {
                        actions.add(new NPCAction(new Vector3f(x, y + 0.1f, z + 0.5f), actiontype, this));
                        actions.add(new NPCAction(new Vector3f(x, y - 0.9f, z + 1f), actiontype, this));
                        z = z + 1;
                        y = y - 1;
                        moving= Direction.RIGHT;
                    }
                    if (temp.getUserData("roadHill").equals("flat")) {
                        actions.add(new NPCAction(new Vector3f(x, y + 0.1f, z + 1), actiontype, this));
                        z = z + 1;
                        moving= Direction.RIGHT;
                    }

                }
                if (temp.getUserData("type").equals("queroad")) {
                }

                if (temp.getUserData("type").equals("shop")) {
                    BasicShop foundshop = Main.shopManager.isthereshop(x, y, z + 1);
                    if (foundshop != null) {
                        NPCAction buy = new NPCAction(new Vector3f(x, y + 0.1f, z + 0.7f), ActionType.BUY, foundshop, this);
                        actions.add(buy);
                        actions.add(new NPCAction(new Vector3f(x, y + 0.1f, z), ActionType.NOTHING, this));
                        moving= Direction.RIGHT;
                    }
                }

            }

        }
        if (suunta == 3) {
            if (roads[x][y][z - 1] != null) {

                Spatial temp = roads[x][y][z - 1];
                if(moving==Direction.RIGHT){
                    int pass = r.nextInt(10);
                    if(pass!=1){
                        return;
                    }
                }
                if (temp.getUserData("type").equals("road")) {
                    if (temp.getUserData("roadHill").equals("uphill")) {
                        actions.add(new NPCAction(new Vector3f(x, y + 0.1f, z - 0.5f), actiontype, this));
                        actions.add(new NPCAction(new Vector3f(x, y + 1.1f, z - 1), actiontype, this));
                        z = z - 1;
                        y = y + 1;
                        moving= Direction.LEFT;
                    }
                    if (temp.getUserData("roadHill").equals("downhill")) {
                        actions.add(new NPCAction(new Vector3f(x, y + 0.1f, z - 0.5f), actiontype, this));
                        actions.add(new NPCAction(new Vector3f(x, y - 0.9f, z - 1), actiontype, this));
                        z = z - 1;
                        y = y - 1;
                        moving= Direction.LEFT;
                    }
                    if (temp.getUserData("roadHill").equals("flat")) {
                        actions.add(new NPCAction(new Vector3f(x, y + 0.1f, z - 1), actiontype, this));
                        z = z - 1;
                        moving= Direction.LEFT;
                    }

                }
                if (temp.getUserData("type").equals("queroad")) {
                }

                if (temp.getUserData("type").equals("shop")) {
                    BasicShop foundshop = Main.shopManager.isthereshop(x, y, z - 1);
                    if (foundshop != null) {
                        NPCAction buy = new NPCAction(new Vector3f(x, y + 0.1f, z - 0.7f), ActionType.BUY, foundshop, this);
                        actions.add(buy);
                        actions.add(new NPCAction(new Vector3f(x, y + 0.1f, z), ActionType.NOTHING, this));
                        moving= Direction.LEFT;
                    }
                }

            }
        }
    }

    public void initXYZ(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
        roads = Main.roadMaker.map;
    }

    public int getGuestNum() {
        return guestnum;
    }

    public GuestWalkingStates getWalkingState() {
        return walkState;
    }
}
