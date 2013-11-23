/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.terrain;

import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.system.AppSettings;
import java.util.ArrayList;
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

    Spatial[][][] map;
    public MapFactory mapfactory;
    ParkWallet parkwallet = new ParkWallet(10000);
    ArrayList<BasicRide> rides;
    ArrayList<BasicNPC> npcs;
    ArrayList<BasicShop> shops;
    ArrayList<Guest> guests;
    public AppSettings settings;

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

    public void setMap(Spatial[][][] map,int[][] mapdata) {
        
        this.map = map;
        //anna kaikille map joka sitä tarvii
        Main.gamestate.roadMaker.map=map;
        //managerilla joka muokkaa maata
        Main.gamestate.worldHandler.map=map;
        Main.gamestate.worldHandler.TerrainMap=mapdata;
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
    
}
