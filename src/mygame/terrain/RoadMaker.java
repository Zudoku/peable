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

    public Direction direction = Direction.UP;
    public RoadHill hill = RoadHill.FLAT;
    public RoadMakerStatus status = RoadMakerStatus.BUILDING;
    public Vector3f startingPosition;
    private final AssetManager assetManager;
    private final Node rootNode;
    public boolean queroad = false;
    //X,Y,Z
    public Spatial map[][][] = new Spatial[100][100][100];
    public boolean change = true;
    public RoadFactory roadF;
    private Spatial lastqueroad;

    public RoadMaker(AssetManager assetManager, Node rootNode) {
        this.assetManager = assetManager;
        this.rootNode = rootNode;
        roadF = new RoadFactory(assetManager);
    }

    public Vector3f calcRoadPosition() {
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
        if (queroad == false) {
            switch (hill) {

                case FLAT:
                    road = roadF.roadStraight();

                    break;

                case DOWN:
                    road = roadF.roadDownHill();
                    break;

                case UP:
                    road = roadF.roadUpHill();
                    break;
            }
        }
        else{
            switch (hill) {

                case FLAT:
                    road = roadF.queroadStraight();

                    break;

                case DOWN:
                    road = roadF.queroadDownHill();
                    break;

                case UP:
                    road = roadF.queroadUpHill();
                    break;
            }
            
        }

        if (change = true) {
            switch (direction) {
                case UP:
                    //jos menosuuntana pohjoinen
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

        road.move(calcRoadPosition());

        /**
         * katsoo jos siinä kohdassa johon tietä rakennetaan on jotain mapissa
         * jos se on tienpala niin se poistaa sen
         */
        
        if (map[(int) calcRoadPosition().x][(int) calcRoadPosition().y][(int) calcRoadPosition().z] != null) {
            Spatial test = map[(int) calcRoadPosition().x][(int) calcRoadPosition().y][(int) calcRoadPosition().z];
            if(queroad==false){
                if (!test.getUserData("type").equals("road")) {
                    return;
                }
            }else{
                if (!test.getUserData("type").equals("queroad")) {
                    return;
                }
            }

            Spatial temproad = map[(int) calcRoadPosition().x][(int) calcRoadPosition().y][(int) calcRoadPosition().z];

            if (temproad.getUserData("roadHill").equals("upHill")) {
                map[(int) calcRoadPosition().x][(int) calcRoadPosition().y + 1][(int) calcRoadPosition().z] = null;
            }

            if (temproad.getUserData("roadHill").equals("downHill")) {
                map[(int) calcRoadPosition().x][(int) calcRoadPosition().y - 1][(int) calcRoadPosition().z] = null;
            }

            rootNode.detachChild(temproad);
        }

        map[(int) calcRoadPosition().x][(int) calcRoadPosition().y][(int) calcRoadPosition().z] = road;

        if (road.getUserData("roadHill").equals("upHill")) {

            map[(int) calcRoadPosition().x][(int) calcRoadPosition().y + 1][(int) calcRoadPosition().z] = road;
        }

        if (road.getUserData("roadHill").equals("downHill")) {

            map[(int) calcRoadPosition().x][(int) calcRoadPosition().y - 1][(int) calcRoadPosition().z] = road;
        }

        /**
         *
         * Uusi Startingposition sen mukaan millanen tie on juuri laitettiin
         *
         */
        switch (hill) {
            case FLAT:
                startingPosition = new Vector3f(road.getWorldTranslation().x, road.getWorldTranslation().y - 0.1f, road.getWorldTranslation().z);
                break;

            case UP:
                Vector3f pos = calcRoadPosition();
                pos.y = (int) pos.y + 1;
                startingPosition = pos;
                break;

            case DOWN:
                Vector3f pos2 = calcRoadPosition();
                pos2.y = (int) pos2.y - 1;
                startingPosition = pos2;
                break;

        }

        rootNode.attachChild(road);

        int tempx = (int) pyorista(startingPosition).x;
        int tempz = (int) pyorista(startingPosition).z;
        int tempy = (int) (startingPosition).y;
        if(queroad==false){
        updateroads(tempx, tempy, tempz);
        lastqueroad=null;
        }
        else{
            updateQueroad(tempx,tempy,tempz,road);
        }


    }

    public void updateroads(int tempx, int tempy, int tempz) {
        updateRoad(tempx - 1, tempy, tempz - 1);
        updateRoad(tempx - 1, tempy, tempz);
        updateRoad(tempx - 1, tempy, tempz + 1);
        updateRoad(tempx, tempy, tempz - 1);
        updateRoad(tempx, tempy, tempz);
        updateRoad(tempx, tempy, tempz + 1);
        updateRoad(tempx + 1, tempy, tempz - 1);
        updateRoad(tempx + 1, tempy, tempz);
        updateRoad(tempx + 1, tempy, tempz + 1);
        updateRoad(tempx, tempy + 1, tempz - 1);
        updateRoad(tempx, tempy - 1, tempz - 1);
    }

    public Vector3f pyorista(Vector3f pos) {
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
        lastqueroad=null;
    }

    private boolean roadtypeCondition(int x, int z, int y, boolean con1, boolean con2, boolean con3, boolean con4) {
        boolean condition1 = false;
        boolean condition2 = false;
        boolean condition3 = false;
        boolean condition4 = false;

        //1 condition
        if (con1 == true) {
            if (map[x + 1][z][y] != null) {
                Spatial temp = map[x + 1][z][y];
                if (temp.getUserData("type").equals("road")) {
                    condition1 = true;

                }
                if (temp.getUserData("type").equals("shop")) {
                    condition1 = true;

                }
            }
        } else {
            if (map[x + 1][z][y] == null) {
                condition1 = true;
            } else {
                Spatial temp = map[x + 1][z][y];
                if (!temp.getUserData("type").equals("road")) {
                    condition1 = true;
                }

            }
        }


        //2 condition
        if (con2 == true) {
            if (map[x - 1][z][y] != null) {
                Spatial temp = map[x - 1][z][y];
                if (temp.getUserData("type").equals("road")) {
                    condition2 = true;

                }
                if (temp.getUserData("type").equals("shop")) {
                    condition2 = true;

                }
            }
        } else {
            if (map[x - 1][z][y] == null) {
                condition2 = true;
            } else {
                Spatial temp = map[x - 1][z][y];
                if (!temp.getUserData("type").equals("road")) {
                    condition2 = true;
                }

            }
        }
        //3 condition
        if (con3 == true) {
            if (map[x][z][y + 1] != null) {
                Spatial temp = map[x][z][y + 1];
                if (temp.getUserData("type").equals("road")) {
                    condition3 = true;

                }
                if (temp.getUserData("type").equals("shop")) {
                    condition3 = true;

                }
            }
        } else {
            if (map[x][z][y + 1] == null) {
                condition3 = true;
            } else {
                Spatial temp = map[x][z][y + 1];
                if (!temp.getUserData("type").equals("road")) {
                    condition3 = true;
                }
            }
        }
        //4 condition
        if (con4 == true) {
            if (map[x][z][y - 1] != null) {
                Spatial temp = map[x][z][y - 1];
                if (temp.getUserData("type").equals("road")) {
                    condition4 = true;

                }
                if (temp.getUserData("type").equals("shop")) {
                    condition1 = true;

                }
            }
        } else {
            if (map[x][z][y - 1] == null) {
                condition4 = true;
            } else {
                Spatial temp = map[x][z][y - 1];
                if (!temp.getUserData("type").equals("road")) {
                    condition4 = true;
                }
            }
        }
        if (condition1 && condition2 && condition3 && condition4) {
            return true;
        }
        return false;
    }

    private void updateRoad(int x, int z, int y) {

        if (map[x][z][y] == null || !map[x][z][y].getUserData("type").equals("road") || map[x][z][y].getUserData("roadHill").equals("upHill") || map[x][z][y].getUserData("roadHill").equals("downHill")) {
            return;
        }

        if (roadtypeCondition(x, z, y, true, true, true, true)) {
            Spatial temp = map[x][z][y];
            if (temp != null) {
                rootNode.detachChild(temp);
            }
            Spatial roadp = roadF.centerRoad();
            roadp.move(x, z + 0.1f, y);
            map[x][z][y] = roadp;
            rootNode.attachChild(roadp);

            return;
        }

        if (roadtypeCondition(x, z, y, true, true, true, false)) {
            Spatial temp = map[x][z][y];
            if (temp != null) {
                rootNode.detachChild(temp);
            }
            Spatial roadp = roadF.tRoad();
            roadp.move(x, z + 0.1f, y);
            float angle = (float) Math.toRadians(90);
            roadp.rotate(0, angle, 0);
            map[x][z][y] = roadp;
            rootNode.attachChild(roadp);



            return;
        }

        if (roadtypeCondition(x, z, y, false, true, true, true)) {
            Spatial temp = map[x][z][y];
            if (temp != null) {
                rootNode.detachChild(temp);
            }
            Spatial roadp = roadF.tRoad();
            roadp.move(x, z + 0.1f, y);

            map[x][z][y] = roadp;
            rootNode.attachChild(roadp);



            return;
        }

        if (roadtypeCondition(x, z, y, true, true, false, true)) {
            Spatial temp = map[x][z][y];
            if (temp != null) {
                rootNode.detachChild(temp);
            }
            Spatial roadp = roadF.tRoad();
            roadp.move(x, z + 0.1f, y);
            float angle = (float) Math.toRadians(-90);
            roadp.rotate(0, angle, 0);
            map[x][z][y] = roadp;
            rootNode.attachChild(roadp);



            return;
        }

        if (roadtypeCondition(x, z, y, true, false, true, true)) {
            Spatial temp = map[x][z][y];
            if (temp != null) {
                rootNode.detachChild(temp);
            }
            Spatial roadp = roadF.tRoad();
            roadp.move(x, z + 0.1f, y);
            float angle = (float) Math.toRadians(180);
            roadp.rotate(0, angle, 0);
            map[x][z][y] = roadp;
            rootNode.attachChild(roadp);



            return;
        }
        if (roadtypeCondition(x, z, y, false, true, false, true)) {
            Spatial temp = map[x][z][y];
            if (temp != null) {
                rootNode.detachChild(temp);
            }
            Spatial roadp = roadF.bendingRoad();
            roadp.move(x, z + 0.1f, y);
            float angle = (float) Math.toRadians(180);
            roadp.rotate(0, angle, 0);
            map[x][z][y] = roadp;
            rootNode.attachChild(roadp);



            return;
        }
        if (roadtypeCondition(x, z, y, true, false, false, true)) {
            Spatial temp = map[x][z][y];
            if (temp != null) {
                rootNode.detachChild(temp);
            }
            Spatial roadp = roadF.bendingRoad();
            roadp.move(x, z + 0.1f, y);
            float angle = (float) Math.toRadians(90);
            roadp.rotate(0, angle, 0);
            map[x][z][y] = roadp;
            rootNode.attachChild(roadp);



            return;
        }
        if (roadtypeCondition(x, z, y, true, false, true, false)) {
            Spatial temp = map[x][z][y];
            if (temp != null) {
                rootNode.detachChild(temp);
            }
            Spatial roadp = roadF.bendingRoad();
            roadp.move(x, z + 0.1f, y);

            map[x][z][y] = roadp;
            rootNode.attachChild(roadp);



            return;
        }
        if (roadtypeCondition(x, z, y, false, true, true, false)) {
            Spatial temp = map[x][z][y];
            if (temp != null) {
                rootNode.detachChild(temp);
            }
            Spatial roadp = roadF.bendingRoad();
            roadp.move(x, z + 0.1f, y);
            float angle = (float) Math.toRadians(-90);
            roadp.rotate(0, angle, 0);
            map[x][z][y] = roadp;
            rootNode.attachChild(roadp);



            return;
        }
        if (roadtypeCondition(x, z, y, true, false, false, false)) {
            Spatial temp = map[x][z][y];
            if (temp != null) {
                rootNode.detachChild(temp);
            }
            Spatial roadp = roadF.roadStraight();
            roadp.move(x, z + 0.1f, y);

            map[x][z][y] = roadp;
            rootNode.attachChild(roadp);



            return;
        }
        if (roadtypeCondition(x, z, y, false, false, true, false)) {
            Spatial temp = map[x][z][y];
            if (temp != null) {
                rootNode.detachChild(temp);
            }
            Spatial roadp = roadF.roadStraight();
            roadp.move(x, z + 0.1f, y);
            float angle = (float) Math.toRadians(90);
            roadp.rotate(0, angle, 0);
            map[x][z][y] = roadp;
            rootNode.attachChild(roadp);



            return;
        }
        if (roadtypeCondition(x, z, y, false, true, false, false)) {
            Spatial temp = map[x][z][y];
            if (temp != null) {
                rootNode.detachChild(temp);
            }
            Spatial roadp = roadF.roadStraight();
            roadp.move(x, z + 0.1f, y);
            map[x][z][y] = roadp;
            rootNode.attachChild(roadp);



            return;
        }
        if (roadtypeCondition(x, z, y, false, false, false, true)) {
            Spatial temp = map[x][z][y];
            if (temp != null) {
                rootNode.detachChild(temp);
            }
            Spatial roadp = roadF.roadStraight();
            roadp.move(x, z + 0.1f, y);
            float angle = (float) Math.toRadians(90);
            roadp.rotate(0, angle, 0);
            map[x][z][y] = roadp;
            rootNode.attachChild(roadp);




        }


    }

    private void updateQueroad(int x,int y,int z,Spatial road) {
        Spatial connectedroad = null;
        /**
         * Etsitään quetienpalasia jos tie on katkennut
         */
        if(lastqueroad==null){
            //TODO check jos connectedroad connected = true
            if(map[x+1][y][z]!=null){
                Spatial isthisEnterance= map[x + 1][y][z];
                if(isthisEnterance.getUserData("type").equals("queroad")){
                    connectedroad=isthisEnterance;
                }
            }else if(map[x-1][y][z]!=null){
                Spatial isthisEnterance= map[x - 1][y][z];
                if(isthisEnterance.getUserData("type").equals("queroad")){
                    connectedroad=isthisEnterance;
                }
            }else if(map[x][y][z+1]!=null){
                Spatial isthisEnterance= map[x][y][z + 1];
                if(isthisEnterance.getUserData("type").equals("queroad")){
                    connectedroad=isthisEnterance;
                }
            }
            else if(map[x][y][z-1]!=null){
                Spatial isthisEnterance= map[x][y][z - 1];
                if(isthisEnterance.getUserData("type").equals("queroad")){
                    connectedroad=isthisEnterance;
                }
            }
            
        }else{
            connectedroad=lastqueroad;
        }
        
        //käännä tiet
        
        /**
         * Yhdistä tiet
         */
        
        if(connectedroad!=null){
            road.setUserData("queconnect1",connectedroad);
            connectedroad.setUserData("queconnect2",road);
            System.out.println(road.getUserData("queconnect1").toString());
            System.out.println(connectedroad.getUserData("queconnect2").toString());
            //connectedroad.setUserData("connected",true);
        }
        //jos löytyy enterance yhdistä siihen jos ei niin 
        
        lastqueroad=road;
        
    }
}
