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
import java.util.ArrayList;
import mygame.Main;
import mygame.inputhandler.ClickingHandler;
import mygame.inputhandler.ClickingModes;
import mygame.npc.Guest;
import mygame.shops.BasicBuildables;
import mygame.terrain.Direction;
import mygame.terrain.RoadHill;
import mygame.terrain.RoadMakerStatus;
import mygame.terrain.WorldHandler;

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
    private WorldHandler worldHandler;
    public BasicBuildables selectedBuilding = BasicBuildables.NULL;
    private ShopDescriptionManager descriptionManager = new ShopDescriptionManager();
    

    public IngameHUD() {
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
    public void onSliderChange(String id, SliderChangedEvent event) {
        Main.worldHandler.setBrush((int) event.getValue());
        updateBrushSize();
    }

    @NiftyEventSubscriber(id = "brushmodebutton")
    public void onModeButtonChange(String id, ButtonClickedEvent event) {
        if (Main.worldHandler.mode == 2) {
            Main.worldHandler.mode = 1;
        } else {
            Main.worldHandler.mode = 2;
        }
        event.getButton().setText(getBrushMode());
    }

    @NiftyEventSubscriber(id = "usetexture")
    public void useTextureChange(String id, CheckBoxStateChangedEvent event) {
        Main.worldHandler.useTexture = event.isChecked();
    }

    @NiftyEventSubscriber(id = "textureforshovel")
    public void onTextureChange(String id, ImageSelectSelectionChangedEvent event) {
        Main.worldHandler.textureindex = event.getImageSelect().getSelectedImageIndex() + 1;
    }
    @NiftyEventSubscriber(id="guests")
    public void DropDownSelectionChangedEvent(String id,DropDownSelectionChangedEvent event)  {
        
        int index=event.getSelectionItemIndex();
        if(index==0){
            //selected default
           
        }
        else{
            Guest guest = null;
            for(Guest g:Main.npcManager.guests){
                if(g.getGuestNum()==index-1){
                    guest=g;
                    break;
                }
            }
            if(guest==null){
                System.out.println("Did not find guest with that index");
                return;
            }
            Main.windowMaker.createGuestWindow(guest,true);
            Element element = nifty.getCurrentScreen().findElementByName("NPCWindow");
            element.setVisible(false);
        }
        
    }
    @NiftyEventSubscriber(id = "guestnametextfield")
    public void onguestnameChanged(final String id, final TextFieldChangedEvent event) {
        if(!event.getText().equals("")) {
            Guest guest=Main.windowMaker.getCurrentGuestWindowGuest();
            guest.setName(event.getText());
            Main.windowMaker.updateGuestWindow(guest);
        }
    }
    public void givefields(ClickingHandler clickingHandler, WorldHandler worldHandler) {
        this.clickingHandler = clickingHandler;
        this.worldHandler = worldHandler;
    }

    public int getBrushSize() {
        return Main.worldHandler.brush;
    }

    public String getBrushMode() {
        if (Main.worldHandler.mode == 2) {
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
            Main.clickingHandler.clickMode = ClickingModes.TERRAIN;

        } else {
            Main.clickingHandler.clickMode = ClickingModes.NOTHING;
        }
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
            Main.clickingHandler.clickMode = ClickingModes.ROAD;


        } else {

            Main.clickingHandler.clickMode = ClickingModes.NOTHING;
        }
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
        ArrayList<Guest> guests=Main.npcManager.guests;
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
        Main.roadMaker.direction = Direction.UP;
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
        Main.roadMaker.direction = Direction.DOWN;
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
        Main.roadMaker.direction = Direction.RIGHT;
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
        Main.roadMaker.direction = Direction.LEFT;
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
        Main.roadMaker.hill = RoadHill.UP;
        Element niftyElement = nifty.getCurrentScreen().findElementByName("roaduphillimg");
        niftyElement.startEffect(EffectEventId.onCustom);
        //resettaa muitten effectit

        niftyElement = nifty.getCurrentScreen().findElementByName("roaddownhillimg");
        niftyElement.stopEffect(EffectEventId.onCustom);
        niftyElement = nifty.getCurrentScreen().findElementByName("roadflatimg");
        niftyElement.stopEffect(EffectEventId.onCustom);
    }

    public void roadFlatHill() {
        Main.roadMaker.hill = RoadHill.FLAT;
        Element niftyElement = nifty.getCurrentScreen().findElementByName("roadflatimg");
        niftyElement.startEffect(EffectEventId.onCustom);
        //resettaa muitten effectit

        niftyElement = nifty.getCurrentScreen().findElementByName("roaddownhillimg");
        niftyElement.stopEffect(EffectEventId.onCustom);
        niftyElement = nifty.getCurrentScreen().findElementByName("roaduphillimg");
        niftyElement.stopEffect(EffectEventId.onCustom);
    }

    public void roadDownHill() {
        Main.roadMaker.hill = RoadHill.DOWN;
        Element niftyElement = nifty.getCurrentScreen().findElementByName("roaddownhillimg");
        niftyElement.startEffect(EffectEventId.onCustom);
        //resettaa muitten effectit

        niftyElement = nifty.getCurrentScreen().findElementByName("roaduphillimg");
        niftyElement.stopEffect(EffectEventId.onCustom);
        niftyElement = nifty.getCurrentScreen().findElementByName("roadflatimg");
        niftyElement.stopEffect(EffectEventId.onCustom);
    }

    public void buildButton() {
        Main.roadMaker.buildRoad();
    }

    public void selectionButton() {
        Main.roadMaker.status = RoadMakerStatus.CHOOSING;
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
        }

    }

    public void buildingSelectmball() {
        selectedBuilding = BasicBuildables.MBALL;
        if (selectedBuilding == Main.shopManager.selectedBuilding) {
            closeWindows(" ");
            Main.shopManager.activateplace();




        } else {
            Main.shopManager.setSelection(selectedBuilding);
            System.out.println("YOU SELECTED BUILDING");
            descriptionManager.setDescriptionMBall();
            updateshopdesc();
        }
    }

    public void buildingSelecttoilet() {
        selectedBuilding = BasicBuildables.TOILET;
        if (selectedBuilding == Main.shopManager.selectedBuilding) {
            closeWindows(" ");
            Main.shopManager.activateplace();

        } else {
            Main.shopManager.selectedBuilding = BasicBuildables.TOILET;
            descriptionManager.setDescriptionToilet();
            updateshopdesc();
        }
    }

    public void buildingSelectenergy() {
        selectedBuilding = BasicBuildables.ENERGY;
        if (selectedBuilding == Main.shopManager.selectedBuilding) {
            closeWindows(" ");
            Main.shopManager.activateplace();

        } else {
            Main.shopManager.selectedBuilding = BasicBuildables.ENERGY;
            descriptionManager.setDescriptionEnergy();
            updateshopdesc();
        }
    }
    public void buildingSelectchess() {
        selectedBuilding = BasicBuildables.CHESSCENTER;
        if (selectedBuilding == Main.shopManager.selectedBuilding) {
            closeWindows(" ");
            Main.shopManager.activateplace();




        } else {
            Main.shopManager.setSelection(selectedBuilding);
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
        niftyElement.getRenderer(TextRenderer.class).setText(getshopname());
        niftyElement.getRenderer(TextRenderer.class).setTextHAlign(HorizontalAlign.left);
        niftyElement.getRenderer(TextRenderer.class).setTextVAlign(VerticalAlign.top);
        niftyElement = nifty.getCurrentScreen().findElementByName("shopprice");
        niftyElement.getRenderer(TextRenderer.class).setText(getshopprice());
        niftyElement.getRenderer(TextRenderer.class).setTextHAlign(HorizontalAlign.left);
        niftyElement.getRenderer(TextRenderer.class).setTextVAlign(VerticalAlign.top);
        niftyElement = nifty.getCurrentScreen().findElementByName("shopdesc");
        niftyElement.getRenderer(TextRenderer.class).setText(getshopdesc());
        niftyElement.getRenderer(TextRenderer.class).setTextHAlign(HorizontalAlign.left);
        niftyElement.getRenderer(TextRenderer.class).setTextVAlign(VerticalAlign.top);
        String bigshoppic=descriptionManager.bigpic;
        
        NiftyImage img = nifty.getRenderEngine().createImage(nifty.getCurrentScreen(),bigshoppic, false);
        niftyElement = nifty.getCurrentScreen().findElementByName("shopbigpic");
        niftyElement.getRenderer(ImageRenderer.class).setImage(img);

    }
}
