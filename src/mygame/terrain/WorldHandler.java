/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.terrain;

import com.jme3.asset.AssetManager;
import com.jme3.collision.CollisionResult;
import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;
import com.jme3.texture.Texture;
import mygame.GUI.IngameHUD;

/**
 *
 * @author arska
 */
public class WorldHandler {

    public static final float HALFTILE = 0.4999f;
    public static final int TERRAINHEIGHT = 101;
    public static final int TERRAINWIDTH = 101;
    Node gameNode;
    AssetManager assetManager;
    int TerrainMap[][] = new int[TERRAINHEIGHT][TERRAINWIDTH];
    Geometry Terrain[][] = new Geometry[TERRAINHEIGHT][TERRAINWIDTH];
    public int brush = 3;
    public int mode = 2;
    private final IngameHUD ingameHUD;
    public int textureindex = 1;
    public boolean useTexture = false;

    public WorldHandler(Node rootNode, AssetManager assetManager, IngameHUD ingameHUD) {
        this.gameNode = rootNode;
        this.assetManager = assetManager;
        this.ingameHUD = ingameHUD;


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

    public void makeGround() {




        for (int x = 0; x < 101; x++) {
            for (int y = 0; y < 101; y++) {

                TerrainMap[x][y] = 5;
            }
        }


        for (int x = 0; x < 100; x++) {
            for (int y = 0; y < 100; y++) {

                Geometry geomclone = TerrainBox();
                geomclone.setLocalScale((new Vector3f(1, (int) TerrainMap[x][y], 1)));

                geomclone.setLocalTranslation(1, geomclone.getLocalTranslation().y + ((float) TerrainMap[x][y] / 2), 1);
                geomclone.move(x, 1, y);
                geomclone.setName("Terrain");

                Terrain[x][y] = geomclone;
                gameNode.attachChild(geomclone);

            }

        }
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
        Geometry deleted = Terrain[x][z];
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
        geomclone.move(x, 1, z);
        Terrain[x][z] = geomclone;
        gameNode.detachChild(deleted);
        gameNode.attachChild(geomclone);


    }

    public void reloadLoweredLand(int x, int z) {
        Geometry deleted = Terrain[x][z];
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
        geomclone.move(x, 1, z);
        Terrain[x][z] = geomclone;
        gameNode.detachChild(deleted);
        gameNode.attachChild(geomclone);


    }

    public void setBrush(int s) {
        brush = s;
    }
}
