/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.terrain.events;

import mygame.ride.BasicRide;

/**
 *
 * @author arska
 */
public class RideDemolishEvent {
    private final BasicRide r;
    public RideDemolishEvent(BasicRide r){
        this.r=r;
    }
    public BasicRide getRide(){
        return r;
    }
}
