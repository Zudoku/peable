/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.shops;

import com.jme3.asset.AssetManager;
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

    public BasicBuildables selectedBuilding= BasicBuildables.NULL;
    public ArrayList<BasicShop> shops = new ArrayList<BasicShop>();
    ShopFactory shopFactory;
    Direction facing = Direction.DOWN;
    private final AssetManager assetManager;
    public Node shopNode;
    public Node rootNode;
    BasicShop boughtshop;
    int shopID=1;

    public ShopManager(AssetManager assetManager,Node rootNode) {
        shopFactory = new ShopFactory(assetManager);
        this.assetManager=assetManager;
        this.rootNode=rootNode;
        shopNode=new Node("shopNode");
        rootNode.attachChild(shopNode);
        
        

    }

    public void buy() {
        
        Vector3f loc = Main.holoDrawer.pyorista(Main.holoDrawer.getLocation());
        
        
        
        switch (selectedBuilding) {
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
                
            default: 
                //eteenpäin shopmanagerille
                Main.rideManager.buy(facing,selectedBuilding);
                resetShopdata();
                return;
               
        }
        boughtshop.shopID=shopID;
        boughtshop.getGeometry().setUserData("shopID",shopID);
        shops.add(boughtshop);
        shopNode.attachChild(boughtshop.getGeometry());
        resetShopdata();
        
       
        
    }
    public void activateplace(){
        Spatial geom;
        switch(selectedBuilding){
            case MBALL:
                geom =assetManager.loadModel("Models/shops/mball.j3o");
                Main.holoDrawer.loadSpatial(geom);
                break; 
                       
            case ENERGY:
                geom =assetManager.loadModel("Models/shops/energyshop.j3o");
                Main.holoDrawer.loadSpatial(geom);
                break;
                
            case TOILET:
                geom =assetManager.loadModel("Models/shops/toilet.j3o");
                Main.holoDrawer.loadSpatial(geom);
                break;
      
            case CHESSCENTER:
                geom =assetManager.loadModel("Models/Rides/chesshouse.j3o");
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
    public void setSelection(BasicBuildables select) {
        if (select == null) {
            return;
        }
        this.selectedBuilding = select;
        
    }

    public void resetShopdata() {
        selectedBuilding = BasicBuildables.NULL;
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
    public BasicShop isthereshop(int x,int y ,int z){
        BasicShop b=null;
        if(shops.isEmpty()==false){
            for(BasicShop p:shops){
                int tx=(int)Main.holoDrawer.pyorista(p.position).x;
                int ty=(int)Main.holoDrawer.pyorista(p.position).y;
                int tz=(int)Main.holoDrawer.pyorista(p.position).z;
                if(tx==x&&ty==y&&tz==z){
                    b=p;
                    System.out.println("SHOP IS LOCATED!");
                    return b;
                    
                }
            }
        }
        return b;
    }
}
