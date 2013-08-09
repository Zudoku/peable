/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.GUI;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyEventSubscriber;
import de.lessvoid.nifty.controls.ButtonClickedEvent;
import de.lessvoid.nifty.controls.CheckBoxStateChangedEvent;
import de.lessvoid.nifty.controls.ImageSelectSelectionChangedEvent;
import de.lessvoid.nifty.controls.SliderChangedEvent;
import de.lessvoid.nifty.controls.Window;
import de.lessvoid.nifty.controls.window.WindowControl;
import de.lessvoid.nifty.controls.window.builder.WindowBuilder;
import de.lessvoid.nifty.effects.EffectEventId;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.TextRenderer;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import java.util.List;
import mygame.Main;
import mygame.inputhandler.ClickingHandler;
import mygame.inputhandler.ClickingModes;
import mygame.terrain.RoadDirection;
import mygame.terrain.RoadHill;
import mygame.terrain.RoadMakerStatus;
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
    @NiftyEventSubscriber(id = "brushmodebutton")
    public void onModeButtonChange(String id, ButtonClickedEvent event){
        if(Main.worldHandler.mode==2){
            Main.worldHandler.mode=1;
        }else{
            Main.worldHandler.mode=2;
        }
        event.getButton().setText(getBrushMode());
    }
     @NiftyEventSubscriber(id = "usetexture")
    public void useTextureChange(String id, CheckBoxStateChangedEvent event){
       Main.worldHandler.useTexture=event.isChecked();
    }
    @NiftyEventSubscriber(id = "textureforshovel")
    public void onTextureChange(String id, ImageSelectSelectionChangedEvent event){
       Main.worldHandler.textureindex=event.getImageSelect().getSelectedImageIndex()+1;
    }
    
    public void givefields(ClickingHandler clickingHandler,WorldHandler worldHandler){
        this.clickingHandler=clickingHandler;
        this.worldHandler=worldHandler;
    }
    public int getBrushSize(){
        return Main.worldHandler.brush;
    }
    public String getBrushMode(){
        if(Main.worldHandler.mode==2){
            return "Raise Land";
        }
        else{
            return "Lower Land";
        }
    }
    public void updateBrushSize(){
    Element niftyElement = nifty.getCurrentScreen().findElementByName("brushsizetextactual");
    niftyElement.getRenderer(TextRenderer.class).setText(Integer.toString(getBrushSize()));
    }
    public void toggleShovelWindow(){
        Element niftyElement = nifty.getCurrentScreen().findElementByName("shovelWindow");

        niftyElement.setVisible(!niftyElement.isVisible());
        if(niftyElement.isVisible()==true){
            Main.clickingHandler.clickMode= ClickingModes.TERRAIN;
            
        }else{
            Main.clickingHandler.clickMode= ClickingModes.NOTHING;
        }
    }
    public void toggleRoadWindow(){
        Element niftyElement = nifty.getCurrentScreen().findElementByName("roadWindow");

        niftyElement.setVisible(!niftyElement.isVisible());
        if(niftyElement.isVisible()==true){
            Main.clickingHandler.clickMode= ClickingModes.ROAD;
            //sulje muut ikkunat
            
        }else{
            
            Main.clickingHandler.clickMode= ClickingModes.NOTHING;
        }
    }
    public void roadDirectionUp(){
        Main.roadMaker.direction= RoadDirection.UP;
        Element niftyElement = nifty.getCurrentScreen().findElementByName("roadupimg");
        niftyElement.startEffect(EffectEventId.onCustom);
        //resettaa muitten effectit
        
        niftyElement = nifty.getCurrentScreen().findElementByName("roaddownimg");
        niftyElement.stopEffect(EffectEventId.onCustom);
        niftyElement = nifty.getCurrentScreen().findElementByName("roadrightimg");
        niftyElement.stopEffect(EffectEventId.onCustom);
        niftyElement = nifty.getCurrentScreen().findElementByName("roadleftimg");
        niftyElement.stopEffect(EffectEventId.onCustom);
        
    }
    public void roadDirectionDown(){
        Main.roadMaker.direction= RoadDirection.DOWN;
        Element niftyElement = nifty.getCurrentScreen().findElementByName("roaddownimg");
        niftyElement.startEffect(EffectEventId.onCustom);
        niftyElement = nifty.getCurrentScreen().findElementByName("roadupimg");
        niftyElement.stopEffect(EffectEventId.onCustom);
        niftyElement = nifty.getCurrentScreen().findElementByName("roadrightimg");
        niftyElement.stopEffect(EffectEventId.onCustom);
        niftyElement = nifty.getCurrentScreen().findElementByName("roadleftimg");
        niftyElement.stopEffect(EffectEventId.onCustom);
    }
    public void roadDirectionRight(){
        Main.roadMaker.direction= RoadDirection.RIGHT;
        Element niftyElement = nifty.getCurrentScreen().findElementByName("roadrightimg");
        niftyElement.startEffect(EffectEventId.onCustom);
        niftyElement = nifty.getCurrentScreen().findElementByName("roaddownimg");
        niftyElement.stopEffect(EffectEventId.onCustom);
        niftyElement = nifty.getCurrentScreen().findElementByName("roadupimg");
        niftyElement.stopEffect(EffectEventId.onCustom);
        niftyElement = nifty.getCurrentScreen().findElementByName("roadleftimg");
        niftyElement.stopEffect(EffectEventId.onCustom);
    }
    public void roadDirectionLeft(){
        Main.roadMaker.direction= RoadDirection.LEFT;
        Element niftyElement = nifty.getCurrentScreen().findElementByName("roadleftimg");
        niftyElement.startEffect(EffectEventId.onCustom);
        niftyElement = nifty.getCurrentScreen().findElementByName("roaddownimg");
        niftyElement.stopEffect(EffectEventId.onCustom);
        niftyElement = nifty.getCurrentScreen().findElementByName("roadrightimg");
        niftyElement.stopEffect(EffectEventId.onCustom);
        niftyElement = nifty.getCurrentScreen().findElementByName("roadupimg");
        niftyElement.stopEffect(EffectEventId.onCustom);
    }
     public void roadUpHill(){
        Main.roadMaker.hill= RoadHill.UP;
        Element niftyElement = nifty.getCurrentScreen().findElementByName("roaduphillimg");
        niftyElement.startEffect(EffectEventId.onCustom);
        //resettaa muitten effectit
        
        niftyElement = nifty.getCurrentScreen().findElementByName("roaddownhillimg");
        niftyElement.stopEffect(EffectEventId.onCustom);
        niftyElement = nifty.getCurrentScreen().findElementByName("roadflatimg");
        niftyElement.stopEffect(EffectEventId.onCustom);
    }
     public void roadFlatHill(){
        Main.roadMaker.hill= RoadHill.FLAT;
        Element niftyElement = nifty.getCurrentScreen().findElementByName("roadflatimg");
        niftyElement.startEffect(EffectEventId.onCustom);
        //resettaa muitten effectit
        
        niftyElement = nifty.getCurrentScreen().findElementByName("roaddownhillimg");
        niftyElement.stopEffect(EffectEventId.onCustom);
        niftyElement = nifty.getCurrentScreen().findElementByName("roaduphillimg");
        niftyElement.stopEffect(EffectEventId.onCustom);
    }
     public void roadDownHill(){
        Main.roadMaker.hill= RoadHill.DOWN;
        Element niftyElement = nifty.getCurrentScreen().findElementByName("roaddownhillimg");
        niftyElement.startEffect(EffectEventId.onCustom);
        //resettaa muitten effectit
        
        niftyElement = nifty.getCurrentScreen().findElementByName("roaduphillimg");
        niftyElement.stopEffect(EffectEventId.onCustom);
        niftyElement = nifty.getCurrentScreen().findElementByName("roadflatimg");
        niftyElement.stopEffect(EffectEventId.onCustom);
    }
    public void buildButton(){
        Main.roadMaker.buildRoad();
    }
    public void selectionButton(){
        Main.roadMaker.status= RoadMakerStatus.CHOOSING;
    }
   
    
    
}
