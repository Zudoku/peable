/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.GUI;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyEventSubscriber;
import de.lessvoid.nifty.controls.ButtonClickedEvent;
import de.lessvoid.nifty.controls.CheckBoxStateChangedEvent;
import de.lessvoid.nifty.controls.DropDown;
import de.lessvoid.nifty.controls.DropDownSelectionChangedEvent;
import de.lessvoid.nifty.controls.ImageSelectSelectionChangedEvent;
import de.lessvoid.nifty.controls.SliderChangedEvent;
import de.lessvoid.nifty.controls.TextFieldChangedEvent;
import de.lessvoid.nifty.effects.EffectEventId;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.ImageRenderer;
import de.lessvoid.nifty.elements.render.TextRenderer;
import de.lessvoid.nifty.layout.align.HorizontalAlign;
import de.lessvoid.nifty.layout.align.VerticalAlign;
import de.lessvoid.nifty.render.NiftyImage;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import de.lessvoid.nifty.tools.SizeValue;
import java.util.ArrayList;
import mygame.Gamestate;
import mygame.Main;
import mygame.inputhandler.ClickingHandler;
import mygame.inputhandler.ClickingModes;
import mygame.npc.Guest;
import mygame.ride.BasicRide;
import mygame.shops.BasicBuildables;
import mygame.terrain.Direction;
import mygame.terrain.RoadHill;
import mygame.terrain.RoadMakerStatus;
import mygame.terrain.TerrainHandler;

/**
 *
 * @author arska
 */
public class IngameHUD implements ScreenController {

    public Nifty nifty;
    public Screen screen;
    public boolean shovel = false;
    public ClickingHandler clickingHandler;
    public int brushsize = 3;
    private TerrainHandler worldHandler;
    public BasicBuildables selectedBuilding = BasicBuildables.NULL;
    private ShopDescriptionManager descriptionManager = new ShopDescriptionManager();
    NiftyImage newImage;
    
    
    
    public IngameHUD() {
        
    }

    public void bind(Nifty nifty, Screen screen) {
        this.nifty = nifty;
        this.screen = screen;
        

    }
    public void updateMoneytextbar(){
        nifty=Main.nifty;
        Element a=nifty.getCurrentScreen().findElementByName("moneytext");
        a.getRenderer(TextRenderer.class).setText(Main.currentPark.getParkWallet().getMoneyString());
        a.setConstraintHorizontalAlign(HorizontalAlign.left);
        a.setId("moneytext");
        
        a=nifty.getCurrentScreen().findElementByName("loantext");
        a.getRenderer(TextRenderer.class).setText(Main.currentPark.getParkWallet().getLoanString());
        a.setConstraintHorizontalAlign(HorizontalAlign.left);
        a.setId("loantext");
        
        a=nifty.getCurrentScreen().findElementByName("guestnumtext");
        a.getRenderer(TextRenderer.class).setText(Main.currentPark.getGuestSizeString());
        a.setConstraintHorizontalAlign(HorizontalAlign.left);
        a.setId("guestnumtext");
    }
    public void updateClickingIndicator(){
        nifty=Main.nifty;
        Element a=nifty.getCurrentScreen().findElementByName("clickmodeindicator");
        NiftyImage img = null;
        
        switch(Gamestate.clickingHandler.clickMode){
            case NOTHING:
                img = nifty.getRenderEngine().createImage(nifty.getCurrentScreen(),"Interface/Nifty/nothingmode.png", false);
                break;
                
            case DECORATION:
                img = nifty.getRenderEngine().createImage(nifty.getCurrentScreen(),"Interface/Nifty/decorationmode.png", false);
                break;
                
            case DEMOLITION:
                img = nifty.getRenderEngine().createImage(nifty.getCurrentScreen(),"Interface/Nifty/demolitionmode.png", false);
                break;
                
            case PLACE:
                img = nifty.getRenderEngine().createImage(nifty.getCurrentScreen(),"Interface/Nifty/placemode.png", false);
                break;
                
            case RIDE:
                img = nifty.getRenderEngine().createImage(nifty.getCurrentScreen(),"Interface/Nifty/ridemode.png", false);
                break;
                
            case ROAD:
                img = nifty.getRenderEngine().createImage(nifty.getCurrentScreen(),"Interface/Nifty/roadmode.png", false);
                break;
                
            case TERRAIN:
                img = nifty.getRenderEngine().createImage(nifty.getCurrentScreen(),"Interface/Nifty/diggingmode.png", false);
                
        }
        a.getRenderer(ImageRenderer.class).setImage(img);
    }

    public void onStartScreen() {
        
        closeWindows("");
        //laita raha kuva oikeaan asentoon
        Element a=screen.findElementByName("buttonlayer").findElementByName("buttons").findElementByName("moneyicon");
        int j=Main.currentPark.settings.getWidth();
        int u=j-300;
        String b=Integer.toString(u); 
        a.setConstraintX(new SizeValue(b));
        //tekstit
        a=screen.findElementByName("buttonlayer").findElementByName("buttons").findElementByName("moneytext");
        u=j-170;
        b=Integer.toString(u);
        a.setConstraintX(new SizeValue(b));
        
        a=screen.findElementByName("buttonlayer").findElementByName("buttons").findElementByName("loantext");
        u=j-170;
        b=Integer.toString(u);
        a.setConstraintX(new SizeValue(b));
        
        a=screen.findElementByName("buttonlayer").findElementByName("buttons").findElementByName("guestnumtext");
        u=j-280;
        b=Integer.toString(u);
        a.setConstraintX(new SizeValue(b));
        
        a=screen.findElementByName("buttonlayer").findElementByName("buttons").findElementByName("clickmodeindicator");
        u=j-364;
        b=Integer.toString(u);
        a.setConstraintX(new SizeValue(b));
        
        updateMoneytextbar();
        
        
    }

    public void onEndScreen() {
    }
    public void testSave(){
        Main.saveManager.Save("testfilexd", Main.currentPark);
    }
    public void clickDemolishbutton(){
        if(Gamestate.clickingHandler.clickMode==ClickingModes.DEMOLITION){
            Gamestate.clickingHandler.clickMode=ClickingModes.NOTHING;
        }
        else{
           Gamestate.clickingHandler.clickMode=ClickingModes.DEMOLITION; 
        }
        updateClickingIndicator();
    }
    @NiftyEventSubscriber(id = "sliderbrushsize")
    public void onSliderChange(String id, SliderChangedEvent event) {
        Main.gamestate.worldHandler.setBrush((int) event.getValue());
        updateBrushSize();
    }

    @NiftyEventSubscriber(id = "brushmodebutton")
    public void onModeButtonChange(String id, ButtonClickedEvent event) {
        if (Main.gamestate.worldHandler.mode == 2) {
            Main.gamestate.worldHandler.mode = 1;
        } else {
            Main.gamestate.worldHandler.mode = 2;
        }
        event.getButton().setText(getBrushMode());
        updateClickingIndicator();
    }

    @NiftyEventSubscriber(id = "usetexture")
    public void useTextureChange(String id, CheckBoxStateChangedEvent event) {
        Main.gamestate.worldHandler.useTexture = event.isChecked();
    }
    @NiftyEventSubscriber(id = "queroad")
    public void queCheckboxChange(String id, CheckBoxStateChangedEvent event) {
        Main.gamestate.roadMaker.queroad = event.isChecked();
        
    }
    @NiftyEventSubscriber(id = "textureforshovel")
    public void onTextureChange(String id, ImageSelectSelectionChangedEvent event) {
        Main.gamestate.worldHandler.textureindex = event.getImageSelect().getSelectedImageIndex() + 1;
    }
    @NiftyEventSubscriber(id="guests")
    public void DropDownSelectionChangedEvent(String id,DropDownSelectionChangedEvent event)  {
        
        int index=event.getSelectionItemIndex();
        if(index==0){
            //selected default
           
        }
        else{
            Guest guest = null;
            for(Guest g:Main.gamestate.npcManager.guests){
                if(g.getGuestNum()==index-1){
                    guest=g;
                    break;
                }
            }
            if(guest==null){
                System.out.println("Did not find guest with that index");
                return;
            }
            Main.gamestate.windowMaker.createGuestWindow(guest,true);
            Element element = nifty.getCurrentScreen().findElementByName("NPCWindow");
            element.setVisible(false);
        }
        
    }
    @NiftyEventSubscriber(id = "guestnametextfield")
    public void onguestnameChanged(final String id, final TextFieldChangedEvent event) {
        if(!event.getText().equals("")) {
            Guest guest=Main.gamestate.windowMaker.getCurrentGuestWindowGuest();
            guest.setName(event.getText());
            Main.gamestate.windowMaker.updateGuestWindow(guest);
        }
    }
    @NiftyEventSubscriber(id = "ridenametextfield")
    public void onRideNameChanged(final String id, final TextFieldChangedEvent event) {
        if(!event.getText().equals("")) {
            BasicRide ride=Main.gamestate.windowMaker.getCurrentRide();
            ride.setName(event.getText());
            Main.gamestate.windowMaker.updateRideWindow(false);
        }
    }
    @NiftyEventSubscriber(id = "ridepriceslider")
    public void onRidePriceChanged(final String id, final SliderChangedEvent event) {
        
            BasicRide ride=Main.gamestate.windowMaker.getCurrentRide();
            ride.setPrice(event.getValue());
            Main.gamestate.windowMaker.updateRideWindow(false);
        
    }
    public void givefields(ClickingHandler clickingHandler, TerrainHandler worldHandler) {
        
        this.clickingHandler = clickingHandler;
        this.worldHandler = worldHandler;
    }
    public void rideStatusToggle(){
        Main.gamestate.windowMaker.handleRideStatusToggle();
    }
    public int getBrushSize() {
        return Gamestate.worldHandler.brush;
    }

    public String getBrushMode() {
        if (Main.gamestate.worldHandler.mode == 2) {
            return "Raise Land";
        } else {
            return "Lower Land";
        }
    }

    public void updateBrushSize() {
        Element niftyElement = nifty.getCurrentScreen().findElementByName("brushsizetextactual");
        niftyElement.getRenderer(TextRenderer.class).setText(Integer.toString(getBrushSize()));
    }

    public void toggleShovelWindow() {
        closeWindows("shovelWindow");
        
        Element niftyElement = nifty.getCurrentScreen().findElementByName("shovelWindow");

        niftyElement.setVisible(!niftyElement.isVisible());
        if (niftyElement.isVisible() == true) {
            Main.gamestate.clickingHandler.clickMode = ClickingModes.TERRAIN;

        } else {
            Main.gamestate.clickingHandler.clickMode = ClickingModes.NOTHING;
        }
        updateClickingIndicator();
    }

    public void toggleShopWindow() {
        closeWindows("shopWindow");
        Element niftyElement = nifty.getCurrentScreen().findElementByName("shopWindow");

        niftyElement.setVisible(!niftyElement.isVisible());

    }

    public void toggleRoadWindow() {
        closeWindows("roadWindow");
        Element niftyElement = nifty.getCurrentScreen().findElementByName("roadWindow");

        niftyElement.setVisible(!niftyElement.isVisible());
        if (niftyElement.isVisible() == true) {
            Main.gamestate.clickingHandler.clickMode = ClickingModes.ROAD;


        } else {

            Main.gamestate.clickingHandler.clickMode = ClickingModes.NOTHING;
        }
        updateClickingIndicator();
    }
    
    public void toggleNPCListWindow(){
        closeWindows("NPCWindow");
        Element niftyElement = nifty.getCurrentScreen().findElementByName("NPCWindow");

        niftyElement.setVisible(!niftyElement.isVisible());
        
        if(niftyElement.isVisible()==true){
            updateNPCBox();
        }
    }
    public void updateNPCBox(){
        ArrayList<Guest> guests=Main.gamestate.npcManager.guests;
    DropDown drop= screen.findNiftyControl("guests",DropDown.class);
    drop.clear();
    drop.addItem("default");
    for(Guest g:guests){
        String guest=Integer.toString(g.getGuestNum())+"- "+g.getName();
        drop.addItem(guest);
    }
    drop.getElement().setConstraintHorizontalAlign(HorizontalAlign.left);
    drop.getElement().setConstraintVerticalAlign(VerticalAlign.top);
    }

    public void roadDirectionUp() {
        Main.gamestate.roadMaker.direction = Direction.UP;
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

    public void roadDirectionDown() {
        Main.gamestate.roadMaker.direction = Direction.DOWN;
        Element niftyElement = nifty.getCurrentScreen().findElementByName("roaddownimg");
        niftyElement.startEffect(EffectEventId.onCustom);
        niftyElement = nifty.getCurrentScreen().findElementByName("roadupimg");
        niftyElement.stopEffect(EffectEventId.onCustom);
        niftyElement = nifty.getCurrentScreen().findElementByName("roadrightimg");
        niftyElement.stopEffect(EffectEventId.onCustom);
        niftyElement = nifty.getCurrentScreen().findElementByName("roadleftimg");
        niftyElement.stopEffect(EffectEventId.onCustom);
    }

    public void roadDirectionRight() {
        Main.gamestate.roadMaker.direction = Direction.RIGHT;
        Element niftyElement = nifty.getCurrentScreen().findElementByName("roadrightimg");
        niftyElement.startEffect(EffectEventId.onCustom);
        niftyElement = nifty.getCurrentScreen().findElementByName("roaddownimg");
        niftyElement.stopEffect(EffectEventId.onCustom);
        niftyElement = nifty.getCurrentScreen().findElementByName("roadupimg");
        niftyElement.stopEffect(EffectEventId.onCustom);
        niftyElement = nifty.getCurrentScreen().findElementByName("roadleftimg");
        niftyElement.stopEffect(EffectEventId.onCustom);
    }

    public void roadDirectionLeft() {
        Main.gamestate.roadMaker.direction = Direction.LEFT;
        Element niftyElement = nifty.getCurrentScreen().findElementByName("roadleftimg");
        niftyElement.startEffect(EffectEventId.onCustom);
        niftyElement = nifty.getCurrentScreen().findElementByName("roaddownimg");
        niftyElement.stopEffect(EffectEventId.onCustom);
        niftyElement = nifty.getCurrentScreen().findElementByName("roadrightimg");
        niftyElement.stopEffect(EffectEventId.onCustom);
        niftyElement = nifty.getCurrentScreen().findElementByName("roadupimg");
        niftyElement.stopEffect(EffectEventId.onCustom);
    }

    public void roadUpHill() {
        Main.gamestate.roadMaker.hill = RoadHill.UP;
        Element niftyElement = nifty.getCurrentScreen().findElementByName("roaduphillimg");
        niftyElement.startEffect(EffectEventId.onCustom);
        //resettaa muitten effectit

        niftyElement = nifty.getCurrentScreen().findElementByName("roaddownhillimg");
        niftyElement.stopEffect(EffectEventId.onCustom);
        niftyElement = nifty.getCurrentScreen().findElementByName("roadflatimg");
        niftyElement.stopEffect(EffectEventId.onCustom);
    }

    public void roadFlatHill() {
        Main.gamestate.roadMaker.hill = RoadHill.FLAT;
        Element niftyElement = nifty.getCurrentScreen().findElementByName("roadflatimg");
        niftyElement.startEffect(EffectEventId.onCustom);
        //resettaa muitten effectit

        niftyElement = nifty.getCurrentScreen().findElementByName("roaddownhillimg");
        niftyElement.stopEffect(EffectEventId.onCustom);
        niftyElement = nifty.getCurrentScreen().findElementByName("roaduphillimg");
        niftyElement.stopEffect(EffectEventId.onCustom);
    }

    public void roadDownHill() {
        Main.gamestate.roadMaker.hill = RoadHill.DOWN;
        Element niftyElement = nifty.getCurrentScreen().findElementByName("roaddownhillimg");
        niftyElement.startEffect(EffectEventId.onCustom);
        //resettaa muitten effectit

        niftyElement = nifty.getCurrentScreen().findElementByName("roaduphillimg");
        niftyElement.stopEffect(EffectEventId.onCustom);
        niftyElement = nifty.getCurrentScreen().findElementByName("roadflatimg");
        niftyElement.stopEffect(EffectEventId.onCustom);
    }

    public void buildButton() {
        Main.gamestate.roadMaker.buildRoad();
    }

    public void selectionButton() {
        Main.gamestate.roadMaker.status = RoadMakerStatus.CHOOSING;
    }

    public void closeWindows(String elementname) {
        Element niftyElement;
        if (!elementname.equals("roadWindow")) {
            niftyElement = nifty.getCurrentScreen().findElementByName("roadWindow");
            niftyElement.setVisible(false);
            
        }
        if (!elementname.equals("shovelWindow")) {
            niftyElement = nifty.getCurrentScreen().findElementByName("shovelWindow");
            niftyElement.setVisible(false);
            
        }
        if (!elementname.equals("shopWindow")) {
            niftyElement = nifty.getCurrentScreen().findElementByName("shopWindow");
            niftyElement.setVisible(false);
            
        }
        if (!elementname.equals("NPCWindow")) {
            niftyElement = nifty.getCurrentScreen().findElementByName("NPCWindow");
            niftyElement.setVisible(false);
            
        }
        if(elementname.equals("")){
        niftyElement = nifty.getCurrentScreen().findElementByName("guesttemplate");
        niftyElement.setVisible(false);
        niftyElement = nifty.getCurrentScreen().findElementByName("shoptemplate");
        niftyElement.setVisible(false);
        niftyElement = nifty.getCurrentScreen().findElementByName("ridetemplate");
        niftyElement.setVisible(false);
        }

    }

    public void buildingSelectmball() {
        selectedBuilding = BasicBuildables.MBALL;
        if (selectedBuilding == Gamestate.shopManager.selectedBuilding) {
            closeWindows(" ");
            Gamestate.shopManager.activateplace();




        } else {
            Main.gamestate.shopManager.setSelection(selectedBuilding);
            System.out.println("YOU SELECTED BUILDING");
            descriptionManager.setDescriptionMBall();
            updateshopdesc();
        }
    }

    public void buildingSelecttoilet() {
        selectedBuilding = BasicBuildables.TOILET;
        if (selectedBuilding == Main.gamestate.shopManager.selectedBuilding) {
            closeWindows(" ");
            Main.gamestate.shopManager.activateplace();

        } else {
            Main.gamestate.shopManager.selectedBuilding = BasicBuildables.TOILET;
            descriptionManager.setDescriptionToilet();
            updateshopdesc();
        }
    }

    public void buildingSelectenergy() {
        selectedBuilding = BasicBuildables.ENERGY;
        if (selectedBuilding == Main.gamestate.shopManager.selectedBuilding) {
            closeWindows(" ");
            Main.gamestate.shopManager.activateplace();

        } else {
            Main.gamestate.shopManager.selectedBuilding = BasicBuildables.ENERGY;
            descriptionManager.setDescriptionEnergy();
            updateshopdesc();
        }
    }
    public void buildingSelectchess() {
        selectedBuilding = BasicBuildables.CHESSCENTER;
        if (selectedBuilding == Main.gamestate.shopManager.selectedBuilding) {
            closeWindows(" ");
            Main.gamestate.shopManager.activateplace();




        } else {
            Main.gamestate.shopManager.setSelection(selectedBuilding);
            System.out.println("YOU SELECTED BUILDING");
            descriptionManager.setDescriptionChess();
            updateshopdesc();
        }
    }

    public String getshopname() {
        return descriptionManager.shopName;
    }

    public String getshopdesc() {
        return descriptionManager.shopDescription;
    }

    public String getshopprice() {
        return descriptionManager.shopPrice;
    }

    public void updateshopdesc() {
        Element niftyElement = nifty.getCurrentScreen().findElementByName("shopname");
        setelementText(niftyElement,getshopname());
        niftyElement = nifty.getCurrentScreen().findElementByName("shopprice");
        setelementText(niftyElement,getshopprice());
        niftyElement = nifty.getCurrentScreen().findElementByName("shopdesc");
        setelementText(niftyElement,getshopdesc());
        String bigshoppic=descriptionManager.bigpic;
        
        NiftyImage img = nifty.getRenderEngine().createImage(nifty.getCurrentScreen(),bigshoppic, false);
        niftyElement = nifty.getCurrentScreen().findElementByName("shopbigpic");
        niftyElement.getRenderer(ImageRenderer.class).setImage(img);

    }
    private void setelementText(Element element,String text){
        element.getRenderer(TextRenderer.class).setText(text);
        element.getRenderer(TextRenderer.class).setTextHAlign(HorizontalAlign.left);
        element.getRenderer(TextRenderer.class).setTextVAlign(VerticalAlign.top);
    }
}
