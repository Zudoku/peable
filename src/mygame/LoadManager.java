/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.scene.Node;
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

    public LoadManager(Node rootNode, AppSettings settings) {
        this.rootNode=rootNode;
        this.settings=settings;
    }
    public void load(String filename) throws FileNotFoundException, IOException{
        BufferedReader br = new BufferedReader(new FileReader(filename+".IntoFile"));
    try {
        StringBuilder sb = new StringBuilder();
        String line = br.readLine();

        

    } finally {
        br.close();
    }
    }
    
    private ParkHandler createParkHandler(){
        ParkHandler parkhandler=Main.currentPark;
        
        return null;
    }
}
