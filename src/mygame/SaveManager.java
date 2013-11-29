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
import mygame.ride.BasicRide;
import mygame.shops.BasicShop;
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
            writeShopData(writer, parkHandler);
            writeGuests(guests, writer);
            writeRideData(writer, parkHandler);
        } catch (IOException ex) {
            // report
        } finally {
            try {
                writer.close();
                System.out.println("Game saved!");
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

    private void writeParkData(Writer writer, ParkHandler parkhandler) throws IOException {
        String parkname = parkhandler.getParkName();
        String parkmoney = Float.toString(parkhandler.getParkWallet().getMoney());
        String parkloan = Float.toString(parkhandler.getParkWallet().getLoan());
        String rideID = Integer.toString(parkhandler.getRideID());
        String shopID = Integer.toString(parkhandler.getShopID());
        String mapheight = Integer.toString(parkhandler.getMapHeight());
        String mapwidth = Integer.toString(parkhandler.getMapWidth());
        writer.write("park:" + parkname + ":" + parkmoney + ":" + parkloan + ":" + rideID + ":" + shopID + ":" + mapheight + ":" + mapwidth + ":");
    }

    private void writeTerrainData(Writer writer, ParkHandler parkhandler) throws IOException {
        int[][] mapdata = parkhandler.getMapData();
        int height = parkhandler.getMapHeight();
        int width = parkhandler.getMapWidth();
        writer.write("map size:" + Integer.toString(height) + ":" + Integer.toString(width) + ":");
        for (int o = 0; o < height; o++) {
            for (int u = 0; u < width; u++) {
                int arvo = mapdata[o][u];
                writer.write(Integer.toString(arvo) + ":");
            }
        }
    }

    private void writeShopData(Writer writer, ParkHandler parkhandler) throws IOException {
        String quantity = Integer.toString(parkhandler.getShops().size());
        writer.write("shops size:" + quantity + ":");
        for (BasicShop b : parkhandler.getShops()) {
            String name = b.shopName;
            String x = Float.toString(b.position.x);
            String z = Float.toString(b.position.z);
            String y = Float.toString(b.position.y);
            String price = Float.toString(b.price);
            String type = b.type;
            String productname = b.productname;
            String shopID = Integer.toString(b.shopID);
            String direction;
            switch (b.facing) {
                case UP:
                    direction = "UP";
                    break;

                case DOWN:
                    direction = "DOWN";
                    break;

                case RIGHT:
                    direction = "RIGHT";
                    break;

                case LEFT:
                    direction = "LEFT";
            }
            writer.write(name + ":" + x + ":" + z + ":" + y + ":" + price + ":" + type + ":" + productname + ":" + shopID + ":");
        }

    }

    private void writeRideData(Writer writer, ParkHandler parkhandler) throws IOException {
        String quantity = Integer.toString(parkhandler.getRides().size());
        writer.write("ride size:" + quantity + ":");
        for (BasicRide r : parkhandler.getRides()) {
            String name = r.getName();
            String type = r.getRide();
            String price = Float.toString(r.getPrice());
            String x = Float.toString(r.getPosition().x);
            String z = Float.toString(r.getPosition().z);
            String y = Float.toString(r.getPosition().y);
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
    }
    
    
    private void writeEnteranceData(BasicRide r,Writer writer) throws IOException{
        String enteranceX = Integer.toString((int) r.enterance.location.x);
                String enteranceZ = Integer.toString((int) r.enterance.location.z);
                String enteranceY = Integer.toString((int) r.enterance.location.y);
                String exit;
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
                writer.write(enteranceX + ":" + enteranceZ + ":" + enteranceY + ":" + enterancedirection + ":"+connected+":");
    }
    private void writeExitData(BasicRide r,Writer writer) throws IOException{
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
                writer.write(exitX + ":" + exitZ + ":" + exitY + ":" + exitdirection + ":"+connected+":");
    }
}
