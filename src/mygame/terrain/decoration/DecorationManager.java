/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.terrain.decoration;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.jme3.asset.AssetManager;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import mygame.GUI.UpdateMoneyTextBarEvent;
import mygame.terrain.AddObjectToMapEvent;
import mygame.terrain.Direction;
import mygame.terrain.PayParkEvent;

/**
 *
 * @author arska
 */
@Singleton
public class DecorationManager {

    Node rootNode;
    Node decorationNode;
    Direction direction = Direction.RIGHT;
    Decorations decoration = Decorations.ROCK;
    DecorationFactory decoFactory;
    float price=0;
    private final EventBus eventBus;
    
    public static final float HALFTILE = 0.4999f;
    
    

    @Inject
    public DecorationManager(Node rootNode, AssetManager assetManager,EventBus eventBus) {
        this.rootNode = rootNode;
        this.eventBus=eventBus;
        
        eventBus.register(this);
        decorationNode = new Node("decorationNode");
        rootNode.attachChild(decorationNode);
        decoFactory = new DecorationFactory(assetManager);

    }
    public void select(Decorations decoratios){
        this.decoration=decoratios;
    }
    
    public void build(Vector3f loc) {
        Spatial decobject = null;
        float angle;
        switch (decoration) {
            case ROCK:
                decobject=decoFactory.getRock();
                price=25;
                break;
        }
        switch (direction) {
            case UP:
                angle = (float) Math.toRadians(0);
                decobject.rotate(0, angle, 0);
                break;

            case DOWN:
                angle = (float) Math.toRadians(180);
                decobject.rotate(0, angle, 0);
                break;

            case RIGHT:
                angle = (float) Math.toRadians(90);
                decobject.rotate(0, angle, 0);
                break;

            case LEFT:
                angle = (float) Math.toRadians(270);
                decobject.rotate(0, angle, 0);

        }
        decobject.setUserData("type", "decoration");
        decobject.setUserData("direction",direction.toString());
        
        int x1=(int)(loc.x+HALFTILE);
        int y1=(int)(loc.y+HALFTILE);
        int z1=(int)(loc.z+HALFTILE);
        decobject.setLocalTranslation(x1,y1,z1);
        eventBus.post(new AddObjectToMapEvent(x1, y1, z1, decobject));
        eventBus.post(new PayParkEvent(price));
        eventBus.post(new UpdateMoneyTextBarEvent());
        decorationNode.attachChild(decobject);
    }

    public String getArrow() {
        if(direction==Direction.UP){
            return "Interface/Roads/up.png";
        }
        if(direction==Direction.RIGHT){
            return "Interface/Roads/right.png";
        }
        if(direction==Direction.LEFT){
            return "Interface/Roads/left.png";
        }
        if(direction==Direction.DOWN){
            return "Interface/Roads/down.png";
        }
        else{
            return "bug !";
        }
    }

    public void turnLeft() {
        if(direction==Direction.UP){
            direction=Direction.LEFT;
            return;
        }
        if(direction==Direction.LEFT){
            direction=Direction.DOWN;
            return;
        }
        if(direction==Direction.DOWN){
            direction=Direction.RIGHT;
            return;
        }
        if(direction==Direction.RIGHT){
            direction=Direction.UP;
        }
    }
    public void turnRight(){
        if(direction==Direction.UP){
            direction=Direction.RIGHT;
            return;
        }
        if(direction==Direction.RIGHT){
            direction=Direction.DOWN;
            return;
        }
        if(direction==Direction.DOWN){
            direction=Direction.LEFT;
            return;
        }
        if(direction==Direction.LEFT){
            direction=Direction.UP;
        }
    }

    public String getDecorationName() {
        switch(decoration){
            case BUSH:
                return "Bush";
                
                
            case ROCK:
                return "Rock";
    
        }
        return "bug !";
    }
    public String getDecorationDescription(){
        switch(decoration){
            case BUSH:
                
                return "Nice Bush";
                
            case ROCK:
                
                return "Big rock. Its hard!";
        }
        return "bug !";
    }
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
