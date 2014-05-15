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
public class HauntedHouse extends BasicRide{
    public HauntedHouse(MapPosition position,CustomAnimation moving,List<Spatial> staticParts, Direction direction) {
        super(position,moving,staticParts,370, direction,"hauntedHouse");
        setRideType(RideType.MEDIUM);
        setName("My HauntedHouse1");
        setRide("hhouse");
        Random r =new Random();
        eventBus.post(new AddGuestLimitEvent(r.nextInt(10)+10));
    }
}
