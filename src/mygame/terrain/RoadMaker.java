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

    public RoadDirection direction = RoadDirection.UP;
    public RoadHill hill = RoadHill.FLAT;
    public RoadMakerStatus status = RoadMakerStatus.BUILDING;
    public Vector3f startingPosition;
    private final AssetManager assetManager;
    private final Node rootNode;
    Spatial roads[][][] = new Spatial[101][101][101];

    public RoadMaker(AssetManager assetManager, Node rootNode) {
        this.assetManager = assetManager;
        this.rootNode = rootNode;
    }
    public void calcPosition(){
        Vector3f roadPos= new Vector3f(startingPosition);
    switch (direction) {
            case UP:
                roadPos.add(1,0.1f, 0);
  
                break;

            case DOWN:
                roadPos.add(-1,0.1f,0);

                break;

            case RIGHT:
                roadPos.add(0,0.1f,1);
                
                break;

            case LEFT:
                roadPos.add(0,0.1f,-1);
                
                break;
        }
     }
    public void buildRoad() {
        if (status == RoadMakerStatus.CHOOSING) {
            return;
        }
        Spatial road = null;
        
        switch (hill) {

            case FLAT:
                road = roadStraight();
                break;

            case DOWN:
                road = roadDownHill();
                break;

            case UP:
                road = roadUpHill();
                break;
        }
        
        
        switch (direction) {
            case UP:
                road.move(startingPosition.x + 1, startingPosition.y + 0.1f, startingPosition.z);
                if(hill==RoadHill.UP){
                    float angle=(float)Math.toRadians(180);
                    road.rotate(0,angle , 0);
                    
                }

                break;

            case DOWN:
                road.move(startingPosition.x - 1, startingPosition.y + 0.1f, startingPosition.z);

                break;

            case RIGHT:
                road.move(startingPosition.x, startingPosition.y + 0.1f, startingPosition.z + 1);
                float angle=(float)Math.toRadians(90);
                road.rotate(0,angle , 0);
                break;

            case LEFT:
                road.move(startingPosition.x, startingPosition.y + 0.1f, startingPosition.z - 1);
                float anglet=(float)Math.toRadians(-90);
                road.rotate(0,anglet , 0);
                break;
        }


        roads[(int) road.getWorldTranslation().x][(int) road.getWorldTranslation().y][(int) road.getWorldTranslation().z] = road;
        switch (hill) {
            case FLAT:
                startingPosition = new Vector3f(road.getWorldTranslation().x, road.getWorldTranslation().y - 0.1f, road.getWorldTranslation().z);
                break;

            case UP:
                startingPosition = new Vector3f(road.getWorldTranslation().x, road.getWorldTranslation().y - 0.1f+0.25f, road.getWorldTranslation().z);
                break;
                
            case DOWN:
                startingPosition = new Vector3f(road.getWorldTranslation().x, road.getWorldTranslation().y - 0.1f-0.25f, road.getWorldTranslation().z);
                break;

        }
        
        rootNode.attachChild(road);



    }

    private Spatial roadStraight() {
        Spatial teapot = assetManager.loadModel("Models/roadStraight.j3o");
        Material mat_default = new Material(
                assetManager, "Common/MatDefs/Misc/ShowNormals.j3md");
        teapot.setMaterial(mat_default);
        teapot.scale(0.5f, 0.5f, 0.5f);
        return teapot;
    }

    private Spatial roadUpHill() {
        Spatial teapot = assetManager.loadModel("Models/roadUpHill.j3o");
        Material mat_default = new Material(
                assetManager, "Common/MatDefs/Misc/ShowNormals.j3md");
        teapot.setMaterial(mat_default);
        //teapot.scale(0.5f, 0.5f, 0.5f);
        teapot.setLocalTranslation(0, 0.25f, 0);
        return teapot;
    }
    private Spatial roadDownHill() {
        Spatial teapot = assetManager.loadModel("Models/roadUpHill.j3o");
        Material mat_default = new Material(
                assetManager, "Common/MatDefs/Misc/ShowNormals.j3md");
        teapot.setMaterial(mat_default);
        //teapot.scale(0.5f, 0.5f, 0.5f);
        float angle=(float)Math.toRadians(180);
        teapot.setLocalTranslation(0, -0.25f, 0);
        teapot.rotate(0,angle,0);
        return teapot;
    }

    public void startingPosition(Vector3f pos) {
        float x = pos.x - 0.4999f + 1;
        float y = pos.y - 0.4999f + 1;
        float z = pos.z - 0.4999f + 1;

        Vector3f vec = new Vector3f((int) x, (int) y, (int) z);
        startingPosition = vec;
        status = RoadMakerStatus.BUILDING;
    }
}
