package controller.TeacherController.CW;


import back.course.*;
import back.enums.HomeWorkOrExamType;
import back.enums.StatusTeacher;
import back.persons.Teacher;
import clientNetwork.ClientNetwork;
import config.ConfigIdentifier;
import config.ReadPropertyFile;
import controller.StudentController.CW.Student.Student_CW_CoursePage_EducationalMaterial;
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
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.image.Image;
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
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import org.apache.log4j.Logger;
import org.apache.log4j.LogManager;


public class Teacher_CW_CoursePage implements Initializable{
    public static final Logger log = LogManager.getLogger(Teacher_CW_CoursePage.class);


    Teacher teacher;
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
    Button addNewStudent;


    @FXML
    ChoiceBox<String> choiceBox;

    @FXML
    TextField textFiled;

    @FXML
    void ChangingColor(ActionEvent event) {
        Color color = colorPicker.getValue();
        HomeButton.setBackground(new Background(new BackgroundFill(color, null, null)));
        addNewStudent.setBackground(new Background(new BackgroundFill(color, null, null)));
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


        String[] items = {"Student", "TA"};
        choiceBox.getItems().addAll(items);
    }

    public void setTeacher(Teacher teacher, ClientNetwork clientNetwork, String courseId){
        this.clientNetwork = clientNetwork;
        this.teacher = teacher;
        this.courseId = courseId;

        Color color = teacher.color;

        HomeButton.setBackground(new Background(new BackgroundFill(color, null, null)));
        addNewStudent.setBackground(new Background(new BackgroundFill(color, null, null)));
        colorPicker.setValue(color);

        timelineForConnected = PublicMethods.reconnectionTimeline(reconnectionButton, clientNetwork, connectionStatus);

        ShowButtonFunction();



    }


    public void updateCalender(){
        LinkedList<HomeWork> homeWorks = clientNetwork.getHomeWorksFromACourse(courseId);
        String hex = "#" + Integer.toHexString(teacher.getColor().hashCode());
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
        buttons[0] = new Button("new");
        buttons[0].setBackground(new Background(new BackgroundFill(teacher.getColor(), null, null)));
        buttons[0].setLayoutX(300.0);
        buttons[0].setLayoutY(10.0 + 100 * counter);
        buttons[0].setCursor(Cursor.HAND);
        buttons[0].setOnAction(event -> {
            try{
                stage = ((Stage)(((Scene)LogOutButton.getScene()).getWindow()));
                FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Fxml\\Teacher\\CW\\Teacher_CW_CoursePage_AddEducationalMaterial.fxml"));
                Parent root = loader.load();
                Teacher_CW_CoursePage_AddEducationalMaterial Teacher_CW_CoursePage_AddEducationalMaterial = loader.getController();
                Teacher_CW_CoursePage_AddEducationalMaterial.setTeacher(teacher, clientNetwork, courseId, educationalMaterials);
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            }
            catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });
        pane.getChildren().add(buttons[0]);
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
                    FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Fxml\\Teacher\\CW\\Teacher_CW_CoursePage_EducationalMaterial.fxml"));
                    Parent root = loader.load();
                    Teacher_CW_CoursePage_EducationalMaterial Teacher_CW_CoursePage_EducationalMaterial = loader.getController();
                    Teacher_CW_CoursePage_EducationalMaterial.setTeacher(teacher, clientNetwork, courseId, educationalMaterials, m);
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
                .fill(teacher.getColor())
                .strokeWidth(5)
                .build();
        line1.setStroke(teacher.getColor());
        pane.getChildren().add(line1);

        counter++;

        Label labelHW = new Label("Home works");
        labelHW.setLayoutX(10.0);
        labelHW.setLayoutY(10.0 + 100 * counter);
        labelHW.setFont(font1);
        pane.getChildren().add(labelHW);
        buttons[1] = new Button("new");
        buttons[1].setBackground(new Background(new BackgroundFill(teacher.getColor(), null, null)));
        buttons[1].setLayoutX(300.0);
        buttons[1].setLayoutY(10.0 + 100 * counter);
        buttons[1].setCursor(Cursor.HAND);
        buttons[1].setOnAction(event -> {
            openTeacher_CW_AddNewExamOrHw(false, exams, homeWorks);
        });
        pane.getChildren().add(buttons[1]);

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
                .fill(teacher.getColor())
                .strokeWidth(5)
                .build();
        line2.setStroke(teacher.getColor());
        pane.getChildren().add(line2);
        counter++;

        Label labelExam = new Label("Exams");
        labelExam.setLayoutX(10.0);
        labelExam.setLayoutY(10.0 + 100 * counter);
        labelExam.setFont(font1);
        pane.getChildren().add(labelExam);
        buttons[2] = new Button("new");
        buttons[2].setBackground(new Background(new BackgroundFill(teacher.getColor(), null, null)));
        buttons[2].setLayoutX(300.0);
        buttons[2].setLayoutY(10.0 + 100 * counter);
        buttons[2].setCursor(Cursor.HAND);
        buttons[2].setOnAction(event -> {
            openTeacher_CW_AddNewExamOrHw(true, exams, homeWorks);
        });
        pane.getChildren().add(buttons[2]);
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

    public void addNewStudentFunction(ActionEvent event){
        if(getResultOfChoiceBox().equals("")){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Choice box is empty");
            alert.showAndWait();
        }
        else if(textFiled.getText() == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Text field is empty");
            alert.showAndWait();
        }
        else if(textFiled.getText().equals("")){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Text field is empty");
            alert.showAndWait();
        }
        else{
            if(clientNetwork.addStudentToCourseInCW(getResultOfChoiceBox(), textFiled.getText(), courseId)){
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setHeaderText("Student added successfully");
                alert.showAndWait();
            }
            else{
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("error");
                alert.showAndWait();
            }
        }
    }

    public String  getResultOfChoiceBox(){
        String stringChoiceBox = (String) choiceBox.getValue();
        if(stringChoiceBox == null){
            return "";
        }
        return stringChoiceBox ;
    }

    public void openCoursePageSendText(boolean isExam, LinkedList<Exam> exams, LinkedList<HomeWork> homeWorks, int index){
        timelineForConnected.stop();

        try{
            stage = ((Stage)(((Scene)LogOutButton.getScene()).getWindow()));
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Fxml\\Teacher\\CW\\Teacher_CW_CoursePage_SendText.fxml"));
            Parent root = loader.load();
            Teacher_CW_CoursePage_SendText Teacher_CW_CoursePage_SendText = loader.getController();
            Teacher_CW_CoursePage_SendText.setTeacher(teacher, clientNetwork, courseId, isExam, exams, homeWorks, index);
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
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Fxml\\Teacher\\CW\\Teacher_CW_CoursePage_SendFile.fxml"));
            Parent root = loader.load();
            Teacher_CW_CoursePage_SendFile Teacher_CW_CoursePage_SendFile = loader.getController();
            Teacher_CW_CoursePage_SendFile.setTeacher(teacher, clientNetwork, courseId, isExam, exams, homeWorks, index);
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();


        }
        catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    public void openTeacher_CW_AddNewExamOrHw(boolean isExam, LinkedList<Exam> exams, LinkedList<HomeWork> homeWorks){
        timelineForConnected.stop();

        try{
            stage = ((Stage)(((Scene)LogOutButton.getScene()).getWindow()));
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Fxml\\Teacher\\CW\\Teacher_CW_AddNewExamOrHw.fxml"));
            Parent root = loader.load();
            Teacher_CW_AddNewExamOrHw Teacher_CW_AddNewExamOrHw = loader.getController();
            Teacher_CW_AddNewExamOrHw.setTeacher(teacher, clientNetwork, courseId, isExam, exams, homeWorks);
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
