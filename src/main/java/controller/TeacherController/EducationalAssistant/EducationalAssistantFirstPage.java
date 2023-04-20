package controller.TeacherController.EducationalAssistant;

import back.persons.Teacher;
import controller.StudentController.BC.BC_Messages;
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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;


import org.apache.log4j.Logger;
import org.apache.log4j.LogManager;import clientNetwork.ClientNetwork;
import config.ConfigIdentifier;
import config.ReadPropertyFile;

public class EducationalAssistantFirstPage implements Initializable {

    public static final Logger log = LogManager.getLogger(EducationalAssistantFirstPage.class);


    Teacher teacher;
    ClientNetwork clientNetwork;



    @FXML
    javafx.scene.control.MenuBar MenuBar;

    @FXML
    ColorPicker colorPicker;

    @FXML
    Button LogOutButton;

    @FXML
    Pane paneFilter;

    @FXML
    Label timeShowLabel;

    @FXML
    ImageView imageViewUser;

    @FXML
    Label Status;

    @FXML
    Label Firstname;

    @FXML
    Label Lastname;

    @FXML
    Label emailLabel;

    @FXML
    Button HomeButton;

    @FXML
    Label lastLoginLabel;

    Timeline timelineForConnected;

    @FXML
    Button reconnectionButton;

    @FXML
    ImageView connectionStatus;

    @FXML
    Button messageButton;

    Stage stage;

    @FXML
    void ChangingColor(ActionEvent event) {
        Color color = colorPicker.getValue();
        paneFilter.setBackground(new Background(new BackgroundFill(color, null, null)));
        MenuBar.setBackground(new Background(new BackgroundFill(color, null, null)));
        timeShowLabel.setBackground(new Background(new BackgroundFill(color, null, null)));
        HomeButton.setBackground(new Background(new BackgroundFill(color, null, null)));
        messageButton.setBackground(new Background(new BackgroundFill(color, null, null)));
        teacher.color = colorPicker.getValue();
        String s = "#" + Integer.toHexString(color.hashCode());
        clientNetwork.changeColorForTeacher(teacher.getUsername(), s);
        log.info("change background");

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        PublicMethods.initImageOfLogOutButton(LogOutButton);

        PublicMethods.initImageOfHomeButton(HomeButton);

        PublicMethods.initImageOfMessageButton(messageButton);

        PublicMethods.initClock(timeShowLabel);
    }

    public void setTeacher(Teacher teacher, ClientNetwork clientNetwork){
        this.clientNetwork = clientNetwork;
        this.teacher = teacher;

        PublicMethods.initImageOfPerson(clientNetwork, imageViewUser, teacher.getUsername(), teacher.getImage());

        Color color = teacher.color;
        paneFilter.setBackground(new Background(new BackgroundFill(color, null, null)));
        MenuBar.setBackground(new Background(new BackgroundFill(color, null, null)));
        timeShowLabel.setBackground(new Background(new BackgroundFill(color, null, null)));
        HomeButton.setBackground(new Background(new BackgroundFill(color, null, null)));
        messageButton.setBackground(new Background(new BackgroundFill(color, null, null)));

        colorPicker.setValue(color);


        Status.setText("Teacher: " + teacher.getTeacherDegree());
        Firstname.setText("First name: " + teacher.getFirstname());
        Lastname.setText("Last name: " + teacher.getLastname());
        emailLabel.setText("Email: " + teacher.getEmail());

        if(teacher.getTimesLogin() == 1){
            lastLoginLabel.setText("Last login:");
        }
        else{
            lastLoginLabel.setText("Last login:\n" + teacher.getLastYearLogin() + "/" + teacher.getLastMonthLogin() + "/" + teacher.getLastDayLogin() + '\n' + teacher.getLastHourLogin() + ":" + teacher.getLastMinuteLogin() + ":" + teacher.getLastSecondLogin());
        }

        timelineForConnected = PublicMethods.reconnectionTimeline(reconnectionButton, clientNetwork, connectionStatus);

    }


    public void messageButtonFunction(ActionEvent event) {
        timelineForConnected.stop();
        try{
            stage = ((Stage)(((Scene)emailLabel.getScene()).getWindow()));
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Fxml\\Teacher\\EducationalAssistant\\EducationalAssistant_Messages.fxml"));
            Parent root = loader.load();
            EducationalAssistant_Messages EducationalAssistant_Messages = loader.getController();
            EducationalAssistant_Messages.setTeacher(teacher, clientNetwork);
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

    public void courseSelection(ActionEvent event) {
        timelineForConnected.stop();
        try{
            stage = ((Stage)(((Scene)timeShowLabel.getScene()).getWindow()));
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Fxml\\Teacher\\EducationalAssistant\\EducationalAssistant_SelectionUnit.fxml"));
            Parent root = loader.load();
            EducationalAssistant_SelectionUnit EducationalAssistant_SelectionUnit = loader.getController();
            EducationalAssistant_SelectionUnit.setTeacher(teacher, clientNetwork);
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
        catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }



}
