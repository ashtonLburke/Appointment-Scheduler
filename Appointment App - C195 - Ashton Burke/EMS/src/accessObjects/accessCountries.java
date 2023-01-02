package accessObjects;

import database.DatabaseConnect;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Countries;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;



/**
 *
 * DAO for accessing country information from database
 *
 * super reference variable used to declare country access as a parent class object (no constructor)
 *
 */
    public class accessCountries extends Countries {

        public accessCountries (int countryID, String countryName) {
            super(countryID, countryName);
        }

    /**
     * getCountries method retrieves country information from database and returns it to an observable list
     * @return
     * @throws SQLException
     */
        public static ObservableList<Countries> getCountries() throws SQLException {

            ObservableList<Countries> countriesList = FXCollections.observableArrayList();
            String sql = "SELECT Country_ID, Country FROM countries";
            PreparedStatement ps = DatabaseConnect.getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int countryID = rs.getInt("Country_ID");
                String countryName = rs.getString("Country");
                accessCountries Countries = new accessCountries(countryID, countryName);
                countriesList.add(Countries);

            }

            return countriesList;
        }


    }
