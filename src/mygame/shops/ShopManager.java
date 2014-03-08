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
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import java.util.ArrayList;
import java.util.logging.Logger;
import mygame.GUI.events.CloseWindowsEvent;
import mygame.GUI.events.UpdateMoneyTextBarEvent;
import mygame.inputhandler.ClickingHandler;
import mygame.inputhandler.ClickingModes;
import mygame.inputhandler.SetClickModeEvent;
import mygame.inputhandler.SetClickingHandlerBufferEvent;
import mygame.ride.RideManager;
import mygame.terrain.events.AddObjectToMapEvent;
import mygame.terrain.Direction;
import mygame.terrain.ParkHandler;

/**
 *
 * @author arska
 */
@Singleton
public class ShopManager {
    //LOGGER
    private static final Logger logger = Logger.getLogger(ShopManager.class.getName());
    //OWNS
    ShopFactory shopFactory;
    public ArrayList<BasicShop> shops = new ArrayList<BasicShop>();
    private final AssetManager assetManager;
    private Node shopNode;
    private final HolomodelDrawer holoDrawer;
    //SELECTION PARAMETERS
    int shopID;
    Direction facing = Direction.DOWN;
    BasicShop boughtshop;
    public BasicBuildables selectedBuilding= BasicBuildables.NULL;
    private boolean placeBuilding=false;
    
    
    @Inject private RideManager rideManager;
    private final EventBus eventBus;
    @Inject
    public ShopManager(AssetManager assetManager,Node rootNode,ShopFactory shopFactory,HolomodelDrawer holoDrawer,EventBus eventBus) {
        this.shopFactory =shopFactory;
        this.assetManager=assetManager;
        this.holoDrawer=holoDrawer;
        this.eventBus=eventBus;

        shopNode=new Node("shopNode");
        rootNode.attachChild(shopNode);
        eventBus.register(this);
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
        int ax=(int)loc.x;
        int ay=(int)loc.y;
        int az=(int)loc.z;
        eventBus.post(new AddObjectToMapEvent(ax, ay, az, boughtshop.getGeometry()));
        shopNode.attachChild(boughtshop.getGeometry());
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

    public void resetShopdata() {
        selectedBuilding = BasicBuildables.NULL;
        facing = Direction.DOWN;
        eventBus.post(new SetClickModeEvent(ClickingModes.NOTHING));
        placeBuilding=false;
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
}
