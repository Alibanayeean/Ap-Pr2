package controller.StudentController.CW.TA;


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
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.*;

import org.apache.log4j.Logger;
import org.apache.log4j.LogManager;


public class TA_CW_CoursePage_SendText implements Initializable{
    public static final Logger log = LogManager.getLogger(TA_CW_CoursePage_SendText.class);


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
    ChoiceBox<String> choiceBoxFileType;

    @FXML
    TextArea textAreaForAnswer;

    @FXML
    Button submitForAnswer;


    @FXML
    void ChangingColor(ActionEvent event) {


        Color color = colorPicker.getValue();
        HomeButton.setBackground(new Background(new BackgroundFill(color, null, null)));
        backButton.setBackground(new Background(new BackgroundFill(color, null, null)));
        downloadButton.setBackground(new Background(new BackgroundFill(color, null, null)));
        submitButton.setBackground(new Background(new BackgroundFill(color, null, null)));

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

        String[] s = {"File", "Text"};
        choiceBoxFileType.getItems().addAll(s);
        choiceBoxFileType.setOnAction(event -> {
            if(getChoiceBoxResult().toLowerCase().equals("file")){
                submitForAnswer.setVisible(true);
            }
            else if(getChoiceBoxResult().toLowerCase().equals("text")){
                submitForAnswer.setVisible(true);
                textAreaForAnswer.setVisible(true);
            }
        });

        submitForAnswer.setVisible(false);
        textAreaForAnswer.setVisible(false);
    }

    public void setStudent(Student student, ClientNetwork clientNetwork, String courseId, boolean isExam, LinkedList<Exam> exams, LinkedList<HomeWork> homeWorks, int index){
        this.clientNetwork = clientNetwork;
        this.student = student;
        this.courseId = courseId;
        this.isExam = isExam;
        this.homeWorks = homeWorks;
        this.exams = exams;
        this.index = index;

        Color color = student.getColor();

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
                String name = clientNetwork.getNameOfAStudentFromId(key);
                if(name.equals("")){
                    continue;
                }
                String studentsNumber = clientNetwork.getStudentsNumberOfAStudentFromId(key);
                if(studentsNumber.equals("")){
                    continue;
                }
                String time = exams.get(index).getStudentsSend_Time().get(key);
                HelpPrint(answer, score, time, counter, key);
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
                String name = clientNetwork.getNameOfAStudentFromId(key);
                if(name.equals("")){
                    continue;
                }
                String studentsNumber = clientNetwork.getStudentsNumberOfAStudentFromId(key);
                if(studentsNumber.equals("")){
                    continue;
                }
                String time = homeWorks.get(index).getStudentsSend_Time().get(key);
                HelpPrint(answer, score, time, counter, key);
                counter++;
            }
        }

    }

    public void HelpPrint(String answer, double score, String time, int counter, int studentId){
        Font font = Font.font("System", FontWeight.NORMAL, FontPosture.REGULAR, 20);

        if(isExam){
            if(exams.get(index).getHomeWorkOrExamType() == HomeWorkOrExamType.TEXT){
                Label label1 = new Label("*");
                label1.prefHeight(142);
                label1.prefWidth(57);
                label1.setLayoutX(5);
                label1.setLayoutY(23 + 200 * counter);
                label1.setFont(font);
                pane.getChildren().add(label1);

                Label label2 = new Label("*");
                label2.prefHeight(142);
                label2.prefWidth(57);
                label2.setLayoutX(240);
                label2.setLayoutY(23 + 200 * counter);
                label2.setFont(font);
                pane.getChildren().add(label2);

                TextArea textArea = new TextArea(answer);
                textArea.setEditable(false);
                textArea.setLayoutX(573);
                textArea.setLayoutY(23 + 200 * counter);
                textArea.setPrefWidth(200);
                textArea.setPrefHeight(100);
                pane.getChildren().add(textArea);


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
            if(homeWorks.get(index).getHomeWorkOrExamType() == HomeWorkOrExamType.TEXT){
                Label label1 = new Label("*");
                label1.prefHeight(142);
                label1.prefWidth(57);
                label1.setLayoutX(5);
                label1.setLayoutY(23 + 200 * counter);
                label1.setFont(font);
                pane.getChildren().add(label1);

                Label label2 = new Label("*");
                label2.prefHeight(142);
                label2.prefWidth(57);
                label2.setLayoutX(240);
                label2.setLayoutY(23 + 200 * counter);
                label2.setFont(font);
                pane.getChildren().add(label2);

                TextArea textArea = new TextArea(answer);
                textArea.setEditable(false);
                textArea.setLayoutX(573);
                textArea.setLayoutY(23 + 200 * counter);
                textArea.setPrefWidth(200);
                textArea.setPrefHeight(100);
                pane.getChildren().add(textArea);

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
            }
            String s = (new Gson()).toJson(exams);
            clientNetwork.saveExamsOrHWsForCw(courseId, s, isExam);
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
            }
            String s = (new Gson()).toJson(homeWorks);
            clientNetwork.saveExamsOrHWsForCw(courseId, s, isExam);
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
            File fileClicked = new File(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.publicURL) + student.getStudentNumber() + "/" + exams.get(index).getAddressOrTextQuestionInServer());
            if(fileClicked.exists()){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText("go to this folder: " + ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.publicURL) + student.getStudentNumber() + "/" + exams.get(index).getAddressOrTextQuestionInServer());
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
                alert.setHeaderText("go to this folder: " + ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.publicURL) +  student.getStudentNumber() + "/" + homeWorks.get(index).getAddressOrTextQuestionInServer());
                alert.showAndWait();
                PublicMethods.openADesktopFolder(fileClicked);
                return;
            }
            clientNetwork.sendFileFromServer(homeWorks.get(index).getAddressOrTextQuestionInServer() , student.getStudentNumber());
        }
    }


    public String getChoiceBoxResult(){
        String s = choiceBoxFileType.getValue();
        if(s == null){
            return "";
        }
        return s;
    }

    public void submitForAnswerFunction(ActionEvent e){
        if(getChoiceBoxResult().toLowerCase().equals("text")){
            if(isExam){
                exams.get(index).setAddressOrTextAnswerInServer(textAreaForAnswer.getText());
                exams.get(index).setAnswerUploaded(true);
                exams.get(index).setTypeAnswer(HomeWorkOrExamType.TEXT);

                clientNetwork.saveExamsOrHWsForCw(courseId, (new Gson().toJson(exams)), isExam);
            }
            else{
                homeWorks.get(index).setAddressOrTextAnswerInServer(textAreaForAnswer.getText());
                homeWorks.get(index).setAnswerUploaded(true);
                homeWorks.get(index).setTypeAnswer(HomeWorkOrExamType.TEXT);
                clientNetwork.saveExamsOrHWsForCw(courseId, (new Gson().toJson(homeWorks)), isExam);
            }
        }
        else if (getChoiceBoxResult().toLowerCase().equals("file")){
            String address = openSendAnswerFileFolder();
            if(address.equals("")){
                return;
            }

            if(isExam){
                exams.get(index).setAnswerUploaded(true);
                exams.get(index).setTypeAnswer(HomeWorkOrExamType.FILE);
                exams.get(index).setAddressOrTextAnswerInServer(address);
                clientNetwork.saveExamsOrHWsForCw(courseId, (new Gson().toJson(exams)), isExam);
            }
            else{
                homeWorks.get(index).setAnswerUploaded(true);
                homeWorks.get(index).setTypeAnswer(HomeWorkOrExamType.FILE);
                homeWorks.get(index).setAddressOrTextAnswerInServer(address);
                clientNetwork.saveExamsOrHWsForCw(courseId, (new Gson().toJson(homeWorks)), isExam);
            }
        }
        else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Choice box is empty");
            alert.showAndWait();
        }
    }

    public String openSendAnswerFileFolder() {

        stage = ((Stage)(((Scene)downloadButton.getScene()).getWindow()));
        FileChooser fil_chooser = new FileChooser();

        fil_chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image file", "*.png", "*.jpg", "*.pdf", "*.mp3", "*.mp4"));
        File file = fil_chooser.showOpenDialog(stage);

        if (file != null) {
            String ss = file.getPath();
            int index = ss.lastIndexOf('.');
            String address = ss.substring(index);
            String time = LocalDateTime.now().toString();
            time = time.replace(':', '-');
            time = time.replace('.', '-');
            address = "An-" + time + address;



            File file1 = new File(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.publicURL) +  student.getStudentNumber() + "/"  +  address);
            try {
                Files.copy(Paths.get(file.getPath()), Paths.get(file1.getPath()));
            } catch (IOException eo) {
                return "";
            }

            clientNetwork.sendFileToServer( student.getStudentNumber(), address);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("file send");
            alert.showAndWait();

            return address;
        }

        return "";

    }


    public void backButtonFunction(ActionEvent e) {
        timelineForConnected.stop();

        try{
            stage = ((Stage)(((Scene)LogOutButton.getScene()).getWindow()));
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Fxml\\Student\\CW\\TA\\TA_CW_CoursePage.fxml"));
            Parent root = loader.load();
            TA_CW_CoursePage TA_CW_CoursePage = loader.getController();
            TA_CW_CoursePage.setStudent(student, clientNetwork, courseId);
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
