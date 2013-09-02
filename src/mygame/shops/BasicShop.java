/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.shops;

import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import mygame.npc.Guest;

/**
 *
 * @author arska
 */
public class BasicShop {
    Vector3f position;
    Spatial object;
    
    public BasicShop(Vector3f position,Spatial object){
        this.position=position;
        this.object=object;
    }
    
    public void interact(Guest guest){
        
    }
    
}
