/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package intopark.terrain;

import intopark.roads.RoadManager;
import intopark.terrain.events.DeleteSpatialFromMapEvent;
import com.google.common.eventbus.DeadEvent;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.jme3.light.Light;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.system.AppSettings;
import intopark.inout.LoadPaths;
import intopark.UtilityMethods;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import intopark.gameplayorgans.Scenario;
import intopark.inout.Identifier;
import intopark.npc.BasicNPC;
import intopark.npc.Guest;
import intopark.npc.NPCManager;
import intopark.npc.events.SetGuestSpawnPointsEvent;
import intopark.roads.Road;
import intopark.roads.RoadGraph;
import intopark.roads.Walkable;
import intopark.terrain.events.AddToRootNodeEvent;
import intopark.terrain.events.RefreshGroundEvent;
import intopark.terrain.events.SetMapDataEvent;
import intopark.util.MapPosition;

/**
 *
 * @author arska
 */
@Singleton
public class ParkHandler {
    //LOGGER
    private transient static final Logger logger = Logger.getLogger(ParkHandler.class.getName());
    //MISC
    public transient AppSettings settings;
    //DEPENDENCIES
    @Inject private transient NPCManager npcManager;
    @Inject private transient RoadManager roadMaker;
    @Inject private transient Identifier identifier;
    private transient EventBus eventBus;
    private transient Node rootNode;
    //OWNS
    private MapContainer map;
    private ParkWallet parkwallet = new ParkWallet(10000);
    private transient List<BasicNPC> npcs = new ArrayList<>();
    private List<Guest> guests = new ArrayList<>();
    private List<Road>roads; //THIS LIST IS ONLY FOR SAVING
    private Scenario scenario;
    //VARIABLES

    /**
     * ParkHandler is the Container object for your whole park.
     * When saving the game, this class is saved to JSON file.
     * This is used as an a layer to do all kinds of this in your park.
     * For example you can send an DemolishRideEvent from any other class.
     * Then this class will catch it and do the needed things with all the needed dependencies to destroy the ride.
     */
    public ParkHandler() {

    }
    @Inject
    public ParkHandler(Node rootNode, AppSettings settings,MapContainer map,EventBus eventBus) {

        this.settings = settings;
        this.rootNode=rootNode;
        this.map=map;
        this.eventBus=eventBus;
        eventBus.register(this);
    }
    /**
     * Critical initialization method.
     * This should be called when ParkHandler is fully populated (nothing is null).
     */
    public void onStartup() {
        //TODO: TIDY THIS METHOD
        logger.log(Level.FINEST,"Configuring ParkHandler..");
        //eventBus.post(new SetMapEvent(map.getMap()));
        eventBus.post(new RefreshGroundEvent()); //Force refresh the ground once.
        scenario.setUp();
        /* Set the indivitual parameters to child-Managers */
        npcManager.setNpcs(npcs);
        npcManager.setGuests(guests);
        npcManager.setMaxGuests(getMaxGuests());
        //Update money counter
        logger.log(Level.FINEST,"Configuring finished");
    }
    public void initializeSaving(){
        roads=new ArrayList<>();
        for(Walkable e:getRoadGraph().getRoadMap().vertexSet()){
            if(e instanceof Road){
                roads.add((Road)e);
            }
        }

    }
    public void postSave(){
        roads.clear();
    }

    /**
     * Test if the MapContainer Array is null in specified coords.
     * @param x coord.
     * @param y coord.
     * @param z coord.
     * @return TRUE if null | else FALSE
     */
    public boolean testForEmptyTile(int x, int y, int z) {
        return true; //TODO:
    }
    /**
     * Return guests list's size() if not null.
     * if guests is null it will return "1".
     * @return guests list's size()
     */
    public String getGuestSizeString() {
        if (guests == null) {
            return Integer.toString(1);
        }
        return Integer.toString(guests.size());
    }
    public Object getObjectWithID(int id){
        return identifier.getObjectWithID(id);
    }
    /**
     * EVENTBUS LISTENERS
     * These methods will get called when some other class posts these EventBus events.
     */

    /**
     * All events that don't catch anything. This is not supposed to happen ever.
     * @param event Event which was not delivered anywhere.
     */
    @Subscribe public void listenDeadEvents(DeadEvent event){
        logger.warning(String.format("%s was NOT delivered to its correct destination!",event.getEvent().toString()));

    }
    /**
     * TODO:
     * @param event
     */
    @Subscribe public void listenSetMapData(SetMapDataEvent event){
        map.setMapData(event.getMapData());
        logger.finest("MapData have been modified!");
    }
    /**
     * This will add a Spatial {@Link Spatial} to the mapContainer map array. TODO:
     * @param event
     */

    /**
     * This will delete Spatial {@Link Spatial} from the mapContainer map array. TODO:
     * @param event
     */
    @Subscribe public void listenDeleteSpatialFromMapEvent(DeleteSpatialFromMapEvent event){
        if(event.getO() != null){
            rootNode.detachChild(event.getO());
            logger.log(Level.FINEST,"Deleted {0} from rootNode", event.getO().toString());
        }else{
            logger.log(Level.FINEST,"Can't delete NULL spatial from rootNode.");
        }
    }
    /**
     * This will add Spatial or light to the rootNode (The world basically).
     * @param event
     */
    @Subscribe public void listenAddRootNodeEvent(AddToRootNodeEvent event){
        if(event.spatial instanceof Spatial){
            logger.log(Level.FINEST,"New Spatial added to rootNode");
            rootNode.attachChild((Spatial)event.spatial);
        }
        if(event.spatial instanceof Light){
            logger.log(Level.FINEST,"New Light added to rootNode");
            rootNode.addLight((Light)event.spatial);
        }
    }

    /**
     * GETTERS AND SETTERS
     */
    public Scenario getScenario() {
        return scenario;
    }
    public void setScenario(Scenario scenario) {
        this.scenario = scenario;
    }
    public void setMap(MapContainer map) {
        this.map = map;
    }
    public MapContainer getMap() {
        return map;
    }
    public int getMaxGuests() {
        return scenario.getMaxGuests();
    }

    public void setParkWallet(ParkWallet wallet) {
        this.parkwallet = wallet;
    }
    
    public void setNpcs(ArrayList<BasicNPC> npcs) {
        this.npcs = npcs;
    }
    public void setGuests(ArrayList<Guest> guests) {
        this.guests = guests;
    }
   
    public ParkWallet getParkWallet() {
        return parkwallet;
    }
    public List<BasicNPC> getNpcs() {
        return npcs;
    }
    public List<Guest> getGuests() {
        return guests;
    }
   
    public int getRideID() {
        return scenario.getRideID();
    }
    public int getShopID() {
        return scenario.getShopID();
    }
    public int getMapHeight() {
        return scenario.getMapHeight();
    }
    public int getMapWidth() {
        return scenario.getMapWidth();
    }
    public float[] getMapData() {
        return map.getMapData();
    }
    public String getParkName() {
        return scenario.getParkName();
    }
    public RoadGraph getRoadGraph(){
        return roadMaker.getRoadGraph();
    }

    public void setRoadMaker(RoadManager roadMaker) {
        this.roadMaker = roadMaker;
    }

    public void setNpcManager(NPCManager npcManager) {
        this.npcManager = npcManager;
    }

    public void setIdentifier(Identifier identifier) {
        this.identifier = identifier;
    }


}
