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
public class MouseController {
    //INTERNAL 
    private boolean locked=false;
    private boolean dragStarted=false;
    private NeedMouse callable;
    //EXTERNAL
    private float startX=-1f;
    private float startY=-1f;
    private float lastRX =-1f;
    private float lastRY =-1f;
    
    private MouseContainer clickContainer;
    private MouseContainer dragContainer;
    private MouseContainer dragReleaseContainer;
    public MouseController() {
        
    }
    public void handleClickingMechanic(boolean leftClick,float currentX,float currentY,CollisionResults results) {
        clickContainer=new MouseContainer(currentX, currentY, results);
        clickContainer.setLeftClick(leftClick);
        clickContainer.setRecent(lastRX, lastRY);
        clickContainer.setStart(startX, startY);
        callable.onClick(clickContainer);
        if (results.size()!=0) {
            if (!locked) {
                locked = true;
            }

        }
    }
    public void handleDragMechanic(boolean leftClick,float currentX,float currentY,CollisionResults results, long lastDragged) {
        if (locked) {
            if (startY < 0|| startX < 0) {
                startX = currentX;
                startY = currentY;
            } else if(!dragStarted){
                float deltaX=Math.abs(startX-currentX);
                float deltaY=Math.abs(startY-currentY);
                if(deltaX > 10||deltaY > 10){
                    dragStarted=true;
                }
            }
            else{
                lastRX=currentX;
                lastRY=currentY;
                dragContainer = new MouseContainer(currentX, currentY, results);
                dragContainer.setLeftClick(leftClick);
                dragContainer.setLastDragged(lastDragged);
                dragContainer.setRecent(lastRX, lastRY);
                dragContainer.setStart(startX, startY);
                callable.onDrag(dragContainer);
            }
        }
    }
    public void handleReleaseDragMechanic(float currentX,float currentY,CollisionResults results) {
        dragReleaseContainer = new MouseContainer(currentX, currentY, results);
        dragReleaseContainer.setRecent(lastRX, lastRY);
        dragReleaseContainer.setStart(startX, startY);
        callable.onDragRelease(dragReleaseContainer);
        reset();
    }
    public void reset() {
        locked = false;
        dragStarted = false;
        startX = -1;
        startY = -1;
        lastRX =-1;
        lastRY =-1;
        clickContainer=null;
        dragContainer=null;
        dragReleaseContainer=null;
    }

    public void setCallable(NeedMouse callable) {
        this.callable = callable;
    }

    public NeedMouse getCallable() {
        return callable;
    }
    
}