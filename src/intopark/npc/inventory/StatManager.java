/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package intopark.npc.inventory;

import java.util.Random;
import java.util.logging.Logger;

/**
 *
 * @author arska
 */
public class StatManager {
    private  transient static final Logger logger = Logger.getLogger(StatManager.class.getName());
    private GuestEmotions currentEmote= GuestEmotions.NORMAL;
    private int hunger=0;
    private int thirst=0;
    private int happyness=0;
    private RideType preferredRide;
    private double size;
    private double walkingSpeed;
    private  transient Random r = new Random();

    public StatManager(){

    }
    public void update(){
        int emote=r.nextInt(2);
        //0hu1th2ha
        switch(emote){
            case 0:
                if(r.nextInt(90)==1){
                    if(hunger==100){
                        currentEmote= GuestEmotions.HUNGRY;
                        return;
                    }
                    hunger++;
                }
            case 1:
                if(r.nextInt(90)==1&&thirst!=100){
                    if(thirst==100){
                        currentEmote= GuestEmotions.THIRSTY;
                        return;
                    }
                    thirst++;



                }
            case 2:
                if(r.nextInt(90)==1&&happyness!=100){
                    if(happyness<25){
                        currentEmote= GuestEmotions.MAD;

                    }
                    if(happyness>=90){
                        currentEmote= GuestEmotions.HAPPY;

                    }
                    happyness--;
                    if(happyness<0){
                        happyness=0;
                    }
                }
        }
    }
    public void randomize(){
        hunger=r.nextInt(100);
        thirst=r.nextInt(100);
        happyness=r.nextInt(100);
        walkingSpeed = 0.5d + r.nextDouble() ;
        size = 0.75d + r.nextDouble()/2 ;
        int u=r.nextInt(2);
        switch(u){
            case 0:
                preferredRide= RideType.MEDIUM;
                break;

            case 1:
                if(r.nextInt(1)==1){
                    preferredRide= RideType.HIGH;
                }else{
                    preferredRide= RideType.LOW;
                }
                break;

            case 2:
                if(r.nextInt(1)==1){
                    preferredRide= RideType.CRAZY;
                }else{
                    preferredRide= RideType.NAUSEA;
                }
                break;
        }

    }

    public GuestEmotions getCurrentEmote(){
        return currentEmote;
    }

    public int getHappyness() {
        return happyness;
    }

    public int getHunger() {
        return hunger;
    }

    public RideType getPreferredRide() {
        return preferredRide;
    }

    public int getThirst() {
        return thirst;
    }

    public void setCurrentEmote(GuestEmotions currentEmote) {
        this.currentEmote = currentEmote;
    }

    public void setHappyness(int happyness) {
        this.happyness = happyness;
    }

    public void setHunger(int hunger) {
        this.hunger = hunger;
    }

    public void setPreferredRide(RideType preferredRide) {
        this.preferredRide = preferredRide;
    }

    public void setThirst(int thirst) {
        this.thirst = thirst;
    }

    public double getSize() {
        return size;
    }

    public void setWalkingSpeed(double walkingSpeed) {
        this.walkingSpeed = walkingSpeed;
    }

    public void setSize(double size) {
        this.size = size;
    }

    public double getWalkingSpeed() {
        return walkingSpeed;
    }


}
