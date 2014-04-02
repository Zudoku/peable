/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package intopark.inout;

import com.google.inject.Singleton;
import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import com.jme3.export.Savable;
import com.jme3.system.AppSettings;
import intopark.UtilityMethods;
import java.io.IOException;

/**
 *
 * @author arska
 */
@Singleton
public class Settings implements Savable {
    //APPSETTINGS
    private int framerateCap = 60;
    private int resolutionHeight = 900;
    private int resolutionWidth = 1480;
    private boolean vSync=false;
    private boolean fullScreen=false;
    private int depthBits=16;// 16 OR 32
    //GAMESETTINGS

    public Settings() {
    }

    public AppSettings getAppSettings() {
        AppSettings appSettings=new AppSettings(false);
        
        appSettings.setFrameRate(framerateCap);
        appSettings.setResolution(resolutionWidth, resolutionHeight);
        appSettings.setVSync(vSync);
        appSettings.setFullscreen(fullScreen);
        appSettings.setDepthBits(depthBits);
        appSettings.setTitle(UtilityMethods.programTitle);
        appSettings.setUseInput(true);
        appSettings.setRenderer("LWJGL-OpenGL2");
        return appSettings;
    }

    public void write(JmeExporter ex) throws IOException {
        OutputCapsule capsule = ex.getCapsule(this);
        capsule.write(framerateCap, "framerateCap", 60);
        capsule.write(resolutionHeight, "resolutionHeight", 900);
        capsule.write(resolutionWidth, "resolutionWidth", 1480);
        capsule.write(fullScreen,"fullScreen",false);
        capsule.write(vSync,"vSync",false);
        capsule.write(depthBits, "depthBits",16);
    }

    public void read(JmeImporter im) throws IOException {
        InputCapsule capsule = im.getCapsule(this);
        framerateCap = capsule.readInt("framerateCap", 60);
        resolutionHeight = capsule.readInt("resolutionHeight", 900);
        resolutionWidth = capsule.readInt("resolutionWidth", 1480);
        fullScreen=capsule.readBoolean("fullScreen", false);
        vSync=capsule.readBoolean("vSync", false);
        depthBits = capsule.readInt("depthBits", 16);

    }
}
