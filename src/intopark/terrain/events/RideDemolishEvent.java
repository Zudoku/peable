/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package intopark.terrain.events;

import com.google.inject.Singleton;
import intopark.ride.BasicRide;

/**
 *
 * @author arska
 */
@Singleton
public class RideDemolishEvent {
    private final BasicRide r;
    public RideDemolishEvent(BasicRide r){
        this.r=r;
    }
    public BasicRide getRide(){
        return r;
    }
}