/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package intopark.roads;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.scene.Spatial;
import intopark.inout.LoadPaths;
import java.util.logging.Logger;

/**
 *
 * @author arska
 */
public class RoadFactory {
    private static final Logger logger = Logger.getLogger(RoadFactory.class.getName());
    private final AssetManager assetManager;
    
    public RoadFactory(AssetManager assetManager){
        this.assetManager=assetManager;
    }
    private Material getTempMaterial(){
        Material m=new Material(assetManager, 
        "Common/MatDefs/Misc/Unshaded.j3md");
        m.setTexture("ColorMap", 
        assetManager.loadTexture("Textures/rocktexturesmall.png"));
        return m;
    }
    public Spatial roadStraight() {
        Spatial road = assetManager.loadModel(LoadPaths.roadstraight);
        road.scale(0.5f, 0.5f, 0.5f);
        road.setUserData("roadHill","flat");
        road.setUserData("type","road");
        return road;
    }

    public Spatial roadUpHill() {
        Spatial road = assetManager.loadModel(LoadPaths.roaduphill);
        road.setLocalTranslation(0, +0.50f, 0);
        road.setUserData("roadHill","upHill");
        road.setUserData("type","road");
        
        
        return road;
    }

    public Spatial roadDownHill() {
        Spatial road = assetManager.loadModel(LoadPaths.roaduphill);
        float angle = (float) Math.toRadians(180);
        road.setLocalTranslation(0, -0.50f, 0);
        road.rotate(0, angle, 0);
        road.setUserData("roadHill","downHill");
        road.setUserData("type","road");
        
        return road;
    }

    public Spatial centerRoad() {
        Spatial road = assetManager.loadModel(LoadPaths.roadcenter);
        road.scale(0.5f, 0.5f, 0.5f);
        road.setUserData("roadHill","flat");
        road.setUserData("type","road");
        return road;
    }

    public Spatial bendingRoad() {
        Spatial road = assetManager.loadModel(LoadPaths.roadbending);
        road.scale(0.5f, 0.5f, 0.5f);
        road.setUserData("roadHill","flat");
        road.setUserData("type","road");
        return road;
    }

    public Spatial tRoad() {
        Spatial road = assetManager.loadModel(LoadPaths.roadT);
        road.scale(0.5f, 0.5f, 0.5f);
        road.setUserData("roadHill","flat");
        road.setUserData("type","road");
        return road;
    }
    //älä käytä vielä
    public Spatial queroadStraight() {
        Spatial road = assetManager.loadModel(LoadPaths.queroadstraight);
        
        road.scale(0.5f, 0.5f, 0.5f);
        road.setUserData("roadHill","flat");
        road.setUserData("type","queroad");
        return road;
    }
    //älä käytä vielä
    public Spatial queroadUpHill() {
        Spatial road = assetManager.loadModel(LoadPaths.queroaduphill);
        
        road.setLocalTranslation(0, +0.50f, 0);
        road.setUserData("roadHill","upHill");
        road.setUserData("type","queroad");
        
        return road;
    }

    public Spatial queroadDownHill() {
        Spatial road = assetManager.loadModel(LoadPaths.queroaduphill);
        
        float angle = (float) Math.toRadians(180);
        road.setLocalTranslation(0, -0.50f, 0);
        road.rotate(0, angle, 0);
        road.setUserData("roadHill","downHill");
        road.setUserData("type","queroad");
        
        return road;
    }


    public Spatial quebendingRoad() {
        Spatial road = assetManager.loadModel(LoadPaths.queroadbending);
        
        road.scale(0.5f, 0.5f, 0.5f);
        road.setUserData("roadHill","flat");
        road.setUserData("type","queroad");
        return road;
    }

    
}
