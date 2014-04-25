/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package intopark.npc.events;

import com.jme3.scene.Spatial;
import intopark.npc.Guest;
import java.util.List;
import intopark.npc.inventory.Item;
import intopark.npc.inventory.StatManager;
import intopark.npc.inventory.Wallet;
import intopark.terrain.Direction;

/**
 *
 * @author arska
 */
public class CreateGuestEvent {
    public Guest g;

    public CreateGuestEvent(Guest g) {
        this.g = g;
    }

    public CreateGuestEvent(Wallet wallet,List<Item>inv, int guestNum, Direction moving, int x1, int y1, int z1, StatManager stats,Spatial model, String name) {
        g=new Guest(wallet, guestNum, moving, x1, y1, z1, stats,model, name);
        g.setInventory(inv);
        /*TEMP FIX*/
        if(moving==null){
            g.setMoving(Direction.NORTH);
        }
    }
    
    
}
