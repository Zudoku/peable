/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.shops;

import mygame.GUI.events.UpdateBuildingUIEvent;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.jme3.asset.AssetManager;
import com.jme3.light.Light;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import mygame.GUI.events.CloseWindowsEvent;
import mygame.GUI.events.UpdateMoneyTextBarEvent;
import mygame.inputhandler.ClickingModes;
import mygame.inputhandler.SetClickModeEvent;
import mygame.inputhandler.SetClickingHandlerBufferEvent;
import mygame.ride.RideManager;
import mygame.terrain.events.AddObjectToMapEvent;
import mygame.terrain.Direction;
import mygame.terrain.MapPosition;
import mygame.terrain.ParkHandler;

/**
 *
 * @author arska
 */
@Singleton
public class ShopManager {
    //LOGGER
    private static final Logger logger = Logger.getLogger(ShopManager.class.getName());
    //DEPENDENCIES
    private final AssetManager assetManager;
    //OWNS
    private List<BasicShop> shops = new ArrayList<BasicShop>();
    private Node shopNode;
    private final HolomodelDrawer holoDrawer;
    //SELECTION PARAMETERS
    private int shopID;
    private Direction facing = Direction.DOWN;
    public BasicBuildables selectedBuilding= BasicBuildables.NULL;
    private boolean placeBuilding=false;
    
    
    @Inject private RideManager rideManager;
    private final EventBus eventBus;
    @Inject
    public ShopManager(AssetManager assetManager,Node rootNode,HolomodelDrawer holoDrawer,EventBus eventBus) {
        this.assetManager=assetManager;
        this.holoDrawer=holoDrawer;
        this.eventBus=eventBus;

        shopNode=new Node("shopNode");
        rootNode.attachChild(shopNode);
        eventBus.register(this);
    }

    public void buy(ParkHandler parkHandler) {
        float constructionmoney=0;
        String type="";
        switch (selectedBuilding) {
            case MBALL:
                constructionmoney=300;
                type="meatballshop";
                break;

            case ENERGY:
                constructionmoney=300;
                type="energyshop";
                break;
                
            case TOILET:
                constructionmoney=300;
                type="toilet";
                break;
                
            case NULL:
                logger.log(Level.WARNING,"Tried to buy null shop");
                return;
                
            default: 
                //Forward to rideManager
                rideManager.buy(facing,selectedBuilding);
                resetShopdataFromRide();
                return;
               
        }
        if(!parkHandler.getParkWallet().canAfford(constructionmoney)){
            logger.log(Level.FINE,"Can't afford constructing shop");
           return; 
        }
        //
        MapPosition pos=new MapPosition(holoDrawer.getLocation());
        float price = 20;
        ShopReputation reputation=ShopReputation.NEW;
        String prodname="prodname";
        String shopName=type+" "+shopID;
        //
        CreateShopEvent event=new CreateShopEvent(type,shopName,prodname,reputation,price,constructionmoney,shopID,pos,facing);
        eventBus.post(event);
        //
        parkHandler.getParkWallet().remove(constructionmoney);
        eventBus.post(new UpdateMoneyTextBarEvent());
        resetShopdata();
  
    }
    private void activateplace(){
        Spatial geom;
        switch(selectedBuilding){
            case MBALL:
                geom =assetManager.loadModel("Models/shops/mball.j3o");
                holoDrawer.loadSpatial(geom);
                eventBus.post(new SetClickingHandlerBufferEvent(1));
                break; 
                       
            case ENERGY:
                geom =assetManager.loadModel("Models/shops/energyshop.j3o");
                holoDrawer.loadSpatial(geom);
                eventBus.post(new SetClickingHandlerBufferEvent(1));
                break;
                
            case TOILET:
                geom =assetManager.loadModel("Models/shops/toilet.j3o");
                holoDrawer.loadSpatial(geom);
                eventBus.post(new SetClickingHandlerBufferEvent(1));
                break;
      
            case CHESSCENTER:
                geom =assetManager.loadModel("Models/Rides/Chesshouse/chesshouse.j3o");
                holoDrawer.loadSpatial(geom);
                eventBus.post(new SetClickingHandlerBufferEvent(1));
                break;
                
            case BLENDER:
                geom =assetManager.loadModel("Models/Rides/Blender/blender.j3o");
                holoDrawer.loadSpatial(geom);
                eventBus.post(new SetClickingHandlerBufferEvent(1));
                break;
                
            case ARCHERYRANGE:
                geom =assetManager.loadModel("Models/Rides/archeryrange/archeryrange.j3o");
                holoDrawer.loadSpatial(geom);
                eventBus.post(new SetClickingHandlerBufferEvent(1));
                break;
                
            case HAUNTEDHOUSE:
                geom =assetManager.loadModel("Models/Rides/Hauntedhouse/hauntedhouse.j3o");
                holoDrawer.loadSpatial(geom);
                eventBus.post(new SetClickingHandlerBufferEvent(1));
                break;
                
            case PIRATESHIP:
                geom =assetManager.loadModel("Models/Rides/PirateShip/core.j3o");
                holoDrawer.loadSpatial(geom);
                eventBus.post(new SetClickingHandlerBufferEvent(1));
                break;
                
                
            case ROTOR:
                geom =assetManager.loadModel("Models/Rides/Rotor/rotor.j3o");
                holoDrawer.loadSpatial(geom);
                eventBus.post(new SetClickingHandlerBufferEvent(1));
                break;
                
            case NULL:
                
                break;
        }
        holoDrawer.toggleDrawSpatial();
        eventBus.post(new SetClickModeEvent(ClickingModes.PLACE));
        eventBus.post(new CloseWindowsEvent("")); 
    }
    public void attachToShopNode(Object object){
        if(object instanceof Spatial){
            logger.log(Level.FINEST,"New Spatial added to shopNode");
            shopNode.attachChild((Spatial)object);
        }
        if(object instanceof Light){
            logger.log(Level.FINEST,"New Light added to shopNode");
            shopNode.addLight((Light)object);
        }
    }
    public void resetShopdata() {
        selectedBuilding = BasicBuildables.NULL;
        facing = Direction.DOWN;
        eventBus.post(new SetClickModeEvent(ClickingModes.NOTHING));
        placeBuilding=false;
        shopID++;
    }
    public void resetShopdataFromRide() {
        selectedBuilding = BasicBuildables.NULL;
        facing = Direction.DOWN;
        placeBuilding=false;
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
    @Deprecated
    public BasicShop isthereshop(int x,int y ,int z){
        BasicShop b=null;
        if(shops.isEmpty()==false){
            for(BasicShop p:shops){
                int tx=(int)holoDrawer.pyorista(p.position.getVector()).x;
                int ty=(int)holoDrawer.pyorista(p.position.getVector()).y;
                int tz=(int)holoDrawer.pyorista(p.position.getVector()).z;
                if(tx==x&&ty==y&&tz==z){
                    b=p;
                    return b;
                    
                }
            }
        }
        return b;
    }
    public void setShopID(int shopID){
        this.shopID=shopID;
    }
    @Subscribe
    public void listenBuildingSelection(BuildingSelectionEvent event){
        checkSelected(event.selection);
        if(placeBuilding){
           activateplace();
           eventBus.post(new CloseWindowsEvent(""));
        }else{
            eventBus.post(new UpdateBuildingUIEvent(selectedBuilding));
        }
    }
    private void checkSelected(BasicBuildables b){
        if(b==selectedBuilding){
            placeBuilding=true;
        }else{
            selectedBuilding=b;
        }
    }

    public void setShops(List<BasicShop> shops) {
        this.shops = shops;
    }

    public List<BasicShop> getShops() {
        return shops;
    }
    
}
