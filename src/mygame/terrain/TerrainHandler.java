/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.terrain;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.jme3.asset.AssetManager;
import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.terrain.geomipmap.TerrainQuad;
import com.jme3.texture.Texture;
import com.jme3.texture.Texture.WrapMode;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.logging.Logger;
import mygame.UtilityMethods;
import mygame.terrain.events.SetMapDataEvent;

/**
 *
 * @author arska
 */
@Singleton
public class TerrainHandler {
    private static final Logger logger = Logger.getLogger(TerrainHandler.class.getName());
    private Node terrainNode=new Node("TerrainNode");
    AssetManager assetManager;
    
    private final static int SIZE_MEDIUM=4;
    private final static int SIZE_SMALL=2;
    private final static int SIZE_MINIMAL=1;
    
    private int brushSize=SIZE_MINIMAL;
    private boolean useTexture=false;
    private int textureID=1;
    private boolean locked=false;
    private ArrayList<Integer>lockedpositions=new ArrayList<Integer>();
    private float startY=-1;
    private float currentY;
    private final ParkHandler parkHandler;
    private final EventBus eventBus;
    private ByteBuffer buf;
    private ByteBuffer bufferReal;
    private Texture alphaTexture;
    
    private int lastBrushx=-1;
    private int lastBrushz=-1;
    
    @Inject
    public TerrainHandler(Node rootNode, AssetManager assetManager,ParkHandler parkHandler,EventBus eventBus) {
        rootNode.attachChild(terrainNode);
        this.assetManager = assetManager;
        this.parkHandler=parkHandler;
        this.eventBus=eventBus;
        alphaTexture=assetManager.loadTexture("Textures/alphamap.png");
        bufferReal = ByteBuffer.allocateDirect(128*128*3);
        for(int x=0;x<128*128;x++){
            bufferReal.put(x*3+2, (byte)128);
        }
        
    }
    private boolean findIsTerrain(Node r){
        if(r==null){
            return false;
        }
        if("Terrain".equals(r.getUserData("type"))){
            return true;
        }
        return findIsTerrain(r.getParent());  
    }

    public void handleClicking(CollisionResults results) {
        Vector3f location = null;
        CollisionResult result = null;
        for (CollisionResult r : results) {
            if(findIsTerrain(r.getGeometry().getParent())){
                result = r;
                break;
            }
            

        }
        if (result != null) {
            if (!locked) {
                location = result.getContactPoint();
                calculateLockedPositions(location);
                locked = true;
            }

        }
    }

    public void handleDrag(float current,long lastDragged) {
        if (locked) {
            if (startY == -1) {
                startY = current;
            }else{
                currentY=current;
                float deltaY=currentY-startY;
                if(deltaY>80||deltaY<-80){
                    if(deltaY>80){
                        changeMapData(true);
                    }else{
                        changeMapData(false);
                    }
                    refreshGround();
                    startY=currentY;
                    lastDragged+=40;
                }
                
            }
        }
    }
    public void releaseDrag(){
        locked=false;
        startY=-1;
        lockedpositions.clear();
        currentY=-1;
        
    }
    public void drawBrush(float x,float z){
        
        int x1=(int)(x);
        int z1=128-(int)(z)-1;
        
        boolean noDifference=false;
        if(lastBrushx==-1||lastBrushz==-1||lastBrushx!=x1||lastBrushz!=z1){
            lastBrushx=x1;
            lastBrushz=z1;
            System.out.println("NEW BUFFER INC "+x1+" "+z1);
        }else{
            noDifference=true;    
        }
        for (int j = 0; j < brushSize; j++) {
            
            for (int i = 0; i < brushSize; i++) {
                if (!noDifference) {
                    
                    buf=UtilityMethods.cloneByteBuffer(bufferReal);
                    int index=((z1 + j) * 128 + x1) * 3 + 1;
                    if(index<128*128*3-1){
                        buf.put(index, (byte) 128);
                        alphaTexture.getImage().setData(buf);
                    }else{
                        System.out.println("OVERBOARD "+ index);
                    }
                }
            }
        }
    }
    private void changeMapData(boolean up){
        float[]mapdata=parkHandler.getMapData();
        if(up){
            for(int i:lockedpositions){
                mapdata[i]+=1;
                
            }
        }else{
            for(int i:lockedpositions){
                mapdata[i]-=1;
            }
        }
    }
    private  void calculateLockedPositions(Vector3f pos){
        
        int x=(int)(pos.x)+1;
        int z=(int)(pos.z)+1;
        

        for (int j = 0; j < brushSize; j++) {
            
            for (int i = 0; i <brushSize; i++) {
                lockedpositions.add((z+j)*128+x + i);
            }
        }
    }
    
    public TerrainQuad buildGround(){
        final Material testmaterial = new Material(assetManager, "Common/MatDefs/Terrain/Terrain.j3md");
        testmaterial.setTexture("Alpha", alphaTexture);
        
        Texture grass = assetManager.loadTexture(
            "Textures/grasstexture.png");
        grass.setWrap(WrapMode.MirroredRepeat);
        testmaterial.setTexture("Tex1", grass);
        testmaterial.setFloat("Tex1Scale",128f);
        
        Texture rock = assetManager.loadTexture(
            "Textures/rocktexture.png");
        rock.setWrap(WrapMode.Repeat);
        testmaterial.setTexture("Tex2", rock);
        testmaterial.setFloat("Tex2Scale",128f);
        
        Texture tex3 = assetManager.loadTexture(
            "Textures/selection.png");
        tex3.setWrap(WrapMode.Repeat);
        testmaterial.setTexture("Tex3", tex3);
        testmaterial.setFloat("Tex3Scale",128f);
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

    public void setBrushSize(int brushSize) {
        this.brushSize = brushSize;
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
    public boolean getLocked(){
        return locked;
    }
    public void brushSizePlus(){
        switch(brushSize){
            case 1:
                brushSize++;
                break;
                
            case 2:
                brushSize+=2;
                break;
        }
    }
    public void brushSizeMinus(){
        switch(brushSize){
            case 2:
                brushSize--;
                break;
                
            case 4:
                brushSize-=2;
                break;
        }
    }
    
   
}
