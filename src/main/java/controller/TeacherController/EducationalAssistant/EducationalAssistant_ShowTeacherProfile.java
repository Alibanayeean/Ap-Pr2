package controller.TeacherController.EducationalAssistant;

import back.persons.Teacher;
import controller.publicController.LoginPage;
import controller.publicMethods.PublicMethods;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import org.apache.log4j.Logger;
import org.apache.log4j.LogManager;import clientNetwork.ClientNetwork;
import config.ConfigIdentifier;
import config.ReadPropertyFile;

public class EducationalAssistant_ShowTeacherProfile implements Initializable{
    public static final Logger log = LogManager.getLogger(EducationalAssistant_ShowTeacherProfile.class);

    Teacher teacher;
    ClientNetwork clientNetwork;

    public static int index = 0;


    @FXML
    javafx.scene.control.MenuBar MenuBar;

    @FXML
    ColorPicker colorPicker;

    @FXML
    Button LogOutButton;

    @FXML
    Label timeShowLabel;

    @FXML
    Button HomeButton;

    @FXML
    Label FirstNameLabel;

    @FXML
    Label LastNameLabel;

    @FXML
    Label IdentityCodeLabel;

    @FXML
    Label IDLabel;

    @FXML
    Label RoomNumberLabel;

    @FXML
    Label CollegeLabel;

    @FXML
    Label TeacherDegreeLabel;

    @FXML
    TextField PhoneNumberTextField;

    @FXML
    TextField EmailTextField;

    @FXML
    Button EditPhoneNumber;

    @FXML
    Button EditEmail;

    @FXML
    ImageView imageViewUser;

    Timeline timelineForConnected;

    @FXML
    Button reconnectionButton;

    @FXML
    ImageView connectionStatus;

    int numClickedEditPhoneNumber = 0;
    int numClickedEditEmail = 0;



    Stage stage;
    @FXML
    void ChangingColor(ActionEvent event) {
        Color color = colorPicker.getValue();
        MenuBar.setBackground(new Background(new BackgroundFill(color, null, null)));
        timeShowLabel.setBackground(new Background(new BackgroundFill(color, null, null)));
        HomeButton.setBackground(new Background(new BackgroundFill(color, null, null)));
        EditEmail.setBackground(new Background(new BackgroundFill(color, null, null)));
        EditPhoneNumber.setBackground(new Background(new BackgroundFill(color, null, null)));
        teacher.color = colorPicker.getValue();
        String s = "#" + Integer.toHexString(color.hashCode());
        clientNetwork.changeColorForTeacher(teacher.getUsername(), s);
        log.info("change background");
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        PublicMethods.initImageOfLogOutButton(LogOutButton);

        PublicMethods.initImageOfHomeButton(HomeButton);

        PublicMethods.initClock(timeShowLabel);

        PhoneNumberTextField.setDisable(true);
        EmailTextField.setDisable(true);
    }

    public void setTeacher(Teacher teacher, ClientNetwork clientNetwork){
        this.clientNetwork = clientNetwork;

        this.teacher = teacher;



        PublicMethods.initImageOfPerson(clientNetwork, imageViewUser, teacher.getUsername(), teacher.getImage());


        Color color = teacher.color;
        MenuBar.setBackground(new Background(new BackgroundFill(color, null, null)));
        timeShowLabel.setBackground(new Background(new BackgroundFill(color, null, null)));
        HomeButton.setBackground(new Background(new BackgroundFill(color, null, null)));
        EditEmail.setBackground(new Background(new BackgroundFill(color, null, null)));
        EditPhoneNumber.setBackground(new Background(new BackgroundFill(color, null, null)));
        colorPicker.setValue(color);

        FirstNameLabel.setText(teacher.getFirstname());
        LastNameLabel.setText(teacher.getLastname());
        IdentityCodeLabel.setText(teacher.getCodeMelli());
        IDLabel.setText(teacher.getId() + "");
        PhoneNumberTextField.setText(teacher.getPhoneNumber());
        EmailTextField.setText(teacher.getEmail());
        CollegeLabel.setText(teacher.getCollege() + "");
        RoomNumberLabel.setText(teacher.getRoomNumber() + "");
        TeacherDegreeLabel.setText(teacher.getTeacherDegree() + "");

        timelineForConnected = PublicMethods.reconnectionTimeline(reconnectionButton, clientNetwork, connectionStatus);


    }

    public void EditPhoneNumberFunction(ActionEvent e) {
        if(numClickedEditPhoneNumber % 2 == 0){
            PhoneNumberTextField.setDisable(false);
            EditPhoneNumber.setText("OK");
            numClickedEditPhoneNumber++;
        }
        else{
            for (int i = 0; i < PhoneNumberTextField.getText().length(); i++) {
                if(!(PhoneNumberTextField.getText().charAt(i) >= '0' && PhoneNumberTextField.getText().charAt(i) <= '9')){
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Incorrect");
                    alert.setHeaderText("Incorrect phone number input");
                    log.error("incorrect phone number input");

                    alert.showAndWait();
                    return;
                }
            }
            EditPhoneNumber.setText("Edit");
            PhoneNumberTextField.setDisable(true);

            teacher.setPhoneNumber(EditPhoneNumber.getText());
            clientNetwork.editPhoneForATeacher(teacher.getId(), EditPhoneNumber.getText());

            log.info("phone number edited");

            numClickedEditPhoneNumber++;
        }

    }

    public void EditEmailFunction(ActionEvent e) {
        if(numClickedEditEmail % 2 == 0){
            EmailTextField.setDisable(false);
            EditEmail.setText("OK");
            numClickedEditEmail++;
        }
        else{
            String email = EmailTextField.getText();
            boolean b1 = false;
            boolean b2 = false;
            for (int i = 0; i < email.length(); i++) {
                if(email.charAt(i) == '@'){
                    b1 = true;
                }
                if(b1 & email.charAt(i) == '.'){
                    b2 = true;
                    break;
                }
            }
            if(!(b1 & b2)){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Incorrect");
                alert.setHeaderText("Invalid email format");
                alert.showAndWait();

                log.error("Invalid email format");

                return;
            }

            log.info("email edited");
            clientNetwork.editEmailForATeacher(teacher.getId(), EmailTextField.getText());
            teacher.setEmail(EmailTextField.getText());


            EditEmail.setText("Edit");
            EmailTextField.setDisable(true);

            numClickedEditEmail++;

        }
    }

    public void OpenButtonFunction(ActionEvent event) {
        stage = ((Stage)(((Scene)EditEmail.getScene()).getWindow()));
        FileChooser fil_chooser = new FileChooser();

        fil_chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.PNG"));
        File file = fil_chooser.showOpenDialog(stage);

        if (file != null) {
            String imageAddress = teacher.getImage().substring(0, teacher.getImage().length() - 4) + "" + 0 + ".png";
            clientNetwork.setImageAddressOfATeacher(teacher.getId(), imageAddress);
            teacher.setImage(imageAddress);

            File file1 = new File(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.publicURL) + imageAddress);

            try {
                Files.copy(Paths.get(file.getPath()), Paths.get(file1.getPath()));
            } catch (IOException e) {
                e.printStackTrace();
            }


            log.info("image changed");


            setTeacher(teacher, clientNetwork);
        }
    }


    public void HomeButtonFunction(ActionEvent e) {
        timelineForConnected.stop();
        try{
            stage = ((Stage)(((Scene)timeShowLabel.getScene()).getWindow()));
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Fxml\\Teacher\\EducationalAssistant\\EducationalAssistantFirstPage.fxml"));
            Parent root = loader.load();
            EducationalAssistantFirstPage EducationalAssistantFirstPage = loader.getController();
            EducationalAssistantFirstPage.setTeacher(teacher, clientNetwork);
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();


        }
        catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    public void LogOutFunction(ActionEvent event) {
        timelineForConnected.stop();
        try{
            stage = ((Stage)(((Scene)timeShowLabel.getScene()).getWindow()));
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Fxml\\Public\\LoginPage.fxml"));
            Parent root = loader.load();
            LoginPage LoginPage = loader.getController();
            LoginPage.setClientNetwork(clientNetwork);
            LoginPage.setImageView();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

            LocalDateTime localDateTime = LocalDateTime.now();
            clientNetwork.changeLastLoginTeacher(teacher.getId(), localDateTime.getSecond(), localDateTime.getMinute(), localDateTime.getHour(), localDateTime.getDayOfMonth(), localDateTime.getMonthValue(), localDateTime.getYear());
        }
        catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    public void CoursesListMenuFunction(ActionEvent event) {
        timelineForConnected.stop();
        try{
            stage = ((Stage)(((Scene)timeShowLabel.getScene()).getWindow()));
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Fxml\\Teacher\\EducationalAssistant\\EducationalAssistant_ShowCourseList.fxml"));
            Parent root = loader.load();
            EducationalAssistant_ShowCourseList EducationalAssistant_ShowCourseList = loader.getController();
            EducationalAssistant_ShowCourseList.setTeacher(teacher, clientNetwork);
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
        catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    public void TeacherListMenuFunction(ActionEvent event) {
        timelineForConnected.stop();
        try{
            stage = ((Stage)(((Scene)timeShowLabel.getScene()).getWindow()));
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Fxml\\Teacher\\EducationalAssistant\\EducationalAssistant_ShowTeacherListMenu.fxml"));
            Parent root = loader.load();
            EducationalAssistant_ShowTeacherListMenu EducationalAssistant_ShowTeacherListMenu = loader.getController();
            EducationalAssistant_ShowTeacherListMenu.setTeacher(teacher, clientNetwork);
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
        catch (IOException ioException) {
            ioException.printStackTrace();
        }

    }

    public void ScheduleFunction(ActionEvent event) {
        timelineForConnected.stop();
        try{
            stage = ((Stage)(((Scene)timeShowLabel.getScene()).getWindow()));
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Fxml\\Teacher\\EducationalAssistant\\EducationalAssistant_ShowWeeklySchedule.fxml"));
            Parent root = loader.load();
            EducationalAssistant_ShowWeeklySchedule EducationalAssistant_ShowWeeklySchedule = loader.getController();
            EducationalAssistant_ShowWeeklySchedule.setTeacher(teacher, clientNetwork);
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
        catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    public void ExamFunction(ActionEvent event) {
        timelineForConnected.stop();
        try{
            stage = ((Stage)(((Scene)timeShowLabel.getScene()).getWindow()));
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Fxml\\Teacher\\EducationalAssistant\\EducationalAssistant_ShowExams.fxml"));
            Parent root = loader.load();
            EducationalAssistant_ShowExams EducationalAssistant_ShowExams = loader.getController();
            EducationalAssistant_ShowExams.setTeacher(teacher, clientNetwork);
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
        catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    public void MinorFunction(ActionEvent event) {
        timelineForConnected.stop();
        try{
            stage = ((Stage)(((Scene)timeShowLabel.getScene()).getWindow()));
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Fxml\\Teacher\\EducationalAssistant\\EducationalAssistant_Minor.fxml"));
            Parent root = loader.load();
            EducationalAssistant_Minor EducationalAssistant_Minor = loader.getController();
            EducationalAssistant_Minor.setTeacher(teacher, clientNetwork);
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
        catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    public void WithdrawalFromEducationFunction(ActionEvent event) {
        timelineForConnected.stop();
        try{
            stage = ((Stage)(((Scene)timeShowLabel.getScene()).getWindow()));
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Fxml\\Teacher\\EducationalAssistant\\EducationalAssistant_WithdrawalFromEducation.fxml"));
            Parent root = loader.load();
            EducationalAssistant_WithdrawalFromEducation EducationalAssistant_WithdrawalFromEducation = loader.getController();
            EducationalAssistant_WithdrawalFromEducation.setTeacher(teacher, clientNetwork);
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
        catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    public void RecommendationFunction(ActionEvent event) {
        timelineForConnected.stop();
        try{
            stage = ((Stage)(((Scene)timeShowLabel.getScene()).getWindow()));
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Fxml\\Teacher\\EducationalAssistant\\EducationalAssistant_GiveRecome.fxml"));
            Parent root = loader.load();
            EducationalAssistant_GiveRecome EducationalAssistant_GiveRecome = loader.getController();
            EducationalAssistant_GiveRecome.setTeacher(teacher, clientNetwork);
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
        catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    public void temporaryScoresFunction(ActionEvent event) {
        timelineForConnected.stop();
        try{
            stage = ((Stage)(((Scene)timeShowLabel.getScene()).getWindow()));
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Fxml\\Teacher\\EducationalAssistant\\EducationalAssistant_temporaryScores.fxml"));
            Parent root = loader.load();
            EducationalAssistant_temporaryScores EducationalAssistant_temporaryScores = loader.getController();
            EducationalAssistant_temporaryScores.setTeacher(teacher, clientNetwork);
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
        catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    public void StatusFunction(ActionEvent event) {
        timelineForConnected.stop();
        try{
            stage = ((Stage)(((Scene)timeShowLabel.getScene()).getWindow()));
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Fxml\\Teacher\\EducationalAssistant\\EducationalAssistant_StudentsStatus.fxml"));
            Parent root = loader.load();
            EducationalAssistant_StudentsStatus EducationalAssistant_StudentsStatus = loader.getController();
            EducationalAssistant_StudentsStatus.setTeacher(teacher, clientNetwork);
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
        catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    public void ProfileFunction(ActionEvent event) {
        timelineForConnected.stop();
        try{
            stage = ((Stage)(((Scene)timeShowLabel.getScene()).getWindow()));
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Fxml\\Teacher\\EducationalAssistant\\EducationalAssistant_ShowTeacherProfile.fxml"));
            Parent root = loader.load();
            EducationalAssistant_ShowTeacherProfile EducationalAssistant_ShowTeacherProfile = loader.getController();
            EducationalAssistant_ShowTeacherProfile.setTeacher(teacher, clientNetwork);
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
        catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    public void AddNewUserFunction(ActionEvent event) {
        timelineForConnected.stop();
        try{
            stage = ((Stage)(((Scene)timeShowLabel.getScene()).getWindow()));
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Fxml\\Teacher\\EducationalAssistant\\EducationalAssistant_AddNewUser.fxml"));
            Parent root = loader.load();
            EducationalAssistant_AddNewUser EducationalAssistant_AddNewUser = loader.getController();
            EducationalAssistant_AddNewUser.setTeacher(teacher, clientNetwork);
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
        catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    public void temporaryScoresAllStudentsFunction(ActionEvent event) {
        timelineForConnected.stop();
        try{
            stage = ((Stage)(((Scene)timeShowLabel.getScene()).getWindow()));
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Fxml\\Teacher\\EducationalAssistant\\EducationalAssistant_temporaryScoresForAllStudents.fxml"));
            Parent root = loader.load();
            EducationalAssistant_temporaryScoresForAllStudents EducationalAssistant_temporaryScoresForAllStudents = loader.getController();
            EducationalAssistant_temporaryScoresForAllStudents.setTeacher(teacher, clientNetwork);
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
        catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }


}
