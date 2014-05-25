/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package intopark.shops;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.jme3.collision.CollisionResult;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import java.util.logging.Logger;
import intopark.UtilityMethods;
import intopark.util.Direction;
import java.util.List;
import java.util.logging.Level;

/**
 *
 * @author arska
 */
@Singleton
public class HolomodelDrawer {
    //LOGGER
    private static final Logger logger = Logger.getLogger(HolomodelDrawer.class.getName());
    //OWNS
    private Node holoNode; //This node will get rendered if HoloModelDrawer is activated.
    //DEPENDENCIES
    private EventBus eventBus; //This is needed to capture and send events.
    private final Node rootNode; //holoNode is getting attached and detached to this Node (WORLD).
    //VARIABLES
    private boolean activated = false; //If HoloModelDrawer is on.Is HoloNode getting drawed to the world.
    private boolean positionLocked=false; //If we want to lock the position.
    
    /**
     * This class is used to draw 'HOLO' render Spatials.
     * Basically this means that you need to render something (For example shop) but you dont actually want to create a shop.
     * You can for example render shops when the user is deciding a location for it (to show what it would look like).
     * @param rootNode 
     * @param eventBus 
     */
    @Inject
    public HolomodelDrawer(Node rootNode,EventBus eventBus) {
        this.rootNode = rootNode;
        this.eventBus=eventBus;
        eventBus.register(this);
        holoNode=new Node("holoNode");        
        //TODO: DRAW BOX WHERE RIDE TOOL IS AT.
    }
    /**
     * Toggle whether we render the holoNode or not.
     */
    public void toggleRenderHoloNode() {
        activated = !activated;
        if (activated == true) {
            rootNode.attachChild(holoNode);
            logger.log(Level.FINEST,"HoloNode attached to RootNode.");
        } else {
            rootNode.detachChild(holoNode);
            logger.log(Level.FINEST,"HoloNode detached from RootNode.");
        }
    }
    /**
     * Attach a Spatial to holoNode.
     * @param geometry Spatial to attach.
     * @param wipe Whether we reset the HoloNode before we attach geometry.
     */
    public void loadSpatial(Spatial geometry,boolean wipe) {
        if(wipe){
            holoNode.detachAllChildren();
        }
        if (geometry != null) {
            holoNode.attachChild(geometry);
            logger.log(Level.FINEST,"{0} Attached to HoloNode.",geometry.getName());
        }
        else{
            logger.log(Level.SEVERE, "Spatial {0} Couldn't be attached to the HoloNode because it was null.",geometry);
        }
      
    }
    /**
     * Same as above but shorter arguments.
     * Automatically wipes the holoNode when loading Spatials.
     * @param geometry 
     */
    public void loadSpatial(Spatial geometry){
        loadSpatial(geometry, true);
    }
    /**
     * Attach a list of Spatials to the HoloNode.
     * @param spatials Spatials to attach.
     * @param wipe Whether we reset the HoloNode before we attach all the spatials.
     */
    public void loadSpatials(List<Spatial> spatials,boolean wipe){
        if(wipe){
            holoNode.detachAllChildren();
        }
        for(Spatial s:spatials){
            if (s != null) {
                holoNode.attachChild(s);
                logger.log(Level.FINEST,"{0} Attached to HoloNode.",s.getName());
            }else{
                logger.log(Level.SEVERE, "Spatial {0} Couldn't be attached to the HoloNode because it was null.",s);
            }
        }
    }
    /**
     * Move Holonode to contactpoint's location.
     * @param target 
     */
    public void updateLocation(CollisionResult target){
        //If the movement is blocked: don't move.
        if(positionLocked){
            return;
        }
        //Test if target is Terrain.
        if(!UtilityMethods.findUserDataType(target.getGeometry().getParent(),"Terrain")){
            return;
        }
        //Move the HoloNode.
        Vector3f loc = target.getContactPoint();
        holoNode.setLocalTranslation(UtilityMethods.roundVector(loc));
    }
    /**
     * Lock the movement of holoNode.
     */
    public void lockPosition(){
        positionLocked=true;
    }
    /**
     * Raise the HoloNode by 1.
     */
    public void raiseLocation() {
        Vector3f tempVector=holoNode.getLocalTranslation();
        tempVector.y=tempVector.y+1;
        holoNode.setLocalTranslation(tempVector);
    }
    /**
     * Lower the HoloNode by 1.
     */
    public void lowerLocation() {
        Vector3f tempvec=holoNode.getLocalTranslation();
        tempvec.y=tempvec.y-1;
        holoNode.setLocalTranslation(tempvec);
    }
    /**
     * Rotate HoloNode to face some direction.
     * ATTENTION! IS NOT FULLY SUPPORTED YET!
     * @param direction 
     */
    public void rotateDrawed(Direction direction){
        //TODO: THIS METHOD
        Quaternion rot=new Quaternion();
        switch (direction){
            case SOUTH:
                rot.fromAngleAxis(90, Vector3f.UNIT_Y);
                holoNode.setLocalRotation(rot);
                break;
                
            case WEST:
                
                break;
                
            case EAST:
                break;
                
            case NORTH:

        }
    }
    /*
     * EVENTBUS EVENTS
     */
    
    /**
     * Listener to toggle HoloNode rendering.
     * @param event 
     */
    @Subscribe
    public void listenToggleRenderHoloNode(ToggleRenderHoloNodeEvent event){
        toggleRenderHoloNode();
    }
    /**
     * GETTERS AND SETTERS
     */
    public Vector3f getLocation(){
        return holoNode.getLocalTranslation();
    }
}
