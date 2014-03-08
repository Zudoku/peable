/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.inputhandler;

import com.google.common.eventbus.EventBus;
import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.input.controls.ActionListener;
import com.jme3.scene.Node;
import java.util.logging.Logger;
import mygame.UtilityMethods;
import static mygame.inputhandler.ClickingModes.DECORATION;
import static mygame.inputhandler.ClickingModes.PLACE;
import static mygame.inputhandler.ClickingModes.ROAD;
import mygame.terrain.decoration.RotationEvent;

/**
 *
 * @author arska
 */
public class MyActionListener implements ActionListener {
    private static final Logger logger = Logger.getLogger(MyActionListener.class.getName());
    private long lastclicked = 0;
    private Node rootNode;
    private ClickingHandler clickingHandler;
    private EventBus eventBus;

    public MyActionListener(Node rootNode, ClickingHandler clickH, EventBus eventBus) {
        this.rootNode = rootNode;
        this.clickingHandler = clickH;
        this.eventBus = eventBus;
    }

    public void onAction(String name, boolean isPressed, float tpf) {
        if (name.equals("mouseleftclick")) {
            if (System.currentTimeMillis() - lastclicked > 100) {
                lastclicked = System.currentTimeMillis();

                CollisionResults results = new CollisionResults();
                UtilityMethods.rayCast(results, rootNode);

                if (results.size() > 0) {
                    CollisionResult target = results.getClosestCollision();
                    clickingHandler.handleClicking(target, results);
                    
                }
            }
        }
        if (name.equals("mouserightclick")) {
            /**
             * TODO! if(isPressed){ return; }
             */
            if (System.currentTimeMillis() - lastclicked > 100) {
                lastclicked = System.currentTimeMillis();

                CollisionResults results = new CollisionResults();
                UtilityMethods.rayCast(results, rootNode);

                if (results.size() > 0) {
                    CollisionResult target = results.getClosestCollision();
                    clickingHandler.handleRightClicking(target, results);

                }
            }
        }
        if (name.equals("rotateRight")) {
            if (isPressed) {
                return;
            }
            switch (clickingHandler.getClickMode()) {
                case DECORATION:
                    eventBus.post(new RotationEvent(1, 0));
                    break;

                case PLACE:
                    eventBus.post(new RotationEvent(1, 2));
                    break;

                case ROAD:
                    eventBus.post(new RotationEvent(1, 1));
                    break;


            }
        }
        if (name.equals("rotateLeft")) {
            if (isPressed) {
                return;
            }
            switch (clickingHandler.getClickMode()) {
                case DECORATION:
                    eventBus.post(new RotationEvent(0, 0));
                    break;

                case PLACE:
                    eventBus.post(new RotationEvent(0, 2));
                    break;

                case ROAD:
                    eventBus.post(new RotationEvent(0, 1));
                    break;
            }
        }
    }
}
