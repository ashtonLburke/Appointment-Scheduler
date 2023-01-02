package controller;
import accessObjects.accessAppts;
import accessObjects.accessContacts;
import database.DatabaseConnect;
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
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Optional;

public class MainScreen  {

        Stage stage;
        Parent scene;

        private static Appts apptForMod;

        @FXML
        private TableColumn<?, ?> apptContact;

        @FXML
        private TableColumn<?, ?> apptDescription;

        @FXML
        private TableColumn<?, ?> apptEndDate;



        @FXML
        private TableColumn<?, ?> apptID;

        @FXML
        private TableColumn<?, ?> apptLocation;

        @FXML
        private TableColumn<?, ?> apptStartDate;



        @FXML
        private TableColumn<?, ?> apptTitle;

        @FXML
        private TableColumn<?, ?> apptType;

        @FXML
        private TableColumn<?, ?> custID;

        @FXML
        private ToggleGroup main;

        @FXML
        private TableColumn<?, ?> userID;
        @FXML
        private TableView<Appts> MainTableView;

        /**
         *
         * Add appointment action event for add appointment button
         *
         */
        @FXML
        void onActionAddAppt(ActionEvent event) throws IOException {

                stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                scene = FXMLLoader.load(getClass().getResource("/view/appt-add.fxml"));
                stage.setScene(new Scene(scene));
                stage.show();

        }
        /**
         *
         *
         * Delete button action event
         * Removes selected appointment from database and refreshes table
         */
        @FXML
        void onActionDeleteAppt(ActionEvent event) throws SQLException {
                Connection connection = DatabaseConnect.getConnection();
                //gets selected items ID
                int removeID = MainTableView.getSelectionModel().getSelectedItem().getApptID();
                //gets selected items type
                String removeType = MainTableView.getSelectionModel().getSelectedItem().getApptType();
                Alert alert = new Alert (Alert.AlertType.CONFIRMATION, "Deleting following appointment: " + removeID + " and appointment type " + removeType + "");
                Optional<ButtonType> result = alert.showAndWait();
                if (result.isPresent() && result.get() == ButtonType.OK)
                {
                        //deleted selected item from database and refreshes appointment table
                        accessAppts.deleteAppt(removeID, connection);
                        ObservableList<Appts> allAppts = accessAppts.getAllAppts();
                        MainTableView.setItems(allAppts);

                }

        }
        /**
         *
         * Log out button action event
         * Sends user to login screen
         */
        @FXML
        void onActionLogout(ActionEvent event) throws IOException {
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
        /**
         *
         * Getter for selected appointment chosen for modification
         *
         */
        public static Appts getApptforMod()
        {
                return apptForMod;
        }

        /**
         *
         * Modify button action event
         *
         */
        @FXML
        void onActionModifyAppt(ActionEvent event) throws IOException, SQLException {

                DatabaseConnect.getConnection();
                //gets selected appointment
                apptForMod = MainTableView.getSelectionModel().getSelectedItem();
                        //if appointment is selected, gets contact information and sends it to the combo box
                if (apptForMod != null) {
                        ObservableList<Contacts> allContacts = accessContacts.getAllContacts();
                        ObservableList<String> contactNames = FXCollections.observableArrayList();
                        String contactName = "";
                                //for loop that retrieves all contact information
                        for (Contacts contact: allContacts) {
                                if (apptForMod.getContactID() == contact.getContactID()) {
                                        contactName = contact.getContactName();
                                }


                        }
                        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                        scene = FXMLLoader.load(getClass().getResource("/view/appt-modify.fxml"));
                        stage.setScene(new Scene(scene));
                        stage.show();
                }
                        //if no appointment is selected, gives error message
                else  {
                        //sets alert type to Confirmation
                        Alert alert = new Alert(Alert.AlertType.ERROR, "Please select an appointment to modify.");
                //waits for user input before continuing
                Optional<ButtonType> result = alert.showAndWait();}

        }
        @FXML
        private DatePicker MainScreenDate;

        /**
         *
         * Reports button action event
         * sends user to reports screen
         *
         */
        @FXML
        void onActionShowReports(ActionEvent event) throws IOException {
                stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                scene = FXMLLoader.load(getClass().getResource("/view/reports-screen.fxml"));
                stage.setScene(new Scene(scene));
                stage.show();

        }
        /**
         *
         * View all radio button action event
         * presents all appointment information
         *
         */
        @FXML
        void onActionViewAll(ActionEvent event) throws SQLException {

                ObservableList<Appts> apptsObservableList = accessAppts.getAllAppts();

                MainTableView.setItems(apptsObservableList);


        }
        /**
         *
         * View customers action event
         * Sends user to view customers form
         */
        @FXML
        void onActionViewCustomers(ActionEvent event) throws IOException {
                stage = (Stage) ((RadioButton) event.getSource()).getScene().getWindow();
                scene = FXMLLoader.load(getClass().getResource("/view/customer-view.fxml"));
                stage.setScene(new Scene(scene));
                stage.show();

        }
        /**
         *
         * view by month radiobutton action event
         * arranges appointment information by month
         * Holds lamda expression #2
         */
        @FXML
        void onActionViewMonth(ActionEvent event) {

                try{
                                //sets observable lists to hold appointments
                       ObservableList<Appts> getAppts = accessAppts.getAllAppts();
                       ObservableList<Appts> apptsByMonth = FXCollections.observableArrayList();
                                //sets parameters for the order of appointment by month table
                        LocalDateTime monthTop = LocalDateTime.now().minusMonths(1);
                        LocalDateTime monthBot = LocalDateTime.now().plusMonths(1);

                        if (getAppts != null)
                                        //Lambda statement #2
                                        // pushes each appointment through the if statement and sets its position in the table view accordingly
                                getAppts.forEach(appts -> {
                                        if (appts.getEndDateTime().isAfter(monthTop) && appts.getEndDateTime().isBefore(monthBot))
                                        {
                                                apptsByMonth.add(appts);
                                        }
                                        MainTableView.setItems(apptsByMonth);
                                });

                } catch (SQLException e) {
                        throw new RuntimeException(e);
                }


        }
        /**
         *
         *view by month radiobutton action event
         * arranges appointment information by month
         * Holds lamda expression #3
         *
         */
        @FXML
        void onActionViewWeek(ActionEvent event) {
                try{
                        //sets observable lists to hold appointments
                        ObservableList<Appts> getAppts = accessAppts.getAllAppts();
                        ObservableList<Appts> apptsByWeek = FXCollections.observableArrayList();
                        //sets parameters for the order of appointment by week table
                        LocalDateTime weekTop = LocalDateTime.now().minusWeeks(1);
                        LocalDateTime weekBot = LocalDateTime.now().plusWeeks(1);

                        if (getAppts != null)
                                //Lambda statement #3
                                // pushes each appointment through the if statement and sets its position in the table view accordingly
                                getAppts.forEach(appts -> {
                                        if (appts.getEndDateTime().isAfter(weekTop) && appts.getEndDateTime().isBefore(weekBot))
                                        {
                                                apptsByWeek.add(appts);
                                        }
                                        MainTableView.setItems(apptsByWeek);
                                });

                } catch (SQLException e) {
                        throw new RuntimeException(e);
                }
        }

        /**
         *
         * Initializes table views
         * sends data to their respective columns
         *
         */
        public void initialize() throws SQLException{

                try {
                        ObservableList<Appts> allAppts = accessAppts.getAllAppts();
                        apptID.setCellValueFactory(new PropertyValueFactory<>("apptID"));
                        apptContact.setCellValueFactory(new PropertyValueFactory<>("contactID"));
                        apptDescription.setCellValueFactory(new PropertyValueFactory<>("apptDescription"));
                        apptTitle.setCellValueFactory(new PropertyValueFactory<>("apptTitle"));
                        apptLocation.setCellValueFactory(new PropertyValueFactory<>("apptLocation"));
                        apptType.setCellValueFactory(new PropertyValueFactory<>("apptType"));
                        custID.setCellValueFactory(new PropertyValueFactory<>("customerID"));
                        userID.setCellValueFactory(new PropertyValueFactory<>("userID"));
                        apptStartDate.setCellValueFactory(new PropertyValueFactory<>("startDateTime"));
                        apptEndDate.setCellValueFactory(new PropertyValueFactory<>("endDateTime"));
                        MainTableView.setItems(allAppts);


                } catch (SQLException e) {
                        throw new RuntimeException(e);
                }


        }
}


