/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package intopark.npc;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.jme3.asset.AssetManager;
import com.jme3.scene.Spatial;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import intopark.UtilityMethods;
import intopark.inout.Identifier;
import intopark.inout.LoadPaths;
import intopark.npc.events.CreateGuestEvent;
import intopark.npc.events.SetGuestSpawnPointsEvent;
import intopark.npc.inventory.Item;
import intopark.npc.inventory.StatManager;
import intopark.npc.inventory.Wallet;
import intopark.ride.RideColor;
import intopark.terrain.ParkHandler;
import intopark.util.Direction;
import intopark.util.MapPosition;
import java.util.Arrays;

/**
 *
 * @author arska
 */
@Singleton
public class GuestSpawner {
    //LOGGER
    private static final Logger logger = Logger.getLogger(GuestSpawner.class.getName());
    //DEPENDENCIES
    private Random r;
    private final AssetManager assetManager;
    private final EventBus eventBus;
    private Identifier identifier;
    //OWNS
    private List<MapPosition> spawnpoints = new ArrayList<>();
    private List<BasicNPC> npcs = new ArrayList<>();
    private List<String> firstNameMale = new ArrayList<>();
    private List<String> firstNameFemale = new ArrayList<>();
    private List<String> surName = new ArrayList<>();
    private List<Guest> guests=new ArrayList<>();
    private ParkHandler parkHandler;
    //VARIABLES

    @Inject
    public GuestSpawner(AssetManager assetManager,EventBus eventBus,ParkHandler parkHandler,Identifier identifier) {
        this.assetManager = assetManager;
        this.eventBus=eventBus;
        this.parkHandler=parkHandler;
        this.identifier=identifier;
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
    public void forceSpawnGuest() {
        if (spawnpoints.isEmpty() == true) {
            logger.log(Level.SEVERE,"No spawnpoints for guests!");
            return;
        }
        //We choose random gender and height
        boolean male=r.nextBoolean();
        double walkingSpeed=(r.nextDouble()*4+3)*2;
        //We choose a random name
        String name;
        if(male){
            int num = r.nextInt(firstNameMale.size() - 1);
            name=firstNameMale.get(num);
        }else{
            int num = r.nextInt(firstNameFemale.size() - 1);
            name=firstNameFemale.get(num);
        }
        int num2 = r.nextInt(surName.size() - 1);
        name = name.concat(" ".concat(surName.get(num2)));
        //random shirt color
        RideColor color=RideColor.values()[r.nextInt(RideColor.values().length-1)];

        //Semi-random money
        float money = r.nextInt(30);
        money = money + 35;
        Wallet wallet=new Wallet(money);
        //Load the model
        Spatial geom = UtilityMethods.loadModel(LoadPaths.guest);
        //Random stats
        StatManager stats=new StatManager();
        stats.randomize();
        //Random spawnpoint
        int spindex = r.nextInt(spawnpoints.size());
        MapPosition pos=spawnpoints.get(spindex);
        int ID = identifier.reserveID();
        //Create an event and post it forward.
        CreateGuestEvent event=new CreateGuestEvent(wallet,new ArrayList<Item>(),ID,
        Direction.NORTH,pos.getX(),pos.getY(),pos.getZ(),stats, geom, name,parkHandler,male,walkingSpeed,color);
        eventBus.post(event);

    }


    private void addNames() {
        String[]fnM=new String[]{
            //M
            "John","Arnold","James","Ed","Matt","Mathias","Jack",
            "Bob","William","Tony","Ken","Sam","Elvis","Robert","Michael",
            "David","Daniel","Joseph","Thomas","Mark","Donald","George",
            "Will","Bill","Jeff","Ronald","Edward","Adam","Jason","Walter"
        };
        String[]fnF=new String[]{
            //W
            "Mary","Patricia","Linda","Barbara","Elisabeth","Jennifer","Maria",
            "Kate","Susan","Margaret","Lisa","Nancy","Helen","Donna","Carol","Laura",
            "Ruth","Karen"

        };
        firstNameMale=Arrays.asList(fnM);
        firstNameFemale=Arrays.asList(fnF);
        String[]sn=new String[]{
            "Smith","Johnson","Williams","Jones","Brown","Davis","Miller","Wilson","Moore",
            "Taylor","Anderson","Thomas","Jackson","White","Harris","Martin","Thompson","Garcia",
            "Martinez","Robinson","Clark","Lee","Allen","Young","King","Scott"
        };
        surName=Arrays.asList(sn);

    }
    @Subscribe public void listenSetGuestSpawnPoints(SetGuestSpawnPointsEvent event){
        spawnpoints.addAll(event.getSpawnpoints());
        String locations="";
        for(MapPosition m: event.getSpawnpoints()){
            locations+= (m.toString()+" ");
        }
        logger.log(Level.FINER,"{0} Spawnpoints added for guests at {1}",new Object[]{event.getSpawnpoints().size(),locations});
    }

    public List<Guest> getGuests() {
        return guests;
    }

}
