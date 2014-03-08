/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.gameplayorgans;

import com.google.inject.Singleton;

/**
 *
 * @author arska
 */
@Singleton
public class Scenario {
    //VARIABLES
    private ScenarioGoal goal;
    private String loadPath;
    private String name;
    //GOAL POSSIBILITIES
    private int neededGuest;

    public Scenario(ScenarioGoal goal, String loadPath) {
        this.goal = goal;
        this.loadPath = loadPath;
    }

    
    public void setLoadPath(String loadPath) {
        this.loadPath = loadPath;
    }
    
    public String getLoadPath() {
        return loadPath;
    }
    
}
