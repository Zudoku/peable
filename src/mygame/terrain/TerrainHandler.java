/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.terrain;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.jme3.asset.AssetManager;
import com.jme3.collision.CollisionResult;
import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.terrain.geomipmap.TerrainQuad;
import com.jme3.texture.Texture;
import com.jme3.texture.Texture.WrapMode;
import java.util.logging.Logger;
import mygame.terrain.events.SetMapDataEvent;

/**
 *
 * @author arska
 */
@Singleton
public class TerrainHandler {
    private static final Logger logger = Logger.getLogger(TerrainHandler.class.getName());
    public static final float HALFTILE = 0.4999f;
    private Node terrainNode=new Node("TerrainNode");
    AssetManager assetManager;
    
    private final static int DEFAULT_MODE=0;
    private final static int MODERN_MODE=1;
    private final static int SIZE_BIG=4;
    private final static int SIZE_MEDIUM=3;
    private final static int SIZE_SMALL=2;
    private final static int SIZE_MINIMAL=1;
    
    private int brushSize=SIZE_MEDIUM;
    private int mode=DEFAULT_MODE;
    private boolean useTexture=false;
    private int textureID=1;
    private final ParkHandler parkHandler;
    private final EventBus eventBus;
    
    @Inject
    public TerrainHandler(Node rootNode, AssetManager assetManager,ParkHandler parkHandler,EventBus eventBus) {
        rootNode.attachChild(terrainNode);
        this.assetManager = assetManager;
        this.parkHandler=parkHandler;
        this.eventBus=eventBus;
        
    }
    public void handleClicking(CollisionResult target){
        Vector3f location=null;
        if(target.getGeometry().getUserData("type").equals("Terrain")){
            location=target.getContactPoint();
            
            
        }
    }
    public TerrainQuad buildGround(){
        Texture grass = assetManager.loadTexture(
            "Textures/grasstexture.png");
        grass.setWrap(WrapMode.MirroredRepeat);
        Material testmaterial = new Material(assetManager, "Common/MatDefs/Terrain/Terrain.j3md");
        testmaterial.setTexture("Tex1", grass);
        testmaterial.setFloat("Tex1Scale",128f);
        int patchSize = 3;
        TerrainQuad terrain;
        terrain = new TerrainQuad("test", patchSize, 129,parkHandler.getMapData());
        terrain.setMaterial(testmaterial);
        
        //terrain.setLocalScale(0.5f);
        terrain.setLocalTranslation(64,0,64);
        terrain.setUserData("type","Terrain");
        return terrain;
    }
    public void refreshGround(){
        terrainNode.detachAllChildren();
        terrainNode.attachChild(buildGround());
    }
    public void resetGround(){
        eventBus.post(new SetMapDataEvent(getHeightMap()));
    }
    public float[] getHeightMap(){
        final int totalsize=128;
        final float[] heightMap=new float[totalsize*totalsize];
        for(int h=0;h<totalsize;h++){
            for(int w=0;w<totalsize;w++){
                if(h%3!=0){
                    heightMap[h*totalsize+w]=6;
                }
                else{
                    heightMap[h*totalsize+w]=6;
                }
            }
        }
        
     
        
        return heightMap;
    }

    public int getBrushSize() {
        return brushSize;
    }

    public int getMode() {
        return mode;
    }

    public void setBrushSize(int brushSize) {
        this.brushSize = brushSize;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public int getTextureID() {
        return textureID;
    }

    public void setTextureID(int textureID) {
        this.textureID = textureID;
    }

    public void setUseTexture(boolean useTexture) {
        this.useTexture = useTexture;
    }
    public boolean getUseTexture(){
        return useTexture;
    }
    
   
}
