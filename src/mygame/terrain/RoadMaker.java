/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.terrain;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.jme3.asset.AssetManager;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import java.util.ArrayList;
import mygame.GUI.UpdateMoneyTextBarEvent;
import mygame.Gamestate;
import mygame.Main;
import mygame.ride.Enterance;

/**
 *
 * @author arska
 */
@Singleton
public class RoadMaker {

    public Direction direction = Direction.UP;
    public RoadHill hill = RoadHill.FLAT;
    public RoadMakerStatus status = RoadMakerStatus.BUILDING;
    public Vector3f startingPosition;
    private final AssetManager assetManager;
    private final Node rootNode;
    public boolean queroad = false;
    //X,Y,Z
    private MapContainer map;
    public boolean change = true;
    public RoadFactory roadF;
    private Spatial lastqueroad;
    public ArrayList<Spatial>roads=new ArrayList<Spatial>();
    public int ID=1;
    @Inject private ParkHandler parkHandler;
    private final EventBus eventBus;
    @Inject
    public RoadMaker(AssetManager assetManager, Node rootNode ,MapContainer map,EventBus eventBus) {
        this.assetManager = assetManager;
        this.rootNode = rootNode;
        this.eventBus=eventBus;
        
        this.map=map;
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
        if(!parkHandler.getParkWallet().canAfford(10)){
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
                    road.setUserData("connected", false);

                    break;

                case DOWN:
                    road = roadF.queroadDownHill();
                    road.setUserData("connected", false);
                    break;

                case UP:
                    road = roadF.queroadUpHill();
                    road.setUserData("connected", false);
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
                        road.setUserData("direction","UP");

                    }

                    break;

                case DOWN:
                    road.setUserData("direction","DOWN");

                    break;

                case RIGHT:

                    float angle = (float) Math.toRadians(90);
                    road.rotate(0, angle, 0);
                    road.setUserData("direction","RIGHT");
                    break;

                case LEFT:

                    float anglet = (float) Math.toRadians(-90);
                    road.rotate(0, anglet, 0);
                    road.setUserData("direction","LEFT");
                    break;
            }
        }

        road.move(calcRoadPosition());

        /**
         * katsoo jos siinä kohdassa johon tietä rakennetaan on jotain mapissa
         * jos se on tienpala niin se poistaa sen
         */
        if (map.getMap()[(int) calcRoadPosition().x][(int) calcRoadPosition().y][(int) calcRoadPosition().z] != null) {
            Spatial test = map.getMap()[(int) calcRoadPosition().x][(int) calcRoadPosition().y][(int) calcRoadPosition().z];
            if (queroad == false) {
                if (!test.getUserData("type").equals("road")) {
                    return;
                }
            } else {
                if (!test.getUserData("type").equals("queroad")) {
                    return;
                }
            }

            Spatial temproad = map.getMap()[(int) calcRoadPosition().x][(int) calcRoadPosition().y][(int) calcRoadPosition().z];

            if (temproad.getUserData("roadHill").equals("upHill")) {
                map.getMap()[(int) calcRoadPosition().x][(int) calcRoadPosition().y + 1][(int) calcRoadPosition().z] = null;
            }

            if (temproad.getUserData("roadHill").equals("downHill")) {
                map.getMap()[(int) calcRoadPosition().x][(int) calcRoadPosition().y - 1][(int) calcRoadPosition().z] = null;
            }

            rootNode.detachChild(temproad);
        }

        map.getMap()[(int) calcRoadPosition().x][(int) calcRoadPosition().y][(int) calcRoadPosition().z] = road;

        if (road.getUserData("roadHill").equals("upHill")) {

            map.getMap()[(int) calcRoadPosition().x][(int) calcRoadPosition().y + 1][(int) calcRoadPosition().z] = road;
        }

        if (road.getUserData("roadHill").equals("downHill")) {

            map.getMap()[(int) calcRoadPosition().x][(int) calcRoadPosition().y - 1][(int) calcRoadPosition().z] = road;
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
        parkHandler.getParkWallet().remove(10);
        eventBus.post(new UpdateMoneyTextBarEvent());
        rootNode.attachChild(road);
        //roads.add(road);

        int tempx = (int) pyorista(startingPosition).x;
        int tempz = (int) pyorista(startingPosition).z;
        int tempy = (int) (startingPosition).y;
        if (queroad == false) {
            updateroads(tempx, tempy, tempz);
            lastqueroad = null;
        } else {
            road.setUserData("roadID",ID);
            ID++;
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
            if (map.getMap()[x + 1][z][y] != null) {
                Spatial temp = map.getMap()[x + 1][z][y];
                if (temp.getUserData("type").equals("road")) {
                    condition1 = true;

                }
                if (temp.getUserData("type").equals("shop")) {
                    condition1 = true;

                }
                if (temp.getUserData("type").equals("queroad")) {
                    boolean connected = true;
                    connected = temp.getUserData("connected");
                    if (connected == false) {
                        condition1 = true;
                    }
                }
            }
        } else {
            if (map.getMap()[x + 1][z][y] == null) {
                condition1 = true;
            } else {
                Spatial temp = map.getMap()[x + 1][z][y];
                if (!temp.getUserData("type").equals("road")) {
                    condition1 = true;
                }

            }
        }


        //2 condition
        if (con2 == true) {
            if (map.getMap()[x - 1][z][y] != null) {
                Spatial temp = map.getMap()[x - 1][z][y];
                if (temp.getUserData("type").equals("road")) {
                    condition2 = true;

                }
                if (temp.getUserData("type").equals("shop")) {
                    condition2 = true;

                }
                if (temp.getUserData("type").equals("queroad")) {
                    boolean connected = true;
                    connected = temp.getUserData("connected");
                    if (connected == false) {
                        condition2 = true;
                    }
                }
            }
        } else {
            if (map.getMap()[x - 1][z][y] == null) {
                condition2 = true;
            } else {
                Spatial temp = map.getMap()[x - 1][z][y];
                if (!temp.getUserData("type").equals("road")) {
                    condition2 = true;
                }

            }
        }
        //3 condition
        if (con3 == true) {
            if (map.getMap()[x][z][y + 1] != null) {
                Spatial temp = map.getMap()[x][z][y + 1];
                if (temp.getUserData("type").equals("road")) {
                    condition3 = true;

                }
                if (temp.getUserData("type").equals("shop")) {
                    condition3 = true;

                }
                if (temp.getUserData("type").equals("queroad")) {
                    boolean connected = true;
                    connected = temp.getUserData("connected");
                    if (connected == false) {
                        condition3 = true;
                    }
                }
            }
        } else {
            if (map.getMap()[x][z][y + 1] == null) {
                condition3 = true;
            } else {
                Spatial temp = map.getMap()[x][z][y + 1];
                if (!temp.getUserData("type").equals("road")) {
                    condition3 = true;
                }
            }
        }
        //4 condition
        if (con4 == true) {
            if (map.getMap()[x][z][y - 1] != null) {
                Spatial temp = map.getMap()[x][z][y - 1];
                if (temp.getUserData("type").equals("road")) {
                    condition4 = true;

                }
                if (temp.getUserData("type").equals("shop")) {
                    condition1 = true;

                }
                if (temp.getUserData("type").equals("queroad")) {
                    boolean connected = true;
                    connected = temp.getUserData("connected");
                    if (connected == false) {
                        condition4 = true;
                    }
                }
            }
        } else {
            if (map.getMap()[x][z][y - 1] == null) {
                condition4 = true;
            } else {
                Spatial temp = map.getMap()[x][z][y - 1];
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

        if (map.getMap()[x][z][y] == null || !map.getMap()[x][z][y].getUserData("type").equals("road") || map.getMap()[x][z][y].getUserData("roadHill").equals("upHill") || map.getMap()[x][z][y].getUserData("roadHill").equals("downHill")) {
            return;
        }

        if (roadtypeCondition(x, z, y, true, true, true, true)) {
            Spatial temp = map.getMap()[x][z][y];
            if (temp != null) {
                rootNode.detachChild(temp);
            }
            Spatial roadp = roadF.centerRoad();
            roadp.move(x, z + 0.1f, y);
            map.getMap()[x][z][y] = roadp;
            rootNode.attachChild(roadp);

            return;
        }

        if (roadtypeCondition(x, z, y, true, true, true, false)) {
            Spatial temp = map.getMap()[x][z][y];
            if (temp != null) {
                rootNode.detachChild(temp);
            }
            Spatial roadp = roadF.tRoad();
            roadp.move(x, z + 0.1f, y);
            float angle = (float) Math.toRadians(90);
            roadp.rotate(0, angle, 0);
            map.getMap()[x][z][y] = roadp;
            rootNode.attachChild(roadp);



            return;
        }

        if (roadtypeCondition(x, z, y, false, true, true, true)) {
            Spatial temp = map.getMap()[x][z][y];
            if (temp != null) {
                rootNode.detachChild(temp);
            }
            Spatial roadp = roadF.tRoad();
            roadp.move(x, z + 0.1f, y);

            map.getMap()[x][z][y] = roadp;
            rootNode.attachChild(roadp);



            return;
        }

        if (roadtypeCondition(x, z, y, true, true, false, true)) {
            Spatial temp = map.getMap()[x][z][y];
            if (temp != null) {
                rootNode.detachChild(temp);
            }
            Spatial roadp = roadF.tRoad();
            roadp.move(x, z + 0.1f, y);
            float angle = (float) Math.toRadians(-90);
            roadp.rotate(0, angle, 0);
            map.getMap()[x][z][y] = roadp;
            rootNode.attachChild(roadp);



            return;
        }

        if (roadtypeCondition(x, z, y, true, false, true, true)) {
            Spatial temp = map.getMap()[x][z][y];
            if (temp != null) {
                rootNode.detachChild(temp);
            }
            Spatial roadp = roadF.tRoad();
            roadp.move(x, z + 0.1f, y);
            float angle = (float) Math.toRadians(180);
            roadp.rotate(0, angle, 0);
            map.getMap()[x][z][y] = roadp;
            rootNode.attachChild(roadp);



            return;
        }
        if (roadtypeCondition(x, z, y, false, true, false, true)) {
            Spatial temp = map.getMap()[x][z][y];
            if (temp != null) {
                rootNode.detachChild(temp);
            }
            Spatial roadp = roadF.bendingRoad();
            roadp.move(x, z + 0.1f, y);
            float angle = (float) Math.toRadians(180);
            roadp.rotate(0, angle, 0);
            map.getMap()[x][z][y] = roadp;
            rootNode.attachChild(roadp);



            return;
        }
        
        if (roadtypeCondition(x, z, y, true, false, false, true)) {
            Spatial temp = map.getMap()[x][z][y];
            if (temp != null) {
                rootNode.detachChild(temp);
            }
            Spatial roadp = roadF.bendingRoad();
            roadp.move(x, z + 0.1f, y);
            float angle = (float) Math.toRadians(90);
            roadp.rotate(0, angle, 0);
            map.getMap()[x][z][y] = roadp;
            rootNode.attachChild(roadp);



            return;
        }
        if (roadtypeCondition(x, z, y, true, false, true, false)) {
            Spatial temp = map.getMap()[x][z][y];
            if (temp != null) {
                rootNode.detachChild(temp);
            }
            Spatial roadp = roadF.bendingRoad();
            roadp.move(x, z + 0.1f, y);

            map.getMap()[x][z][y] = roadp;
            rootNode.attachChild(roadp);



            return;
        }
        if (roadtypeCondition(x, z, y, false, true, true, false)) {
            Spatial temp = map.getMap()[x][z][y];
            if (temp != null) {
                rootNode.detachChild(temp);
            }
            Spatial roadp = roadF.bendingRoad();
            roadp.move(x, z + 0.1f, y);
            float angle = (float) Math.toRadians(-90);
            roadp.rotate(0, angle, 0);
            map.getMap()[x][z][y] = roadp;
            rootNode.attachChild(roadp);



            return;
        }
        if (roadtypeCondition(x, z, y, true, false, false, false)) {
            Spatial temp = map.getMap()[x][z][y];
            if (temp != null) {
                rootNode.detachChild(temp);
            }
            Spatial roadp = roadF.roadStraight();
            roadp.move(x, z + 0.1f, y);

            map.getMap()[x][z][y] = roadp;
            rootNode.attachChild(roadp);



            return;
        }
        if (roadtypeCondition(x, z, y, false, false, true, false)) {
            Spatial temp = map.getMap()[x][z][y];
            if (temp != null) {
                rootNode.detachChild(temp);
            }
            Spatial roadp = roadF.roadStraight();
            roadp.move(x, z + 0.1f, y);
            float angle = (float) Math.toRadians(90);
            roadp.rotate(0, angle, 0);
            map.getMap()[x][z][y] = roadp;
            rootNode.attachChild(roadp);



            return;
        }
        if (roadtypeCondition(x, z, y, false, true, false, false)) {
            Spatial temp = map.getMap()[x][z][y];
            if (temp != null) {
                rootNode.detachChild(temp);
            }
            Spatial roadp = roadF.roadStraight();
            roadp.move(x, z + 0.1f, y);
            map.getMap()[x][z][y] = roadp;
            rootNode.attachChild(roadp);



            return;
        }
        if (roadtypeCondition(x, z, y, false, false, false, true)) {
            Spatial temp = map.getMap()[x][z][y];
            if (temp != null) {
                rootNode.detachChild(temp);
            }
            Spatial roadp = roadF.roadStraight();
            roadp.move(x, z + 0.1f, y);
            float angle = (float) Math.toRadians(90);
            roadp.rotate(0, angle, 0);
            map.getMap()[x][z][y] = roadp;
            rootNode.attachChild(roadp);




        }
        if (roadtypeCondition(x, z, y, false, false,true, true)) {
            Spatial temp = map.getMap()[x][z][y];
            if (temp != null) {
                rootNode.detachChild(temp);
            }
            Spatial roadp = roadF.roadStraight();
            roadp.move(x, z + 0.1f, y);
            float angle = (float) Math.toRadians(90);
            roadp.rotate(0, angle, 0);
            map.getMap()[x][z][y] = roadp;
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

            if (map.getMap()[x + 1][y][z] != null) {
                Spatial isthisEnterance = map.getMap()[x + 1][y][z];
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
            if (map.getMap()[x - 1][y][z] != null && found == false) {
                Spatial isthisEnterance = map.getMap()[x - 1][y][z];
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
            if (map.getMap()[x][y][z + 1] != null && found == false) {
                Spatial isthisEnterance = map.getMap()[x][y][z + 1];
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
            if (map.getMap()[x][y][z - 1] != null && found == false) {
                Spatial isthisEnterance = map.getMap()[x][y][z - 1];
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

        if (!checkforEnterance(x, y, z, road)) {
            lastqueroad = road;
        } else {
            lastqueroad = null;
        }



    }

    private boolean checkforEnterance(int x, int y, int z, Spatial road) {
        Spatial temp = map.getMap()[x + 1][y][z];
        if (temp != null) {
            if (temp.getUserData("type").equals("enterance")) {
                Enterance enterance = temp.getUserData("enterance");
                if (enterance.facing == Direction.DOWN && enterance.connected == false) {
                    enterance.connectedRoad = road;
                    enterance.connected = true;
                    road.setUserData("connectedEnterance", enterance);
                    road.setUserData("connected", true);
                    refreshqueroadarray(road);
                    return true;
                }
            }
        }
        temp = map.getMap()[x - 1][y][z];
        if (temp != null) {
            if (temp.getUserData("type").equals("enterance")) {
                Enterance enterance = temp.getUserData("enterance");
                if (enterance.facing == Direction.UP && enterance.connected == false) {
                    enterance.connectedRoad = road;
                    enterance.connected = true;
                    road.setUserData("connectedEnterance", enterance);
                    road.setUserData("connected", true);
                    refreshqueroadarray(road);
                    return true;
                }
            }
        }
        temp = map.getMap()[x][y][z + 1];
        if (temp != null) {
            if (temp.getUserData("type").equals("enterance")) {
                Enterance enterance = temp.getUserData("enterance");
                if (enterance.facing == Direction.RIGHT && enterance.connected == false) {
                    enterance.connectedRoad = road;
                    enterance.connected = true;
                    road.setUserData("connectedEnterance", enterance);
                    road.setUserData("connected", true);
                    refreshqueroadarray(road);
                    return true;
                }
            }
        }
        temp = map.getMap()[x][y][z - 1];
        if (temp != null) {
            if (temp.getUserData("type").equals("enterance")) {
                Enterance enterance = temp.getUserData("enterance");
                if (enterance.facing == Direction.LEFT && enterance.connected == false) {
                    enterance.connectedRoad = road;
                    enterance.connected = true;
                    road.setUserData("connectedEnterance", enterance);
                    road.setUserData("connected", true);
                    refreshqueroadarray(road);
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
        String direction0="UP";
        if(connectedroad.getUserData("direction")!=null){
            direction0=connectedroad.getUserData("direction");
        }
        
        if (connected == true) {
            Spatial connected1 = null;
            if(connectedroad.getUserData("queconnect1")!=null){
                connected1 = connectedroad.getUserData("queconnect1");
            }
            else if(connectedroad.getUserData("queconnect2")!=null){
                connected1=connectedroad.getUserData("queconnect2");
            }else{
                //BUG
                System.out.println("BUG");
            }
            
            Spatial connected2 = road;
            int IDr=connectedroad.getUserData("roadID");
            

            Vector3f connected1loc = connected1.getLocalTranslation();
            Vector3f connected2loc = connected2.getLocalTranslation();

            if (connected1loc.x > connected2loc.x && connected1loc.z == connected2loc.z) {
                Spatial temp = map.getMap()[x1][y1][z1];
                rootNode.detachChild(temp);
                temp = roadF.queroadStraight();
                temp.setLocalTranslation(connectedroad.getLocalTranslation());
                temp.setUserData("connected", true);
                temp.setUserData("queconnect1", connected1);
                temp.setUserData("queconnect2", connected2);
                temp.setUserData("roadID",IDr);
                temp.setUserData("direction",direction0);
                map.getMap()[x1][y1][z1] = temp;
                rootNode.attachChild(temp);
                return;
            }
            if (connected1loc.x < connected2loc.x && connected1loc.z == connected2loc.z) {
                Spatial temp = map.getMap()[x1][y1][z1];
                rootNode.detachChild(temp);
                temp = roadF.queroadStraight();
                temp.setLocalTranslation(connectedroad.getLocalTranslation());
                temp.setUserData("connected", true);
                temp.setUserData("queconnect1", connected1);
                temp.setUserData("queconnect2", connected2);
                temp.setUserData("roadID",IDr);
                temp.setUserData("direction",direction0);
                map.getMap()[x1][y1][z1] = temp;
                rootNode.attachChild(temp);
                return;
            }
            if (connected1loc.x == connected2loc.x && connected1loc.z < connected2loc.z) {
                Spatial temp = map.getMap()[x1][y1][z1];
                rootNode.detachChild(temp);
                temp = roadF.queroadStraight();
                float angle = (float) Math.toRadians(90);
                temp.rotate(0, angle, 0);
                temp.setLocalTranslation(connectedroad.getLocalTranslation());
                temp.setUserData("connected", true);
                temp.setUserData("queconnect1", connected1);
                temp.setUserData("queconnect2", connected2);
                temp.setUserData("roadID",IDr);
                temp.setUserData("direction",direction0);
                map.getMap()[x1][y1][z1] = temp;
                rootNode.attachChild(temp);
                return;
            }
            if (connected1loc.x == connected2loc.x && connected1loc.z > connected2loc.z) {
                Spatial temp = map.getMap()[x1][y1][z1];
                rootNode.detachChild(temp);
                temp = roadF.queroadStraight();
                float angle = (float) Math.toRadians(90);
                temp.rotate(0, angle, 0);
                temp.setLocalTranslation(connectedroad.getLocalTranslation());
                temp.setUserData("connected", true);
                temp.setUserData("queconnect1", connected1);
                temp.setUserData("queconnect2", connected2);
                temp.setUserData("roadID",IDr);
                temp.setUserData("direction",direction0);
                map.getMap()[x1][y1][z1] = temp;
                rootNode.attachChild(temp);
                return;
            }
            if (connected1loc.x > connected2loc.x && connected1loc.z > connected2loc.z) {
                if (connectedroad.getLocalTranslation().z < connected1loc.z) {
                    Spatial temp = map.getMap()[x1][y1][z1];
                    rootNode.detachChild(temp);
                    temp = roadF.quebendingRoad();
                    float angle = (float) Math.toRadians(-90);
                    temp.rotate(0, angle, 0);
                    temp.setLocalTranslation(connectedroad.getLocalTranslation());
                    temp.setUserData("connected", true);
                    temp.setUserData("queconnect1", connected1);
                    temp.setUserData("queconnect2", connected2);
                    temp.setUserData("roadID",IDr);
                    temp.setUserData("direction",direction0);
                    map.getMap()[x1][y1][z1] = temp;
                    rootNode.attachChild(temp);
                } else {
                    Spatial temp = map.getMap()[x1][y1][z1];
                    rootNode.detachChild(temp);
                    temp = roadF.quebendingRoad();
                    float angle = (float) Math.toRadians(90);
                    temp.rotate(0, angle, 0);
                    temp.setLocalTranslation(connectedroad.getLocalTranslation());
                    temp.setUserData("connected", true);
                    temp.setUserData("queconnect1", connected1);
                    temp.setUserData("queconnect2", connected2);
                    temp.setUserData("roadID",IDr);
                    temp.setUserData("direction",direction0);
                    map.getMap()[x1][y1][z1] = temp;
                    rootNode.attachChild(temp);
                }

                return;
            }
            if (connected1loc.x < connected2loc.x && connected1loc.z > connected2loc.z) {
                if (connectedroad.getLocalTranslation().z < connected1loc.z) {
                    Spatial temp = map.getMap()[x1][y1][z1];
                    rootNode.detachChild(temp);
                    temp = roadF.quebendingRoad();

                    temp.setLocalTranslation(connectedroad.getLocalTranslation());
                    temp.setUserData("connected", true);
                    temp.setUserData("queconnect1", connected1);
                    temp.setUserData("queconnect2", connected2);
                    temp.setUserData("roadID",IDr);
                    temp.setUserData("direction",direction0);
                    map.getMap()[x1][y1][z1] = temp;
                    rootNode.attachChild(temp);
                } else {
                    Spatial temp = map.getMap()[x1][y1][z1];
                    rootNode.detachChild(temp);
                    temp = roadF.quebendingRoad();
                    float angle = (float) Math.toRadians(180);
                    temp.rotate(0, angle, 0);
                    temp.setLocalTranslation(connectedroad.getLocalTranslation());
                    temp.setUserData("connected", true);
                    temp.setUserData("queconnect1", connected1);
                    temp.setUserData("queconnect2", connected2);
                    temp.setUserData("roadID",IDr);
                    temp.setUserData("direction",direction0);
                    map.getMap()[x1][y1][z1] = temp;
                    rootNode.attachChild(temp);
                }

                return;
            }
            if (connected1loc.x > connected2loc.x && connected1loc.z < connected2loc.z) {
                if (connectedroad.getLocalTranslation().x < connected1loc.x) {
                    Spatial temp = map.getMap()[x1][y1][z1];
                    rootNode.detachChild(temp);
                    temp = roadF.quebendingRoad();

                    temp.setLocalTranslation(connectedroad.getLocalTranslation());
                    temp.setUserData("connected", true);
                    temp.setUserData("queconnect1", connected1);
                    temp.setUserData("queconnect2", connected2);
                    temp.setUserData("roadID",IDr);
                    temp.setUserData("direction",direction0);
                    map.getMap()[x1][y1][z1] = temp;
                    rootNode.attachChild(temp);
                } else {
                    Spatial temp = map.getMap()[x1][y1][z1];
                    rootNode.detachChild(temp);
                    temp = roadF.quebendingRoad();
                    float angle = (float) Math.toRadians(-180);
                    temp.rotate(0, angle, 0);
                    temp.setLocalTranslation(connectedroad.getLocalTranslation());
                    temp.setUserData("connected", true);
                    temp.setUserData("queconnect1", connected1);
                    temp.setUserData("queconnect2", connected2);
                    temp.setUserData("roadID",IDr);
                    temp.setUserData("direction",direction0);
                    map.getMap()[x1][y1][z1] = temp;
                    rootNode.attachChild(temp);
                }
                return;
            }
            if (connected1loc.x < connected2loc.x && connected1loc.z < connected2loc.z) {
                if (connectedroad.getLocalTranslation().z > connected1loc.z) {
                    Spatial temp = map.getMap()[x1][y1][z1];
                    rootNode.detachChild(temp);
                    temp = roadF.quebendingRoad();
                    float angle = (float) Math.toRadians(90);
                    temp.rotate(0, angle, 0);
                    temp.setLocalTranslation(connectedroad.getLocalTranslation());
                    temp.setUserData("connected", true);
                    temp.setUserData("queconnect1", connected1);
                    temp.setUserData("queconnect2", connected2);
                    temp.setUserData("roadID",IDr);
                    temp.setUserData("direction",direction0);
                    map.getMap()[x1][y1][z1] = temp;
                    rootNode.attachChild(temp);
                } else {
                    
                    Spatial temp = map.getMap()[x1][y1][z1];
                    rootNode.detachChild(temp);
                    temp = roadF.quebendingRoad();
                    float angle = (float) Math.toRadians(-90);
                    temp.rotate(0, angle, 0);
                    temp.setLocalTranslation(connectedroad.getLocalTranslation());
                    temp.setUserData("connected", true);
                    temp.setUserData("queconnect1", connected1);
                    temp.setUserData("queconnect2", connected2);
                    temp.setUserData("roadID",IDr);
                    temp.setUserData("direction",direction0);
                    map.getMap()[x1][y1][z1] = temp;
                    rootNode.attachChild(temp);
                }

            }

        } else {
            Vector3f connected1loc = connectedroad.getLocalTranslation();
            Vector3f recentroadloc = road.getLocalTranslation();
            int x3 = (int) connected1loc.x;
            int y3 = (int) connected1loc.y;
            int z3 = (int) connected1loc.z;
            if (connected1loc.x > recentroadloc.x && connected1loc.z == recentroadloc.z || connected1loc.x < recentroadloc.x && connected1loc.z == recentroadloc.z) {
                Spatial temp = map.getMap()[x3][y3][z3];
                rootNode.detachChild(temp);
                temp = roadF.queroadStraight();
                temp.setLocalTranslation(connectedroad.getLocalTranslation());
                temp.setUserData("connected", false);
                temp.setUserData("queconnect1", road);
                temp.setUserData("roadID",ID);
                temp.setUserData("direction",direction0);
                map.getMap()[x3][y3][z3] = temp;
                rootNode.attachChild(temp);
                return;
            }
            if (connected1loc.x == recentroadloc.x && connected1loc.z < recentroadloc.z || connected1loc.x < recentroadloc.x && connected1loc.z == recentroadloc.z) {
                Spatial temp = map.getMap()[x3][y3][z3];
                rootNode.detachChild(temp);
                temp = roadF.queroadStraight();
                temp.setLocalTranslation(connectedroad.getLocalTranslation());
                float angle = (float) Math.toRadians(90);
                temp.rotate(0, angle, 0);
                temp.setUserData("connected", false);
                temp.setUserData("queconnect1", road);
                temp.setUserData("roadID",ID);
                temp.setUserData("direction",direction0);
                map.getMap()[x3][y3][z3] = temp;
                rootNode.attachChild(temp);

            }
        }


    }

    private void refreshqueroadarray(Spatial road) {
        System.out.println("Refreshing..");

        ArrayList<Spatial> queroads = getlinkedqueroads(road);
        System.out.println("Refresh size " + queroads.size());
        for (int i = 0; i < (queroads.size() - 2); i++) {
            Spatial cur = queroads.get(i);
            Spatial second = queroads.get(i + 1);

            int curx = (int) cur.getLocalTranslation().x;
            int cury = (int) cur.getLocalTranslation().y;
            int curz = (int) cur.getLocalTranslation().z;

            int secondx = (int) second.getLocalTranslation().x;
            int secondy = (int) second.getLocalTranslation().y;
            int secondz = (int) second.getLocalTranslation().z;

            turnqueroads(cur, curx, cury, curz, road, secondx, secondy, secondz);
            System.out.println("Refreshing road");

        }
        System.out.println("Refresh done");

    }

    public ArrayList<Spatial> getlinkedqueroads(Spatial firstroad) {
        ArrayList<Spatial> list = new ArrayList<Spatial>();
        Spatial curRoad = firstroad;
        if (firstroad == null) {
            System.out.println("firstroad = null !! WTF");
            return list;
        }
        //lisää eka pala
        list.add(curRoad);
        boolean end = false;
        int max = 0;
        while (end == false) {

            max++;
            boolean connected = false;
            if (curRoad.getUserData("connected") != null) {
                connected = curRoad.getUserData("connected");
            }
            boolean added = false;
            if (connected == true) {
                if (curRoad.getUserData("queconnect1") != null) {
                    Spatial locatedroad = curRoad.getUserData("queconnect1");
                    

                    //onko tie jo listassa
                    boolean old = false;
                    for (Spatial s : list) {
                        if (s.equals(locatedroad)) {
                            old = true;

                            //System.out.println("queconnect1 was already on the list");
                            break;
                        }
                    }
                    //jos ei niin lisää se 
                    if (old == false) {
                        added = true;
                        curRoad = locatedroad;
                        list.add(curRoad);
                        

                    }

                }
                if (curRoad.getUserData("queconnect2") != null && added == false) {
                    Spatial locatedroad = curRoad.getUserData("queconnect2");
                    

                    //onko tie jo listassa
                    boolean old = false;
                    for (Spatial s : list) {
                        if (s.equals(locatedroad)) {
                            old = true;
                            
                            break;
                        }
                    }
                    //jos ei niin lisää se 
                    if (old == false) {
                        curRoad = locatedroad;
                        list.add(curRoad);
                        //System.out.println("queconnect2 added to the list ");

                    }
                }

            } else {
                end = true;
                //System.out.println("finding connect ended because connected was false");
                //System.out.println("Still looking for the last one...");

                Spatial locatedroad = curRoad.getUserData("queconnect1");
                if (locatedroad != null) {
                    //System.out.println("queconnect1 found...");

                    //onko tie jo listassa
                    boolean old = false;
                    for (Spatial s : list) {
                        if (s.equals(locatedroad)) {
                            old = true;

                            //System.out.println("queconnect1 was already on the list");
                            break;
                        }
                    }
                    //jos ei niin lisää se 
                    if (old == false) {
                        added = true;
                        curRoad = locatedroad;
                        list.add(curRoad);
                        //System.out.println("queconnect1 added to the list  NOW FINISHING");

                    }
                }


                if (added == false) {
                    locatedroad=null;
                    locatedroad = curRoad.getUserData("queconnect2");
                    if(locatedroad!=null){
                        //System.out.println("queconnect2 found...");

                        //onko tie jo listassa
                        boolean old = false;
                        for (Spatial s : list) {
                            if (s.equals(locatedroad)) {
                                old = true;
                                //System.out.println("queconnect2 was already on the list");
                                break;
                            }
                        }
                        //jos ei niin lisää se 
                        if (old == false) {
                            curRoad = locatedroad;
                            list.add(curRoad);
                            //System.out.println("queconnect2 added to the list NOW FINISHING");
                        }
                    }
                    
                }






            }
            if (max > 100) {
                end = true;
                System.out.println("Gettin linked queroads looped for 100 times. Quitting...");
            }
        }

        return list;

    }

    void roadsToUpdate(ArrayList<Vector3f> roadtoUpdatePositions) {
        for(Vector3f a:roadtoUpdatePositions){
            updateroads((int)a.x, (int)a.y,(int)a.z);
        }
    }

    void queRoadsToUpdate(ArrayList<Spatial> queRoadsToUpdate) {
        for(Spatial s:queRoadsToUpdate){
            
            refreshqueroadarray(s);
        }
    }
}
