/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package intopark.inout;

import com.jme3.export.binary.BinaryExporter;
import com.jme3.export.binary.BinaryImporter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author arska
 */
public class SettingsLoader {
    //LOGGER
    private static final Logger logger = Logger.getLogger(SettingsLoader.class.getName());
    /**
     * Saves rendering settings to a binary file.
     * Settings will be saved to file named settings.conf.
     * @param settings Settings to save.
     */
    public void save(Settings settings) {
        BinaryExporter exporter = BinaryExporter.getInstance();
        //Get file
        Path path = Paths.get("settings.conf");
        File file =path.toFile();
        //If file doesn't exist, create it.
        if(!file.exists()){
            try {
                file.createNewFile();
                saveDefaultSettings(); //Magic (?)
            } catch (IOException ex) {
                logger.log(Level.WARNING,"Critical error while making settings file - Settings might be corrupt",ex.getStackTrace());
                file.delete();
            }
        }
        //Save current settings.
        try {
            exporter.save(settings, file);
            logger.log(Level.INFO,"Saved settings..");
        } catch (IOException ex) {
            logger.log(Level.WARNING,"Critical error while saving settings - Settings might be corrupt",ex.getStackTrace());
            ex.printStackTrace();
            file.delete();
        }
    }
    /**
     * Get saved rendering settings. If current settings don't exist, returns default settings.
     * @return Settings
     */
    public Settings load(){
        Settings loadedSettings=null;
        BinaryImporter importer=BinaryImporter.getInstance();
        //Get file
        Path path = Paths.get("settings.conf");
        File file =path.toFile();
        //If it doen't exist create default settings.
        if(!file.exists()){
            try {
                file.createNewFile();
                saveDefaultSettings();

            } catch (IOException ex) {
                logger.log(Level.WARNING,"Critical error while making settings file - Settings might be corrupt",ex.getStackTrace());
                file.delete();
            }
        }
        //load the settings.
        try {
           loadedSettings=(Settings)importer.load(file);
        } catch (IOException ex) {
            logger.log(Level.WARNING,"Critical error while loading settings - Settings might be corrupt: ",ex.getStackTrace());
            ex.printStackTrace();
            file.delete();
        }
        return loadedSettings;
    }
    /**
     * Saves default settings to be loaded. Gets executed whenever settings.conf doesn't exist (removed)
     */
    private void saveDefaultSettings(){
        save(new Settings());
    }
}
