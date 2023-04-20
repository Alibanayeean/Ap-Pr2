package controller.TeacherController.CW;


import back.course.Course;
import back.enums.StatusTeacher;
import back.persons.Teacher;
import clientNetwork.ClientNetwork;
import config.ConfigIdentifier;
import config.ReadPropertyFile;
import controller.TeacherController.Boss.BossFirstPage;
import controller.TeacherController.EducationalAssistant.EducationalAssistantFirstPage;
import controller.TeacherController.Teacher.TeacherFirstPage;
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


public class Teacher_CW implements Initializable{
    public static final Logger log = LogManager.getLogger(Teacher_CW.class);


    Teacher teacher;
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
        teacher.color = colorPicker.getValue();
        String s = "#" + Integer.toHexString(color.hashCode());
        clientNetwork.changeColorForTeacher(teacher.getUsername(), s);

        ShowButtonFunction();

        colorPicker.setValue(color);
        log.info("change background");



    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        PublicMethods.initImageOfLogOutButton(LogOutButton);

        PublicMethods.initImageOfHomeButton(HomeButton);


        File file3 = new File(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.publicURL) + "myCourses.png");
        Image image3 = new Image(file3.toURI().toString());
        myCoursesImageView.setImage(image3);


    }

    public void setTeacher(Teacher teacher, ClientNetwork clientNetwork){
        this.clientNetwork = clientNetwork;
        this.teacher = teacher;

        Color color = teacher.color;

        HomeButton.setBackground(new Background(new BackgroundFill(color, null, null)));
        colorPicker.setValue(color);

        timelineForConnected = PublicMethods.reconnectionTimeline(reconnectionButton, clientNetwork, connectionStatus);

        ShowButtonFunction();

    }

    public void ShowButtonFunction(){
        LinkedList<Course> courses = clientNetwork.GiveCoursesFromATeacher(teacher.getId());
        PrintForPane(courses);
    }

    public void PrintForPane(LinkedList<Course> courses){
        Font font = Font.font("Comic Sans MS", FontWeight.BOLD, FontPosture.ITALIC, 15);
//        courses = clientNetwork.getAllCourses();
        pane.getChildren().clear();
        pane.setPrefHeight(courses.size() * 100 + 80);
        int counterForY = 0;
        int counterForX = 0;

        for (int i = 0; i < courses.size(); i++) {
            Polygon rhombus1 = new Polygon(50.0 + 500 * counterForX, 50.0 + 300 * counterForY, 250.0 + 500 * counterForX, 50.0 + 300 * counterForY
                    , 250.0 + 500 * counterForX, 250.0 + 300 * counterForY, 50.0 + 500 * counterForX, 250.0 + 300 * counterForY);
            Color color1 = new Color(teacher.getColor().getRed(), teacher.getColor().getGreen(), teacher.getColor().getBlue(), 0.5);
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
                    stage = ((Stage)(((Scene)LogOutButton.getScene()).getWindow()));
                    FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Fxml\\Teacher\\CW\\Teacher_CW_CoursePage.fxml"));
                    Parent root = loader.load();
                    Teacher_CW_CoursePage Teacher_CW_CoursePage = loader.getController();
                    Teacher_CW_CoursePage.setTeacher(teacher, clientNetwork, courses.get(m).getId());
                    Scene scene = new Scene(root);
                    stage.setScene(scene);
                    stage.show();
                }
                catch (IOException ioException) {
                    ioException.printStackTrace();
                }
                return;
            });
            pane.getChildren().add(label1);

            counterForX++;
            if(i != 0 && i % 3 == 0){
                counterForY++;
                counterForX = 0;
            }

        }
    }



    public void HomeButtonFunction(ActionEvent e) {
        timelineForConnected.stop();

        try{
            if(teacher.getStatusTeacher() == StatusTeacher.Simple){
                stage = ((Stage)(((Scene)LogOutButton.getScene()).getWindow()));
                FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Fxml\\Teacher\\Teacher\\TeacherFirstPage.fxml"));
                Parent root = loader.load();
                TeacherFirstPage teacherFirstPage = loader.getController();
                teacherFirstPage.setTeacher(teacher, clientNetwork);
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            }
            else if(teacher.getStatusTeacher() == StatusTeacher.Boss){
                stage = ((Stage)(((Scene)LogOutButton.getScene()).getWindow()));
                FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Fxml\\Teacher\\Boss\\BossFirstPage.fxml"));
                Parent root = loader.load();
                BossFirstPage BossFirstPage = loader.getController();
                BossFirstPage.setTeacher(teacher, clientNetwork);
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            }
            else if(teacher.getStatusTeacher() == StatusTeacher.EA){
                stage = ((Stage)(((Scene)LogOutButton.getScene()).getWindow()));
                FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Fxml\\Teacher\\EducationalAssistant\\EducationalAssistantFirstPage.fxml"));
                Parent root = loader.load();
                EducationalAssistantFirstPage EducationalAssistantFirstPage = loader.getController();
                EducationalAssistantFirstPage.setTeacher(teacher, clientNetwork);
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
            clientNetwork.changeLastLoginTeacher(teacher.getId(), localDateTime.getSecond(), localDateTime.getMinute(), localDateTime.getHour(), localDateTime.getDayOfMonth(), localDateTime.getMonthValue(), localDateTime.getYear());




        }
        catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
}
