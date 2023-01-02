package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
/**
 *
 * Establishes a connection with the database while also holding
 * getters and setters for methods retrieving the connection to access the database
 *
 */
public abstract class DatabaseConnect {


        private static final String protocol = "jdbc";
        private static final String vendor = ":mysql:";
        private static final String location = "//localhost/";
        private static final String databaseName = "client_schedule";
        private static final String jdbcUrl = protocol + vendor + location + databaseName + "?connectionTimeZone = SERVER"; // LOCAL
        private static final String driver = "com.mysql.cj.jdbc.Driver"; // Driver reference
        private static final String userName = "sqlUser"; // Username
        private static String password = "Dudeiscool1?"; // Password
        public static Connection connection;  // Connection Interface
        public static void openConnection()
        {
                try {
                        Class.forName(driver); // Locate Driver
                        connection = DriverManager.getConnection(jdbcUrl, userName, password); // Reference Connection object
                        System.out.println("Connection successful!");
                }
                catch(Exception e)
                {
                        System.out.println("Error:" + e.getMessage());
                }
        }

        public static void closeConnection() {
                try {
                        connection.close();
                        System.out.println("Connection closed!");
                }
                catch(Exception e)
                {
                        System.out.println("Error:" + e.getMessage());
                }
        }


        public static Connection getConnection() {
                return connection;
        }

        private static PreparedStatement pS;
        /**
         *
         * Method that allows the use of a connection with the prepareStatement java/sql statement
         *
         */
        public static void setPS (Connection connection, String sqlStatement) throws SQLException {
                pS = connection.prepareStatement(sqlStatement);
        }

        public static PreparedStatement getPS() {

                return pS;
        }

}