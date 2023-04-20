package controller.Mohseni;


import back.persons.Mohseni;
import back.persons.Student;
import clientNetwork.ClientNetwork;
import config.ConfigIdentifier;
import config.ReadPropertyFile;
import controller.StudentController.BC.*;
import controller.StudentController.CW.Student.Student_CW_CoursePage;
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
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;


public class Mohseni_Profiles_StudentProfile implements Initializable {
    public static final Logger log = LogManager.getLogger(Mohseni_Profiles_StudentProfile.class);

    Student student;
    Mohseni mohseni;
    ClientNetwork clientNetwork;

    @FXML
    ColorPicker colorPicker;

    @FXML
    Button LogOutButton;

    @FXML
    Button HomeButton;

    @FXML
    Label FirstNameLabel;

    @FXML
    Label LastNameLabel;

    @FXML
    Label IdentityCodeLabel;

    @FXML
    Label UsernameLabel;

    @FXML
    Label AverageScoreLabel;

    @FXML
    Label CollegeLabel;

    @FXML
    Label HelpTeacherLabel;

    @FXML
    Label YearComeToUniversityLabel;

    @FXML
    Label GradeLabel;

    @FXML
    Label StatusLabel;

    @FXML
    Label PhoneNumberTextField;

    @FXML
    Label EmailTextField;

    @FXML
    ImageView imageViewUser;

    Timeline timelineForConnected;

    @FXML
    Button reconnectionButton;

    @FXML
    ImageView connectionStatus;

    @FXML
    Button backButton;

    Stage stage;
    @FXML
    void ChangingColor(ActionEvent event) {
        Color color = colorPicker.getValue();
        HomeButton.setBackground(new Background(new BackgroundFill(color, null, null)));
        backButton.setBackground(new Background(new BackgroundFill(color, null, null)));

        mohseni.color = colorPicker.getValue();
        String s = "#" + Integer.toHexString(color.hashCode());
        clientNetwork.changeColorForMohseni(mohseni.getUsername(), s);

        log.info("change background");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        PublicMethods.initImageOfLogOutButton(LogOutButton);

        PublicMethods.initImageOfHomeButton(HomeButton);

        PublicMethods.initImageOfBackButton(backButton);
    }

    public void setMohseni(Mohseni mohseni, Student student, ClientNetwork clientNetwork){
        this.clientNetwork = clientNetwork;
        this.student = student;
        this.mohseni = mohseni;


        PublicMethods.initImageOfPerson(clientNetwork, imageViewUser, mohseni.getUsername(), student.getImage());



        Color color = mohseni.color;
        HomeButton.setBackground(new Background(new BackgroundFill(color, null, null)));
        backButton.setBackground(new Background(new BackgroundFill(color, null, null)));
        colorPicker.setValue(color);

        FirstNameLabel.setText(student.getFirstname());
        LastNameLabel.setText(student.getLastname());
        IdentityCodeLabel.setText(student.getCodeMelli());
        UsernameLabel.setText(student.getStudentNumber());
        PhoneNumberTextField.setText(student.getPhoneNumber());
        EmailTextField.setText(student.getEmail());
        AverageScoreLabel.setText(student.getAverage_score() + "");
        CollegeLabel.setText(student.getCollege() + "");

        String teacher = clientNetwork.getLastNameHelpTeacherFromId(student.getHelpTeacher());

        if(teacher == null){

        }
        else{
            HelpTeacherLabel.setText(teacher);
        }
        YearComeToUniversityLabel.setText(student.getYearComeToUniversity() + "");
        GradeLabel.setText(student.getGrade() + "");
        StatusLabel.setText(student.getEducationStatus() + "");


        timelineForConnected = PublicMethods.reconnectionTimeline(reconnectionButton, clientNetwork, connectionStatus);
    }

    public void HomeButtonFunction(ActionEvent e) {
        timelineForConnected.stop();
        try{
            stage = ((Stage)(((Scene)LogOutButton.getScene()).getWindow()));
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Fxml\\Mohseni\\MohseniFirstPage.fxml"));
            Parent root = loader.load();
            MohseniFirstPage MohseniFirstPage = loader.getController();
            MohseniFirstPage.setMohseni(mohseni, clientNetwork);
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
            stage = ((Stage)(((Scene)LogOutButton.getScene()).getWindow()));
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Fxml\\Public\\LoginPage.fxml"));
            Parent root = loader.load();
            LoginPage LoginPage = loader.getController();
            LoginPage.setClientNetwork(clientNetwork);
            LoginPage.setImageView();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

        }
        catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    public void backButtonFunction(ActionEvent e) {
        timelineForConnected.stop();
        try{
            stage = ((Stage)(((Scene)LogOutButton.getScene()).getWindow()));
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Fxml\\Mohseni\\Mohseni_Profiles.fxml"));
            Parent root = loader.load();
            Mohseni_Profiles Mohseni_Profiles = loader.getController();
            Mohseni_Profiles.setMohseni(mohseni, clientNetwork);
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
        catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }



}
