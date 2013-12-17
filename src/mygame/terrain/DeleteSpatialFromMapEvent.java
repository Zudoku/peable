/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.terrain;

import com.jme3.scene.Spatial;

/**
 *
 * @author arska
 */
public class DeleteSpatialFromMapEvent {

    Spatial o;
    public DeleteSpatialFromMapEvent(Spatial op){
        this.o=op;
    }

    public Spatial getO() {
        return o;
    }
    
}
