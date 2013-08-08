/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.terrain;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;


/**
 *
 * @author arska
 */
public class RoadMaker {
    public RoadDirection direction= RoadDirection.UP;
    public RoadHill hill=RoadHill.FLAT;
    public Vector3f startingPosition;
    private final AssetManager assetManager;
    private final Node rootNode;
    Spatial roads[][][]=new Spatial[101][101][101];
    
    public RoadMaker(AssetManager assetManager,Node rootNode){
        this.assetManager=assetManager;
        this.rootNode=rootNode;
    }
    
    public void buildRoad(){
        Spatial road=roadStraight();
        
        switch(direction){
            case UP:
                road.move(startingPosition.x+1, startingPosition.y, startingPosition.z);
                road.rotate(90, 0, 0);
                break;
                
            case DOWN:
                road.move(startingPosition.x-1, startingPosition.y, startingPosition.z);
                road.rotate(90, 0, 0);
                break;
                
            case RIGHT:
                road.move(startingPosition.x, startingPosition.y, startingPosition.z-1);
                break;
                
            case LEFT:
                road.move(startingPosition.x, startingPosition.y, startingPosition.z+1);
                break;
        }
        roads[(int)road.getWorldTranslation().x][(int)road.getWorldTranslation().y][(int)road.getWorldTranslation().z]=road;
        startingPosition=road.getWorldTranslation();
        rootNode.attachChild(road);
        
        
        
    }
    
    private Spatial roadStraight(){
        Spatial teapot = assetManager.loadModel("Models/roadStraight.j3o");
        Material mat_default = new Material( 
            assetManager, "Common/MatDefs/Misc/ShowNormals.j3md");
        teapot.setMaterial(mat_default);
        teapot.scale(0.5f, 0.5f, 0.5f);
        return teapot;
    }
    
    
}
