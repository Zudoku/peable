/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package intopark;

import com.google.common.eventbus.EventBus;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.jme3.asset.AssetManager;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Logger;
import intopark.ride.AnimationType;
import intopark.ride.BasicRide;
import intopark.ride.CustomAnimation;
import intopark.ride.Enterance;
import intopark.ride.actualrides.Archeryrange;
import intopark.ride.actualrides.Blender;
import intopark.ride.actualrides.ChessCenter;
import intopark.ride.actualrides.HauntedHouse;
import intopark.ride.actualrides.PirateShip;
import intopark.ride.actualrides.Rotor;
import intopark.terrain.events.AddObjectToMapEvent;
import intopark.terrain.Direction;
import intopark.terrain.MapPosition;
import intopark.terrain.ParkHandler;
import intopark.terrain.RoadFactory;
import intopark.terrain.decoration.DecorationFactory;
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
    private final AssetManager assetManager;
    private RoadFactory roadF;
    private ParkHandler parkHandler;

    /**
     * LoadManager is class to load .IntoFile files and transform them to Scenarios.
     * @param rootNode Attach rides,shops,guests,terrain to the world (Everything that is included in a park).
     * @param assetManager Load Ride Guest Shop models.
     * @param parkHandler Set lists of shops parks guests etc.
     * @param eventBus Send events.
     */
    @Inject
    public LoadManager(Node rootNode, AssetManager assetManager,ParkHandler parkHandler,EventBus eventBus) {
        this.rootNode = rootNode;
        this.assetManager = assetManager;
        this.parkHandler=parkHandler;
        this.eventBus=eventBus;
        roadF = new RoadFactory(assetManager);

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
    private void loadRideData( String string) {
        Node rideNode=(Node)rootNode.getChild("rideNode");
        ArrayList<BasicRide> asd = new ArrayList<BasicRide>();
        String worked = string;
        String[] values = worked.split(":");
        int quantity = Integer.parseInt(values[2]);
        int c = 3;
        for (int i = 0; i < quantity; i++) {
            String name = values[c];
            c += 1;
            String type = values[c];
            c += 1;
            float price = Float.parseFloat(values[c]);
            c += 1;
            float x = Float.parseFloat(values[c]);
            c += 1;
            float z = Float.parseFloat(values[c]);
            c += 1;
            float y = Float.parseFloat(values[c]);
            c += 1;
            int rideID = Integer.parseInt(values[c]);
            c += 1;
            int exitement = Integer.parseInt(values[c]);
            c += 1;
            int nausea = Integer.parseInt(values[c]);
            c += 1;
            int broken = Integer.parseInt(values[c]);
            c += 1;
            boolean status;
            if (values[c].equals("ON")) {
                status = true;
            } else {
                status = false;
            }
            c += 1;
            if (type.equals("chess")) {
                
                CustomAnimation moving=new CustomAnimation();
                ArrayList<Spatial>staticContent=new ArrayList<Spatial>();
                staticContent.add(assetManager.loadModel(LoadPaths.chess));
                
                
                ChessCenter a = new ChessCenter(new MapPosition(new Vector3f(x, y, z)),moving,staticContent,Direction.UP);
                a.setName(name);
                a.setRideID(rideID);
                a.setStats(broken, exitement, nausea, status);
                a.getGeometry().setUserData("rideID", rideID);
                a.getGeometry().setUserData("type", "ride");
                a.setAllSpatialsUserData("rideID",rideID);
                a.attachToNode(rideNode);
                asd.add(a);
            }
            if (type.equals("archery")) {
                
                CustomAnimation moving=new CustomAnimation();
                ArrayList<Spatial>staticContent=new ArrayList<Spatial>();
                staticContent.add(assetManager.loadModel(LoadPaths.archery));
                
                Archeryrange a = new Archeryrange(new MapPosition(new Vector3f(x, y, z)),moving,staticContent, Direction.UP);
                a.setName(name);
                a.setRideID(rideID);
                a.setStats(broken, exitement, nausea, status);
                a.getGeometry().setUserData("rideID", rideID);
                a.getGeometry().setUserData("type", "ride");
                a.setAllSpatialsUserData("rideID",rideID);
                a.attachToNode(rideNode);
                asd.add(a);
            }
            if (type.equals("blender")) {
                
                CustomAnimation moving=new CustomAnimation();
                ArrayList<Spatial>staticContent=new ArrayList<Spatial>();
                staticContent.add(assetManager.loadModel(LoadPaths.blender));
                
                Blender a = new Blender(new MapPosition(new Vector3f(x, y, z)),moving,staticContent, Direction.UP);
                a.setName(name);
                a.setRideID(rideID);
                a.setStats(broken, exitement, nausea, status);
                a.getGeometry().setUserData("rideID", rideID);
                a.getGeometry().setUserData("type", "ride");
                a.setAllSpatialsUserData("rideID",rideID);
                a.attachToNode(rideNode);
                asd.add(a);
            }
            if (type.equals("hhouse")) {
                
                CustomAnimation moving=new CustomAnimation();
                ArrayList<Spatial>staticContent=new ArrayList<Spatial>();
                staticContent.add(assetManager.loadModel(LoadPaths.hauntedhouse));
                
                HauntedHouse a = new HauntedHouse(new MapPosition(new Vector3f(x, y, z)),moving,staticContent,Direction.UP);
                a.setName(name);
                a.setRideID(rideID);
                a.setStats(broken, exitement, nausea, status);
                a.getGeometry().setUserData("rideID", rideID);
                a.getGeometry().setUserData("type", "ride");
                a.setAllSpatialsUserData("rideID",rideID);
                a.attachToNode(rideNode);
                asd.add(a);
            }
            if (type.equals("rotor")) {
                
                CustomAnimation moving=new CustomAnimation();
                ArrayList<Spatial>staticContent=new ArrayList<Spatial>();
                staticContent.add(assetManager.loadModel(LoadPaths.rotor));
                
                Rotor a = new Rotor(new MapPosition(new Vector3f(x, y, z)),moving,staticContent, Direction.UP);
                a.setName(name);
                a.setRideID(rideID);
                a.setStats(broken, exitement, nausea, status);
                a.getGeometry().setUserData("rideID", rideID);
                a.getGeometry().setUserData("type", "ride");
                a.setAllSpatialsUserData("rideID",rideID);
                a.attachToNode(rideNode);
                asd.add(a);
            }
            if (type.equals("pirateship")) {
                
                CustomAnimation moving=new CustomAnimation(assetManager.loadModel(LoadPaths.pirateSwing), AnimationType.ROLLH);
                ArrayList<Spatial>staticContent=new ArrayList<Spatial>();
                staticContent.add(assetManager.loadModel(LoadPaths.pirateCore));
                
                PirateShip a = new PirateShip(new MapPosition(new Vector3f(x, y, z)),moving,staticContent, Direction.UP);
                a.setName(name);
                a.setRideID(rideID);
                a.setStats(broken, exitement, nausea, status);
                a.getGeometry().setUserData("rideID", rideID);
                a.getGeometry().setUserData("type", "ride");
                a.setAllSpatialsUserData("rideID",rideID);
                a.attachToNode(rideNode);
                asd.add(a);
            }
            String result = values[c];
            String resulta = result.substring(1);
            String resultb = result.substring(0, 1);
            c++;
            if (resulta.equals("1")) {
                Float eX = Float.parseFloat(values[c]);
                c++;
                Float eZ = Float.parseFloat(values[c]);
                c++;
                Float eY = Float.parseFloat(values[c]);
                c++;
                Direction direction = null;
                if (values[c].equals("UP")) {
                    direction = Direction.UP;
                }
                if (values[c].equals("DOWN")) {
                    direction = Direction.DOWN;
                }
                if (values[c].equals("RIGHT")) {
                    direction = Direction.RIGHT;
                }
                if (values[c].equals("LEFT")) {
                    direction = Direction.LEFT;
                }
                c++;
                boolean connected;
                if (values[c].equals("TRUE")) {
                    connected = true;
                } else {
                    connected = false;
                }
                c++;
                //Enterance e = new Enterance(false, new MapPosition((int)eX, (int)eY,(int) eZ), direction);
                //e.connected = connected;
                //rideNode.attachChild(e.object);
                //e.connectedRide = asd.get(asd.size() - 1);
                //asd.get(asd.size() - 1).enterance = e;

            }
            if (resultb.equals("1")) {
                Float eX = Float.parseFloat(values[c]);
                c++;
                Float eZ = Float.parseFloat(values[c]);
                c++;
                Float eY = Float.parseFloat(values[c]);
                c++;
                Direction direction = null;
                if (values[c].equals("UP")) {
                    direction = Direction.UP;
                }
                if (values[c].equals("DOWN")) {
                    direction = Direction.DOWN;
                }
                if (values[c].equals("RIGHT")) {
                    direction = Direction.RIGHT;
                }
                if (values[c].equals("LEFT")) {
                    direction = Direction.LEFT;
                }
                c++;
                boolean connected;
                if (values[c].equals("TRUE")) {
                    connected = true;
                } else {
                    connected = false;
                }

                c++;
                //Enterance e = new Enterance(true, new Vector3f(eX, eY, eZ), direction, assetManager);
                //rideNode.attachChild(e.object);
                //e.connectedRide = asd.get(asd.size() - 1);
                //e.connected = connected;
                //asd.get(asd.size() - 1).exit = e;
            }



        }
        parkHandler.setRides(asd);
    }

    private void loadRoadData(String string) {
        
        String worked = string;
        String[] values = worked.split(":");
        int quantity = Integer.parseInt(values[2]);
        for (int i = 0; i < quantity; i++) {
            int a = i * 5+2;
            float x = Float.parseFloat(values[a + 1]);
            float z = Float.parseFloat(values[a + 2]);
            float y = Float.parseFloat(values[a + 3]);
            y +=0.1;
            String roadHill = values[a + 4];
            int id = Integer.parseInt(values[a + 5]);
            if (roadHill.equals("flat")) {
                Spatial road = roadF.roadStraight();
                road.move(new Vector3f(x, y, z));
                road.setUserData("roadID",id);
                int ax=(int) x;
                int ay=(int) y;
                int az=(int) z;
                //map.getMap()[ax][ay][az] = road;
                eventBus.post(new AddObjectToMapEvent(ax, ay, az, road));
                rootNode.attachChild(road);
            } else {
                //do the actual road TODO
            }

        }
        ArrayList <Vector3f> pos=new ArrayList<Vector3f>();
        for (int xi = 0; xi < parkHandler.getMapHeight(); xi++) {
            for (int zi = 0; zi < parkHandler.getMapWidth(); zi++) {
                for (int yi = 0; yi < 15; yi++) {
                    Spatial tested = parkHandler.getSpatialAt(xi, yi, zi);
                    if (tested != null) {
                        if (tested.getUserData("type").equals("road")) {
                            pos.add(new Vector3f(xi, yi, zi));
                        }
                    }
                }
            }
        }
        parkHandler.setUpdatedRoadsList(pos);

    }

    private void loadQueRoadData(String string) {
        ArrayList<Spatial> queRoadstoUpdate = new ArrayList<Spatial>();
        String worked = string;
        String[] values = worked.split(":");
        int quantity = Integer.parseInt(values[2]);
        for(int i=0;i<quantity;i++){
            int a=i*9+2;
            float x = Float.parseFloat(values[a + 1]);
            float z = Float.parseFloat(values[a + 2]);
            float y = Float.parseFloat(values[a + 3]);
            y +=0.1;
            String roadHill = values[a + 4];
            int roadID=Integer.parseInt(values[a+5]);
            boolean connected;
            if(values[a+6].equals("FALSE")){
                connected=false;
                
            }
            else{
                connected=true;
            }
            int queconnect1 = 0;
            boolean connec1;
            boolean connec2;
            if(!values[a+7].equals("null")){
                queconnect1=Integer.parseInt(values[a+7]);
                connec1=false;
            }else{
                connec1=true;
            }
            int queconnect2 = 0;
            if(!values[a+8].equals("null")){
                queconnect2=Integer.parseInt(values[a+8]);
                connec2=false;
            }else{
                connec2=true;
            }
            
            
            String direction=values[a+9];
            if(roadHill.equals("flat")){
                Spatial qr=roadF.queroadStraight();
                qr.setLocalTranslation(new Vector3f(x, y, z));
                qr.setUserData("roadID", roadID);
                if(connec1==false){
                    qr.setUserData("qc1o",queconnect1);
                }
                if(connec2==false){
                    qr.setUserData("qc2o",queconnect2);
                }
                
                qr.setUserData("connected",connected);
                qr.setUserData("direction",direction);
                rootNode.attachChild(qr);
                int ax=(int) x;
                int ay=(int) y;
                int az=(int) z;
                //map.getMap()[(int)x][(int)y][(int)z]=qr;
                eventBus.post(new AddObjectToMapEvent(ax, ay, az, qr));
                queRoadstoUpdate.add(qr);
            }
        }
        for(Spatial s:queRoadstoUpdate){
            if(s.getUserData("qc1o")!=null){
                int lookedID=s.getUserData("qc1o");
                for(Spatial o:queRoadstoUpdate){
                    int fID=o.getUserData("roadID");
                    if(fID==lookedID){
                        s.setUserData("queconnect1",o);
                    }
                }
            }
            if(s.getUserData("qc2o")!=null){
                int lookedID=s.getUserData("qc2o");
                for(Spatial o:queRoadstoUpdate){
                    int fID=o.getUserData("roadID");
                    if(fID==lookedID){
                        s.setUserData("queconnect2",o);
                    }
                }
            }
        }
        parkHandler.setUpdatedQueRoadsList(queRoadstoUpdate);
        //parkhandler.setShops(asd);
    }
    private void loadDecorationData(String string) {
        DecorationFactory dFactory=new DecorationFactory(assetManager);
        String worked = string;
        String[] values = worked.split(":");
        Node decorationNode=(Node)rootNode.getChild("decorationNode");
        int quantity = Integer.parseInt(values[2]);
        for(int i=0;i<quantity;i++){
            int a=i*5+2;
            float x=Float.parseFloat(values[a+1]);
            float y=Float.parseFloat(values[a+2]);
            float z=Float.parseFloat(values[a+3]);
            String type=values[a+4];
            String direction=values[a+5];
            if(type.equals("rock")){
                Spatial decoration=dFactory.getRock();
                decoration.setUserData("type","decoration");
                decoration.setLocalTranslation(x, y, z);
                Float angle;
                if(direction.equals("UP")){
                    angle = (float) Math.toRadians(90);
                    decoration.rotate(0, angle, 0);
                }
                if(direction.equals("DOWN")){
                    angle = (float) Math.toRadians(90);
                    decoration.rotate(0, angle, 0);
                }
                if(direction.equals("RIGHT")){
                    angle = (float) Math.toRadians(90);
                    decoration.rotate(0, angle, 0);
                }
                if(direction.equals("LEFT")){
                    angle = (float) Math.toRadians(90);
                    decoration.rotate(0, angle, 0);
                }
                decoration.setUserData("direction",direction);
                int xi=(int)x;
                int yi=(int)y;
                int zi=(int)z;
                //map.getMap()[xi][yi][zi]=decoration;
                eventBus.post(new AddObjectToMapEvent(xi, yi, zi, decoration));
                decorationNode.attachChild(decoration);
            }
        }
    }
}