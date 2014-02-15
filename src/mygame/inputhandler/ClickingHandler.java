/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.inputhandler;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.scene.Node;
import java.util.logging.Level;
import java.util.logging.Logger;
import mygame.GUI.UpdateMoneyTextBarEvent;
import mygame.GUI.WindowMaker;
import mygame.Gamestate;
import mygame.Main;
import mygame.npc.Guest;
import mygame.ride.BasicRide;
import mygame.shops.BasicShop;
import mygame.terrain.events.DeleteSpatialFromMapEvent;
import mygame.terrain.MapContainer;
import mygame.terrain.ParkHandler;
import mygame.terrain.events.PayParkEvent;
import mygame.terrain.RoadMaker;
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
    @Inject
    private TerrainHandler terrainHandler;
    @Inject
    EventBus eventBus;
    @Inject
    WindowMaker windowMaker;
    public ClickingModes clickMode = ClickingModes.NOTHING;
    public int buffer = 0;
    private final Node rootNode;
    private final ParkHandler parkHandler;
    private final RoadMaker roadMaker;
    private final DecorationManager decorationManager;

    @Inject
    public ClickingHandler(Node rootNode, ParkHandler parkHandler, RoadMaker roadMaker, DecorationManager decorationManager) {
        
        this.rootNode = rootNode;
        this.parkHandler = parkHandler;
        this.roadMaker = roadMaker;
        this.decorationManager = decorationManager;

    }

    public void handleClicking(CollisionResult target, CollisionResults results) {
        if (target != null) {
            logger.log(Level.FINEST, target.getContactPoint().toString()+" .");
        }else{
            logger.log(Level.FINER, "NULL");
        }
        switch (clickMode) {
            case TERRAIN:
                terrainHandler.handleClicking(target);
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
                    for (Guest g : Main.gamestate.npcManager.guests) {
                        if (g.getGuestNum() == rootTarget.getUserData("guestnum")) {
                            windowMaker.createGuestWindow(g, true);
                            logger.log(Level.FINEST, "Displaying Guestwindow for guest with id {1}", g.getGuestNum());
                            return;
                        }
                    }
                }
                if (rootTarget.getUserData("shopID") != null) {
                    for (BasicShop g : Main.gamestate.shopManager.shops) {
                        if (g.shopID == rootTarget.getUserData("shopID")) {
                            windowMaker.createShopWindow(g);
                            logger.log(Level.FINEST, "Displaying Shopwindow for shop with id {1}", g.shopID);
                            return;
                        }
                    }
                }
                if (rootTarget.getUserData("rideID") != null) {
                    for (BasicRide r : Main.gamestate.rideManager.rides) {
                        if (r.getRideID() == rootTarget.getUserData("rideID")) {
                            windowMaker.CreateRideWindow(r);
                            logger.log(Level.FINEST, "Displaying Ridewindow for Ride with id {1}", r.getRideID());
                            return;
                        }
                    }
                }

                break;

            case ROAD:
                if (target != null) {
                    if (Main.gamestate.roadMaker.status == RoadMakerStatus.CHOOSING) {
                        Main.gamestate.roadMaker.startingPosition(target.getContactPoint());
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
                    Main.gamestate.rideManager.placeEnterance(target.getContactPoint());
                    Gamestate.ingameHUD.updateClickingIndicator();
                } else {
                }
                break;

            case PLACE:
                if (buffer == 0) {
                    Main.gamestate.shopManager.buy(parkHandler);
                    Main.gamestate.holoDrawer.toggleDrawSpatial();
                    Gamestate.ingameHUD.updateClickingIndicator();
                } else {
                    buffer = buffer - 1;
                }
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
}
