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
import de.lessvoid.nifty.effects.EffectEventId;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.TextRenderer;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import mygame.Main;
import mygame.inputhandler.ClickingHandler;
import mygame.inputhandler.ClickingModes;
import mygame.shops.Basicshops;
import mygame.terrain.Direction;
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
  public Basicshops selectedShop= Basicshops.NULL;

 
 public IngameHUD(){

     
 }
 
  public void bind(Nifty nifty, Screen screen) {
    this.nifty = nifty;
    this.screen = screen;
    
  }
    public void onStartScreen() {
     closeWindows("");   
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
        closeWindows("shovelWindow");
        Element niftyElement = nifty.getCurrentScreen().findElementByName("shovelWindow");

        niftyElement.setVisible(!niftyElement.isVisible());
        if(niftyElement.isVisible()==true){
            Main.clickingHandler.clickMode= ClickingModes.TERRAIN;
            
        }else{
            Main.clickingHandler.clickMode= ClickingModes.NOTHING;
        }
    }
    public void toggleShopWindow(){
        closeWindows("shopWindow");
        Element niftyElement = nifty.getCurrentScreen().findElementByName("shopWindow");

        niftyElement.setVisible(!niftyElement.isVisible());
        
    }
    public void toggleRoadWindow(){
        closeWindows("roadWindow");
        Element niftyElement = nifty.getCurrentScreen().findElementByName("roadWindow");

        niftyElement.setVisible(!niftyElement.isVisible());
        if(niftyElement.isVisible()==true){
            Main.clickingHandler.clickMode= ClickingModes.ROAD;
            
            
        }else{
            
            Main.clickingHandler.clickMode= ClickingModes.NOTHING;
        }
    }
    public void roadDirectionUp(){
        Main.roadMaker.direction= Direction.UP;
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
        Main.roadMaker.direction= Direction.DOWN;
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
        Main.roadMaker.direction= Direction.RIGHT;
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
        Main.roadMaker.direction= Direction.LEFT;
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
    public void closeWindows(String elementname){
        if(!elementname.equals("roadWindow")){
            Element niftyElement = nifty.getCurrentScreen().findElementByName("roadWindow");
        niftyElement.setVisible(false);
        niftyElement = nifty.getCurrentScreen().findElementByName("shopWindow");
        niftyElement.setVisible(false);
        }
        if(!elementname.equals("shovelWindow")){
            Element niftyElement = nifty.getCurrentScreen().findElementByName("shovelWindow");
        niftyElement.setVisible(false);
        niftyElement = nifty.getCurrentScreen().findElementByName("shopWindow");
        niftyElement.setVisible(false);
        }
        if(!elementname.equals("shopWindow")){
            Element niftyElement = nifty.getCurrentScreen().findElementByName("shovelWindow");
        niftyElement.setVisible(false);
         niftyElement = nifty.getCurrentScreen().findElementByName("roadWindow");
        niftyElement.setVisible(false);
        }
         
        
    }
    public void shopSelectmball(){
        selectedShop= Basicshops.MBALL;
        if(selectedShop==Main.shopManager.selectedShop){
            closeWindows(" ");
            Main.shopManager.activateplace();
            
            
            
            
        }
        else{
            Main.shopManager.setSelection(selectedShop);
            System.out.println("YOU SELECTED SHOP");
            //set description
        }
    }
    public void shopSelecttoilet(){
        selectedShop= Basicshops.TOILET;
        if(selectedShop==Main.shopManager.selectedShop){
            closeWindows(" ");
            Main.shopManager.activateplace();
            
        }
        else{
            Main.shopManager.selectedShop=Basicshops.TOILET;
            //set description
        }
    }
    public void shopSelectenergy(){
        selectedShop= Basicshops.ENERGY;
        if(selectedShop==Main.shopManager.selectedShop){
           closeWindows(" ");
           Main.shopManager.activateplace();
            
        }
        else{
            Main.shopManager.selectedShop=Basicshops.ENERGY;
            //set description
        }
    }
   
    
    
}
