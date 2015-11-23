/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package intopark.input;

/**
 *
 * @author arska
 */
public class CameraTurnedEvent {
    private double angle;

    public CameraTurnedEvent(double angle) {
        this.angle = angle;
    }

    public double getAngle() {
        return angle;
    }
    
}
