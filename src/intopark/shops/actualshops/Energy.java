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
import intopark.terrain.Direction;
import intopark.terrain.MapPosition;
import intopark.terrain.events.PayParkEvent;

/**
 *
 * @author arska
 */
public class Energy extends BasicShop{
    
    
    
    public Energy(MapPosition position,Direction facing,Spatial model,int shopID,float price,float constrm,String prodName,String shopName){
        super(position,model,shopID,constrm,price,facing,prodName,shopName,"energyshop");
        //DEF prodName "Yak Energy drink" | name "Energyshop "+shopID | type "energyshop"
    }

    @Override
    public void interact(Guest guest) {
        if(guest.getWallet().canAfford(price)){
            guest.getInventory().add(new Item(productname, Itemtypes.DRINK,10));
            guest.getWallet().pay(price);
            eventBus.post(new PayParkEvent(price));
            eventBus.post(new UpdateMoneyTextBarEvent());
        }
    }
}
