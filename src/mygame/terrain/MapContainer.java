/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.terrain;

import com.google.inject.Singleton;
import com.jme3.scene.Spatial;

/**
 *
 * @author arska
 */
@Singleton
public class MapContainer {
    private Spatial[][][]map;
    private float[] mapData;
    
    public Spatial[][][]getMap(){
        return map;
    }
    public void setMap(Spatial[][][]newMap){
        this.map=newMap;
    }
    public void setMapData(float[]a){
        mapData=a;
    }
    public float[]getMapData(){
        return mapData;
    }
}
