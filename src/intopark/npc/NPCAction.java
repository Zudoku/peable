/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package intopark.npc;

import com.google.common.eventbus.EventBus;
import intopark.UtilityMethods;
import intopark.npc.events.NPCLeaveEvent;
import java.util.logging.Level;
import java.util.logging.Logger;
import intopark.util.MapPosition;

/**
 *
 * @author arska
 */
public class NPCAction {
    private static final Logger logger = Logger.getLogger(NPCAction.class.getName());
    //VARIABLES
    private MapPosition movepoint;
    private ActionType action = ActionType.NOTHING;
    private boolean setPosition;
    private BasicNPC owner;
    /**
     * NPCs consume these actions
     * @param movePoint Where to move?
     * @param action what kind of action?
     * @param owner who completes this action?
     * @param setPosition When this action completes, do you want to set the owners position to action movePoint.
     */
    public NPCAction(MapPosition movePoint, ActionType action,BasicNPC owner,boolean setPosition) {
        this.movepoint = movePoint;
        this.action = action;
        this.owner=owner;
        this.setPosition = setPosition;
    }

    /**
     * What happens when NPC consumes this action?
     */
    public void onAction() {
        switch (action) {
            case NOTHING:
                //Probably move action
                break;

            case BUY:
                if(owner==null ||!(owner instanceof Guest)){
                    logger.log(Level.WARNING,"NPCAction was corrupted when trying to consume it !");
                    return;
                }
                break;

            case CONSUME:
                if(owner==null ||!(owner instanceof Guest)){
                    logger.log(Level.WARNING,"NPCAction was corrupted when trying to consume it !");
                    return;
                }
                Guest guest = (Guest)owner;
                if(guest.getInventory().isEmpty()==true){

                    return;
                }
                guest.getInventory().get(0).consume(guest.getStats());
                guest.getInventory().remove(guest.getInventory().get(0));
                break;

            case INSPECT:
            case EXIT:
                //NPC is supposed to leave the park
                NPCLeaveEvent npcleaveEvent = new NPCLeaveEvent(owner);
                UtilityMethods.publishEvent(npcleaveEvent);
                break;
        }
        if(setPosition){
            owner.getPosition().setX(movepoint.getX());
            owner.getPosition().setY(movepoint.getY());
            owner.getPosition().setZ(movepoint.getZ());
        }
    }

    public MapPosition getMovePoint() {

        return movepoint;
    }
}
