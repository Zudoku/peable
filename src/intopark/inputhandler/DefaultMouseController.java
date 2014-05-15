/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package intopark.inputhandler;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.jme3.collision.CollisionResult;
import com.jme3.scene.Node;
import intopark.GUI.WindowHandler;
import intopark.GUI.events.UpdateMoneyTextBarEvent;
import intopark.npc.Guest;
import intopark.ride.BasicRide;
import intopark.shops.BasicShop;
import intopark.terrain.ParkHandler;
import intopark.terrain.events.DeleteSpatialFromMapEvent;
import intopark.terrain.events.PayParkEvent;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author arska
 */
@Singleton
public class DefaultMouseController implements NeedMouse{
    private static final Logger logger = Logger.getLogger(DefaultMouseController.class.getName());
    private ClickingHandler handler;
    private WindowHandler windowHandler;
    private ParkHandler parkHandler;
    @Inject
    public DefaultMouseController(ClickingHandler handler,WindowHandler manager,ParkHandler parkHandler) {
        this.handler = handler;
        this.windowHandler=manager;
        this.parkHandler=parkHandler;
    }

    @Override
    public void onClick(MouseContainer container) {
        CollisionResult target=container.getResults().getClosestCollision();
        if (target.getGeometry().getParent() == null) {
            logger.log(Level.WARNING, "Clicked object which has no parent!");
            return;
        }
        if (target.getGeometry().getParent().getParent() == null) {
            logger.log(Level.WARNING, "Clicked object which has no parent's parent!");
            return;
        }
        Node rootTarget = target.getGeometry().getParent().getParent();
        if(rootTarget.getUserData("type")== null){
            System.err.println("FAIL");
            return;
        }
        switch(handler.getClickMode()){
            case NOTHING:
                if(container.isLeftClick()){
                    
                    if (rootTarget.getUserData("type").equals("guest")) {
                        Guest g = parkHandler.getGuestWithID((int) rootTarget.getUserData("guestnum"));
                        windowHandler.setGuestID(g.getGuestNum());
                        windowHandler.updateGuestWindow(true, true);
                        logger.log(Level.FINEST, "Displaying Guestwindow for guest with id {0}", g.getGuestNum());
                        return;
                    }
                    if (rootTarget.getUserData("type").equals("shop")) {
                        BasicShop shop = parkHandler.getShopWithID((int) rootTarget.getUserData("shopID"));
                        windowHandler.setShopID(shop.getShopID());
                        windowHandler.updateShopWindow(true);
                        logger.log(Level.FINEST, "Displaying Shopwindow for shop with id {0}", shop.getShopID());
                        return;
                    }
                    if (rootTarget.getUserData("rideID") != null) {
                        BasicRide r=parkHandler.getRideWithID((int)rootTarget.getUserData("rideID"));
                        windowHandler.setRideID(r.getRideID());
                        windowHandler.updateRideWindow(true, true);
                        logger.log(Level.FINEST, "Displaying Ridewindow for Ride with id {0}", r.getRideID());
                        return;
                    }
                }
                break;
                
            case DEMOLITION:
                if (rootTarget.getUserData("type").equals("road")) {
                    //TODO:
                }
                if (rootTarget.getUserData("type").equals("decoration")) {
                    //TODO:

                }
                break;
        }
    }

    @Override
    public void onDrag(MouseContainer container) {
        
    }

    @Override
    public void onDragRelease(MouseContainer container) {
        
    }
    
}
