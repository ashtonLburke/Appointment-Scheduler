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
import model.ReportsforType;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Optional;

public class ReportsbyType {
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
    private TableView<ReportsforType> ReportsTableView;

    @FXML
    private TableColumn<?, ?> apptTotalbyType;

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
    void onActionCustomersbyTypeReports(ActionEvent event) {

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
     * creates table view on initialization based on type of appointment
     *
     *  Lamda #1 seperates the appointments by type
     *
     *
     *
     *
     */
    public void initialize() throws SQLException {

        try {
            //sets table data to respective columms
            ObservableList<Appts> allAppts = accessAppts.getAllAppts();
            apptTotalbyType.setCellValueFactory(new PropertyValueFactory<>("apptType"));
            totalAppointments.setCellValueFactory(new PropertyValueFactory<>("apptTotal"));


            ObservableList<Appts> getAppts = accessAppts.getAllAppts();

            ObservableList<String> apptbyType = FXCollections.observableArrayList();
            ObservableList<String> differrentTypeAppt = FXCollections.observableArrayList();

            ObservableList<ReportsforType> reportByType = FXCollections.observableArrayList();

             //   LAMDA #1
            allAppts.forEach(appts -> { apptbyType.add(appts.getApptType()); });

            //for loop that sets and seperates each appointment to their respective types
            for (Appts appointments : allAppts) {
                String apptsType = appointments.getApptType();
                if (!differrentTypeAppt.contains(apptsType)) {
                    differrentTypeAppt.add(apptsType);
                }
            }

            //counts the amount of appointments per type
            for (String types : differrentTypeAppt) {
                String setType = types;
                int totalAppts = Collections.frequency(apptbyType, types);
                ReportsforType allApptTypes = new ReportsforType(setType, totalAppts);
                reportByType.add(allApptTypes);
            }
            //sets data to the table view
            ReportsTableView.setItems(reportByType);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}





