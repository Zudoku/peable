/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package intopark.terrain.decoration;

import com.jme3.asset.AssetManager;
import com.jme3.scene.Spatial;
import intopark.LoadPaths;

/**
 *
 * @author arska
 */
public class DecorationFactory {
    private final AssetManager assetManager;
    public DecorationFactory(AssetManager assetManager){
        this.assetManager=assetManager;
    }
    
    public Spatial getRock() {
        Spatial object=assetManager.loadModel(LoadPaths.rock);
        object.setUserData("decoration","rock");
        object.scale(0.3f);
        return object;
    }
}
