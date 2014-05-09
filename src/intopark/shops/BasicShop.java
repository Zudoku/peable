/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package intopark.shops;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import com.jme3.scene.Spatial;
import java.util.Random;
import java.util.logging.Logger;
import intopark.GUI.events.UpdateMoneyTextBarEvent;
import intopark.Main;
import intopark.npc.events.AddGuestLimitEvent;
import intopark.npc.Guest;
import intopark.roads.BuildingEnterance;
import intopark.roads.events.CreateBuildingEnteranceEvent;
import intopark.terrain.Direction;
import intopark.terrain.MapPosition;
import intopark.terrain.events.PayParkEvent;

/**
 *
 * @author arska
 */
public class BasicShop {
    //LOGGER
    protected transient static final Logger logger = Logger.getLogger(BasicShop.class.getName());
    //DEPENDENCIES
    @Inject transient protected EventBus eventBus;
    //VARIABLES
    protected Direction facing;
    protected MapPosition position;
    protected transient Spatial object;
    protected int shopID=0;
    protected float constructionmoney=0;
    protected String productname="productname";
    protected String shopName="SHOPNAME";
    protected float price=0;
    protected ShopUpgradeContainer upgrades=new ShopUpgradeContainer();
    protected String type;
    protected BuildingEnterance enterance;
    protected boolean needToFaceDirection=true;
    
    public BasicShop(MapPosition position,Spatial object,int shopID,float constr,float price,Direction facing,String prodname,String shopName,String type,boolean needToFaceDirection){
        this.position=position;
        this.object=object;
        this.constructionmoney=constr;
        this.facing=facing;
        this.shopID=shopID;
        this.productname=prodname;
        this.price=price;
        this.shopName=shopName;
        this.type=type;
        this.needToFaceDirection=needToFaceDirection;
        /* Assign 3d model to right coordinates */
        object.setLocalTranslation(position.getVector());
        Main.injector.injectMembers(this);
        Random r =new Random();
        this.enterance=new BuildingEnterance(position, shopID, BuildingEnterance.SHOP,facing,true);
        eventBus.post(new CreateBuildingEnteranceEvent(this.enterance));
        eventBus.post(new AddGuestLimitEvent(r.nextInt(5)+5));
        object.setUserData("type","shop");
        object.setUserData("shopID",shopID);
    }
    
    public void interact(Guest guest){
        
    }
    public void demolish(){

        eventBus.post(new ShopDemolishEvent(this));
        eventBus.post(new PayParkEvent(0.5f*constructionmoney));
        eventBus.post(new UpdateMoneyTextBarEvent());
        
    }
    
    /**
     * GETTERS AND SETTERS
     */
    
    public void setConstructionmoney(float constructionmoney) {
        this.constructionmoney = constructionmoney;
    }

    public void setFacing(Direction facing) {
        this.facing = facing;
    }

    public void setObject(Spatial object) {
        this.object = object;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public void setPosition(MapPosition position) {
        this.position = position;
    }

    public void setProductname(String productname) {
        this.productname = productname;
    }

    public void setShopID(int shopID) {
        this.shopID = shopID;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public void setType(String type) {
        this.type = type;
    }

    public float getConstructionmoney() {
        return constructionmoney;
    }

    public Direction getFacing() {
        return facing;
    }

    public Spatial getObject() {
        return object;
    }

    public MapPosition getPosition() {
        return position;
    }

    public float getPrice() {
        return price;
    }

    public String getProductname() {
        return productname;
    }

    public int getShopID() {
        return shopID;
    }

    public String getShopName() {
        return shopName;
    }

    public ShopUpgradeContainer getUpgrades() {
        return upgrades;
    }

    public void setUpgrades(ShopUpgradeContainer upgrades) {
        this.upgrades = upgrades;
    }
    
    public String getType() {
        return type;
    }

    public BuildingEnterance getEnterance() {
        return enterance;
    }

    public void setEnterance(BuildingEnterance enterance) {
        this.enterance = enterance;
    }

    public boolean getNeedToFaceDirection() {
        return needToFaceDirection;
    }

    public void setNeedToFaceDirection(boolean needToFaceDirection) {
        this.needToFaceDirection = needToFaceDirection;
    }
    
    
    
}
