/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package intopark.terrain.events;

import com.google.inject.Singleton;
import com.jme3.scene.Spatial;

/**
 *
 * @author arska
 */
@Singleton
public class DeleteSpatialFromMapEvent {

    Spatial o;
    public DeleteSpatialFromMapEvent(Spatial op){
        this.o=op;
    }

    public Spatial getO() {
        return o;
    }
    
}
