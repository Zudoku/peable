/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package intopark.terrain;

/**
 *
 * @author arska
 */
public enum Direction {
    UP,DOWN,RIGHT,LEFT;
    public boolean isAligned(Direction dir2){
        /* Check: if both directions are up or down OR if both directions are left or right */
        if(((this==DOWN||this==UP)&&(dir2==DOWN||dir2==UP))||((this==LEFT||this==RIGHT)&&(dir2==LEFT||dir2==RIGHT))){
            return true;
        }
        return false;
    }
    public boolean isOpposite(Direction dir1){
        if((this==DOWN&&dir1==UP)||(this==UP&&dir1==DOWN)){
            return true;
        }
        if((this==LEFT&&dir1==RIGHT)||(this==RIGHT&&dir1==LEFT)){
            return true;
        }
        return false;
    }
}
