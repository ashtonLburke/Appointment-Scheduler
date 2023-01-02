package accessObjects;

import database.DatabaseConnect;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Appts;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
/**
 *
 * DAO for appointments
 *
 */
public class accessAppts {

    /**
     *
     * getter that retrieves all appointment information from the database and returns it to an observable list
     *
     */


    public static ObservableList<Appts> getAllAppts() throws SQLException
    {

        ObservableList<Appts> apptsObservableList = FXCollections.observableArrayList();
        String sql = "SELECT * from appointments";
        PreparedStatement ps = DatabaseConnect.getConnection().prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {

            int apptID = rs.getInt("Appointment_ID");
            String apptTitle = rs.getString("Title");
            String apptDescription = rs.getString("Description");
            String apptLocation = rs.getString("Location");
            String apptType = rs.getString("Type");
           LocalDateTime startDateTime = rs.getTimestamp("Start").toLocalDateTime();
           LocalDateTime endDateTime = rs.getTimestamp("End").toLocalDateTime();

            int customerID = rs.getInt("Customer_ID");
            int userID = rs.getInt("User_ID");
            int contactID = rs.getInt("Contact_ID");

            Appts appts = new Appts(apptID, apptTitle, apptDescription, apptLocation, apptType, startDateTime, endDateTime, customerID, userID, contactID);
            apptsObservableList.add(appts);


        }
        return apptsObservableList;

    }
    /**
     *
     * delete method for deleting appointment information from the dataabase
     *
     */
        public static int deleteAppt (int customer, Connection connection) throws SQLException
        {

            String query = "DELETE FROM Appointments WHERE Appointment_ID=?";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, customer);
            int result = ps.executeUpdate();
            ps.close();
            return result;

        }







}
