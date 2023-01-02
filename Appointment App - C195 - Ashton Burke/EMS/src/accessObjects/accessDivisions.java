package accessObjects;

import database.DatabaseConnect;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Divisions;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
/**
 *
 * DAO for accessing divisions data from database
 *
 */
public class accessDivisions extends Divisions {
    /**
     * super reference variable used to declare divisions access as a parent class object (no constructor)
     * @param divisionID
     * @param divisionName
     * @param countryID
     */
    public accessDivisions(int divisionID, String divisionName, int countryID){
        super(divisionID, divisionName, countryID);
    }

    /**
     * getter that retrieves divisions data from database and returns it to an observable list
     * @return
     * @throws SQLException
     */

    public static ObservableList<accessDivisions> getDivisions() throws SQLException {

        ObservableList<accessDivisions> divisionsList = FXCollections.observableArrayList();
        String sql = "SELECT * FROM first_level_divisions";
        PreparedStatement ps = DatabaseConnect.getConnection().prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            int divisionID = rs.getInt("Division_ID");
            String divisionName = rs.getString("Division");
            int countryID = rs.getInt("COUNTRY_ID");
            accessDivisions divisions = new accessDivisions(divisionID, divisionName, countryID);
            divisionsList.add(divisions);

        }

        return divisionsList;
    }




}
