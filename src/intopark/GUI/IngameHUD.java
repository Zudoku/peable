/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package intopark.GUI;

import intopark.GUI.events.UpdateMoneyTextBarEvent;
import intopark.GUI.events.UpdateRoadDirectionEvent;
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
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import intopark.GUI.events.CloseWindowsEvent;
import intopark.Main;
import intopark.inout.SaveManager;
import intopark.inputhandler.ClickingHandler;
import intopark.inputhandler.ClickingModes;
import intopark.npc.Guest;
import intopark.npc.NPCManager;
import intopark.ride.BasicRide;
import intopark.shops.BasicBuildables;
import intopark.shops.BasicShop;
import intopark.shops.BuildingSelectionEvent;
import intopark.shops.ShopManager;
import intopark.GUI.events.UpdateBuildingUIEvent;
import static intopark.inputhandler.ClickingModes.DECORATION;
import static intopark.inputhandler.ClickingModes.DEMOLITION;
import static intopark.inputhandler.ClickingModes.NOTHING;
import static intopark.inputhandler.ClickingModes.PLACE;
import static intopark.inputhandler.ClickingModes.RIDE;
import static intopark.inputhandler.ClickingModes.ROAD;
import static intopark.inputhandler.ClickingModes.TERRAIN;
import intopark.terrain.Direction;
import static intopark.terrain.Direction.SOUTH;
import static intopark.terrain.Direction.WEST;
import static intopark.terrain.Direction.EAST;
import static intopark.terrain.Direction.NORTH;
import intopark.terrain.ParkHandler;
import intopark.roads.RoadHill;
import intopark.roads.RoadMaker;
import intopark.roads.RoadMakerStatus;
import intopark.terrain.TerrainHandler;
import intopark.terrain.decoration.DecorationManager;
import intopark.terrain.decoration.Decorations;

/**
 *
 * @author arska
 */
public class IngameHUD implements ScreenController {
    //LOGGER
    private static final Logger logger = Logger.getLogger(IngameHUD.class.getName());
    //DEPENDENCIES
    public Nifty nifty;
    public Screen screen;
    private ShopDescriptionManager descriptionManager = new ShopDescriptionManager();
    @Inject private TerrainHandler terrainHandler;
    @Inject private ParkHandler parkHandler;
    @Inject private ClickingHandler clickingHandler;
    @Inject private RoadMaker roadMaker;
    @Inject ShopManager shopManager;
    @Inject EventBus eventBus;
    @Inject private WindowHandler windowMaker;
    @Inject NPCManager npcManager;
    @Inject SaveManager saveManager;
    @Inject private DecorationManager decorationManager;
    //VARIABLES
    public boolean shovel = false;
    public BasicBuildables selectedBuilding = BasicBuildables.NULL;
    NiftyImage newImage;
    
    
    /*
     * Called by Nifty
     */
    public IngameHUD() {
        Injector i=Main.injector;
        i.injectMembers(this);
        eventBus.register(this);
    }
    /*
     * Called by Nifty.
     */
    public void bind(Nifty nifty, Screen screen) {
        this.nifty = nifty;
        this.screen = screen;
        
    }
    /**
     * Called by Nifty when this screen is activated.
     * Moves the guest-money bar on its right place.
     * It moves them relative to screenwidth.
     */
    public void onStartScreen() {
        closeWindows("");
        //laita raha kuva oikeaan asentoon
        Element a=screen.findElementByName("buttonlayer").findElementByName("buttons").findElementByName("moneyicon");
        int screenwidth=parkHandler.settings.getWidth();
        int elementlocation=screenwidth-300;
        String b=Integer.toString(elementlocation); 
        a.setConstraintX(new SizeValue(b));
        //tekstit
        a=screen.findElementByName("buttonlayer").findElementByName("buttons").findElementByName("moneytext");
        elementlocation=screenwidth-170;
        b=Integer.toString(elementlocation);
        a.setConstraintX(new SizeValue(b));
        
        a=screen.findElementByName("buttonlayer").findElementByName("buttons").findElementByName("loantext");
        elementlocation=screenwidth-170;
        b=Integer.toString(elementlocation);
        a.setConstraintX(new SizeValue(b));
        
        a=screen.findElementByName("buttonlayer").findElementByName("buttons").findElementByName("guestnumtext");
        elementlocation=screenwidth-280;
        b=Integer.toString(elementlocation);
        a.setConstraintX(new SizeValue(b));
        
        a=screen.findElementByName("buttonlayer").findElementByName("buttons").findElementByName("clickmodeindicator");
        elementlocation=screenwidth-364;
        b=Integer.toString(elementlocation);
        a.setConstraintX(new SizeValue(b));
        
        updateMoneytextbar();
        updateshopdesc();
        
    }
    /**
     * Called by Nifty on when the screen exits.
     */
    public void onEndScreen() {
        
    }
    /**
     * Save the scenario to file.
     */
    public void testSave(){
        saveManager.Save("testfilexd");
    }
    /**
     * Close open all open windows.
     * @param elementname if "" close everything.
     * Else close everything but the elementname.
     */
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
    /**
     * This will update the Labels (element) text with string provided.
     * Also resets H-V_Allign to top-left. 
     * @param element Label element.
     * @param text New text
     */
    private void setelementText(Element element,String text){
        logger.log(Level.FINEST,"{0} Changed to {1}",new Object[]{element.getRenderer(TextRenderer.class).getOriginalText(),text});
        element.getRenderer(TextRenderer.class).setText(text);
        element.getRenderer(TextRenderer.class).setTextHAlign(HorizontalAlign.left);
        element.getRenderer(TextRenderer.class).setTextVAlign(VerticalAlign.top);
    }
    /*
     * REFRESH SOMETHING IN THE UI.
     */
    
    /**
     * Refreshes the guest and money counter.
     */
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
    /**
     * Refresh the icon indicating what clickmode you have.
     */
    public void updateClickingIndicator(){
        nifty=Main.nifty;
        Element a=nifty.getCurrentScreen().findElementByName("clickmodeindicator");
        NiftyImage img = null;
        
        switch(clickingHandler.getClickMode()){
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
    
    /**
     * EVENTBUS LISTENERS
     * These methods will get called when some other class posts these EventBus events.
     */
    
    
    /**
     * Updates the UI so that guests money and such are updated. 
     * @param e 
     */
    @Subscribe public void processMoneyTextBarUpdate(UpdateMoneyTextBarEvent e) {
        updateMoneytextbar();
    }
    
    /**
     * Update BuildingUI. Set a description on the building shop ui.
     * @param event 
     */
    @Subscribe
    public void listenUpdateBuildingUIEvent(UpdateBuildingUIEvent event){
        descriptionManager.setDescription(event.buildable);
        updateshopdesc();
    }
    /**
     * Close open windows.
     * @param event 
     */
    @Subscribe
    public void listenCloseWindowsEvent(CloseWindowsEvent event){
        closeWindows(event.parameters);
    }
    
    /*
     * ALL RELATED TO TOP BAR 
     */
    public void clickDemolishbutton(){
        if(clickingHandler.getClickMode()==ClickingModes.DEMOLITION){
            clickingHandler.setClickMode(ClickingModes.NOTHING);
        }
        else{
           clickingHandler.setClickMode(ClickingModes.DEMOLITION); 
        }
        updateClickingIndicator();
    }
    public void toggleShovelWindow() {
        closeWindows("shovelWindow");
        Element niftyElement = nifty.getCurrentScreen().findElementByName("shovelWindow");
        updateShovelWindow();
        niftyElement.setVisible(!niftyElement.isVisible());
        if (niftyElement.isVisible() == true) {
            clickingHandler.setClickMode(ClickingModes.TERRAIN);

        } else {
            clickingHandler.setClickMode(ClickingModes.NOTHING);
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
            clickingHandler.setClickMode(ClickingModes.ROAD);


        } else {

            clickingHandler.setClickMode(ClickingModes.NOTHING);
        }
        updateClickingIndicator();
    }
    public void toggleDecorationWindow(){
        closeWindows("decorationWindow");
        Element niftyElement = nifty.getCurrentScreen().findElementByName("decorationWindow");
        updateDecorationWindow();
        niftyElement.setVisible(!niftyElement.isVisible());
        if (niftyElement.isVisible() == true) {
            clickingHandler.setClickMode(ClickingModes.DECORATION);
        } else {
            clickingHandler.setClickMode(ClickingModes.NOTHING);
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
    /*
     * ALL RELATED TO GUEST UI.
     */
    
    /**
     * Called when the user clicks a guest on the guest-list window.
     * @param id
     * @param event 
     */
    @NiftyEventSubscriber(id="guests")
    public void DropDownSelectionChangedEvent(String id,DropDownSelectionChangedEvent event)  {
        int index=event.getSelectionItemIndex();
        if(index==0){
            //selected default  
        }
        else{
            Guest guest = null;
            for(Guest g:npcManager.getGuests()){
                if(g.getGuestNum()==index-1){
                    guest=g;
                    break;
                }
            }
            if(guest==null){
                logger.log(Level.WARNING,"No such guest with guestID {0}",index-1);
                return;
            }
            windowMaker.createGuestWindow(guest,true);
            Element element = nifty.getCurrentScreen().findElementByName("NPCWindow");
            element.setVisible(false);
        }
        
    }
    /**
     * Called when user changes guests name on the guest window.
     * @param id
     * @param event 
     */
    @NiftyEventSubscriber(id = "guestnametextfield")
    public void onguestnameChanged(final String id, final TextFieldChangedEvent event) {
        if(!event.getText().equals("")) {
            Guest guest=windowMaker.getCurrentGuestWindowGuest();
            guest.setName(event.getText());
            windowMaker.updateGuestWindow(guest);
        }
    }
    /**
     * Refreshes the guest-list windows content.
     */
    public void updateNPCBox() {
        List<Guest> guests = parkHandler.getGuests();
        DropDown drop = screen.findNiftyControl("guests", DropDown.class);
        drop.clear();
        drop.addItem("default");
        for (Guest g : guests) {
            String guest = Integer.toString(g.getGuestNum()) + " - " + g.getName();
            drop.addItem(guest);
        }
        drop.getElement().setConstraintHorizontalAlign(HorizontalAlign.left);
        drop.getElement().setConstraintVerticalAlign(VerticalAlign.top);
    }
    /*
     * ALL RELATED TO TERRAIN UI.
     */
    
    @NiftyEventSubscriber(id = "sliderbrushsize")
    public void onSliderChange(String id, SliderChangedEvent event) {
        terrainHandler.setBrushSize((int) event.getValue());
        updateBrushSize();
    }
    @NiftyEventSubscriber(id = "usetexture")
    public void useTextureChange(String id, CheckBoxStateChangedEvent event) {
        terrainHandler.setUseTexture(event.isChecked());
    }
    @NiftyEventSubscriber(id = "textureforshovel")
    public void onTextureChange(String id, ImageSelectSelectionChangedEvent event) {
        terrainHandler.setTextureID(event.getImageSelect().getSelectedImageIndex() + 1);
    }
    public void updateBrushSize() {
        Element niftyElement = nifty.getCurrentScreen().findElementByName("shovelbrushsize");
        niftyElement.getRenderer(TextRenderer.class).setText(Integer.toString(getBrushSize()));
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
    /*
     * ALL RELATED TO SHOP UI.
     */
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
    /**
     * Called when user clicks demolish button on shop UI. Calls to demolish the shop.
     */
    public void shopDemolishToggle(){
        for(BasicShop s:parkHandler.getShops()){
            if(s.getShopID()==windowMaker.getShopID()){
                s.demolish();
                closeWindows("");
                break;
            }
        }
    }
    public void toggleShopUpgrade1(){
        
    }
    public void toggleShopUpgrade2(){
        
    }
    public void toggleShopUpgrade3(){
        
    }
    public void toggleShopUpgrade4(){
        
    }
    /*
     * ALL RELATED TO RIDE UI.
     */
    
    /**
     * Called when selecting buildings in building window (UI)
     * @param selection enum BASICBUILDABLES in string-form
     */
    public void selectbuilding(String selection){
        logger.log(Level.FINEST,"Selecting building {0}",selection);
        try{
            BasicBuildables sel=BasicBuildables.valueOf(selection);
            eventBus.post(new BuildingSelectionEvent(sel));
        }catch (IllegalArgumentException n){
            logger.log(Level.SEVERE, "Critical error parsing selected building: selection in wrong format! ,{0} is not maching ENUM.",selection);
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
    /**
     * Called when user clicks demolish button on ride UI. Calls to demolish the ride.
     */
    public void rideDemolishToggle() {
        for (BasicRide r : parkHandler.getRides()) {
            if (r.getRideID() == windowMaker.getRideID()) {
                r.demolish();
                closeWindows("");
                break;
            }
        }
    }
    /*
     * ALL RELATED TO ROAD UI.
     */
    @Subscribe public void listenUpdateRoadDirectionEvent(UpdateRoadDirectionEvent event){
        switch(event.getD()){
            case SOUTH:
                roadDirectionDown();
                break;
                
            case WEST:
                roadDirectionLeft();
                break;
                
            case EAST:
                roadDirectionRight();
                break;
                
            case NORTH:
                roadDirectionUp();  
        }
    }
    @NiftyEventSubscriber(id = "queroad")
    public void queCheckboxChange(String id, CheckBoxStateChangedEvent event) {
        roadMaker.setQueroad( event.isChecked());  
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
        roadMaker.setDirection(Direction.NORTH);
        roadDirectionReset();
        Element niftyElement = nifty.getCurrentScreen().findElementByName("roadupimg");
        niftyElement.startEffect(EffectEventId.onCustom);
    }
    public void roadDirectionDown() {
        roadMaker.setDirection(Direction.SOUTH);
        roadDirectionReset();
        Element niftyElement = nifty.getCurrentScreen().findElementByName("roaddownimg");
        niftyElement.startEffect(EffectEventId.onCustom);
    }
    public void roadDirectionRight() {
        roadMaker.setDirection(Direction.EAST);
        roadDirectionReset();
        Element niftyElement = nifty.getCurrentScreen().findElementByName("roadrightimg");
        niftyElement.startEffect(EffectEventId.onCustom);
    }
    public void roadDirectionLeft() {
        roadMaker.setDirection(Direction.WEST);
        roadDirectionReset();
        Element niftyElement = nifty.getCurrentScreen().findElementByName("roadleftimg");
        niftyElement.startEffect(EffectEventId.onCustom);
    }
    public void roadUpHill() {
        roadMaker.setHill(RoadHill.UP);
        roadHillReset();
        Element niftyElement = nifty.getCurrentScreen().findElementByName("roaduphillimg");
        niftyElement.startEffect(EffectEventId.onCustom);
    }
    public void roadFlatHill() {
        roadMaker.setHill(RoadHill.FLAT);
        roadHillReset();
        Element niftyElement = nifty.getCurrentScreen().findElementByName("roadflatimg");
        niftyElement.startEffect(EffectEventId.onCustom);
    }
    public void roadDownHill() {
        roadMaker.setHill(RoadHill.DOWN);
        roadHillReset();
        Element niftyElement = nifty.getCurrentScreen().findElementByName("roaddownhillimg");
        niftyElement.startEffect(EffectEventId.onCustom);
    }
    public void buildButton() {
        roadMaker.buildRoad();
    }
    public void selectionButton() {
        roadMaker.setStatus(RoadMakerStatus.CHOOSING);
    }
    /*
     * ALL RELATED TO DECORATION UI.
     */
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
    /*
     * GETTERS.
     */
    public int getBrushSize() {
        return terrainHandler.getBrushSize();
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
}
        
    

