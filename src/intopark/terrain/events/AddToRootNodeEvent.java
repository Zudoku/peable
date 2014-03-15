/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package intopark.terrain.events;

import com.jme3.scene.Spatial;

/**
 *
 * @author arska
 */
public class AddToRootNodeEvent {
    public Object spatial;

    public AddToRootNodeEvent(Object spatial) {
        this.spatial = spatial;
    }
    
}
