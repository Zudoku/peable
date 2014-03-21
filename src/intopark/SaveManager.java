/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package intopark;

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
import intopark.gameplayorgans.Scenario;
import intopark.gameplayorgans.ScenarioGoal;
import intopark.npc.Guest;
import intopark.ride.BasicRide;
import intopark.shops.BasicShop;
import intopark.terrain.MapContainer;
import intopark.terrain.ParkHandler;
import intopark.terrain.QueRoad;
import intopark.terrain.Road;
import intopark.terrain.decoration.Decoration;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;

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
        } catch (UnsupportedEncodingException ex) {
            logger.log(Level.SEVERE,"Unsupported encoding! {0}",ex);
        } catch (FileNotFoundException ex) {
            logger.log(Level.SEVERE,"File not found! {0}",ex);
        }
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
