/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.GUI;

import mygame.GUI.events.UpdateMoneyTextBarEvent;
import mygame.GUI.events.UpdateRoadDirectionEvent;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import com.google.inject.Injector;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyEventSubscriber;
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
import java.util.logging.Level;
import java.util.logging.Logger;
import mygame.GUI.events.CloseWindowsEvent;
import mygame.Main;
import mygame.SaveManager;
import mygame.inputhandler.ClickingHandler;
import mygame.inputhandler.ClickingModes;
import mygame.npc.Guest;
import mygame.npc.NPCManager;
import mygame.ride.BasicRide;
import mygame.shops.BasicBuildables;
import mygame.shops.BasicShop;
import mygame.shops.BuildingSelectionEvent;
import mygame.shops.ShopManager;
import mygame.GUI.events.UpdateBuildingUIEvent;
import mygame.terrain.Direction;
import mygame.terrain.ParkHandler;
import mygame.terrain.RoadHill;
import mygame.terrain.RoadMaker;
import mygame.terrain.RoadMakerStatus;
import mygame.terrain.TerrainHandler;
import mygame.terrain.decoration.DecorationManager;
import mygame.terrain.decoration.Decorations;

/**
 *
 * @author arska
 */
public class IngameHUD implements ScreenController {
    private static final Logger logger = Logger.getLogger(IngameHUD.class.getName());
    public Nifty nifty;
    public Screen screen;
    public boolean shovel = false;
    @Inject private TerrainHandler terrainHandler;
    @Inject private ParkHandler parkHandler;
    @Inject private ClickingHandler clickingHandler;
    @Inject private RoadMaker roadMaker;
    @Inject ShopManager shopManager;
    @Inject EventBus eventBus;
    @Inject private WindowMaker windowMaker;
    @Inject NPCManager npcManager;
    @Inject SaveManager saveManager;
    @Inject private DecorationManager decorationManager;
    public BasicBuildables selectedBuilding = BasicBuildables.NULL;
    private ShopDescriptionManager descriptionManager = new ShopDescriptionManager();
    NiftyImage newImage;
    
    
    
    public IngameHUD() {
        Injector i=Main.injector;
        i.injectMembers(this);
        eventBus.register(this);
    }

    public void bind(Nifty nifty, Screen screen) {
        this.nifty = nifty;
        this.screen = screen;
        

    }
    @Subscribe public void processMoneyTextBarUpdate(UpdateMoneyTextBarEvent e) {
        updateMoneytextbar();
    }
    private void updateMoneytextbar(){
        nifty=Main.nifty;
        Element a=nifty.getCurrentScreen().findElementByName("moneytext");
        a.getRenderer(TextRenderer.class).setText(parkHandler.getParkWallet().getMoneyString());
        a.setConstraintHorizontalAlign(HorizontalAlign.left);
        a.setId("moneytext");
        
        a=nifty.getCurrentScreen().findElementByName("loantext");
        a.getRenderer(TextRenderer.class).setText(parkHandler.getParkWallet().getLoanString());
        a.setConstraintHorizontalAlign(HorizontalAlign.left);
        a.setId("loantext");
        
        a=nifty.getCurrentScreen().findElementByName("guestnumtext");
        a.getRenderer(TextRenderer.class).setText(parkHandler.getGuestSizeString());
        a.setConstraintHorizontalAlign(HorizontalAlign.left);
        a.setId("guestnumtext");
    }
    public void updateClickingIndicator(){
        nifty=Main.nifty;
        Element a=nifty.getCurrentScreen().findElementByName("clickmodeindicator");
        NiftyImage img = null;
        
        switch(clickingHandler.clickMode){
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
    public void rideDemolishToggle(){
        for(BasicRide r:parkHandler.getRides()){
            if(r.getRideID()==windowMaker.getRideID()){
                r.demolish();
                closeWindows("");
                break;
            }
        }
        
        
    }
    public void shopDemolishToggle(){
        for(BasicShop s:parkHandler.getShops()){
            if(s.shopID==windowMaker.getShopID()){
                s.demolish();
                closeWindows("");
                break;
            }
        }
    }

    public void onStartScreen() {
        
        closeWindows("");
        //laita raha kuva oikeaan asentoon
        Element a=screen.findElementByName("buttonlayer").findElementByName("buttons").findElementByName("moneyicon");
        int j=parkHandler.settings.getWidth();
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
        updateshopdesc();
        
    }

    public void onEndScreen() {
    }
    public void testSave(){
        saveManager.Save("testfilexd");
    }
    public void clickDemolishbutton(){
        if(clickingHandler.clickMode==ClickingModes.DEMOLITION){
            clickingHandler.clickMode=ClickingModes.NOTHING;
        }
        else{
           clickingHandler.clickMode=ClickingModes.DEMOLITION; 
        }
        updateClickingIndicator();
    }
    @NiftyEventSubscriber(id = "sliderbrushsize")
    public void onSliderChange(String id, SliderChangedEvent event) {
        terrainHandler.setBrushSize((int) event.getValue());
        updateBrushSize();
    }

    

    @NiftyEventSubscriber(id = "usetexture")
    public void useTextureChange(String id, CheckBoxStateChangedEvent event) {
        terrainHandler.setUseTexture(event.isChecked());
    }
    @NiftyEventSubscriber(id = "queroad")
    public void queCheckboxChange(String id, CheckBoxStateChangedEvent event) {
        roadMaker.queroad = event.isChecked();
        
    }
    @NiftyEventSubscriber(id = "textureforshovel")
    public void onTextureChange(String id, ImageSelectSelectionChangedEvent event) {
        terrainHandler.setTextureID(event.getImageSelect().getSelectedImageIndex() + 1);
    }
    @NiftyEventSubscriber(id="guests")
    public void DropDownSelectionChangedEvent(String id,DropDownSelectionChangedEvent event)  {
        
        int index=event.getSelectionItemIndex();
        if(index==0){
            //selected default  
        }
        else{
            Guest guest = null;
            for(Guest g:npcManager.guests){
                if(g.getGuestNum()==index-1){
                    guest=g;
                    break;
                }
            }
            if(guest==null){
                System.out.println("Did not find guest with that index");
                return;
            }
            windowMaker.createGuestWindow(guest,true);
            Element element = nifty.getCurrentScreen().findElementByName("NPCWindow");
            element.setVisible(false);
        }
        
    }
    @NiftyEventSubscriber(id = "guestnametextfield")
    public void onguestnameChanged(final String id, final TextFieldChangedEvent event) {
        if(!event.getText().equals("")) {
            Guest guest=windowMaker.getCurrentGuestWindowGuest();
            guest.setName(event.getText());
            windowMaker.updateGuestWindow(guest);
        }
    }
    @NiftyEventSubscriber(id = "ridenametextfield")
    public void onRideNameChanged(final String id, final TextFieldChangedEvent event) {
        if(!event.getText().equals("")) {
            BasicRide ride=windowMaker.getCurrentRide();
            ride.setName(event.getText());
            windowMaker.updateRideWindow(false);
        }
    }
    @NiftyEventSubscriber(id = "ridepriceslider")
    public void onRidePriceChanged(final String id, final SliderChangedEvent event) {
            BasicRide ride=windowMaker.getCurrentRide();
            ride.setPrice(event.getValue());
            windowMaker.updateRideWindow(false);
        
    }
    
    public void rideStatusToggle(){
        windowMaker.handleRideStatusToggle();
    }
    public int getBrushSize() {
        return terrainHandler.getBrushSize();
    }

    public void updateBrushSize() {
        Element niftyElement = nifty.getCurrentScreen().findElementByName("shovelbrushsize");
        niftyElement.getRenderer(TextRenderer.class).setText(Integer.toString(getBrushSize()));
    }
    public void toggleShovelWindow() {
        closeWindows("shovelWindow");
        Element niftyElement = nifty.getCurrentScreen().findElementByName("shovelWindow");
        updateShovelWindow();
        niftyElement.setVisible(!niftyElement.isVisible());
        if (niftyElement.isVisible() == true) {
            clickingHandler.clickMode = ClickingModes.TERRAIN;

        } else {
            clickingHandler.clickMode = ClickingModes.NOTHING;
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
            clickingHandler.clickMode = ClickingModes.ROAD;


        } else {

            clickingHandler.clickMode = ClickingModes.NOTHING;
        }
        updateClickingIndicator();
    }
    public void toggleDecorationWindow(){
        closeWindows("decorationWindow");
        Element niftyElement = nifty.getCurrentScreen().findElementByName("decorationWindow");
        updateDecorationWindow();
        niftyElement.setVisible(!niftyElement.isVisible());
        if (niftyElement.isVisible() == true) {
            clickingHandler.clickMode = ClickingModes.DECORATION;


        } else {

            clickingHandler.clickMode = ClickingModes.NOTHING;
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
        ArrayList<Guest> guests=parkHandler.getGuests();
    DropDown drop= screen.findNiftyControl("guests",DropDown.class);
    drop.clear();
    drop.addItem("default");
    for(Guest g:guests){
        String guest=Integer.toString(g.getGuestNum())+" - "+g.getName();
        drop.addItem(guest);
    }
    drop.getElement().setConstraintHorizontalAlign(HorizontalAlign.left);
    drop.getElement().setConstraintVerticalAlign(VerticalAlign.top);
    }
    private void roadDirectionReset(){
        Element niftyElement;
        niftyElement = nifty.getCurrentScreen().findElementByName("roaddownimg");
        niftyElement.stopEffect(EffectEventId.onCustom);
        niftyElement = nifty.getCurrentScreen().findElementByName("roadrightimg");
        niftyElement.stopEffect(EffectEventId.onCustom);
        niftyElement = nifty.getCurrentScreen().findElementByName("roadleftimg");
        niftyElement.stopEffect(EffectEventId.onCustom);
        niftyElement = nifty.getCurrentScreen().findElementByName("roadupimg");
        niftyElement.stopEffect(EffectEventId.onCustom);
    }
    private void roadHillReset(){
        Element niftyElement;
        niftyElement = nifty.getCurrentScreen().findElementByName("roaddownhillimg");
        niftyElement.stopEffect(EffectEventId.onCustom);
        niftyElement = nifty.getCurrentScreen().findElementByName("roadflatimg");
        niftyElement.stopEffect(EffectEventId.onCustom);
        niftyElement = nifty.getCurrentScreen().findElementByName("roaduphillimg");
        niftyElement.stopEffect(EffectEventId.onCustom);
    }
    public void roadDirectionUp() {
        roadMaker.direction = Direction.UP;
        roadDirectionReset();
        Element niftyElement = nifty.getCurrentScreen().findElementByName("roadupimg");
        niftyElement.startEffect(EffectEventId.onCustom);
        //resettaa muitten effectit


    }

    public void roadDirectionDown() {
        roadMaker.direction = Direction.DOWN;
        roadDirectionReset();
        Element niftyElement = nifty.getCurrentScreen().findElementByName("roaddownimg");
        niftyElement.startEffect(EffectEventId.onCustom);
        
    }

    public void roadDirectionRight() {
        roadMaker.direction = Direction.RIGHT;
        roadDirectionReset();
        Element niftyElement = nifty.getCurrentScreen().findElementByName("roadrightimg");
        niftyElement.startEffect(EffectEventId.onCustom);
        
    }

    public void roadDirectionLeft() {
        roadMaker.direction = Direction.LEFT;
        roadDirectionReset();
        Element niftyElement = nifty.getCurrentScreen().findElementByName("roadleftimg");
        niftyElement.startEffect(EffectEventId.onCustom);
        
    }

    public void roadUpHill() {
        roadMaker.hill = RoadHill.UP;
        roadHillReset();
        Element niftyElement = nifty.getCurrentScreen().findElementByName("roaduphillimg");
        niftyElement.startEffect(EffectEventId.onCustom);
        
    }

    public void roadFlatHill() {
        roadMaker.hill = RoadHill.FLAT;
        roadHillReset();
        Element niftyElement = nifty.getCurrentScreen().findElementByName("roadflatimg");
        niftyElement.startEffect(EffectEventId.onCustom);
        
    }

    public void roadDownHill() {
        roadMaker.hill = RoadHill.DOWN;
        roadHillReset();
        Element niftyElement = nifty.getCurrentScreen().findElementByName("roaddownhillimg");
        niftyElement.startEffect(EffectEventId.onCustom);
        
    }

    public void buildButton() {
        roadMaker.buildRoad();
    }

    public void selectionButton() {
        roadMaker.status = RoadMakerStatus.CHOOSING;
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
        if(!elementname.equals("decorationWindow")){
            niftyElement = nifty.getCurrentScreen().findElementByName("decorationWindow");
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
        Element niftyElement = nifty.getCurrentScreen().findElementByName("buildingDescName");
        setelementText(niftyElement,getshopname());
        niftyElement = nifty.getCurrentScreen().findElementByName("buildingDescPrice");
        setelementText(niftyElement,getshopprice());
        niftyElement = nifty.getCurrentScreen().findElementByName("buildingDescDesc");
        setelementText(niftyElement,getshopdesc());
        String bigshoppic=descriptionManager.bigpic;
        
        NiftyImage img = nifty.getRenderEngine().createImage(nifty.getCurrentScreen(),bigshoppic, false);
        niftyElement = nifty.getCurrentScreen().findElementByName("shopbigpic");
        niftyElement.getRenderer(ImageRenderer.class).setImage(img);

    }
    private void setelementText(Element element,String text){
        System.out.println(element.getRenderer(TextRenderer.class).getOriginalText()+ " Changed to "+ text);
        element.getRenderer(TextRenderer.class).setText(text);
        element.getRenderer(TextRenderer.class).setTextHAlign(HorizontalAlign.left);
        element.getRenderer(TextRenderer.class).setTextVAlign(VerticalAlign.top);
    }
    public void turnDecorationLeft(){
        decorationManager.turnLeft();
        updateDecorationWindow();
        
    }
    public void turnDecorationRight(){
        decorationManager.turnRight();
        updateDecorationWindow();
    }
    private String getDecorationArrow(){
        return decorationManager.getArrow();
    }
    private void selectRock(){
        decorationManager.select(Decorations.ROCK);
        updateDecorationWindow();
    }

    private void updateDecorationWindow() {
        Element element=nifty.getCurrentScreen().findElementByName("decorationname");
        String text=decorationManager.getDecorationName();
        setelementText(element,text);
        element=nifty.getCurrentScreen().findElementByName("decorationdescription");
        text=decorationManager.getDecorationDescription();
        setelementText(element,text);
        NiftyImage img = nifty.getRenderEngine().createImage(nifty.getCurrentScreen(),getDecorationArrow(), false);
        Element niftyElement = nifty.getCurrentScreen().findElementByName("decorationdirectionimg");
        niftyElement.getRenderer(ImageRenderer.class).setImage(img);
        niftyElement.setId("decorationdirectionimg");
    }
    @Subscribe public void listenUpdateRoadDirectionEvent(UpdateRoadDirectionEvent event){
        switch(event.getD()){
            case DOWN:
                roadDirectionDown();
                break;
                
            case LEFT:
                roadDirectionLeft();
                break;
                
            case RIGHT:
                roadDirectionRight();
                break;
                
            case UP:
                roadDirectionUp();
                
        }
    }
    public void toggleShovelSizePlus(){
        terrainHandler.brushSizePlus();
        updateShovelWindow();
    }
    public void toggleShovelSizeMinus(){
        terrainHandler.brushSizeMinus();
        updateShovelWindow();
    }
    private void updateShovelWindow(){
        updateBrushSize();
    }
    /**
     * Called when selecting buildings in building window (UI)
     * @param selection In format TAB(int):INDEX(int)
     * For example 2:1  =>> tab 2 index 1
     */
    public void selectbuilding(String selection){
        System.out.println(selection);
        String[] splittedString = selection.split(":");
        try{
            int tab=Integer.parseInt(splittedString[0]);
            int index=Integer.parseInt(splittedString[1]);
            eventBus.post(new BuildingSelectionEvent(tab, index));
        }catch (NullPointerException n){
            logger.log(Level.SEVERE,"Critical error parsing selected building: selection in wrong format! ",selection);
        }
        
    }
    @Subscribe
    public void listenUpdateBuildingUIEvent(UpdateBuildingUIEvent event){
        descriptionManager.setDescription(event.buildable);
        updateshopdesc();
    }
    @Subscribe
    public void listenCloseWindowsEvent(CloseWindowsEvent event){
        closeWindows(event.parameters);
    }
}
