/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package intopark.npc;

import com.google.inject.Inject;
import com.jme3.math.Vector3f;
import java.util.logging.Level;
import java.util.logging.Logger;
import intopark.GUI.WindowHandler;
import intopark.shops.BasicShop;

/**
 *
 * @author arska
 */
public class NPCAction {
    private static final Logger logger = Logger.getLogger(NPCAction.class.getName());
    private Vector3f movepoint;
    public ActionType action = ActionType.NOTHING;
    private Guest guest;
    private BasicShop shop;
    @Inject WindowHandler windowMaker;
    /**
     * NPCs consume these actions
     * @param movePoint Where to move?
     * @param action what kind of action?
     * @param owner who completes this action?
     */
    public NPCAction(Vector3f movePoint, ActionType action,Guest owner) {
        this.movepoint = movePoint;
        this.action = action;
        this.guest=owner;
    }
    /**
     * Buyaction constructor
     * @param movePoint Where to move?
     * @param action what kind of action?
     * @param basicshop where to buy stuff?
     * @param guest who completes this action?
     */
    public NPCAction(Vector3f movePoint, ActionType action,BasicShop basicshop,Guest guest) {
        this.movepoint = movePoint;
        this.action = action;
        this.shop=basicshop;
        this.guest=guest;
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
                if(shop==null||guest==null){
                    logger.log(Level.WARNING,"NPCAction was corrupted when trying to consume it !");
                    return;
                }
                shop.interact(guest);
                //windowMaker.updateGuestWindow(guest);
                break;
                
            case CONSUME:
                if(guest==null){
                    logger.log(Level.WARNING,"NPCAction was corrupted when trying to consume it !");
                    return;
                }
                if(guest.getInventory().isEmpty()==true){
                    
                    return;
                }
                guest.getInventory().get(0).consume(guest.getStats());
                guest.getInventory().remove(guest.getInventory().get(0));
                //windowMaker.updateGuestWindow(guest);
                break;
                
            case QUE:
                
                break;
        }
    }

    public Vector3f getMovePoint() {

        return movepoint;
    }
    public void buyAction(BasicShop shop,Guest guest){
        this.guest=guest;
        this.shop=shop;
    }
}
