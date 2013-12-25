/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.shops;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.jme3.asset.AssetManager;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import java.util.ArrayList;
import mygame.GUI.UpdateMoneyTextBarEvent;
import mygame.inputhandler.ClickingHandler;
import mygame.inputhandler.ClickingModes;
import mygame.ride.RideManager;
import mygame.terrain.Direction;
import mygame.terrain.MapContainer;
import mygame.terrain.ParkHandler;
import mygame.terrain.RoadMaker;

/**
 *
 * @author arska
 */
@Singleton
public class ShopManager {

    public BasicBuildables selectedBuilding= BasicBuildables.NULL;
    public ArrayList<BasicShop> shops = new ArrayList<BasicShop>();
    ShopFactory shopFactory;
    Direction facing = Direction.DOWN;
    private final AssetManager assetManager;
    public Node shopNode;
    public Node rootNode;
    BasicShop boughtshop;
    int shopID;
    private final HolomodelDrawer holoDrawer;
    
    @Inject private ClickingHandler clickingHandler;
    @Inject private RoadMaker roadMaker;
    @Inject private RideManager rideManager;
    private final MapContainer map;
    private final EventBus eventBus;
    @Inject
    public ShopManager(AssetManager assetManager,Node rootNode,ShopFactory shopFactory,HolomodelDrawer holoDrawer,MapContainer map,
            EventBus eventBus) {
        this.shopFactory =shopFactory;
        this.assetManager=assetManager;
        this.rootNode=rootNode;
        this.holoDrawer=holoDrawer;
        this.eventBus=eventBus;
        
       
        this.map=map;
        shopNode=new Node("shopNode");
        rootNode.attachChild(shopNode);
        
        

    }

    public void buy(ParkHandler parkHandler) {
        
        
        Vector3f loc = holoDrawer.pyorista(holoDrawer.getLocation());
        
        
        
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
                rideManager.buy(facing,selectedBuilding);
                resetShopdataFromRide();
                return;
               
        }
        if(!parkHandler.getParkWallet().canAfford(boughtshop.constructionmoney)){
           return; 
        }
        boughtshop.shopID=shopID;
        boughtshop.getGeometry().setUserData("shopID",shopID);
        shops.add(boughtshop);
        boughtshop.getGeometry().setUserData("type","shop");
        parkHandler.getParkWallet().remove(boughtshop.constructionmoney);
        eventBus.post(new UpdateMoneyTextBarEvent());
        map.getMap()[(int)loc.x][(int)loc.y][(int)loc.z]=boughtshop.getGeometry();
        shopNode.attachChild(boughtshop.getGeometry());
        resetShopdata();
        
       
        
    }
    public void activateplace(){
        Spatial geom;
        switch(selectedBuilding){
            case MBALL:
                geom =assetManager.loadModel("Models/shops/mball.j3o");
                holoDrawer.loadSpatial(geom);
                clickingHandler.buffer=1;
                break; 
                       
            case ENERGY:
                geom =assetManager.loadModel("Models/shops/energyshop.j3o");
                holoDrawer.loadSpatial(geom);
                clickingHandler.buffer=1;
                break;
                
            case TOILET:
                geom =assetManager.loadModel("Models/shops/toilet.j3o");
                holoDrawer.loadSpatial(geom);
                clickingHandler.buffer=1;
                break;
      
            case CHESSCENTER:
                geom =assetManager.loadModel("Models/Rides/Chesshouse/chesshouse.j3o");
                holoDrawer.loadSpatial(geom);
                clickingHandler.buffer=1;
                break;
                
            case BLENDER:
                geom =assetManager.loadModel("Models/Rides/Blender/blender.j3o");
                holoDrawer.loadSpatial(geom);
                clickingHandler.buffer=1;
                break;
                
            case ARCHERYRANGE:
                geom =assetManager.loadModel("Models/Rides/archeryrange/archeryrange.j3o");
                holoDrawer.loadSpatial(geom);
                clickingHandler.buffer=1;
                break;
                
            case HAUNTEDHOUSE:
                geom =assetManager.loadModel("Models/Rides/Hauntedhouse/hauntedhouse.j3o");
                holoDrawer.loadSpatial(geom);
                clickingHandler.buffer=1;
                break;
                
            case PIRATESHIP:
                geom =assetManager.loadModel("Models/Rides/PirateShip/core.j3o");
                holoDrawer.loadSpatial(geom);
                clickingHandler.buffer=1;
                break;
                
                
            case ROTOR:
                geom =assetManager.loadModel("Models/Rides/Rotor/rotor.j3o");
                holoDrawer.loadSpatial(geom);
                clickingHandler.buffer=1;
                break;
                
            case NULL:
                
                break;
        }
        holoDrawer.toggleDrawSpatial();
        clickingHandler.clickMode= ClickingModes.PLACE;
        
        
        
        
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
        clickingHandler.clickMode= ClickingModes.NOTHING;
    }
    public void resetShopdataFromRide() {
        selectedBuilding = BasicBuildables.NULL;
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
        holoDrawer.rotateDrawed(facing);
    }
    public BasicShop isthereshop(int x,int y ,int z){
        BasicShop b=null;
        if(shops.isEmpty()==false){
            for(BasicShop p:shops){
                int tx=(int)holoDrawer.pyorista(p.position).x;
                int ty=(int)holoDrawer.pyorista(p.position).y;
                int tz=(int)holoDrawer.pyorista(p.position).z;
                if(tx==x&&ty==y&&tz==z){
                    b=p;
                    System.out.println("SHOP IS LOCATED!");
                    return b;
                    
                }
            }
        }
        return b;
    }
    public void setShopID(int shopID){
        this.shopID=shopID;
    }
}
