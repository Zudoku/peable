/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.shops.actualshops;

import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import mygame.npc.Guest;
import mygame.shops.BasicShop;
import mygame.terrain.Direction;

/**
 *
 * @author arska
 */
public class Toilet extends BasicShop{
    
    
    
    public Toilet(Vector3f position,Spatial geom,Direction facing){
        super(position,geom,300,facing);
        price=5;
        constructionmoney=300;
        productname="Toilet usage";
        shopName="Dirty ol' toilet";
    }

    @Override
    public void interact(Guest guest) {
        if(guest.wallet.canAfford(price)){
            //USE BATHROOM
        }
    }
}
