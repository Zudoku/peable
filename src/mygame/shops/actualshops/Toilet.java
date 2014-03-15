/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.shops.actualshops;

import com.jme3.scene.Spatial;
import mygame.GUI.events.UpdateMoneyTextBarEvent;
import mygame.npc.Guest;
import mygame.npc.inventory.Item;
import mygame.npc.inventory.Itemtypes;
import mygame.shops.BasicShop;
import mygame.terrain.Direction;
import mygame.terrain.MapPosition;
import mygame.terrain.events.PayParkEvent;

/**
 *
 * @author arska
 */
public class Toilet extends BasicShop{
    
    
    
    public Toilet(MapPosition position,Direction facing,Spatial model,int shopID,float price,float constrm,String prodName,String shopName){
        super(position,model,shopID,constrm,price,facing,prodName,shopName,"toilet");
        //DEF prodName "Toilet usage" | name "Dirty ol' toilet "+shopID | type "toilet" 
    }

    @Override
    public void interact(Guest guest) {
        if(guest.wallet.canAfford(price)){
            guest.getInventory().add(new Item(productname, Itemtypes.FUN,10));
            guest.wallet.pay(price);
            eventBus.post(new PayParkEvent(price));
            eventBus.post(new UpdateMoneyTextBarEvent());
        }
    }
}
