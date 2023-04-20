package controller.StudentController.CW.TA;


import back.course.*;
import back.enums.Grade;
import back.enums.HomeWorkOrExamType;
import back.persons.Student;
import clientNetwork.ClientNetwork;
import controller.StudentController.BC.BCFirstPage;
import controller.StudentController.MS.MSFirstPage;
import controller.StudentController.PHD.PHDFirstPage;
import controller.publicController.LoginPage;
import controller.publicMethods.PublicMethods;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.LineBuilder;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import org.apache.log4j.Logger;
import org.apache.log4j.LogManager;


public class TA_CW_CoursePage implements Initializable{
    public static final Logger log = LogManager.getLogger(TA_CW_CoursePage.class);


    Student student;
    String courseId;
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
    DatePicker datePicker;


    @FXML
    void ChangingColor(ActionEvent event) {
        Color color = colorPicker.getValue();
        HomeButton.setBackground(new Background(new BackgroundFill(color, null, null)));
        student.color = colorPicker.getValue();
        String s = "#" + Integer.toHexString(color.hashCode());

        ShowButtonFunction();

        colorPicker.setValue(color);
        log.info("change background");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        PublicMethods.initImageOfLogOutButton(LogOutButton);

        PublicMethods.initImageOfHomeButton(HomeButton);
    }

    public void setStudent(Student Student, ClientNetwork clientNetwork, String courseId){
        this.clientNetwork = clientNetwork;
        this.student = Student;
        this.courseId = courseId;

        Color color = Student.color;

        HomeButton.setBackground(new Background(new BackgroundFill(color, null, null)));
        colorPicker.setValue(color);

        timelineForConnected = PublicMethods.reconnectionTimeline(reconnectionButton, clientNetwork, connectionStatus);

        ShowButtonFunction();



    }

    public void updateCalender(){
        LinkedList<HomeWork> homeWorks = clientNetwork.getHomeWorksFromACourse(courseId);
        String hex = "#" + Integer.toHexString(student.getColor().hashCode());
        final Callback<DatePicker, DateCell> dayCellFactory = new Callback<DatePicker, DateCell>() {
            @Override
            public DateCell call(final DatePicker datePicker) {
                return new DateCell() {
                    @Override public void updateItem(LocalDate item, boolean empty) {
                        super.updateItem(item, empty);
                        for (int i = 0; i < homeWorks.size(); i++) {
                            String[] s = homeWorks.get(i).getEndTime().split("-");
                            LocalDate date = getItem();
                            if (isEmpty()) {
                                setDisable(true);
                            }
                            else if(date.isBefore(LocalDate.now())){
                                setDisable(true);
                            }
                            else if(item.getYear() == Integer.parseInt(s[0]) && item.getMonthValue() == Integer.parseInt(s[1]) && item.getDayOfMonth() == Integer.parseInt(s[2])){
                                setStyle("-fx-background-color: " + hex + ";");
                            }
                        }
                    }
                };
            }
        };

        datePicker.setDayCellFactory(dayCellFactory);
    }

    public void ShowButtonFunction(){
        PrintForPane();
        updateCalender();
    }

    public void PrintForPane(){
        Font font1 = Font.font("Comic Sans MS", FontWeight.BOLD, FontPosture.ITALIC, 20);
        Font font2 = Font.font("System", FontPosture.REGULAR, 15);
        pane.getChildren().clear();
        LinkedList<EducationalMaterials> educationalMaterials = clientNetwork.getEducationalMaterialsFromACourse(courseId);
        LinkedList<HomeWork> homeWorks = clientNetwork.getHomeWorksFromACourse(courseId);
        LinkedList<Exam> exams = clientNetwork.getExamsFromACourse(courseId);
        int counter = 0;

        Button[] buttons = new Button[3];


        Label labelEducationalMaterials = new Label("Educational Materials");
        labelEducationalMaterials.setLayoutX(10.0);
        labelEducationalMaterials.setLayoutY(10.0 + 100 * counter);
        labelEducationalMaterials.setFont(font1);
        pane.getChildren().add(labelEducationalMaterials);
        counter ++;


        for (int i = 0; i < educationalMaterials.size(); i++) {
            Label label = new Label(educationalMaterials.get(i).toString());
            label.setLayoutX(10.0);
            label.setLayoutY(10.0 + 100 * counter);
            label.setFont(font2);
            label.setCursor(Cursor.HAND);
            final int m = i;
            label.setOnMouseClicked(event -> {
                try{
                    stage = ((Stage)(((Scene)LogOutButton.getScene()).getWindow()));
                    FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Fxml\\Student\\CW\\TA\\TA_CW_CoursePage_EducationalMaterial.fxml"));
                    Parent root = loader.load();
                    TA_CW_CoursePage_EducationalMaterial TA_CW_CoursePage_EducationalMaterial = loader.getController();
                    TA_CW_CoursePage_EducationalMaterial.setStudent(student, clientNetwork, courseId, educationalMaterials, m);
                    Scene scene = new Scene(root);
                    stage.setScene(scene);
                    stage.show();
                }
                catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            });
            pane.getChildren().add(label);
            counter++;
        }

        Line line1 = LineBuilder.create()
                .startX(0)
                .startY(10 + 100 * counter)
                .endX(10000)
                .endY(10 + 100 * counter)
                .fill(student.getColor())
                .strokeWidth(5)
                .build();
        line1.setStroke(student.getColor());
        pane.getChildren().add(line1);

        counter++;

        Label labelHW = new Label("Home works");
        labelHW.setLayoutX(10.0);
        labelHW.setLayoutY(10.0 + 100 * counter);
        labelHW.setFont(font1);
        pane.getChildren().add(labelHW);

        counter++;


        for (int i = 0; i < homeWorks.size(); i++) {
            Label label = new Label(homeWorks.get(i).toString());
            label.setLayoutX(10.0);
            label.setLayoutY(10.0 + 100 * counter);
            label.setFont(font2);
            label.setCursor(Cursor.HAND);
            int index = i;
            label.setOnMouseClicked(event -> {
                if(homeWorks.get(index).getHomeWorkOrExamType() == HomeWorkOrExamType.FILE){
                    openCoursePageSendFile(false, exams, homeWorks, index);
                }
                else{
                    openCoursePageSendText(false, exams, homeWorks, index);
                }
            });
            pane.getChildren().add(label);
            counter++;
        }

        Line line2 = LineBuilder.create()
                .startX(0)
                .startY(10 + 100 * counter)
                .endX(10000)
                .endY(10 + 100 * counter)
                .fill(student.getColor())
                .strokeWidth(5)
                .build();
        line2.setStroke(student.getColor());
        pane.getChildren().add(line2);
        counter++;

        Label labelExam = new Label("Exams");
        labelExam.setLayoutX(10.0);
        labelExam.setLayoutY(10.0 + 100 * counter);
        labelExam.setFont(font1);
        pane.getChildren().add(labelExam);
        counter++;

        for (int i = 0; i < exams.size(); i++) {
            Label label = new Label(exams.get(i).toString());
            label.setLayoutX(10.0);
            label.setLayoutY(10.0 + 100 * counter);
            label.setFont(font2);
            label.setCursor(Cursor.HAND);
            int index = i;
            label.setOnMouseClicked(event -> {
                if(exams.get(index).getHomeWorkOrExamType() == HomeWorkOrExamType.FILE){
                    openCoursePageSendFile(true, exams, homeWorks, index);
                }
                else{
                    openCoursePageSendText(true, exams, homeWorks, index);
                }
            });
            pane.getChildren().add(label);
            counter++;
        }


        pane.setPrefHeight(counter * 100 + 80);

    }


    public void openCoursePageSendText(boolean isExam, LinkedList<Exam> exams, LinkedList<HomeWork> homeWorks, int index){
        timelineForConnected.stop();

        try{
            stage = ((Stage)(((Scene)LogOutButton.getScene()).getWindow()));
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Fxml\\Student\\CW\\TA\\TA_CW_CoursePage_SendText.fxml"));
            Parent root = loader.load();
            TA_CW_CoursePage_SendText TA_CW_CoursePage_SendText = loader.getController();
            TA_CW_CoursePage_SendText.setStudent(student, clientNetwork, courseId, isExam, exams, homeWorks, index);
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();


        }
        catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    public void openCoursePageSendFile(boolean isExam, LinkedList<Exam> exams, LinkedList<HomeWork> homeWorks, int index){
        timelineForConnected.stop();

        try{
            stage = ((Stage)(((Scene)LogOutButton.getScene()).getWindow()));
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Fxml\\Student\\CW\\TA\\TA_CW_CoursePage_SendFile.fxml"));
            Parent root = loader.load();
            TA_CW_CoursePage_SendFile TA_CW_CoursePage_SendFile = loader.getController();
            TA_CW_CoursePage_SendFile.setStudent(student, clientNetwork, courseId, isExam, exams, homeWorks, index);
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
        catch (IOException ioException) {
            ioException.printStackTrace();
        }
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
