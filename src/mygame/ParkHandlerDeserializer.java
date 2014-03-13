package mygame;

import com.google.common.eventbus.EventBus;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.jme3.light.AmbientLight;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Spatial;
import java.lang.reflect.Type;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import mygame.npc.CreateGuestEvent;
import mygame.npc.inventory.Item;
import mygame.npc.inventory.StatManager;
import mygame.npc.inventory.Wallet;
import mygame.terrain.Direction;
import mygame.terrain.ParkHandler;
import mygame.terrain.ParkWallet;
import mygame.terrain.events.AddToRootNodeEvent;

/**
 *
 * @author arska
 */
public class ParkHandlerDeserializer implements JsonDeserializer<ParkHandler>{
    private static final Logger logger = Logger.getLogger(ParkHandlerDeserializer.class.getName());
    private ParkHandler parkHandler;
    private EventBus eventBus;
    public ParkHandlerDeserializer(ParkHandler parkHandler,EventBus eventBus) {
        this.parkHandler=parkHandler;
        this.eventBus=eventBus;
    }
    
    public ParkHandler deserialize(JsonElement je, Type type, JsonDeserializationContext jdc) throws JsonParseException {
        final JsonObject jo = je.getAsJsonObject();
        //META DATA
        String parkname = getS(jo,"parkName");
        int rideID=getI(jo,"rideID");
        int shopID=getI(jo,"shopID");
        int maxGuests=getI(jo,"maxGuests");
        ParkWallet pw=jdc.deserialize(jo.get("parkwallet"),ParkWallet.class);
        parkHandler.setUp(parkname, rideID, shopID,pw, maxGuests);
        
        //GUESTS
        JsonArray guestsarray = jo.get("guests").getAsJsonArray();
        for(int x=0;x<guestsarray.size();x++){
            final JsonObject jg = guestsarray.get(x).getAsJsonObject();
            Wallet gw=jdc.deserialize(jg.get("wallet"),Wallet.class);
            StatManager sm=jdc.deserialize(jg.get("stats"),StatManager.class);
            Direction dir=jdc.deserialize(jg.get("direction"),Direction.class);
            List<Item> inv = jdc.deserialize(jg.get("inventory"),new TypeToken<List<Item>>(){}.getType());
            String name=getS(jg,"name");
            int guestnum=getI(jg,"guestnum");
            int x1=getI(jg,"x");
            int y1=getI(jg,"y");
            int z1=getI(jg,"z");
            Spatial model=UtilityMethods.loadModel("Models/Human/guest2.j3o");
            
            eventBus.post(new CreateGuestEvent(gw,inv,guestnum,dir,x1,y1,z1,sm,model,name));
        }
        
        //TERRAIN TEMP FIX
        int mapHeight = getI(jo,"mapHeight");
        int mapWidth = getI(jo,"mapWidth");
        parkHandler.setMapSize(mapHeight, mapWidth);
        float[] mapData = new float[128*128];
        for (int i = 0; i < 128*128; i++) {
                mapData[i] = 6;
        }
        
        Spatial[][][] map = new Spatial[mapHeight][25][mapWidth];
        parkHandler.setMap(map, mapData);
        //ATTACH LIGHTS
        attachDirectionalLights();
        
        //DONE
        logger.log(Level.FINEST, "PARKHANDLER DONE!");
        return parkHandler;
        
    }
    private String getS(JsonObject j,String name){
        return j.get(name).getAsString();
    }
    private int getI(JsonObject j,String name){
        return j.get(name).getAsInt();
    }
    /**
     * There needs to be light so that models in the Node are visible.
     */
    private void attachDirectionalLights() {
//        DirectionalLight sun = new DirectionalLight();
//        sun.setDirection((new Vector3f(0.5f, -0.5f, 0.5f).normalizeLocal()));
//        sun.setColor(ColorRGBA.White);
//        rootNode.addLight(sun);
//        DirectionalLight sun2 = new DirectionalLight();
//        sun2.setDirection((new Vector3f(-0.5f, 0.5f, -0.5f).normalizeLocal()));
//        sun2.setColor(ColorRGBA.White);
//        rootNode.addLight(sun2); 
        
        AmbientLight l=new AmbientLight();
        l.setColor(new ColorRGBA(1,0,1,0.5f));
        
        eventBus.post(new AddToRootNodeEvent(l));
    }
    
}
