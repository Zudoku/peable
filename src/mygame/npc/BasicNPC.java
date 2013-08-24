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
            System.out.println("NAME IS NULL");
            return;

        }
        this.name = name;
    }

    public void update() {
    }

    public void move(Vector3f point, ArrayList<Vector3f> movePoints) {
        float tarx = 0;
        float tary = 0;
        float tarz = 0;
        
        if (-(object.getWorldTranslation().x - point.x) > 0) {
            tarx = (0.02f);
        }
        if (-(object.getWorldTranslation().x - point.x) < 0) {
            tarx = (-0.02f);
        }
        if (-(object.getWorldTranslation().y - point.y) > 0) {
            tary = (0.02f);
        }
        if (-(object.getWorldTranslation().y - point.y) < 0) {
            tary = (-0.02f);
        }
        if (-(object.getWorldTranslation().z - point.z) > 0) {
            tarz = (0.02f);
        }
        if (-(object.getWorldTranslation().z - point.z) < 0) {
            tarz = (-0.02f);
        }

        
        //System.out.println(location.x + " " + location.y + " " + location.z + " name" + getName());
        object.move(tarx,tary,tarz);
        System.out.println((point.x - point.x % 0.2) + "   " + (object.getWorldTranslation().x - object.getWorldTranslation().x % 0.2)+"  "+(point.y - point.y % 0.2) + "   " + (object.getWorldTranslation().y - object.getWorldTranslation().y % 0.2)+"  "+(point.z - point.z % 0.2) + "   " + (object.getWorldTranslation().z - object.getWorldTranslation().z % 0.2));
        if ((point.x - point.x % 0.2) == (object.getWorldTranslation().x - object.getWorldTranslation().x % 0.2)&&(point.y - point.y % 0.2) == (object.getWorldTranslation().y - object.getWorldTranslation().y % 0.2)&&(point.z - point.z % 0.2) == (object.getWorldTranslation().z - object.getWorldTranslation().z % 0.2)) {
            movePoints.remove(point);

        }
        Vector3f location = new Vector3f(object.getLocalTranslation());
    }
}
