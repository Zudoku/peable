/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

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
     * 
     * @param settings 
     */
    public void save(Settings settings) {
        BinaryExporter exporter = BinaryExporter.getInstance();
        Path path = Paths.get("settings.conf");
        File file =path.toFile();
        if(!file.exists()){
            try {
                file.createNewFile();
                initSave();
            } catch (IOException ex) {
                logger.log(Level.WARNING,"Critical error while making settings file - Settings might be corrupt",ex.getStackTrace());
                file.delete();
            }
        }
        try {
            exporter.save(settings, file);
            logger.log(Level.INFO,"Saved settings..");
        } catch (IOException ex) {
            logger.log(Level.WARNING,"Critical error while saving settings - Settings might be corrupt",ex.getStackTrace());
            ex.printStackTrace();
            file.delete();
        }
    }
    public Settings load(){
        Settings loadedSettings=null;
        BinaryImporter importer=BinaryImporter.getInstance();
        Path path = Paths.get("settings.conf");
        File file =path.toFile();
        if(!file.exists()){
            try {
                file.createNewFile();
                initSave();
                
            } catch (IOException ex) {
                logger.log(Level.WARNING,"Critical error while making settings file - Settings might be corrupt",ex.getStackTrace());
                file.delete();
            }
        }
        try {
           loadedSettings=(Settings)importer.load(file);
        } catch (IOException ex) {
            logger.log(Level.WARNING,"Critical error while loading settings - Settings might be corrupt: ",ex.getStackTrace());
            ex.printStackTrace();
            file.delete();
        }
        return loadedSettings;
    }
    private void initSave(){
        save(new Settings());
    }
}
