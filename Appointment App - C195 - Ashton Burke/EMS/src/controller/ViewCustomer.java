package controller;

import accessObjects.accessAppts;
import accessObjects.accessCustomers;
import database.DatabaseConnect;
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
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Optional;

public class ViewCustomer {

    Stage stage;
    Parent scene;

    private static Customers custForMod;
    @FXML
    private TableColumn<?, ?> custAddress;

    @FXML
    private TableColumn<?, ?> custCreator;

    @FXML
    private TableColumn<?, ?> custID;

    @FXML
    private TableColumn<?, ?> custLast;

    @FXML
    private TableColumn<?, ?> custLastBy;

    @FXML
    private TableColumn<?, ?> custName;

    @FXML
    private TableColumn<?, ?> custPhone;

    @FXML
    private TableColumn<?, ?> custState;

    @FXML
    private TableColumn<?, ?> custZip;

    @FXML
    private TableView<Customers> CustomersViewTableView;


    @FXML
    private ToggleGroup main;
    @FXML
    void onActionViewMonthCust(ActionEvent event) {

    }

    @FXML
    void onActionViewWeekCust(ActionEvent event) {

    }
    @FXML
    void onActionAddCust(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/customer-add.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }
    /**
     *
     *
     * Delete button action event
     * Removes selected customer from database and refreshes table
     */
    @FXML
    void onActionDeleteCust(ActionEvent event) throws SQLException {

        Connection connection = DatabaseConnect.getConnection();
        ObservableList<Appts> getAppts = accessAppts.getAllAppts();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Deleting customer information. Are you sure?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK)
        {
            //gets selected items ID
            int removeCustID = CustomersViewTableView.getSelectionModel().getSelectedItem().getCustomerID();
            //establishes potential deletion
            accessAppts.deleteAppt(removeCustID, connection);
            //sql statement that deletes selected customers data from database
            String removeCust = "DELETE FROM customers WHERE Customer_ID = ?";
            //allows exchange with database from program
            DatabaseConnect.setPS(DatabaseConnect.getConnection(), removeCust);
            PreparedStatement ps = DatabaseConnect.getPS();
            //gets selected items data
            int selectedCust = CustomersViewTableView.getSelectionModel().getSelectedItem().getCustomerID();
            //for loop that retrieves appointments based on customers id
            for (Appts appts: getAppts)
            {
                int custAppts = appts.getCustomerID();
                if (selectedCust == custAppts)
                    {
                        //if customer has any appointments, runs sql statement deleting them prior to customer deletion
                        String removeCustAppts = "DELETE FROM appointments WHERE Appointment_ID = ?";
                        DatabaseConnect.setPS(DatabaseConnect.getConnection(), removeCustAppts);
                    }

            }
            //deleted selected item from database and refreshes appointment table
            ps.setInt(1, selectedCust);
            ps.execute();
            ObservableList<Customers> custsReload = accessCustomers.getAllCusts(connection);
            CustomersViewTableView.setItems(custsReload);

        }




    }

    @FXML
    void onActionLogoutCustomer(ActionEvent event) throws IOException {
        //sets alert type to Confirmation
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "You will now be logged out. Are you sure?");
        //waits for user input before continuing
        Optional<ButtonType> result = alert.showAndWait();
        //if OK button is pressed then executes code block
        if(result.isPresent() && result.get() == ButtonType.OK)
        //sends user back to main form
        {
            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("/view/login-screen.fxml"));
            stage.setScene(new Scene(scene));
            stage.show();
        }
    }

    public static Customers getCustforMod()
    {
        return custForMod;
    }
    /**
     *
     * Modify button action event
     *
     */
    @FXML
    void onActionModifyCust(ActionEvent event) throws IOException {

        DatabaseConnect.getConnection();
        //retrieves data for modification
        custForMod = CustomersViewTableView.getSelectionModel().getSelectedItem();

        if (custForMod != null) {


            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("/view/customer-modify.fxml"));
            stage.setScene(new Scene(scene));
            stage.show();

            }



        else  {//sets alert type to Confirmation
            Alert alert = new Alert(Alert.AlertType.ERROR, "Please select a customer to modify.");
            //waits for user input before continuing
            Optional<ButtonType> result = alert.showAndWait();}

    }


    @FXML
    void onActionShowReportsCust(ActionEvent event) throws IOException {
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/reports-screen.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    @FXML
    void onActionViewAllCust(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((RadioButton) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/main-screen.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();

    }

    @FXML
    void onActionViewCustomersCust(ActionEvent event) {

    }

    /**
     *
     * Intializes main table view for customer
     *
     */
    public void initialize() throws SQLException {

        try {
            Connection connection = DatabaseConnect.getConnection();

            //sets values of customer data to respective columns
            ObservableList<Customers> allCusts = accessCustomers.getAllCusts(connection);
            custAddress.setCellValueFactory(new PropertyValueFactory<>("customerAddress"));
            custName.setCellValueFactory(new PropertyValueFactory<>("customerName"));
            custPhone.setCellValueFactory(new PropertyValueFactory<>("customerPhoneNumber"));
            custID.setCellValueFactory(new PropertyValueFactory<>("customerID"));
            custZip.setCellValueFactory(new PropertyValueFactory<>("customerPostalCode"));
            custState.setCellValueFactory(new PropertyValueFactory<>("divisionName"));

            CustomersViewTableView.setItems(allCusts);


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }



}
