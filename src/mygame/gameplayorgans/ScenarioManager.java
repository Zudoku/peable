package mygame.gameplayorgans;

import com.google.inject.Inject;

/**
 *
 * @author arska
 */
public class ScenarioManager {
    
    @Inject 
    public ScenarioManager() {
        
    }
    public Scenario greenGolly(){
        Scenario scene=new Scenario(ScenarioGoal.GUESTS,"Scenarios/greengolly.IntoFile");
        
        return scene;
    }
    
}
