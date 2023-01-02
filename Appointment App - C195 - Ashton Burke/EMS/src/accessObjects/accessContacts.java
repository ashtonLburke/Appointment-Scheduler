package accessObjects;

import database.DatabaseConnect;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Contacts;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
/**
 *
 * DAO for the contacts
 */
public class accessContacts
{

    /**
     *
     * getter that retrieves all contact information from the database and returns it to an observable list
     */
    public static ObservableList<Contacts> getAllContacts() throws SQLException {

        ObservableList<Contacts> allContacts = FXCollections.observableArrayList();
        String sql = "SELECT * FROM contacts";
        PreparedStatement ps = DatabaseConnect.getConnection().prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            int contactID = rs.getInt("Contact_ID");
            String contactName = rs.getString("Contact_Name");
            String contactEmail = rs.getString("Email");
            Contacts contacts = new Contacts(contactID, contactName, contactEmail);
            allContacts.add(contacts);

        }

        return allContacts;

    }

    /**
     *
     * getter that retrieves contact ID from database
     *
     */
    public static String getContactID(String contactID) throws SQLException{

        PreparedStatement ps = DatabaseConnect.getConnection().prepareStatement("SELECT * FROM contacts WHERE Contact_Name = ?");
        ps.setString(1, contactID);
        ResultSet rs = ps.executeQuery();
        while (rs.next()){
            contactID = rs.getString("Contact_ID");
        }
        return contactID;
    }

}
