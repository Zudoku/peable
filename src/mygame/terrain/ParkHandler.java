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
import mygame.Gamestate;
import mygame.Main;
import mygame.npc.BasicNPC;
import mygame.npc.Guest;
import mygame.npc.NPCManager;
import mygame.ride.BasicRide;
import mygame.shops.BasicShop;

/**
 *
 * @author arska
 */
@Singleton
public class ParkHandler {

    private Spatial[][][] map;
    public MapFactory mapfactory;
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
    private final TerrainHandler worldHandler;
    private ArrayList<Spatial> queRoadsToUpdate=new ArrayList<Spatial>();
    private final NPCManager npcManager;
    @Inject
    public ParkHandler(Node rootNode, AppSettings settings,TerrainHandler terrainHandler,MapFactory mapFactory,NPCManager npcManager) {
        mapfactory =mapFactory;
        this.settings = settings;
        this.worldHandler=terrainHandler;
        this.npcManager=npcManager;
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

    public void loadDebugPlain() {
        mapfactory.setCurrentMapPlain();
    }

    public String getParkName() {
        return parkName;
    }

    public void onStartup() {
        //anna kaikille map joka sitä tarvii
        Gamestate.roadMaker.map = map;
        
        //managerilla joka muokkaa maata
        worldHandler.map = map;
        worldHandler.TerrainMap = mapData;
        Gamestate.roadMaker.roadsToUpdate(RoadtoUpdatePositions);
        Gamestate.roadMaker.queRoadsToUpdate(queRoadsToUpdate);
        Main.gamestate.rideManager.rides = rides;
        npcManager.npcs = npcs;
        npcManager.guestSpawner.setNpcs(npcs);
        npcManager.guests = this.guests;
        npcManager.guestSpawner.setGuests(guests);
        Main.gamestate.shopManager.shops = shops;
        Gamestate.rideManager.setRideID(rideID);
        Gamestate.shopManager.setShopID(shopID);
    }

    public void setMap(Spatial[][][] map, int[][] mapdata) {

        this.map = map;
        this.mapData = mapdata;

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

    /**
     * käytä vain tilanteissa jossa map ei voida antaa suoraan sen luodessa
     *
     * @return mapin
     */
    public Spatial[][][] getMap() {
        return map;
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
