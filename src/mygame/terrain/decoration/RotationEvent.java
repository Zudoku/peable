/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.terrain.decoration;

import com.google.inject.Singleton;

/**
 *
 * @author arska
 */
@Singleton
public class RotationEvent {
    private int value;
    private final int who;
    /**
     * 
     * @param v 0==left 1==right
     * @param who 0==decoration 1==road 2==ride/shop
     */
    public RotationEvent(int v,int who){
        this.value=v;
        this.who=who;
    }

    public int getValue() {
        return value;
    }

    public int getWho() {
        return who;
    }
    
    
}
