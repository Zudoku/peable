/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package intopark.npc.inspector;

/**
 *
 * @author arska
 */
public class InspectionComment {
    private String comment;
    private int status; //  -1 | 0 | 1  Negative | Neutral |Positive

    public InspectionComment(String comment, int status) {
        this.comment = comment;
        this.status = status;
    }

    public String getComment() {
        return comment;
    }

    public int getStatus() {
        return status;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setStatus(int status) {
        this.status = status;
    }
    
}
