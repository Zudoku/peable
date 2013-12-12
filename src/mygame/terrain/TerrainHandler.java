/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.terrain;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.jme3.asset.AssetManager;
import com.jme3.collision.CollisionResult;
import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.jme3.texture.Texture;
import mygame.Gamestate;

/**
 *
 * @author arska
 */
@Singleton
public class TerrainHandler {

    public static final float HALFTILE = 0.4999f;
    
    Node gameNode;
    AssetManager assetManager;
    int TerrainMap[][];
    public Spatial[][][] map;
    public int brush = 3;
    public int mode = 2;
    
    public int textureindex = 1;
    public boolean useTexture = false;
    private final ParkHandler parkHandler;
    @Inject
    public TerrainHandler(Node rootNode, AssetManager assetManager,ParkHandler parkHandler) {
        this.gameNode = rootNode;
        this.assetManager = assetManager;
        this.parkHandler=parkHandler;
        
    }

    public Geometry TerrainBox() {

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

    public void lowerland(CollisionResult target) {
        float tarx = ((target.getContactPoint().x) - HALFTILE);
        float tarz = ((target.getContactPoint().z) - HALFTILE);

        switch (brush) {
            case 1:
                reloadLoweredLand((int) tarx, (int) tarz);

                break;

            case 2:
                reloadLoweredLand((int) tarx, (int) tarz);
                reloadLoweredLand((int) tarx + 1, (int) tarz);
                reloadLoweredLand((int) tarx, (int) tarz + 1);
                reloadLoweredLand((int) tarx + 1, (int) tarz + 1);
                break;


            case 3:
                reloadLoweredLand((int) tarx + 1, (int) tarz - 1);
                reloadLoweredLand((int) tarx + 1, (int) tarz);
                reloadLoweredLand((int) tarx + 1, (int) tarz + 1);

                reloadLoweredLand((int) tarx, (int) tarz - 1);
                reloadLoweredLand((int) tarx, (int) tarz);
                reloadLoweredLand((int) tarx, (int) tarz + 1);

                reloadLoweredLand((int) tarx - 1, (int) tarz - 1);
                reloadLoweredLand((int) tarx - 1, (int) tarz);
                reloadLoweredLand((int) tarx - 1, (int) tarz + 1);
                break;

            case 4:

                break;

        }




    }

    public void raiseland(CollisionResult target) {
        float tarx = ((target.getContactPoint().x - HALFTILE));
        float tarz = ((target.getContactPoint().z - HALFTILE));

        switch (brush) {
            case 1:
                reloadRaisedLand((int) tarx, (int) tarz);

                break;

            case 2:
                reloadRaisedLand((int) tarx, (int) tarz);
                reloadRaisedLand((int) tarx + 1, (int) tarz);
                reloadRaisedLand((int) tarx, (int) tarz + 1);
                reloadRaisedLand((int) tarx + 1, (int) tarz + 1);
                break;


            case 3:
                reloadRaisedLand((int) tarx + 1, (int) tarz - 1);
                reloadRaisedLand((int) tarx + 1, (int) tarz);
                reloadRaisedLand((int) tarx + 1, (int) tarz + 1);

                reloadRaisedLand((int) tarx, (int) tarz - 1);
                reloadRaisedLand((int) tarx, (int) tarz);
                reloadRaisedLand((int) tarx, (int) tarz + 1);

                reloadRaisedLand((int) tarx - 1, (int) tarz - 1);
                reloadRaisedLand((int) tarx - 1, (int) tarz);
                reloadRaisedLand((int) tarx - 1, (int) tarz + 1);
                break;

            case 4:

                break;

        }







    }

    public void reloadRaisedLand(int x, int z) {
        if(x<0||z<0||x>99||z>99){
            return;
        }
        if(!parkHandler.getParkWallet().canAfford(10)){
           return; 
        }
        Spatial deleted = map[x][0][z];
        TerrainMap[x][z]++;

        Geometry geomclone = TerrainBox();
        if (useTexture == true) {
            if (textureindex == 1) {
                Texture grass = assetManager.loadTexture(
                        "Textures/grasstexture.png");
                geomclone.getMaterial().setTexture("ColorMap", grass);

            }
            if (textureindex == 2) {
                Texture rock = assetManager.loadTexture(
                        "Textures/rocktexture.png");
                geomclone.getMaterial().setTexture("ColorMap", rock);

            }
        }
        
        geomclone.setLocalScale((new Vector3f(1, TerrainMap[x][z], 1)));
        geomclone.setLocalTranslation(1, geomclone.getLocalTranslation().z + ((float) TerrainMap[x][z] / 2), 1);
        geomclone.move(x, 0, z);
        //laita mappiin terraini
        map[x][0][z] = geomclone;
        gameNode.detachChild(deleted);
        gameNode.attachChild(geomclone);
        parkHandler.getParkWallet().remove(10);
        Gamestate.ingameHUD.updateMoneytextbar();

    }

    public void reloadLoweredLand(int x, int z) {
        if(x<0||z<0||x>99||z>99){
            return;
        }
        if(!parkHandler.getParkWallet().canAfford(10)){
           return; 
        }
        Spatial deleted = map[x][0][z];
        TerrainMap[x][z]--;

        Geometry geomclone = TerrainBox();
        if (useTexture == true) {
            if (textureindex == 1) {
                Texture grass = assetManager.loadTexture(
                        "Textures/grasstexture.png");
                geomclone.getMaterial().setTexture("ColorMap", grass);

            }
            if (textureindex == 2) {
                Texture rock = assetManager.loadTexture(
                        "Textures/rocktexture.png");
                geomclone.getMaterial().setTexture("ColorMap", rock);

            }
        }
        geomclone.setLocalScale((new Vector3f(1, TerrainMap[x][z], 1)));
        geomclone.setLocalTranslation(1, geomclone.getLocalTranslation().z + ((float) TerrainMap[x][z] / 2), 1);
        geomclone.move(x, 0, z);
        map[x][0][z] = geomclone;
        gameNode.detachChild(deleted);
        gameNode.attachChild(geomclone);
        parkHandler.getParkWallet().remove(10);
        Gamestate.ingameHUD.updateMoneytextbar();

    }

    public void setBrush(int s) {
        brush = s;
    }
}
