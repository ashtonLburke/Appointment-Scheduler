package controller;

import accessObjects.accessAppts;
import accessObjects.accessUsers;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;

import java.time.ZoneId;
import java.util.MissingResourceException;
import java.util.Optional;
import java.util.ResourceBundle;
import java.io.IOException;
import javafx.stage.Stage;
import model.Appts;

import java.sql.SQLException;
import java.net.URL;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Locale;
import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 *
 *
 *
 */
public class LoginScreen implements Initializable {

    Stage stage;
    Parent scene;

    @FXML
    private TextField PasswordField;

    @FXML
    private TextField UsernameField;

    @FXML
    private Button exitButton;

    @FXML
    private Button loginButton;

    @FXML
    private Label passwordLabel;

    @FXML
    private Button resetButton;

    @FXML
    private Label timezoneLabel;

    @FXML
    private Label usernameLabel;

    @FXML
    private Label zoneID;

    /**
     *
     * Exit button action event that closes the program when clicked
     * Includes confirmation message
     *
     */
    @FXML
    void onActionExit(ActionEvent event) {
        //Shows confirmation message before running system.exit
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Alert");
        alert.setContentText("Exiting the program! Are you sure?");
        Optional<ButtonType> result = alert.showAndWait();
        //If OK button is pressed then runs System.exit
        if (result.isPresent() && result.get() == ButtonType.OK) {
            System.exit(0);
        }
    }


    /**
     *
     *  Login action event
     *  Checks input username and password against user information stored in database
     *  Uses a resource bundle to manage languages
     *
     *  Creates a text log of all attempted logins
     */
    @FXML
   private void onActionLogin(ActionEvent event) throws IOException {
        try {
           ObservableList<Appts> getAllAppts = accessAppts.getAllAppts();
            LocalDateTime fifteenPrior = LocalDateTime.now().minusMinutes(15);
            LocalDateTime fifteenAfter = LocalDateTime.now().plusMinutes(15);
            LocalDateTime currentTime;
            int getApptID = 0;
            LocalDateTime showTime = null;
            boolean apptInFifteen = false;
            //holds translation information
            ResourceBundle resourceBundle = ResourceBundle.getBundle("lang_resourcebundle/resourcebundle", Locale.getDefault());

            //gets inputed information
            String inputUsername = UsernameField.getText();
            String inputPassword = PasswordField.getText();
            int userid = accessUsers.userAuthenticate(inputUsername, inputPassword);
            //writes log for attempted logins
            FileWriter fileWriter = new FileWriter("login_activity.txt", true);
            PrintWriter printFile = new PrintWriter(fileWriter);
            //if login inputs matches that of a user in the database then sends user to main screen
            if (userid > 0) {

                stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                scene = FXMLLoader.load(getClass().getResource("/view/main-screen.fxml"));
                stage.setScene(new Scene(scene));
                stage.show();
                    //sets format for log file
                printFile.print("user: " + inputUsername + " successful login. Timestamp: " + Timestamp.valueOf(LocalDateTime.now()) + "\n");
                //for loop that checks if user has any upcoming appointment information
                for (Appts appts : getAllAppts) {
                    currentTime = appts.getStartDateTime();
                    if ((currentTime.isEqual(fifteenPrior) || currentTime.isAfter(fifteenPrior)) &&
                            (currentTime.isEqual(fifteenAfter) || (currentTime.isBefore(fifteenAfter)))) {
                        getApptID = appts.getApptID();
                        showTime = currentTime;
                        apptInFifteen = true;
                    }

                }
                //if appointment is in 15min then shows confirmation message with included upcoming appointment information
                if (apptInFifteen == true) {

                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Upcoming appointment" + getApptID + "starts at: " + showTime);
                    Optional<ButtonType> alertConfirm = alert.showAndWait();
                } else {

                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "No upcoming appointments found.");
                    Optional<ButtonType> alertConfirm = alert.showAndWait();
                }
                // if inputed user information does not match any in the database then stops login and presents error message
            } else if (userid < 0) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle(resourceBundle.getString("ErrorMessage"));
                alert.setContentText(resourceBundle.getString("IncorrectInput"));
                alert.show();
                    //records attempted login to log file
                printFile.print("Attempted login by: " + inputUsername + " at: " + Timestamp.valueOf(LocalDateTime.now()) + "\n");

            }
            printFile.close();
        }
    catch(IOException | SQLException ioe){
        ioe.printStackTrace();
    }
    }


    /**
     *
     * Action event for reset button that resets text fields to null
     *
     */
        @FXML
        void onActionReset(ActionEvent event) {
        UsernameField.setText("");
        PasswordField.setText("");


        }
    /**
     * Reads local time zone information and prints to login screen
     * Initializes resource bundle for translations
     *
     */
        @Override
        public void initialize(URL url, ResourceBundle resourceBundle) {


            try
            {

                Locale locale = Locale.getDefault();
                Locale.setDefault(locale);
                ZoneId zoneid = ZoneId.systemDefault();
                zoneID.setText(String.valueOf(zoneid));

                resourceBundle = ResourceBundle.getBundle("lang_resourcebundle/resourcebundle", Locale.getDefault());
                usernameLabel.setText(resourceBundle.getString("username.text"));
                passwordLabel.setText(resourceBundle.getString("password.text"));
                loginButton.setText(resourceBundle.getString("login.button"));
                resetButton.setText(resourceBundle.getString("reset.button"));
                timezoneLabel.setText(resourceBundle.getString("timezone.text"));
                exitButton.setText(resourceBundle.getString("exit.button"));
                timezoneLabel.setText(resourceBundle.getString("Locale"));


            } catch(MissingResourceException e) {
                System.out.println("Resource file missing: " + e);
            } catch (Exception e)
            {
                System.out.println(e);
            }




        }
}




