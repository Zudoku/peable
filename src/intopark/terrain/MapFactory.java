/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package intopark.terrain;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import java.util.ArrayList;
import java.util.logging.Logger;
import intopark.npc.BasicNPC;
import intopark.npc.Guest;
import intopark.ride.BasicRide;
import intopark.shops.BasicShop;

/**
 *
 * @author arska
 */
@Singleton
public class MapFactory {
    private static final Logger logger = Logger.getLogger(MapFactory.class.getName());
    private final int Mapheight=101; //-1 ==100
    private final int Mapwidth=101;  //-1 ==100
    private final ParkHandler parkHandler;
    @Inject TerrainHandler terrainHandler;
    private Node rootNode;
    
    @Inject
    public MapFactory(ParkHandler parkHandler,Node rootNode){
        this.parkHandler=parkHandler;
        this.rootNode=rootNode;
        //TODO: DELETE THIS CLASS BECAUSE ITS DEPRECATED.
    }
    public void setCurrentMapPlain(){
        parkHandler.setMap(getPlainMap(Mapheight, Mapwidth),terrainHandler.getHeightMap());
        parkHandler.setNpcs(new ArrayList<BasicNPC>());
        parkHandler.setGuests(new ArrayList<Guest>());
        parkHandler.setRides(new ArrayList<BasicRide>());
        parkHandler.setShops(new ArrayList<BasicShop>());
        parkHandler.getScenario().setRideID(1);
        parkHandler.getScenario().setShopID(1);
        parkHandler.getScenario().setMapSize(Mapheight, Mapwidth);
        parkHandler.setParkWallet(new ParkWallet(10000));
        parkHandler.getScenario().setMaxGuests(20);
        DirectionalLight sun = new DirectionalLight();
        sun.setDirection((new Vector3f(0.5f, -0.5f, 0.5f).normalizeLocal()));
        sun.setColor(ColorRGBA.White);
        rootNode.addLight(sun);
        DirectionalLight sun2 = new DirectionalLight();
        sun2.setDirection((new Vector3f(-0.5f, 0.5f, -0.5f).normalizeLocal()));
        sun2.setColor(ColorRGBA.White);
        rootNode.addLight(sun2); 
        
        AmbientLight l=new AmbientLight();
        l.setColor(new ColorRGBA(1,1,1,300));
        
        rootNode.addLight(l);
    }
   
    
    public Spatial[][][] getPlainMap(int height,int width){
        Spatial[][][] maps=new Spatial[height][25][width];
        return maps;
    }
}
