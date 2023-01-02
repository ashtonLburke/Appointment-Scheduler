package accessObjects;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Users;
import database.DatabaseConnect;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * DAO for accessing user information from database
 *
 */
public class accessUsers extends Users {
    /**
     * super reference variable used to declare divisions access as a parent class object (no constructor)
     * @param userID
     * @param userName
     * @param userPassword
     */
    public accessUsers(int userID, String userName, String userPassword) {
        super();
    }

    /**
     *
     * method that verifies user information entered on login matches user info stored in database
     *
     */
    public static int userAuthenticate(String username, String password) throws SQLException {

        try {

            String userQuery = "SELECT * FROM users WHERE User_Name = '" + username + "' AND Password = '" + password + "'";
            PreparedStatement ps = DatabaseConnect.getConnection().prepareStatement(userQuery);
            ResultSet rs = ps.executeQuery();
            rs.next();
            if (rs.getString("User_Name").equals(username)) {
                if (rs.getString("Password").equals(password)) {
                    return rs.getInt("User_ID");
                }
            }

        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     *
     * getter that retrieves user data from database and returns it to an observable list
     * @return
     * @throws SQLException
     */
    public static ObservableList<accessUsers> getUsers() throws SQLException
    {
            ObservableList<accessUsers> accessUsersObservableList = FXCollections.observableArrayList();
            String sql = "SELECT * FROM USERS";
            PreparedStatement ps = DatabaseConnect.getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                int userID = rs.getInt("User_ID");
                String userName = rs.getString("User_Name");
                String userPassword = rs.getString("Password");
                accessUsers users = new accessUsers(userID, userName, userPassword);
                accessUsersObservableList.add(users);

    }
    return accessUsersObservableList;


        }







}
