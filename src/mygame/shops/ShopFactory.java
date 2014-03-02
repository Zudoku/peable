/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.shops;

import com.google.inject.Inject;
import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import java.util.logging.Logger;
import mygame.shops.actualshops.Energy;
import mygame.shops.actualshops.Meatballshop;
import mygame.shops.actualshops.Toilet;
import mygame.terrain.Direction;

/**
 *
 * @author arska
 */
public class ShopFactory {
    private static final Logger logger = Logger.getLogger(ShopFactory.class.getName());
    private final AssetManager assetManager;
    private final Node rootNode;
    @Inject
    public ShopFactory(AssetManager assetManager,Node rootNode){
        this.assetManager=assetManager;
        this.rootNode=rootNode;
    }
    public Meatballshop meatBallShop(Vector3f pos,Direction facing){
        Spatial geom =assetManager.loadModel("Models/shops/mball.j3o");
        
        Meatballshop shop=new Meatballshop(pos,geom,facing,rootNode);
        
        return shop;
    }
    public Energy energyShop(Vector3f pos,Direction facing){
        Spatial geom =assetManager.loadModel("Models/shops/energyshop.j3o");
        Energy shop=new Energy(pos,geom,facing,rootNode);
        
        return shop;
    }
    public Toilet toilet(Vector3f pos,Direction facing){
        Spatial geom =assetManager.loadModel("Models/shops/toilet.j3o");
        Toilet shop=new Toilet(pos,geom,facing,rootNode);
        
        return shop;
    }
    
}
