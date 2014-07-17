/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package intopark.npc.inspector;

import intopark.ride.BasicRide;

/**
 *
 * @author arska
 */
public class InviteInspectorEvent {
    private BasicRide rideToInspect;
    private boolean paid;
    private int paidAmount;

    public InviteInspectorEvent(BasicRide rideToInspect, int paidAmount) {
        this.rideToInspect = rideToInspect;
        this.paid = true;
        this.paidAmount = paidAmount;
    }

    public InviteInspectorEvent(BasicRide rideToInspect) {
        this.rideToInspect = rideToInspect;
        this.paid = false;
    }

    public BasicRide getRideToInspect() {
        return rideToInspect;
    }

    public boolean isPaid() {
        return paid;
    }

    public int getPaidAmount() {
        return paidAmount;
    }


}
