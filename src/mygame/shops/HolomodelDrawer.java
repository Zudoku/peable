/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.shops;

import com.jme3.asset.AssetManager;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

/**
 *
 * @author arska
 */
public class HolomodelDrawer {

    AssetManager assetManager;
    private final Node node;
    boolean activated = false;
    Spatial drawed;

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
        drawed.setLocalTranslation(pyorista(loc));
    }
    public Vector3f pyorista(Vector3f pos){
         float x = pos.x - 0.4999f + 1;
        float y = pos.y - 0.4999f + 1;
        float z = pos.z - 0.4999f + 1;

        Vector3f vec = new Vector3f((int) x, (int) y, (int) z);
        return vec;
    }
    
}
