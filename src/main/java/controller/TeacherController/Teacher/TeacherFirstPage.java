package controller.TeacherController.Teacher;

import back.persons.Teacher;
import controller.TeacherController.CW.Teacher_CW;
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
import javafx.scene.layout.GridPane;
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

public class TeacherFirstPage implements Initializable {
    public static final Logger log = LogManager.getLogger(TeacherFirstPage.class);


    Teacher teacher;
    ClientNetwork clientNetwork;



    @FXML
    javafx.scene.control.MenuBar MenuBar;

    @FXML
    ColorPicker colorPicker;

    @FXML
    Button LogOutButton;

    @FXML
    GridPane gridPane;

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

    Stage stage;
    @FXML
    void ChangingColor(ActionEvent event) {
        Color color = colorPicker.getValue();
        paneFilter.setBackground(new Background(new BackgroundFill(color, null, null)));
        MenuBar.setBackground(new Background(new BackgroundFill(color, null, null)));
        timeShowLabel.setBackground(new Background(new BackgroundFill(color, null, null)));
        HomeButton.setBackground(new Background(new BackgroundFill(color, null, null)));
        teacher.color = colorPicker.getValue();
        String s = "#" + Integer.toHexString(color.hashCode());
        clientNetwork.changeColorForTeacher(teacher.getUsername(), s);

        colorPicker.setValue(color);
        log.info("change background");

        

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        PublicMethods.initImageOfLogOutButton(LogOutButton);

        PublicMethods.initImageOfHomeButton(HomeButton);

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

    public void cwFunction(ActionEvent event) {
        timelineForConnected.stop();
        try{
            stage = ((Stage)(((Scene)emailLabel.getScene()).getWindow()));
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Fxml\\Teacher\\CW\\Teacher_CW.fxml"));
            Parent root = loader.load();
            Teacher_CW Teacher_CW = loader.getController();
            Teacher_CW.setTeacher(teacher, clientNetwork);
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
        catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    public void CoursesListMenuFunction(ActionEvent event) {
        timelineForConnected.stop();
        try{
            stage = ((Stage)(((Scene)timeShowLabel.getScene()).getWindow()));
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Fxml\\Teacher\\Teacher\\Teacher_ShowCourseListMenu.fxml"));
            Parent root = loader.load();
            Teacher_ShowCourseListMenu Teacher_ShowCourseListMenu = loader.getController();
            Teacher_ShowCourseListMenu.setTeacher(teacher, clientNetwork);
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
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Fxml\\Teacher\\Teacher\\Teacher_ShowTeacherListMenu.fxml"));
            Parent root = loader.load();
            Teacher_ShowTeacherListMenu Teacher_ShowTeacherListMenu = loader.getController();
            Teacher_ShowTeacherListMenu.setTeacher(teacher, clientNetwork);
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
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Fxml\\Teacher\\Teacher\\Teacher_ShowWeeklySchedule.fxml"));
            Parent root = loader.load();
            Teacher_ShowWeeklySchedule Teacher_ShowWeeklySchedule = loader.getController();
            Teacher_ShowWeeklySchedule.setTeacher(teacher, clientNetwork);
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
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Fxml\\Teacher\\Teacher\\Teacher_ShowExams.fxml"));
            Parent root = loader.load();
            Teacher_ShowExams Teacher_ShowExams = loader.getController();
            Teacher_ShowExams.setTeacher(teacher, clientNetwork);
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
        catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    public void RecomeFunction(ActionEvent event) {
        timelineForConnected.stop();
        try{
            stage = ((Stage)(((Scene)timeShowLabel.getScene()).getWindow()));
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Fxml\\Teacher\\Teacher\\Teacher_GiveRecome.fxml"));
            Parent root = loader.load();
            Teacher_GiveRecome Teacher_GiveRecome = loader.getController();
            Teacher_GiveRecome.setTeacher(teacher, clientNetwork);
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
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Fxml\\Teacher\\Teacher\\Teacher_temporaryScores.fxml"));
            Parent root = loader.load();
            Teacher_temporaryScores Teacher_temporaryScores = loader.getController();
            Teacher_temporaryScores.setTeacher(teacher, clientNetwork);
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
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Fxml\\Teacher\\Teacher\\Teacher_ShowTeacherProfile.fxml"));
            Parent root = loader.load();
            Teacher_ShowTeacherProfile Teacher_ShowTeacherProfile = loader.getController();
            Teacher_ShowTeacherProfile.setTeacher(teacher, clientNetwork);
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
        catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    public void chatRoomFunction(ActionEvent event) {
        timelineForConnected.stop();
        try{
            stage = ((Stage)(((Scene)timeShowLabel.getScene()).getWindow()));
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Fxml\\Teacher\\Teacher\\Teacher_ChatRoom.fxml"));
            Parent root = loader.load();
            Teacher_ChatRoom Teacher_ChatRoom = loader.getController();
            Teacher_ChatRoom.setTeacher(teacher, clientNetwork);
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
        catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

}
