/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package intopark.inout;

import com.google.common.eventbus.EventBus;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.jme3.scene.Node;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Logger;
import intopark.terrain.ParkHandler;
import java.util.logging.Level;

/**
 *
 * @author arska
 */
@Singleton
public class LoadManager {
    //LOGGER
    private static final Logger logger = Logger.getLogger(LoadManager.class.getName());
    private final Node rootNode;
    private EventBus eventBus;
    private ParkHandler parkHandler;

    /**
     * LoadManager is class to load .IntoFile files and transform them to Scenarios.
     * @param rootNode Attach rides,shops,guests,terrain to the world (Everything that is included in a park).
     * @param assetManager Load Ride Guest Shop models.
     * @param parkHandler Set lists of shops parks guests etc.
     * @param eventBus Send events.
     */
    @Inject
    public LoadManager(Node rootNode,ParkHandler parkHandler,EventBus eventBus) {
        this.rootNode = rootNode;
        this.parkHandler=parkHandler;
        this.eventBus=eventBus;

    }
    /**
     * Function to load .IntoFile files and transform them to scenarios.
     * @param filename path to file with .IntoFile at the end of it.
     * @param scenario Gives some extra info about the park including park-enterance transformation information.
     * @throws FileNotFoundException No such file!
     * @throws IOException Error opening file!
     */
    public void load(String filename){
        try {
            GsonBuilder gb=new GsonBuilder();
            gb.registerTypeAdapter(ParkHandler.class, new ParkHandlerDeserializer(parkHandler,eventBus));
            Gson gson = gb.create();
            gson.fromJson(new FileReader(filename), ParkHandler.class);
        } catch (FileNotFoundException ex) {
            logger.log(Level.WARNING,"Failed to load such file: {0} {1}",new Object[]{filename,ex});
        }
        
    }
}
