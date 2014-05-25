/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package intopark.npc;

import com.jme3.scene.Spatial;
import java.util.List;
import java.util.logging.Logger;

/**
 *
 * @author arska
 */
public class BasicNPC {
    //LOGGER
    protected static final Logger logger = Logger.getLogger(BasicNPC.class.getName());
    //VARIABLES
    private String name;
    private  transient Spatial object;

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
            
            return;

        }
        this.name = name;
    }

    public void update() {
    }

    public void move(NPCAction action, List<NPCAction> npcActions) {
        //TODO: REWORK
        float tarx = 0;
        float tary = 0;
        float tarz = 0;
        //x
        if (-(object.getWorldTranslation().x - action.getMovePoint().x) > 0) {
            tarx = (0.02f);
        }
        if (-(object.getWorldTranslation().x - action.getMovePoint().x) < 0) {
            tarx = (-0.02f);
        }
        //y
        if (-(object.getWorldTranslation().y - action.getMovePoint().y) > 0) {
            tary = (0.02f);
        }
        if (-(object.getWorldTranslation().y - action.getMovePoint().y) < 0) {
            tary = (-0.02f);
        }
        //z
        if (-(object.getWorldTranslation().z - action.getMovePoint().z) > 0) {
            tarz = (0.02f);
        }
        if (-(object.getWorldTranslation().z - action.getMovePoint().z) < 0) {
            tarz = (-0.02f);
        }

        object.move(tarx,tary,tarz);

        if(action.getMovePoint().distance(object.getWorldTranslation())<0.2){
            action.onAction();
            
            npcActions.remove(action);
        }
        
        
    }
}
