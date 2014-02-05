/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.ride.actualrides;

import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import java.util.Random;
import mygame.npc.AddGuestLimitEvent;
import mygame.npc.inventory.PreferredRides;
import mygame.ride.BasicRide;
import mygame.ride.CustomAnimation;
import mygame.terrain.Direction;

/**
 *
 * @author arska
 */
public class HauntedHouse extends BasicRide{
    public HauntedHouse(Vector3f position, Spatial object, float cost, Direction facing,Node rootNode) {
        super(position,new CustomAnimation(object), cost, facing,"hauntedHouse",rootNode);
        rideType= PreferredRides.MEDIUM;
        setName("My HauntedHouse1");
        setRideType("hhouse");
        Random r =new Random();
        eventBus.post(new AddGuestLimitEvent(r.nextInt(10)+10));
    }
}
