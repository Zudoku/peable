/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.ride.actualrides;

import com.jme3.scene.Spatial;
import java.util.ArrayList;
import java.util.Random;
import mygame.npc.AddGuestLimitEvent;
import mygame.npc.inventory.RideType;
import mygame.ride.BasicRide;
import mygame.ride.CustomAnimation;
import mygame.terrain.Direction;
import mygame.terrain.MapPosition;

/**
 *
 * @author arska
 */
public class ChessCenter extends BasicRide{
    
    public ChessCenter(MapPosition position,CustomAnimation moving,ArrayList<Spatial> staticParts,Direction facing) {
        super(position,moving,staticParts,500, facing,"Chesslair");
        rideType= RideType.LOW;
        setName("My Chesscentre1");
        setRideType("chess");
        Random r =new Random();
        eventBus.post(new AddGuestLimitEvent(r.nextInt(10)+10));
    }
    
}
