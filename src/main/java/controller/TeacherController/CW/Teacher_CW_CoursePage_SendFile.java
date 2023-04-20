package controller.TeacherController.CW;


import back.course.Exam;
import back.course.HomeWork;
import back.enums.HomeWorkOrExamType;
import back.enums.StatusTeacher;
import back.persons.Teacher;
import clientNetwork.ClientNetwork;
import com.google.gson.Gson;
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
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.*;

import org.apache.log4j.Logger;
import org.apache.log4j.LogManager;


public class Teacher_CW_CoursePage_SendFile implements Initializable{
    public static final Logger log = LogManager.getLogger(Teacher_CW_CoursePage_SendFile.class);


    Teacher teacher;
    String courseId;
    ClientNetwork clientNetwork;

    @FXML
    ColorPicker colorPicker;

    @FXML
    Button LogOutButton;

    @FXML
    Button HomeButton;

    Stage stage;

    Timeline timelineForConnected;

    @FXML
    Button reconnectionButton;

    @FXML
    ImageView connectionStatus;

    @FXML
    Button backButton;

    @FXML
    Button downloadButton;

    @FXML
    Pane pane;

    @FXML
    Button submitButton;

    boolean isExam;
    LinkedList<Exam> exams;
    LinkedList<HomeWork> homeWorks;

    int index;

    LinkedList<TextField> textFields = new LinkedList<>();
    LinkedList<Integer> IdsForTextFields = new LinkedList<>();



    @FXML
    void ChangingColor(ActionEvent event) {


        Color color = colorPicker.getValue();
        HomeButton.setBackground(new Background(new BackgroundFill(color, null, null)));
        backButton.setBackground(new Background(new BackgroundFill(color, null, null)));
        downloadButton.setBackground(new Background(new BackgroundFill(color, null, null)));
        submitButton.setBackground(new Background(new BackgroundFill(color, null, null)));

        teacher.color = colorPicker.getValue();
        String s = "#" + Integer.toHexString(color.hashCode());
        clientNetwork.changeColorForTeacher(teacher.getUsername(), s);

        ShowFunction();

        log.info("change background");

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        PublicMethods.initImageOfLogOutButton(LogOutButton);

        PublicMethods.initImageOfHomeButton(HomeButton);

        PublicMethods.initImageOfBackButton(backButton);

    }

    public void setTeacher(Teacher teacher, ClientNetwork clientNetwork, String courseId, boolean isExam, LinkedList<Exam> exams, LinkedList<HomeWork> homeWorks, int index){
        this.clientNetwork = clientNetwork;
        this.teacher = teacher;
        this.courseId = courseId;
        this.isExam = isExam;
        this.homeWorks = homeWorks;
        this.exams = exams;
        this.index = index;

        Color color = teacher.color;

        HomeButton.setBackground(new Background(new BackgroundFill(color, null, null)));
        backButton.setBackground(new Background(new BackgroundFill(color, null, null)));
        downloadButton.setBackground(new Background(new BackgroundFill(color, null, null)));
        submitButton.setBackground(new Background(new BackgroundFill(color, null, null)));
        colorPicker.setValue(color);

        timelineForConnected = PublicMethods.reconnectionTimeline(reconnectionButton, clientNetwork, connectionStatus);

        ShowFunction();



    }

    public void ShowFunction(){
        if(isExam){
            exams = clientNetwork.getExamsFromACourse(courseId);
        }
        else{
            homeWorks = clientNetwork.getHomeWorksFromACourse(courseId);
        }

        PrintForPane();
    }

    public void PrintForPane(){
        textFields.clear();
        IdsForTextFields.clear();
        if(isExam){
            pane.getChildren().clear();
            pane.setPrefHeight(exams.get(index).getStudentsSend_Score().size() * 200 + 30);

            int counter = 0;
            for (Map.Entry<Integer, Double> entry : exams.get(index).getStudentsSend_Score().entrySet()) {
                Integer key = entry.getKey();
                Double score = entry.getValue();
                String answer = exams.get(index).getStudentsSend_AddressOrText().get(key);
                String time = exams.get(index).getStudentsSend_Time().get(key);
                String name = clientNetwork.getNameOfAStudentFromId(key);
                if(name.equals("")){
                    continue;
                }
                String studentsNumber = clientNetwork.getStudentsNumberOfAStudentFromId(key);
                if(studentsNumber.equals("")){
                    continue;
                }
                HelpPrint(name , answer, score, time, studentsNumber, counter, key);
                counter++;
            }
        }
        else{
            pane.getChildren().clear();
            pane.setPrefHeight(homeWorks.get(index).getStudentsSend_Score().size() * 200 + 30);

            int counter = 0;
            for (Map.Entry<Integer, Double> entry : homeWorks.get(index).getStudentsSend_Score().entrySet()) {
                Integer key = entry.getKey();
                Double score = entry.getValue();
                String answer = homeWorks.get(index).getStudentsSend_AddressOrText().get(key);
                String time = homeWorks.get(index).getStudentsSend_Time().get(key);
                String name = clientNetwork.getNameOfAStudentFromId(key);
                if(name.equals("")){
                    continue;
                }
                String studentsNumber = clientNetwork.getStudentsNumberOfAStudentFromId(key);
                if(studentsNumber.equals("")){
                    continue;
                }
                HelpPrint(name , answer, score, time, studentsNumber, counter, key);
                counter++;
            }
        }
    }

    public void HelpPrint(String name, String answer, double score, String time, String studentNumber, int counter, int studentId){
        Font font = Font.font("System", FontWeight.NORMAL, FontPosture.REGULAR, 20);

        if(isExam){
            if(exams.get(index).getHomeWorkOrExamType() == HomeWorkOrExamType.FILE){
                Label label1 = new Label(name);
                label1.prefHeight(142);
                label1.prefWidth(57);
                label1.setLayoutX(5);
                label1.setLayoutY(23 + 200 * counter);
                label1.setFont(font);
                pane.getChildren().add(label1);

                Label label2 = new Label(studentNumber);
                label2.prefHeight(142);
                label2.prefWidth(57);
                label2.setLayoutX(240);
                label2.setLayoutY(23 + 200 * counter);
                label2.setFont(font);
                pane.getChildren().add(label2);

                Button button  = new Button("Download");
                button.prefHeight(142);
                button.prefWidth(57);
                button.setLayoutX(573);
                button.setLayoutY(23 + 200 * counter);
                button.setFont(font);
                button.setBackground(new Background(new BackgroundFill(teacher.getColor(), null, null)));
                button.setCursor(Cursor.HAND);
                button.setOnAction(event -> {
                    File fileClicked = new File(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.publicURL) +  teacher.getUsername() + "/" + answer);
                    if(fileClicked.exists()){
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setHeaderText("go to this folder: " + ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.publicURL) + teacher.getUsername() + "/" + answer);
                        alert.showAndWait();
                        PublicMethods.openADesktopFolder(fileClicked);
                        return;
                    }
                    clientNetwork.sendFileFromServer(answer , teacher.getUsername());
                });
                pane.getChildren().add(button);


                TextField textFieldForScore = new TextField();
                textFieldForScore.setPrefHeight(50);
                textFieldForScore.setPrefWidth(100);
                textFieldForScore.setLayoutX(800);
                textFieldForScore.setLayoutY(23 + 200 * counter);
                textFields.add(textFieldForScore);
                IdsForTextFields.add(studentId);
                pane.getChildren().add(textFieldForScore);

                if(score != -1){
                    textFieldForScore.setText(score + "");
                }

                Label label = new Label(time);
                label.setFont(font);
                label.setLayoutX(1050);
                label.setLayoutY(23 + 200 * counter);
                pane.getChildren().add(label);
            }
        }
        else{
            if(homeWorks.get(index).getHomeWorkOrExamType() == HomeWorkOrExamType.FILE){
                Label label1 = new Label(name);
                label1.prefHeight(142);
                label1.prefWidth(57);
                label1.setLayoutX(5);
                label1.setLayoutY(23 + 200 * counter);
                label1.setFont(font);
                pane.getChildren().add(label1);

                Label label2 = new Label(studentNumber);
                label2.prefHeight(142);
                label2.prefWidth(57);
                label2.setLayoutX(243);
                label2.setLayoutY(23 + 200 * counter);
                label2.setFont(font);
                pane.getChildren().add(label2);

                Button button  = new Button("Download");
                button.prefHeight(142);
                button.prefWidth(57);
                button.setLayoutX(573);
                button.setLayoutY(23 + 200 * counter);
                button.setFont(font);
                button.setBackground(new Background(new BackgroundFill(teacher.getColor(), null, null)));
                button.setCursor(Cursor.HAND);
                button.setOnAction(event -> {
                    File fileClicked = new File(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.publicURL) + teacher.getUsername() + "/" + answer);
                    if(fileClicked.exists()){
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setHeaderText("go to this folder: " + ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.publicURL) + teacher.getUsername() + "/" + answer);
                        alert.showAndWait();
                        PublicMethods.openADesktopFolder(fileClicked);
                        return;
                    }
                    clientNetwork.sendFileFromServer(answer , teacher.getUsername());
                });
                pane.getChildren().add(button);


                TextField textFieldForScore = new TextField();
                textFieldForScore.setPrefHeight(50);
                textFieldForScore.setPrefWidth(100);
                textFieldForScore.setLayoutX(800);
                textFieldForScore.setLayoutY(23 + 200 * counter);
                textFields.add(textFieldForScore);
                IdsForTextFields.add(studentId);
                pane.getChildren().add(textFieldForScore);
                if(score != -1){
                    textFieldForScore.setText(score + "");
                }

                Label label = new Label(time);
                label.setFont(font);
                label.setLayoutX(1050);
                label.setLayoutY(23 + 200 * counter);
                pane.getChildren().add(label);


            }
        }


    }

    public void submitFunction(){
        if(isExam){
            for (int i = 0; i < textFields.size(); i++) {
                boolean b = checkValidScore(textFields.get(i).getText());
                if(!b){
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("ERROR");
                    alert.setHeaderText("Incorrect score");
                    alert.showAndWait();
                    log.error("invalid score");
                    continue;
                }
                exams.get(index).getStudentsSend_Score().replace(IdsForTextFields.get(i), Double.parseDouble(textFields.get(i).getText()));
                String s = (new Gson()).toJson(exams);
                clientNetwork.saveExamsOrHWsForCw(courseId, s, isExam);
            }
        }
        else{
            for (int i = 0; i < textFields.size(); i++) {
                boolean b = checkValidScore(textFields.get(i).getText());
                if(!b){
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("ERROR");
                    alert.setHeaderText("Incorrect score");
                    alert.showAndWait();
                    log.error("invalid score");
                    continue;
                }
                homeWorks.get(index).getStudentsSend_Score().replace(IdsForTextFields.get(i), Double.parseDouble(textFields.get(i).getText()));
                String s = (new Gson()).toJson(homeWorks);
                clientNetwork.saveExamsOrHWsForCw(courseId, s, isExam);
            }
        }

        ShowFunction();
    }

    public boolean checkValidScore(String s){
        if(s == null)
            return false;
        if(s.equals("")){
            return false;
        }
        if(s.charAt(0) == '.'){
            return false;
        }
        for (int i = 0; i < s.length(); i++) {
            if(!((s.charAt(i) >= '0' & s.charAt(i) <= '9') | s.charAt(i) == '.')){
                return false;
            }
        }
        double a = Double.parseDouble(s);
        if(!(a >= 0 & a <= 20.0)){
            return false;
        }
        return true;

    }

    public void downloadButtonFunction(ActionEvent e) {
        if(isExam){
            File fileClicked = new File(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.publicURL) +  teacher.getUsername() + "/" + exams.get(index).getAddressOrTextQuestionInServer());
            if(fileClicked.exists()){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText("go to this folder: " + ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.publicURL) + teacher.getUsername() + "/" + exams.get(index).getAddressOrTextQuestionInServer());
                alert.showAndWait();
                PublicMethods.openADesktopFolder(fileClicked);
                return;
            }
            clientNetwork.sendFileFromServer(exams.get(index).getAddressOrTextQuestionInServer() , teacher.getUsername());
        }
        else{
            File fileClicked = new File(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.publicURL) + teacher.getUsername() + "/" + homeWorks.get(index).getAddressOrTextQuestionInServer());
            if(fileClicked.exists()){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText("go to this folder: " + ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.publicURL) + teacher.getUsername() + "/" + homeWorks.get(index).getAddressOrTextQuestionInServer());
                alert.showAndWait();
                PublicMethods.openADesktopFolder(fileClicked);
                return;
            }
            clientNetwork.sendFileFromServer(homeWorks.get(index).getAddressOrTextQuestionInServer() , teacher.getUsername());
        }
    }


    public void backButtonFunction(ActionEvent e) {
        timelineForConnected.stop();

        try{
            stage = ((Stage)(((Scene)LogOutButton.getScene()).getWindow()));
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Fxml\\Teacher\\CW\\Teacher_CW_CoursePage.fxml"));
            Parent root = loader.load();
            Teacher_CW_CoursePage Teacher_CW_CoursePage = loader.getController();
            Teacher_CW_CoursePage.setTeacher(teacher, clientNetwork, courseId);
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
