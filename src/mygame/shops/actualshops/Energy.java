/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.shops.actualshops;

import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import mygame.Gamestate;
import mygame.Main;
import mygame.npc.Guest;
import mygame.npc.inventory.Item;
import mygame.npc.inventory.Itemtypes;
import mygame.shops.BasicShop;
import mygame.terrain.Direction;

/**
 *
 * @author arska
 */
public class Energy extends BasicShop{
    
    
    
    public Energy(Vector3f position,Spatial geom,Direction facing,Node rootNode){
        super(position,geom,300,facing,rootNode);
        productname="Energy Drink";
        constructionmoney=300;
        price=20;
        shopName="Energydrink shop";
        type="energyshop"; 
    }

    @Override
    public void interact(Guest guest) {
        if(guest.wallet.canAfford(price)){
            guest.inventory.add(new Item(productname, Itemtypes.DRINK,10));
            guest.wallet.pay(price);
            Main.currentPark.getParkWallet().add(price);
            Gamestate.ingameHUD.updateMoneytextbar();
        }
    }
}
