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
import mygame.terrain.Direction;

/**
 *
 * @author arska
 */
public class Blender extends BasicRide{
    public Blender(Vector3f pos,Spatial object,Direction facing,Node rootNode){
        super(pos,object,350,facing,"Blender",rootNode);
        rideType= PreferredRides.NAUSEA;
        setName("My blender1");
        setRideType("blender");
        Random r =new Random();
        eventBus.post(new AddGuestLimitEvent(r.nextInt(10)+10));
    }
}
