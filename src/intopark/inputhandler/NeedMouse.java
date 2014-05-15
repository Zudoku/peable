/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package intopark.inputhandler;

/**
 *
 * @author arska
 */
public interface NeedMouse {
    public void onClick(MouseContainer container);
    public void onDrag(MouseContainer container);
    public void onDragRelease(MouseContainer container);
}
