/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package intopark.GUI;

import com.google.inject.Inject;
import com.google.inject.Singleton;
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
import intopark.terrain.ParkHandler;

/**
 *
 * @author arska
 */
@Singleton
public class WindowMaker {
    //LOGGER
    private static final Logger logger = Logger.getLogger(IngameHUD.class.getName());
    //DEPENDENCIES
    Nifty nifty;
    @Inject ParkHandler parkHandler;
    @Inject NPCManager npcManager;
    //VARIABLES
    private int guestnumber;
    private int rideID;
    private int shopID;
    
    public WindowMaker() {
        nifty = Main.nifty;
    }
    /**
     * Returns guest with *
     * @return latest guest that was opened by this class
     */
    public Guest getCurrentGuestWindowGuest() {
        Guest guest = null;
        for (Guest g : npcManager.getGuests()) {
            if (g.getGuestNum() == guestnumber) {
                guest = g;
                break;
            }
        }
        return guest;
    }

    private void updateNifty() {
        nifty = Main.nifty;
    }

    public void createGuestWindow(Guest guest, boolean updateTextField) {
        updateNifty();
        if (guest == null) {
            logger.log(Level.WARNING,"Trying to display window to null guest");
            return;
        }
        Element guestwindow = nifty.getCurrentScreen().getLayerElements().get(2).findElementByName("guesttemplate");


        Element temppanel = guestwindow.findElementByName("g_rootpanel").findElementByName("g_tabspanel");
                
        Element niftyElement = temppanel.findElementByName("guestname");
        updateText(niftyElement, guest.getName());
        niftyElement = temppanel.findElementByName("guestwallet");
        updateText(niftyElement, guest.wallet.toString());
        niftyElement = temppanel.findElementByName("guestnumber");
        updateText(niftyElement, Integer.toString(guest.getGuestNum()));
        guestnumber = guest.getGuestNum();
        niftyElement = temppanel.findElementByName("gueststatus");
        updateText(niftyElement, guest.getWalkingState().toString());
        niftyElement = temppanel.findElementByName("guestreputation");
        updateText(niftyElement, "good");

        //panel 2
        niftyElement = temppanel.findElementByName("guesthunger");
        updateText(niftyElement, Integer.toString(guest.stats.hunger));
        niftyElement = temppanel.findElementByName("guestthirst");
        updateText(niftyElement, Integer.toString(guest.stats.thirst));
        niftyElement = temppanel.findElementByName("guesthappyness");
        updateText(niftyElement, Integer.toString(guest.stats.happyness));

        //panel3 
        if (updateTextField == true) {
            TextField textfield = nifty.getCurrentScreen().findNiftyControl("guestnametextfield", TextField.class);
            textfield.setText(guest.getName());
        }


        //panel 4
        int counter = 1;
        niftyElement = temppanel.findElementByName("guestinventory1");
        updateText(niftyElement, "");
        niftyElement = temppanel.findElementByName("guestinventory2");
        updateText(niftyElement, "");
        niftyElement = temppanel.findElementByName("guestinventory3");
        updateText(niftyElement, "");
        niftyElement = temppanel.findElementByName("guestinventory4");
        updateText(niftyElement, "");
        niftyElement = temppanel.findElementByName("guestinventory5");
        updateText(niftyElement, "");
        niftyElement = temppanel.findElementByName("guestinventory6");
        updateText(niftyElement, "");
        niftyElement = temppanel.findElementByName("guestinventory7");
        updateText(niftyElement, "");
        niftyElement = temppanel.findElementByName("guestinventory8");
        updateText(niftyElement, "");
        niftyElement = temppanel.findElementByName("guestinventory9");
        updateText(niftyElement, "");


        for (Item item : guest.getInventory()) {
            String elementname = "guestinventory" + Integer.toString(counter);
            niftyElement = temppanel.findElementByName(elementname);
            updateText(niftyElement, item.toString());
            counter++;
            if (counter == 10) {
                break;
            }
        }


        guestwindow.setVisible(true);

        nifty.getCurrentScreen().getLayerElements().get(2).add(guestwindow);
    }

    public void updateText(Element niftyElement, String string) {
        niftyElement.getRenderer(TextRenderer.class).setText(string);
        niftyElement.getRenderer(TextRenderer.class).setTextHAlign(HorizontalAlign.left);
        niftyElement.getRenderer(TextRenderer.class).setTextVAlign(VerticalAlign.top);
    }
    /**
     * Create niftyGUI window element from shop. (Basically the shop window).
     * @param shop 
     */
    public void createShopWindow(BasicShop shop) {
        updateNifty(); //Just to make sure nifty is not null
        if (shop == null) {
            logger.log(Level.WARNING,"Trying to display window to null shop");
            return;
        }
        shopID = shop.getShopID();//Store shopID for later use
        Element shopwindow = nifty.getCurrentScreen().getLayerElements().get(2).findElementByName("shoptemplate");
        Element temppanel = shopwindow.findElementByName("s_rootpanel").findElementByName("s_tabspanel");
        /* Search the name element and populate it */
        Element niftyElement = temppanel.findElementByName("shopname");
        updateText(niftyElement, shop.getShopName());
        /* .. price element .. */
        niftyElement = temppanel.findElementByName("shopprice");
        updateText(niftyElement, Float.toString(shop.getPrice()));
        /* .. productname element .. */
        niftyElement = temppanel.findElementByName("shopproduct");
        updateText(niftyElement, shop.getProductname());
        /* .. location element .. */
        niftyElement = temppanel.findElementByName("shoplocation");
        String location = Float.toString(shop.getPosition().getVector().x) + " " + Float.toString(shop.getPosition().getVector().y) + " " + Float.toString(shop.getPosition().getVector().z);
        updateText(niftyElement, location);
        /* .. reputation element .. */
        niftyElement = temppanel.findElementByName("shopreputation");
        updateText(niftyElement, shop.getReputation().toString());
        /* .. employees element TODO: .. */
        niftyElement = temppanel.findElementByName("shopemployees");
        String employees = "No employees";
        updateText(niftyElement, employees);
        /* Show the window */
        shopwindow.setVisible(true);
        nifty.getCurrentScreen().getLayerElements().get(2).add(shopwindow);
        
    }

    public void updateGuestWindow(Guest guest) {
        if(guest==null){
            return;
        }
        updateNifty();
        if (guest.getGuestNum() == guestnumber && nifty.getCurrentScreen().getLayerElements().get(2).findElementByName("guesttemplate").isVisible()) {
            createGuestWindow(guest, false);
        }
    }

    public BasicRide getCurrentRide() {
        BasicRide u = null;
        for (BasicRide o :parkHandler.getRides()) {
            if (o.getRideID() == rideID) {
                u = o;
                break;
            }
        }
        return u;
    }

    public void CreateRideWindow(BasicRide ride) {
        updateNifty();
        if (ride == null) {
            return;
        }
        Element rideWindow = nifty.getCurrentScreen().findElementByName("ridetemplate");
        rideID = ride.getRideID();
        /**
         * tab 1
         */
        updateRideNameTextfield(ride.getName());
        updateRidePriceTextTab1(rideWindow, ride.getPrice(), true);
        updateRideStatusText(rideWindow, ride.getStatus());
        updateRideToggleImage(ride.getStatus());
        /**
         * tab 2
         */
        updateRidePriceText(rideWindow, ride.getPrice());
        updateRideNameText(rideWindow, ride.getName());
        updateRideTypeText(rideWindow, ride.getRide());
        updateRideExitementText(rideWindow, ride.getExitement());
        updateRideNauseaText(rideWindow, ride.getNausea());
        updateRideStatusText(rideWindow, ride.getStatus());
        updateRideBrokenText(rideWindow, ride.getBroken());
        /**
         * tab 3
         */
        updateRideCustomersText(rideWindow, ride.customers());
        updateRideCustomersLifeText(rideWindow, ride.getCustomersTotal());
        updateRideCustomersHourText(rideWindow, ride.getGuestRateHour());
        updateRideMoneyGainedText(rideWindow, ride.getMoneyGainedTotal());
        updateRideMoneyHourText(rideWindow, ride.getMoneyRateHour());
        updateRideCostHourText(rideWindow, ride.getRepairCost());


        //laita näkyviin
        rideWindow.setVisible(true);

    }

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

    public void updateRideWindow(boolean updateNameTextField) {
        updateNifty();
        BasicRide ride = getCurrentRide();
        Element rideWindow = nifty.getCurrentScreen().findElementByName("ridetemplate");
        /**
         * tab 1
         */
        if (updateNameTextField) {
            updateRideNameTextfield(ride.getName());
        }
        updateRidePriceTextTab1(rideWindow, ride.getPrice(), false);
        updateRideStatusText(rideWindow, ride.getStatus());
        /**
         * tab 2
         */
        updateRidePriceText(rideWindow, ride.getPrice());
        updateRideNameText(rideWindow, ride.getName());
        updateRideTypeText(rideWindow, ride.getRide());
        updateRideExitementText(rideWindow, ride.getExitement());
        updateRideNauseaText(rideWindow, ride.getNausea());
        updateRideStatusText(rideWindow, ride.getStatus());
        updateRideBrokenText(rideWindow, ride.getBroken());
        /**
         * tab 3
         */
        updateRideCustomersText(rideWindow, ride.customers());
        updateRideCustomersLifeText(rideWindow, ride.getCustomersTotal());
        updateRideCustomersHourText(rideWindow, ride.getGuestRateHour());
        updateRideMoneyGainedText(rideWindow, ride.getMoneyGainedTotal());
        updateRideMoneyHourText(rideWindow, ride.getMoneyRateHour());
        updateRideCostHourText(rideWindow, ride.getRepairCost());

    }

    public void handleRideStatusToggle() {

        updateRideToggleImage(getCurrentRide().toggleStatus());
        updateRideWindow(false);
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

    public int getRideID() {
        return rideID;
    }

    int getShopID() {
        return shopID;
    }

    
}
