/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.terrain;

import com.jme3.asset.AssetManager;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import mygame.ride.Enterance;

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
        } else {
            switch (hill) {

                case FLAT:
                    road = roadF.queroadStraight();
                    road.setUserData("connected",false);

                    break;

                case DOWN:
                    road = roadF.queroadDownHill();
                    road.setUserData("connected",false);
                    break;

                case UP:
                    road = roadF.queroadUpHill();
                    road.setUserData("connected",false);
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
            if (queroad == false) {
                if (!test.getUserData("type").equals("road")) {
                    return;
                }
            } else {
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
        if (queroad == false) {
            updateroads(tempx, tempy, tempz);
            lastqueroad = null;
        } else {
            updateQueroad(tempx, tempy, tempz, road);
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
        lastqueroad = null;
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
                if(temp.getUserData("type").equals("queroad")){
                    boolean connected=true;
                    connected=temp.getUserData("connected");
                    if(connected==false){
                        condition1=true;
                    }
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
                if(temp.getUserData("type").equals("queroad")){
                    boolean connected=true;
                    connected=temp.getUserData("connected");
                    if(connected==false){
                        condition2=true;
                    }
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
                if(temp.getUserData("type").equals("queroad")){
                    boolean connected=true;
                    connected=temp.getUserData("connected");
                    if(connected==false){
                        condition3=true;
                    }
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
                if(temp.getUserData("type").equals("queroad")){
                    boolean connected=true;
                    connected=temp.getUserData("connected");
                    if(connected==false){
                        condition4=true;
                    }
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

    private void updateQueroad(int x, int y, int z, Spatial road) {
        Spatial connectedroad = null;
        int x2 = 0;
        int y2 = 0;
        int z2 = 0;

        /**
         * Etsitään quetienpalasia jos tie on katkennut
         */
        if (lastqueroad == null) {

            boolean found = false;

            if (map[x + 1][y][z] != null) {
                Spatial isthisEnterance = map[x + 1][y][z];
                if (isthisEnterance.getUserData("type").equals("queroad")) {
                    boolean connected = true;
                    if (isthisEnterance.getUserData("connected") != null) {
                        connected = isthisEnterance.getUserData("connected");
                    }
                    if (connected == false) {
                        found = true;
                        connectedroad = isthisEnterance;

                    }
                }
            }
            if (map[x - 1][y][z] != null && found == false) {
                Spatial isthisEnterance = map[x - 1][y][z];
                if (isthisEnterance.getUserData("type").equals("queroad")) {
                    boolean connected = true;
                    if (isthisEnterance.getUserData("connected") != null) {
                        connected = isthisEnterance.getUserData("connected");
                    }
                    if (connected == false) {
                        found = true;
                        connectedroad = isthisEnterance;
                    }
                }
            }
            if (map[x][y][z + 1] != null && found == false) {
                Spatial isthisEnterance = map[x][y][z + 1];
                if (isthisEnterance.getUserData("type").equals("queroad")) {
                    boolean connected = true;
                    if (isthisEnterance.getUserData("connected") != null) {
                        connected = isthisEnterance.getUserData("connected");
                    }
                    if (connected == false) {
                        found = true;
                        connectedroad = isthisEnterance;
                    }
                }
            }
            if (map[x][y][z - 1] != null && found == false) {
                Spatial isthisEnterance = map[x][y][z - 1];
                if (isthisEnterance.getUserData("type").equals("queroad")) {
                    boolean connected = true;
                    if (isthisEnterance.getUserData("connected") != null) {
                        connected = isthisEnterance.getUserData("connected");
                    }
                    if (connected == false) {
                        connectedroad = isthisEnterance;
                    }
                }
            }

        } else {
            connectedroad = lastqueroad;
        }



        /**
         * Yhdistä tiet
         */
        if (connectedroad != null) {
            x2 = (int) connectedroad.getLocalTranslation().x;
            y2 = (int) connectedroad.getLocalTranslation().y;
            z2 = (int) connectedroad.getLocalTranslation().z;


            road.setUserData("queconnect1", connectedroad);
            road.setUserData("connected", false);
            connectedroad.setUserData("queconnect2", road);
            System.out.println(road.getUserData("queconnect1").toString());
            System.out.println(connectedroad.getUserData("queconnect2").toString());
            if (connectedroad.getUserData("queconnect1") != null) {
                connectedroad.setUserData("connected", true);
            }


        }
        turnqueroads(connectedroad, x2, y2, z2, road, x, y, z);
        
        if(!checkforEnterance(x, y, z, road)){
            lastqueroad = road;
        }else{
            lastqueroad =null;
        }
 


    }
    private boolean checkforEnterance(int x,int y,int z,Spatial road){
        Spatial temp=map[x+1][y][z];
        if(temp!=null){
            if(temp.getUserData("type").equals("enterance")){
                Enterance enterance=temp.getUserData("enterance");
                if(enterance.facing==Direction.DOWN&&enterance.connected==false){
                    enterance.connectedRoad=road;
                    enterance.connected=true;
                    road.setUserData("connectedEnterance",enterance);
                    road.setUserData("connected",true);
                    return true;
                }
            }
        }
        temp=map[x-1][y][z];
        if(temp!=null){
            if(temp.getUserData("type").equals("enterance")){
                Enterance enterance=temp.getUserData("enterance");
                if(enterance.facing==Direction.UP&&enterance.connected==false){
                    enterance.connectedRoad=road;
                    enterance.connected=true;
                    road.setUserData("connectedEnterance",enterance);
                    road.setUserData("connected",true);
                    return true;
                }
            }
        }
        temp=map[x][y][z+1];
        if(temp!=null){
            if(temp.getUserData("type").equals("enterance")){
                Enterance enterance=temp.getUserData("enterance");
                if(enterance.facing==Direction.RIGHT&&enterance.connected==false){
                    enterance.connectedRoad=road;
                    enterance.connected=true;
                    road.setUserData("connectedEnterance",enterance);
                    road.setUserData("connected",true);
                    return true;
                }
            }
        }
        temp=map[x][y][z-1];
        if(temp!=null){
            if(temp.getUserData("type").equals("enterance")){
                Enterance enterance=temp.getUserData("enterance");
                if(enterance.facing==Direction.LEFT&&enterance.connected==false){
                    enterance.connectedRoad=road;
                    enterance.connected=true;
                    road.setUserData("connectedEnterance",enterance);
                    road.setUserData("connected",true);
                    return true;
                }
            }
        }
        return false;
    }
    
    //real shitty code
    private void turnqueroads(Spatial connectedroad, int x1, int y1, int z1, Spatial road, int x2, int y2, int z2) {
        if (connectedroad == null) {
            return;
        }
        
        boolean connected = false;
        if (connectedroad.getUserData("connected") != null) {
            connected = connectedroad.getUserData("connected");
        }
        
        if (connected == true) {
            Spatial connected1 = connectedroad.getUserData("queconnect1");
            Spatial connected2 = road;

            Vector3f connected1loc = connected1.getLocalTranslation();
            Vector3f connected2loc = connected2.getLocalTranslation();

            if (connected1loc.x > connected2loc.x && connected1loc.z == connected2loc.z) {
                Spatial temp = map[x1][y1][z1];
                rootNode.detachChild(temp);
                temp = roadF.queroadStraight();
                temp.setLocalTranslation(connectedroad.getLocalTranslation());
                temp.setUserData("connected", true);
                temp.setUserData("queconnect1", connected1);
                temp.setUserData("queconnect2", connected2);
                map[x1][y1][z1] = temp;
                rootNode.attachChild(temp);
                return;
            }
            if (connected1loc.x < connected2loc.x && connected1loc.z == connected2loc.z) {
                Spatial temp = map[x1][y1][z1];
                rootNode.detachChild(temp);
                temp = roadF.queroadStraight();
                temp.setLocalTranslation(connectedroad.getLocalTranslation());
                temp.setUserData("connected", true);
                temp.setUserData("queconnect1", connected1);
                temp.setUserData("queconnect2", connected2);
                map[x1][y1][z1] = temp;
                rootNode.attachChild(temp);
                return;
            }
            if (connected1loc.x == connected2loc.x && connected1loc.z < connected2loc.z) {
                Spatial temp = map[x1][y1][z1];
                rootNode.detachChild(temp);
                temp = roadF.queroadStraight();
                float angle = (float) Math.toRadians(90);
                temp.rotate(0, angle, 0);
                temp.setLocalTranslation(connectedroad.getLocalTranslation());
                temp.setUserData("connected", true);
                temp.setUserData("queconnect1", connected1);
                temp.setUserData("queconnect2", connected2);
                map[x1][y1][z1] = temp;
                rootNode.attachChild(temp);
                return;
            }
            if (connected1loc.x == connected2loc.x && connected1loc.z > connected2loc.z) {
                Spatial temp = map[x1][y1][z1];
                rootNode.detachChild(temp);
                temp = roadF.queroadStraight();
                float angle = (float) Math.toRadians(90);
                temp.rotate(0, angle, 0);
                temp.setLocalTranslation(connectedroad.getLocalTranslation());
                temp.setUserData("connected", true);
                temp.setUserData("queconnect1", connected1);
                temp.setUserData("queconnect2", connected2);
                map[x1][y1][z1] = temp;
                rootNode.attachChild(temp);
                return;
            }
            if (connected1loc.x > connected2loc.x && connected1loc.z > connected2loc.z) {
                if (connectedroad.getLocalTranslation().z < connected1loc.z) {
                    Spatial temp = map[x1][y1][z1];
                    rootNode.detachChild(temp);
                    temp = roadF.quebendingRoad();
                    float angle = (float) Math.toRadians(-90);
                    temp.rotate(0, angle, 0);
                    temp.setLocalTranslation(connectedroad.getLocalTranslation());
                    temp.setUserData("connected", true);
                    temp.setUserData("queconnect1", connected1);
                    temp.setUserData("queconnect2", connected2);
                    map[x1][y1][z1] = temp;
                    rootNode.attachChild(temp);
                } else {
                    Spatial temp = map[x1][y1][z1];
                    rootNode.detachChild(temp);
                    temp = roadF.quebendingRoad();
                    float angle = (float) Math.toRadians(90);
                    temp.rotate(0, angle, 0);
                    temp.setLocalTranslation(connectedroad.getLocalTranslation());
                    temp.setUserData("connected", true);
                    temp.setUserData("queconnect1", connected1);
                    temp.setUserData("queconnect2", connected2);
                    map[x1][y1][z1] = temp;
                    rootNode.attachChild(temp);
                }

                return;
            }
            if (connected1loc.x < connected2loc.x && connected1loc.z > connected2loc.z) {
                if (connectedroad.getLocalTranslation().z < connected1loc.z) {
                    Spatial temp = map[x1][y1][z1];
                    rootNode.detachChild(temp);
                    temp = roadF.quebendingRoad();
                    
                    temp.setLocalTranslation(connectedroad.getLocalTranslation());
                    temp.setUserData("connected", true);
                    temp.setUserData("queconnect1", connected1);
                    temp.setUserData("queconnect2", connected2);
                    map[x1][y1][z1] = temp;
                    rootNode.attachChild(temp);
                }else{
                    Spatial temp = map[x1][y1][z1];
                    rootNode.detachChild(temp);
                    temp = roadF.quebendingRoad();
                    float angle = (float) Math.toRadians(180);
                    temp.rotate(0, angle, 0);
                    temp.setLocalTranslation(connectedroad.getLocalTranslation());
                    temp.setUserData("connected", true);
                    temp.setUserData("queconnect1", connected1);
                    temp.setUserData("queconnect2", connected2);
                    map[x1][y1][z1] = temp;
                    rootNode.attachChild(temp);
                }
               
                return;
            }
            if (connected1loc.x > connected2loc.x && connected1loc.z < connected2loc.z) {
                if (connectedroad.getLocalTranslation().x < connected1loc.x) {
                    Spatial temp = map[x1][y1][z1];
                    rootNode.detachChild(temp);
                    temp = roadF.quebendingRoad();
                    
                    temp.setLocalTranslation(connectedroad.getLocalTranslation());
                    temp.setUserData("connected", true);
                    temp.setUserData("queconnect1", connected1);
                    temp.setUserData("queconnect2", connected2);
                    map[x1][y1][z1] = temp;
                    rootNode.attachChild(temp);
                }else{
                    Spatial temp = map[x1][y1][z1];
                    rootNode.detachChild(temp);
                    temp = roadF.quebendingRoad();
                    float angle = (float) Math.toRadians(-180);
                    temp.rotate(0, angle, 0);
                    temp.setLocalTranslation(connectedroad.getLocalTranslation());
                    temp.setUserData("connected", true);
                    temp.setUserData("queconnect1", connected1);
                    temp.setUserData("queconnect2", connected2);
                    map[x1][y1][z1] = temp;
                    rootNode.attachChild(temp);
                }
                return;
            }
            if (connected1loc.x < connected2loc.x && connected1loc.z < connected2loc.z) {
               if (connectedroad.getLocalTranslation().z > connected1loc.z) {
                    Spatial temp = map[x1][y1][z1];
                    rootNode.detachChild(temp);
                    temp = roadF.quebendingRoad();
                    float angle = (float) Math.toRadians(90);
                    temp.rotate(0, angle, 0);
                    temp.setLocalTranslation(connectedroad.getLocalTranslation());
                    temp.setUserData("connected", true);
                    temp.setUserData("queconnect1", connected1);
                    temp.setUserData("queconnect2", connected2);
                    map[x1][y1][z1] = temp;
                    rootNode.attachChild(temp);
                }else{//this
                    Spatial temp = map[x1][y1][z1];
                    rootNode.detachChild(temp);
                    temp = roadF.quebendingRoad();
                    float angle = (float) Math.toRadians(-90);
                    temp.rotate(0, angle, 0);
                    temp.setLocalTranslation(connectedroad.getLocalTranslation());
                    temp.setUserData("connected", true);
                    temp.setUserData("queconnect1", connected1);
                    temp.setUserData("queconnect2", connected2);
                    map[x1][y1][z1] = temp;
                    rootNode.attachChild(temp);
                }
                
            }

        }
        else{
            Vector3f connected1loc = connectedroad.getLocalTranslation();
            Vector3f recentroadloc = road.getLocalTranslation();
            int x3=(int)connected1loc.x;
            int y3=(int)connected1loc.y;
            int z3=(int)connected1loc.z;
            if (connected1loc.x > recentroadloc.x && connected1loc.z == recentroadloc.z || connected1loc.x < recentroadloc.x && connected1loc.z == recentroadloc.z) {
                Spatial temp = map[x3][y3][z3];
                rootNode.detachChild(temp);
                temp = roadF.queroadStraight();
                temp.setLocalTranslation(connectedroad.getLocalTranslation());
                temp.setUserData("connected", false);
                temp.setUserData("queconnect1", road);
                map[x3][y3][z3] = temp;
                rootNode.attachChild(temp);
                return;
            }
            if (connected1loc.x == recentroadloc.x && connected1loc.z < recentroadloc.z || connected1loc.x < recentroadloc.x && connected1loc.z == recentroadloc.z) {
                Spatial temp = map[x3][y3][z3];
                rootNode.detachChild(temp);
                temp = roadF.queroadStraight();
                temp.setLocalTranslation(connectedroad.getLocalTranslation());
                float angle = (float) Math.toRadians(90);
                temp.rotate(0, angle, 0);
                temp.setUserData("connected", false);
                temp.setUserData("queconnect1", road);
                map[x3][y3][z3] = temp;
                rootNode.attachChild(temp);
                
            }
        }

        
    }
}
