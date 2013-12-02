/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

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
import mygame.npc.BasicNPC;
import mygame.npc.Guest;
import mygame.npc.inventory.PreferredRides;
import mygame.npc.inventory.StatManager;
import mygame.npc.inventory.Wallet;
import mygame.ride.BasicRide;
import mygame.shops.BasicShop;
import mygame.terrain.Direction;
import mygame.terrain.ParkHandler;
import mygame.terrain.ParkWallet;
import mygame.terrain.Road;

/**
 *
 * @author arska
 */
public class LoadManager {

    private final Node rootNode;
    private final AppSettings settings;
    private final AssetManager assetManager;
    

    public LoadManager(Node rootNode, AppSettings settings,AssetManager assetManager) {
        this.rootNode = rootNode;
        this.settings = settings;
        this.assetManager=assetManager;
        
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
        ParkHandler parkhandler = Main.currentPark;
        String lines[] = splitStrings(line);
        //park data 
        loadParkData(parkhandler, lines[0]);
        loadTerrainData(parkhandler, lines[1]);
        loadShopData(parkhandler, lines[2]);
        loadGuestData(parkhandler, lines[3]);
        //loadRideData(parkhandler,lines[4]);
        //loadRoadData(parkhandler,lines[5]);
        //loadQueRoadData(parkhandler,lines[6]);

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

        ParkWallet wallet = new ParkWallet(Float.parseFloat(money));
        wallet.setLoan(Float.parseFloat(loan));

        parkHandler.setUp(parkname, Integer.parseInt(rideID), Integer.parseInt(shopID), wallet);
        parkHandler.setMapSize(Integer.parseInt(mapHeight), Integer.parseInt(mapWidth));
    }

    private String[] splitStrings(String line) {
        String[] working = line.split("map size:");
        String workingString;
        String[] lines = new String[7];
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
        //queroads info
        lines[6] = working[1];
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
                System.out.println(values[i * o + o + 2]);
            }
        }
        Spatial[][][] map = new Spatial[mapHeight][25][mapWidth];
        for (int x = 0; x < mapHeight - 1; x++) {
            for (int y = 0; y < mapWidth - 1; y++) {

                Geometry geomclone = TerrainBox();


                geomclone.setLocalScale((new Vector3f(1, (int) mapData[x][y], 1)));

                geomclone.setLocalTranslation(1, geomclone.getLocalTranslation().y + ((float) mapData[x][y] / 2), 1);
                System.out.println(geomclone.getLocalScale());
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
        ArrayList<BasicShop> asd = new ArrayList<BasicShop>();
        parkhandler.setShops(asd);
    }

    private void loadGuestData(ParkHandler parkhandler, String string) {
        ArrayList<Guest> guests = new ArrayList<Guest>();
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
            PreferredRides preference = null;
            if (values[a + 10].equals("LOW")) {
                preference = PreferredRides.LOW;
            }
            if (values[a + 10].equals("MEDIUM")) {
                preference = PreferredRides.MEDIUM;
            }
            if (values[a + 10].equals("HIGH")) {
                preference = PreferredRides.HIGH;
            }
            if (values[a + 10].equals("CRAZY")) {
                preference = PreferredRides.CRAZY;
            }
            if (values[a + 10].equals("NAUSEA")) {
                preference = PreferredRides.NAUSEA;
            }
            int guestnum = Integer.parseInt(values[a + 11]);
            StatManager stats = new StatManager();
            stats.happyness = happyness;
            stats.hunger = hunger;
            stats.thirst = thrist;
            stats.preferredRide = preference;
            Spatial geom = assetManager.loadModel("Models/Human/guest.j3o");
            
            Guest g = new Guest(new Wallet(money), guestnum, direction, x, y, z, stats,geom , name);
            guests.add(g);



        }
        Main.currentPark.setGuests(guests);
        Main.currentPark.setNpcs(new ArrayList<BasicNPC>());
    }

    private void loadRideData(ParkHandler parkhandler, String string) {
        ArrayList<BasicRide> asd = new ArrayList<BasicRide>();
        parkhandler.setRides(asd);
    }

    private void loadRoadData(ParkHandler parkhandler, String string) {
        ArrayList<Road> asd = new ArrayList<Road>();
        //parkhandler.set(asd);
    }

    private void loadQueRoadData(ParkHandler parkhandler, String string) {
        ArrayList<BasicShop> asd = new ArrayList<BasicShop>();
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
}
