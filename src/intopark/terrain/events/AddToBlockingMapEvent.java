/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package intopark.terrain.events;

/**
 *
 * @author arska
 */
public class AddToBlockingMapEvent {
    private int ID;
    private int[] coords;

    public AddToBlockingMapEvent(int ID, int[] coords) {
        if(ID>0){
            this.ID = ID;
        }else{
            throw new RuntimeException("BAD ID - Must be positive");
        }if(coords.length == 3){
            this.coords =coords;
        }else{
            throw new RuntimeException("BAD COORDS - Should be X,Y,Z lenght 3");
        }
    }

    public int[] getCoords() {
        return coords;
    }

    public int getID() {
        return ID;
    }


}
