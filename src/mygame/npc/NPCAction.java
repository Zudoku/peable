/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.npc;

import com.jme3.math.Vector3f;
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

    public NPCAction(Vector3f movePoint, ActionType action) {
        this.movepoint = movePoint;
        this.action = action;
    }

    public void onAction() {
        switch (action) {
            case NOTHING:

                break;

            case BUY:
                if(shop==null||guest==null){
                    System.out.println("null shop or guest in action");
                    return;
                }
                shop.interact(guest);
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
