/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package intopark.inputhandler;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.scene.Node;
import java.util.logging.Level;
import java.util.logging.Logger;
import intopark.GUI.events.UpdateMoneyTextBarEvent;
import intopark.GUI.WindowHandler;
import intopark.Gamestate;
import static intopark.inputhandler.ClickingModes.TERRAIN;
import intopark.npc.Guest;
import intopark.npc.NPCManager;
import intopark.ride.BasicRide;
import intopark.ride.RideManager;
import intopark.shops.BasicShop;
import intopark.shops.ShopManager;
import intopark.shops.ToggleRenderHoloNodeEvent;
import intopark.terrain.events.DeleteSpatialFromMapEvent;
import intopark.terrain.ParkHandler;
import intopark.roads.RoadMaker;
import intopark.terrain.events.PayParkEvent;
import intopark.roads.RoadMakerStatus;
import intopark.terrain.TerrainHandler;
import intopark.terrain.decoration.DecorationManager;

/**
 *
 * @author arska
 */
@Singleton
public class ClickingHandler {
    //LOGGER
    private static final Logger logger = Logger.getLogger(ClickingHandler.class.getName());
    //DEPENDENCIES
    @Inject private TerrainHandler terrainHandler;
    @Inject private WindowHandler windowMaker;
    @Inject private RideManager rideManager;
    private RoadMaker roadMaker;
    private ShopManager shopManager;
    private NPCManager npcManager;
    private final Node rootNode;
    private final ParkHandler parkHandler;
    private EventBus eventBus;
    //OWNS
    private final DecorationManager decorationManager;
    //VARIABLES
    private ClickingModes clickMode = ClickingModes.NOTHING;
    public int buffer = 0;
    
    @Inject
    public ClickingHandler(EventBus eventBus,NPCManager npcManager,ShopManager shopManager,Node rootNode, ParkHandler parkHandler, DecorationManager decorationManager,RoadMaker roadMaker) {
        this.roadMaker= roadMaker;
        this.shopManager=shopManager;
        this.npcManager=npcManager;
        this.rootNode = rootNode;
        this.parkHandler = parkHandler;
        this.decorationManager = decorationManager;
        this.eventBus=eventBus;
        eventBus.register(this);
    }
    /**
     * This defines what to do when you click something. It takes account of ClickMode (State of user). 
     * @param target What user clicked (first result).
     * @param results All the results.
     */
    public void handleClicking(CollisionResult target, CollisionResults results) {
        switch (clickMode) {
            case TERRAIN:
                terrainHandler.handleClicking(results);
                Gamestate.ingameHUD.updateClickingIndicator();
                break;

            case NOTHING:
                if (target.getGeometry().getParent() == null) {
                    logger.log(Level.WARNING, "Clicked object which has no parent!");
                    return;
                }
                if (target.getGeometry().getParent().getParent() == null) {
                    logger.log(Level.WARNING, "Clicked object which has no parent's parent!");
                    return;
                }
                Node rootTarget = target.getGeometry().getParent().getParent();

                if (rootTarget.getUserData("guestnum") != null) {
                    for (Guest g : npcManager.getGuests()) {
                        if (g.getGuestNum() == rootTarget.getUserData("guestnum")) {
                            windowMaker.setGuestID(g.getGuestNum());
                            windowMaker.updateGuestWindow(true, true);
                            logger.log(Level.FINEST, "Displaying Guestwindow for guest with id {0}", g.getGuestNum());
                            return;
                        }
                    }
                }
                if (rootTarget.getUserData("shopID") != null) {
                    for (BasicShop g : shopManager.getShops()) {
                        if (g.getShopID() == rootTarget.getUserData("shopID")) {
                            windowMaker.setShopID(g.getShopID());
                            windowMaker.updateShopWindow(true);
                            logger.log(Level.FINEST, "Displaying Shopwindow for shop with id {0}", g.getShopID());
                            return;
                        }
                    }
                }
                if (rootTarget.getUserData("rideID") != null) {
                    for (BasicRide r : rideManager.rides) {
                        if (r.getRideID() == rootTarget.getUserData("rideID")) {
                            windowMaker.setRideID(r.getRideID());
                            windowMaker.updateRideWindow(true, true);
                            logger.log(Level.FINEST, "Displaying Ridewindow for Ride with id {0}", r.getRideID());
                            return;
                        }
                    }
                }

                break;

            case ROAD:
                if (target != null) {
                    if (roadMaker.getStatus() == RoadMakerStatus.CHOOSING) {
                        roadMaker.setStartingPosition(target.getContactPoint());
                        roadMaker.setStatus(RoadMakerStatus.MANUAL);
                        Gamestate.ingameHUD.updateClickingIndicator();
                        logger.log(Level.FINEST, "Updated Roads starting position");
                    } else if(roadMaker.getStatus()==RoadMakerStatus.AUTOMATIC){
                        roadMaker.handleClicking(results);
                    }else{
                        logger.log(Level.FINER, "Tried clicking while in road mode and not choosing start position, not doing anything");
                    }
                } else {
                    
                }
                break;

            case DECORATION:
                if (target != null) {
                    decorationManager.build(target.getContactPoint());
                    Gamestate.ingameHUD.updateClickingIndicator();
                } else {
                    logger.log(Level.WARNING, "NPE in ClickingHandler");
                }

                break;

            case RIDE:
                if (target != null) {
                    rideManager.placeEnterance(target.getContactPoint());
                    Gamestate.ingameHUD.updateClickingIndicator();
                } else {
                }
                break;

            case PLACE:
                if (buffer == 0) {
                    shopManager.buy(parkHandler);
                    eventBus.post(new ToggleRenderHoloNodeEvent());
                    Gamestate.ingameHUD.updateClickingIndicator();
                } else {
                    buffer = buffer - 1;
                }
                break;

        }


    }
    public void handleMouseDrag(float mouseYPos,long lastDragged){
        switch (clickMode) {
            case TERRAIN:
                terrainHandler.handleDrag(mouseYPos,lastDragged);
                break;
        }
    }
    public void handleMouseDragRelease(){
        switch (clickMode) {
            case TERRAIN:
                terrainHandler.releaseDrag();
                break;
        }
            
    }

    void handleRightClicking(CollisionResult target, CollisionResults results) {
        if (target.getGeometry().getParent() == null) {
            return;
        }
        if (target.getGeometry().getParent().getParent() == null) {
            return;
        }

        Node rootTarget = target.getGeometry().getParent().getParent();


        switch (clickMode) {

            case NOTHING:

                break;

            case DECORATION:

                break;

            case DEMOLITION:
                if (rootTarget.getUserData("type") == null) {
                    return;
                }

                if (rootTarget.getUserData("type").equals("road")) {
                    rootNode.detachChild(rootTarget);
                    eventBus.post(new DeleteSpatialFromMapEvent(rootTarget));
                    eventBus.post(new PayParkEvent(5f)); 
                    eventBus.post(new UpdateMoneyTextBarEvent());
                }
                if (rootTarget.getUserData("type").equals("decoration")) {
                    Node decorationnode = (Node) rootNode.getChild("decorationNode");
                    decorationnode.detachChild(rootTarget);
                    eventBus.post(new DeleteSpatialFromMapEvent(rootTarget));

                }

        }
    }

    public ClickingModes getClickMode() {
        return clickMode;
    }

    public  void setClickMode(ClickingModes clickMode) {
        this.clickMode = clickMode;
    }
    @Subscribe
    public void listenSetBufferEvent(SetClickingHandlerBufferEvent event){
        buffer=event.value;
    }
    @Subscribe
    public void listenSetClickModeEvent(SetClickModeEvent event){
        clickMode=event.clickmode;
    }
}
