/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.terrain.events;

import com.google.inject.Singleton;

/**
 *
 * @author arska
 */
@Singleton
public class SetMapDataEvent {
    private float [] mapData;
    public SetMapDataEvent(float[] data) {
        mapData=data;
    }

    public float[] getMapData() {
        return mapData;
    } 
}
