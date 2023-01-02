package model;
/**
 *
 * Model class used by reports that listeds appointments by month
 *
 */
public class ReportsforMonth {

    public String apptMonth;
    public int apptTotal;


    /**
     *
     *
     * Holds getters and setters
     */
    public ReportsforMonth(String apptType, int apptTotal)
    {
        this.apptMonth = apptType;
        this.apptTotal = apptTotal;
    }

    public String getApptMonth() {
        return apptMonth;
    }

    public int getApptTotal() {
        return apptTotal;
    }
}
