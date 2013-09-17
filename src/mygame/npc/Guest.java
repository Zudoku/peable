/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.npc;

import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import java.util.ArrayList;
import java.util.Random;
import mygame.Main;
import mygame.npc.inventory.Item;
import mygame.npc.inventory.Wallet;
import mygame.shops.BasicShop;

/**
 *
 * @author arska
 */
public class Guest extends BasicNPC {

    public Wallet wallet;
    private int guestnum;
    private int hunger = 0;
    private int thirst = 0;
    private int bathroom = 0;
    private int x;
    private int y;
    private int z;
    private Random r;
    private GuestWalkingStates walkState = GuestWalkingStates.WALK;
    Spatial[][][] roads;
    ArrayList<NPCAction> actions = new ArrayList<NPCAction>();
    public ArrayList<Item> inventory = new ArrayList<Item>();

    public Guest(String name, float money, int guestNum, Spatial geom) {
        super(name, geom);

        Node test = new Node();

        this.wallet=new Wallet(money);
        this.guestnum = guestNum;
        r = new Random();

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

    }

    public void randomizeStats() {
        //todo
    }

    public void calcMovePoints() {
        //0 p 1 e 2 i 3 l
        int suunta = r.nextInt(4);
        if (suunta == 0) {
            if (roads[x + 1][y][z] != null) {
                if (roads[x + 1][y+1][z] != null && roads[x + 2][y+1][z] != null) {
                    actions.add(new NPCAction(new Vector3f(x + 0.5f, y + 0.1f, z), ActionType.NOTHING));
                    actions.add(new NPCAction(new Vector3f(x + 1f, y + 1.1f, z), ActionType.NOTHING));
                    x = x + 1;
                    y = y + 1;
                } else if (roads[x + 1][y-1][z] != null && roads[x + 2][y-1][z] != null) {
                    actions.add(new NPCAction(new Vector3f(x + 0.5f, y + 0.1f, z), ActionType.NOTHING));
                    actions.add(new NPCAction(new Vector3f(x + 1f, y - 0.9f, z), ActionType.NOTHING));
                    x = x + 1;
                    y = y - 1;
                } else {
                    actions.add(new NPCAction(new Vector3f(x + 1f, y + 0.1f, z), ActionType.NOTHING));
                    x = x + 1;
                }
            }
            BasicShop foundshop=Main.shopManager.isthereshop(x+1, y, z);
            if(foundshop!=null){
                
                NPCAction buy=new NPCAction(new Vector3f(x + 0.3f, y + 0.1f, z), ActionType.BUY);
                buy.buyAction(foundshop, this);
                actions.add(buy);
                actions.add(new NPCAction(new Vector3f(x - 0.3f, y + 0.1f, z), ActionType.NOTHING));
            }
        }
        if (suunta == 1) {
            if (roads[x - 1][y][z] != null) {
                if (roads[x - 1][y+1][z] !=null && roads[x - 2][y+1][z] != null) {
                    actions.add(new NPCAction(new Vector3f(x - 0.5f, y + 0.1f, z), ActionType.NOTHING));
                    actions.add(new NPCAction(new Vector3f(x - 1f, y + 1.1f, z), ActionType.NOTHING));
                    x = x - 1;
                    y=y+1;
                } else if (roads[x - 1][y-1][z] !=null && roads[x - 2][y-1][z] != null) {
                    actions.add(new NPCAction(new Vector3f(x - 0.5f, y + 0.1f, z), ActionType.NOTHING));
                    actions.add(new NPCAction(new Vector3f(x - 1f, y - 0.9f, z), ActionType.NOTHING));
                    x = x-1;
                    y=y-1;
                } else {
                    actions.add(new NPCAction(new Vector3f(x - 1, y + 0.1f, z), ActionType.NOTHING));
                    x = x - 1;
                }
            }
            BasicShop foundshop=Main.shopManager.isthereshop(x-1, y, z);
            if(foundshop!=null){
                
                NPCAction buy=new NPCAction(new Vector3f(x - 0.3f, y + 0.1f, z), ActionType.BUY);
                buy.buyAction(foundshop, this);
                actions.add(buy);
                actions.add(new NPCAction(new Vector3f(x + 0.3f, y + 0.1f, z), ActionType.NOTHING));
            }
        }
        if (suunta == 2) {
            if (roads[x][y][z + 1] != null) {
                if(roads[x][y+1][z + 1] != null && roads[x][y+1][z + 2] != null){
                    actions.add(new NPCAction(new Vector3f(x, y + 0.1f, z + 0.5f), ActionType.NOTHING));
                    actions.add(new NPCAction(new Vector3f(x, y + 1.1f, z + 1f), ActionType.NOTHING));
                    z=z+1;
                    y=y+1;
                }
                else if (roads[x][y-1][z + 1] != null && roads[x][y-1][z + 2] != null){
                    actions.add(new NPCAction(new Vector3f(x, y + 0.1f, z + 0.5f), ActionType.NOTHING));
                    actions.add(new NPCAction(new Vector3f(x, y - 0.9f, z + 1f), ActionType.NOTHING));
                    z=z+1;
                    y=y-1;
                    
                }
                else{
                    actions.add(new NPCAction(new Vector3f(x, y + 0.1f, z + 1), ActionType.NOTHING));
                    z = z + 1;
                }
            }
            BasicShop foundshop=Main.shopManager.isthereshop(x, y, z+1);
            if(foundshop!=null){
                
                NPCAction buy=new NPCAction(new Vector3f(x, y + 0.1f, z+ 0.3f), ActionType.BUY);
                buy.buyAction(foundshop, this);
                actions.add(buy);
                actions.add(new NPCAction(new Vector3f(x, y + 0.1f, z- 0.3f), ActionType.NOTHING));
            }
        }
        if (suunta == 3) {
            if (roads[x][y][z - 1] != null) {
                if(roads[x][y+1][z - 1] != null && roads[x][y+1][z - 2] != null){
                    actions.add(new NPCAction(new Vector3f(x, y + 0.1f, z - 0.5f), ActionType.NOTHING));
                    actions.add(new NPCAction(new Vector3f(x, y + 1.1f, z - 1), ActionType.NOTHING));
                    z=z-1;
                    y=y+1;
                }
                else if (roads[x][y-1][z - 1] != null && roads[x][y-1][z - 2] != null){
                    actions.add(new NPCAction(new Vector3f(x, y + 0.1f, z - 0.5f), ActionType.NOTHING));
                    actions.add(new NPCAction(new Vector3f(x, y - 0.9f, z - 1), ActionType.NOTHING));
                    z=z-1;
                    y=y-1;
                }
                else{
                    actions.add(new NPCAction(new Vector3f(x, y + 0.1f, z - 1), ActionType.NOTHING));
                    z = z - 1;
                }
               
            }
            BasicShop foundshop=Main.shopManager.isthereshop(x, y, z-1);
            if(foundshop!=null){
                
                NPCAction buy=new NPCAction(new Vector3f(x, y + 0.1f, z- 0.3f), ActionType.BUY);
                buy.buyAction(foundshop, this);
                actions.add(buy);
                actions.add(new NPCAction(new Vector3f(x, y + 0.1f, z+ 0.3f), ActionType.NOTHING));
            }

        }
    }

    public void initXYZ(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
        roads = Main.roadMaker.roads;
    }
}
