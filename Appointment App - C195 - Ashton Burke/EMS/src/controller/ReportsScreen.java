package controller;

import accessObjects.accessAppts;
import accessObjects.accessContacts;
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
import model.Contacts;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

public class ReportsScreen {

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
    private ComboBox<String> GetContactReport;

    @FXML
    private TableView<Appts> ReportsTableView;

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
    void onActionContactScheduleReports (ActionEvent event){

        }


        @FXML
        void onActionCustomersbyMonthReports (ActionEvent event) throws IOException {
            Stage stage = (Stage) ((RadioButton) event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("/view/reports-bymonth.fxml"));
            stage.setScene(new Scene(scene));
            stage.show();
        }

        @FXML
        void onActionCustomersbyTypeReports (ActionEvent event) throws IOException {
            Stage stage = (Stage) ((RadioButton) event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("/view/reports-bytype.fxml"));
            stage.setScene(new Scene(scene));
            stage.show();
        }
    /**
     *
     * creates table view based on contact
     */
        @FXML
        void onActionGetContactReport (ActionEvent event) throws IOException {
            try {


                int contactID = 0;

                ObservableList<Appts> getAppts = accessAppts.getAllAppts();
                ObservableList<Appts> allAppts = FXCollections.observableArrayList();
                ObservableList<Contacts> getContacts = accessContacts.getAllContacts();

                Appts contactAppts = null;
                //retrieves data for customer table report
                String allContacts = GetContactReport.getSelectionModel().getSelectedItem();
                //for loop that retrieves contact ID and pairs it with customer name
                for (Contacts contacts: getContacts)
                {
                    if(allContacts.equals(contacts.getContactName()))
                    {
                        contactID = contacts.getContactID();
                    }
                }
                //for loop that retrieves appointment information based on customer id
                for (Appts appts: getAppts)
                {
                    if (appts.getContactID() == contactID)
                    {
                        contactAppts = appts;
                        allAppts.add(contactAppts);
                    }
                }
                ReportsTableView.setItems(allAppts);

            } catch (Exception e) {
                throw new RuntimeException(e);
            }


        }


        @FXML
        void onActionLogoutReports (ActionEvent event) throws IOException {
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
            ObservableList<Contacts> getContacts = accessContacts.getAllContacts();
            ObservableList<String> allContacts = FXCollections.observableArrayList();
            getContacts.forEach(contacts -> allContacts.add(contacts.getContactName()));
            GetContactReport.setItems(allContacts);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }



    }

