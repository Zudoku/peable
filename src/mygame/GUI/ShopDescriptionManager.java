/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.GUI;

/**
 *
 * @author arska
 */
public class ShopDescriptionManager {
    public String shopName="Name:";
    public String shopDescription="Description: ";
    public String shopPrice="Price: ";
    public String bigpic="";
    public ShopDescriptionManager(){
        
    }
    public void setDescriptionMBall(){
        shopName="Name: Meatball Shop";
        shopDescription="Description: test description";
        shopPrice="Price: 300";
        
    }
    public void setDescriptionToilet(){
        shopName="Name: Toilet";
        shopDescription="Description: test description";
        shopPrice="Price: 300";
    }
    public void setDescriptionEnergy(){
        shopName="Name: EnergyDrink shop";
        shopDescription="Description: test description";
        shopPrice="Price: 300";
        
    }
    
}
