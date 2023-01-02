package model;
import java.time.LocalDateTime;

/**
 *
 * Model class constructor for all appointments
 *
 */
public class Appts {


    private int apptID;
    private String apptTitle;
    private String apptDescription;
    private String apptLocation;
    private String apptType;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;

    public int countryID;
    public int customerID;
    public int userID;
    public int contactID;


    public Appts(int apptID, String apptTitle, String apptDescription, String apptLocation,
                 String apptType, LocalDateTime startDateTime, LocalDateTime endDateTime, int customerID, int userID, int contactID){

        this.apptID = apptID;
        this.apptTitle = apptTitle;
        this.apptDescription = apptDescription;
        this.apptLocation = apptLocation;
        this.apptType = apptType;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;

        this.customerID = customerID;
        this.userID = userID;
        this.contactID = contactID;


    }





    /**
     *
     *
     * Holds getters and setters
     */
    public int getApptID() {
        return apptID;
    }

    public String getApptTitle() {
        return apptTitle;
    }

    public String getApptDescription() {
        return apptDescription;
    }

    public String getApptLocation() {
        return apptLocation;
    }

    public String getApptType() {
        return apptType;
    }

    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }

    public LocalDateTime getEndDateTime() {
        return endDateTime;
    }

    public int getCustomerID() {
        return customerID;
    }

    public int getUserID() {
        return userID;
    }

    public int getContactID() {
        return contactID;
    }
}
