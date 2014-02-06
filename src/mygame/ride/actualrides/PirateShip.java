/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.ride.actualrides;

import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
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
public class PirateShip extends BasicRide{
    
    public PirateShip(MapPosition pos,CustomAnimation moving,ArrayList<Spatial> staticParts,Direction facing,Node rootNode){
        super(pos,moving,staticParts,400,facing,"Pirate Ship",rootNode);
        rideType= RideType.HIGH;
        setName("My PirateShip1");
        setRideType("pirateship");
        Random r =new Random();
        eventBus.post(new AddGuestLimitEvent(r.nextInt(10)+10));
        
    }
}
