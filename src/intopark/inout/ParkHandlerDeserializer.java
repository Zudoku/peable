package intopark.inout;

import com.google.common.eventbus.EventBus;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import intopark.UtilityMethods;
import java.lang.reflect.Type;
import java.util.List;
import java.util.logging.Logger;
import intopark.gameplayorgans.Scenario;
import intopark.npc.events.CreateGuestEvent;
import intopark.npc.inventory.Item;
import intopark.npc.inventory.StatManager;
import intopark.npc.inventory.Wallet;
import intopark.roads.Road;
import intopark.roads.RoadHill;
import intopark.roads.events.CreateRoadEvent;
import intopark.util.Direction;
import intopark.terrain.MapContainer;
import intopark.util.MapPosition;
import intopark.terrain.ParkHandler;
import intopark.terrain.ParkWallet;
import intopark.terrain.events.AddToRootNodeEvent;
import java.util.logging.Level;

/**
 *
 * @author arska
 */
public class ParkHandlerDeserializer implements JsonDeserializer<ParkHandler>{
    //LOGGER
    private static final Logger logger = Logger.getLogger(ParkHandlerDeserializer.class.getName());
    //DEPENDENCIES
    private ParkHandler parkHandler;
    private EventBus eventBus;
    public ParkHandlerDeserializer(ParkHandler parkHandler,EventBus eventBus) {
        this.parkHandler=parkHandler;
        this.eventBus=eventBus;
    }
    /**
     * Deserialize JSON file that contains the whole imformation of the scenario.
     * It will assign all the information to parkHandler automatically using EventBus.
     * If error occurs during the deserialization, JsonParseException will be thrown.
     * If you want to understand this class, you should look at the .IntoFile files and JSON.
     * @param je
     * @param type
     * @param jdc
     * @return
     * @throws JsonParseException if something goes wrong.
     */
    public ParkHandler deserialize(JsonElement je, Type type, JsonDeserializationContext jdc) throws JsonParseException {
        logger.log(Level.FINER,"Beginning to construct park");
        final JsonObject jo = je.getAsJsonObject();
        //META DATA
        logger.log(Level.FINER,"Deserializing META-DATA...");
        Scenario scene=jdc.deserialize(jo.get("scenario"),Scenario.class);
        ParkWallet pw=jdc.deserialize(jo.get("parkwallet"),ParkWallet.class);
        parkHandler.setScenario(scene);
        parkHandler.setParkWallet(pw);
        //GUESTS
        logger.log(Level.FINER,"Deserializing guests...");
        JsonArray guestsarray = jo.get("guests").getAsJsonArray();
        for(int x=0;x<guestsarray.size();x++){
            final JsonObject jg = guestsarray.get(x).getAsJsonObject();
            Wallet gw=jdc.deserialize(jg.get("wallet"),Wallet.class);
            StatManager sm=jdc.deserialize(jg.get("stats"),StatManager.class);
            Direction dir=jdc.deserialize(jg.get("moving"),Direction.class);
            List<Item> inv = jdc.deserialize(jg.get("inventory"),new TypeToken<List<Item>>(){}.getType());
            String name=getS(jg,"name");
            int ID=getI(jg,"ID");
            int x1=getI(jg,"x");
            int y1=getI(jg,"y");
            int z1=getI(jg,"z");
            boolean male=getB(jg, "male");
            double walkingSpeed = getD(jg,"walkingSpeed");
            Spatial model=UtilityMethods.loadModel(LoadPaths.guest);
            CreateGuestEvent event=new CreateGuestEvent(gw,inv,ID,dir,x1,y1,z1,sm,model,name,parkHandler,male,walkingSpeed);
            eventBus.post(event);
        }
        logger.log(Level.FINER,"Deserializing terrain...");
        MapContainer map=jdc.deserialize(jo.get("map"),MapContainer.class);
        parkHandler.setMap(map);
        //ROADS
        logger.log(Level.FINEST, "Deserializing roads...");
        JsonArray roadsarray = jo.get("roads").getAsJsonArray();
        for(int x=0;x<roadsarray.size();x++){
            final JsonObject rp = roadsarray.get(x).getAsJsonObject();
            MapPosition pos=jdc.deserialize(rp.get("position"),MapPosition.class);
            Direction dir=jdc.deserialize(rp.get("direction"),Direction.class);
            RoadHill hill=jdc.deserialize(rp.get("roadhill"),RoadHill.class);
            int ID=getI(rp,"ID");
            int skin=getI(rp,"skin");
            boolean queroad=getB(rp,"queRoad");
            //FINISH THE ROAD
            Road road=new Road(pos, hill, ID, skin, queroad, dir);
            CreateRoadEvent event=new CreateRoadEvent(road);
            eventBus.post(event);
            logger.log(Level.FINER,"Road {0}, Initialized!",ID);
        }
        //ATTACH LIGHTS
        logger.log(Level.FINER,"Attaching lights");
        attachDirectionalLights();

        //DONE
        logger.log(Level.FINER, "Loading complete");
        return parkHandler;

    }
    /**
     * Utility method to write less when accessing String fields.
     * @param j
     * @param name
     * @return j.get(name).getAsString();
     */
    private String getS(JsonObject j,String name){
        return j.get(name).getAsString();
    }
    /**
     * Utility method to write less when accessing integer fields.
     * @param j
     * @param name
     * @return j.get(name).getAsInt();
     */
    private int getI(JsonObject j,String name){
        return j.get(name).getAsInt();
    }
    /**
     * Utility method to write less when accessing boolean fields.
     * @param j
     * @param name
     * @return j.get(name).getAsBoolean();
     */
    private boolean getB(JsonObject j,String name){
        return j.get(name).getAsBoolean();
    }
    /**
     * Utility method to write less when accessing float fields.
     * @param j
     * @param name
     * @return j.get(name).getAsFloat();
     */
    private float getF(JsonObject j,String name){
        return j.get(name).getAsFloat();
    }
    private double getD(JsonObject j,String name){
        return j.get(name).getAsDouble();
    }
    /**
     * There needs to be light so that models in the Node are visible.
     */
    private void attachDirectionalLights() {
        DirectionalLight sun = new DirectionalLight();
        sun.setDirection((new Vector3f(0.5f, -0.5f, 0.5f).normalizeLocal()));
        sun.setColor(ColorRGBA.White);
        eventBus.post(new AddToRootNodeEvent(sun));
        DirectionalLight sun2 = new DirectionalLight();
        sun2.setDirection((new Vector3f(-0.5f, 0.5f, -0.5f).normalizeLocal()));
        sun2.setColor(ColorRGBA.White);
        eventBus.post(new AddToRootNodeEvent(sun2));

        AmbientLight l=new AmbientLight();
        l.setColor(new ColorRGBA(1,0,1,0.5f));

        eventBus.post(new AddToRootNodeEvent(l));
    }

}
