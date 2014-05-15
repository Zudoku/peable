/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package intopark.ride.actualrides;

import com.jme3.scene.Spatial;
import java.util.ArrayList;
import java.util.Random;
import intopark.npc.events.AddGuestLimitEvent;
import intopark.npc.inventory.RideType;
import intopark.ride.BasicRide;
import intopark.ride.CustomAnimation;
import intopark.util.Direction;
import intopark.util.MapPosition;
import java.util.List;

/**
 *
 * @author arska
 */
public class PirateShip extends BasicRide{
    
    public PirateShip(MapPosition pos,CustomAnimation moving,List<Spatial> staticParts,Direction direction){
        super(pos,moving,staticParts,400,direction,"Pirate Ship");
        setRideType(RideType.HIGH);
        setName("My PirateShip1");
        setRide("pirateship");
        Random r =new Random();
        eventBus.post(new AddGuestLimitEvent(r.nextInt(10)+10));
        
    }
}
