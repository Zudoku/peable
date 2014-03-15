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
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import java.lang.reflect.Type;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import mygame.gameplayorgans.Scenario;
import mygame.npc.CreateGuestEvent;
import mygame.npc.inventory.Item;
import mygame.npc.inventory.StatManager;
import mygame.npc.inventory.Wallet;
import mygame.shops.CreateShopEvent;
import mygame.shops.ShopReputation;
import mygame.terrain.Direction;
import mygame.terrain.MapContainer;
import mygame.terrain.MapPosition;
import mygame.terrain.ParkHandler;
import mygame.terrain.ParkWallet;
import mygame.terrain.events.AddToRootNodeEvent;

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
            Direction dir=jdc.deserialize(jg.get("direction"),Direction.class);
            List<Item> inv = jdc.deserialize(jg.get("inventory"),new TypeToken<List<Item>>(){}.getType());
            String name=getS(jg,"name");
            int guestnum=getI(jg,"guestnum");
            int x1=getI(jg,"x");
            int y1=getI(jg,"y");
            int z1=getI(jg,"z");
            Spatial model=UtilityMethods.loadModel("Models/Human/guest.j3o");
            
            eventBus.post(new CreateGuestEvent(gw,inv,guestnum,dir,x1,y1,z1,sm,model,name));
        }
        logger.log(Level.FINER,"Deserializing terrain...");
        MapContainer map=jdc.deserialize(jo.get("map"),MapContainer.class);
        parkHandler.setMap(map);
        //SHOPS
        logger.log(Level.FINER,"Deserializing shops...");
        JsonArray shopsarray = jo.get("shops").getAsJsonArray();
        for(int x=0;x<shopsarray.size();x++){
            final JsonObject sp = shopsarray.get(x).getAsJsonObject();
            Direction dir=jdc.deserialize(sp.get("facing"),Direction.class);
            MapPosition pos=jdc.deserialize(sp.get("position"),MapPosition.class);
            int shopID=getI(sp,"shopID");
            float cm=jdc.deserialize(sp.get("constructionmoney"),Float.class);
            String prodname=getS(sp,"productname");
            String shopname=getS(sp,"shopName");
            float price=jdc.deserialize(sp.get("price"),Float.class);
            ShopReputation sr=jdc.deserialize(sp.get("reputation"),ShopReputation.class);
            String stype=getS(sp,"type");
            CreateShopEvent event=new CreateShopEvent(stype,shopname,prodname,sr,price,cm,shopID,pos,dir);
            eventBus.post(event);   
        }
        //ATTACH LIGHTS
        logger.log(Level.FINER,"Attaching lights");
        attachDirectionalLights();
        
        //DONE
        logger.log(Level.FINER, "Loading complete");
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