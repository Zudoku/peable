/*+
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package intopark.npc.inspector;

import java.util.List;

/**
 *
 * @author arska
 */
public class Inspection {
    private String inspector; // Inspectors name
    private boolean inspectionPassed;
    private boolean paid;
    private double paidAmount;
    private InspectionComment workingConditions;
    private InspectionComment workingSafety;
    private List<InspectionComment> additionalComments;
    private int leftImage;

    public Inspection(String inspector, boolean inspectionPassed, boolean paid, double paidAmount, InspectionComment workingConditions,
            InspectionComment workingSafety, List<InspectionComment> additionalComments) {
        this.inspector = inspector;
        this.inspectionPassed = inspectionPassed;
        this.paid = paid;
        this.paidAmount = paidAmount;
        this.workingConditions = workingConditions;
        this.workingSafety = workingSafety;
        this.additionalComments = additionalComments;
        calculateInspectionLeftImage();
    }
    private void calculateInspectionLeftImage(){
        int image = 0;
        if(inspectionPassed){
            image += 5;
        }else{
            image -= 5;
        }
        if(paid){
            image--;
        }
        image +=workingConditions.getStatus();
        image +=workingSafety.getStatus();
        for(InspectionComment comment:additionalComments){
            image += comment.getStatus();
        }
        this.leftImage  =image;
    }

    public int getLeftImage() {
        return leftImage;
    }

    public List<InspectionComment> getAdditionalComments() {
        return additionalComments;
    }

    public String getInspector() {
        return inspector;
    }

    public double getPaidAmount() {
        return paidAmount;
    }

    public InspectionComment getWorkingConditions() {
        return workingConditions;
    }

    public InspectionComment getWorkingSafety() {
        return workingSafety;
    }

    public boolean isInspectionPassed() {
        return inspectionPassed;
    }

    public boolean isPaid() {
        return paid;
    }
    

}