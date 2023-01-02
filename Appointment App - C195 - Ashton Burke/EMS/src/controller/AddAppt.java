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

public class AddAppt {

    Stage stage;
    Parent scene;

    @FXML
    private ComboBox<String> AddContactIDCombo;

    @FXML
    private TextField AddCustomerID;

    @FXML
    private TextField AddDescriptionField;

    @FXML
    private DatePicker AddEndDate;

    @FXML
    private TextField AddLocationField;

    @FXML
    private DatePicker AddStartDate;

    @FXML
    private ComboBox<String> pickEndTime;

    @FXML
    private ComboBox<String> pickStartTime;

    @FXML
    private TextField AddTitleField;

    @FXML
    private TextField AddTypeField;

    @FXML
    private TextField AddUserID;

    /**
     *
     * ActionEvent for cancel button
     * Includes confirmation alert
     */
    @FXML
    void onActionCancelAddAppt(ActionEvent event) throws IOException {
//sets alert type to Confirmation
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "You will lose unsaved values. Do you want to continue?");
        //waits for user input before continuing
        Optional<ButtonType> result = alert.showAndWait();
        //if OK button is pressed then executes code block
        if(result.isPresent() && result.get() == ButtonType.OK)
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
     * Saves all appointment information to the appointment table in the database
     *
     */
    @FXML
    void onActionSaveAddAppt(ActionEvent event) throws IOException {
try {
    Connection connection = DatabaseConnect.getConnection();
    // if all text fields are filled, adds appointment information
    if (!AddTitleField.getText().isEmpty() && !AddDescriptionField.getText().isEmpty()
            && !AddDescriptionField.getText().isEmpty() && !AddTypeField.getText().isEmpty() &&
            AddStartDate.getValue() != null && AddEndDate.getValue() != null && !pickStartTime.getValue().isEmpty()
            && !pickEndTime.getValue().isEmpty() && !AddCustomerID.getText().isEmpty()) {
        //creates observable lists for all needed appointment information
        ObservableList<Customers> getCusts = accessCustomers.getAllCusts(connection);
        ObservableList<Integer> custIDList = FXCollections.observableArrayList();
        ObservableList<accessUsers> getUsers = accessUsers.getUsers();
        ObservableList<Integer> userIDList = FXCollections.observableArrayList();
        ObservableList<Appts> getAppts = accessAppts.getAllAppts();

        //function mapper that returns stream informaiton of customerID list
        getCusts.stream().map(Customers::getCustomerID).forEach(custIDList::add);
        //function mapper that returns stream informaiton of customerID list
        getUsers.stream().map(Users::getUserID).forEach(userIDList::add);

        //getter for local start and end dates
        LocalDate localAddEnd = AddEndDate.getValue();
        LocalDate localAddStart = AddStartDate.getValue();
        //sets format for time
        DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("HH:mm");
        //sets format for date
        String apptStartDate = AddStartDate.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String apptStartTime = pickStartTime.getValue();

        String endDate = AddEndDate.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String endTime = pickEndTime.getValue();

        //universal time conversion method
        String universalStart = timeDateConversion(apptStartDate + " " + apptStartTime + ":00");
        String universalEnd = timeDateConversion(endDate + " " + endTime + ":00");
        //gets selected appointment times
        LocalTime localTimeStart = LocalTime.parse(pickStartTime.getValue(), timeFormat);
        LocalTime LocalTimeEnd = LocalTime.parse(pickEndTime.getValue(), timeFormat);
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
        int newApptID = Integer.parseInt(String.valueOf((int) (Math.random() * 50)));
        int customerID = Integer.parseInt(AddCustomerID.getText());
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
            //SQL statement that sends appointment information to the database
        PreparedStatement ps = connection.prepareStatement("INSERT INTO appointments (Appointment_ID, Title, Description, Location, Type, Start, End, Create_Date, Created_By, Last_Update, Last_Updated_By, Customer_ID, User_ID, Contact_ID) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
            //sends each respective set of information to their columns in the appointments table
        ps.setInt(1, newApptID);

        ps.setString(2, AddTitleField.getText());
        ps.setString(3, AddDescriptionField.getText());
        ps.setString(4, AddLocationField.getText());
        ps.setString(5, AddTypeField.getText());

        ps.setTimestamp(6, Timestamp.valueOf(universalStart));
        ps.setTimestamp(7, Timestamp.valueOf(universalEnd));
        ps.setTimestamp(8, Timestamp.valueOf(LocalDateTime.now()));

        ps.setString(9, "admin");

        ps.setTimestamp(10, Timestamp.valueOf(LocalDateTime.now()));

        ps.setInt(11, 1);
        ps.setInt(12, Integer.parseInt(AddCustomerID.getText()));
        ps.setInt(13, Integer.parseInt(accessContacts.getContactID(AddContactIDCombo.getValue())));
        ps.setInt(14, Integer.parseInt(accessContacts.getContactID(AddUserID.getText())));

        ps.execute();
    }

    Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
    scene = FXMLLoader.load(getClass().getResource("/view/main-screen.fxml"));
    stage.setScene(new Scene(scene));
    stage.show();
}
catch (SQLException sql){
    sql.printStackTrace();
}

    }
    /**
     * Sends contact names and available appointment times to
     * their respective combo boxes
     *
     */
    @FXML
    public void initialize() throws SQLException {

        ObservableList<Contacts> allContacts = accessContacts.getAllContacts();
        ObservableList<String> contactNames = FXCollections.observableArrayList();

        for (Contacts contacts : allContacts) {
            contactNames.add(contacts.getContactName());
        }

        ObservableList<String> apptTimes = FXCollections.observableArrayList();
        //sets starting and end points for available appointment times combo box
        LocalTime apptMIN = LocalTime.MIN.plusHours(8);
        LocalTime apptMAX = LocalTime.MAX.minusHours(1).minusMinutes(45);
        //sets available appointment times in increments of 15 minutes
        if (!apptMIN.equals(0) || !apptMAX.equals(0)) {
            while (apptMIN.isBefore(apptMAX)) {
                apptTimes.add(String.valueOf(apptMIN));
                apptMIN = apptMIN.plusMinutes(15);
            }
        }
        //applies contact and appointment times to their respective combo boxes
        pickStartTime.setItems(apptTimes);
        pickEndTime.setItems(apptTimes);
        AddContactIDCombo.setItems(contactNames);

    }
    }











