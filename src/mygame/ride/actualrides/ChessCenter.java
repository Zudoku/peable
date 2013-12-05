/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.ride.actualrides;

import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import mygame.npc.inventory.PreferredRides;
import mygame.ride.BasicRide;
import mygame.terrain.Direction;

/**
 *
 * @author arska
 */
public class ChessCenter extends BasicRide{

    public ChessCenter(Vector3f position, Spatial object, float cost, Direction facing) {
        super(position, object, cost, facing,"Chesslair");
        rideType= PreferredRides.LOW;
        setName("My Chesscentre1");
        setRideType("chess");
        
    }
    
}
