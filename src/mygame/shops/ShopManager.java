/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.shops;

import com.jme3.asset.AssetManager;
import com.jme3.math.Vector3f;
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

    public ShopManager(AssetManager assetManager) {
        shopFactory = new ShopFactory(assetManager);

    }

    public void buy() {
        if(Main.clickingHandler.clickMode!=ClickingModes.PLACE){
            System.out.println("Something just went Wrong!! you tried to buy when you were not in placing mode!");
            return;
        }
        Vector3f loc = Main.holoDrawer.pyorista(Main.holoDrawer.getLocation());
        
        
        switch (selectedShop) {
            case MBALL:

                break;

            case ENERGY:
                
                break;
                
            case TOILET:
                
                break;
                
            case NULL:
                System.out.println("You just tried to buy null shop!");
                break;
        }
        shops.add(null);

    }

    public void setSelection(Basicshops select) {
        if (select == null) {
            return;
        }
        this.selectedShop = select;
        Main.holoDrawer.toggleDrawSpatial();
    }

    public void resetShopdata() {
        selectedShop = Basicshops.NULL;
        facing = Direction.DOWN;
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
