package controller;

import accessObjects.accessAppts;
import accessObjects.accessContacts;
import accessObjects.accessCustomers;
import accessObjects.accessUsers;
import database.DatabaseConnect;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Appts;
import model.Contacts;
import model.Customers;
import model.Users;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import static database.timeConvert.timeDateConversion;
import static java.lang.String.valueOf;

/**
 *
 *
 *
 */
public class ModifyAppt {
    Stage stage;
    Parent scene;

    private Appts selectedAppt;

    @FXML
    private ComboBox<String> ModifyContactIDCombo;

    @FXML
    private TextField ModifyCustomerField;

    @FXML
    private TextField ModifyApptID;

    @FXML
    private TextField ModifyDescriptionField;

    @FXML
    private ComboBox<String> ModifyEndCombo;

    @FXML
    private DatePicker ModifyEndDate;

    @FXML
    private TextField ModifyLocationField;

    @FXML
    private ComboBox<String> ModifyStartCombo;

    @FXML
    private DatePicker ModifyStartDate;

    @FXML
    private TextField ModifyTitleField;

    @FXML
    private TextField ModifyTypeField;

    @FXML
    private TextField ModifyUserField;
    /**
     *
     *action event for the cancel button
     *   sends user back to main screen
     *  ncludes confirmation message
     *
     */
    @FXML
    void onActionCancelModifyAppt(ActionEvent event) throws IOException {
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
    void onActionSaveModifyAppt(ActionEvent event) {
        try {
            Connection connection = DatabaseConnect.getConnection();
            //if all text fields are filled, saves modified appointment
            if (!ModifyTitleField.getText().isEmpty() && !ModifyDescriptionField.getText().isEmpty()
                    && !ModifyDescriptionField.getText().isEmpty() && !ModifyTypeField.getText().isEmpty() &&
                    ModifyStartDate.getValue() != null && ModifyEndDate.getValue() != null && !ModifyStartCombo.getValue().isEmpty()
                    && !ModifyEndCombo.getValue().isEmpty() && !ModifyCustomerField.getText().isEmpty()) {
                //creates all required observable lists for user and customer information
                ObservableList<Customers> getAllCusts = accessCustomers.getAllCusts(connection);
                ObservableList<Integer> custIDList = FXCollections.observableArrayList();
                ObservableList<accessUsers> getUsers = accessUsers.getUsers();
                ObservableList<Integer> userIDList = FXCollections.observableArrayList();
                ObservableList<Appts> getAppts = accessAppts.getAllAppts();

                //function mapper that returns stream informaiton of customerID list
                getAllCusts.stream().map(Customers::getCustomerID).forEach(custIDList::add);
                //function mapper that returns stream informaiton of userID list
                getUsers.stream().map(Users::getUserID).forEach(userIDList::add);
                //getter for local start and end dates
                LocalDate localAddEnd = ModifyEndDate.getValue();
                LocalDate localAddStart = ModifyStartDate.getValue();
                //sets format for time
                DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("HH:mm");
                //sets format for date
                String apptStartDate = ModifyStartDate.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                String apptStartTime = ModifyStartCombo.getValue();

                String endDate = ModifyEndDate.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                String endTime = ModifyEndCombo.getValue();

                //universal time conversion method
                String universalStart = timeDateConversion(apptStartDate + " " + apptStartTime + ":00");
                String universalEnd = timeDateConversion(endDate + " " + endTime + ":00");
                //gets selected appointment times
                LocalTime localTimeStart = LocalTime.parse(ModifyStartCombo.getValue(), timeFormat);
                LocalTime LocalTimeEnd = LocalTime.parse(ModifyEndCombo.getValue(), timeFormat);
                //generalized appointment times
                LocalDateTime apptStart = LocalDateTime.of(localAddStart, localTimeStart);
                LocalDateTime apptEnd = LocalDateTime.of(localAddEnd, LocalTimeEnd);
                //times based on zone ID
                ZonedDateTime zoneIDStart = ZonedDateTime.of(apptStart, ZoneId.systemDefault());
                ZonedDateTime zoneIDEnd = ZonedDateTime.of(apptEnd, ZoneId.systemDefault());
                //sets EST zone ID
                ZonedDateTime estConversionStart = zoneIDStart.withZoneSameInstant(ZoneId.of("America/New_York"));
                ZonedDateTime estConversionEnd = zoneIDEnd.withZoneSameInstant(ZoneId.of("America/New_York"));
                //converts to EST
                LocalTime checkApptStart = estConversionStart.toLocalTime();
                LocalTime checkApptEnd = estConversionEnd.toLocalTime();
                //converts dates to EST
                DayOfWeek checkApptStartDay = estConversionStart.toLocalDate().getDayOfWeek();
                DayOfWeek checkApptEndDay = estConversionEnd.toLocalDate().getDayOfWeek();

                int apptStartDay = checkApptStartDay.getValue();
                int apptEndDay = checkApptEndDay.getValue();

                int startOfWeek = DayOfWeek.MONDAY.getValue();
                int endOfWeek = DayOfWeek.FRIDAY.getValue();
                //opening and closing times of operations
                LocalTime localStart = LocalTime.of(8, 0, 0);
                LocalTime localClose = LocalTime.of(22, 0, 0);
                //if appointment is schedules outside of work week, then cancel appointment creation
                if (apptStartDay < startOfWeek || apptStartDay > endOfWeek || apptEndDay < startOfWeek || apptEndDay > endOfWeek) {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "SCHEDULE ERROR: Cannot schedule appointments outside of work week!");
                    Optional<ButtonType> result = alert.showAndWait();

                    return;
                }
                // if appointment is scheduled before 8:00 or after 22:00 then cancel appointment creation
                if (checkApptStart.isBefore(localStart) || checkApptStart.isAfter(localClose) || checkApptEnd.isBefore(localStart) || checkApptEnd.isAfter(localClose)) {

                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "SCHEDULE ERROR: Cannot schedule appointments outside of regular working hours!");
                    Optional<ButtonType> result = alert.showAndWait();
                    return;
                }
                //randomizes appointment ID
                int newApptID = Integer.parseInt(valueOf((int) (Math.random() * 50)));
                int customerID = Integer.parseInt(ModifyCustomerField.getText());
                //if appointment start time is after end time cancels appointment information
                if (apptStart.isAfter(apptEnd)) {

                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "LOGIC ERROR: Cannot set end time before start time!");
                    Optional<ButtonType> result = alert.showAndWait();
                    return;
                }
                //if appointment start and end times are equal then cancels appointment information
                if (apptStart.isEqual(apptEnd)) {

                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "LOGIC ERROR: Appointment cannot have same start and end times!");
                    Optional<ButtonType> result = alert.showAndWait();
                    return;
                }
                //for loop that retrieves start and end times
                for (Appts appointment : getAppts) {
                    LocalDateTime checkStart = appointment.getStartDateTime();
                    LocalDateTime checkEnd = appointment.getEndDateTime();

                    //if customer has appointment scheduled during chosen date and times then cancels appointment creation
                    if ((customerID == appointment.getCustomerID()) && (newApptID != appointment.getApptID()) &&
                            (apptStart.isBefore(checkStart)) && (apptEnd.isAfter(checkEnd))) {
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "SCHEDULE ERROR: Appointment already scheduled during requested date and times!");
                        Optional<ButtonType> result = alert.showAndWait();

                        return;
                    }
                    //if customer has appointment scheduled during chosen date and times then cancels appointment creation
                    if ((customerID == appointment.getCustomerID()) && (newApptID != appointment.getApptID()) &&
                            (apptStart.isAfter(checkStart)) && (apptStart.isBefore(checkEnd))) {
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "SCHEDULE ERROR: Appointment already scheduled during requested date and times!");
                        Optional<ButtonType> result = alert.showAndWait();

                        return;
                    }

                    //if customer has appointment scheduled during chosen date and times then cancels appointment creation
                    if (customerID == appointment.getCustomerID() && (newApptID != appointment.getApptID()) &&
                            (apptEnd.isAfter(checkStart)) && (apptEnd.isBefore(checkEnd))) {
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "SCHEDULE ERROR: Appointment already scheduled during requested date and times!");
                        Optional<ButtonType> result = alert.showAndWait();

                        return;
                    }
                }
                //SQL statement that updates appointment information in the database
                String updateInsert = "UPDATE appointments SET Appointment_ID = ?, Title = ?, Description = ?, Location = ?, Type = ?, Start = ?, End = ?, Last_Update = ?, Last_Updated_By = ?, Customer_ID = ?, User_ID = ?, Contact_ID = ? WHERE Appointment_ID = ?";



                DatabaseConnect.setPS(DatabaseConnect.getConnection(), updateInsert);
                PreparedStatement ps = DatabaseConnect.getPS();

                //sends each respective set of information to their columns in the appointments table
                ps.setInt(1, Integer.parseInt(ModifyApptID.getText()));

                ps.setString(2, ModifyTitleField.getText());
                ps.setString(3, ModifyDescriptionField.getText());
                ps.setString(4, ModifyLocationField.getText());
                ps.setString(5, ModifyTypeField.getText());

                ps.setString(6, universalStart);
                ps.setString(7, universalEnd);
                ps.setTimestamp(8, Timestamp.valueOf(LocalDateTime.now()));

                ps.setString(9, "admin");

                ps.setTimestamp(10, Timestamp.valueOf(LocalDateTime.now()));

                ps.setInt(10, Integer.parseInt(ModifyCustomerField.getText()));
                ps.setInt(11, Integer.parseInt(accessContacts.getContactID(ModifyContactIDCombo.getValue())));
                ps.setInt(12, Integer.parseInt(accessContacts.getContactID(ModifyUserField.getText())));
                ps.setInt(13, Integer.parseInt(ModifyApptID.getText()));
                ps.execute();
            }

            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("/view/main-screen.fxml"));
            stage.setScene(new Scene(scene));
            stage.show();
        } catch (SQLException | IOException sql) {
            sql.printStackTrace();
        }

    }
    /**
     *
     *Sends contact names and available appointment times to
     * their respective combo boxes
     *
     */

    @FXML
    public void initialize() throws SQLException {

        DatabaseConnect.getConnection();
        //retrieves selected appts information from main table view
        selectedAppt = MainScreen.getApptforMod();
        ObservableList<Contacts> allContacts = accessContacts.getAllContacts();
        ObservableList<String> contactNames = FXCollections.observableArrayList();
        String contactName = "";
        //for loop that retrieves contact names from observable list
        for (Contacts contacts : allContacts) {
            contactNames.add(contacts.getContactName());
        }
        //sets contact information to combobox
        ModifyContactIDCombo.setItems(contactNames);
        //for loop that pairs contact ID of selected appointment to contact name
        for (Contacts contact : allContacts) {
            if (selectedAppt.getContactID() == contact.getContactID()) {
                contactName = contact.getContactName();
            }
            //sets values to the tables columns
            ModifyApptID.setText(valueOf(selectedAppt.getApptID()));
            ModifyTitleField.setText(selectedAppt.getApptTitle());
            ModifyDescriptionField.setText(selectedAppt.getApptDescription());
            ModifyLocationField.setText(selectedAppt.getApptLocation());
            ModifyTypeField.setText(selectedAppt.getApptType());
            ModifyCustomerField.setText(valueOf(selectedAppt.getCustomerID()));
            ModifyStartDate.setValue(selectedAppt.getStartDateTime().toLocalDate());
            ModifyEndDate.setValue(selectedAppt.getEndDateTime().toLocalDate());
            ModifyStartCombo.setValue(String.valueOf(selectedAppt.getStartDateTime().toLocalTime()));
            ModifyEndCombo.setValue(String.valueOf(selectedAppt.getEndDateTime().toLocalTime()));
            ModifyUserField.setText(valueOf(selectedAppt.getUserID()));
            ModifyContactIDCombo.setValue(contactName);

            ObservableList<String> apptTimes2 = FXCollections.observableArrayList();
            //sets starting and end points for available appointment times combo box
            LocalTime apptMIN = LocalTime.MIN.plusHours(8);
            LocalTime apptMAX = LocalTime.MAX.minusHours(1).minusMinutes(45);
            if (!apptMIN.equals(0) || !apptMAX.equals(0)) {
                while (apptMIN.isBefore(apptMAX)) {
                    apptTimes2.add(String.valueOf(apptMIN));
                    apptMIN = apptMIN.plusMinutes(15);
                }
            }

            //applies appointment times to their respective combo boxes
           ModifyStartCombo.setItems(apptTimes2);
            ModifyEndCombo.setItems(apptTimes2);


        }
    }
}