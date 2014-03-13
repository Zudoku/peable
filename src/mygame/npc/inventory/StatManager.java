/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.npc.inventory;

import java.util.Random;
import java.util.logging.Logger;

/**
 *
 * @author arska
 */
public class StatManager {
    private  transient static final Logger logger = Logger.getLogger(StatManager.class.getName());
    private GuestEmotions currentEmote= GuestEmotions.NORMAL;
    public int hunger=0;
    public int thirst=0;
    public int happyness=0;
    public RideType preferredRide;
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
}
