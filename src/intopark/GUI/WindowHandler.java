/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package intopark.GUI;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.jme3.math.Vector3f;
import de.lessvoid.nifty.Nifty;
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
import intopark.npc.Guest;
import intopark.npc.NPCManager;
import intopark.npc.inventory.Item;
import intopark.ride.BasicRide;
import intopark.shops.BasicShop;
import intopark.shops.ShopReputation;
import intopark.shops.ShopUpgradeContainer;
import intopark.terrain.ParkHandler;
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
    @Inject
    ParkHandler parkHandler;
    @Inject
    NPCManager npcManager;
    //VARIABLES
    private int guestID;
    private int rideID;
    private int shopID;

    public WindowHandler() {
        nifty = Main.nifty;
    }
    private void updateNifty() {
        nifty = Main.nifty;
    }
    /* GUEST WINDOW CREATION AND UPDATE METHODS */

    public void updateGuestWindow(boolean toggleVisible, boolean updateTextField) {
        updateNifty();
        Guest guest = parkHandler.getGuestWithID(guestID);
        if (guest == null) {
            logger.log(Level.WARNING, "Trying to display window to null guest");
            return;
        }
        Element guestwindow = nifty.getCurrentScreen().getLayerElements().get(2).findElementByName("guesttemplate");
        /**
         * TAB 1
         */
        updateGuestNameText(guestwindow, guest.getName());
        updateGuestWalletText(guestwindow, guest.getWallet().toString());
        updateGuestIDText(guestwindow, guest.getGuestNum());
        updateGuestStatusText(guestwindow, guest.getWalkState().toString());
        updateGuestReputationText(guestwindow, "Good"); //TODO:
        /**
         * TAB 2
         */
        updateGuestHungerText(guestwindow, guest.getStats().hunger);
        updateGuestThirstyText(guestwindow, guest.getStats().thirst);
        updateGuestHappynessText(guestwindow, guest.getStats().happyness);
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

        //nifty.getCurrentScreen().getLayerElements().get(2).add(guestwindow);
    }
    private void updateGuestInventoryTexts(Element guestWindow,List<Item> inv){
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

    private void updateGuestWalletText(Element guestWindow, String money) {
        Element updatedText = guestWindow.findElementByName("guestwallet");
        updateText(updatedText, money);
    }

    private void updateGuestIDText(Element guestWindow, int ID) {
        Element updatedText = guestWindow.findElementByName("guestnumber");
        updateText(updatedText, Integer.toString(ID));
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

    public void updateRideWindow(boolean updateNameTextField, boolean toggleVisible) {
        updateNifty();
        BasicRide ride = parkHandler.getRideWithID(rideID);
        if (ride == null) {
            logger.log(Level.WARNING, "Could not update ui for ride {0} >ID> {1}", new Object[]{ride, rideID});
            return;
        }
        Element rideWindow = nifty.getCurrentScreen().findElementByName("ridetemplate");
        /**
         * TAB 1
         */
        if (updateNameTextField) {
            updateRideNameTextfield(ride.getName());
        }
        updateRidePriceTextTab1(rideWindow, ride.getPrice(), false);
        updateRideStatusText(rideWindow, ride.getStatus());
        /**
         * TAB 2
         */
        updateRidePriceText(rideWindow, ride.getPrice());
        updateRideNameText(rideWindow, ride.getName());
        updateRideTypeText(rideWindow, ride.getRide());
        updateRideExitementText(rideWindow, ride.getExitement());
        updateRideNauseaText(rideWindow, ride.getNausea());
        updateRideStatusText(rideWindow, ride.getStatus());
        updateRideBrokenText(rideWindow, ride.getBroken());
        /**
         * TAB 3
         */
        updateRideCustomersText(rideWindow, ride.customers());
        updateRideCustomersLifeText(rideWindow, ride.getCustomersTotal());
        updateRideCustomersHourText(rideWindow, ride.getGuestRateHour());
        updateRideMoneyGainedText(rideWindow, ride.getMoneyGainedTotal());
        updateRideMoneyHourText(rideWindow, ride.getMoneyRateHour());
        updateRideCostHourText(rideWindow, ride.getRepairCost());

        if (toggleVisible) {
            rideWindow.setVisible(true);
        }
    }

    public void handleRideStatusToggle() {
        updateRideToggleImage(parkHandler.getRideWithID(rideID).toggleStatus());
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

    /* SHOP WINDOW CREATION AND UPDATE METHODS */
    /**
     * Create niftyGUI window element from shop. (Basically the shop window).
     *
     * @param shop
     */
    public void updateShopWindow(boolean toggleVisible) {
        updateNifty(); //Just to make sure nifty is not null
        BasicShop shop = parkHandler.getShopWithID(shopID);
        if (shop == null) {
            logger.log(Level.WARNING, "Could not update ui for shop {0} >ID> {1}", new Object[]{shop, shopID});
            return;
        }
        Element shopwindow = nifty.getCurrentScreen().getLayerElements().get(2).findElementByName("shoptemplate");
        /**
         * TAB 1
         */
        updateShopNameText(shopwindow, shop.getShopName());
        updateShopPriceText(shopwindow, shop.getPrice());
        updateShopProdNameText(shopwindow, shop.getProductname());
        updateShopLocationText(shopwindow, shop.getPosition().getVector());
        updateShopReputationText(shopwindow, shop.getUpgrades().getReputation());
        /**
         * TAB 2
         */
        /**
         * TAB 3
         */
        if (toggleVisible) {
            shopwindow.setVisible(true);
        }

        //        nifty.getCurrentScreen().getLayerElements().get(2).add(shopwindow);

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
        BasicShop currentshop = parkHandler.getShopWithID(shopID);
        if (currentshop == null || currentshop.getUpgrades() == null) {
            throw new NullPointerException("BasicShop or BasicShop.getUpgrades()");
        }
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
    public int getRideID() {
        return rideID;
    }

    public int getShopID() {
        return shopID;
    }

    public int getGuestID() {
        return guestID;
    }

    public void setGuestID(int guestID) {
        this.guestID = guestID;
    }

    public void setRideID(int rideID) {
        this.rideID = rideID;
    }

    public void setShopID(int shopID) {
        this.shopID = shopID;
    }
}
