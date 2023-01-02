package model;
/**
 *
 * Model class constructor for the main reports page (reports by contact)
 *
 */
public class Reports {

        private int countryTotal;
        private String countryName;
        public String apptbyMonth;
        public int totalAppts;
    /**
     *
     *
     * Holds getters and setters
     */

        public Reports(String countryName, int countryCount) {
            this.countryTotal = countryTotal;
            this.countryName = countryName;

        }

        public String getCountryName() { return countryName; }


        public int getCountryTotal() { return countryTotal; }

    }
