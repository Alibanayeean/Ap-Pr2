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
import java.util.LinkedList;
import java.util.ResourceBundle;


public class Teacher_CW_AddNewExamOrHw implements Initializable {

    public static final Logger log = LogManager.getLogger(controller.TeacherController.EducationalAssistant.EducationalAssistant_AddNewUser.class);


    Teacher teacher;
    ClientNetwork clientNetwork;



    @FXML
    ColorPicker colorPicker;

    @FXML
    Button LogOutButton;

    @FXML
    Label timeShowLabel;

    @FXML
    Button HomeButton;

    @FXML
    Button AddButton;

    @FXML
    ChoiceBox typeChoiceBox;


    @FXML
    TextField nameTextField;

    @FXML
    TextField beginTextField;

    @FXML
    TextField endTimeTextField;


    @FXML
    TextArea descriptionTextArea;

    @FXML
    Button fileButton;

    Timeline timelineForConnected;

    @FXML
    Button reconnectionButton;

    @FXML
    ImageView connectionStatus;

    @FXML
    Button backButton;

    String courseId;
    boolean isExam;
    LinkedList<Exam> exams;
    LinkedList<HomeWork> homeWorks;



    Stage stage;

    File file = null;
    String address = "";

    @FXML
    void ChangingColor(ActionEvent event) {
        Color color = colorPicker.getValue();
        timeShowLabel.setBackground(new Background(new BackgroundFill(color, null, null)));
        HomeButton.setBackground(new Background(new BackgroundFill(color, null, null)));
        backButton.setBackground(new Background(new BackgroundFill(color, null, null)));
        AddButton.setBackground(new Background(new BackgroundFill(color, null, null)));
        fileButton.setBackground(new Background(new BackgroundFill(color, null, null)));
        teacher.color = colorPicker.getValue();
        String s = "#" + Integer.toHexString(color.hashCode());
        clientNetwork.changeColorForTeacher(teacher.getUsername(), s);


        log.info("change background");



    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        PublicMethods.initImageOfLogOutButton(LogOutButton);

        PublicMethods.initImageOfHomeButton(HomeButton);

        PublicMethods.initImageOfBackButton(backButton);

        PublicMethods.initClock(timeShowLabel);


        String[] choices_Grade = {"Text" , "File"};
        typeChoiceBox.getItems().addAll(choices_Grade);

        fileButton.setVisible(false);


        typeChoiceBox.setOnAction(event -> {
            fileButton.setVisible(true);
        });

    }

    public void setTeacher(Teacher teacher, ClientNetwork clientNetwork, String courseId, boolean isExam,  LinkedList<Exam> exams, LinkedList<HomeWork> homeWorks){
        this.clientNetwork = clientNetwork;
        this.teacher = teacher;
        this.courseId = courseId;
        this.isExam = isExam;
        this.exams = exams;
        this.homeWorks = homeWorks;

        Color color = teacher.color;
        timeShowLabel.setBackground(new Background(new BackgroundFill(color, null, null)));
        backButton.setBackground(new Background(new BackgroundFill(color, null, null)));
        HomeButton.setBackground(new Background(new BackgroundFill(color, null, null)));
        AddButton.setBackground(new Background(new BackgroundFill(color, null, null)));
        fileButton.setBackground(new Background(new BackgroundFill(color, null, null)));

        colorPicker.setValue(color);

        timelineForConnected = PublicMethods.reconnectionTimeline(reconnectionButton, clientNetwork, connectionStatus);
    }

    public void AddFunction(ActionEvent e) {

        String stringChooseGrade = getChoiceBoxResult();

        if(stringChooseGrade.equals("")){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Incorrect");
            alert.setHeaderText("Choose one of types!!");
            log.error("choose one of types!!");

            alert.showAndWait();
            return;
        }


        if(nameTextField.getText() == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Incorrect");
            alert.setHeaderText("Name field is empty");
            log.error("Name field is empty!!");

            alert.showAndWait();
            return;
        }
        else if(nameTextField.getText().equals("")){

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Incorrect");
            alert.setHeaderText("Name field is empty");
            log.error("Name field is empty!!");

            alert.showAndWait();
            return;
        }

        if(beginTextField.getText() == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Incorrect");
            alert.setHeaderText("Begin time field is empty");
            log.error("Begin time field is empty!!");

            alert.showAndWait();
            return;
        }
        else if(beginTextField.getText().equals("")){

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Incorrect");
            alert.setHeaderText("Begin time field is empty");
            log.error("Begin time field is empty!!");

            alert.showAndWait();
            return;
        }
        else if(!checkValidTimeInput(beginTextField.getText())){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Incorrect");
            alert.setHeaderText("Invalid format begin time.");
            log.error("Invalid format begin time.");

            alert.showAndWait();
            return;
        }

        if(endTimeTextField.getText() == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Incorrect");
            alert.setHeaderText("End time field is empty");
            log.error("End time field is empty!!");

            alert.showAndWait();
            return;
        }
        else if(endTimeTextField.getText().equals("")){

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Incorrect");
            alert.setHeaderText("End time field is empty");
            log.error("End time field is empty!!");

            alert.showAndWait();
            return;
        }
        else if(!checkValidTimeInput(endTimeTextField.getText())){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Incorrect");
            alert.setHeaderText("Invalid format end time.");
            log.error("Invalid format end time.");

            alert.showAndWait();
            return;
        }

        if(!checkEndTimeLaterThanBeginTime(beginTextField.getText(), endTimeTextField.getText())){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Incorrect");
            alert.setHeaderText("End time should be later than begin time.");
            log.error("End time should be later than begin time.");

            alert.showAndWait();
            return;
        }

        if(file == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Incorrect");
            alert.setHeaderText("Choose one file");
            log.error("Choose one file");

            alert.showAndWait();
            return;
        }

        HomeWorkOrExamType homeWorkOrExamType = null;
        boolean doseHaveFile;
        if(getChoiceBoxResult().toLowerCase().equals("file")){
            homeWorkOrExamType = HomeWorkOrExamType.FILE;
            doseHaveFile = true;
        }
        else if (getChoiceBoxResult().toLowerCase().equals("text")){
            homeWorkOrExamType = HomeWorkOrExamType.TEXT;
            doseHaveFile = false;
        }
        else{
            return;
        }

        if(isExam){
            String json = (new Gson()).toJson(new Exam(nameTextField.getText(), beginTextField.getText(), endTimeTextField.getText(), address, homeWorkOrExamType, descriptionTextArea.getText()));
            clientNetwork.addNewFileInCwForExamOrHw(courseId, isExam, teacher.getUsername(), doseHaveFile, address, json);
            try {
                Thread.sleep(500);
            } catch (InterruptedException interruptedException) {
                interruptedException.printStackTrace();
            }
        }
        else{
            String json = (new Gson()).toJson(new HomeWork(nameTextField.getText(), beginTextField.getText(), endTimeTextField.getText(), address, homeWorkOrExamType, descriptionTextArea.getText()));
            clientNetwork.addNewFileInCwForExamOrHw(courseId, isExam,  teacher.getUsername(), doseHaveFile, address, json);
            try {
                Thread.sleep(500);
            } catch (InterruptedException interruptedException) {
                interruptedException.printStackTrace();
            }
        }

        backButtonFunction(e);
    }

    public String getChoiceBoxResult(){
        String string = (String) typeChoiceBox.getValue();
        if(string == null){
            return "";
        }
        return string ;
    }

    public boolean checkValidTimeInput(String time){
        if(time.length() != 16){
            return false;
        }
        for (int i = 0; i < time.length(); i++) {
            if(i == 0 || i == 1 || i == 2 || i == 3 || i == 5 || i == 6 || i == 8 || i == 9 || i == 11 || i == 12 || i == 14 || i == 15){
                if(!(time.charAt(i) >= '0' &  time.charAt(i) <= '9')){
                    return false;
                }
            }
            else{
                if(time.charAt(i) != '-'){
                  return false;
                }
            }
        }

        return true;
    }

    public boolean checkEndTimeLaterThanBeginTime(String begin, String end){
        String[] be = begin.split("-");
        String[] en = end.split("-");
        for (int i = 0; i < be.length; i++) {
            if(Integer.parseInt(be[i]) > Integer.parseInt(en[i])){
                return false;
            }
            else if(Integer.parseInt(be[i]) < Integer.parseInt(en[i])){
                return true;
            }
        }

        return false;
    }


    public void selectFile(){
        stage = ((Stage)(((Scene)timeShowLabel.getScene()).getWindow()));
        FileChooser fil_chooser = new FileChooser();

        fil_chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image file", "*.pdf"));
        File file = fil_chooser.showOpenDialog(stage);

        if (file != null) {
            String ss = file.getPath();
            int index = ss.lastIndexOf('.');
            String address = ss.substring(index);
            String time = LocalDateTime.now().toString();
            time = time.replace(':', '-');
            time = time.replace('.', '-');
            if(isExam){
                address = "Ex-" + time + teacher.getUsername().hashCode() + address;
            }
            else{
                address = "Hw-" + time + teacher.getUsername().hashCode() + address;
            }


            File file1 = new File(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.publicURL) + teacher.getUsername() + "/"  +  address);
            try {
                Files.copy(Paths.get(file.getPath()), Paths.get(file1.getPath()));
            } catch (IOException eo) {
                return;
            }

            if(this.file != null){
                if(this.file.exists()){
                    this.file.delete();
                    this.file = null;
                    clientNetwork.removeAFileFromServerForCw(this.address);
                }
            }

            this.address = address;
            this.file = file;
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

