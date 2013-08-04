/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.GUI;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import mygame.inputhandler.ClickingHandler;

/**
 *
 * @author arska
 */
public class IngameHUD implements ScreenController{

     
  private Nifty nifty;
  private Screen screen;
  public boolean shovel=false;
  public ClickingHandler clickingHandler;

 
 public IngameHUD(){
    
     
 }
 
  public void bind(Nifty nifty, Screen screen) {
    this.nifty = nifty;
    this.screen = screen;
    
  }
    public void onStartScreen() {
        
    }

    public void onEndScreen() {
        
    }
    public void decreaseShovelSize(){
        
        clickingHandler.worldHandler.brushMinus();
    }
    public void increaseShovelSize(){
        clickingHandler.worldHandler.brushPlus();
    }
    public void Givefields(ClickingHandler clickingHandler){
        this.clickingHandler=clickingHandler;
        
    }
    public void test(){
        System.out.println("brushplus");
        System.out.println("brushplus");
        System.out.println("brushplus");
        System.out.println("brushplus");
    }
    
    
}
