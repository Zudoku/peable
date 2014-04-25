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
    /* NORTH: X+    */
    NORTH,SOUTH,EAST,WEST;
    public boolean isAligned(Direction dir2){
        /* Check: if both directions are up or down OR if both directions are left or right */
        if(((this==SOUTH||this==NORTH)&&(dir2==SOUTH||dir2==NORTH))||((this==WEST||this==EAST)&&(dir2==WEST||dir2==EAST))){
            return true;
        }
        return false;
    }
    public boolean isOpposite(Direction dir1){
        if((this==SOUTH&&dir1==NORTH)||(this==NORTH&&dir1==SOUTH)){
            return true;
        }
        if((this==WEST&&dir1==EAST)||(this==EAST&&dir1==WEST)){
            return true;
        }
        return false;
    }
    public Direction intToDirection(int dir) {
        switch (dir) {
            default:
                //0
                return NORTH;
            case 1:
                return SOUTH;
            case 2:
                return EAST;
            case 3:
                return WEST;
        }
    }
    public int getAngle() {
        /* NORTH = 180 */
        switch (this) {
            case NORTH:
                return 180;

            case SOUTH:
                return 0;

            case EAST:
                return 90;

            case WEST:
                return -90;
        }
        return 0;
    }
}
