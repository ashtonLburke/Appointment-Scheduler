package controller;
import accessObjects.accessCountries;
import accessObjects.accessCustomers;
import accessObjects.accessDivisions;
import database.DatabaseConnect;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Countries;
import model.Customers;
import model.Divisions;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.ResourceBundle;

public class AddCustomer implements Initializable {

    Stage stage;
    Parent scene;

    @FXML
    private TextField AddCustomerAddress;

    @FXML
    private TextField AddCustomerID;

    @FXML
    private TextField AddCustomerName;

    @FXML
    private TextField AddPhoneNumber;

    @FXML
    private TextField AddPostalCode;

    @FXML
    private ComboBox<String> AddSelectCountry;

    @FXML
    private ComboBox<String> AddSelectState;
    /**
     *
     * action event for the cancel button
     * sends user back to main screen
     * includes confirmation message
     */
    @FXML
    void onActionCancelAdd(ActionEvent event) throws IOException {
        //sets alert type to Confirmation
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "You will lose unsaved values. Do you want to continue?");
        //waits for user input before continuing
        Optional<ButtonType> result = alert.showAndWait();
        //if OK button is pressed then executes code block
        if (result.isPresent() && result.get() == ButtonType.OK)
        //sends user back to main form
        {
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("/view/main-screen.fxml"));
            stage.setScene(new Scene(scene));
            stage.show();

        }

    }
    /**
     *
     * saves added customers information to database
     *
     */
    @FXML
    void saveAddCustomer(ActionEvent event) {
        try {
            Connection connection = DatabaseConnect.getConnection();
            //if all text fields are filled, saves customer
            if (!AddCustomerName.getText().isEmpty() || !AddPostalCode.getText().isEmpty()
                    || !AddPhoneNumber.getText().isEmpty() || !AddCustomerAddress.getText().isEmpty() ||
                    AddSelectCountry.getValue().isEmpty() || AddSelectState.getValue().isEmpty()) {

                int divisionName = 0;
            // randomizes customer ID
                Integer newCustID = (int) (Math.random() * 50);
                //for loop that retrieves division and state information for the combo box
                for (accessDivisions divisions : accessDivisions.getDivisions()) {
                    if (AddSelectState.getSelectionModel().getSelectedItem().equals(divisions.getDivisionName())) {
                        divisionName = divisions.getDivisionID();
                    }
                }
                //SQL statement that sends inputed information to the database while assigning each set to its respective column
                String addStatement = "INSERT INTO customers (Customer_ID, Customer_Name, Address, Postal_Code, Phone, Create_Date, Created_By, Last_Update, Last_Updated_By, Division_ID) VALUES (?,?,?,?,?,?,?,?,?,?)";
                DatabaseConnect.setPS(DatabaseConnect.getConnection(), addStatement);
                PreparedStatement ps = DatabaseConnect.getPS();
                ps.setInt(1, newCustID);
                ps.setString(2, AddCustomerName.getText());
                ps.setString(3, AddCustomerAddress.getText());
                ps.setString(4, AddPostalCode.getText());
                ps.setString(5, AddPhoneNumber.getText());
                ps.setTimestamp(6, Timestamp.valueOf(LocalDateTime.now()));
                ps.setString(7, "admin");
                ps.setTimestamp(8, Timestamp.valueOf(LocalDateTime.now()));
                ps.setString(9, "admin");
                ps.setInt(10, divisionName);
                ps.execute();


            }
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("/view/customer-view.fxml"));
            stage.setScene(new Scene(scene));
            stage.show();

            //sets alert type to Confirmation
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Customer successfully deleted.");
            //waits for user input before continuing
            Optional<ButtonType> result = alert.showAndWait();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * Only shows state/division data for the selected country
     * Prevents all other state/diuvision data from showing in the combo box
     *
     * Includes lamda expression for if statements that retrieve each country's respective state information
     */
    @FXML
    void onActionSelectCountry(ActionEvent event) {
        try {
            DatabaseConnect.getConnection();
            //gets selected country from combo box
            String selectedCountry = AddSelectCountry.getSelectionModel().getSelectedItem();

            ObservableList<accessDivisions> getDivisions = accessDivisions.getDivisions();

            ObservableList<String> divisionsUS = FXCollections.observableArrayList();
            ObservableList<String> divisionsUK = FXCollections.observableArrayList();
            ObservableList<String> divisionsCA = FXCollections.observableArrayList();
                // Lambda expression #1 (retrieves only the selected countries state information)
            getDivisions.forEach(divisions -> {
                if (divisions.getCountryID() == 1) {
                    divisionsUS.add(divisions.getDivisionName());
                } else if (divisions.getCountryID() == 2) {
                    divisionsUK.add(divisions.getDivisionName());
                } else if (divisions.getCountryID() == 3) {
                    divisionsCA.add(divisions.getDivisionName());
                }
            });
            //Sets selected country's state information the combo box
            if (selectedCountry.equals("U.S")) {
                AddSelectState.setItems(divisionsUS);
            }
            else if (selectedCountry.equals("UK")) {
                AddSelectState.setItems(divisionsUK);
            }
            else if (selectedCountry.equals("Canada")) {
                AddSelectState.setItems(divisionsCA);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void onActionSelectState(ActionEvent event) {

    }

    /**
     * Initializes country and division data
     * Sends data to their respective observable lists
     *
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            Connection connection = DatabaseConnect.getConnection();


            ObservableList<accessDivisions> getDivisions = accessDivisions.getDivisions();
            ObservableList<String> divisionNames = FXCollections.observableArrayList();
            ObservableList<Countries> getCountries = accessCountries.getCountries();
            ObservableList<String> countryNames = FXCollections.observableArrayList();
            ObservableList<Customers> allCusts = accessCustomers.getAllCusts(connection);

            //function mapper that returns stream informaiton of country list to the combo box
            getCountries.stream().map(Countries::getCountryName).forEach(countryNames::add);
            AddSelectCountry.setItems(countryNames);
            //for loop that does the same thing the function mapper does but for division data
           for (Divisions Divisions : getDivisions) {
                divisionNames.add(Divisions.getDivisionName());
            }
            AddSelectState.setItems(divisionNames);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}