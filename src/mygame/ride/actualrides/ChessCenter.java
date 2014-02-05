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
public class ChessCenter extends BasicRide{
    
    public ChessCenter(Vector3f position, Spatial object, float cost, Direction facing,Node rootNode) {
        super(position, new CustomAnimation(object), cost, facing,"Chesslair",rootNode);
        rideType= PreferredRides.LOW;
        setName("My Chesscentre1");
        setRideType("chess");
        Random r =new Random();
        eventBus.post(new AddGuestLimitEvent(r.nextInt(10)+10));
    }
    
}
