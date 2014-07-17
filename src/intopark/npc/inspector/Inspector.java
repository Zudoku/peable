/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package intopark.npc.inspector;

import com.jme3.scene.Spatial;
import intopark.npc.BasicNPC;
import intopark.ride.BasicRide;
import intopark.util.MapPosition;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;

/**
 *
 * @author arska
 */
public class Inspector extends BasicNPC{

    private BasicRide targetRide;
    private float paidAmount;
    private boolean inspected;
    public Inspector(String name, Spatial object,int ID,MapPosition position,BasicRide targetRide,float paidAmount) {
        super(name, object,ID,position);
        this.targetRide = targetRide;
        this.paidAmount = paidAmount;
        super.getGeometry().setLocalTranslation(position.getVector());
        object.setUserData("type","inspector");
        object.setUserData("ID", ID);
    }
    public void inspect(){
        targetRide.setLastInspection(getInspect());
        inspected=true;
        logger.log(Level.FINE, "Inspector {0} inspected {1} . ",new Object[]{this.toString(),targetRide.toString()});

    }
    private void reportCorruption(){
        logger.log(Level.FINE, "Inspector {0} reported your park because of corruption.",this.toString());
    }
    private Inspection getInspect(){
        //Figure out if inspection is passed.
        boolean inspectionPassed = (paidAmount > 1)?wasIPaidEnough():true;
        //Paid or not
        boolean paid;
        if(paidAmount > 1){
            paid = true;
        }else{
            paid= false;
        }
        boolean boost = inspectionPassed && paid; //If we are corrupted and give better inspection results
        if(inspectionPassed && paid){
            Random random = new Random();
            if(random.nextInt(4)<=3){
                logger.log(Level.FINE, "Inspector {0} did not appreciate your money. Your park will be reported.");
                reportCorruption();
            }else{
                logger.log(Level.FINE,"Inspector {0} did not appreciate your money. He will let this slide this time but he/she will take your money you gave him/her. It won't boost the inspection ratings.");
            }
        }
        //Working conditions
        InspectionComment workingConditions = rateWorkingConditions(targetRide.getGuestRateHour(),boost);
        //Working safety
        InspectionComment workingSafety = rateWorkingSafety(targetRide.getBroken(),boost);
        //Additional comments
        List<InspectionComment> additionalComments = generateAdditionalComments(boost);

        Inspection inspection = new Inspection(getName(),inspectionPassed,paid, paidAmount,workingConditions,workingSafety,additionalComments);
        return inspection;
    }
    private List<InspectionComment> generateAdditionalComments(boolean boost){
        List<InspectionComment> comments = new ArrayList<>();

        return comments;
    }
    private InspectionComment rateWorkingSafety(int broken,boolean boost){
        // 0-29
        if(broken < 30 || boost){
            return new InspectionComment("Workplace is safe.",1);
        }
        // 30-79
        if(broken < 80){
            return new InspectionComment("Workplace safety is questionable.",0);
        }
        // 80-100
        return new InspectionComment("Workplace is dangerous!",-1);
    }
    private InspectionComment rateWorkingConditions(double guestHourRate,boolean boost){
        // 0-99
        if(guestHourRate < 100 || boost){
            return new InspectionComment("Best possible working conditions.",1);
        }
        // 100-199
        if(guestHourRate < 200){
            return new InspectionComment("Questionable working conditions.",0);
        }
        // x > 199
        return new InspectionComment("Too much work for the staff.",-1);
    }
    private boolean wasIPaidEnough(){
        Random r = new Random();
        int targetValue=(r.nextInt(6)-3)*50+250;
        return paidAmount >=targetValue; //?true:false;
    }
    @Override
    public void update() {
        super.update();
    }

    public float getPaidAmount() {
        return paidAmount;
    }

    public BasicRide getTargetRide() {
        return targetRide;
    }

    public boolean isInspected() {
        return inspected;
    }



}
