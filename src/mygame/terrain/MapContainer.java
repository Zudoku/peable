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
    private int[][]mapData;
    
    public Spatial[][][]getMap(){
        return map;
    }
    public void setMap(Spatial[][][]newMap){
        this.map=newMap;
    }
    public void setMapData(int[][]a){
        mapData=a;
    }
    public int[][]getMapData(){
        return mapData;
    }
    
}
