/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.terrain;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.jme3.texture.Texture;
import java.util.ArrayList;
import mygame.npc.BasicNPC;
import mygame.npc.Guest;
import mygame.ride.BasicRide;
import mygame.shops.BasicShop;

/**
 *
 * @author arska
 */
@Singleton
public class MapFactory {
    private final int Mapheight=101; //-1 ==100
    private final int Mapwidth=101;  //-1 ==100
    private final ParkHandler parkHandler;
    @Inject TerrainHandler terrainHandler;
    
    @Inject
    public MapFactory(ParkHandler parkHandler){
        this.parkHandler=parkHandler;
    }
    public void setCurrentMapPlain(){
        parkHandler.setMap(getPlainMap(Mapheight, Mapwidth),terrainHandler.getHeightMap());
        parkHandler.setNpcs(new ArrayList<BasicNPC>());
        parkHandler.setGuests(new ArrayList<Guest>());
        parkHandler.setRides(new ArrayList<BasicRide>());
        parkHandler.setShops(new ArrayList<BasicShop>());
        parkHandler.setRideID(1);
        parkHandler.setShopID(1);
        parkHandler.setMapSize(Mapheight, Mapwidth);
        parkHandler.setParkWallet(new ParkWallet(10000));
        parkHandler.setMaxGuests(20);
    }
   
    
    public Spatial[][][] getPlainMap(int height,int width){
        Spatial[][][] maps=new Spatial[height][25][width];
        return maps;
    }
}
