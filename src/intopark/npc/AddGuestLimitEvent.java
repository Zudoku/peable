/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package intopark.npc;

import com.google.inject.Singleton;

/**
 *
 * @author arska
 */
@Singleton
public class AddGuestLimitEvent {
    private int m;
    public AddGuestLimitEvent(int m){
        this.m=m;
    }

    public int getM() {
        return m;
    }
    
}
