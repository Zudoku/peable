/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.terrain;

import com.jme3.asset.AssetManager;
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
    Spatial roads[][] = new Spatial[100][100];
    public boolean change = true;
    public RoadFactory roadF;

    public RoadMaker(AssetManager assetManager, Node rootNode) {
        this.assetManager = assetManager;
        this.rootNode = rootNode;
        roadF = new RoadFactory(assetManager);
    }

    public Vector3f calcPosition() {
        Vector3f roadPos = new Vector3f(startingPosition);
        switch (direction) {
            case UP:

                roadPos.x = roadPos.x + 1;
                roadPos.y = roadPos.y + 0.1f;

                break;

            case DOWN:
                roadPos.x = roadPos.x - 1;
                roadPos.y = roadPos.y + 0.1f;
                break;

            case RIGHT:
                roadPos.z = roadPos.z + 1;
                roadPos.y = roadPos.y + 0.1f;
                break;

            case LEFT:
                roadPos.z = roadPos.z - 1;
                roadPos.y = roadPos.y + 0.1f;
                break;
        }
        return roadPos;
    }

    public void buildRoad() {
        if (status == RoadMakerStatus.CHOOSING) {
            return;
        }
        Spatial road = null;

        switch (hill) {

            case FLAT:
                //check 
                road = roadF.roadStraight();

                break;

            case DOWN:
                road = roadF.roadDownHill();
                break;

            case UP:
                road = roadF.roadUpHill();
                break;
        }

        if (change = true) {
            switch (direction) {
                case UP:

                    if (hill == RoadHill.UP || hill == RoadHill.DOWN) {
                        float angle = (float) Math.toRadians(180);
                        road.rotate(0, angle, 0);

                    }

                    break;

                case DOWN:


                    break;

                case RIGHT:

                    float angle = (float) Math.toRadians(90);
                    road.rotate(0, angle, 0);
                    break;

                case LEFT:

                    float anglet = (float) Math.toRadians(-90);
                    road.rotate(0, anglet, 0);
                    break;
            }
        }
        road.move(calcPosition());
        if(roads[(int) calcPosition().x][(int) calcPosition().z]!=null){
            Spatial temproad=roads[(int) calcPosition().x][(int) calcPosition().z];
            rootNode.detachChild(temproad);
        }
        roads[(int) calcPosition().x][(int) calcPosition().z] = road;
        switch (hill) {
            case FLAT:
                startingPosition = new Vector3f(road.getWorldTranslation().x, road.getWorldTranslation().y - 0.1f, road.getWorldTranslation().z);
                break;

            case UP:
                startingPosition = new Vector3f(road.getWorldTranslation().x, road.getWorldTranslation().y - 0.1f + 0.25f, road.getWorldTranslation().z);
                break;

            case DOWN:
                startingPosition = new Vector3f(road.getWorldTranslation().x, road.getWorldTranslation().y - 0.1f - 0.25f, road.getWorldTranslation().z);
                break;

        }

        rootNode.attachChild(road);
        int tempx=(int)pyorista(startingPosition).x;
        int tempz=(int)pyorista(startingPosition).z;
        int tempy=(int)pyorista(startingPosition).y;
        updateRoad(tempx-1, tempz-1);
        updateRoad(tempx-1, tempz);
        updateRoad(tempx-1, tempz+1);
        updateRoad(tempx, tempz-1);
        updateRoad(tempx, tempz);
        updateRoad(tempx, tempz+1);
        updateRoad(tempx+1, tempz-1);
        updateRoad(tempx+1, tempz);
        updateRoad(tempx+1, tempz+1);
        
        
    }
    public Vector3f pyorista(Vector3f pos){
         float x = pos.x - 0.4999f + 1;
        float y = pos.y - 0.4999f + 1;
        float z = pos.z - 0.4999f + 1;

        Vector3f vec = new Vector3f((int) x, (int) y, (int) z);
        return vec;
    }

    public void startingPosition(Vector3f pos) {
        float x = pos.x - 0.4999f + 1;
        float y = pos.y - 0.4999f + 1;
        float z = pos.z - 0.4999f + 1;

        Vector3f vec = new Vector3f((int) x, (int) y, (int) z);
        startingPosition = vec;
        status = RoadMakerStatus.BUILDING;
    }

    public void updateRoad(int x, int y) {
        if(roads[x][y]==null){
            return;
        }
        if (roads[x + 1][y] != null && roads[x - 1][y] != null && roads[x][y + 1] != null && roads[x][y - 1] != null) {
            Spatial temp = roads[x][y];
            if (temp != null) {
                rootNode.detachChild(temp);
            }
            Spatial roadp = roadF.centerRoad();
            roadp.move(x, 6.10f, y);
            roads[x][y]=roadp;
            rootNode.attachChild(roadp);

            return;
        }
        if (roads[x + 1][y] != null && roads[x - 1][y] != null && roads[x][y + 1] != null && roads[x][y - 1] == null) {
            Spatial temp = roads[x][y];
            if (temp != null) {
                rootNode.detachChild(temp);
            }
            Spatial roadp = roadF.tRoad();
            roadp.move(x, 6.10f, y);
            float angle = (float) Math.toRadians(90);
            roadp.rotate(0,angle,0);
            roads[x][y]=roadp;
            rootNode.attachChild(roadp);
            


            return;
        }
        if (roads[x + 1][y] == null && roads[x - 1][y] != null && roads[x][y + 1] != null && roads[x][y - 1] != null) {
            Spatial temp = roads[x][y];
            if (temp != null) {
                rootNode.detachChild(temp);
            }
            Spatial roadp = roadF.tRoad();
            roadp.move(x, 6.10f, y);
            
            roads[x][y]=roadp;
            rootNode.attachChild(roadp);
            


            return;
        }
        if (roads[x + 1][y] != null && roads[x - 1][y] != null && roads[x][y + 1] == null && roads[x][y - 1] != null) {
            Spatial temp = roads[x][y];
            if (temp != null) {
                rootNode.detachChild(temp);
            }
            Spatial roadp = roadF.tRoad();
            roadp.move(x, 6.10f, y);
            float angle = (float) Math.toRadians(-90);
            roadp.rotate(0,angle,0);
            roads[x][y]=roadp;
            rootNode.attachChild(roadp);
            


            return;
        }
        if (roads[x + 1][y] != null && roads[x - 1][y] == null && roads[x][y + 1] != null && roads[x][y - 1] != null) {
            Spatial temp = roads[x][y];
            if (temp != null) {
                rootNode.detachChild(temp);
            }
            Spatial roadp = roadF.tRoad();
            roadp.move(x, 6.10f, y);
            float angle = (float) Math.toRadians(180);
            roadp.rotate(0,angle,0);
            roads[x][y]=roadp;
            rootNode.attachChild(roadp);
            


            return;
        }
        if (roads[x + 1][y] == null && roads[x - 1][y] != null && roads[x][y + 1] == null && roads[x][y - 1] != null) {
            Spatial temp = roads[x][y];
            if (temp != null) {
                rootNode.detachChild(temp);
            }
            Spatial roadp = roadF.bendingRoad();
            roadp.move(x, 6.10f, y);
            float angle = (float) Math.toRadians(180);
            roadp.rotate(0,angle,0);
            roads[x][y]=roadp;
            rootNode.attachChild(roadp);
            


            return;
        }
        if (roads[x + 1][y] != null && roads[x - 1][y] == null && roads[x][y + 1] == null && roads[x][y - 1] != null) {
            Spatial temp = roads[x][y];
            if (temp != null) {
                rootNode.detachChild(temp);
            }
            Spatial roadp = roadF.bendingRoad();
            roadp.move(x, 6.10f, y);
            float angle = (float) Math.toRadians(90);
            roadp.rotate(0,angle,0);
            roads[x][y]=roadp;
            rootNode.attachChild(roadp);
            


            return;
        }
        if (roads[x + 1][y] != null && roads[x - 1][y] == null && roads[x][y + 1] != null && roads[x][y - 1] == null) {
            Spatial temp = roads[x][y];
            if (temp != null) {
                rootNode.detachChild(temp);
            }
            Spatial roadp = roadF.bendingRoad();
            roadp.move(x, 6.10f, y);
            
            roads[x][y]=roadp;
            rootNode.attachChild(roadp);
            


            return;
        }
        if (roads[x + 1][y] == null && roads[x - 1][y] != null && roads[x][y + 1] != null && roads[x][y - 1] == null) {
            Spatial temp = roads[x][y];
            if (temp != null) {
                rootNode.detachChild(temp);
            }
            Spatial roadp = roadF.bendingRoad();
            roadp.move(x, 6.10f, y);
            float angle = (float) Math.toRadians(-90);
            roadp.rotate(0,angle,0);
            roads[x][y]=roadp;
            rootNode.attachChild(roadp);
            


            return;
        }
        if (roads[x + 1][y] != null && roads[x - 1][y] == null && roads[x][y + 1] == null && roads[x][y - 1] == null) {
            Spatial temp = roads[x][y];
            if (temp != null) {
                rootNode.detachChild(temp);
            }
            Spatial roadp = roadF.roadStraight();
            roadp.move(x, 6.10f, y);
            
            roads[x][y]=roadp;
            rootNode.attachChild(roadp);
            


            return;
        }
        if (roads[x + 1][y] == null && roads[x - 1][y] == null && roads[x][y + 1] != null && roads[x][y - 1] == null) {
            Spatial temp = roads[x][y];
            if (temp != null) {
                rootNode.detachChild(temp);
            }
            Spatial roadp = roadF.roadStraight();
            roadp.move(x, 6.10f, y);
            float angle = (float) Math.toRadians(90);
            roadp.rotate(0,angle,0);
            roads[x][y]=roadp;
            rootNode.attachChild(roadp);
            


            return;
        }
        if (roads[x + 1][y] == null && roads[x - 1][y] != null && roads[x][y + 1] == null && roads[x][y - 1] == null) {
            Spatial temp = roads[x][y];
            if (temp != null) {
                rootNode.detachChild(temp);
            }
            Spatial roadp = roadF.roadStraight();
            roadp.move(x, 6.10f, y);        
            roads[x][y]=roadp;
            rootNode.attachChild(roadp);
            


            return;
        }
        if (roads[x + 1][y] == null && roads[x - 1][y] == null && roads[x][y + 1] == null && roads[x][y - 1] != null) {
            Spatial temp = roads[x][y];
            if (temp != null) {
                rootNode.detachChild(temp);
            }
            Spatial roadp = roadF.roadStraight();
            roadp.move(x, 6.10f, y);
            float angle = (float) Math.toRadians(90);
            roadp.rotate(0,angle,0);
            roads[x][y]=roadp;
            rootNode.attachChild(roadp);
            


            return;
        }
        System.out.println("BUG IN THE SYSTEM ZZZZ");
        return;
    }
}
