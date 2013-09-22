/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.npc;

import com.jme3.asset.AssetManager;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author arska
 */
public class GuestSpawner {

    ArrayList<Vector3f> spawnpoints = new ArrayList<Vector3f>();
    ArrayList<BasicNPC> npcs = new ArrayList<BasicNPC>();
    ArrayList<String> firstName = new ArrayList<String>();
    ArrayList<String> surName = new ArrayList<String>();
    ArrayList<Guest> guests=new ArrayList<Guest>();
    int guestNum = 0;
    private final Node nPCNode;
    private final Node rootNode;
    Random r;
    private final AssetManager assetManager;

    public GuestSpawner(Node nPCNode, Node rootNode, AssetManager assetManager, ArrayList<BasicNPC> npcs,ArrayList<Guest>guests) {
        this.nPCNode = nPCNode;
        this.rootNode = rootNode;
        this.assetManager = assetManager;
        this.npcs = npcs;
        this.guests=guests;
        r = new Random();
        addNames();
        spawnpoints.add(new Vector3f(5, 6, 5));
        spawnpoints.add(new Vector3f(5, 6, 5));
    }

    public void forceSpawnGuest() {
        if (spawnpoints.isEmpty() == true) {
            System.out.println("No or too little spawnpoints");
            return;
        }
        

        int num = r.nextInt(firstName.size() - 1);
        int num2 = r.nextInt(surName.size() - 1);
        String name = firstName.get(num) + " " + surName.get(num2);
        //nimi raha numero
        float money = r.nextInt(30);
        money = money + 35;
        Spatial geom = assetManager.loadModel("Models/Human/guest.j3o");
        geom.setName("thisiswatwerlukinfor");
        geom.setUserData("guestnum", guestNum);
        Guest g = new Guest(name, money, guestNum, geom);
        guestNum++;
        int spp = r.nextInt(spawnpoints.size() - 1);
        g.getGeometry().move(spawnpoints.get(spp));
        g.initXYZ((int) spawnpoints.get(spp).x, (int) spawnpoints.get(spp).y, (int) spawnpoints.get(spp).z);
        npcs.add(g);
        guests.add(g);
        nPCNode.attachChild(g.getGeometry());
        System.out.println("Guest " + Integer.toString(guestNum) + " Named: " + name + " has entered the world");

    }

    public void guestLeave(int guestnum) {
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
