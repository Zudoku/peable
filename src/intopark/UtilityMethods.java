/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package intopark;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.jme3.asset.AssetManager;
import com.jme3.collision.CollisionResults;
import com.jme3.input.InputManager;
import com.jme3.math.Ray;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import java.nio.ByteBuffer;

/**
 *
 * @author arska
 */
@Singleton
public class UtilityMethods {
    private static InputManager inputManager;
    private static AssetManager assetManager;
    private static Camera cam;
    public static float HALFTILE=0.49999f;
    public static String programTitle="Into Park 0.04A";
    @Inject
    public UtilityMethods(Camera cam,InputManager inputManager,AssetManager assetManager) {
        this.cam=cam;
        this.inputManager=inputManager;
        this.assetManager=assetManager;
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
    /**
     * Clones bytebuffer but doesn't use the copy(). Useful to create identical bytebuffer but they cant be linked together.(?)
     * @param original`bytebuffer
     * @return copy of original bytebuffer
     */
    public static ByteBuffer cloneByteBuffer(ByteBuffer original) {
       ByteBuffer clone = ByteBuffer.allocateDirect(original.capacity());
       original.rewind();//copy from the beginning
       clone.put(original);
       original.rewind();
       //clone.flip();
       clone.position(original.position());
       return clone;
    }
    /**
     * Tries to find Nodes or its parents userdata if its matching type
     * @param r Spatial to search
     * @param type UserData type
     * @return 
     */
     public static boolean findUserDataType(Node r,String type){
         if(r==null){
            return false;
        }
        if(type.equals(r.getUserData("type"))){
            return true;
        }
        return findUserDataType(r.getParent(),type); 
     }
     /**
      * Load model so that the class that needs the model doesn't need AssetManager.
      * @param path to load.
      * @return Spatial.
      */
     public static Spatial loadModel(String path){
         return assetManager.loadModel(path);
     }
}
