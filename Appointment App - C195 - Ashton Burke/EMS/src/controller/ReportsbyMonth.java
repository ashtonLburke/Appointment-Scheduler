package controller;

import accessObjects.accessAppts;
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
import model.ReportsforMonth;

import java.io.IOException;
import java.sql.SQLException;
import java.time.Month;
import java.util.Collections;
import java.util.Optional;

public class ReportsbyMonth {
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
    private TableView<ReportsforMonth> ReportsTableView;

    @FXML
    private TableColumn<?, ?> apptTotalbyMonth;

    @FXML
    private ToggleGroup reports;

    @FXML
    private TableColumn<?, ?> totalAppointments;

    @FXML
    void onActionApptsbyCountryReports(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((RadioButton) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/reports-bycustomer.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
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
    void onActionCustomersbyMonthReports(ActionEvent event) {

    }

    @FXML
    void onActionCustomersbyTypeReports(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((RadioButton) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/reports-bytype.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
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
     * creates table view on initialization based on month of appointment
     * holds two seperate lamda expressions
     *  Lamda #1 uses a function mapper to retrieve month and time of each appointment and send it to the apptbymonth observable list
     *
     *  Lamda#2 uses another stream filter that returns a stream of data that matches the listed predicate
     *  filters the list of appointments by month
     *
     *
     */
    public void initialize() throws SQLException {



        try {
            //sets table data to respective columms
            apptTotalbyMonth.setCellValueFactory(new PropertyValueFactory<>("apptMonth"));
            totalAppointments.setCellValueFactory(new PropertyValueFactory<>("apptTotal"));
            ObservableList<Appts> allAppts = accessAppts.getAllAppts();

            ObservableList<Month> apptbyMonth = FXCollections.observableArrayList();
            ObservableList<Month> differrentMonth = FXCollections.observableArrayList();

            ObservableList<ReportsforMonth> reportByMonth = FXCollections.observableArrayList();

                             //Lambda #1
            allAppts.stream().map(appt -> { return appt.getStartDateTime().getMonth(); }).forEach(apptbyMonth::add);

                            //Lambda #2
            apptbyMonth.stream().filter(months -> { return !differrentMonth.contains(months); }).forEach(differrentMonth::add);
        //for loop that utilizes collections.frequency method that counts the frequency of the declared element
            for (Month month: differrentMonth) {
                int apptTotalbyMonth = Collections.frequency(apptbyMonth, month);
                String months = month.name();
                ReportsforMonth apptbyMonths = new ReportsforMonth(months, apptTotalbyMonth);
                reportByMonth.add(apptbyMonths);
            }
           ReportsTableView.setItems(reportByMonth);



        } catch (Exception e) {
            e.printStackTrace();
        }



    }

}
