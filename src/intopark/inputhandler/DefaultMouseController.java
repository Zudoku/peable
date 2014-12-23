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
import intopark.inout.Identifier;
import intopark.npc.Guest;
import intopark.ride.BasicRide;
import intopark.shops.BasicShop;
import intopark.terrain.ParkHandler;
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
    private Identifier identifier;
    @Inject
    public DefaultMouseController(ClickingHandler handler,WindowHandler manager,ParkHandler parkHandler,Identifier identifier) {
        this.handler = handler;
        this.windowHandler=manager;
        this.parkHandler=parkHandler;
        this.identifier=identifier;
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
                        int ID=(int) rootTarget.getUserData("ID");
                        Object object = identifier.getObjectWithID(ID);
                        if(!(object instanceof Guest)){
                            logger.log(Level.SEVERE, "ID corruption. ID {0} should return Guest",ID);
                            return;
                        }
                        Guest g = (Guest) object;
                        windowHandler.setIDforGuest(g.getID());
                        windowHandler.updateGuestWindow(true, true);
                        logger.log(Level.FINEST, "Displaying Guestwindow for guest with id {0}", g.getID());
                        return;
                    }
                    if (rootTarget.getUserData("type").equals("shop")) {
                        int ID=(int) rootTarget.getUserData("ID");
                        Object object = identifier.getObjectWithID(ID);
                        if(!(object instanceof BasicShop)){
                            logger.log(Level.SEVERE, "ID corruption. ID {0} should return Shop",ID);
                            return;
                        }
                        BasicShop shop = (BasicShop)object;
                        windowHandler.setIDforShop(shop.getID());
                        windowHandler.updateShopWindow(true);
                        logger.log(Level.FINEST, "Displaying Shopwindow for shop with id {0}", shop.getID());
                        return;
                    }
                    if (rootTarget.getUserData("type").equals("ride")||rootTarget.getUserData("type").equals("rideEnterance")) {
                        int ID=(int) rootTarget.getUserData("ID");
                        Object object = identifier.getObjectWithID(ID);
                        if(!(object instanceof BasicRide)){
                            logger.log(Level.SEVERE, "ID corruption. ID {0} should return Ride",ID);
                            return;
                        }
                        BasicRide r=(BasicRide)object;
                        windowHandler.setIDforRide(r.getID());
                        windowHandler.updateRideWindow(true, true);
                        logger.log(Level.FINEST, "Displaying Ridewindow for Ride with id {0}", r.getID());
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

    @Override
    public void onCursorHover(MouseContainer container) {

    }

}
