/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.GUI;

import com.jme3.asset.AssetManager;
import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.effect.ParticleEmitter;
import com.jme3.effect.ParticleMesh;
import com.jme3.effect.shapes.EmitterPointShape;
import com.jme3.input.InputManager;
import com.jme3.material.Material;
import com.jme3.math.Ray;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Node;
import mygame.Main;
import mygame.terrain.RoadMakerStatus;
import mygame.terrain.WorldHandler;

/**
 *
 * @author arska
 */
public class SelectionParticleEmitter {

    private final AssetManager assetManager;
    private final Node rootNode;
    ParticleEmitter brush[][] = new ParticleEmitter[3][3];
    private Vector3f Vector3f;
    private final WorldHandler worldHandler;
    private InputManager inputManager;
    private Camera cam;
    CollisionResult last;

    public SelectionParticleEmitter(AssetManager assetManager, Node rootNode, WorldHandler worldHandler) {
        this.assetManager = assetManager;
        this.rootNode = rootNode;
        this.worldHandler = worldHandler;
        last = new CollisionResult();
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
   
    public void deleteParticles(){
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

        switch (worldHandler.brush) {
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
        brush[num1][num2].setLocalTranslation(x + 1, y + 1.01f, z + 1);
        brush[num1][num2].emitAllParticles();
        brush[num1][num2].setEnabled(true);
    }

    public void initSelection() {
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

    public void updateSelection(Node rootNode, InputManager inputManager, Camera cam) {
        this.inputManager = inputManager;
        this.cam = cam;

        CollisionResults results = new CollisionResults();


        Vector2f click2d = inputManager.getCursorPosition();
        Vector3f click3d = cam.getWorldCoordinates(
                new Vector2f(click2d.getX(), click2d.getY()), 0f);

        Vector3f dir = cam.getWorldCoordinates(
                new Vector2f(click2d.getX(), click2d.getY()), 1f).
                subtractLocal(click3d);

        Ray ray = new Ray(cam.getLocation(), dir);

        rootNode.collideWith(ray, results);
        CollisionResult target = results.getClosestCollision();
        switch (Main.clickingHandler.clickMode) {
            case TERRAIN:
                if (target == null) {
                    return;
                }
                if (target != last) {
                    MoveSelectionEmitters((int) (target.getContactPoint().x - worldHandler.HALFTILE), (int) (target.getContactPoint().y - worldHandler.HALFTILE), (int) (target.getContactPoint().z - worldHandler.HALFTILE));
                }
                last = target;
                break;
            case ROAD:
                if(Main.roadMaker.status==RoadMakerStatus.CHOOSING){
                    worldHandler.brush=1;
                    if (target == null) {
                    return;
                    }
                    int testx=(int) (target.getContactPoint().x - worldHandler.HALFTILE);
                    int testy=(int) (target.getContactPoint().y - worldHandler.HALFTILE);
                    int testz=(int) (target.getContactPoint().z - worldHandler.HALFTILE);
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
                        Main.holoDrawer.updateLocation(target);
                    break;
                    
        }
       
    }
}
