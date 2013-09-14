/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.shops;

import com.jme3.asset.AssetManager;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import java.util.ArrayList;
import mygame.Main;
import mygame.inputhandler.ClickingModes;
import mygame.terrain.Direction;

/**
 *
 * @author arska
 */
public class ShopManager {

    public Basicshops selectedShop= Basicshops.NULL;
    ArrayList<BasicShop> shops = new ArrayList<BasicShop>();
    ShopFactory shopFactory;
    Direction facing = Direction.DOWN;
    private final AssetManager assetManager;
    public Node shopNode;
    public Node rootNode;
    BasicShop boughtshop;

    public ShopManager(AssetManager assetManager,Node rootNode) {
        shopFactory = new ShopFactory(assetManager);
        this.assetManager=assetManager;
        this.rootNode=rootNode;
        shopNode=new Node("shopNode");
        rootNode.attachChild(shopNode);
        
        

    }

    public void buy() {
        
        Vector3f loc = Main.holoDrawer.pyorista(Main.holoDrawer.getLocation());
        loc.y=6;
        
        
        switch (selectedShop) {
            case MBALL:
                boughtshop=shopFactory.meatBallShop(loc, facing);
                break;

            case ENERGY:
                boughtshop=shopFactory.energyShop(loc, facing);
                break;
                
            case TOILET:
                boughtshop=shopFactory.toilet(loc, facing);
                break;
                
            case NULL:
                System.out.println("You just tried to buy null shop!");
                break;
        }
        shops.add(boughtshop);
        shopNode.attachChild(boughtshop.getGeometry());
        resetShopdata();
        System.out.println("SHOP BOUGHT AT "+loc.x+" "+loc.y+" "+loc.z);
        Vector3f tempshit=boughtshop.object.getWorldTranslation();
        System.out.println("SHOP IS AT "+tempshit.x+" "+tempshit.y+" "+tempshit.z);
        DirectionalLight sun = new DirectionalLight(); 
        sun.setDirection((new Vector3f(-0.5f, -0.5f, -0.5f))); 
        sun.setColor(ColorRGBA.White);
        shopNode.addLight(sun);
        
    }
    public void activateplace(){
        Spatial geom;
        switch(selectedShop){
            case MBALL:
                geom =assetManager.loadModel("Models/shops/mball.j3o");
                Main.holoDrawer.loadSpatial(geom);
                break; 
                       
            case NULL:
                System.out.println("BUG IN SHOPMANAGER LINE 64!");
                break;
        }
        Main.holoDrawer.toggleDrawSpatial();
        Main.clickingHandler.clickMode= ClickingModes.PLACE;
        Main.clickingHandler.buffer=1;
        
        
        
    }
    public void setSelection(Basicshops select) {
        if (select == null) {
            return;
        }
        this.selectedShop = select;
        
    }

    public void resetShopdata() {
        selectedShop = Basicshops.NULL;
        facing = Direction.DOWN;
        Main.clickingHandler.clickMode= ClickingModes.NOTHING;
    }
    public void rotateShop(){
        switch(facing){
            case DOWN:
                facing= Direction.LEFT;
                break;
                
            case LEFT:
                facing= Direction.UP;
                break;
                
            case RIGHT:
                facing= Direction.DOWN;
                break;
                
            case UP:
                facing= Direction.RIGHT;
            
        }
        Main.holoDrawer.rotateDrawed(facing);
    }
}
