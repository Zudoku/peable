/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.shops;

import com.jme3.asset.AssetManager;
import com.jme3.math.Matrix3f;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import mygame.terrain.Direction;

/**
 *
 * @author arska
 */
public class HolomodelDrawer {

    AssetManager assetManager;
    private final Node node;
    boolean activated = false;
    Spatial drawed;
    boolean positionLocked=false;

    public HolomodelDrawer(AssetManager assetManager, Node rootNode) {
        this.assetManager = assetManager;
        this.node = rootNode;
        
        
    }
    
    public void toggleDrawSpatial() {
        activated = !activated;
        if (activated == true) {
            node.attachChild(drawed);
        } else {
            node.detachChild(drawed);
        }
    }

    public void loadSpatial(Spatial geom) {
        if (geom != null) {
            drawed = geom;
        }
    }
    public Vector3f getLocation(){
        return drawed.getLocalTranslation();
    }
    
    public void updateLocation(Vector3f loc){
        if(positionLocked){
            return;
        }
        drawed.setLocalTranslation(pyorista(loc));
    }
    public Vector3f pyorista(Vector3f pos){
         float x = pos.x - 0.4999f + 1;
        float y = pos.y - 0.4999f + 1;
        float z = pos.z - 0.4999f + 1;

        Vector3f vec = new Vector3f((int) x, (int) y, (int) z);
        return vec;
    }
    public void lockPos(){
        positionLocked=true;
    }
    public void raiseDrawed() {
        Vector3f tempvec=drawed.getLocalTranslation();
        tempvec.y=tempvec.y+1;
        drawed.setLocalTranslation(tempvec);
    }
    public void lowerDrawed() {
        Vector3f tempvec=drawed.getLocalTranslation();
        tempvec.y=tempvec.y-1;
        drawed.setLocalTranslation(tempvec);
    }
    public void rotateDrawed(Direction facing){
        Quaternion rot=new Quaternion();
        switch (facing){
            case DOWN:
                rot.fromAngleAxis(90, Vector3f.UNIT_Y);
                drawed.setLocalRotation(rot);
                break;
                
            case LEFT:
                
                break;
                
            case RIGHT:
                break;
                
            case UP:
                
            
        }
    }
    
}
