/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import mygame.gameplayorgans.Scenario;
import mygame.gameplayorgans.ScenarioGoal;
import mygame.npc.Guest;
import mygame.ride.BasicRide;
import mygame.shops.BasicShop;
import mygame.terrain.MapContainer;
import mygame.terrain.ParkHandler;
import mygame.terrain.QueRoad;
import mygame.terrain.Road;
import mygame.terrain.decoration.Decoration;

/**
 *
 * @author arska
 */
@Singleton
public class SaveManager {
    private static final Logger logger = Logger.getLogger(SaveManager.class.getName());
    private final ParkHandler parkHandler;
    private final MapContainer map;
    /**
     * 
     * @param loadmanager
     * @param parkHandler
     * @param map 
     */
    @Inject
    public SaveManager(ParkHandler parkHandler,MapContainer map){
        this.parkHandler=parkHandler;
        this.map=map;
    }
    
    /**
     * 
     * @param filename 
     */
    public void Save(String filename) {
        Gson gson;
        GsonBuilder ga=new GsonBuilder();
        gson=ga.setPrettyPrinting().create();
        
        Writer writer = null;
        logger.log(Level.FINEST,"Starting to save {0}",filename);
        try {
            writer = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(filename + ".IntoFile"), "utf-8"));
            gson.toJson(parkHandler, writer);
            /*
            writer.write("#"+System.getProperty("line.separator"));
            logger.log(Level.FINEST,"Writing META data...");
            writeMetaData(writer);
            logger.log(Level.FINEST,"Writing scenario data...");
            writeScenarioData(writer);
            
            logger.log(Level.FINEST,"Writing terrain data...");
            writeTerrainData(writer);
            logger.log(Level.FINEST,"Writing shop data...");
            writeShopData(writer);
            logger.log(Level.FINEST,"Writing guest data...");
            writeGuests(writer);
            logger.log(Level.FINEST,"Writing ride data...");
            writeRideData(writer);
            logger.log(Level.FINEST,"Writing road data...");
            writeRoadData(writer);
            logger.log(Level.FINEST,"Writing quepark data...");
            writeQueRoads(writer);
            logger.log(Level.FINEST,"Writing decoration data...");
            writeDecorations(writer);*/
        } catch (IOException ex) {
            logger.log(Level.SEVERE,"Something failed! Couldn't save game data. your save might be corrupt!");
        } finally {
            try {
                writer.close();
                logger.log(Level.INFO,"Game saved");
                
            } catch (Exception ex) {
            }
        }
    }
    
    private void writeGuests(Writer writer) throws IOException {
        List<Guest> guests = parkHandler.getGuests();
        int size = guests.size();
        writer.write("GUEST DATA " + ":" + "guest size:" + Integer.toString(size) + ":");
        for (Guest g : guests) {
            String name = g.getName();
            String money = Float.toString(g.wallet.getmoney());
            String moving = "null";
            switch (g.getmoveDirection()) {
                case UP:
                    moving = "UP";
                    break;

                case DOWN:
                    moving = "DOWN";
                    break;

                case RIGHT:
                    moving = "RIGHT";
                    break;

                case LEFT:
                    moving = "LEFT";
            }
            String x = Integer.toString(g.getX());
            String z = Integer.toString(g.getZ());
            String y = Integer.toString(g.getY());
            String hunger = Integer.toString(g.stats.hunger);
            String thrist = Integer.toString(g.stats.thirst);
            String happyness = Integer.toString(g.stats.happyness);
            String preferredRide = "";
            String guestnum=Integer.toString(g.getGuestNum());
            switch (g.stats.preferredRide) {
                case CRAZY:
                    preferredRide = "CRAZY";
                    break;

                case HIGH:
                    preferredRide = "HIGH";
                    break;

                case LOW:
                    preferredRide = "LOW";
                    break;

                case MEDIUM:
                    preferredRide = "MEDIUM";
                    break;

                case NAUSEA:
                    preferredRide = "NAUSEA";

            }
            writer.write(name + ":" + money + ":" + moving + ":" + x + ":" + z + ":" + y + ":" + hunger + ":" + thrist + ":" + happyness + ":" + preferredRide + ":"+guestnum+":");

        }
        writer.write(System.getProperty("line.separator"));
    }

    private void writeMetaData(Writer writer) throws IOException {
        String parkname = parkHandler.getParkName();
        String parkmoney = Float.toString(parkHandler.getParkWallet().getMoney());
        String parkloan = Float.toString(parkHandler.getParkWallet().getLoan());
        String rideID = Integer.toString(parkHandler.getRideID());
        String shopID = Integer.toString(parkHandler.getShopID());
        String mapheight = Integer.toString(parkHandler.getMapHeight());
        String mapwidth = Integer.toString(parkHandler.getMapWidth());
        String maxGuests = Integer.toString(parkHandler.getMaxGuests());
        writer.write("META-DATA "+ ":" + parkname + ":" + parkmoney + ":" + parkloan + ":" + rideID + ":" + shopID + ":" + mapheight + ":" + mapwidth + ":"+maxGuests+":");
        writer.write(System.getProperty("line.separator"));
    }
    private void writeScenarioData(Writer writer) throws IOException {
        
        Scenario scene=new Scenario(ScenarioGoal.GUESTS);
        //TEMP
        scene.setEnterancePos(new Vector3f(4, 6, 0));
        scene.setEnteranceYRotation(1);
        scene.setGoal(ScenarioGoal.GUESTS);
        //END
        String enteranceXPosition=Float.toString(scene.getEnterancePos().x);
        String enteranceYPosition=Float.toString(scene.getEnterancePos().y);
        String enteranceZPosition=Float.toString(scene.getEnterancePos().z);
        String enteranceYRotation=Double.toString(scene.getEnteranceYRotation());
        String scenarioGoal=scene.getGoal().toString();
        writer.write("SCENARIO DATA " + ":" + enteranceXPosition + ":" + enteranceYPosition + ":" + enteranceZPosition + ":" + enteranceYRotation + ":" + scenarioGoal + ":");
        
        writer.write(System.getProperty("line.separator"));
    }

    private void writeTerrainData(Writer writer) throws IOException {
        float[] mapdata = map.getMapData();
        int height = parkHandler.getMapHeight();
        int width = parkHandler.getMapWidth();
        writer.write("MAP DATA " + ":" + "map size:" + Integer.toString(height) + ":" + Integer.toString(width) + ":");
        final int mapsize=128;
        for (int o = 0; o < height; o++) {
            for (int u = 0; u < width; u++) {
                float arvo = mapdata[o*mapsize+u];
                writer.write(Float.toString(arvo) + ":");
            }
        }
        writer.write(System.getProperty("line.separator"));
    }
    private void writeRideData(Writer writer) throws IOException {
        String quantity = Integer.toString(parkHandler.getRides().size());
        writer.write("RIDE DATA "+ ":" +"ride size:" + quantity + ":");
        for (BasicRide r : parkHandler.getRides()) {
            String name = r.getName();
            String type = r.getRide();
            String price = Float.toString(r.getPrice());
            String x = Float.toString(r.getPositionVector().x);
            String z = Float.toString(r.getPositionVector().z);
            String y = Float.toString(r.getPositionVector().y);
            String rideid = Integer.toString(r.getRideID());
            String exitement = Integer.toString(r.getExitement());
            String nausea = Integer.toString(r.getNausea());
            String broken = Integer.toString(r.getBroken());
            String status;
            if (r.getStatus()) {
                status = "ON";
            } else {
                status = "OFF";
            }
            //enterance + exit
            String a = "0";
            String b = "0";
            if (r.enterance != null) {
                a = "1";
            }
            if (r.exit != null) {
                b = "1";
            }

            String result = a + b;
            writer.write(name + ":" + type + ":" + price + ":" + x + ":" + z + ":" + y + ":" + rideid + ":" + exitement + ":" + nausea + ":" + broken + ":" + status + ":" + result + ":");
            if (a.equals("1")) {
                writeEnteranceData(r, writer);
            }
            if (b.equals("1")) {
                writeExitData(r, writer);
            }

        }
        writer.write(System.getProperty("line.separator"));
    }

    private void writeEnteranceData(BasicRide r, Writer writer) throws IOException {
        String enteranceX = Integer.toString((int) r.enterance.location.x);
        String enteranceZ = Integer.toString((int) r.enterance.location.z);
        String enteranceY = Integer.toString((int) r.enterance.location.y);
        String exit="";
        if (r.enterance.exit) {
            exit = "TRUE";
        } else {
            exit = "FALSE";
        }
        String enterancedirection = null;
        switch (r.enterance.facing) {
            case UP:
                enterancedirection = "UP";
                break;

            case DOWN:
                enterancedirection = "DOWN";
                break;

            case RIGHT:
                enterancedirection = "RIGHT";
                break;

            case LEFT:
                enterancedirection = "LEFT";
        }
        String connected;
        if (r.enterance.connected) {
            connected = "TRUE";
        } else {
            connected = "FALSE";
        }
        writer.write(enteranceX + ":" + enteranceZ + ":" + enteranceY + ":" + enterancedirection + ":" + connected + ":");
    }

    private void writeExitData(BasicRide r, Writer writer) throws IOException {
        String exitX = Integer.toString((int) r.exit.location.x);
        String exitZ = Integer.toString((int) r.exit.location.z);
        String exitY = Integer.toString((int) r.exit.location.y);
        String exit;
        if (r.exit.exit) {
            exit = "TRUE";
        } else {
            exit = "FALSE";
        }
        String exitdirection = null;
        switch (r.exit.facing) {
            case UP:
                exitdirection = "UP";
                break;

            case DOWN:
                exitdirection = "DOWN";
                break;

            case RIGHT:
                exitdirection = "RIGHT";
                break;

            case LEFT:
                exitdirection = "LEFT";
        }
        String connected;
        if (r.exit.connected) {
            connected = "TRUE";
        } else {
            connected = "FALSE";
        }
        writer.write(exitX + ":" + exitZ + ":" + exitY + ":" + exitdirection + ":" + connected + ":");
    }

    private ArrayList<Road> getRoadstoClasses() {
        ArrayList<Spatial> roads = new ArrayList<Spatial>(); //tänne kerätään roadit 
        ArrayList<Road> roadListTrue = new ArrayList<Road>();  //tänne tehdään objektit
        
        for (int xi = 0; xi < parkHandler.getMapHeight(); xi++) {
            for (int zi = 0; zi < parkHandler.getMapWidth(); zi++) {
                for (int yi = 0; yi < 15; yi++) {
                    Spatial tested = map.getMap()[xi][yi][zi];
                    if (tested != null) {
                        if (tested.getUserData("type").equals("road")) {
                            roads.add(tested);
                        }
                    }
                }
            }
        }
        for (Spatial s : roads) {
            String x = Integer.toString((int) s.getWorldTranslation().x);
            String z = Integer.toString((int) s.getWorldTranslation().z);
            String y = Integer.toString((int) s.getWorldTranslation().y);
            String roadhill = s.getUserData("roadHill");
            Road created = new Road(x, z, y, roadhill, "0");
            roadListTrue.add(created);
        }
        return roadListTrue;
    }

    private void writeRoadData(Writer writer) throws IOException {
        ArrayList<Road> roads = getRoadstoClasses();
        writer.write("ROAD DATA " + ":" +"roads size:" + roads.size() + ":");
        for (Road r : roads) {
            writer.write(r.x + ":" + r.z + ":" + r.y + ":" + r.roadhill + ":" + r.ID + ":");

        }
        writer.write(System.getProperty("line.separator"));
    }

    private ArrayList<QueRoad> getQueRoads() {
        ArrayList<Spatial> queroads = new ArrayList<Spatial>(); //tänne kerätään roadit 
        ArrayList<QueRoad> queroadListTrue = new ArrayList<QueRoad>();  //tänne tehdään objektit
        
        for (int xi = 0; xi < parkHandler.getMapHeight(); xi++) {
            for (int zi = 0; zi < parkHandler.getMapWidth(); zi++) {
                for (int yi = 0; yi < 15; yi++) {
                    Spatial tested = map.getMap()[xi][yi][zi];
                    if (tested != null) {
                        if (tested.getUserData("type").equals("queroad")) {
                            queroads.add(tested);
                        }
                    }
                }
            }
        }
        for (Spatial s : queroads) {
            String x = Integer.toString((int) s.getWorldTranslation().x);
            String z = Integer.toString((int) s.getWorldTranslation().z);
            String y = Integer.toString((int) s.getWorldTranslation().y);
            String roadhill = s.getUserData("roadHill");
            int ID=s.getUserData("roadID");
            String rideID=Integer.toString(ID);
            String queconnect1 = null;
            String queconnect2 = null;
            if(s.getUserData("queconnect1")!=null){
                Spatial o=s.getUserData("queconnect1");
                Integer que1=(Integer) o.getUserData("roadID");
                if(que1!=null){
                    
                    queconnect1=Integer.toString(que1);
                }
                
                
            }
            if(s.getUserData("queconnect2")!=null){
                Spatial o=s.getUserData("queconnect2");
                Integer que2=(Integer) o.getUserData("roadID");
                if(que2!=null){
                    
                    queconnect2=Integer.toString(que2); 
                }
                
            }
            
            String connected;
            if(s.getUserData("connected")==null){
                connected="nullerino";
            }
            else{
                if(s.getUserData("connected")){
                    connected="TRUE";
                }else{
                    connected="FALSE";
                }
            }
            String direction=s.getUserData("direction");
            QueRoad created = new QueRoad(x, z, y, roadhill,rideID,queconnect1,queconnect2,connected,direction);
            queroadListTrue.add(created);
        }
        return queroadListTrue;
    }
    private void writeQueRoads(Writer writer) throws IOException {
        ArrayList<QueRoad>queroads=getQueRoads();
        writer.write("QUEROAD DATA " + ":" + "queroad size:"+queroads.size()+":");
        for(QueRoad q:queroads){
            String x=q.x;
            String z=q.z;
            String y=q.y;
            String roadhill=q.roadhill;
            String connected=q.connected;
            String queconnect1=q.queconnect1;
            String queconnect2=q.queconnect2;
            String direction=q.direction;
            writer.write(x+":"+z+":"+y+":"+roadhill+":"+q.ID+":"+connected+":"+queconnect1+":"+queconnect2+":"+direction+":");
        }
        writer.write(System.getProperty("line.separator"));
    }
    private void writeDecorations(Writer writer) throws IOException{
        ArrayList<Decoration>decorations=getDecorations();
        writer.write("DECORATION DATA " + ":" + "decoration size:"+decorations.size()+":");
        for(Decoration d:decorations){
            String x=d.x;
            String y=d.y;
            String z=d.z;
            String type=d.type;
            String direction=d.direction;
            writer.write(x+":"+y+":"+z+":"+type+":"+direction+":");
        }
        writer.write(System.getProperty("line.separator"));
    }
    private ArrayList<Decoration> getDecorations(){
        ArrayList<Decoration>decorations=new ArrayList<Decoration>();
        
        for (int xi = 0; xi < parkHandler.getMapHeight(); xi++) {
            for (int zi = 0; zi < parkHandler.getMapWidth(); zi++) {
                for (int yi = 0; yi < 15; yi++) {
                    if(map.getMap()[xi][yi][zi]!=null){
                        Spatial test=map.getMap()[xi][yi][zi];
                        if(test.getUserData("type").equals("decoration")){
                            String x=Float.toString(test.getLocalTranslation().x);
                            String y=Float.toString(test.getLocalTranslation().y);
                            String z=Float.toString(test.getLocalTranslation().z);
                            String direction=test.getUserData("direction").toString();
                            String type=test.getUserData("decoration");
                            Decoration d=new Decoration(x, y, z,type,direction);
                            decorations.add(d);
                        }
                    }
                }
            }
        }
        return decorations;
    }
}
