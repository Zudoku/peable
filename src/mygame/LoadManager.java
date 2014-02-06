/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.jme3.system.AppSettings;
import com.jme3.texture.Texture;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Logger;
import mygame.npc.BasicNPC;
import mygame.npc.Guest;
import mygame.npc.inventory.RideType;
import mygame.npc.inventory.StatManager;
import mygame.npc.inventory.Wallet;
import mygame.ride.AnimationType;
import mygame.ride.BasicRide;
import mygame.ride.CustomAnimation;
import mygame.ride.Enterance;
import mygame.ride.actualrides.Archeryrange;
import mygame.ride.actualrides.Blender;
import mygame.ride.actualrides.ChessCenter;
import mygame.ride.actualrides.HauntedHouse;
import mygame.ride.actualrides.PirateShip;
import mygame.ride.actualrides.Rotor;
import mygame.shops.BasicShop;
import mygame.shops.actualshops.Energy;
import mygame.shops.actualshops.Meatballshop;
import mygame.shops.actualshops.Toilet;
import mygame.terrain.AddObjectToMapEvent;
import mygame.terrain.Direction;
import mygame.terrain.MapContainer;
import mygame.terrain.MapPosition;
import mygame.terrain.ParkHandler;
import mygame.terrain.ParkWallet;
import mygame.terrain.RoadFactory;
import mygame.terrain.RoadMaker;
import mygame.terrain.decoration.DecorationFactory;

/**
 *
 * @author arska
 */
@Singleton
public class LoadManager {
    private static final Logger logger = Logger.getLogger(LoadManager.class.getName());
    private final Node rootNode;
    private final AppSettings settings;
    private EventBus eventBus;
    private final AssetManager assetManager;
    RoadFactory roadF;
    private final RoadMaker roadMaker;
    private final ParkHandler parkHandler;
    private final MapContainer map;

    @Inject
    public LoadManager(Node rootNode, AppSettings settings, AssetManager assetManager,RoadMaker roadMaker,ParkHandler parkHandler,MapContainer map,EventBus eventBus) {
        this.rootNode = rootNode;
        this.settings = settings;
        this.assetManager = assetManager;
        this.roadMaker=roadMaker;
        this.parkHandler=parkHandler;
        this.map=map;
        this.eventBus=eventBus;
        roadF = new RoadFactory(assetManager);

    }

    public void load(String filename) throws FileNotFoundException, IOException {
        BufferedReader br = new BufferedReader(new FileReader(filename + ".IntoFile"));
        try {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();
            createParkHandler(line);



        } finally {
            br.close();
        }
    }

    private ParkHandler createParkHandler(String line) {
        logger.finest("Starting to build map!");
        String lines[] = splitStrings(line);
        loadParkData(parkHandler, lines[0]);
        loadTerrainData(parkHandler, lines[1]);
        loadShopData(parkHandler, lines[2]);
        loadGuestData(lines[3]);
        loadRideData(parkHandler, lines[4]);
        loadRoadData(parkHandler, lines[5]);
        loadQueRoadData(parkHandler,lines[6]);
        loadDecorationData(lines[7]);
        logger.finest("Map Done!");
        return null;
    }

    private void loadParkData(ParkHandler parkHandler, String line) {
        String worked = line;
        String[] parkinfo = worked.split(":");
        String parkname = parkinfo[1];
        String money = parkinfo[2];
        String loan = parkinfo[3];
        String shopID = parkinfo[4];
        String rideID = parkinfo[5];
        String mapHeight = parkinfo[6];
        String mapWidth = parkinfo[7];
        String maxGuests= parkinfo[8];

        ParkWallet wallet = new ParkWallet(Float.parseFloat(money));
        wallet.setLoan(Float.parseFloat(loan));

        parkHandler.setUp(parkname, Integer.parseInt(rideID), Integer.parseInt(shopID), wallet,Integer.parseInt(maxGuests));
        parkHandler.setMapSize(Integer.parseInt(mapHeight), Integer.parseInt(mapWidth));
    }

    private String[] splitStrings(String line) {
        String[] working = line.split("map size:");
        String workingString;
        String[] lines = new String[8];
        //park info
        lines[0] = working[0];

        workingString = working[1];
        working = workingString.split("shops size:");
        //terrain info
        lines[1] = working[0];

        workingString = working[1];
        working = workingString.split("guest size:");
        //shop info
        lines[2] = working[0];

        workingString = working[1];
        working = workingString.split("ride size:");
        //guest info
        lines[3] = working[0];

        workingString = working[1];
        working = workingString.split("roads size:");
        //ride info
        lines[4] = working[0];

        workingString = working[1];
        working = workingString.split("queroad size:");
        //roads info
        lines[5] = working[0];
        workingString = working[1];
        working = workingString.split("decoration size:");
        
        //queroads info
        lines[6] = working[0];
        //decorations
        lines[7] = working[1];
        
        return lines;
    }

    private void loadTerrainData(ParkHandler parkhandler, String string) {
        String worked = string;
        String[] values = worked.split(":");
        int mapHeight = Integer.parseInt(values[0]);
        int mapWidth = Integer.parseInt(values[1]);
        //mapHeight -=1;
        //mapWidth -=1;
        int[][] mapData = new int[mapHeight][mapWidth];
        for (int i = 0; i < mapHeight; i++) {
            for (int o = 0; o < mapWidth; o++) {
                if (i * o + o + 2 >= 10304) {
                    break;
                }
                //mapData[i][o]=Integer.parseInt(values[i*o+o+2]);
                mapData[i][o] = 6;
                
            }
        }
        Spatial[][][] map = new Spatial[mapHeight][25][mapWidth];
        for (int x = 0; x < mapHeight - 1; x++) {
            for (int y = 0; y < mapWidth - 1; y++) {

                Geometry geomclone = TerrainBox();


                geomclone.setLocalScale((new Vector3f(1, (int) mapData[x][y], 1)));

                geomclone.setLocalTranslation(1, geomclone.getLocalTranslation().y + ((float) mapData[x][y] / 2), 1);
                
                geomclone.move(x, 0, y);
                geomclone.setName("Terrain");
                geomclone.setUserData("type", "terrain");

                map[x][0][y] = geomclone;
                rootNode.attachChild(geomclone);

            }

        }

        parkhandler.setMap(map, mapData);
    }

    private void loadShopData(ParkHandler parkhandler, String string) {
        Node shopNode=(Node)rootNode.getChild("shopNode");
        ArrayList<BasicShop> shops = new ArrayList<BasicShop>();
        String worked = string;
        String[] values = worked.split(":");
        int quantity = Integer.parseInt(values[0]);
        for(int i=0;i<quantity;i++){
            int a=i*9;
            String name = values[a+1];
            float x = Float.parseFloat(values[a+2]);
            float z = Float.parseFloat(values[a+3]);
            float y = Float.parseFloat(values[a+4]);
            Float price = Float.parseFloat(values[a+5]);
            String type = values[a+6];
            String productname = values[a+7];
            int shopID =Integer.parseInt(values[a+8]);
            Direction direction = null;
            if(values[a+9].equals("UP")){
                direction= Direction.UP;
            }
            if(values[a+9].equals("DOWN")){
                direction= Direction.DOWN;
            }
            if(values[a+9].equals("RIGHT")){
                direction= Direction.RIGHT;
            }
            if(values[a+9].equals("LEFT")){
                direction= Direction.LEFT;
            }
 
            if(type.equals("energyshop")){
                Spatial geom=assetManager.loadModel("Models/shops/energyshop.j3o");
                Energy e=new Energy(new Vector3f(x, y, z),geom , direction,rootNode);
                e.getGeometry().setUserData("type","shop");
                e.getGeometry().setUserData("shopID",shopID);
                e.shopName=name;
                e.price=price;
                e.productname=productname;
                e.shopID=shopID;
                shops.add(e);
                shopNode.attachChild(e.getGeometry());
                int ax=(int) x;
                int ay=(int) y;
                int az=(int) z;
                //map.getMap()[ax][ay][az]=e.getGeometry();
                eventBus.post(new AddObjectToMapEvent(ax, ay, az, e.getGeometry()));
            }
            if(type.equals("meatballshop")){
                Spatial geom=assetManager.loadModel("Models/shops/mball.j3o");
                Meatballshop e=new Meatballshop(new Vector3f(x, y, z),geom , direction,rootNode);
                e.getGeometry().setUserData("type","shop");
                e.getGeometry().setUserData("shopID",shopID);
                e.shopName=name;
                e.price=price;
                e.productname=productname;
                e.shopID=shopID;
                shops.add(e);
                shopNode.attachChild(e.getGeometry());
                int ax=(int) x;
                int ay=(int) y;
                int az=(int) z;
                //map.getMap()[ax][ay][az]=e.getGeometry();
                eventBus.post(new AddObjectToMapEvent(ax, ay, az, e.getGeometry()));
            }
            if(type.equals("toilet")){
                Spatial geom=assetManager.loadModel("Models/shops/toilet.j3o");
                Toilet e=new Toilet(new Vector3f(x, y, z),geom , direction,rootNode);
                e.getGeometry().setUserData("type","shop");
                e.getGeometry().setUserData("shopID",shopID);
                e.shopName=name;
                e.price=price;
                e.productname=productname;
                e.shopID=shopID;
                shops.add(e);
                shopNode.attachChild(e.getGeometry());
                int ax=(int) x;
                int ay=(int) y;
                int az=(int) z;
                //map.getMap()[ax][ay][az]=e.getGeometry();
                eventBus.post(new AddObjectToMapEvent(ax, ay, az, e.getGeometry()));
            }
            
        }
        parkhandler.setShops(shops);
    }

    private void loadGuestData(String string) {
        ArrayList<Guest> guests = new ArrayList<Guest>();
        ArrayList<BasicNPC>npcs=new ArrayList<BasicNPC>();
        String worked = string;
        String[] values = worked.split(":");
        int quantity = Integer.parseInt(values[0]);
        for (int i = 0; i < quantity; i++) {
            int a = i * 11;
            String name = values[a + 1];
            float money = Float.parseFloat(values[a + 2]);
            Direction direction = null;
            if (values[a + 3].equals("UP")) {
                direction = Direction.UP;
            }
            if (values[a + 3].equals("DOWN")) {
                direction = Direction.DOWN;
            }
            if (values[a + 3].equals("RIGHT")) {
                direction = Direction.RIGHT;
            }
            if (values[a + 3].equals("LEFT")) {
                direction = Direction.LEFT;
            }
            int x = Integer.parseInt(values[a + 4]);
            int z = Integer.parseInt(values[a + 5]);
            int y = Integer.parseInt(values[a + 6]);

            int hunger = Integer.parseInt(values[a + 7]);
            int thrist = Integer.parseInt(values[a + 8]);
            int happyness = Integer.parseInt(values[a + 9]);
            RideType preference = null;
            if (values[a + 10].equals("LOW")) {
                preference = RideType.LOW;
            }
            if (values[a + 10].equals("MEDIUM")) {
                preference = RideType.MEDIUM;
            }
            if (values[a + 10].equals("HIGH")) {
                preference = RideType.HIGH;
            }
            if (values[a + 10].equals("CRAZY")) {
                preference = RideType.CRAZY;
            }
            if (values[a + 10].equals("NAUSEA")) {
                preference = RideType.NAUSEA;
            }
            int guestnum = Integer.parseInt(values[a + 11]);
            StatManager stats = new StatManager();
            stats.happyness = happyness;
            stats.hunger = hunger;
            stats.thirst = thrist;
            stats.preferredRide = preference;
            Spatial geom = assetManager.loadModel("Models/Human/guest.j3o");

            Guest g = new Guest(new Wallet(money), guestnum, direction, x, y, z, stats, geom, name);
            rootNode.attachChild(g.getGeometry());
            guests.add(g);
            npcs.add(g);



        }
        parkHandler.setGuests(guests);
        parkHandler.setNpcs(npcs);
    }

    private void loadRideData(ParkHandler parkhandler, String string) {
        Node rideNode=(Node)rootNode.getChild("rideNode");
        ArrayList<BasicRide> asd = new ArrayList<BasicRide>();
        String worked = string;
        String[] values = worked.split(":");
        int quantity = Integer.parseInt(values[0]);
        int c = 1;
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
                staticContent.add(assetManager.loadModel("Models/Rides/Chesshouse/chesshouse.j3o"));
                
                
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
                staticContent.add(assetManager.loadModel("Models/Rides/archeryrange/archeryrange.j3o"));
                
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
                staticContent.add(assetManager.loadModel("Models/Rides/Blender/blender.j3o"));
                
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
                staticContent.add(assetManager.loadModel("Models/Rides/Hauntedhouse/hauntedhouse.j3o"));
                
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
                staticContent.add(assetManager.loadModel("Models/Rides/Rotor/rotor.j3o"));
                
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
                
                CustomAnimation moving=new CustomAnimation(assetManager.loadModel("Models/Rides/PirateShip/swing.j3o"), AnimationType.ROLLH);
                ArrayList<Spatial>staticContent=new ArrayList<Spatial>();
                staticContent.add(assetManager.loadModel("Models/Rides/PirateShip/core.j3o"));
                
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
                Enterance e = new Enterance(false, new Vector3f(eX, eY, eZ), direction, assetManager);
                e.connected = connected;
                rideNode.attachChild(e.object);
                e.connectedRide = asd.get(asd.size() - 1);
                asd.get(asd.size() - 1).enterance = e;

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
                Enterance e = new Enterance(true, new Vector3f(eX, eY, eZ), direction, assetManager);
                rideNode.attachChild(e.object);
                e.connectedRide = asd.get(asd.size() - 1);
                e.connected = connected;
                asd.get(asd.size() - 1).exit = e;
            }



        }
        parkhandler.setRides(asd);
    }

    private void loadRoadData(ParkHandler parkhandler, String string) {
        
        String worked = string;
        String[] values = worked.split(":");
        int quantity = Integer.parseInt(values[0]);
        for (int i = 0; i < quantity; i++) {
            int a = i * 5;
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
                    Spatial tested = map.getMap()[xi][yi][zi];
                    if (tested != null) {
                        if (tested.getUserData("type").equals("road")) {
                            pos.add(new Vector3f(xi, yi, zi));
                        }
                    }
                }
            }
        }
        parkhandler.setUpdatedRoadsList(pos);

    }

    private void loadQueRoadData(ParkHandler parkhandler, String string) {
        ArrayList<Spatial> queRoadstoUpdate = new ArrayList<Spatial>();
        String worked = string;
        String[] values = worked.split(":");
        int quantity = Integer.parseInt(values[0]);
        for(int i=0;i<quantity;i++){
            int a=i*9;
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
        parkhandler.setUpdatedQueRoadsList(queRoadstoUpdate);
        //parkhandler.setShops(asd);
    }

    private Geometry TerrainBox() {

        Box b = new Box(Vector3f.ZERO, 0.5f, 0.5f, 0.5f);
        Geometry geom = new Geometry("Terrain", b);
        Texture grass = assetManager.loadTexture(
                "Textures/grasstexture.png");
        Material mat = new Material(assetManager,
                "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setTexture("ColorMap", grass);
        geom.setMaterial(mat);
        return geom;
    }

    private void loadDecorationData( String string) {
        DecorationFactory dFactory=new DecorationFactory(assetManager);
        String worked = string;
        String[] values = worked.split(":");
        Node decorationNode=(Node)rootNode.getChild("decorationNode");
        int quantity = Integer.parseInt(values[0]);
        for(int i=0;i<quantity;i++){
            int a=i*5;
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
