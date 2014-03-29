/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package intopark.shops;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.jme3.asset.AssetManager;
import com.jme3.collision.CollisionResult;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import java.util.logging.Logger;
import intopark.UtilityMethods;
import intopark.terrain.Direction;

/**
 *
 * @author arska
 */
@Singleton
public class HolomodelDrawer {
    private static final Logger logger = Logger.getLogger(HolomodelDrawer.class.getName());
    private AssetManager assetManager;
    private final Node node;
    private boolean activated = false;
    private Spatial drawed;
    private boolean positionLocked=false;
    private Node holoNode;
    private EventBus eventBus;
    @Inject
    public HolomodelDrawer(AssetManager assetManager, Node rootNode,EventBus eventBus) {
        this.assetManager = assetManager;
        this.node = rootNode;
        this.eventBus=eventBus;
        eventBus.register(this);
        holoNode=new Node("holonode");
        
        
    }
    
    public void toggleDrawSpatial() {
        activated = !activated;
        if (activated == true) {
            node.attachChild(holoNode);
        } else {
            node.detachChild(holoNode);
        }
    }

    public void loadSpatial(Spatial geom) {
        if (geom != null) {
            
            this.drawed = geom;
            
        }
        else{
            return;
        }
        holoNode.detachAllChildren();
        
        holoNode.attachChild(drawed);
    }
    public Vector3f getLocation(){
        return drawed.getLocalTranslation();
    }
    
    public void updateLocation(CollisionResult target){
        if(positionLocked){
            return;
        }
        if(!UtilityMethods.findUserDataType(target.getGeometry().getParent(),"Terrain")){
            return;
        }
        if(drawed==null){
            return;
        }
        Vector3f loc = target.getContactPoint();
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
    @Subscribe
    public void listenToggleDrawSpatial(ToggleHoloModelDrawEvent event){
        toggleDrawSpatial();
    }
    
    
}