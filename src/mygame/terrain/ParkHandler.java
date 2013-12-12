/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.terrain;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.system.AppSettings;
import java.util.ArrayList;
import mygame.npc.BasicNPC;
import mygame.npc.Guest;
import mygame.npc.NPCManager;
import mygame.ride.BasicRide;
import mygame.ride.RideManager;
import mygame.shops.BasicShop;
import mygame.shops.ShopManager;

/**
 *
 * @author arska
 */
@Singleton
public class ParkHandler {

    private MapContainer map;
    
    private ParkWallet parkwallet = new ParkWallet(10000);
    private ArrayList<BasicRide> rides=new ArrayList<BasicRide>();
    private ArrayList<BasicNPC> npcs=new ArrayList<BasicNPC>();
    private ArrayList<BasicShop> shops=new ArrayList<BasicShop>();
    private ArrayList<Guest> guests=new ArrayList<Guest>();
    private String parkName = "defaultparkname";
    public AppSettings settings;
    private int rideID;
    private int shopID;
    private int mapHeight;
    private int mapWidth;
    private int[][] mapData;
    private ArrayList<Vector3f>RoadtoUpdatePositions=new ArrayList<Vector3f>();
    
    
    private ArrayList<Spatial> queRoadsToUpdate=new ArrayList<Spatial>();
    @Inject private NPCManager npcManager;
    @Inject private RoadMaker roadMaker;
    @Inject private ShopManager shopManager;
    @Inject private RideManager rideManager;
    @Inject
    public ParkHandler(Node rootNode, AppSettings settings,MapContainer map) {
        
        this.settings = settings;
        this.map=map;
    }

    public void setUp(String parkname, int rideID, int shopID, ParkWallet wallet) {
        this.parkName = parkname;
        this.rideID = rideID;
        this.shopID = shopID;
        this.parkwallet = wallet;
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
        
        System.out.println("Setting Map!");
        
        roadMaker.roadsToUpdate(RoadtoUpdatePositions);
        roadMaker.queRoadsToUpdate(queRoadsToUpdate);
        rideManager.rides = rides;
        npcManager.npcs = npcs;
        npcManager.guestSpawner.setNpcs(npcs);
        npcManager.guests = this.guests;
        npcManager.guestSpawner.setGuests(guests);
        shopManager.shops = shops;
        rideManager.setRideID(rideID);
        shopManager.setShopID(shopID);
    }

    public void setMap(Spatial[][][] mapu, int[][] mapdata) {

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

    public int[][] getMapData() {
        return mapData;
    }

    public void setUpdatedRoadsList(ArrayList<Vector3f> pos) {
        this.RoadtoUpdatePositions=pos;
    }

    public void setUpdatedQueRoadsList(ArrayList<Spatial> queRoadstoUpdate) {
        this.queRoadsToUpdate=queRoadstoUpdate;
    }
}
