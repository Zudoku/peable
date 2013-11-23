/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.npc;

import com.jme3.math.Vector3f;
import mygame.Main;
import mygame.shops.BasicShop;

/**
 *
 * @author arska
 */
public class NPCAction {

    private Vector3f movepoint;
    public ActionType action = ActionType.NOTHING;
    private Guest guest;
    private BasicShop shop;

    public NPCAction(Vector3f movePoint, ActionType action,Guest owner) {
        this.movepoint = movePoint;
        this.action = action;
        this.guest=owner;
    }
    public NPCAction(Vector3f movePoint, ActionType action,BasicShop basicshop,Guest guest) {
        this.movepoint = movePoint;
        this.action = action;
        this.shop=basicshop;
        this.guest=guest;
    }
    

    public void onAction() {
        switch (action) {
            case NOTHING:

                break;

            case BUY:
                if(shop==null||guest==null){
                    System.out.println("null shop or guest in buyaction");
                    return;
                }
                shop.interact(guest);
                Main.gamestate.windowMaker.updateGuestWindow(guest);
                break;
                
            case CONSUME:
                if(guest==null){
                    System.out.println("null guest in consumeaction");
                    return;
                }
                if(guest.inventory.isEmpty()==true){
                    
                    return;
                }
                guest.inventory.get(0).consume(guest.stats);
                guest.inventory.remove(guest.inventory.get(0));
                Main.gamestate.windowMaker.updateGuestWindow(guest);
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
