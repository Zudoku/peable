/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package intopark.inout;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import intopark.terrain.ParkHandler;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 *
 * @author arska
 */
@Singleton
public class SaveManager {
    private static final Logger logger = Logger.getLogger(SaveManager.class.getName());
    private final ParkHandler parkHandler;
    /**
     * Class that will save current scenario to a file using Gson JSON-serializer.
     * @param parkHandler the scenario to save.
     */
    @Inject
    public SaveManager(ParkHandler parkHandler){
        this.parkHandler=parkHandler;
    }
    /**
     * Saves current scenario to a JSON-file. The filepath will be Saves/<FILENAME>.IntoFile
     * @param filename the filename for the file.
     */
    public void Save(String filename) {
        // Make Gson JSON serializer.
        Gson gson;
        GsonBuilder ga=new GsonBuilder();
        gson=ga.setPrettyPrinting().create();

        BufferedWriter writer = null;
        logger.log(Level.FINEST,"Starting to save {0}",filename);
        //Pre save function needed to save certain elements in scenario.
        parkHandler.initializeSaving();
        try {
            //Open file
            writer = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream("Saves/"+filename + ".IntoFile"), "utf-8"));
            //Write to file
            gson.toJson(parkHandler, writer);
            //Close file
            writer.close();
        } catch (UnsupportedEncodingException ex) {
            logger.log(Level.SEVERE,"Unsupported encoding! {0}",ex);
        } catch (FileNotFoundException ex) {
            logger.log(Level.SEVERE,"File not found! {0}",ex);
        } catch (IOException ex) {
            logger.log(Level.SEVERE,"IO Exeption! {0}",ex);
        }
        parkHandler.postSave();
        logger.log(Level.FINEST,"Saving finished!");
    }
}
