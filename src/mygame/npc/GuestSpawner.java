/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.npc;

import com.google.common.eventbus.EventBus;
import com.jme3.asset.AssetManager;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import mygame.GUI.events.UpdateMoneyTextBarEvent;

/**
 *
 * @author arska
 */
public class GuestSpawner {
    private static final Logger logger = Logger.getLogger(GuestSpawner.class.getName());
    //DEPENDENCIES
    private Random r;
    private final AssetManager assetManager;
    private final EventBus eventBus;
    //OWNS
    private ArrayList<Vector3f> spawnpoints = new ArrayList<Vector3f>();
    private ArrayList<BasicNPC> npcs = new ArrayList<BasicNPC>();
    private ArrayList<String> firstName = new ArrayList<String>();
    private ArrayList<String> surName = new ArrayList<String>();
    private ArrayList<Guest> guests=new ArrayList<Guest>();
    //VARIABLES
    private int guestNum = 1;
    private final Node nPCNode;
    

    public GuestSpawner(Node nPCNode,AssetManager assetManager,EventBus eventBus) {
        this.nPCNode = nPCNode;
        this.assetManager = assetManager;
        this.eventBus=eventBus;
        r = new Random();
        addNames();
        spawnpoints.add(new Vector3f(3, 6, 2));
        spawnpoints.add(new Vector3f(2, 6, 2));
    }
    public void setNpcs(ArrayList<BasicNPC> npcs){
        this.npcs=npcs;
    }
    public void setGuests(ArrayList<Guest> guests){
        this.guests=guests;
    }
    public void forceSpawnGuest(int n) {
        if (spawnpoints.isEmpty() == true) {
            logger.log(Level.SEVERE,"No or too little spawnpoints");
            return;
        }
        //We choose a random name
        int num = r.nextInt(firstName.size() - 1);
        int num2 = r.nextInt(surName.size() - 1);
        
        String name = firstName.get(num) + " " + surName.get(num2);
        //Calculate random number
        float money = r.nextInt(30);
        money = money + 35;
        //Load the model and give it unique ID and money.
        Spatial geom = assetManager.loadModel("Models/Human/guest.j3o");
        geom.setName("guest");
        geom.setUserData("guestnum", guestNum);
        Guest g = new Guest(name, money, guestNum, geom);
        guestNum++;
        //Random spawnpoint
        int spp = r.nextInt(spawnpoints.size());
        g.getGeometry().move(spawnpoints.get(spp));
        //Initialize X,Y,Z so that the guest can move
        g.initXYZ((int) spawnpoints.get(spp).x, (int) spawnpoints.get(spp).y, (int) spawnpoints.get(spp).z);
        //Add the guest to list and add it to rootNode.
        npcs.add(g);
        guests.add(g);
        nPCNode.attachChild(g.getGeometry());
        logger.log(Level.FINEST, "Guest {0} Named: {1} has entered the world", new Object[]{Integer.toString(guestNum), name});
        if(n!=1){
            eventBus.post(new UpdateMoneyTextBarEvent());
        }
        
    }

    public void guestLeave(int guestnum) {
    }

    public int getGuestNum() {
        return guestNum;
    }
    
    private void addNames() {
        firstName.add("John");
        firstName.add("Arnold");
        firstName.add("James");
        firstName.add("Ed");
        firstName.add("Matt");
        firstName.add("Mathias");
        firstName.add("Jack");
        firstName.add("Bob");
        firstName.add("William");
        firstName.add("Tony");
        firstName.add("Ken");
        firstName.add("Sam");
        firstName.add("Elvis");
        firstName.add("Robert");
        firstName.add("Michael");
        firstName.add("David");
        firstName.add("Daniel");
        firstName.add("Joseph");
        firstName.add("Thomas");
        firstName.add("Mark");
        firstName.add("Donald");
        firstName.add("George");
        firstName.add("Will");
        firstName.add("Bill");
        firstName.add("Jeff");
        firstName.add("Ronald");
        firstName.add("Edward");
        firstName.add("Adam");
        firstName.add("Jason");


        firstName.add("Mary");
        firstName.add("Patricia");
        firstName.add("Linda");
        firstName.add("Barbara");
        firstName.add("Elisabeth");
        firstName.add("Jennifer");
        firstName.add("Maria");
        firstName.add("Kate");
        firstName.add("Susan");
        firstName.add("Margaret");
        firstName.add("Lisa");
        firstName.add("Nancy");
        firstName.add("Helen");
        firstName.add("Donna");
        firstName.add("Carol");
        firstName.add("Laura");
        firstName.add("Ruth");
        firstName.add("Karen");
        firstName.add("Ruth");

        surName.add("Smith");
        surName.add("Johnson");
        surName.add("Williams");
        surName.add("Jones");
        surName.add("Brown");
        surName.add("Davis");
        surName.add("Miller");
        surName.add("Wilson");
        surName.add("Moore");
        surName.add("Taylor");
        surName.add("Anderson");
        surName.add("Thomas");
        surName.add("Jackson");
        surName.add("White");
        surName.add("Harris");
        surName.add("Martin");
        surName.add("Thompson");
        surName.add("Garcia");
        surName.add("Martinez");
        surName.add("Robinson");
        surName.add("Clark");
        surName.add("Lee");
        surName.add("Allen");
        surName.add("Young");
        surName.add("King");
        surName.add("Scott");

    }
}
