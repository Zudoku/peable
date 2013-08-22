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
    //X,Y,Z
    public Spatial roads[][][] = new Spatial[100][100][100];
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
        if(roads[(int) calcPosition().x][(int)calcPosition().y][(int) calcPosition().z]!=null){
            Spatial temproad=roads[(int) calcPosition().x][(int)calcPosition().y][(int) calcPosition().z];
            if(temproad.getName()=="upHill"){
                roads[(int) calcPosition().x][(int)calcPosition().y+1][(int) calcPosition().z]=null;
            }
            if(temproad.getName()=="downHill"){
                roads[(int) calcPosition().x][(int)calcPosition().y-1][(int) calcPosition().z]=null;
            }
            rootNode.detachChild(temproad);
        }
        roads[(int) calcPosition().x][(int)calcPosition().y][(int) calcPosition().z] = road;
        if(road.getName()=="upHill"){
            roads[(int) calcPosition().x][(int)calcPosition().y+1][(int) calcPosition().z] = road;
        }
        if(road.getName()=="downHill"){
            roads[(int) calcPosition().x][(int)calcPosition().y-1][(int) calcPosition().z] = road;
        }
        switch (hill) {
            case FLAT:
                startingPosition = new Vector3f(road.getWorldTranslation().x, road.getWorldTranslation().y - 0.1f, road.getWorldTranslation().z);
                break;

            case UP:
                Vector3f pos=calcPosition();
                pos.y=(int)pos.y+1;
                startingPosition = pos;
                break;

            case DOWN:
                Vector3f pos2=calcPosition();
                pos2.y=(int)pos2.y-1;
                startingPosition = pos2;
                break;

        }

        rootNode.attachChild(road);
        int tempx=(int)pyorista(startingPosition).x;
        int tempz=(int)pyorista(startingPosition).z;
        int tempy=(int)(startingPosition).y;
        updateRoad(tempx-1,tempy, tempz-1);
        updateRoad(tempx-1,tempy, tempz);
        updateRoad(tempx-1,tempy, tempz+1);
        updateRoad(tempx,tempy, tempz-1);
        updateRoad(tempx,tempy, tempz);
        updateRoad(tempx,tempy, tempz+1);
        updateRoad(tempx+1,tempy, tempz-1);
        updateRoad(tempx+1,tempy, tempz);
        updateRoad(tempx+1,tempy, tempz+1);
        
        
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
    //X,Y,Z
    public void updateRoad(int x,int z, int y) {
        if(roads[x][z][y]==null||roads[x][z][y].getName()=="upHill"||roads[x][z][y].getName()=="downHill"){
            return;
        }
        if (roads[x + 1][z][y] != null && roads[x - 1][z][y] != null && roads[x][z][y + 1] != null && roads[x][z][y - 1] != null) {
            Spatial temp = roads[x][z][y];
            if (temp != null) {
                rootNode.detachChild(temp);
            }
            Spatial roadp = roadF.centerRoad();
            roadp.move(x, z+0.1f, y);
            roads[x][z][y]=roadp;
            rootNode.attachChild(roadp);

            return;
        }
        if (roads[x + 1][z][y] != null && roads[x - 1][z][y] != null && roads[x][z][y + 1] != null && roads[x][z][y - 1] == null) {
            Spatial temp = roads[x][z][y];
            if (temp != null) {
                rootNode.detachChild(temp);
            }
            Spatial roadp = roadF.tRoad();
            roadp.move(x,z+0.1f, y);
            float angle = (float) Math.toRadians(90);
            roadp.rotate(0,angle,0);
            roads[x][z][y]=roadp;
            rootNode.attachChild(roadp);
            


            return;
        }
        if (roads[x + 1][z][y] == null && roads[x - 1][z][y] != null && roads[x][z][y + 1] != null && roads[x][z][y - 1] != null) {
            Spatial temp = roads[x][z][y];
            if (temp != null) {
                rootNode.detachChild(temp);
            }
            Spatial roadp = roadF.tRoad();
            roadp.move(x, z+0.1f, y);
            
            roads[x][z][y]=roadp;
            rootNode.attachChild(roadp);
            


            return;
        }
        if (roads[x + 1][z][y] != null && roads[x - 1][z][y] != null && roads[x][z][y + 1] == null && roads[x][z][y - 1] != null) {
            Spatial temp = roads[x][z][y];
            if (temp != null) {
                rootNode.detachChild(temp);
            }
            Spatial roadp = roadF.tRoad();
            roadp.move(x, z+0.1f, y);
            float angle = (float) Math.toRadians(-90);
            roadp.rotate(0,angle,0);
            roads[x][z][y]=roadp;
            rootNode.attachChild(roadp);
            


            return;
        }
        if (roads[x + 1][z][y] != null && roads[x - 1][z][y] == null && roads[x][z][y + 1] != null && roads[x][z][y - 1] != null) {
            Spatial temp = roads[x][z][y];
            if (temp != null) {
                rootNode.detachChild(temp);
            }
            Spatial roadp = roadF.tRoad();
            roadp.move(x,z+0.1f, y);
            float angle = (float) Math.toRadians(180);
            roadp.rotate(0,angle,0);
            roads[x][z][y]=roadp;
            rootNode.attachChild(roadp);
            


            return;
        }
        if (roads[x + 1][z][y] == null && roads[x - 1][z][y] != null && roads[x][z][y + 1] == null && roads[x][z][y - 1] != null) {
            Spatial temp = roads[x][z][y];
            if (temp != null) {
                rootNode.detachChild(temp);
            }
            Spatial roadp = roadF.bendingRoad();
            roadp.move(x, z+0.1f, y);
            float angle = (float) Math.toRadians(180);
            roadp.rotate(0,angle,0);
            roads[x][z][y]=roadp;
            rootNode.attachChild(roadp);
            


            return;
        }
        if (roads[x + 1][z][y] != null && roads[x - 1][z][y] == null && roads[x][z][y + 1] == null && roads[x][z][y - 1] != null) {
            Spatial temp = roads[x][z][y];
            if (temp != null) {
                rootNode.detachChild(temp);
            }
            Spatial roadp = roadF.bendingRoad();
            roadp.move(x, z+0.1f, y);
            float angle = (float) Math.toRadians(90);
            roadp.rotate(0,angle,0);
            roads[x][z][y]=roadp;
            rootNode.attachChild(roadp);
            


            return;
        }
        if (roads[x + 1][z][y] != null && roads[x - 1][z][y] == null && roads[x][z][y + 1] != null && roads[x][z][y - 1] == null) {
            Spatial temp = roads[x][z][y];
            if (temp != null) {
                rootNode.detachChild(temp);
            }
            Spatial roadp = roadF.bendingRoad();
            roadp.move(x, z+0.1f, y);
            
            roads[x][z][y]=roadp;
            rootNode.attachChild(roadp);
            


            return;
        }
        if (roads[x + 1][z][y] == null && roads[x - 1][z][y] != null && roads[x][z][y + 1] != null && roads[x][z][y - 1] == null) {
            Spatial temp = roads[x][z][y];
            if (temp != null) {
                rootNode.detachChild(temp);
            }
            Spatial roadp = roadF.bendingRoad();
            roadp.move(x, z+0.1f, y);
            float angle = (float) Math.toRadians(-90);
            roadp.rotate(0,angle,0);
            roads[x][z][y]=roadp;
            rootNode.attachChild(roadp);
            


            return;
        }
        if (roads[x + 1][z][y] != null && roads[x - 1][z][y] == null && roads[x][z][y + 1] == null && roads[x][z][y - 1] == null) {
            Spatial temp = roads[x][z][y];
            if (temp != null) {
                rootNode.detachChild(temp);
            }
            Spatial roadp = roadF.roadStraight();
            roadp.move(x, z+0.1f, y);
            
            roads[x][z][y]=roadp;
            rootNode.attachChild(roadp);
            


            return;
        }
        if (roads[x + 1][z][y] == null && roads[x - 1][z][y] == null && roads[x][z][y + 1] != null && roads[x][z][y - 1] == null) {
            Spatial temp = roads[x][z][y];
            if (temp != null) {
                rootNode.detachChild(temp);
            }
            Spatial roadp = roadF.roadStraight();
            roadp.move(x, z+0.1f, y);
            float angle = (float) Math.toRadians(90);
            roadp.rotate(0,angle,0);
            roads[x][z][y]=roadp;
            rootNode.attachChild(roadp);
            


            return;
        }
        if (roads[x + 1][z][y] == null && roads[x - 1][z][y] != null && roads[x][z][y + 1] == null && roads[x][z][y - 1] == null) {
            Spatial temp = roads[x][z][y];
            if (temp != null) {
                rootNode.detachChild(temp);
            }
            Spatial roadp = roadF.roadStraight();
            roadp.move(x, z+0.1f, y);        
            roads[x][z][y]=roadp;
            rootNode.attachChild(roadp);
            


            return;
        }
        if (roads[x + 1][z][y] == null && roads[x - 1][z][y] == null && roads[x][z][y + 1] == null && roads[x][z][y - 1] != null) {
            Spatial temp = roads[x][z][y];
            if (temp != null) {
                rootNode.detachChild(temp);
            }
            Spatial roadp = roadF.roadStraight();
            roadp.move(x, z+0.1f, y);
            float angle = (float) Math.toRadians(90);
            roadp.rotate(0,angle,0);
            roads[x][z][y]=roadp;
            rootNode.attachChild(roadp);
            


            return;
        }
        System.out.println("BUG IN THE SYSTEM ZZZZ");
        return;
    }
}
