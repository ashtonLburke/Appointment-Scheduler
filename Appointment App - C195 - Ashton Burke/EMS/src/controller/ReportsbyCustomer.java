package controller;

import accessObjects.accessAppts;
import accessObjects.accessCustomers;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Appts;
import model.Customers;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

import static database.DatabaseConnect.connection;

public class ReportsbyCustomer {
    Stage stage;
    Parent scene;
    @FXML
    private RadioButton ApptsbyCountryReports;

    @FXML
    private RadioButton ContactScheduleReports;

    @FXML
    private RadioButton CustomersbyMonthReports;

    @FXML
    private RadioButton CustomersbyTypeReports;

    @FXML
    private ComboBox<String> GetCountryReport;

    @FXML
    private TableView<Appts> ReportsbyCustomerTableView;

    @FXML
    private TableColumn<?, ?> apptContact;

    @FXML
    private TableColumn<?, ?> apptContactID;

    @FXML
    private TableColumn<?, ?> apptCustomerID;

    @FXML
    private TableColumn<?, ?> apptDescription;

    @FXML
    private TableColumn<?, ?> apptEnd;

    @FXML
    private TableColumn<?, ?> apptID;

    @FXML
    private TableColumn<?, ?> apptLocation;

    @FXML
    private TableColumn<?, ?> apptStart;

    @FXML
    private TableColumn<?, ?> apptTitle;

    @FXML
    private TableColumn<?, ?> apptType;

    @FXML
    private ToggleGroup reports;

    @FXML
    void onActionApptsbyCountryReports(ActionEvent event) {

    }
    /**
     *
     * Cancel button action event
     * Sends users back to main screen
      */
    @FXML
    void onActionCancelReports(ActionEvent event) throws IOException {
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
     * contact schedule report radio button
     * sends users to contact schedule report
     */
    @FXML
    void onActionContactScheduleReports(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((RadioButton) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/reports-screen.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    @FXML
    void onActionCustomersbyMonthReports(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((RadioButton) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/reports-bymonth.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    @FXML
    void onActionCustomersbyTypeReports(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((RadioButton) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/reports-bytype.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }
    /**
     *
     * creates table view based on customer
     *
     * method mentions country but that was from a previous attempt
     * to make a table view based on country
     *
     * decided to just leave it instead of refactoring everything
     */
    @FXML
    void onActionGetCountryReport(ActionEvent event) {
        try {


            int customerID = 0;

            ObservableList<Appts> getAppts = accessAppts.getAllAppts();
            ObservableList<Appts> allAppts = FXCollections.observableArrayList();
            ObservableList<Customers> getCusts = accessCustomers.getAllCusts(connection);

            Appts customerAppts = null;
            //retrieves data for customer table report
            String allCusts = GetCountryReport.getSelectionModel().getSelectedItem();
            //for loop that retrieves customer ID and pairs it with customer name
            for (Customers customers: getCusts)
            {
                if(allCusts.equals(customers.getCustomerName()))
                {
                    customerID = customers.getCustomerID();
                }
            }
            //for loop that retrieves appointment information based on customer id
            for (Appts appts: getAppts)
            {
                if (appts.getCustomerID() == customerID)
                {
                    customerAppts = appts;
                    allAppts.add(customerAppts);
                }
            }
            ReportsbyCustomerTableView.setItems(allAppts);

        } catch (Exception e) {
           e.printStackTrace(); ;
        }
    }

    @FXML
    void onActionLogoutReports(ActionEvent event) throws IOException {
        //sets alert type to Confirmation
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Returning to login screen. Do you want to continue?");
        //waits for user input before continuing
        Optional<ButtonType> result = alert.showAndWait();
        //if OK button is pressed then executes code block
        if (result.isPresent() && result.get() == ButtonType.OK)
        //sends user back to main form
        {
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("/view/login-screen.fxml"));
            stage.setScene(new Scene(scene));
            stage.show();

        }
    }
    /**
     *
     * Initializes table
     * sets data to respective columns
     *
     */
    public void initialize() throws SQLException {

        try {
            ObservableList<Appts> allAppts = accessAppts.getAllAppts();
            apptID.setCellValueFactory(new PropertyValueFactory<>("apptID"));
            apptContact.setCellValueFactory(new PropertyValueFactory<>("contactID"));
            apptDescription.setCellValueFactory(new PropertyValueFactory<>("apptDescription"));
            apptTitle.setCellValueFactory(new PropertyValueFactory<>("apptTitle"));
            apptLocation.setCellValueFactory(new PropertyValueFactory<>("apptLocation"));
            apptType.setCellValueFactory(new PropertyValueFactory<>("apptType"));
            apptCustomerID.setCellValueFactory(new PropertyValueFactory<>("customerID"));
            apptStart.setCellValueFactory(new PropertyValueFactory<>("startDateTime"));
            apptEnd.setCellValueFactory(new PropertyValueFactory<>("endDateTime"));
            ObservableList<Customers> getCustomers = accessCustomers.getAllCusts(connection);
            ObservableList<String> allCusts = FXCollections.observableArrayList();
            getCustomers.forEach(customers -> allCusts.add((customers.getCustomerName())));
            GetCountryReport.setItems(allCusts);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }
}
