/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package intopark.roads;

import com.google.common.base.Preconditions;
import com.google.inject.Singleton;
import com.jme3.scene.Spatial;
import intopark.UtilityMethods;
import intopark.inout.LoadPaths;
import static intopark.roads.RoadHill.FLAT;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author arska
 */
@Singleton
public class RoadFactory {
    private static final Logger logger = Logger.getLogger(RoadFactory.class.getName());
    private Map<String,RoadShapeContainer>transformMap;
    /*  */
    private static final int SKIN_1_STRAIGHT=100;
    private static final int SKIN_1_UPHILL=101;
    private static final int SKIN_1_DOWNHILL=102;
    private static final int SKIN_1_BENDING=103;
    private static final int SKIN_1_TROAD=104;
    private static final int SKIN_1_4ROAD=105;
    
    public RoadFactory(){
        initTransformMap();
        
    }
    private void initTransformMap(){
        transformMap=new HashMap<>();
        /* STRING FORMAT NORTH-SOUTH-EAST-WEST  | VALUES t = true - f = false */
        /* NORTH =X+ WEST =Z+ BENDING ROAD IS TOWARDS X+ AND Z+ */
        /* T_ROAD HAS BACK AGAINST X+ */
        transformMap.put("tttt", new RoadShapeContainer(SKIN_1_4ROAD,0));
        
        transformMap.put("ffff", new RoadShapeContainer(SKIN_1_STRAIGHT, 0));
        transformMap.put("ffft", new RoadShapeContainer(SKIN_1_STRAIGHT, -90));
        transformMap.put("fftf", new RoadShapeContainer(SKIN_1_STRAIGHT, 90));
        transformMap.put("ftff", new RoadShapeContainer(SKIN_1_STRAIGHT, 180));
        transformMap.put("tfff", new RoadShapeContainer(SKIN_1_STRAIGHT, 0));
        
        transformMap.put("fftt", new RoadShapeContainer(SKIN_1_STRAIGHT, 90));
        transformMap.put("ttff", new RoadShapeContainer(SKIN_1_STRAIGHT, 0));
        
        transformMap.put("ftft", new RoadShapeContainer(SKIN_1_BENDING, 180)); 
        transformMap.put("tfft", new RoadShapeContainer(SKIN_1_BENDING, 90));
        transformMap.put("tftf", new RoadShapeContainer(SKIN_1_BENDING, 0));
        transformMap.put("fttf", new RoadShapeContainer(SKIN_1_BENDING, -90));
        
        transformMap.put("fttt", new RoadShapeContainer(SKIN_1_TROAD, 0));
        transformMap.put("tftt", new RoadShapeContainer(SKIN_1_TROAD, 180));
        transformMap.put("ttft", new RoadShapeContainer(SKIN_1_TROAD, -90));
        transformMap.put("tttf", new RoadShapeContainer(SKIN_1_TROAD, 90));
    }
    /**
     * 
     * @param road
     * @return 
     */
    public Spatial roadToSpatial(Road road,boolean... cDirections){
        Preconditions.checkArgument(road!=null,"Null road can't be transformed to spatial");
        Preconditions.checkArgument(cDirections.length==4,"Not enough arguments OR too many arguments. Needs 4 booleans!");
        Spatial roadSpatial= null;
        /* We get the spatial and rotate it  */
        switch(road.getRoadhill()){
            case FLAT:
                RoadShapeContainer info=transformMap.get(booleanToString(cDirections));
                logger.log(Level.FINEST, "Road ID: {0} Position:{1} transformed to CODE: {2} ROTATION:{3}",new Object[]{road.getID(),road.getPosition().getVector(),info.getCode(),info.getAngle()});
                roadSpatial=getAbsoluteSpatial(info.getCode(),road.getQueRoad());
                roadSpatial.rotate(0,(float) Math.toRadians(info.getAngle()),0);
                break;
                
            default:
                if(road.getRoadhill()== RoadHill.UP){
                        if(road.getSkin()==1){
                            roadSpatial=getAbsoluteSpatial(SKIN_1_UPHILL,road.getQueRoad());
                        }
                }else{
                        if(road.getSkin()==1){
                            roadSpatial=getAbsoluteSpatial(SKIN_1_DOWNHILL,road.getQueRoad());
                        }
                }
                float anglet = (float) Math.toRadians(road.getDirection().getAngle());
                roadSpatial.rotate(0, anglet, 0);
                break;
        }
        roadSpatial.move(road.getVector3f());
        roadSpatial.setUserData("type", "road");
        roadSpatial.setUserData("ID", road.getID());
        logger.log(Level.FINEST,"Roads final translation is {0}",roadSpatial.getLocalTranslation());
        return roadSpatial;
    }

    private String booleanToString(boolean... b1) {
        StringBuilder builder = new StringBuilder(4);
        for (boolean b2 : b1) {
            builder.append(b2 ? "t" : "f");
        }
        return builder.toString();
    }

    private Spatial getAbsoluteSpatial(int code, boolean queue) {
        Spatial road = null;
        switch (code) {
            case SKIN_1_STRAIGHT:
                if (queue) {
                    road = UtilityMethods.loadModel(LoadPaths.queroadstraight);
                    road.scale(0.5f, 0.5f, 0.5f);
                    return road;
                } else {
                    road = UtilityMethods.loadModel(LoadPaths.roadstraight);
                    road.scale(0.5f, 0.5f, 0.5f);
                    return road;
                }
            case SKIN_1_UPHILL:
                if (queue) {
                    road = UtilityMethods.loadModel(LoadPaths.queroaduphill);
                    road.setLocalTranslation(0, +0.50f, 0);
                    
                    return road;
                } else {
                    road = UtilityMethods.loadModel(LoadPaths.roaduphill);
                    road.setLocalTranslation(0, +0.50f, 0);
                    return road;
                }

            case SKIN_1_DOWNHILL:
                if (queue) {
                    road = UtilityMethods.loadModel(LoadPaths.queroaduphill);

                    Float angle = (float) Math.toRadians(180);
                    road.setLocalTranslation(0, -0.50f, 0);
                    road.rotate(0, angle, 0);
                    return road;
                } else {
                    road = UtilityMethods.loadModel(LoadPaths.roaduphill);
                    float angle = (float) Math.toRadians(180);
                    road.setLocalTranslation(0, -0.50f, 0);
                    road.rotate(0, angle, 0);
                    return road;
                }

            case SKIN_1_4ROAD:
                road = UtilityMethods.loadModel(LoadPaths.roadcenter);
                road.scale(0.5f, 0.5f, 0.5f);
                return road;
            case SKIN_1_TROAD:
                road = UtilityMethods.loadModel(LoadPaths.roadT);
                road.scale(0.5f, 0.5f, 0.5f);
                return road;
            case SKIN_1_BENDING:
                if (queue) {
                    road = UtilityMethods.loadModel(LoadPaths.queroadbending);

                    road.scale(0.5f, 0.5f, 0.5f);
                    return road;
                } else {
                    road = UtilityMethods.loadModel(LoadPaths.roadbending);
                    road.scale(0.5f, 0.5f, 0.5f);
                    return road;
                }

        }
        return road;
    }

 
    
}
