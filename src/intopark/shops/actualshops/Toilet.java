/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package intopark.shops.actualshops;

import com.jme3.scene.Spatial;
import intopark.GUI.events.UpdateMoneyTextBarEvent;
import intopark.npc.Guest;
import intopark.npc.inventory.Item;
import intopark.npc.inventory.Itemtypes;
import intopark.shops.BasicShop;
import intopark.util.Direction;
import intopark.util.MapPosition;
import intopark.terrain.events.PayParkEvent;

/**
 *
 * @author arska
 */
public class Toilet extends BasicShop{
    
    
    
    public Toilet(MapPosition position,Direction direction,Spatial model,int shopID,float price,float constrm,String prodName,String shopName){
        super(position,model,shopID,constrm,price,direction,prodName,shopName,"toilet",true);
        //DEF prodName "Toilet usage" | name "Dirty ol' toilet "+shopID | type "toilet" 
    }

    @Override
    public void interact(Guest guest) {
        if(guest.getWallet().canAfford(price)){
            guest.getInventory().add(new Item(productname, Itemtypes.FUN,10));
            guest.getWallet().pay(price);
            eventBus.post(new PayParkEvent(price));
            eventBus.post(new UpdateMoneyTextBarEvent());
        }
    }
}
