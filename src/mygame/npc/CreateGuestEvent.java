/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.npc;

import com.jme3.scene.Spatial;
import java.util.ArrayList;
import java.util.List;
import mygame.npc.inventory.Item;
import mygame.npc.inventory.StatManager;
import mygame.npc.inventory.Wallet;
import mygame.terrain.Direction;

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
    }
    
    
}
