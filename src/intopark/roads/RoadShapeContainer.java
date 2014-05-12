/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package intopark.roads;

/**
 *
 * @author arska
 */
public class RoadShapeContainer {
    private int code;
    private int angle;
    
    public RoadShapeContainer(int code, int angle) {
        this.code = code;
        this.angle = angle;
    }

    public int getAngle() {
        return angle;
    }

    public int getCode() {
        return code;
    }

    public void setAngle(int angle) {
        this.angle = angle;
    }

    public void setCode(int code) {
        this.code = code;
    }
     
   
}
