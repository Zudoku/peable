/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.GUI;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.TextField;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.TextRenderer;
import de.lessvoid.nifty.layout.align.HorizontalAlign;
import de.lessvoid.nifty.layout.align.VerticalAlign;
import mygame.Main;
import mygame.npc.Guest;
import mygame.npc.inventory.Item;
import mygame.ride.BasicRide;
import mygame.shops.BasicShop;
import mygame.shops.Employee;

/**
 *
 * @author arska
 */
public class WindowMaker {
    Nifty nifty;
    private int guestnumber;
    public WindowMaker(Nifty nifty){
        this.nifty=nifty;
      
    }
    public Guest getCurrentGuestWindowGuest(){
        Guest guest =null;
        for(Guest g:Main.npcManager.guests){
            if(g.getGuestNum()==guestnumber){
                guest=g;
                break;        
            }
        }
        return guest;
    }
    public void createGuestWindow(Guest guest,boolean updateTextField){
        if(guest==null){
            System.out.println("Error Guest null!!!!");
            return;
        }
       Element guestwindow = nifty.getCurrentScreen().getLayerElements().get(2).findElementByName("guesttemplate");
       
     
       Element temppanel = guestwindow.findElementByName("rootpanel").findElementByName("tabspanel")
               .findElementByName("tabs").findElementByName("tab_1").findElementByName("tab_1_panel");
      Element niftyElement= temppanel.findElementByName("guestname");
      updateText(niftyElement, guest.getName());
      niftyElement= temppanel.findElementByName("guestwallet");
      updateText(niftyElement, guest.wallet.toString());
      niftyElement= temppanel.findElementByName("guestnumber");
      updateText(niftyElement, Integer.toString(guest.getGuestNum()));
      guestnumber=guest.getGuestNum();
      niftyElement= temppanel.findElementByName("gueststatus");
      updateText(niftyElement, guest.getWalkingState().toString());
      niftyElement= temppanel.findElementByName("guestreputation");
      updateText(niftyElement, "good");
      
      temppanel=temppanel.getParent().getParent().findElementByName("tab_2").findElementByName("tab_2_panel");
      niftyElement= temppanel.findElementByName("guesthunger");
      updateText(niftyElement, Integer.toString(guest.stats.hunger));
      niftyElement= temppanel.findElementByName("guestthirst");
      updateText(niftyElement, Integer.toString(guest.stats.thirst));
      niftyElement= temppanel.findElementByName("guesthappyness");
      updateText(niftyElement, Integer.toString(guest.stats.happyness));
      
      temppanel=temppanel.getParent().getParent().findElementByName("tab_3").findElementByName("tab_3_panel");
      if(updateTextField==true){
        TextField textfield=nifty.getCurrentScreen().findNiftyControl("guestnametextfield",TextField.class);
        textfield.setText(guest.getName());
      }
      
      
      temppanel=temppanel.getParent().getParent().findElementByName("tab_4").findElementByName("tab_4_panel");
      int counter=1;
      niftyElement= temppanel.findElementByName("guestinventory1");
      updateText(niftyElement,"");
      niftyElement= temppanel.findElementByName("guestinventory2");
      updateText(niftyElement,"");
      niftyElement= temppanel.findElementByName("guestinventory3");
      updateText(niftyElement,"");
      niftyElement= temppanel.findElementByName("guestinventory4");
      updateText(niftyElement,"");
      niftyElement= temppanel.findElementByName("guestinventory5");
      updateText(niftyElement,"");
      niftyElement= temppanel.findElementByName("guestinventory6");
      updateText(niftyElement,"");
      niftyElement= temppanel.findElementByName("guestinventory7");
      updateText(niftyElement,"");
      niftyElement= temppanel.findElementByName("guestinventory8");
      updateText(niftyElement,"");
      niftyElement= temppanel.findElementByName("guestinventory9");
      updateText(niftyElement,"");
      
      
      for(Item item:guest.inventory){
          String elementname="guestinventory"+Integer.toString(counter);
          niftyElement= temppanel.findElementByName(elementname);
          updateText(niftyElement,item.toString());
          counter++;
          if(counter==10){
              break;
          }
      }
      
      
      guestwindow.setVisible(true);
       
       nifty.getCurrentScreen().getLayerElements().get(2).add(guestwindow);
    }
    public void updateText(Element niftyElement,String string){
    niftyElement.getRenderer(TextRenderer.class).setText(string);
    niftyElement.getRenderer(TextRenderer.class).setTextHAlign(HorizontalAlign.left);
    niftyElement.getRenderer(TextRenderer.class).setTextVAlign(VerticalAlign.top);
    }
    public void createShopWindow(BasicShop shop){
         if(shop==null){
            System.out.println("Error shop null!!!!");
            return;
        }
       Element shopwindow = nifty.getCurrentScreen().getLayerElements().get(2).findElementByName("shoptemplate");
       
     
       Element temppanel = shopwindow.findElementByName("rootpanel").findElementByName("tabspanel")
               .findElementByName("tabs").findElementByName("tab_1").findElementByName("tab_1_panel");
      Element niftyElement= temppanel.findElementByName("shopname");
      updateText(niftyElement, shop.shopName);
      niftyElement= temppanel.findElementByName("shopprice");
      updateText(niftyElement, Float.toString(shop.price));
      niftyElement= temppanel.findElementByName("shopproduct");
      updateText(niftyElement,shop.productname );
      niftyElement= temppanel.findElementByName("shoplocation");
      String location=Float.toString(shop.position. x)+" "+Float.toString(shop.position.y)+" "+Float.toString(shop.position.z);
      updateText(niftyElement,location);
      niftyElement= temppanel.findElementByName("shopreputation");
      updateText(niftyElement, shop.reputation.toString());
      niftyElement= temppanel.findElementByName("shopemployees");
      String employees="No employees";
      for(Employee e:shop.employees){
          if(employees.equals("No employees")){
              employees="";
          }
          employees=employees+" "+e.toString();
      }
      
      updateText(niftyElement,employees);
      
      
      
      shopwindow.setVisible(true);
       
       nifty.getCurrentScreen().getLayerElements().get(2).add(shopwindow);
    }
    public void updateGuestWindow(Guest guest){
        if(guest.getGuestNum()==guestnumber&&nifty.getCurrentScreen().getLayerElements().get(2).findElementByName("guesttemplate").isVisible()){
            createGuestWindow(guest,false);
        }
    }
    public void CreateRideWindow(BasicRide ride){
       if(ride==null){
           return;
       }
       Element rideWindow=nifty.getCurrentScreen().findElementByName("ridetemplate");
       /**
        * tab 2
        */
       updateRidePriceText(rideWindow, ride.getPrice());
       updateRideNameText(rideWindow, ride.getName());
       updateRideTypeText(rideWindow,"Chess-lair");
       updateRideExitementText(rideWindow, ride.getExitement());
       updateRideNauseaText(rideWindow, ride.getNausea());
       updateRideStatusText(rideWindow,ride.getStatus());
       updateRideBrokenText(rideWindow, ride.getBroken());
       /**
        * tab 3
        */
       updateRideCustomersText(rideWindow, ride.customers());
       updateRideCustomersLifeText(rideWindow, ride.getCustomersTotal());
       updateRideCustomersHourText(rideWindow,1); //todo
       updateRideMoneyGainedText(rideWindow, ride.getMoneyGainedTotal());
       updateRideMoneyHourText(rideWindow, 1); //todo
       updateRideCostHourText(rideWindow, 1); //todo
       
       
       //laita näkyviin
       rideWindow.setVisible(true);
       
    }
    
    private void updateRidePriceText(Element rideWindow,float price){
        
        Element updatedText=rideWindow.findElementByName("rideprice");
        updateText(updatedText,Float.toString(price));
    }
    private void updateRideNameText(Element rideWindow,String name){
        Element updatedText=rideWindow.findElementByName("ridename");
        updateText(updatedText,name);
    }
    private void updateRideTypeText(Element rideWindow,String type){
        Element updatedText=rideWindow.findElementByName("ridetype");
        updateText(updatedText,type);
    }
    private void updateRideExitementText(Element rideWindow,float exitement){
        Element updatedText=rideWindow.findElementByName("rideexitement");
        updateText(updatedText,Float.toString(exitement));
    }
    private void updateRideNauseaText(Element rideWindow,float nausea){
        Element updatedText=rideWindow.findElementByName("ridenausea");
        updateText(updatedText,Float.toString(nausea));
    }
    private void updateRideStatusText(Element rideWindow,boolean status){
        Element updatedText=rideWindow.findElementByName("ridestatus");
        if(status==true){
            updateText(updatedText,"Open!");
        }
        else{
            updateText(updatedText,"Closed");
        }
        
    }
    private void updateRideBrokenText(Element rideWindow,float broken){
        Element updatedText=rideWindow.findElementByName("ridebroken");
        updateText(updatedText,Float.toString(broken));
    }
    
    
    private void updateRideCustomersText(Element rideWindow,float customers){
        Element updatedText=rideWindow.findElementByName("ridecustomers");
        updateText(updatedText,Float.toString(customers));
    }
    private void updateRideCustomersLifeText(Element rideWindow,float customers){
        Element updatedText=rideWindow.findElementByName("ridecustomerslife");
        updateText(updatedText,Float.toString(customers));
    }
    private void updateRideCustomersHourText(Element rideWindow,float customers){
        Element updatedText=rideWindow.findElementByName("ridecustomershour");
        updateText(updatedText,Float.toString(customers));
    }
    private void updateRideMoneyGainedText(Element rideWindow,float moneytotal){
        Element updatedText=rideWindow.findElementByName("ridemoneygained");
        updateText(updatedText,Float.toString(moneytotal));
    }
    private void updateRideMoneyHourText(Element rideWindow,float moneyhour){
        Element updatedText=rideWindow.findElementByName("ridemoneyhour");
        updateText(updatedText,Float.toString(moneyhour));
    }
    private void updateRideCostHourText(Element rideWindow,float moneyhour){
        Element updatedText=rideWindow.findElementByName("ridehourcost");
        updateText(updatedText,Float.toString(moneyhour));
    }
    
    
    
}
