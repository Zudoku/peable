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
public class Blender extends BasicRide{
    public Blender(MapPosition pos,CustomAnimation moving,ArrayList<Spatial> staticParts,Direction facing){
        super(pos,moving,staticParts,350,facing,"Blender");
        rideType= RideType.NAUSEA;
        setName("My blender1");
        setRideType("blender");
        Random r =new Random();
        eventBus.post(new AddGuestLimitEvent(r.nextInt(10)+10));
    }
}
