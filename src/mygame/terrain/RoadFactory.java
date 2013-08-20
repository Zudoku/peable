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
        Spatial teapot = assetManager.loadModel("Models/roadStraight.j3o");
        Material mat_default = new Material(
                assetManager, "Common/MatDefs/Misc/ShowNormals.j3md");
        teapot.setMaterial(mat_default);
        teapot.scale(0.5f, 0.5f, 0.5f);
        return teapot;
    }

    public Spatial roadUpHill() {
        Spatial teapot = assetManager.loadModel("Models/roadUpHill.j3o");
        Material mat_default = new Material(
                assetManager, "Common/MatDefs/Misc/ShowNormals.j3md");
        teapot.setMaterial(mat_default);
        //teapot.scale(0.5f, 0.5f, 0.5f);
        teapot.setLocalTranslation(0, +0.50f, 0);
        teapot.setName("upHill");
        return teapot;
    }

    public Spatial roadDownHill() {
        Spatial teapot = assetManager.loadModel("Models/roadUpHill.j3o");
        Material mat_default = new Material(
                assetManager, "Common/MatDefs/Misc/ShowNormals.j3md");
        teapot.setMaterial(mat_default);
        //teapot.scale(0.5f, 0.5f, 0.5f);
        float angle = (float) Math.toRadians(180);
        teapot.setLocalTranslation(0, -0.50f, 0);
        teapot.rotate(0, angle, 0);
        teapot.setName("downHill");
        return teapot;
    }

    public Spatial centerRoad() {
        Spatial teapot = assetManager.loadModel("Models/roadCenter.j3o");
        Material mat_default = new Material(
                assetManager, "Common/MatDefs/Misc/ShowNormals.j3md");
        teapot.setMaterial(mat_default);
        teapot.scale(0.5f, 0.5f, 0.5f);
        return teapot;
    }

    public Spatial bendingRoad() {
        Spatial teapot = assetManager.loadModel("Models/roadBending.j3o");
        Material mat_default = new Material(
                assetManager, "Common/MatDefs/Misc/ShowNormals.j3md");
        teapot.setMaterial(mat_default);
        teapot.scale(0.5f, 0.5f, 0.5f);
        return teapot;
    }

    public Spatial tRoad() {
        Spatial teapot = assetManager.loadModel("Models/roadT.j3o");
        Material mat_default = new Material(
                assetManager, "Common/MatDefs/Misc/ShowNormals.j3md");
        teapot.setMaterial(mat_default);
        teapot.scale(0.5f, 0.5f, 0.5f);
        return teapot;
    }
}
