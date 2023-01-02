package model;
/**
 *
 * Model class constructor for contacts
 *
 */
public class Contacts {

    public int contactID;
    private String contactName;
    private String contactEmail;

    public Contacts (int contactID, String contactName, String contactEmail){
        this.contactID = contactID;
        this.contactName = contactName;
        this.contactEmail = contactEmail;
    }
    /**
     * Holds getters and setters
     *
     *
     */
    public int getContactID() {
        return contactID;
    }


    public String getContactName() {
        return contactName;
    }


    public String getContactEmail() {
        return contactEmail;
    }


}
