/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.npc;

import com.jme3.math.Vector3f;

/**
 *
 * @author arska
 */
public class NPCAction {

    private Vector3f movepoint;
    public ActionType action = ActionType.NOTHING;

    public NPCAction(Vector3f movePoint, ActionType action) {
        this.movepoint = movePoint;
        this.action = action;
    }

    public void onAction() {
        switch (action) {
            case NOTHING:

                break;

            case BUY:

                break;
        }
    }

    public Vector3f getMovePoint() {

        return movepoint;
    }
}
