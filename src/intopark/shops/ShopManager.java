/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package intopark.shops;

import intopark.GUI.events.UpdateBuildingUIEvent;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.jme3.light.Light;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import intopark.GUI.events.CloseWindowsEvent;
import intopark.GUI.events.UpdateMoneyTextBarEvent;
import intopark.Gamestate;
import intopark.UtilityMethods;
import intopark.gameplayorgans.ClickModeManager;
import intopark.inout.Identifier;
import intopark.inout.LoadPaths;
import intopark.input.mouse.ClickingModes;
import intopark.input.mouse.MouseContainer;
import intopark.input.mouse.NeedMouse;
import intopark.input.SetClickModeEvent;
import intopark.input.SetClickingHandlerBufferEvent;
import intopark.ride.RideManager;
import intopark.util.Direction;
import intopark.util.MapPosition;
import intopark.terrain.ParkHandler;

/**
 *
 * @author arska
 */
@Singleton
public class ShopManager implements NeedMouse,ClickModeManager{
    //LOGGER
    private static final Logger logger = Logger.getLogger(ShopManager.class.getName());
    //DEPENDENCIES
    @Inject private ParkHandler parkHandler;
    @Inject private RideManager rideManager;
    private final EventBus eventBus;
    private Identifier identifier;
    //OWNS
    private List<BasicShop> shops = new ArrayList<>();
    private Node shopNode;
    private final HolomodelDrawer holoDrawer;
    //VARIABLES
    private int buffer = 0;
    private Direction direction = Direction.SOUTH;
    public BasicBuildables selectedBuilding= BasicBuildables.NULL;
    private boolean placeBuilding=false;

    @Inject
    public ShopManager(Node rootNode,HolomodelDrawer holoDrawer,EventBus eventBus,Identifier identifier) {
        this.holoDrawer=holoDrawer;
        this.eventBus=eventBus;
        this.identifier=identifier;
        shopNode=new Node("shopNode");
        rootNode.attachChild(shopNode);
        eventBus.register(this);
        //TODO: PROPERLY DOCUMENT THIS CLASS.
    }

    public void buy(ParkHandler parkHandler) {
        float constructionmoney;
        String type;
        switch (selectedBuilding) {
            case MEATBALLS:
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
                rideManager.buildNewRide(direction,selectedBuilding);
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
        int ID=identifier.reserveID();
        String shopName=type+" "+ID;
        //
        CreateShopEvent event=new CreateShopEvent(type,shopName,prodname,reputation,price,constructionmoney,ID,pos,direction,eventBus);
        eventBus.post(event);
        //
        parkHandler.getParkWallet().remove(constructionmoney);
        eventBus.post(new UpdateMoneyTextBarEvent());
        resetShopdata();

    }
    private void activateplace(){
        Spatial geom;
        switch(selectedBuilding){
            case MEATBALLS:
                geom = UtilityMethods.loadModel(LoadPaths.mball);
                holoDrawer.loadSpatial(geom);
                eventBus.post(new SetClickingHandlerBufferEvent(1));
                break;

            case ENERGY:
                geom =UtilityMethods.loadModel(LoadPaths.energy);
                holoDrawer.loadSpatial(geom);
                eventBus.post(new SetClickingHandlerBufferEvent(1));
                break;

            case TOILET:
                geom =UtilityMethods.loadModel(LoadPaths.toilet);
                holoDrawer.loadSpatial(geom);
                eventBus.post(new SetClickingHandlerBufferEvent(1));
                break;

            case CHESSCENTER:
                geom =UtilityMethods.loadModel(LoadPaths.chess);
                holoDrawer.loadSpatial(geom);
                eventBus.post(new SetClickingHandlerBufferEvent(1));
                break;

            case BLENDER:
                geom =UtilityMethods.loadModel(LoadPaths.blender);
                holoDrawer.loadSpatial(geom);
                eventBus.post(new SetClickingHandlerBufferEvent(1));
                break;

            case ARCHERYRANGE:
                geom =UtilityMethods.loadModel(LoadPaths.archery);
                holoDrawer.loadSpatial(geom);
                eventBus.post(new SetClickingHandlerBufferEvent(1));
                break;

            case HAUNTEDHOUSE:
                geom =UtilityMethods.loadModel(LoadPaths.archery);
                holoDrawer.loadSpatial(geom);
                eventBus.post(new SetClickingHandlerBufferEvent(1));
                break;

            case PIRATESHIP:
                geom =UtilityMethods.loadModel(LoadPaths.pirateCore);
                holoDrawer.loadSpatial(geom);
                eventBus.post(new SetClickingHandlerBufferEvent(1));
                break;


            case ROTOR:
                geom =UtilityMethods.loadModel(LoadPaths.rotor);
                holoDrawer.loadSpatial(geom);
                eventBus.post(new SetClickingHandlerBufferEvent(1));
                break;

            case NULL:

                break;
        }
        holoDrawer.toggleRenderHoloNode();
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
        direction = Direction.SOUTH;
        eventBus.post(new SetClickModeEvent(ClickingModes.NOTHING));
        placeBuilding=false;
    }
    public void resetShopdataFromRide() {
        selectedBuilding = BasicBuildables.NULL;
        direction = Direction.SOUTH;
        eventBus.post(new SetClickModeEvent(ClickingModes.RIDE));
        placeBuilding=false;
    }
    public void rotateShop(){
        switch(direction){
            case SOUTH:
                direction= Direction.WEST;
                break;

            case WEST:
                direction= Direction.NORTH;
                break;

            case EAST:
                direction= Direction.SOUTH;
                break;

            case NORTH:
                direction= Direction.EAST;

        }
        holoDrawer.rotateDrawed(direction);
    }
    @Deprecated
    public BasicShop isthereshop(int x,int y ,int z){
        BasicShop b=null;
        if(shops.isEmpty()==false){
            for(BasicShop p:shops){
                int tx=(int)UtilityMethods.roundVector(p.position.getVector()).x;
                int ty=(int)UtilityMethods.roundVector(p.position.getVector()).y;
                int tz=(int)UtilityMethods.roundVector(p.position.getVector()).z;
                if(tx==x&&ty==y&&tz==z){
                    b=p;
                    return b;

                }
            }
        }
        return b;
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
    @Subscribe
    public void listenSetBufferEvent(SetClickingHandlerBufferEvent event){
        buffer=event.value;
    }
    @Override
    public void onClick(MouseContainer container) {
        if (buffer == 0) {
            buy(parkHandler);
            eventBus.post(new ToggleRenderHoloNodeEvent());
            Gamestate.ingameHUD.updateClickingIndicator();
        } else {
            buffer = buffer - 1;
        }
    }

    @Override
    public void onDrag(MouseContainer container) {

    }

    @Override
    public void onDragRelease(MouseContainer container) {

    }

    @Override
    public void onCursorHover(MouseContainer container) {

    }

    @Override
    public void onSelection() {

    }

    @Override
    public void cleanUp() {
        
    }

}
