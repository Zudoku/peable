/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package intopark.GUI;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.jme3.math.Vector3f;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.Button;
import de.lessvoid.nifty.controls.CheckBox;
import de.lessvoid.nifty.controls.DropDown;
import de.lessvoid.nifty.controls.ListBox;
import de.lessvoid.nifty.controls.Slider;
import de.lessvoid.nifty.controls.TextField;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.ImageRenderer;
import de.lessvoid.nifty.elements.render.TextRenderer;
import de.lessvoid.nifty.layout.align.HorizontalAlign;
import de.lessvoid.nifty.layout.align.VerticalAlign;
import de.lessvoid.nifty.render.NiftyImage;
import java.util.logging.Level;
import java.util.logging.Logger;
import intopark.Main;
import intopark.inout.Identifier;
import intopark.npc.Guest;
import intopark.npc.NPCManager;
import intopark.npc.inspector.Inspection;
import intopark.npc.inspector.InspectionComment;
import intopark.npc.inventory.Item;
import intopark.ride.BasicRide;
import intopark.shops.BasicShop;
import intopark.shops.ShopReputation;
import intopark.shops.ShopUpgradeContainer;
import intopark.terrain.ParkHandler;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author arska
 */
@Singleton
public class WindowHandler {
    //LOGGER
    private static final Logger logger = Logger.getLogger(IngameHUD.class.getName());
    //DEPENDENCIES
    Nifty nifty;
    @Inject ParkHandler parkHandler;
    @Inject NPCManager npcManager;
    @Inject Identifier identifier;
    //VARIABLES
    private int lastIDforGuest;
    private int lastIDforShop;
    private int lastIDforRide;

    private int amountCounter=200;

    public WindowHandler() {
        nifty = Main.nifty;
    }
    private void updateNifty() {
        nifty = Main.nifty;
    }
    /* GUEST WINDOW CREATION AND UPDATE METHODS */

    public void updateGuestWindow(boolean toggleVisible, boolean updateTextField) {
        updateNifty();
        Object object=identifier.getObjectWithID(lastIDforGuest);
        if(!(object instanceof Guest)){
            logger.log(Level.SEVERE, "ID corruption. ID {0} should return Guest",lastIDforGuest);
            return;
        }
        Guest guest = (Guest)object;
        Element guestwindow = nifty.getCurrentScreen().getLayerElements().get(2).findElementByName("guesttemplate");
        /**
         * TAB 1
         */
        updateGuestNameText(guestwindow, guest.getName());
        updateGuestAgeText(guestwindow, guest.getSize()==1?"adult":"child");
        updateGuestGenderText(guestwindow,guest.isMale()?"female":"male");
        updateGuestWalletText(guestwindow, guest.getWallet().toString());
        updateGuestPrefText(guestwindow,guest.getStats().getPreferredRide().toString());
        updateGuestStatusText(guestwindow, guest.getStats().getCurrentEmote().toString());
        /**
         * TAB 2
         */
        updateGuestHungerText(guestwindow, guest.getStats().getHunger());
        updateGuestThirstyText(guestwindow, guest.getStats().getThirst());
        updateGuestHappynessText(guestwindow, guest.getStats().getHappyness());
        /**
         * TAB 3
         */
        if (updateTextField) {
            updateGuestNameTextField(guest.getName());
        }
        /**
         * TAB 4
         */
        updateGuestInventoryTexts(guestwindow, guest.getInventory());
        if (toggleVisible) {
            guestwindow.setVisible(true);
        }

    }
    private void updateGuestInventoryTexts(Element guestWindow,List<Item> inv){
        //TODO: create a loop to clear them all. O_o
        int counter = 1;
        Element niftyElement = guestWindow.findElementByName("guestinventory1");
        updateText(niftyElement, "");
        niftyElement = guestWindow.findElementByName("guestinventory2");
        updateText(niftyElement, "");
        niftyElement = guestWindow.findElementByName("guestinventory3");
        updateText(niftyElement, "");
        niftyElement = guestWindow.findElementByName("guestinventory4");
        updateText(niftyElement, "");
        niftyElement = guestWindow.findElementByName("guestinventory5");
        updateText(niftyElement, "");
        niftyElement = guestWindow.findElementByName("guestinventory6");
        updateText(niftyElement, "");
        niftyElement = guestWindow.findElementByName("guestinventory7");
        updateText(niftyElement, "");
        niftyElement = guestWindow.findElementByName("guestinventory8");
        updateText(niftyElement, "");
        niftyElement = guestWindow.findElementByName("guestinventory9");
        updateText(niftyElement, "");


        for (Item item : inv) {
            String elementname = "guestinventory" + Integer.toString(counter);
            niftyElement = guestWindow.findElementByName(elementname);
            updateText(niftyElement, item.toString());
            counter++;
            if (counter == 10) {
                break;
            }
        }
    }
    private void updateGuestNameTextField(String name){
        TextField textfield = nifty.getCurrentScreen().findNiftyControl("guestnametextfield", TextField.class);
        textfield.setText(name);
    }
    private void updateGuestNameText(Element guestWindow, String name) {
        Element updatedText = guestWindow.findElementByName("guestname");
        updateText(updatedText, name);
    }
    private void updateGuestGenderText(Element guestWindow, String gender) {
        Element updatedText = guestWindow.findElementByName("guestgender");
        updateText(updatedText, gender);
    }
    private void updateGuestPrefText(Element guestWindow, String preference) {
        Element updatedText = guestWindow.findElementByName("guestpref");
        updateText(updatedText, preference);
    }
    private void updateGuestWalletText(Element guestWindow, String money) {
        Element updatedText = guestWindow.findElementByName("guestwallet");
        updateText(updatedText, money);
    }

    private void updateGuestAgeText(Element guestWindow, String age) {
        Element updatedText = guestWindow.findElementByName("guestage");
        updateText(updatedText, age);
    }

    private void updateGuestStatusText(Element guestWindow, String status) {
        Element updatedText = guestWindow.findElementByName("gueststatus");
        updateText(updatedText, status);
    }

    private void updateGuestReputationText(Element guestWindow, String repu) {
        Element updatedText = guestWindow.findElementByName("guestreputation");
        updateText(updatedText, repu);
    }

    private void updateGuestHungerText(Element guestWindow, int stat) {
        Element updatedText = guestWindow.findElementByName("guesthunger");
        updateText(updatedText, Integer.toString(stat));
    }

    private void updateGuestThirstyText(Element guestWindow, int stat) {
        Element updatedText = guestWindow.findElementByName("guestthirst");
        updateText(updatedText, Integer.toString(stat));
    }

    private void updateGuestHappynessText(Element guestWindow, int stat) {
        Element updatedText = guestWindow.findElementByName("guesthappyness");
        updateText(updatedText, Integer.toString(stat));
    }
    /* RIDE WINDOW CREATION AND UPDATE METHODS */

    private void updateRidePriceText(Element rideWindow, float price) {

        Element updatedText = rideWindow.findElementByName("rideprice");
        updateText(updatedText, Float.toString(price));
    }

    private void updateRideNameText(Element rideWindow, String name) {
        Element updatedText = rideWindow.findElementByName("ridename");
        updateText(updatedText, name);
    }

    private void updateRideTypeText(Element rideWindow, String type) {
        Element updatedText = rideWindow.findElementByName("ridetype");
        updateText(updatedText, type);
    }

    private void updateRideExitementText(Element rideWindow, float exitement) {
        Element updatedText = rideWindow.findElementByName("rideexitement");
        updateText(updatedText, Float.toString(exitement));
    }

    private void updateRideNauseaText(Element rideWindow, float nausea) {
        Element updatedText = rideWindow.findElementByName("ridenausea");
        updateText(updatedText, Float.toString(nausea));
    }

    private void updateRideStatusText(Element rideWindow, boolean status) {
        Element updatedText = rideWindow.findElementByName("ridestatus");
        if (status == true) {
            updateText(updatedText, "Open!");
        } else {
            updateText(updatedText, "Closed");
        }

    }

    private void updateRideBrokenText(Element rideWindow, float broken) {
        Element updatedText = rideWindow.findElementByName("ridebroken");
        updateText(updatedText, Float.toString(broken));
    }

    private void updateRideCustomersText(Element rideWindow, float customers) {
        Element updatedText = rideWindow.findElementByName("ridecustomers");
        updateText(updatedText, Float.toString(customers));
    }

    private void updateRideCustomersLifeText(Element rideWindow, float customers) {
        Element updatedText = rideWindow.findElementByName("ridecustomerslife");
        updateText(updatedText, Float.toString(customers));
    }

    private void updateRideCustomersHourText(Element rideWindow, double customers) {
        Element updatedText = rideWindow.findElementByName("ridecustomershour");
        updateText(updatedText, Double.toString(customers));
    }

    private void updateRideMoneyGainedText(Element rideWindow, float moneytotal) {
        Element updatedText = rideWindow.findElementByName("ridemoneygained");
        updateText(updatedText, Float.toString(moneytotal));
    }

    private void updateRideMoneyHourText(Element rideWindow, double moneyhour) {
        Element updatedText = rideWindow.findElementByName("ridemoneyhour");
        updateText(updatedText, Double.toString(moneyhour));
    }

    private void updateRideCostHourText(Element rideWindow, float moneycost) {
        Element updatedText = rideWindow.findElementByName("ridehourcost");
        updateText(updatedText, Float.toString(moneycost));
    }

    private void updateRideNameTextfield(String name) {
        TextField textfield = nifty.getCurrentScreen().findNiftyControl("ridenametextfield", TextField.class);
        textfield.setText(name);
    }

    private void updateRidePriceTextTab1(Element rideWindow, float price, boolean updateSlider) {
        Element updatedText = rideWindow.findElementByName("ridepricechange");
        updateText(updatedText, Float.toString(price));
        Slider slider = nifty.getCurrentScreen().findNiftyControl("ridepriceslider", Slider.class);
        if (updateSlider) {
            slider.setValue(price);
        }

    }
    private void updateRideInspectionTime(Element rideWindow,String time){
        Element updatedText = rideWindow.findElementByName("ridelastinspect");
        updateText(updatedText, time);
    }
    private void updateRideInspectionWorkingCond(Element rideWindow,String conditions){
        Element updatedText = rideWindow.findElementByName("rideworkingconditions");
        updateText(updatedText, conditions);
    }
    private void updateRideInspectionWorkingProdQuality(Element rideWindow,String quality){
        Element updatedText = rideWindow.findElementByName("rideproductquality");
        updateText(updatedText, quality);
    }
    private void updateRideInspectionLeftImage(Element rideWindow,String image){
        Element updatedText = rideWindow.findElementByName("rideinspecttaste");
        updateText(updatedText, image);
    }
    private void updateRideInspectionInspector(Element rideWindow,String name){
        Element updatedText = rideWindow.findElementByName("rideinspectedby");
        updateText(updatedText, name);
    }
    private void updateRideInspectionPaid(Element rideWindow,String paid){
        Element updatedText = rideWindow.findElementByName("rideinspectpaid");
        updateText(updatedText, paid);
    }
    private void updateRideInspectionNotices(Element rideWindow,List<InspectionComment> comments){
        ListBox commentBox = rideWindow.findNiftyControl("ridenoticeslistbox", ListBox.class);
        commentBox.clear();
        for(InspectionComment comment : comments){
            commentBox.addItem(comment.getComment());
        }
    }
    private void updateRideInspectionTab(Element rideWindow, BasicRide ride) {
        if(ride.isValidInspection()){
            Inspection ins = ride.getLastInspection();
            updateRideInspectionTime(rideWindow, "1.1 1970");
            updateRideInspectionWorkingCond(rideWindow, ins.getWorkingConditions().getComment());
            updateRideInspectionWorkingProdQuality(rideWindow, ins.getWorkingSafety().getComment());
            updateRideInspectionLeftImage(rideWindow, Integer.toString(ins.getLeftImage()));
            updateRideInspectionInspector(rideWindow, ins.getInspector());
            updateRideInspectionPaid(rideWindow, Boolean.toString(ins.isPaid()));
            updateRideInspectionNotices(rideWindow, ins.getAdditionalComments());
        }else{
            String notInspected = "NOT INSPECTED";
            updateRideInspectionTime(rideWindow,notInspected);
            updateRideInspectionWorkingCond(rideWindow, notInspected);
            updateRideInspectionWorkingProdQuality(rideWindow, notInspected);
            updateRideInspectionLeftImage(rideWindow, notInspected);
            updateRideInspectionInspector(rideWindow, notInspected);
            updateRideInspectionPaid(rideWindow, notInspected);
            updateRideInspectionNotices(rideWindow, new ArrayList<InspectionComment>());
        }
    }

    public void updateRideWindow(boolean updateNameTextField, boolean toggleVisible) {
        updateNifty();
        Object object=identifier.getObjectWithID(lastIDforRide);
        if(!(object instanceof BasicRide)){
            logger.log(Level.SEVERE, "ID corruption. ID {0} should return Ride",lastIDforRide);
            return;
        }
        BasicRide ride = (BasicRide)object;
        Element rideWindow = nifty.getCurrentScreen().findElementByName("ridetemplate");
        /**
         * TAB 1
         */

        if (updateNameTextField) {
            updateRideNameTextfield(ride.getName());
        }
        updateRidePriceTextTab1(rideWindow, ride.getPrice(), false);


        /**
         * TAB 2
         */
        updateRideInspectionTab(rideWindow,ride);
        /**
        updateRidePriceText(rideWindow, ride.getPrice());
        updateRideNameText(rideWindow, ride.getName());
        updateRideTypeText(rideWindow, ride.getRide().toString());
        updateRideExitementText(rideWindow, ride.getExitement());
        updateRideNauseaText(rideWindow, ride.getNausea());
        updateRideStatusText(rideWindow, ride.getStatus());
        updateRideBrokenText(rideWindow, ride.getBroken());
        **/

        /**
         * TAB 3
         */
        updateRideStatusText(rideWindow, ride.getStatus());
        /**
        updateRideCustomersText(rideWindow, ride.customers());
        updateRideCustomersLifeText(rideWindow, ride.getCustomersTotal());
        updateRideCustomersHourText(rideWindow, ride.getGuestRateHour());
        updateRideMoneyGainedText(rideWindow, ride.getMoneyGainedTotal());
        updateRideMoneyHourText(rideWindow, ride.getMoneyRateHour());
        updateRideCostHourText(rideWindow, ride.getRepairCost());
        **/

        if (toggleVisible) {
            rideWindow.setVisible(true);
        }
    }

    public void handleRideStatusToggle() {
        Object object = parkHandler.getObjectWithID(lastIDforRide);
        if (!(object instanceof BasicRide)) {
            logger.log(Level.SEVERE, "ID corruption. ID {0} should return Ride",lastIDforRide);
            return;
        }
        BasicRide ride = (BasicRide) object;
        updateRideToggleImage(ride.toggleStatus());
        updateRideWindow(false, false);
    }

    private void updateRideToggleImage(boolean toggleStatus) {
        if (toggleStatus) {
            NiftyImage img = nifty.getRenderEngine().createImage(nifty.getCurrentScreen(), "Interface/Rides/statuson.png", false);
            Element changedImg = nifty.getCurrentScreen().findElementByName("ridestatusbutton");
            changedImg.getRenderer(ImageRenderer.class).setImage(img);
        } else {
            NiftyImage img = nifty.getRenderEngine().createImage(nifty.getCurrentScreen(), "Interface/Rides/statusoff.png", false);
            Element changedImg = nifty.getCurrentScreen().findElementByName("ridestatusbutton");
            changedImg.getRenderer(ImageRenderer.class).setImage(img);
        }


    }

    public void updateInspectionWindow(boolean turnVisible,boolean reset){
        updateNifty();
        Object object=identifier.getObjectWithID(lastIDforRide);
        if(!(object instanceof BasicRide)){
            logger.log(Level.SEVERE, "ID corruption. ID {0} should return Ride",lastIDforRide);
            return;
        }

        BasicRide ride = (BasicRide)object;
        Element inspectionWindow = nifty.getCurrentScreen().findElementByName("inspectionWindow");
        if(reset){
            int default_amount = 200;
            amountCounter = default_amount;
            CheckBox payshadyCash = nifty.getCurrentScreen().findNiftyControl("inspectionpaycheckbox", CheckBox.class);
            payshadyCash.setChecked(false);
        }

        updateInspectionName(inspectionWindow, ride.getName());
        updateInspectionAmount(inspectionWindow,amountCounter);
        CheckBox payshadyCash = inspectionWindow.findNiftyControl("inspectionpaycheckbox", CheckBox.class);
        //Turn this here because if you do this in the end, it resets every visible attribute to its children.*
        if(turnVisible){
            inspectionWindow.setVisible(true);
        }
        //Set visibility to other elements based on if checkbox is checked or not. (Hides spare elements that are not needed)
        Element element = inspectionWindow.findElementByName("inspectionpaytext");
        element.setVisible(payshadyCash.isChecked());
        element = inspectionWindow.findElementByName("inspectionpay");
        element.setVisible(payshadyCash.isChecked());
        element = inspectionWindow.findElementByName("inspectionaddvalue");
        element.setVisible(payshadyCash.isChecked());
        element = inspectionWindow.findElementByName("inspectiontakevalue");
        element.setVisible(payshadyCash.isChecked());

    }
    private void updateInspectionName(Element inspectionWindow,String name){
        Element updatedText = inspectionWindow.findElementByName("inspectionto");
        updateText(updatedText,name);
    }
    private void updateInspectionAmount(Element inspectionWindow,int amount){
        Element updatedText = inspectionWindow.findElementByName("inspectionpay");
        updateText(updatedText,Integer.toString(amount));
    }
    public void inspectionAddValue(){
        amountCounter +=50;
        updateInspectionWindow(false, false);
    }
    public void inspectionTakeValue(){
        if(amountCounter > 0){
            amountCounter -=50;
        }
        updateInspectionWindow(false, false);
    }
    /* SHOP WINDOW CREATION AND UPDATE METHODS */
    /**
     * Create niftyGUI window element from shop. (Basically the shop window).
     *
     * @param shop
     */
    public void updateShopWindow(boolean toggleVisible) {
        updateNifty(); //Just to make sure nifty is not null
        Object object=identifier.getObjectWithID(lastIDforShop);
        if(!(object instanceof BasicShop)){
            logger.log(Level.SEVERE, "ID corruption. ID {0} should return Shop",lastIDforShop);
            return;
        }
        BasicShop shop = (BasicShop)object;
        Element shopwindow = nifty.getCurrentScreen().getLayerElements().get(2).findElementByName("shoptemplate");
        /**
         * TAB 1
         */

        /**
        updateShopNameText(shopwindow, shop.getShopName());
        updateShopPriceText(shopwindow, shop.getPrice());
        updateShopProdNameText(shopwindow, shop.getProductname());
        updateShopLocationText(shopwindow, shop.getPosition().getVector());
        updateShopReputationText(shopwindow, shop.getUpgrades().getReputation());
        **/

        /**
         * TAB 2
         */
        /**
         * TAB 3
         */
        if (toggleVisible) {
            shopwindow.setVisible(true);
        }

    }

    private void updateShopNameText(Element shopWindow, String name) {
        Element updatedText = shopWindow.findElementByName("shopname");
        updateText(updatedText, name);
    }

    private void updateShopPriceText(Element shopWindow, float price) {
        Element updatedText = shopWindow.findElementByName("shopprice");
        updateText(updatedText, Float.toString(price));
    }

    private void updateShopProdNameText(Element shopWindow, String prodName) {
        Element updatedText = shopWindow.findElementByName("shopproduct");
        updateText(updatedText, prodName);
    }

    private void updateShopLocationText(Element shopWindow, Vector3f location) {
        Element updatedText = shopWindow.findElementByName("shoplocation");
        String locationText = Float.toString(location.x) + " " + Float.toString(location.y) + " " + Float.toString(location.z);
        updateText(updatedText, locationText);
    }

    private void updateShopReputationText(Element shopWindow, ShopReputation rep) {
        Element updatedText = shopWindow.findElementByName("shopreputation");
        updateText(updatedText, rep.toString());
    }

    public void toggleUpgrade(int index) {
        Object object=identifier.getObjectWithID(lastIDforShop);
        if(!(object instanceof BasicShop)){
            logger.log(Level.SEVERE, "ID corruption. ID {0} should return Shop",lastIDforShop);
            return;
        }
        BasicShop currentshop =(BasicShop)object;
        ShopUpgradeContainer upgrades = currentshop.getUpgrades();
        switch (index) {
            case 1:
                upgrades.setTrendyUpgrade(!upgrades.isTrendyUpgrade());
                break;

            case 2:
                upgrades.setFriendlyStaffUpgrade(!upgrades.isFriendlyStaffUpgrade());
                break;

            case 3:
                upgrades.setCleaningUpgrade(!upgrades.isCleaningUpgrade());
                break;

            case 4:
                upgrades.setQualityUpgrade(!upgrades.isQualityUpgrade());
                break;
        }
        /* Update ui*/
        updateShopWindow(false);

    }

    /* UtilityMethods */
    public void updateText(Element niftyElement, String string) {
        niftyElement.getRenderer(TextRenderer.class).setText(string);
        niftyElement.getRenderer(TextRenderer.class).setTextHAlign(HorizontalAlign.left);
        niftyElement.getRenderer(TextRenderer.class).setTextVAlign(VerticalAlign.top);
    }

    /* SETTERS AND GETTERS */
    public int getLastIDforGuest() {
        return lastIDforGuest;
    }

    public int getLastIDforRide() {
        return lastIDforRide;
    }

    public int getLastIDforShop() {
        return lastIDforShop;
    }
    public void setIDforGuest(int ID){
        this.lastIDforGuest=ID;
    }
    public void setIDforRide(int ID){
        this.lastIDforRide=ID;
    }
    public void setIDforShop(int ID){
        this.lastIDforShop=ID;
    }

    public int getAmountCounter() {
        return amountCounter;
    }
    public boolean isInspectionPayCheckboxChecked(){
        updateNifty();
        Element inspectionWindow = nifty.getCurrentScreen().findElementByName("inspectionWindow");
        CheckBox payshadyCash = inspectionWindow.findNiftyControl("inspectionpaycheckbox", CheckBox.class);
        return payshadyCash.isChecked();
    }

}
