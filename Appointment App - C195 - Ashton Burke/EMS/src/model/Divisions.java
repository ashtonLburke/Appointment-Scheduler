package model;


/**
 *
 * Model class constructor for divisions/states used throughout the program
 *
 */
    public class Divisions {

        private int divisionID;
        private String divisionName;
        public int countryID;

        public Divisions(int divisionID, String divisionName, int countryID){
            this.divisionID = divisionID;
            this.divisionName = divisionName;
            this.countryID = countryID;
        }
    /**
     *
     * Holds getters and setters
     *
     */
        public int getDivisionID(){
            return divisionID;
        }
        public String getDivisionName(){
            return divisionName;
        }

        public int getCountryID(){
            return countryID;
        }


    }





