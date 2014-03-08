/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.terrain;

import mygame.terrain.events.PayParkEvent;
import mygame.terrain.events.RideDemolishEvent;
import mygame.terrain.events.DeleteSpatialFromMapEvent;
import mygame.terrain.events.AddObjectToMapEvent;
import com.google.common.eventbus.DeadEvent;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.system.AppSettings;
import java.util.ArrayList;
import java.util.logging.Logger;
import mygame.npc.BasicNPC;
import mygame.npc.Guest;
import mygame.npc.NPCManager;
import mygame.ride.BasicRide;
import mygame.ride.RideManager;
import mygame.shops.BasicShop;
import mygame.shops.ShopDemolishEvent;
import mygame.shops.ShopManager;
import mygame.terrain.events.RefreshGroundEvent;
import mygame.terrain.events.SetMapDataEvent;

/**
 *
 * @author arska
 */
@Singleton
public class ParkHandler {
    //LOGGER
    private static final Logger logger = Logger.getLogger(ParkHandler.class.getName());
    //MISC
    public AppSettings settings;
    //DEPENDENCIES
    @Inject private NPCManager npcManager;
    @Inject private RoadMaker roadMaker;
    @Inject private ShopManager shopManager;
    @Inject private RideManager rideManager;
    private final EventBus eventBus;
    private final Node rootNode;
    //OWNS
    private MapContainer map;
    private ParkWallet parkwallet = new ParkWallet(10000);
    private ArrayList<BasicRide> rides=new ArrayList<BasicRide>();
    private ArrayList<BasicNPC> npcs=new ArrayList<BasicNPC>();
    private ArrayList<BasicShop> shops=new ArrayList<BasicShop>();
    private ArrayList<Guest> guests=new ArrayList<Guest>();
    private ArrayList<Vector3f>RoadtoUpdatePositions=new ArrayList<Vector3f>();
    private ArrayList<Spatial> queRoadsToUpdate=new ArrayList<Spatial>();
    //VARIABLES
    private String parkName = "defaultparkname";
    private int rideID;
    private int shopID;
    private int mapHeight;
    private int mapWidth;
    private int maxGuests;
    
    @Inject
    public ParkHandler(Node rootNode, AppSettings settings,MapContainer map,EventBus eventBus) {
        
        this.settings = settings;
        this.rootNode=rootNode;
        this.map=map;
        this.eventBus=eventBus;
        eventBus.register(this);
        
    }

    public void setUp(String parkname, int rideID, int shopID, ParkWallet wallet,int maxGuests) {
        this.parkName = parkname;
        this.rideID = rideID;
        this.shopID = shopID;
        this.parkwallet = wallet;
        this.maxGuests=maxGuests;
    }

    public String getGuestSizeString() {
        if (guests == null) {
            return Integer.toString(1);
        }
        return Integer.toString(guests.size());
    }

    

    public String getParkName() {
        return parkName;
    }

    public void onStartup() {
        logger.finest("Setting Map");
        //eventBus.post(new SetMapEvent(map.getMap()));
        eventBus.post(new RefreshGroundEvent());
        roadMaker.roadsToUpdate(RoadtoUpdatePositions);
        roadMaker.queRoadsToUpdate(queRoadsToUpdate);
        rideManager.rides = rides;
        npcManager.npcs = npcs;
        npcManager.guestSpawner.setNpcs(npcs);
        npcManager.guests = this.guests;
        npcManager.guestSpawner.setGuests(guests);
        npcManager.setMaxGuests(maxGuests);
        shopManager.shops = shops;
        rideManager.setRideID(rideID);
        shopManager.setShopID(shopID);
    }
    /**
     * Called only once in start
     * @param mapu
     * @param mapdata 
     */
    public void setMap(Spatial[][][] mapu, float[] mapdata) {
        map.setMap(mapu);
        map.setMapData(mapdata);
        
    }

    public void setParkWallet(ParkWallet wallet) {
        this.parkwallet = wallet;
    }

    public void setRides(ArrayList<BasicRide> rides) {
        this.rides = rides;


    }

    public void setNpcs(ArrayList<BasicNPC> npcs) {
        this.npcs = npcs;


    }

    public void setGuests(ArrayList<Guest> guests) {
        this.guests = guests;


    }

    public void setShops(ArrayList<BasicShop> shops) {
        this.shops = shops;


    }

    public void setRideID(int rideID) {
        this.rideID = rideID;


    }

    public void setShopID(int shopID) {
        this.shopID = shopID;

    }
    public void setMaxGuests(int maxGuests){
        this.maxGuests=maxGuests;
    }

    public void setMapSize(int height, int width) {
        this.mapHeight = height;
        this.mapWidth = width;


    }


    public ParkWallet getParkWallet() {
        return parkwallet;
    }

    public ArrayList<BasicNPC> getNpcs() {
        return npcs;
    }

    public ArrayList<Guest> getGuests() {
        return guests;
    }

    public ArrayList<BasicShop> getShops() {
        return shops;
    }

    public ArrayList<BasicRide> getRides() {
        return rides;
    }

    public int getRideID() {
        return rideID;
    }

    public int getShopID() {
        return shopID;
    }

    public int getMapHeight() {
        return mapHeight;
    }

    public int getMapWidth() {
        return mapWidth;
    }

    public float[] getMapData() {
        return map.getMapData();
    }

    public void setUpdatedRoadsList(ArrayList<Vector3f> pos) {
        this.RoadtoUpdatePositions=pos;
    }

    public void setUpdatedQueRoadsList(ArrayList<Spatial> queRoadstoUpdate) {
        this.queRoadsToUpdate=queRoadstoUpdate;
    }
    @Subscribe public void listenDeadEvents(DeadEvent event){
        logger.warning(String.format("%s was NOT delivered to its correct destination!",event.getEvent().toString()));
        
    }
    @Subscribe public void listenRideDemolishEvent(RideDemolishEvent event){
        Node rideNode=(Node)rootNode.getChild("rideNode");

        event.getRide().detachFromNode(rideNode);
        event.getRide().detachFromNode(rootNode);
        
        for(int y=0;y<25;y++){
            for(int x=0;x<getMapHeight();x++){
                for(int z=0;z<getMapWidth();z++){
                    if(map.getMap()[x][y][z]!=null){
                        for(Spatial s:event.getRide().getAllSpatialsFromRide()){
                            if(map.getMap()[x][y][z]==s){
                                map.getMap()[x][y][z]=null;
                            }
                        } 
                    }
                }
            }
        }
        rides.remove(event.getRide());
    }
    @Subscribe public void listenShopDemolishEvent(ShopDemolishEvent event){
        Node shopNode=(Node)rootNode.getChild("shopNode");
        shopNode.detachChild(event.getShop().getGeometry());
        rootNode.detachChild(event.getShop().getGeometry());
        
        for(int y=0;y<25;y++){
            for(int x=0;x<getMapHeight();x++){
                for(int z=0;z<getMapWidth();z++){
                    if(map.getMap()[x][y][z]!=null){
                        if(map.getMap()[x][y][z]==event.getShop().getGeometry()){
                            map.getMap()[x][y][z]=null;
                        }
                    }
                }
            }
        }
        
        getShops().remove(event.getShop());
    }
    @Subscribe public void listenPayParkEvents(PayParkEvent event){
        parkwallet.add(event.getAmount());
        logger.finest(String.format("%s Added to your parks account!", event.getAmount()));
    }
    @Subscribe public void listenSetMapData(SetMapDataEvent event){
        map.setMapData(event.getMapData());
        logger.finest("MapData have been modified!");
    }
    @Subscribe public void listenAddSpatialToMap(AddObjectToMapEvent event){
        
        map.getMap()[event.getX()][event.getY()][event.getZ()]=event.getO();
    }
    @Subscribe public void listenDeleteSpatialFromMapEvent(DeleteSpatialFromMapEvent event){
        for(int y=0;y<25;y++){
            for(int x=0;x<getMapHeight();x++){
                for(int z=0;z<getMapWidth();z++){
                    if(map.getMap()[x][y][z]!=null){
                        if(map.getMap()[x][y][z]==event.getO()){
                            map.getMap()[x][y][z]=null;
                            if (event.getO().getUserData("type").equals("road")) {
                                    roadMaker.updateroads(x, y, z);
                                    roadMaker.updateroads(x, y+1, z);
                                    roadMaker.updateroads(x, y-1, z);
                                }
                        }
                    }
                }
            }
        }
    }

    public int getMaxGuests() {
        return maxGuests;
    }
    public Spatial getSpatialAt(int x,int y,int z){
        Spatial found;
        found=map.getMap()[x][y][z];
        return found;
    }
    public boolean testForEmptyTile(int x,int y,int z){
        if(map.getMap()[x][y][z]!=null){
            return false;
        }else{
            return true;
        }
    }
    
}
