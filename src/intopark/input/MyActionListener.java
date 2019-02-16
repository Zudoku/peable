/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package intopark.input;

import intopark.input.mouse.ClickingHandler;
import com.google.common.eventbus.EventBus;
import com.jme3.collision.CollisionResults;
import com.jme3.input.InputManager;
import com.jme3.input.controls.ActionListener;
import java.util.logging.Logger;
import intopark.UtilityMethods;
import static intopark.input.mouse.ClickingModes.DECORATION;
import static intopark.input.mouse.ClickingModes.PLACE;
import static intopark.input.mouse.ClickingModes.ROAD;

/**
 *
 * @author arska
 */
public class MyActionListener implements ActionListener {
    private static final Logger logger = Logger.getLogger(MyActionListener.class.getName());
    //DEPENDENCY
    private InputManager inputManager;
    private ClickingHandler clickingHandler;
    private EventBus eventBus;
    //VARIABLES
    private long lastclicked = 0;
    
    public MyActionListener(InputManager inputManager, ClickingHandler clickH, EventBus eventBus) {
        this.inputManager=inputManager;
        this.clickingHandler = clickH;
        this.eventBus = eventBus;
    }

    @Override
    public void onAction(String name, boolean isPressed, float tpf) {
        if (name.equals("mouseleftclick")||name.equals("mouserightclick")) {
            if (System.currentTimeMillis() - lastclicked > 100) {
                lastclicked = System.currentTimeMillis();
                CollisionResults results = new CollisionResults();
                UtilityMethods.rayCast(results, null);
                float x=inputManager.getCursorPosition().x;
                float z=inputManager.getCursorPosition().y;
                if (results.size() > 0) {
                    clickingHandler.handleMouseClick(UserInput.getLeftMouse(name),x,z, results);
                }
            }
        }
    }
}
