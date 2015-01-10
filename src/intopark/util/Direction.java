/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package intopark.util;

import com.google.common.collect.ImmutableMap;
import java.util.Map;

/**
 *
 * @author arska
 */
public enum Direction {
    /**
     * Direction represents the point of the compass in our world.
     * Directions are used when we rotate things.
     * World coordinates are (X,Y,Z)
     * North is X+
     * South is X-
     * East is Z+
     * West is Z-
     * This enum also contains many methods to compare and change the direction.
     * Direction.ANY value is used when making roads, it should be used carefully
     * because it will throw IllegalArgumentExeptions if used wrongly.
     */

    NORTH,SOUTH,EAST,WEST,ANY;



    /**
     * Returns true if this is same/opposite of dir2.
     * @param dir2 another direction to compare
     * @return boolean
     */
    public boolean isAligned(Direction dir2){
        if(dir2==null){
            throw new IllegalArgumentException();
        }
        if(dir2==this||dir2==this.getOpposite()){
            return true;
        }
        return false;
    }
    /**
     * Returns true if dir1 is the opposite of this.
     * Throws an IllegalArgumentExeption for Direction.ANY value.
     * @param dir1 another direction to compare
     * @return boolean
     */
    public boolean isOpposite(Direction dir1){
        if(dir1==null||dir1==Direction.ANY){
            throw new IllegalArgumentException();
        }
        if(dir1==this.getOpposite()){
            return true;
        }
        return false;
    }
    /**
     * Returns the opposite Direction of this.
     * Returns ANY for ANY.
     * @return Direction
     */
    public Direction getOpposite(){
        switch (this) {
            case NORTH:

                return SOUTH;
            case SOUTH:

                return NORTH;

            case EAST:

                return WEST;

            case WEST:

                return EAST;
            default:

                return ANY;
        }
    }
    /**
     * Returns the Direction for given int code
     * 0=north
     * 1=south
     * 2=east
     * 3=west
     * 4=any
     * ANY OTHER WILL THROW AN ILLEGALARGUMENTEXEPTION
     * .
     * @param dir int code to change to Direction
     * @return Direction
     */
    public static Direction intToDirection(int dir) {
        switch (dir) {
            case 0:
                return NORTH;
            case 1:
                return SOUTH;
            case 2:
                return EAST;
            case 3:
                return WEST;
            case 4:
                return ANY;
            default:
                throw new IllegalArgumentException();
        }
    }
    /**
     * Returns the angle of this for rotation modifications.
     * NORTH = 180 degrees.
     * Throws an IllegalArgumentExeption for Direction.ANY value.
     * @return int value of rotation in degrees.
     */
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
        throw new IllegalArgumentException();
    }
    /**
     * Returns the int code for this Direction
     * 0=north
     * 1=south
     * 2=east
     * 3=west
     * 4=any
     * .
     * @return int
     */
    public int directiontoInt(){
        switch (this) {
            case NORTH:
                return 0;
            case SOUTH:
                return 1;
            case EAST:
                return 2;
            case WEST:
                return 3;
            case ANY:
                return 4;
            default:
                throw new IllegalArgumentException();

        }
    }
    /**
     * Returns the offset for position if you were to go to the Direction of this.
     * Throws an IllegalArgumentExeption for Direction.ANY value.
     * @return MapPosition
     */
    public MapPosition directiontoPosition(){
        switch (this) {
            case NORTH:
                return new MapPosition(1, 0, 0);
            case SOUTH:
                return new MapPosition(-1, 0, 0);
            case EAST:
                return new MapPosition(0, 0, 1);
            case WEST:
                return new MapPosition(0, 0, -1);
            default:
                throw new IllegalArgumentException();

        }
    }

    public Direction turnRight(){
        ImmutableMap<Object,Object> turnRightMap = ImmutableMap.builder().put(EAST,SOUTH).put(SOUTH,WEST).put(WEST,NORTH).put(NORTH,EAST).build();
        Direction newDirection =(Direction)turnRightMap.get(this);
        return newDirection;
    }
    public Direction turnLeft(){
        ImmutableMap<Object,Object> turnLeftMap = ImmutableMap.builder().put(EAST,NORTH).put(NORTH,WEST).put(WEST,SOUTH).put(SOUTH,EAST).build();
        Direction newDirection =(Direction)turnLeftMap.get(this);
        return newDirection;
    }
}
