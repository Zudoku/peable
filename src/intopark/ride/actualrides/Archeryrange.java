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
public class Archeryrange extends BasicRide{
    public Archeryrange(MapPosition pos,CustomAnimation moving,List<Spatial> staticParts,Direction direction){
        super(pos,moving,staticParts,200,direction,"Archeryrange");
        setRideType(RideType.LOW);
        setName("My Archeryrange1");
        setRide("archery");
        Random r =new Random();
        eventBus.post(new AddGuestLimitEvent(r.nextInt(10)+10));
    }
}
