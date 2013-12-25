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
public class SpinWheel extends BasicRide{
    Spatial swing;
    float curAngle=0;
    
    public SpinWheel(Vector3f pos,Spatial object,Direction facing,Node rootNode,Spatial swing){
        super(pos,object,400,facing,"SpinWheel",rootNode);
        this.swing=swing;
        rideType= PreferredRides.HIGH;
        setName("My Spinwheel1");
        setRideType("spinwheel");
        Random r =new Random();
        eventBus.post(new AddGuestLimitEvent(r.nextInt(10)+10));
        swing.setLocalTranslation(pos);
        
    }
    @Override
    public void update(){
        curAngle += 0.1;
        float angle = (float) Math.toRadians(curAngle);
        swing.rotate(0,0, angle);
    }
}
