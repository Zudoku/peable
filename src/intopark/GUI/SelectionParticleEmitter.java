/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package intopark.GUI;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.jme3.asset.AssetManager;
import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.effect.ParticleEmitter;
import com.jme3.effect.ParticleMesh;
import com.jme3.effect.shapes.EmitterPointShape;
import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import intopark.UtilityMethods;
import intopark.inputhandler.ClickingHandler;
import intopark.shops.HolomodelDrawer;
import intopark.terrain.RoadMaker;
import intopark.terrain.RoadMakerStatus;
import intopark.terrain.TerrainHandler;

/**
 *
 * @author arska
 */
@Singleton
public class SelectionParticleEmitter {

    private final AssetManager assetManager;
    private final Node rootNode;
    ParticleEmitter brush[][] = new ParticleEmitter[3][3];
    private Vector3f Vector3f;
    private final TerrainHandler terrainHandler;
    private ClickingHandler clickingHandler;
    private RoadMaker roadMaker;
    private HolomodelDrawer holoDrawer;
    Vector3f last;
    @Inject
    public SelectionParticleEmitter(HolomodelDrawer holomodelDrawer,AssetManager aM,ClickingHandler click,RoadMaker roadMaker, Node rootNode, TerrainHandler worldHandler) {
        this.assetManager = aM;
        this.rootNode = rootNode;
        this.terrainHandler = worldHandler;
        this.clickingHandler=click;
        this.roadMaker=roadMaker;
        this.holoDrawer=holomodelDrawer;
        last = new Vector3f(0,0,0);
        initSelection();
    }

    public void MakeSelectionEmitter(int x, int z) {
        ParticleEmitter selection =
                new ParticleEmitter("SelectionEmitter", ParticleMesh.Type.Triangle, 1);
        Material mat_red = new Material(assetManager,
                "Common/MatDefs/Misc/Particle.j3md");
        mat_red.setTexture("Texture", assetManager.loadTexture(
                "Textures/selection.png"));
        selection.setMaterial(mat_red);
        selection.setShape(new EmitterPointShape(Vector3f.ZERO));
        selection.setImagesX(20);
        selection.setImagesY(20);
        selection.setStartSize(0.5f);
        selection.setEndSize(0.5f);
        selection.setParticlesPerSec(0.01f);
        selection.setVelocityVariation(0);
        selection.setFaceNormal(Vector3f = Vector3f.UNIT_Y);
        selection.getParticleInfluencer().setInitialVelocity(new Vector3f(0, 0, 0));
        selection.getParticleInfluencer().setVelocityVariation(0);
        selection.setGravity(0, 0, 0);
        selection.move(20, 6, 20);
        selection.setLowLife(100f);
        selection.setHighLife(100f);
        selection.setSelectRandomImage(false);
        brush[x][z] = selection;

        rootNode.attachChild(selection);
    }
   
    public void deleteParticles() {
        brush[0][0].killAllParticles();
        brush[0][1].killAllParticles();
        brush[0][2].killAllParticles();
        brush[1][0].killAllParticles();
        brush[1][1].killAllParticles();
        brush[1][2].killAllParticles();
        brush[2][0].killAllParticles();
        brush[2][1].killAllParticles();
        brush[2][2].killAllParticles();
    }

    public void MoveSelectionEmitters(int x, int y, int z) {
        
        switch (terrainHandler.getBrushSize()) {
            case 1:
                brush[0][0].killAllParticles();
                brush[0][1].killAllParticles();
                brush[0][2].killAllParticles();
                brush[1][0].killAllParticles();
                moveEmitter(1, 1, x, y, z);
                brush[1][2].killAllParticles();
                brush[2][0].killAllParticles();
                brush[2][1].killAllParticles();
                brush[2][2].killAllParticles();
                break;
            case 2:
                brush[0][0].killAllParticles();
                brush[0][1].killAllParticles();
                brush[0][2].killAllParticles();
                brush[1][0].killAllParticles();
                moveEmitter(1, 1, x, y, z);
                moveEmitter(1, 2, x, y, z + 1);
                brush[2][0].killAllParticles();
                moveEmitter(2, 1, x + 1, y, z);
                moveEmitter(2, 2, x + 1, y, z + 1);

                break;
            case 3:
                moveEmitter(0, 0, x - 1, y, z - 1);
                moveEmitter(0, 1, x - 1, y, z);
                moveEmitter(0, 2, x - 1, y, z + 1);

                moveEmitter(1, 0, x, y, z - 1);
                moveEmitter(1, 1, x, y, z);
                moveEmitter(1, 2, x, y, z + 1);

                moveEmitter(2, 0, x + 1, y, z - 1);
                moveEmitter(2, 1, x + 1, y, z);
                moveEmitter(2, 2, x + 1, y, z + 1);
        }


    }

    public void moveEmitter(int num1, int num2, int x, int y, int z) {
        brush[num1][num2].killAllParticles();
        brush[num1][num2].setLocalTranslation(x + 1, y + 0.51f, z + 1);
        brush[num1][num2].emitAllParticles();
        brush[num1][num2].setEnabled(true);
    }

    private void initSelection() {
        MakeSelectionEmitter(0, 0);
        MakeSelectionEmitter(0, 1);
        MakeSelectionEmitter(0, 2);
        MakeSelectionEmitter(1, 0);
        MakeSelectionEmitter(1, 1);
        MakeSelectionEmitter(1, 2);
        MakeSelectionEmitter(2, 0);
        MakeSelectionEmitter(2, 1);
        MakeSelectionEmitter(2, 2);
    }

    public void updateSelection() {
        

        CollisionResults results = new CollisionResults();
        UtilityMethods.rayCast(results, rootNode);
        
        CollisionResult target = results.getClosestCollision();
        switch (clickingHandler.getClickMode()) {
            case TERRAIN:
                if (target == null) {
                    return;
                }
                if (!terrainHandler.getLocked()) {
                    terrainHandler.drawBrush(target.getContactPoint().x,target.getContactPoint().z);
                }
                
                break;
            case ROAD:
                if(roadMaker.status==RoadMakerStatus.CHOOSING){
                    //worldHandler.brush=1;
                    if (target == null) {
                    return;
                    }
                    int testx=(int) (target.getContactPoint().x - UtilityMethods.HALFTILE);
                    int testy=(int) (target.getContactPoint().y - UtilityMethods.HALFTILE);
                    int testz=(int) (target.getContactPoint().z - UtilityMethods.HALFTILE);
                    MoveSelectionEmitters(testx,testy,testz);
                }
                break;
            case NOTHING:
                    deleteParticles();
                    break;
                    
                case PLACE:
                    if(target==null){
                        return;
                    }
                        holoDrawer.updateLocation(target);
                    break;
                    
        }
       
    }
}
