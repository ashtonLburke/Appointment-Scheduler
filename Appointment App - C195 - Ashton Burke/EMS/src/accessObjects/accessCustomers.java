package accessObjects;

import database.DatabaseConnect;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Customers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
/**
 *
 * DAO for accessing customer information
 *
 */
public class accessCustomers {
    /**
     *
     * getter that retrieves all customer information from the database and returns it to an observable list
     *
     *  Inner joins the divisions table with customers respective division ID
     *
     */
        public static ObservableList<Customers> getAllCusts(Connection connection) throws SQLException
        {
            String customerQuery = "SELECT customers.Customer_ID, " +
                    "customers.Customer_Name, customers.Address, customers.Postal_Code, " +
                    "customers.Phone, customers.Division_ID, " +
                    "first_level_divisions.Division FROM customers INNER JOIN  first_level_divisions ON customers.Division_ID = first_level_divisions.Division_ID";

            PreparedStatement ps = DatabaseConnect.getConnection().prepareStatement(customerQuery);
            ResultSet rs = ps.executeQuery();

            ObservableList<Customers> custs = FXCollections.observableArrayList();

            while (rs.next()) {
                int custID = rs.getInt("Customer_ID");
                String custName = rs.getString("Customer_Name");
                String custAddress = rs.getString("Address");
                String custZip = rs.getString("Postal_Code");
                String custPhone = rs.getString("Phone");
                int state = rs.getInt("Division_ID");
                String divisionName = rs.getString("Division");
                Customers customer = new Customers(custID, custName, custAddress, custZip, custPhone, divisionName, state);
                custs.add(customer);
            }
                    return custs;
        }


}
