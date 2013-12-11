/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.terrain.decoration;

import com.jme3.asset.AssetManager;
import com.jme3.scene.Spatial;

/**
 *
 * @author arska
 */
public class DecorationFactory {
    private final AssetManager assetManager;
    public DecorationFactory(AssetManager assetManager){
        this.assetManager=assetManager;
    }
    public Spatial getTestDecor(){
        Spatial object=assetManager.loadModel("Models/Roads/roadStraightblue.j3o");
        object.setUserData("decoration","test");
        return object;
    }
}
