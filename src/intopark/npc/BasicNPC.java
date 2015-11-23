/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package intopark.npc;

import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import intopark.util.Direction;
import intopark.util.MapPosition;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import sun.misc.FloatingDecimal;

/**
 *
 * @author arska
 */
public class BasicNPC {
    //LOGGER
    protected static final Logger logger = Logger.getLogger(BasicNPC.class.getName());
    //VARIABLES
    protected MapPosition position;
    protected transient List<NPCAction> actions = new ArrayList<>(); //Npcs actions. Determine where the NPC moves and what does he do.
    protected Direction moving = Direction.NORTH; //What direction NPC is moving.
    private String name;
    private transient Spatial object;
    protected double walkingSpeed;
    protected int ID;

    public BasicNPC(String name, Spatial object,double walkingspeed,int ID,MapPosition position) {
        this.name = name;
        this.object = object;
        this.ID=ID;
        this.position= position;
        this.walkingSpeed = walkingspeed;
    }

    public void update() {
        move();
    }

    public void move() {
        if(actions.isEmpty()){
            return;
        }
        NPCAction action = actions.get(0);
        //TODO: REWORK
        float tarx = 0;
        float tary = 0;
        float tarz = 0;


        tarx = (float) calculateWhereToMove(object.getWorldTranslation().x,action.getMovePoint().getAllX(),action.getMovePoint().getVector());
        tary = (float) calculateWhereToMove(object.getWorldTranslation().y,action.getMovePoint().getAllY(),action.getMovePoint().getVector());
        tarz = (float) calculateWhereToMove(object.getWorldTranslation().z,action.getMovePoint().getAllZ(),action.getMovePoint().getVector());


        object.move(tarx,tary,tarz);

        if(action.getMovePoint().getVector().distance(object.getWorldTranslation())<0.2){
            action.onAction();

            actions.remove(action);
        }


    }
    private double calculateWhereToMove(float npcCoordinate, float actionCoordinate,Vector3f actionMovepoint){

        double worldWalkingSpeed = (walkingSpeed / 100)/5 ;

        if (npcCoordinate - actionCoordinate < 0) {
            if(actionMovepoint.distance(object.getWorldTranslation())<worldWalkingSpeed){
                //Float distanceToMove = new Float(-(npcCoordinate- actionCoordinate)); CAUSES JITTER :X
                return 0;//new FloatingDecimal(distanceToMove.floatValue()).doubleValue();
            }else{
                return worldWalkingSpeed;
            }
        }else if(npcCoordinate- actionCoordinate > 0){
            if(actionMovepoint.distance(object.getWorldTranslation())<worldWalkingSpeed){
                //Float distanceToMove = new Float(-(npcCoordinate- actionCoordinate));
                return 0;//new FloatingDecimal(distanceToMove.floatValue()).doubleValue();
            }else{
                return -worldWalkingSpeed;
            }
        }else {
            return 0d;
        }
    }
    /**
     * GETTERS AND SETTERS
     */
    public Spatial getGeometry() {
        return object;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null) {

            return;

        }
        this.name = name;
    }

    public List<NPCAction> getActions() {
        return actions;
    }

    public int getID() {
        return ID;
    }
    public Direction getMoving() {
        return moving;
    }

    public Spatial getObject() {
        return object;
    }

    public void setMoving(Direction moving) {
        this.moving = moving;
    }
    public void setActions(List<NPCAction> actions) {
        this.actions = actions;
    }

    public MapPosition getPosition() {
        return position;
    }

    public void setPosition(MapPosition position) {
        this.position = position;
    }


}
