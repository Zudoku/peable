/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.shops;

import mygame.shops.actualshops.Meatballshop;
import com.jme3.asset.AssetManager;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import mygame.terrain.Direction;

/**
 *
 * @author arska
 */
public class ShopFactory {
    private final AssetManager assetManager;
    
    public ShopFactory(AssetManager assetManager){
        this.assetManager=assetManager;
    }
    public Meatballshop meatBallShop(Vector3f pos,Direction facing){
        Spatial geom =assetManager.loadModel("Models/shops/mball.j3o");
        Meatballshop shop=new Meatballshop(pos,geom,facing);
        
        return shop;
    }
    
}
