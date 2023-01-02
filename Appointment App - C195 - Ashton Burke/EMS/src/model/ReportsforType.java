package model;
/**
 *
 * Model class for reports that get listed by appointment type
 * holds their respective getters and setters
 */
public class ReportsforType {

    public String apptType;
    public int apptTotal;


    /**
     *
     *
     *
     */
    public ReportsforType(String apptType, int apptTotal)
    {
        this.apptType = apptType;
        this.apptTotal = apptTotal;
    }

    public String getApptType() {
        return apptType;
    }

    public int getApptTotal() {
        return apptTotal;
    }
}
