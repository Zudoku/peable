/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.inputhandler;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.scene.Node;
import java.util.logging.Level;
import java.util.logging.Logger;
import mygame.GUI.events.UpdateMoneyTextBarEvent;
import mygame.GUI.WindowMaker;
import mygame.Gamestate;
import static mygame.inputhandler.ClickingModes.TERRAIN;
import mygame.npc.Guest;
import mygame.npc.NPCManager;
import mygame.ride.BasicRide;
import mygame.ride.RideManager;
import mygame.shops.BasicShop;
import mygame.shops.ShopManager;
import mygame.shops.ToggleHoloModelDrawEvent;
import mygame.terrain.events.DeleteSpatialFromMapEvent;
import mygame.terrain.ParkHandler;
import mygame.terrain.RoadMaker;
import mygame.terrain.events.PayParkEvent;
import mygame.terrain.RoadMakerStatus;
import mygame.terrain.TerrainHandler;
import mygame.terrain.decoration.DecorationManager;

/**
 *
 * @author arska
 */
@Singleton
public class ClickingHandler {

    private static final Logger logger = Logger.getLogger(ClickingHandler.class.getName());
    @Inject private TerrainHandler terrainHandler;
    private EventBus eventBus;
    @Inject private WindowMaker windowMaker;
    @Inject private RideManager rideManager;
    private ClickingModes clickMode = ClickingModes.NOTHING;
    public int buffer = 0;
    private final Node rootNode;
    private final ParkHandler parkHandler;
    private final DecorationManager decorationManager;
    private RoadMaker roadMaker;
    private ShopManager shopManager;
    private NPCManager npcManager;
    
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
                            windowMaker.createGuestWindow(g, true);
                            logger.log(Level.FINEST, "Displaying Guestwindow for guest with id {0}", g.getGuestNum());
                            return;
                        }
                    }
                }
                if (rootTarget.getUserData("shopID") != null) {
                    for (BasicShop g : shopManager.getShops()) {
                        if (g.shopID == rootTarget.getUserData("shopID")) {
                            windowMaker.createShopWindow(g);
                            logger.log(Level.FINEST, "Displaying Shopwindow for shop with id {0}", g.shopID);
                            return;
                        }
                    }
                }
                if (rootTarget.getUserData("rideID") != null) {
                    for (BasicRide r : rideManager.rides) {
                        if (r.getRideID() == rootTarget.getUserData("rideID")) {
                            windowMaker.CreateRideWindow(r);
                            logger.log(Level.FINEST, "Displaying Ridewindow for Ride with id {0}", r.getRideID());
                            return;
                        }
                    }
                }

                break;

            case ROAD:
                if (target != null) {
                    if (roadMaker.status == RoadMakerStatus.CHOOSING) {
                        roadMaker.startingPosition(target.getContactPoint());
                        Gamestate.ingameHUD.updateClickingIndicator();
                        logger.log(Level.FINEST, "Updated Roads starting position");
                    } else {
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
                    eventBus.post(new ToggleHoloModelDrawEvent());
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
