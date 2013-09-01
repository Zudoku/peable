/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.npc;

import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import java.util.ArrayList;

/**
 *
 * @author arska
 */
public class BasicNPC {

    private String name;
    private Spatial object;

    public BasicNPC(String name, Spatial object) {
        this.name = name;
        this.object = object;

    }

    public Spatial getGeometry() {
        return object;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null) {
            System.out.println("Guest name is empty");
            return;

        }
        this.name = name;
    }

    public void update() {
    }

    public void move(NPCAction action, ArrayList<NPCAction> npcActions) {
        float tarx = 0;
        float tary = 0;
        float tarz = 0;
        
        if (-(object.getWorldTranslation().x - action.getMovePoint().x) > 0) {
            tarx = (0.02f);
        }
        if (-(object.getWorldTranslation().x - action.getMovePoint().x) < 0) {
            tarx = (-0.02f);
        }
        if (-(object.getWorldTranslation().y - action.getMovePoint().y) > 0) {
            tary = (0.02f);
        }
        if (-(object.getWorldTranslation().y - action.getMovePoint().y) < 0) {
            tary = (-0.02f);
        }
        if (-(object.getWorldTranslation().z - action.getMovePoint().z) > 0) {
            tarz = (0.02f);
        }
        if (-(object.getWorldTranslation().z - action.getMovePoint().z) < 0) {
            tarz = (-0.02f);
        }

        
        
        object.move(tarx,tary,tarz);
        System.out.println(object.getWorldTranslation().x+" "+object.getWorldTranslation().y+" "+object.getWorldTranslation().z);
        if(action.getMovePoint().distance(object.getWorldTranslation())<0.2){
            action.onAction();
            System.out.println("NPCAction completed");
            npcActions.remove(action);
        }
        
        
    }
}
