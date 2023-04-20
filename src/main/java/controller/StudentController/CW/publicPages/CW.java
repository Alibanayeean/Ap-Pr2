package controller.StudentController.CW.publicPages;

import back.course.Course;
import back.enums.Grade;
import back.persons.Student;
import clientNetwork.ClientNetwork;
import config.ConfigIdentifier;
import config.ReadPropertyFile;
import controller.StudentController.BC.BCFirstPage;
import controller.StudentController.BC.BC_ShowCourseListMenu;
import controller.StudentController.CW.Student.Student_CW_CoursePage;
import controller.StudentController.CW.TA.TA_CW_CoursePage;
import controller.StudentController.MS.MSFirstPage;
import controller.StudentController.PHD.PHDFirstPage;
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
import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;
import org.apache.log4j.LogManager;


public class CW implements Initializable{
    public static final Logger log = LogManager.getLogger(BC_ShowCourseListMenu.class);


    Student student;
    ClientNetwork clientNetwork;

    @FXML
    ColorPicker colorPicker;

    @FXML
    Button LogOutButton;

    @FXML
    Button HomeButton;

    @FXML
    Pane pane;

    Stage stage;

    Timeline timelineForConnected;

    @FXML
    Button reconnectionButton;

    @FXML
    ImageView connectionStatus;

    @FXML
    ImageView myCoursesImageView;


    @FXML
    void ChangingColor(ActionEvent event) {


        Color color = colorPicker.getValue();
        HomeButton.setBackground(new Background(new BackgroundFill(color, null, null)));

        student.color = colorPicker.getValue();
        String s = "#" + Integer.toHexString(color.hashCode());
        clientNetwork.changeColorForStudent(student.getStudentNumber(), s);

        ShowButtonFunction();

        log.info("change background");

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        PublicMethods.initImageOfLogOutButton(LogOutButton);

        PublicMethods.initImageOfHomeButton(HomeButton);

        File file = new File(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.publicURL) + "myCourses.png");
        Image image = new Image(file.toURI().toString());
        myCoursesImageView.setImage(image);


    }

    public void setStudent(Student student, ClientNetwork clientNetwork){
        this.clientNetwork = clientNetwork;
        this.student = student;

        Color color = student.color;

        HomeButton.setBackground(new Background(new BackgroundFill(color, null, null)));
        colorPicker.setValue(color);

        timelineForConnected = PublicMethods.reconnectionTimeline(reconnectionButton, clientNetwork, connectionStatus);

        ShowButtonFunction();

    }

    public void ShowButtonFunction(){
        LinkedList<Course> courses = clientNetwork.GiveCoursesFromAStudent(student.getId());
        PrintForPane(courses);
    }

    public void PrintForPane(LinkedList<Course> courses){
        Font font = Font.font("Comic Sans MS", FontWeight.BOLD, FontPosture.ITALIC, 15);
        pane.getChildren().clear();
        int counterForY = 0;
        int counterForX = 0;

        for (int i = 0; i < courses.size(); i++) {
            Polygon rhombus1 = new Polygon(50.0 + 500 * counterForX, 50.0 + 300 * counterForY, 250.0 + 500 * counterForX, 50.0 + 300 * counterForY
                    , 250.0 + 500 * counterForX, 250.0 + 300 * counterForY, 50.0 + 500 * counterForX, 250.0 + 300 * counterForY);
            Color color1 = new Color(student.getColor().getRed(), student.getColor().getGreen(), student.getColor().getBlue(), 0.5);
            rhombus1.setFill(color1);
            pane.getChildren().add(rhombus1);


            Label label1 = new Label(courses.get(i).getName());
            label1.setLayoutX(80.0 + 500 * counterForX);
            label1.setLayoutY(80.0 + 300 * counterForY);
            label1.setMinWidth(140);
            label1.setMinHeight(140);
            label1.setFont(font);
            label1.setAlignment(Pos.CENTER);
            label1.setBackground(new Background(new BackgroundFill(Color.WHITE, null, null)));
            label1.setCursor(Cursor.HAND);

            final int m = i;
            label1.setOnMouseClicked(event -> {
                timelineForConnected.stop();

                try{
                    if(!clientNetwork.isAStudentTAOfACourse(student.getId(), courses.get(m).getId())){
                        stage = ((Stage)(((Scene)LogOutButton.getScene()).getWindow()));
                        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Fxml\\Student\\CW\\Student\\Student_CW_CoursePage.fxml"));
                        Parent root = loader.load();
                        Student_CW_CoursePage Student_CW_CoursePage = loader.getController();
                        Student_CW_CoursePage.setStudent(student, clientNetwork, courses.get(m).getId());
                        Scene scene = new Scene(root);
                        stage.setScene(scene);
                        stage.show();
                    }
                    else{
                        stage = ((Stage)(((Scene)LogOutButton.getScene()).getWindow()));
                        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Fxml\\Student\\CW\\TA\\TA_CW_CoursePage.fxml"));
                        Parent root = loader.load();
                        TA_CW_CoursePage TA_CW_CoursePage = loader.getController();
                        TA_CW_CoursePage.setStudent(student, clientNetwork, courses.get(m).getId());
                        Scene scene = new Scene(root);
                        stage.setScene(scene);
                        stage.show();
                    }
                }
                catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            });
            pane.getChildren().add(label1);

            counterForX++;
            if(i != 0 && (i + 1) % 3 == 0){
                counterForY++;
                counterForX = 0;
            }

        }

        pane.setPrefHeight((counterForY + 1) * 300 + 80);

    }



    public void HomeButtonFunction(ActionEvent e) {
        timelineForConnected.stop();

        try{
            if(student.getGrade() == Grade.BC){
                stage = ((Stage)(((Scene)LogOutButton.getScene()).getWindow()));
                FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Fxml\\Student\\BC\\BCFirstPage.fxml"));
                Parent root = loader.load();
                BCFirstPage BCFirstPage = loader.getController();
                BCFirstPage.setStudent(student, clientNetwork);
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            }
            else if(student.getGrade() == Grade.MS){
                stage = ((Stage)(((Scene)LogOutButton.getScene()).getWindow()));
                FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Fxml\\Student\\MS\\MSFirstPage.fxml"));
                Parent root = loader.load();
                MSFirstPage MSFirstPage = loader.getController();
                MSFirstPage.setStudent(student, clientNetwork);
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            }

            else if(student.getGrade() == Grade.PHD){
                stage = ((Stage)(((Scene)LogOutButton.getScene()).getWindow()));
                FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Fxml\\Student\\PHD\\PHDFirstPage.fxml"));
                Parent root = loader.load();
                PHDFirstPage PHDFirstPage = loader.getController();
                PHDFirstPage.setStudent(student, clientNetwork);
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            }



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

            LocalDateTime localDateTime = LocalDateTime.now();
            clientNetwork.changeLastLoginStudent(student.getId(), localDateTime.getSecond(), localDateTime.getMinute(), localDateTime.getHour(), localDateTime.getDayOfMonth(), localDateTime.getMonthValue(), localDateTime.getYear());

        }
        catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
}
