/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.terrain.events;

import com.google.inject.Singleton;
import com.jme3.scene.Spatial;

/**
 *
 * @author arska
 */
@Singleton
public class SetMapEvent {
    Spatial[][][]map;
    public SetMapEvent(Spatial[][][]map){
        this.map=map;
    }

    public Spatial[][][] getMap() {
        return map;
    }
    
}
