/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.shops;

import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import mygame.npc.Guest;
import mygame.npc.inventory.Item;
import mygame.npc.inventory.Itemtypes;

/**
 *
 * @author arska
 */
public class Meatballshop extends BasicShop{
     
    float price=5;
    String productname="Meatballs";
    
    public Meatballshop(Vector3f position,Spatial geom){
        super(position,geom);
        
        
        
    }

    @Override
    public void interact(Guest guest) {
        if(guest.wallet.canAfford(price)){
            guest.inventory.add(new Item(productname, Itemtypes.FOOD));
            guest.wallet.pay(price);
        }
    }
    
    
}
