/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.jme3.collision.CollisionResults;
import com.jme3.input.InputManager;
import com.jme3.math.Ray;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Node;
import java.nio.ByteBuffer;

/**
 *
 * @author arska
 */
@Singleton
public class UtilityMethods {
    private static InputManager inputManager;
    private static Camera cam;
    public static float HALFTILE=0.49999f;
    public static String programTitle="Into Park 0.02C";
    @Inject
    public UtilityMethods(Camera cam,InputManager inputManager) {
        this.cam=cam;
        this.inputManager=inputManager;
    }
    
    public static void rayCast(CollisionResults results,Node rootNode) {
        Vector2f click2d = inputManager.getCursorPosition();
        Vector3f click3d = cam.getWorldCoordinates(
                new Vector2f(click2d.getX(), click2d.getY()), 0f);

        Vector3f dir = cam.getWorldCoordinates(
                new Vector2f(click2d.getX(), click2d.getY()), 1f).
                subtractLocal(click3d).normalizeLocal();

        Ray ray = new Ray(cam.getLocation(), dir);

        rootNode.collideWith(ray, results);
    }
    
    public static ByteBuffer cloneByteBuffer(ByteBuffer original) {
       ByteBuffer clone = ByteBuffer.allocateDirect(original.capacity());
       original.rewind();//copy from the beginning
       clone.put(original);
       original.rewind();
       //clone.flip();
       clone.position(original.position());
       return clone;
    }
     public static boolean findIsTerrain(Node r){
        if(r==null){
            return false;
        }
        if("Terrain".equals(r.getUserData("type"))){
            return true;
        }
        return findIsTerrain(r.getParent());  
    }
}
