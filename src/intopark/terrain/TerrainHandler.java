/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package intopark.terrain;

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
import intopark.inout.LoadPaths;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.logging.Logger;
import intopark.UtilityMethods;
import intopark.inputhandler.MouseContainer;
import intopark.inputhandler.NeedMouse;
import intopark.terrain.events.RefreshGroundEvent;
import intopark.terrain.events.SetMapDataEvent;

/**
 *
 * @author arska
 */
@Singleton
public class TerrainHandler implements NeedMouse{
    //LOGGER
    private static final Logger logger = Logger.getLogger(TerrainHandler.class.getName());
    //MISC
    //CONSTANTS
    private final static int SIZE_MEDIUM = 8;
    private final static int SIZE_SMALL = 4;
    private final static int SIZE_MINIMAL = 2;
    //DEPENDENCIES
    AssetManager assetManager;
    private final ParkHandler parkHandler;
    private final EventBus eventBus;
    //OWNS
    private Node terrainNode = new Node("TerrainNode");
    private ArrayList<Integer> lockedpositions = new ArrayList<>();
    //VARIABLES
    private int brushSize = SIZE_MINIMAL;
    private boolean useTexture = false;
    private boolean locked=false;
    private int textureID = 1;
    private ByteBuffer buf;
    private ByteBuffer bufferReal;
    private Texture alphaTexture;
    private int lastBrushx = -1;
    private int lastBrushz = -1;
    private float movedY=-1;
    /**
     * This class is Manager for everything that involves terrain. Shovel and texturing.
     * @param rootNode This is used to attach the terrain to node.
     * @param assetManager This is used to load textures and materials.
     * @param parkHandler This is used to get the MapData.
     * @param eventBus Sending some events regarding UI.
     */
    @Inject
    public TerrainHandler(Node rootNode, AssetManager assetManager, ParkHandler parkHandler, EventBus eventBus) {
        rootNode.attachChild(terrainNode);
        this.assetManager = assetManager;
        this.parkHandler = parkHandler;
        this.eventBus = eventBus;
        alphaTexture = assetManager.loadTexture(LoadPaths.alphamap);
        bufferReal = ByteBuffer.allocateDirect(128 * 128 * 3);
        for (int x = 0; x < 128 * 128; x++) {
            bufferReal.put(x * 3 + 2, (byte) 128);
        }
        eventBus.register(this);
        //TODO: PROPERLY DOCUMENT THIS CLASS.
    }

    public void drawBrush(float x, float z) {

        int x1 = (int) (x) +1- brushSize / 2;
        int z1 = 128 - (int) (z) - brushSize / 2;


        if (lastBrushx == -1 || lastBrushz == -1 || lastBrushx != x1 || lastBrushz != z1) {
            lastBrushx = x1;
            lastBrushz = z1;
        } else {
            return;
        }
        buf = UtilityMethods.cloneByteBuffer(bufferReal);
        for (int j = 0; j < brushSize-1; j++) {

            for (int i = 0; i < brushSize-1; i++) {


                
                int index = ((z1 + j) * 128 + x1 + i) * 3 + 1;
                if (index < 128 * 128 * 3 - 1) {
                    buf.put(index, (byte) 128);
                    alphaTexture.getImage().setData(buf);
                } else {
                }

            }
        }
    }

    private void changeMapData(boolean up) {
        float[] mapdata = parkHandler.getMapData();
        if (up) {
            for (int i : lockedpositions) {
                mapdata[i] += 1;

            }
        } else {
            for (int i : lockedpositions) {
                mapdata[i] -= 1;
            }
        }
    }

    private void calculateLockedPositions(Vector3f pos) {

        int x = (int) (pos.x) + 1 - brushSize / 2;
        int z = (int) (pos.z) + 1 - brushSize / 2;


        for (int j = 0; j < brushSize; j++) {

            for (int i = 0; i < brushSize; i++) {
                lockedpositions.add((z + j) * 128 + x + i);
            }
        }
    }
    /**
     * This returns new ground generated from the heightmap.
     * @return Terrain in TerrainQuad form.
     */
    public TerrainQuad buildGround() {
        final Material testmaterial = new Material(assetManager, "Common/MatDefs/Terrain/Terrain.j3md");
        testmaterial.setTexture("Alpha", alphaTexture);

        Texture grass = assetManager.loadTexture(
                LoadPaths.grasstexture);

        grass.setWrap(WrapMode.MirroredRepeat);
        testmaterial.setTexture("Tex1", grass);
        testmaterial.setFloat("Tex1Scale", 128f);

        Texture rock = assetManager.loadTexture(
                LoadPaths.rocktexture);
        rock.setWrap(WrapMode.Repeat);
        testmaterial.setTexture("Tex2", rock);
        testmaterial.setFloat("Tex2Scale", 128f);

        Texture tex3 = assetManager.loadTexture(
                LoadPaths.selectiontexture);
        tex3.setWrap(WrapMode.Repeat);
        testmaterial.setTexture("Tex3", tex3);
        testmaterial.setFloat("Tex3Scale", 128f);
        int patchSize = 3;
        TerrainQuad terrain;
        terrain = new TerrainQuad("test", patchSize, 129, parkHandler.getMapData());
        terrain.setMaterial(testmaterial);

        terrain.setLocalTranslation(63.5f, 0, 63.5f);
        terrain.setUserData("type", "Terrain");
        return terrain;
    }
    /**
     * This is called whenever ground is updated.
     * This force-refreshes the whole ground.
     */
    public void refreshGround() {
        terrainNode.detachAllChildren();
        terrainNode.attachChild(buildGround());
    }

    public void resetGround() {
        eventBus.post(new SetMapDataEvent(getHeightMap()));
    }
    /**
     * Called from the UI to increment brushSize.
     */
    public void brushSizePlus() {
        switch (brushSize) {
            case 1:
                brushSize++;
                break;

            case 2:
                brushSize += 2;
                break;
        }
    }
    /**
     * Called from the UI to decrement brushSize.
     */
    public void brushSizeMinus() {
        switch (brushSize) {
            case 2:
                brushSize--;
                break;

            case 4:
                brushSize -= 2;
                break;
        }
    }
    @Subscribe
    public void listenRefreshGroundEvent(RefreshGroundEvent event){
        refreshGround();
    }
    /**
     * GETTERS AND SETTERS
     */
    
    /**
     * Get Blank heightmap for debugging purposes
     * @return blank 128*128 float heightmap containing only 6.
     */
    public float[] getHeightMap() {
        final int totalsize = 128;
        final float[] heightMap = new float[totalsize * totalsize];
        for (int h = 0; h < totalsize; h++) {
            for (int w = 0; w < totalsize; w++) {
                if (h % 3 != 0) {
                    heightMap[h * totalsize + w] = 6;
                } else {
                    heightMap[h * totalsize + w] = 6;
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

    public boolean getUseTexture() {
        return useTexture;
    }


    @Override
    public void onClick(MouseContainer container) {
        Vector3f location = null;
        CollisionResult result = null;
        for (CollisionResult r : container.getResults()) {
            if (UtilityMethods.findUserDataType(r.getGeometry().getParent(), "Terrain")) {
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

    @Override
    public void onDrag(MouseContainer container) {
        float deltaY = container.getCurrentY()-movedY;
        if (deltaY > 80 || deltaY < -80) {
            if (deltaY > 80) {
                changeMapData(true);
            } else {
                changeMapData(false);
            }
            refreshGround();
            movedY = container.getCurrentY();
        }
    }

    @Override
    public void onDragRelease(MouseContainer container) {
        locked = false;
        lockedpositions.clear();
    }
    public boolean getLocked(){
        return locked;
    }
    
}
