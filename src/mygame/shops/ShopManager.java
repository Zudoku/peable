/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.shops;

import com.jme3.asset.AssetManager;
import com.jme3.math.Vector3f;
import java.util.ArrayList;
import mygame.Main;
import mygame.terrain.Direction;

/**
 *
 * @author arska
 */
public class ShopManager {

    Basicshops selectedShop;
    ArrayList<BasicShop> shops = new ArrayList<BasicShop>();
    ShopFactory shopFactory;
    Direction facing = Direction.DOWN;

    public ShopManager(AssetManager assetManager) {
        shopFactory = new ShopFactory(assetManager);

    }

    public void buy() {
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
    }

    public void resetShopdata() {
        selectedShop = Basicshops.NULL;
        facing = Direction.DOWN;
    }
}
