/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.GUI;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import mygame.Main;

/**
 *
 * @author arska
 */
public class StartScreen implements ScreenController {
    private Nifty nifty;
    private Screen screen;
    public int startgame=0;
 
   

    public StartScreen(){
        
    }
  /** custom methods */ 
  public void startGame(String nextScreen) {
    nifty.gotoScreen(nextScreen);  // switch to another screen
    
    
    
  }

   public void bind(Nifty nifty, Screen screen) {
    this.nifty = nifty;
    this.screen = screen;
  }

    public void onStartScreen() {
       
    }

    public void onEndScreen() {
       
    }
   
  
    public void startGame() {
        Main.startgame=1;
        startGame("hud");
        
        
    }
    public void startGameLoad(){
        Main.startgame=5;
        startGame("hud");
    }
 

}
