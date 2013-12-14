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
public class AddObjectToMapEvent {
    private int x;
    private int y;
    private int z;
    private Spatial o;
    public AddObjectToMapEvent(int x,int y,int z,Spatial o){
        this.x=x;
        this.y=y;
        this.z=z;
        this.o=o;
    }

    public Spatial getO() {
        return o;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }
    
}
