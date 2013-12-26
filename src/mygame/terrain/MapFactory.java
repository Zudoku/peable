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
    int Mapheight=101; //-1 ==100
    int Mapwidth=101;  //-1 ==100
    private final Node rootNode;
    
    private final ParkHandler parkHandler;
    private final MapContainer map;
    private final AssetManager assetManager;
    @Inject
    public MapFactory(Node rootNode,ParkHandler parkHandler,MapContainer map,AssetManager assetManager){
        this.rootNode=rootNode;
        this.assetManager=assetManager;
        this.parkHandler=parkHandler;
        this.map=map;
    }
    public void setCurrentMapPlain(){
        parkHandler.setMap(getPlainMap(Mapheight, Mapwidth),getPlainMapData(Mapheight, Mapwidth,6));

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
        Spatial[][][] map=new Spatial[height][25][width];
        int[][] TerrainMap=new int[height][width];
        for (int x = 0; x < height; x++) {
            for (int y = 0; y < width; y++) {
                TerrainMap[x][y] = 6;
            }
        }


        for (int x = 0; x < 100; x++) {
            for (int y = 0; y < 100; y++) {

                Geometry geomclone =TerrainBox();
                geomclone.setLocalScale((new Vector3f(1, (int) TerrainMap[x][y], 1)));

                geomclone.setLocalTranslation(1, geomclone.getLocalTranslation().y + ((float) TerrainMap[x][y] / 2), 1);
                System.out.println(geomclone.getLocalScale());
                geomclone.move(x, 0, y);
                geomclone.setName("Terrain");
                geomclone.setUserData("type","terrain");

                map[x][0][y] = geomclone;
                rootNode.attachChild(geomclone);

            }

        }
        
        return map;
    }

    private int[][] getPlainMapData(int Mapheight, int Mapwidth,int mapZ) {
        int[][]data=new int[Mapheight][Mapwidth];
        for(int x=0;x<Mapheight;x++){
            for(int y=0;y<Mapwidth;y++){
                data[x][y]=mapZ;
            }
        }
        return data;
    }
    //TODO DELETE THIS
    private Geometry TerrainBox() {

        Box b = new Box(Vector3f.ZERO, 0.5f, 0.5f, 0.5f);
        Geometry geom = new Geometry("Terrain", b);
        Texture grass = assetManager.loadTexture(
                "Textures/grasstexture.png");
        Material mat = new Material(assetManager,
                "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setTexture("ColorMap", grass);
        geom.setMaterial(mat);
        return geom;
    }
}
