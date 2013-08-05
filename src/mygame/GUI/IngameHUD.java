/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.GUI;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyEventSubscriber;
import de.lessvoid.nifty.controls.ButtonClickedEvent;
import de.lessvoid.nifty.controls.SliderChangedEvent;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.TextRenderer;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import mygame.Main;
import mygame.inputhandler.ClickingHandler;
import mygame.terrain.WorldHandler;

/**
 *
 * @author arska
 */
public class IngameHUD implements ScreenController{

     
  private Nifty nifty;
  private Screen screen;
  public boolean shovel=false;
  public ClickingHandler clickingHandler;
  public int brushsize=3;
    private WorldHandler worldHandler;

 
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
    @NiftyEventSubscriber(id = "sliderbrushsize")
    public void onSliderChange(String id, SliderChangedEvent event){
        Main.worldHandler.setBrush((int)event.getValue());
        updateBrushSize();
    }
    public void givefields(ClickingHandler clickingHandler,WorldHandler worldHandler){
        this.clickingHandler=clickingHandler;
        this.worldHandler=worldHandler;
    }
    public int getBrushSize(){
        return Main.worldHandler.brush;
    }
    public void updateBrushSize(){
        // find old text
    Element niftyElement = nifty.getCurrentScreen().findElementByName("brushsizetextactual");
    
    // swap old with new text
    niftyElement.getRenderer(TextRenderer.class).setText(Integer.toString(getBrushSize()));
    }
   
    
    
}
