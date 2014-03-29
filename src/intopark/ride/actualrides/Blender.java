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
import intopark.terrain.Direction;
import intopark.terrain.MapPosition;
import java.util.List;

/**
 *
 * @author arska
 */
public class Blender extends BasicRide{
    public Blender(MapPosition pos,CustomAnimation moving,List<Spatial> staticParts,Direction facing){
        super(pos,moving,staticParts,350,facing,"Blender");
        setRideType(RideType.NAUSEA);
        setName("My blender1");
        setRide("blender");
        Random r =new Random();
        eventBus.post(new AddGuestLimitEvent(r.nextInt(10)+10));
    }
}
