/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package intopark.shops;

import com.google.common.eventbus.EventBus;
import com.jme3.scene.Spatial;
import intopark.UtilityMethods;
import intopark.shops.actualshops.Energy;
import intopark.shops.actualshops.Meatballshop;
import intopark.shops.actualshops.Toilet;
import intopark.util.Direction;
import intopark.util.MapPosition;

/**
 *
 * @author arska
 */
public class CreateShopEvent {
    private String type;
    private String shopName;
    private String prodName;
    private ShopReputation shopRep;
    private float price;
    private float constmoney;
    private int shopID;
    private Direction direction;
    private MapPosition pos;
    private Spatial model;
    private EventBus eventBus;
    public CreateShopEvent() {
        
    }

    public CreateShopEvent(String stype, String shopname, String prodname, ShopReputation sr,
            float price, float cm, int shopID, MapPosition pos, Direction dir,EventBus eventBus) {
        this.type=stype;
        this.shopName=shopname;
        this.prodName=prodname;
        this.shopRep=sr;
        this.price=price;
        this.constmoney=cm;
        this.shopID=shopID;
        this.direction=dir;
        this.pos=pos;
        this.eventBus=eventBus;
        if(type.equals("energyshop")){
            model= UtilityMethods.loadModel("Models/shops/energyshop.j3o");
        }
        if(type.equals("meatballshop")){
            model= UtilityMethods.loadModel("Models/shops/mball.j3o");
        }
        if(type.equals("toilet")){
            model= UtilityMethods.loadModel("Models/shops/toilet.j3o");
        }
        
    }
    public BasicShop toShop(){
        BasicShop shop=null;
        if(type.equals("energyshop")){
            shop=new Energy(pos,direction,model,shopID,price,constmoney,prodName,shopName,eventBus);
        }
        if(type.equals("meatballshop")){
            shop=new Meatballshop(pos,direction,model,shopID,price,constmoney,prodName,shopName,eventBus);
        }
        if(type.equals("toilet")){
            shop=new Toilet(pos,direction,model,shopID,price,constmoney,prodName,shopName,eventBus);
        }
        return shop;
    }
}
