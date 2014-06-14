/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package intopark.npc.inventory;

/**
 *
 * @author arska
 */
public enum GuestEmotions {
    MAD,HAPPY,HUNGRY,THIRSTY,NORMAL,TIRED,SUPER;

    @Override
    public String toString() {
        switch(this){
            case HAPPY:
                return "Happy";
            case HUNGRY:
                return "Hungry";
            case MAD:
                return "Angry";
            case NORMAL:
                return "Okay";
            case SUPER:
                return "Awesome!";
            case THIRSTY:
                return "thristy";
            case TIRED:
                return "Tired";
            default:
                return super.toString();
        }
    }
    
}
