package controller.StudentController.CW.Student;


import back.course.Exam;
import back.course.HomeWork;
import back.enums.Grade;
import back.enums.HomeWorkOrExamType;
import back.persons.Student;
import clientNetwork.ClientNetwork;
import com.google.gson.Gson;
import config.ConfigIdentifier;
import config.ReadPropertyFile;
import controller.StudentController.BC.BCFirstPage;
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


public class Student_CW_CoursePage_SendText implements Initializable{
    public static final Logger log = LogManager.getLogger(Student_CW_CoursePage_SendText.class);


    Student student;
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
    TextArea textArea;

    @FXML
    Label beginLabel;

    @FXML
    Label endLabel;

    @FXML
    Label timeLeftLabel;

    @FXML
    Label scoreLabel;

    @FXML
    Label isSendLabel;

    @FXML
    Button submitButton;

    @FXML
    Button backButton;

    @FXML
    Button downloadButton;

    boolean isExam;
    LinkedList<Exam> exams;
    LinkedList<HomeWork> homeWorks;

    @FXML
    Button buttonAnswer;

    @FXML
    TextArea textAreaAnswer;

    @FXML
    Label answerLabel;


    int index;

    @FXML
    void ChangingColor(ActionEvent event) {


        Color color = colorPicker.getValue();
        HomeButton.setBackground(new Background(new BackgroundFill(color, null, null)));
        submitButton.setBackground(new Background(new BackgroundFill(color, null, null)));
        backButton.setBackground(new Background(new BackgroundFill(color, null, null)));
        downloadButton.setBackground(new Background(new BackgroundFill(color, null, null)));

        student.color = colorPicker.getValue();
        String s = "#" + Integer.toHexString(color.hashCode());
        clientNetwork.changeColorForStudent(student.getStudentNumber(), s);

        ShowFunction();

        log.info("change background");

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        PublicMethods.initImageOfLogOutButton(LogOutButton);

        PublicMethods.initImageOfHomeButton(HomeButton);

        PublicMethods.initImageOfBackButton(backButton);

        buttonAnswer.setVisible(false);
        answerLabel.setVisible(false);
        textAreaAnswer.setEditable(false);
        textAreaAnswer.setEditable(false);
    }

    public void setStudent(Student student, ClientNetwork clientNetwork, String courseId, boolean isExam, LinkedList<Exam> exams, LinkedList<HomeWork> homeWorks, int index){
        this.clientNetwork = clientNetwork;
        this.student = student;
        this.courseId = courseId;
        this.isExam = isExam;
        this.homeWorks = homeWorks;
        this.exams = exams;
        this.index = index;

        Color color = student.color;

        HomeButton.setBackground(new Background(new BackgroundFill(color, null, null)));
        submitButton.setBackground(new Background(new BackgroundFill(color, null, null)));
        backButton.setBackground(new Background(new BackgroundFill(color, null, null)));
        downloadButton.setBackground(new Background(new BackgroundFill(color, null, null)));
        colorPicker.setValue(color);

        timelineForConnected = PublicMethods.reconnectionTimeline(reconnectionButton, clientNetwork, connectionStatus);


        initForFirstTime();
        ShowFunction();



    }

    public void initForFirstTime(){
        String text = "";
        if(isExam){
            text = exams.get(index).getStudentsSend_AddressOrText().get(student.getId());
            if(text == null){
                text = "";
            }
        }
        else{
            text = homeWorks.get(index).getStudentsSend_AddressOrText().get(student.getId());
            if(text == null){
                text = "";
            }
        }
        textArea.setText(text);
        String end = "";
        if(isExam){
            end = calculateTimeLeft(exams.get(index).getEndTime());
        }
        else{
            end = calculateTimeLeft(homeWorks.get(index).getEndTime());
        }
        if(end.equals("0")){
            submitButton.setVisible(false);
            textArea.setEditable(false);
        }
    }

    public void ShowFunction(){
        exams = clientNetwork.getExamsFromACourse(courseId);
        homeWorks = clientNetwork.getHomeWorksFromACourse(courseId);
        if (isExam){
            if(exams.get(index).isAnswerUploaded()){
                Font font1 = Font.font("Comic Sans MS", FontWeight.BOLD, FontPosture.ITALIC, 15);
                answerLabel.setText("Answer: ");
                answerLabel.setFont(font1);
                if(exams.get(index).getTypeAnswer() == HomeWorkOrExamType.FILE){
                    textAreaAnswer.setVisible(false);
                    buttonAnswer.setVisible(true);
                }
                else{
                    textAreaAnswer.setVisible(true);
                    buttonAnswer.setVisible(false);
                    textAreaAnswer.setText(exams.get(index).getAddressOrTextAnswerInServer());
                }
            }
            else{
                textAreaAnswer.setVisible(false);
                buttonAnswer.setVisible(false);
                answerLabel.setVisible(false);
            }
        }
        else{
            if(homeWorks.get(index).isAnswerUploaded()){
                Font font1 = Font.font("Comic Sans MS", FontWeight.BOLD, FontPosture.ITALIC, 15);
                answerLabel.setText("Answer: ");
                answerLabel.setFont(font1);
                if(homeWorks.get(index).getTypeAnswer() == HomeWorkOrExamType.FILE){
                    textAreaAnswer.setVisible(false);
                    buttonAnswer.setVisible(true);
                }
                else{
                    textAreaAnswer.setVisible(true);
                    buttonAnswer.setVisible(false);
                    textAreaAnswer.setText(homeWorks.get(index).getAddressOrTextAnswerInServer());
                }
            }
            else{
                textAreaAnswer.setVisible(false);
                buttonAnswer.setVisible(false);
                answerLabel.setVisible(false);
            }
        }
        initForFirstTime();
        DrawForLabels();
    }

    public void DrawForLabels(){
        Font font1 = Font.font("Comic Sans MS", FontWeight.BOLD, FontPosture.ITALIC, 15);

        String begin = "";
        String end = "";
        String timeLeft = "";
        String score = "";
        String isSend = "";
        if(isExam){
            begin = exams.get(index).getBeginTime();
            end = exams.get(index).getEndTime();
            score = exams.get(index).getStudentsSend_Score().get(student.getId()) + "";
            if(exams.get(index).getStudentsSend_AddressOrText().get(student.getId()) != null){
                isSend = "send";
            }
            else{
                isSend = "nothing yet";
            }
        }
        else {
            begin = homeWorks.get(index).getBeginTime();
            end = homeWorks.get(index).getEndTime();
            score = homeWorks.get(index).getStudentsSend_Score().get(student.getId()) + "";
            if(homeWorks.get(index).getStudentsSend_AddressOrText().get(student.getId()) != null){
                isSend = "send";
            }
            else{
                isSend = "nothing yet";
            }
        }
        timeLeft = calculateTimeLeft(end);
        LinkedList<HomeWork> homeWorks = clientNetwork.getHomeWorksFromACourse(courseId);
        LinkedList<Exam> exams = clientNetwork.getExamsFromACourse(courseId);

        beginLabel.setText("Begin: " + begin);
        beginLabel.setFont(font1);

        endLabel.setText("End: " + end);
        endLabel.setFont(font1);

        timeLeftLabel.setText("Time left: " + timeLeft);
        timeLeftLabel.setFont(font1);

        scoreLabel.setText("Score: " + score);
        scoreLabel.setFont(font1);

        isSendLabel.setText("Status: " + isSend);
        isSendLabel.setFont(font1);

    }

    public String calculateTimeLeft(String end){
        LocalDateTime localDateTime = LocalDateTime.now();
        String[] s = end.split("-");
        int year = (Integer.parseInt(s[0]) - localDateTime.getYear());
        int month = (Integer.parseInt(s[1]) - localDateTime.getMonthValue());
        int day = (Integer.parseInt(s[2]) - localDateTime.getDayOfMonth());
        int hour = (Integer.parseInt(s[3]) - localDateTime.getHour());
        int minute = (Integer.parseInt(s[4]) - localDateTime.getMinute());

        if(year < 0){
            return "0";
        }
        else if(year > 0){

        }
        else{
            if(month < 0){
                return "0";
            }
            else if(month > 0){

            }
            else{
                if(day < 0){
                    return  "0";
                }
                else if(day > 0){

                }
                else {
                    if(hour < 0){
                        return "0";
                    }
                    else if(hour > 0){

                    }
                    else{
                        if(minute <= 0){
                            return "0";
                        }
                    }
                }
            }
        }

        String result = year + "-" +
                 month + "-" +
                 day + "-" +
                 hour + "-" +
                 minute;
        return result;
    }

    public void submitButtonFunction(ActionEvent e) {
        String answer = textArea.getText();
        if(answer == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("text area is empty");
            alert.showAndWait();
            return;
        }
        else if(answer.equals("")){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("text area is empty");
            alert.showAndWait();
            return;
        }
        if(isExam){
            if(exams.get(index).getStudentsSend_AddressOrText().containsKey(student.getId())){
                exams.get(index).getStudentsSend_AddressOrText().replace(student.getId(), answer);
                LocalDateTime localDateTime = LocalDateTime.now();
                String t = localDateTime.getYear() + "-" + localDateTime.getMonthValue() + "-"
                        + localDateTime.getDayOfMonth() + "-" + localDateTime.getHour() + "-" +
                        localDateTime.getMinute();
                exams.get(index).getStudentsSend_Time().replace(student.getId(), t);
            }
            else{
                exams.get(index).getStudentsSend_AddressOrText().put(student.getId(), answer);
                exams.get(index).getStudentsSend_Score().put(student.getId(), -1.0);
                LocalDateTime localDateTime = LocalDateTime.now();
                String t = localDateTime.getYear() + "-" + localDateTime.getMonthValue() + "-"
                        + localDateTime.getDayOfMonth() + "-" + localDateTime.getHour() + "-" +
                        localDateTime.getMinute();
                exams.get(index).getStudentsSend_Time().put(student.getId(), t);
            }
        }
        else{
            if(homeWorks.get(index).getStudentsSend_AddressOrText().containsKey(student.getId())){
                homeWorks.get(index).getStudentsSend_AddressOrText().replace(student.getId(), answer);
                LocalDateTime localDateTime = LocalDateTime.now();
                String t = localDateTime.getYear() + "-" + localDateTime.getMonthValue() + "-"
                        + localDateTime.getDayOfMonth() + "-" + localDateTime.getHour() + "-" +
                        localDateTime.getMinute();
                homeWorks.get(index).getStudentsSend_Time().replace(student.getId(), t);
            }
            else{
                homeWorks.get(index).getStudentsSend_AddressOrText().put(student.getId(), answer);
                homeWorks.get(index).getStudentsSend_Score().put(student.getId(), -1.0);
                LocalDateTime localDateTime = LocalDateTime.now();
                String t = localDateTime.getYear() + "-" + localDateTime.getMonthValue() + "-"
                        + localDateTime.getDayOfMonth() + "-" + localDateTime.getHour() + "-" +
                        localDateTime.getMinute();
                homeWorks.get(index).getStudentsSend_Time().put(student.getId(), t);
            }
        }

        Gson gson = new Gson();
        String hs = gson.toJson(homeWorks);
        String es = gson.toJson(exams);
        clientNetwork.changeAnswerForHomeWorkOrExamForAStudent(courseId, es, hs, isExam);
        ShowFunction();
    }

    public void downloadButtonFunction(ActionEvent e) {
        if(isExam){
            File fileClicked = new File(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.publicURL) + student.getStudentNumber() + "/" + exams.get(index).getAddressOrTextQuestionInServer());
            if(fileClicked.exists()){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText("go to this folder: " + ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.publicURL) +  student.getStudentNumber() + "/" + exams.get(index).getAddressOrTextQuestionInServer());
                alert.showAndWait();
                PublicMethods.openADesktopFolder(fileClicked);
                return;
            }
            clientNetwork.sendFileFromServer(exams.get(index).getAddressOrTextQuestionInServer() , student.getStudentNumber());
        }
        else{
            File fileClicked = new File(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.publicURL) + student.getStudentNumber() + "/" + homeWorks.get(index).getAddressOrTextQuestionInServer());
            if(fileClicked.exists()){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText("go to this folder: " + ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.publicURL) + student.getStudentNumber() + "/" + homeWorks.get(index).getAddressOrTextQuestionInServer());
                alert.showAndWait();
                PublicMethods.openADesktopFolder(fileClicked);
                return;
            }
            clientNetwork.sendFileFromServer(homeWorks.get(index).getAddressOrTextQuestionInServer() , student.getStudentNumber());
        }
    }

    public void buttonAnswerFunction(ActionEvent e) {
        if(isExam){
            File fileClicked = new File(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.publicURL) + student.getStudentNumber() + "/" + exams.get(index).getAddressOrTextAnswerInServer());
            if(fileClicked.exists()){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText("go to this folder: " + ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.publicURL) + student.getStudentNumber() + "/" + exams.get(index).getAddressOrTextAnswerInServer());
                alert.showAndWait();
                PublicMethods.openADesktopFolder(fileClicked);
                return;
            }
            clientNetwork.sendFileFromServer(exams.get(index).getAddressOrTextAnswerInServer() , student.getStudentNumber());
        }
        else{
            File fileClicked = new File(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.publicURL) + student.getStudentNumber() + "/" + homeWorks.get(index).getAddressOrTextAnswerInServer());
            if(fileClicked.exists()){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText("go to this folder: " + ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.publicURL) + student.getStudentNumber() + "/" + homeWorks.get(index).getAddressOrTextAnswerInServer());
                alert.showAndWait();
                PublicMethods.openADesktopFolder(fileClicked);
                return;
            }
            clientNetwork.sendFileFromServer(homeWorks.get(index).getAddressOrTextAnswerInServer() , student.getStudentNumber());
        }
    }



    public void backButtonFunction(ActionEvent e) {
        timelineForConnected.stop();

        try{
            stage = ((Stage)(((Scene)LogOutButton.getScene()).getWindow()));
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Fxml\\Student\\CW\\Student\\Student_CW_CoursePage.fxml"));
            Parent root = loader.load();
            Student_CW_CoursePage Student_CW_CoursePage = loader.getController();
            Student_CW_CoursePage.setStudent(student, clientNetwork, courseId);
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
