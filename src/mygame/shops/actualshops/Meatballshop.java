/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.shops.actualshops;

import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import mygame.GUI.UpdateMoneyTextBarEvent;
import mygame.npc.Guest;
import mygame.npc.inventory.Item;
import mygame.npc.inventory.Itemtypes;
import mygame.shops.BasicShop;
import mygame.terrain.Direction;
import mygame.terrain.PayParkEvent;

/**
 *
 * @author arska
 */
public class Meatballshop extends BasicShop{
     

    
    
    public Meatballshop(Vector3f position,Spatial geom,Direction facing,Node rootNode){
        super(position,geom,300,facing,rootNode);
        shopName="MeatBallShop";
        productname="Herggu MeatBalls";
        price=20;
        type="meatballshop";
        
        
    }

    @Override
    public void interact(Guest guest) {
        if(guest.wallet.canAfford(price)){
            guest.inventory.add(new Item(productname, Itemtypes.FOOD,10));
            guest.wallet.pay(price);
            eventBus.post(new PayParkEvent(price));
            eventBus.post(new UpdateMoneyTextBarEvent());
        }
    }
    
    
}
