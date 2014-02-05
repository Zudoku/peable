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
public class Archeryrange extends BasicRide{
    public Archeryrange(Vector3f pos,Spatial object,Direction facing,Node rootNode){
        super(pos,new CustomAnimation(object),200,facing,"Archeryrange",rootNode);
        rideType= PreferredRides.LOW;
        setName("My Archeryrange1");
        setRideType("archery");
        Random r =new Random();
        eventBus.post(new AddGuestLimitEvent(r.nextInt(10)+10));
    }
}
