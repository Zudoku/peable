/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.GUI;

import com.google.inject.Singleton;
import mygame.shops.BasicBuildables;

/**
 *
 * @author arska
 */
@Singleton
public class ShopDescriptionManager {
    public String shopName="Name:";
    public String shopDescription="Description: ";
    public String shopPrice="Price: ";
    public String bigpic="Interface/Shops/Icon.png";
    public ShopDescriptionManager(){
        
    }
    private void setDescriptionMBall(){
        shopName="Name: Meatball Shop";
        shopDescription="Description: Lovely meatballs! Yum!";
        shopPrice="Price: 300";
        bigpic="Interface/Shops/Icon.png";
    }
    private void setDescriptionToilet(){
        shopName="Name: Toilet";
        shopDescription="Description: Old toilet ";
        shopPrice="Price: 300";
        bigpic="Interface/Shops/Icon.png";
    }
    private void setDescriptionEnergy(){
        shopName="Name: EnergyDrink shop";
        shopDescription="Description: adasjdh";
        shopPrice="Price: 300";
        bigpic="Interface/Shops/Icon.png";
        
    }
    private void setDescriptionChess(){
        shopName="Name: ChessCenter";
        shopDescription="Description: Building to play Chess. Smart!";
        shopPrice="Price: 500";
        bigpic="Interface/Shops/Icon.png";
        
    }

    private void setDescriptionArchery() {
        shopName="Name: Archery Range";
        shopDescription="Description: Test your archery skills";
        shopPrice="Price: 500";
        bigpic="Interface/Shops/Icon.png";
    }

    private void setDescriptionBlender() {
        shopName="Name: Blender";
        shopDescription="Description: Get yourself Blended ! Barf";
        shopPrice="Price: 500";
        bigpic="Interface/Shops/Icon.png";
    }

    private void setDescriptionHHouse() {
        shopName="Name: HauntedHouse";
        shopDescription="Description: This place is haunted oh no";
        shopPrice="Price: 500";
        bigpic="Interface/Shops/Icon.png";
    }

    private void setDescriptionPirateShip() {
        shopName="Name: Pirate Ship";
        shopDescription="Description: Prepare to be boarded!";
        shopPrice="Price: 500";
        bigpic="Interface/Shops/Icon.png";
    }

    private void setDescriptionRotor() {
        shopName="Name: Rotor";
        shopDescription="Description: I have no idea";
        shopPrice="Price: 500";
        bigpic="Interface/Shops/Icon.png";
    }
    private void setDescriptionSpinW() {
        shopName="Name: Spin Wheel";
        shopDescription="Description: Spins faster than ever!";
        shopPrice="Price: 500";
        bigpic="Interface/Shops/Icon.png";
    }
    public void setDescription(BasicBuildables buildable) {
        switch(buildable){
            case ARCHERYRANGE:
                setDescriptionArchery();
                break;
                
            case BLENDER:
                setDescriptionBlender();
                break;
                
            case CHESSCENTER:
                setDescriptionChess();
                break;
                
            case ENERGY:
                setDescriptionEnergy();
                break;
                
            case HAUNTEDHOUSE:
                setDescriptionHHouse();
                break;
                
            case MBALL:
                setDescriptionMBall();
                break;
            case PIRATESHIP:
                setDescriptionPirateShip();
                break;
                
            case ROTOR:
                setDescriptionRotor();
                break;
                
            case SPINWHEEL:
                setDescriptionSpinW();
                break;
                
            case TOILET:
                setDescriptionToilet();
                break;
            case NULL:
        }
    }

    
    
}
