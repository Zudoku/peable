/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package intopark.terrain;

import com.google.inject.Singleton;
import com.jme3.scene.Spatial;

/**
 *
 * @author arska
 */
@Singleton
public class MapContainer {
    private transient Spatial[][][]map;
    private float[] mapData;

    public MapContainer() {
        map = new Spatial[101][25][101];
    }
    
    
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