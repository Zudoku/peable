/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package intopark.terrain.decoration;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.jme3.asset.AssetManager;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import java.util.logging.Logger;
import intopark.GUI.events.UpdateMoneyTextBarEvent;
import intopark.UtilityMethods;
import intopark.terrain.events.AddObjectToMapEvent;
import intopark.terrain.Direction;
import intopark.terrain.ParkHandler;
import intopark.terrain.events.PayParkEvent;

/**
 *
 * @author arska
 */
@Singleton
public class DecorationManager {
    //LOGGER
    private static final Logger logger = Logger.getLogger(DecorationManager.class.getName());
    //DEPENDENCIES
    private ParkHandler parkHandler;
    private final EventBus eventBus;
    private DecorationFactory decoFactory;
    //OWNS
    private Node decorationNode;
    //VARIABLES
    Direction direction = Direction.EAST;
    Decorations decoration = Decorations.ROCK;
    float price=0;
    
    
    
    /**
     * This is manager that controls deploying decoration objects to the park.
     * @param rootNode This is used to attach the decorations to world.
     * @param assetManager This is used to load Models.
     * @param eventBus This is used to listen to turn-events.
     * @param parkHandler This is used to access building space.
     */
    @Inject
    public DecorationManager(Node rootNode, AssetManager assetManager,EventBus eventBus,ParkHandler parkHandler) {
        this.eventBus=eventBus;
        this.parkHandler=parkHandler;
        
        eventBus.register(this);
        decorationNode = new Node("decorationNode");
        rootNode.attachChild(decorationNode);
        decoFactory = new DecorationFactory(assetManager);

    }
    /**
     * Called from the UI. Used to select decoration.
     * @param decoratios Decoration to select.
     */
    
    public void select(Decorations decoratios){
        this.decoration=decoratios;
    }
    /**
     * Constructs the selected decoration to given location.
     * @param loc Location to put the decoration.
     */
    
    public void build(Vector3f loc) {
        if(loc.x<1||loc.x>parkHandler.getMapHeight()){
            return;
        }
        if(loc.z<1||loc.x>parkHandler.getMapWidth()){
            return;
        }
        Spatial decobject = null;
        float angle;
        switch (decoration) {
            case ROCK:
                decobject=decoFactory.getRock();
                price=25;
                break;
        }
        switch (direction) {
            case NORTH:
                angle = (float) Math.toRadians(0);
                decobject.rotate(0, angle, 0);
                break;

            case SOUTH:
                angle = (float) Math.toRadians(180);
                decobject.rotate(0, angle, 0);
                break;

            case EAST:
                angle = (float) Math.toRadians(90);
                decobject.rotate(0, angle, 0);
                break;

            case WEST:
                angle = (float) Math.toRadians(270);
                decobject.rotate(0, angle, 0);

        }
        decobject.setUserData("type", "decoration");
        decobject.setUserData("direction",direction.toString());
        
        int x1=(int)(loc.x+UtilityMethods.HALFTILE);
        int y1=(int)(loc.y+UtilityMethods.HALFTILE);
        int z1=(int)(loc.z+UtilityMethods.HALFTILE);
        decobject.setLocalTranslation(x1,y1,z1);
        eventBus.post(new AddObjectToMapEvent(x1, y1, z1, decobject));
        eventBus.post(new PayParkEvent(price));
        eventBus.post(new UpdateMoneyTextBarEvent());
        decorationNode.attachChild(decobject);
    }
    /**
     * Called from the UI. Returns image path depending on direction.
     * @return image path 
     */
    public String getArrow() {
        if(direction==Direction.NORTH){
            return "Interface/Roads/up.png";
        }
        if(direction==Direction.EAST){
            return "Interface/Roads/right.png";
        }
        if(direction==Direction.WEST){
            return "Interface/Roads/left.png";
        }
        if(direction==Direction.SOUTH){
            return "Interface/Roads/down.png";
        }
        else{
            return "bug !";
        }
    }
    /**
     * Turn decoration placement 90 degrees to left.
     */
    public void turnLeft() {
        if(direction==Direction.NORTH){
            direction=Direction.WEST;
            return;
        }
        if(direction==Direction.WEST){
            direction=Direction.SOUTH;
            return;
        }
        if(direction==Direction.SOUTH){
            direction=Direction.EAST;
            return;
        }
        if(direction==Direction.EAST){
            direction=Direction.NORTH;
        }
    }
    /**
     * Turn decoration placement 90 degrees to right.
     */
    public void turnRight(){
        if(direction==Direction.NORTH){
            direction=Direction.EAST;
            return;
        }
        if(direction==Direction.EAST){
            direction=Direction.SOUTH;
            return;
        }
        if(direction==Direction.SOUTH){
            direction=Direction.WEST;
            return;
        }
        if(direction==Direction.WEST){
            direction=Direction.NORTH;
        }
    }
    /**
     * Called from the UI. Returns the name of current selected decoration.
     * @return name of currently selected decoration.
     */
    public String getDecorationName() {
        switch(decoration){
            case BUSH:
                return "Bush";
                
                
            case ROCK:
                return "Rock";
    
        }
        return "bug !";
    }
    /**
     * Called from the UI. Returns the description of current selected decoration.
     * @return description of currently selected decoration.
     */
    public String getDecorationDescription(){
        switch(decoration){
            case BUSH:
                
                return "Nice Bush";
                
            case ROCK:
                
                return "Big rock. Its hard!";
        }
        return "bug !";
    }
    /**
     * Listens for rotation events and turns the decoration if needed.
     * @param event RotationEvent.
     */
    @Subscribe public void listenRotationEvents(RotationEvent event){
        if(event.getWho()==0){
            if(event.getValue()==1){
                turnRight();
            }
            else{
                turnLeft();
            }
            
            
        }
    }
}
