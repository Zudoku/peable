/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.shops;

import com.jme3.asset.AssetManager;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;

/**
 *
 * @author arska
 */
public class ShopFactory {
    private final AssetManager assetManager;
    
    public ShopFactory(AssetManager assetManager){
        this.assetManager=assetManager;
    }
    public Meatballshop meatBallShop(Vector3f pos){
        Spatial geom =assetManager.loadModel("");
        Meatballshop shop=new Meatballshop(pos,geom);
        return shop;
    }
    
}
