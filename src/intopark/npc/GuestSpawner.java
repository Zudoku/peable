/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package intopark.npc;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.jme3.asset.AssetManager;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import intopark.GUI.events.UpdateMoneyTextBarEvent;
import intopark.inout.LoadPaths;
import intopark.npc.events.SetGuestSpawnPointsEvent;
import java.util.Arrays;

/**
 *
 * @author arska
 */
public class GuestSpawner {
    //LOGGER
    private static final Logger logger = Logger.getLogger(GuestSpawner.class.getName());
    //DEPENDENCIES
    private Random r;
    private final AssetManager assetManager;
    private final EventBus eventBus;
    //OWNS
    private List<Vector3f> spawnpoints = new ArrayList<Vector3f>();
    private List<BasicNPC> npcs = new ArrayList<BasicNPC>();
    private List<String> firstName = new ArrayList<String>();
    private List<String> surName = new ArrayList<String>();
    private List<Guest> guests=new ArrayList<Guest>();
    //VARIABLES
    private int guestNum = 1;
    private final Node nPCNode;
    

    public GuestSpawner(Node nPCNode,AssetManager assetManager,EventBus eventBus) {
        this.nPCNode = nPCNode;
        this.assetManager = assetManager;
        this.eventBus=eventBus;
        r = new Random();
        eventBus.register(this);
        addNames();
    }
    public void setNpcs(List<BasicNPC> npcs){
        this.npcs=npcs;
    }
    public void setGuests(List<Guest> guests){
        this.guests=guests;
    }
    public void forceSpawnGuest(int n) {
        if (spawnpoints.isEmpty() == true) {
            logger.log(Level.SEVERE,"No spawnpoints for guests!");
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
        Spatial geom = assetManager.loadModel(LoadPaths.guest);
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
        String[]fn=new String[]{
            //M
            "John","Arnold","James","Ed","Matt","Mathias","Jack",
            "Bob","William","Tony","Ken","Sam","Elvis","Robert","Michael",
            "David","Daniel","Joseph","Thomas","Mark","Donald","George",
            "Will","Bill","Jeff","Ronald","Edward","Adam","Jason","Walter",
            //W
            "Mary","Patricia","Linda","Barbara","Elisabeth","Jennifer","Maria",
            "Kate","Susan","Margaret","Lisa","Nancy","Helen","Donna","Carol","Laura",
            "Ruth","Karen"
        
        };
        firstName=Arrays.asList(fn);
        String[]sn=new String[]{
            "Smith","Johnson","Williams","Jones","Brown","Davis","Miller","Wilson","Moore",
            "Taylor","Anderson","Thomas","Jackson","White","Harris","Martin","Thompson","Garcia",
            "Martinez","Robinson","Clark","Lee","Allen","Young","King","Scott"
        };
        surName=Arrays.asList(sn);

    }
    @Subscribe public void listenSetGuestSpawnPoints(SetGuestSpawnPointsEvent event){
        spawnpoints.addAll(event.getSpawnpoints());
        logger.log(Level.FINER,"{0} Spawnpoints added for guests.",event.getSpawnpoints().size());
    }
}
