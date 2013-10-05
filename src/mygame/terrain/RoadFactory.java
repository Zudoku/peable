/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.terrain;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.scene.Spatial;

/**
 *
 * @author arska
 */
public class RoadFactory {
    private final AssetManager assetManager;
    
    public RoadFactory(AssetManager assetManager){
        this.assetManager=assetManager;
    }
    public Spatial roadStraight() {
        Spatial road = assetManager.loadModel("Models/Roads/roadStraight.j3o");
        road.scale(0.5f, 0.5f, 0.5f);
        return road;
    }

    public Spatial roadUpHill() {
        Spatial road = assetManager.loadModel("Models/Roads/roadUpHill.j3o");
        road.setLocalTranslation(0, +0.50f, 0);
        road.setName("upHill");
        return road;
    }

    public Spatial roadDownHill() {
        Spatial road = assetManager.loadModel("Models/Roads/roadUpHill.j3o");
        float angle = (float) Math.toRadians(180);
        road.setLocalTranslation(0, -0.50f, 0);
        road.rotate(0, angle, 0);
        road.setName("downHill");
        return road;
    }

    public Spatial centerRoad() {
        Spatial road = assetManager.loadModel("Models/Roads/roadCenter.j3o");
        road.scale(0.5f, 0.5f, 0.5f);
        return road;
    }

    public Spatial bendingRoad() {
        Spatial road = assetManager.loadModel("Models/Roads/roadBending.j3o");
        road.scale(0.5f, 0.5f, 0.5f);
        return road;
    }

    public Spatial tRoad() {
        Spatial road = assetManager.loadModel("Models/Roads/roadT.j3o");
        road.scale(0.5f, 0.5f, 0.5f);
        return road;
    }
}
