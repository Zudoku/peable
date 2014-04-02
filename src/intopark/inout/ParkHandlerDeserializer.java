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
import java.util.logging.Level;
import java.util.logging.Logger;
import intopark.gameplayorgans.Scenario;
import intopark.npc.events.CreateGuestEvent;
import intopark.npc.inventory.Item;
import intopark.npc.inventory.StatManager;
import intopark.npc.inventory.Wallet;
import intopark.ride.CreateRideEvent;
import intopark.ride.Enterance;
import intopark.shops.CreateShopEvent;
import intopark.shops.ShopReputation;
import intopark.terrain.Direction;
import intopark.terrain.MapContainer;
import intopark.terrain.MapPosition;
import intopark.terrain.ParkHandler;
import intopark.terrain.ParkWallet;
import intopark.terrain.events.AddToRootNodeEvent;

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
        //RIDES
        JsonArray ridesarray = jo.get("rides").getAsJsonArray();
        for(int x=0;x<ridesarray.size();x++){
            final JsonObject rp = ridesarray.get(x).getAsJsonObject();
            
            MapPosition pos=jdc.deserialize(rp.get("position"),MapPosition.class);
            String ride=getS(rp,"ride");
            Direction dir=jdc.deserialize(rp.get("facing"),Direction.class);
            String name=getS(rp,"rideName");
            int rideID=getI(rp,"rideID");
            int broken=getI(rp,"broken");
            int exitement=getI(rp,"exitement");
            int nausea=getI(rp,"nausea");
            boolean status=getB(rp,"status");
            float price=getF(rp,"price");
            
            //CONSTRUCT ENTERANCE
            Enterance exit=null;
            Enterance enterance = null;
            for(int i=0;i<2;i++){
                JsonObject enteranceObject;
                if(i==0){
                    enteranceObject=jo.get("enterance").getAsJsonObject();
                }else{
                    enteranceObject=jo.get("exit").getAsJsonObject();
                }
                boolean exitValue=getB(enteranceObject,"exit");
                MapPosition locationValue=jdc.deserialize(enteranceObject.get("location"),MapPosition.class);
                Direction directionValue=jdc.deserialize(enteranceObject.get("direction"),Direction.class);
                if(i==0){
                    enterance=new Enterance(exitValue, locationValue, directionValue);
                    continue;
                }
                exit=new Enterance(exitValue, locationValue, directionValue);
            }
            //FINISH THE RIDE
            CreateRideEvent event=new CreateRideEvent(pos,ride,dir,name,rideID,broken,exitement,nausea,status,price,enterance,exit);
            eventBus.post(event);
            logger.log(Level.FINER,"Ride {0}, Initialized!",name);
        }
        //ROADS
        
        //QUEROADS
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
    private boolean getB(JsonObject j,String name){
        return j.get(name).getAsBoolean();
    }
    private float getF(JsonObject j,String name){
        return j.get(name).getAsFloat();
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
