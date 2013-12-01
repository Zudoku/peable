/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.system.AppSettings;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import mygame.npc.BasicNPC;
import mygame.npc.Guest;
import mygame.ride.BasicRide;
import mygame.shops.BasicShop;
import mygame.terrain.ParkHandler;
import mygame.terrain.ParkWallet;

/**
 *
 * @author arska
 */
public class LoadManager {
    private final Node rootNode;
    private final AppSettings settings;
    private final Main main;
    

    public LoadManager(Node rootNode, AppSettings settings,Main main) {
        this.rootNode=rootNode;
        this.settings=settings;
        this.main=main;
    }
    public void load(String filename) throws FileNotFoundException, IOException{
        BufferedReader br = new BufferedReader(new FileReader(filename+".IntoFile"));
    try {
        StringBuilder sb = new StringBuilder();
        String line = br.readLine();
        createParkHandler(line);
        
        

    } finally {
        br.close();
    }
    }
    
    private ParkHandler createParkHandler(String line){
        ParkHandler parkhandler=Main.currentPark;
        String lines[]=splitStrings(line);
        //park data 
        loadParkData(parkhandler, lines[0]);
        loadTerrainData(parkhandler,lines[1]);
        loadShopData(parkhandler,lines[2]);
        loadGuestData(parkhandler,lines[3]);
        loadRideData(parkhandler,lines[4]);
        loadRoadData(parkhandler,lines[5]);
        loadQueRoadData(parkhandler,lines[6]);
     
        return null;
    }
    private void loadParkData(ParkHandler parkHandler,String line){
        String worked=line;
        String[]parkinfo=worked.split(":");
        String parkname=parkinfo[1];
        String money=parkinfo[2];
        String loan=parkinfo[3];
        String shopID=parkinfo[4];
        String rideID=parkinfo[5];
        String mapHeight=parkinfo[6];
        String mapWidth=parkinfo[7];
        
        ParkWallet wallet=new ParkWallet(Float.parseFloat(money));
        wallet.setLoan(Float.parseFloat(loan));
        
        parkHandler.setUp(parkname,Integer.parseInt(rideID),Integer.parseInt(shopID),wallet);
        parkHandler.setMapSize(Integer.parseInt(mapHeight), Integer.parseInt(mapWidth));
    }
    private String[] splitStrings(String line){
        String[] working=line.split("map size:");
        String workingString;
        String[] lines=new String[7];
        //park info
        lines[0]=working[0];
        
        workingString=working[1];
        working=workingString.split("shops size:");
        //terrain info
        lines[1]=working[0];
        
        workingString=working[1];
        working=workingString.split("guest size:");
        //shop info
        lines[2]=working[0];
        
        workingString=working[1];
        working=workingString.split("ride size:");
        //guest info
        lines[3]=working[0];
        
        workingString=working[1];
        working=workingString.split("roads size:");
        //ride info
        lines[4]=working[0];
        
        workingString=working[1];
        working=workingString.split("queroad size:");
        //roads info
        lines[5]=working[0];
        //queroads info
        lines[6]=working[1];
        return lines;
    }

    private void loadTerrainData(ParkHandler parkhandler, String string) {
        String worked=string;
        String[]values=worked.split(":");
        int mapHeight=Integer.parseInt(values[0]);
        int mapWidth=Integer.parseInt(values[1]);
        mapHeight -=1;
        mapWidth -=1;
        int [][]mapData=new int[mapHeight][mapWidth];
        for(int i=0;i<mapHeight;i++){
            for(int o=0;o<mapWidth;o++){
                mapData[i][o]=Integer.parseInt(values[mapHeight*mapWidth+mapWidth+2]);
            }
        }
        Spatial[][][]map=new Spatial[mapHeight][25][mapWidth];
        for (int x = 0; x < mapHeight-1; x++) {
            for (int y = 0; y < mapWidth-1; y++) {

                Geometry geomclone =main.TerrainBox();
                geomclone.setLocalScale((new Vector3f(1, (int) mapData[x][y], 1)));

                geomclone.setLocalTranslation(1, geomclone.getLocalTranslation().y + ((float) mapData[x][y] / 2), 1);
                geomclone.move(x, 0, y);
                geomclone.setName("Terrain");
                geomclone.setUserData("type","terrain");

                map[x][0][y] = geomclone;
                rootNode.attachChild(geomclone);

            }

        }
        
        parkhandler.setMap(map, mapData);
    }

    private void loadShopData(ParkHandler parkhandler, String string) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private void loadGuestData(ParkHandler parkhandler, String string) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private void loadRideData(ParkHandler parkhandler, String string) {
        
    }

    private void loadRoadData(ParkHandler parkhandler, String string) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private void loadQueRoadData(ParkHandler parkhandler, String string) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
    
}
