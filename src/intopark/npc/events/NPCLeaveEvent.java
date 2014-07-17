/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package intopark.npc.events;

import intopark.npc.BasicNPC;

/**
 *
 * @author arska
 */
public class NPCLeaveEvent {
    private BasicNPC npcLeaving;

    public NPCLeaveEvent(BasicNPC npcLeaving) {
        this.npcLeaving = npcLeaving;
    }

    public BasicNPC getNpcLeaving() {
        return npcLeaving;
    }
    
}
