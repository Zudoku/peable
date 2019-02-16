/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package intopark.input.mouse;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.jme3.collision.CollisionResult;
import com.jme3.scene.Node;
import intopark.inout.Identifier;
import intopark.npc.Guest;
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
    private Identifier identifier;
    @Inject
    public DefaultMouseController(ClickingHandler handler,Identifier identifier) {
        this.handler = handler;
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
                        logger.log(Level.FINEST, "Displaying Guestwindow for guest with id {0}", g.getID());
                        return;
                    }
                    if (rootTarget.getUserData("type").equals("shop")) {
                        int ID=(int) rootTarget.getUserData("ID");
                        Object object = identifier.getObjectWithID(ID);
                        return;
                    }
                    if (rootTarget.getUserData("type").equals("ride")||rootTarget.getUserData("type").equals("rideEnterance")) {
                        int ID=(int) rootTarget.getUserData("ID");
                        Object object = identifier.getObjectWithID(ID);
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
