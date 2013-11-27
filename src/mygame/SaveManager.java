/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import mygame.npc.Guest;
import mygame.terrain.ParkHandler;

/**
 *
 * @author arska
 */
public class SaveManager {

    public void Save(String filename, ParkHandler parkHandler) {
        Writer writer = null;

        try {
            writer = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(filename + ".IntoFile"), "utf-8"));
            ArrayList<Guest> guests = parkHandler.getGuests();
            writeParkData(writer, parkHandler);
            writeTerrainData(writer, parkHandler);
            writeGuests(guests, writer);
        } catch (IOException ex) {
            // report
        } finally {
            try {
                writer.close();
            } catch (Exception ex) {
            }
        }
    }

    private void writeGuests(ArrayList<Guest> guests, Writer writer) throws IOException {
        int size = guests.size();
        writer.write("Guest size " + Integer.toString(size) + ":");
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
            writer.write(name + ":" + money + ":" + moving + ":" + x + ":" + z + ":" + y + ":" + hunger + ":" + thrist + ":" + happyness + ":" + preferredRide + ":");

        }

    }

    private void writeParkData(Writer writer,ParkHandler parkhandler) throws IOException {
        String parkname=parkhandler.getParkName();
        String parkmoney=Float.toString(parkhandler.getParkWallet().getMoney());
        String parkloan=Float.toString(parkhandler.getParkWallet().getLoan());
        String rideID=Integer.toString(parkhandler.getRideID());
        String shopID=Integer.toString(parkhandler.getShopID());
        String mapheight=Integer.toString(parkhandler.getMapHeight());
        String mapwidth=Integer.toString(parkhandler.getMapWidth());
        writer.write("park:"+parkname+":"+parkmoney+":"+parkloan+":"+rideID+":"+shopID+":"+mapheight+":"+mapwidth+":");
    }
    private void writeTerrainData(Writer writer,ParkHandler parkhandler) throws IOException {
        int[][] mapdata=parkhandler.getMapData();
        int height=parkhandler.getMapHeight();
        int width=parkhandler.getMapWidth();
        writer.write("map size:"+Integer.toString(height)+":"+Integer.toString(width)+":");
        for(int o=0;o<height;o++){
            for(int u=0;u<width;u++){
                int arvo=mapdata[o][u];
                writer.write(Integer.toString(arvo)+":");
            }
        }
    }
}
