/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.GUI;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.TextRenderer;
import de.lessvoid.nifty.layout.align.HorizontalAlign;
import de.lessvoid.nifty.layout.align.VerticalAlign;
import mygame.npc.Guest;
import mygame.npc.inventory.Item;
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
    public void createGuestWindow(Guest guest){
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
      
      temppanel=temppanel.getParent().getParent().findElementByName("tab_4").findElementByName("tab_4_panel");
      int counter=1;
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
            createGuestWindow(guest);
        }
    }
    
}
