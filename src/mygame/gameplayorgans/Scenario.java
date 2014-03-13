/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.gameplayorgans;

import com.google.inject.Singleton;
import com.jme3.math.Vector3f;

/**
 *
 * @author arska
 */
@Singleton
public class Scenario {
    //VARIABLES
    private ScenarioGoal goal;
    private  transient Vector3f enterancePos;
    private double enteranceYRotation;
    //GOAL POSSIBILITIES
    private int neededGuest;

    public Scenario(ScenarioGoal goal) {
        this.goal = goal;
    }

    public void setEnterancePos(Vector3f enterancePos) {
        this.enterancePos = enterancePos;
    }

    public Vector3f getEnterancePos() {
        return enterancePos;
    }

    public void setEnteranceYRotation(double enteranceYRotation) {
        this.enteranceYRotation = enteranceYRotation;
    }

    public double getEnteranceYRotation() {
        return enteranceYRotation;
    }

    public void setGoal(ScenarioGoal goal) {
        this.goal = goal;
    }

    public ScenarioGoal getGoal() {
        return goal;
    }
    
    
}
