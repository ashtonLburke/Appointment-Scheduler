package model;
/**
 *
 * Model class constructor for countries
 *
 */
public class Countries {


    private int countryID;
    private String countryName;

    public Countries(int countryID, String countryName){

        this.countryID = countryID;
        this.countryName = countryName;
    }

    /**
     *
     * Holds getters and setters
     *
     */
    public int getCountryID() {
        return countryID;
    }

    public String getCountryName() {
        return countryName;
    }
}
