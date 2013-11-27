/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.terrain;

import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.system.AppSettings;
import java.util.ArrayList;
import mygame.Gamestate;
import mygame.Main;
import mygame.npc.BasicNPC;
import mygame.npc.Guest;
import mygame.ride.BasicRide;
import mygame.shops.BasicShop;

/**
 *
 * @author arska
 */
public class ParkHandler {

    private Spatial[][][] map;
    public MapFactory mapfactory;
    private ParkWallet parkwallet;
    private ArrayList<BasicRide> rides;
    private ArrayList<BasicNPC> npcs;
    private ArrayList<BasicShop> shops;
    private ArrayList<Guest> guests;
    private String parkName="defaultparkname";
    public AppSettings settings;
    private int rideID;
    private int shopID;
    private int mapHeight;
    private int mapWidth;
    private int[][]mapData;

    public ParkHandler(Node rootNode,AppSettings settings) {
        mapfactory = new MapFactory(rootNode);
        this.settings=settings;
    }

   public String getGuestSizeString(){
       if(guests==null){
           return Integer.toString(1);
       }
       return Integer.toString(guests.size());
   }

    public void loadDebugPlain() {
        mapfactory.setCurrentMapPlain();
    }
    public String getParkName(){
        return parkName;
    }

    public void setMap(Spatial[][][] map,int[][] mapdata) {
        
        this.map = map;
        this.mapData=mapdata;
        //anna kaikille map joka sitä tarvii
        Gamestate.roadMaker.map=map;
        //managerilla joka muokkaa maata
        Gamestate.worldHandler.map=map;
        Gamestate.worldHandler.TerrainMap=mapdata;
    }
    public void setParkWallet(ParkWallet wallet){
        this.parkwallet=wallet;
    }
    public void setRides(ArrayList<BasicRide> rides) {
        this.rides = rides;
        
        Main.gamestate.rideManager.rides=rides;
    }

    public void setNpcs(ArrayList<BasicNPC> npcs) {
        this.npcs = npcs;
        
        Main.gamestate.npcManager.npcs=npcs;
        Main.gamestate.npcManager.guestSpawner.setNpcs(npcs);
    }

    public void setGuests(ArrayList<Guest> guests) {
        this.guests = guests;
        
        Main.gamestate.npcManager.guests=this.guests;
        Main.gamestate.npcManager.guestSpawner.setGuests(guests);
    }

    public void setShops(ArrayList<BasicShop> shops) {
        this.shops = shops;
        
        Main.gamestate.shopManager.shops=shops;
    }
    public void setRideID(int rideID){
        this.rideID=rideID;
        
        Gamestate.rideManager.setRideID(rideID);
    }
    public void setShopID(int shopID){
        this.shopID=shopID;
        Gamestate.shopManager.setShopID(shopID);
    }
    public void setMapSize(int height,int width){
        this.mapHeight=height;
        this.mapWidth=width;
        
        
    }
    /**
     * käytä vain tilanteissa jossa map ei voida antaa suoraan sen luodessa
     * @return mapin
     */
    public Spatial[][][]getMap(){
        return map;
    }
    public ParkWallet getParkWallet(){
        return parkwallet;
    }
    public ArrayList<BasicNPC> getNpcs(){
        return npcs;
    }
    public ArrayList<Guest> getGuests(){
        return guests;
    }
    public ArrayList<BasicShop> getShops(){
        return shops;
    }
    public ArrayList<BasicRide> getRides(){
        return rides;
    }
    public int getRideID(){
        return rideID;
    }
    public int getShopID(){
        return shopID;
    }
    public int getMapHeight(){
        return mapHeight;
    }
    public int getMapWidth(){
        return mapWidth;
    }
    public int[][] getMapData(){
        return mapData;
    }
    
}
