/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.terrain;

import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
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
public class MapFactory {
    int Mapheight=101; //-1 ==100
    int Mapwidth=101;  //-1 ==100
    private final Node rootNode;
    public MapFactory(Node rootNode){
        this.rootNode=rootNode;
    }
    public void setCurrentMapPlain(){
        Main.currentPark.setMap(getPlainMap(Mapheight, Mapwidth),getPlainMapData(Mapheight, Mapwidth,6));
        //todo later SAVING AND READING TO HERE
        Main.currentPark.setNpcs(new ArrayList<BasicNPC>());
        Main.currentPark.setGuests(new ArrayList<Guest>());
        Main.currentPark.setRides(new ArrayList<BasicRide>());
        Main.currentPark.setShops(new ArrayList<BasicShop>());
        Main.currentPark.setRideID(1);
        Main.currentPark.setShopID(1);
        Main.currentPark.setMapSize(Mapheight, Mapwidth);
        Main.currentPark.setParkWallet(new ParkWallet(10000));
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

                Geometry geomclone = Main.gamestate.worldHandler.TerrainBox();
                geomclone.setLocalScale((new Vector3f(1, (int) TerrainMap[x][y], 1)));

                geomclone.setLocalTranslation(1, geomclone.getLocalTranslation().y + ((float) TerrainMap[x][y] / 2), 1);
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
}
