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

/**
 *
 * @author arska
 */
public class WorldHandler {
    private static final double HALFTILE = 0.4999;
    public static final int TERRAINHEIGHT=101;
    public static final int TERRAINWIDTH=101;

    Node gameNode;
    AssetManager assetManager;
    int TerrainMap[][] = new int[TERRAINHEIGHT][TERRAINWIDTH];
    Geometry Terrain[][]=new Geometry[TERRAINHEIGHT][TERRAINWIDTH];
    public int brush = 3;
    public int mode = 2;

    public WorldHandler(Node rootNode, AssetManager assetManager) {
        this.gameNode = rootNode;
        this.assetManager = assetManager;


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

                Terrain[x][y]=geomclone;
                gameNode.attachChild(geomclone);

            }

        }
    }

    public void lowerland(CollisionResult target) {
        float tarx = ((target.getContactPoint().x));
        float tarz = ((target.getContactPoint().z));
        TerrainMap[(int) (tarx - HALFTILE)][(int) (tarz - HALFTILE)]--;
        gameNode.detachChild(target.getGeometry());
        reloadLand((int) (tarx - HALFTILE), (int) (tarz - HALFTILE));


    }


    public void raiseland(CollisionResult target) {
        float tarx = ((target.getContactPoint().x));
        float tarz = ((target.getContactPoint().z));
        switch(brush){
            case 1:
                TerrainMap[(int) (tarx - HALFTILE)][(int) (tarz - HALFTILE)]++;
                reloadLand((int) (tarx - HALFTILE), (int) (tarz - HALFTILE));
                break;
                
            case 2:
                TerrainMap[(int) (tarx - HALFTILE)][(int) (tarz - HALFTILE)]++;
                reloadLand((int) (tarx - HALFTILE), (int) (tarz - HALFTILE));
                
                TerrainMap[(int) (tarx - HALFTILE)+1][(int) (tarz - HALFTILE)]++;
                reloadLand((int) (tarx - HALFTILE)+1, (int) (tarz - HALFTILE));
                
                TerrainMap[(int) (tarx - HALFTILE)+1][(int) (tarz - HALFTILE)+1]++;
                reloadLand((int) (tarx - HALFTILE)+1, (int) (tarz - HALFTILE)+1);
                
                TerrainMap[(int) (tarx - HALFTILE)][(int) (tarz - HALFTILE)+1]++;
                reloadLand((int) (tarx - HALFTILE), (int) (tarz - HALFTILE)+1);
                break;
                
                
            case 3:
                TerrainMap[(int) (tarx - HALFTILE)-1][(int) (tarz - HALFTILE)-1]++;
                reloadLand((int) (tarx - HALFTILE)-1, (int) (tarz - HALFTILE)-1);
                
                TerrainMap[(int) (tarx - HALFTILE)-1][(int) (tarz - HALFTILE)]++;
                reloadLand((int) (tarx - HALFTILE)-1, (int) (tarz - HALFTILE));
                
                TerrainMap[(int) (tarx - HALFTILE)-1][(int) (tarz - HALFTILE)+1]++;
                reloadLand((int) (tarx - HALFTILE)-1, (int) (tarz - HALFTILE)+1);
                
                TerrainMap[(int) (tarx - HALFTILE)][(int) (tarz - HALFTILE)-1]++;
                reloadLand((int) (tarx - HALFTILE), (int) (tarz - HALFTILE)-1);
                
                TerrainMap[(int) (tarx - HALFTILE)][(int) (tarz - HALFTILE)]++;
                reloadLand((int) (tarx - HALFTILE), (int) (tarz - HALFTILE));
                
                TerrainMap[(int) (tarx - HALFTILE)][(int) (tarz - HALFTILE)+1]++;
                reloadLand((int) (tarx - HALFTILE), (int) (tarz - HALFTILE)+1);
                
                TerrainMap[(int) (tarx - HALFTILE)+1][(int) (tarz - HALFTILE)-1]++;
                reloadLand((int) (tarx - HALFTILE)+1, (int) (tarz - HALFTILE)-1);
                
                TerrainMap[(int) (tarx - HALFTILE)+1][(int) (tarz - HALFTILE)]++;
                reloadLand((int) (tarx - HALFTILE)+1, (int) (tarz - HALFTILE));
                
                TerrainMap[(int) (tarx - HALFTILE)+1][(int) (tarz - HALFTILE)+1]++;
                reloadLand((int) (tarx - HALFTILE)+1, (int) (tarz - HALFTILE)+1);
                break;
                
            case 4:
                
                break;
                
        }
        gameNode.detachChild(target.getGeometry());
        
        




    }

    public void reloadLand(int x, int z) {



        Geometry geomclone = TerrainBox();
        geomclone.setLocalScale((new Vector3f(1, TerrainMap[x][z], 1)));
        System.out.println(TerrainMap[x][z] + " " + x + " " + z);
        geomclone.setLocalTranslation(1, geomclone.getLocalTranslation().z + ((float) TerrainMap[x][z] / 2), 1);
        geomclone.move(x, 1, z);
        gameNode.attachChild(geomclone);


    }
    public void brushMinus(){
        System.out.println("brushminus");
        System.out.println("brushminus");
        System.out.println("brushminus");
        System.out.println("brushminus");
        if(brush-1==0){
            return;
        }
        else{
            brush=brush-1;
        }
    }
    public void brushPlus(){
        System.out.println("brushplus");
        System.out.println("brushplus");
        System.out.println("brushplus");
        System.out.println("brushplus");
        
        
        if(brush+1==4){
            return;
        }
        else{
            brush++;
        }
    }
}
