/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package intopark.npc.inspector;

/**
 *
 * @author arska
 */
public class CreateInspectorEvent {
    private Inspector inspector;

    public CreateInspectorEvent(Inspector inspector) {
        this.inspector = inspector;
    }

    public Inspector getInspector() {
        return inspector;
    }

}
