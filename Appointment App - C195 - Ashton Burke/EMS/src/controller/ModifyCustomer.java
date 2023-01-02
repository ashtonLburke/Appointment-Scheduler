package controller;

import accessObjects.accessCountries;
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
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.ResourceBundle;

import static java.lang.String.valueOf;
/**
 *
 *
 *
 */
public class ModifyCustomer implements Initializable {


    Stage stage;
    Parent scene;
    private Customers selectedCust;
    @FXML
    private TextField CustomerAddressMod;

    @FXML
    private TextField CustomerIDMod;

    @FXML
    private TextField CustomerNameMod;

    @FXML
    private TextField CustomerPostalCodeMod;

    @FXML
    private TextField PhoneNumberMod;

    @FXML
    private ComboBox<String> SelectCountryMod;

    @FXML
    private ComboBox<String> SelectStateMod;
    /**
     *
     * action event for the cancel button
     * sends user back to main screen
     * includes confirmation message
     *
     */
    @FXML
    void onActionCancelMod(ActionEvent event) throws IOException {
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
    void saveModdedCust(ActionEvent event) throws IOException {
        try {
            Connection connection = DatabaseConnect.getConnection();
            //if all text fields are filled, saves customer
            if (!CustomerNameMod.getText().isEmpty() || !CustomerPostalCodeMod.getText().isEmpty()
                    || !PhoneNumberMod.getText().isEmpty() || !CustomerAddressMod.getText().isEmpty() ||
                    SelectCountryMod.getValue().isEmpty() || SelectStateMod.getValue().isEmpty()) {

                int divisionName = 0;
                // randomizes customer ID
                Integer newCustID = (int) (Math.random() * 50);
                //for loop that retrieves division and state information for the combo box
                for (accessDivisions divisions : accessDivisions.getDivisions()) {
                    if (SelectStateMod.getSelectionModel().getSelectedItem().equals(divisions.getDivisionName())) {

                        divisionName = divisions.getDivisionID();

                    }
                }
                //SQL statement that sends inputed information to the database while assigning each set to its respective column
                String addStatement = "UPDATE customers SET Customer_ID = ?, Customer_Name = ?, Address = ?, Postal_Code = ?, Phone = ?, Create_Date = ?, Created_By = ?, Last_Update = ?, Last_Updated_By = ?, Division_ID = ? WHERE Customer_ID = ?";
                DatabaseConnect.setPS(DatabaseConnect.getConnection(), addStatement);
                PreparedStatement ps = DatabaseConnect.getPS();
                ps.setInt(1, Integer.parseInt(CustomerIDMod.getText()));
                ps.setString(2, CustomerNameMod.getText());
                ps.setString(3, CustomerAddressMod.getText());
                ps.setString(4, CustomerPostalCodeMod.getText());
                ps.setString(5, PhoneNumberMod.getText());
                ps.setTimestamp(6, Timestamp.valueOf(LocalDateTime.now()));
                ps.setString(7, "admin");
                ps.setTimestamp(8, Timestamp.valueOf(LocalDateTime.now()));
                ps.setString(9, "admin");
                ps.setInt(10, divisionName);
                ps.setInt(11, Integer.parseInt(CustomerIDMod.getText()));
                ps.execute();



            }
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("/view/customer-view.fxml"));
            stage.setScene(new Scene(scene));
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     *
     *Only shows state/division data for the selected country
     * Prevents all other state/diuvision data from showing in the combo box
     *
     * Includes lamda expression for if statements that retrieve each country's respective state information
     *
     */

    @FXML
    void onActionSelectCountryMod(ActionEvent event) {
        try {
            DatabaseConnect.getConnection();
            //gets selected country from combo box
            String selectedCountry = SelectCountryMod.getSelectionModel().getSelectedItem();

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
                SelectStateMod.setItems(divisionsUS);
            } else if (selectedCountry.equals("UK")) {
                SelectStateMod.setItems(divisionsUK);
            } else if (selectedCountry.equals("Canada")) {
                SelectStateMod.setItems(divisionsCA);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @FXML
    void onActionSelectStateMod(ActionEvent event) {

    }
    /**
     *
     *Initializes country and division data
     *  Sends data to their respective observable lists
     *
     *
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            Connection connection = DatabaseConnect.getConnection();
            //retrieves selected customers information
            selectedCust = ViewCustomer.getCustforMod();
            //sets variables to empty strings
            String division = "", country = "";

            ObservableList<accessDivisions> getDivisions = accessDivisions.getDivisions();
            ObservableList<Countries> getCountries = accessCountries.getCountries();
            ObservableList<String> allDivisions = FXCollections.observableArrayList();

            //sets values to columns
            CustomerIDMod.setText(valueOf(selectedCust.getCustomerID()));
            CustomerNameMod.setText(selectedCust.getCustomerName());
            CustomerAddressMod.setText(selectedCust.getCustomerAddress());
            CustomerPostalCodeMod.setText(selectedCust.getCustomerPostalCode());
            PhoneNumberMod.setText(selectedCust.getCustomerPhoneNumber());
            //for loop that does the same thing the function mapper does but for division data
            for (Divisions divisions : getDivisions) {
                allDivisions.add(divisions.getDivisionName());
                int countryIDToSet = divisions.getCountryID();

                //gets division of selected customer
                if (divisions.getDivisionID() == selectedCust.getDivisionID()) {
                    division = divisions.getDivisionName();
                    //for loop that sets country of selected customer
                    for (Countries countries : getCountries) {
                        if (countries.getCountryID() == countryIDToSet) {
                            country = countries.getCountryName();



                        }
                    }
                }
            }
            //sets division and country values to combo boxes
            SelectStateMod.setValue(division);
            SelectCountryMod.setValue(country);
        }
        catch (SQLException sqe) {
            new RuntimeException(sqe);
        }
    }
}




