/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package intopark.ride.actualrides;

import com.jme3.scene.Spatial;
import java.util.ArrayList;
import java.util.Random;
import intopark.npc.AddGuestLimitEvent;
import intopark.npc.inventory.RideType;
import intopark.ride.BasicRide;
import intopark.ride.CustomAnimation;
import intopark.terrain.Direction;
import intopark.terrain.MapPosition;

/**
 *
 * @author arska
 */
public class PirateShip extends BasicRide{
    
    public PirateShip(MapPosition pos,CustomAnimation moving,ArrayList<Spatial> staticParts,Direction facing){
        super(pos,moving,staticParts,400,facing,"Pirate Ship");
        rideType= RideType.HIGH;
        setName("My PirateShip1");
        setRideType("pirateship");
        Random r =new Random();
        eventBus.post(new AddGuestLimitEvent(r.nextInt(10)+10));
        
    }
}
