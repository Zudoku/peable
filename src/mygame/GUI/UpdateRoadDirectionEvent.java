/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.GUI;

import mygame.terrain.Direction;

/**
 *
 * @author arska
 */
public class UpdateRoadDirectionEvent {
    Direction d;
    public UpdateRoadDirectionEvent(Direction d){
        this.d=d;
        
    }

    public Direction getD() {
        return d;
    }
    
}
