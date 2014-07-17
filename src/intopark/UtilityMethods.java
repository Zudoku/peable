/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package intopark;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.jme3.asset.AssetManager;
import com.jme3.collision.CollisionResults;
import com.jme3.input.InputManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Ray;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.elements.Element;
import intopark.roads.RoadGraph;
import intopark.roads.Walkable;
import java.nio.ByteBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jgraph.graph.DefaultEdge;

/**
 *
 * @author arska
 */
@Singleton
public class UtilityMethods {
    //LOGGER
    private static final Logger logger = Logger.getLogger(UtilityMethods.class.getName());
    //DEPENDENCIES
    private static InputManager inputManager;
    private static Node rootNode;
    private static AssetManager assetManager;
    private static Camera cam;
    private static EventBus eventBus;
    //OWNS

    //VARIABLES
    public static float HALFTILE=0.49999f;
    public static String programTitle="Into Park 0.05A";

    @Inject
    public UtilityMethods(Camera cam,InputManager inputManager,AssetManager assetManager,Node rootNode,EventBus eventBus) {
        this.cam=cam;
        this.inputManager=inputManager;
        this.assetManager=assetManager;
        this.rootNode=rootNode;
        this.eventBus=eventBus;
    }

    public static void rayCast(CollisionResults results,Node someNode) {
        Vector2f click2d = inputManager.getCursorPosition();
        Vector3f click3d = cam.getWorldCoordinates(
                new Vector2f(click2d.getX(), click2d.getY()), 0f);

        Vector3f dir = cam.getWorldCoordinates(
                new Vector2f(click2d.getX(), click2d.getY()), 1f).
                subtractLocal(click3d).normalizeLocal();

        Ray ray = new Ray(cam.getLocation(), dir);
        if(someNode!=null){
            someNode.collideWith(ray, results);
        }else{
            rootNode.collideWith(ray, results);
        }

    }
    public static Walkable getTheOtherEndOfEdge(RoadGraph roadGraph,DefaultEdge edge,Walkable currentWalkable){
        if(roadGraph.getRoadMap().getEdgeSource(edge)!=currentWalkable){
            return roadGraph.getRoadMap().getEdgeSource(edge);
        }
        else if(roadGraph.getRoadMap().getEdgeTarget(edge)!=currentWalkable){
            return roadGraph.getRoadMap().getEdgeTarget(edge);
        }
        throw new RuntimeException("Can't figure out the other end. Both ends are the same.");
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
     /**
      * Toggles the nifty-GUI elements visibility.
      * @param nifty
      * @param elementname
      */
     public static void toggleVisibility(Nifty nifty,String elementname){
        Element e=nifty.getCurrentScreen().findElementByName(elementname);
        e.setVisible(!e.isVisible());
        e.setId(elementname);
     }
     /**
      * This rounds a vector to ~int values. Helps to calculate things like:
      * What tile is this on and so forth.
      * @param vector Vector3f to round.
      * @return Rounded Vector3f.
      */
     public static Vector3f roundVector(Vector3f vector){
        //
        float x = vector.x - 0.4999f + 1;
        float y = vector.y - 0.4999f + 1;
        float z = vector.z - 0.4999f + 1;

        Vector3f vector2 = new Vector3f((int) x, (int) y, (int) z);
        logger.log(Level.FINEST,"Vector {0} rounded to {1}",new Object[]{vector,vector2});
        return vector2;
    }
     public static Spatial getDebugBoxSpatial(float size,ColorRGBA color){
         Box b = new Box(1, 1, 1);
         Geometry geom = new Geometry("Box", b);
         Material mat = new Material(assetManager,"Common/MatDefs/Misc/Unshaded.j3md");
         mat.setColor("Color", color);
         geom.setMaterial(mat);
         geom.setLocalScale(size);
         return geom;
     }
     public static void publishEvent(Object event){
         eventBus.post(event);
     }

}
