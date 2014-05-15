/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package intopark.inputhandler;

import com.jme3.collision.CollisionResults;

/**
 *
 * @author arska
 */
public class MouseContainer {
    
    private boolean leftClick;
    private float currentX=0;
    private float currentY=0;
    private CollisionResults results;
    private long lastDragged;
    
    private float startX=-1f;
    private float startY=-1f;
    private float lastRX =-1f;
    private float lastRY =-1f;
    
    public MouseContainer(float currentX,float currentY, CollisionResults results) {
        this.currentX=currentX;
        this.currentY=currentY;
        this.results = results;
    }
    public void setStart(float startX,float startY){
        this.startX=startX;
        this.startY=startY;
    }
    public void setRecent(float recentX,float recentY){
        this.lastRX=recentX;
        this.lastRY=recentY;
    }
    public void setLeftClick(boolean leftClick) {
        this.leftClick = leftClick;
    }

    public void setLastDragged(long lastDragged) {
        this.lastDragged = lastDragged;
    }

    public long getLastDragged() {
        return lastDragged;
    }

    public float getCurrentX() {
        return currentX;
    }

    public float getCurrentY() {
        return currentY;
    }

    public float getLastRX() {
        return lastRX;
    }

    public float getLastRY() {
        return lastRY;
    }

    public float getStartX() {
        return startX;
    }

    public float getStartY() {
        return startY;
    }
    
    public CollisionResults getResults() {
        return results;
    }

    public boolean isLeftClick() {
        return leftClick;
    }
    
    
    
}
