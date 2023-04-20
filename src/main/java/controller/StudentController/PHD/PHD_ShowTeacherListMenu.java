package controller.StudentController.PHD;
import back.enums.TeacherDegree;
import back.persons.Student;
import back.persons.Teacher;
import clientNetwork.ClientNetwork;
import config.ConfigIdentifier;
import config.ReadPropertyFile;
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
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;
import org.apache.log4j.LogManager;

public class PHD_ShowTeacherListMenu implements Initializable{
    public static final Logger log = LogManager.getLogger(PHD_ShowTeacherListMenu.class);


    Student student;
    ClientNetwork clientNetwork;

    @FXML
    javafx.scene.control.MenuBar MenuBar;

    @FXML
    ColorPicker colorPicker;

    @FXML
    Button LogOutButton;

    @FXML
    Label timeShowLabel;

    @FXML
    Button HomeButton;

    @FXML
    TextField LastnameTextField;

    @FXML
    ChoiceBox CollegeNameField;

    @FXML
    ChoiceBox TeacherDegreeField;

    @FXML
    Pane pane;

    Timeline timeline;

    @FXML
    Button reconnectionButton;

    @FXML
    ImageView connectionStatus;

    Stage stage;
    @FXML
    void ChangingColor(ActionEvent event) {
        Color color = colorPicker.getValue();
        MenuBar.setBackground(new Background(new BackgroundFill(color, null, null)));
        timeShowLabel.setBackground(new Background(new BackgroundFill(color, null, null)));
        HomeButton.setBackground(new Background(new BackgroundFill(color, null, null)));

        student.color = colorPicker.getValue();
        String s = "#" + Integer.toHexString(color.hashCode());
        clientNetwork.changeColorForStudent(student.getStudentNumber(), s);
        
        log.info("change background");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        PublicMethods.initImageOfLogOutButton(LogOutButton);

        PublicMethods.initImageOfHomeButton(HomeButton);

        PublicMethods.initClock(timeShowLabel);

        String[] choices_TeacherDegreeField = {"--", "FullProfessor", "AssistantProfessor", "AssociateProfessor"};
        TeacherDegreeField.getItems().addAll(choices_TeacherDegreeField);

        String[] choices_CollegeNameField = {"--", "EE", "CE", "CS", "Math", "Physics", "Chemistry", "Mechanic", "Public"};
        CollegeNameField.getItems().addAll(choices_CollegeNameField);


    }

    public void setStudent(Student student, ClientNetwork clientNetwork){
        this.clientNetwork = clientNetwork;
        this.student = student;

        Color color = student.color;
        MenuBar.setBackground(new Background(new BackgroundFill(color, null, null)));
        timeShowLabel.setBackground(new Background(new BackgroundFill(color, null, null)));
        HomeButton.setBackground(new Background(new BackgroundFill(color, null, null)));
        colorPicker.setValue(color);

        LinkedList<Teacher> teachers = clientNetwork.getAllTeachers();

        PrintForPane(teachers);

        CollegeNameField.setOnAction(event -> {
            ShowButtonFunction();
        });
        TeacherDegreeField.setOnAction(event -> {
            ShowButtonFunction();
        });

        LastnameTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            ShowButtonFunction();
        });


        timeline = new Timeline(new KeyFrame(Duration.seconds(0.1), new EventHandler<ActionEvent>(){

            int timesForFirst = 0;
            int timesForSecond = 0;

            @Override
            public void handle(ActionEvent event) {
                ShowButtonFunction();
                if(clientNetwork.isConnected && timesForFirst == 0){
                    connectionStatus.setVisible(true);
                    reconnectionButton.setVisible(false);
                    File file = new File(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.publicURL) + "connected.png");
                    Image image2 = new Image(file.toURI().toString());
                    connectionStatus.setImage(image2);
                    timesForFirst++;
                    timesForSecond = 0;
                }
                else if(!clientNetwork.isConnected && timesForSecond == 0){
                    reconnectionButton.setVisible(true);
                    connectionStatus.setVisible(false);
                    File file = new File(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.publicURL) + "reconnectionButton.png");
                    Image image2 = new Image(file.toURI().toString());
                    reconnectionButton.setGraphic(new ImageView(image2));
                    timesForSecond++;
                    timesForFirst = 0;

                    reconnectionButton.setOnAction(e -> {
                        clientNetwork.attemptToMakeNewConnection();
                        if(clientNetwork.isConnected){
                            reconnectionButton.setVisible(false);
                            File file1 = new File(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.publicURL) + "connected.png");
                            Image image3 = new Image(file1.toURI().toString());
                            connectionStatus.setImage(image3);
                        }
                    });

                }
            }

        }));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
        

    }

    @FXML
    void ShowButtonFunction(){
        String stringChooseCollegeName = chooseCollegeName();
        String stringChooseTeacherDegreeField = chooseTeacherDegreeField();


        if(stringChooseCollegeName == null){
            stringChooseCollegeName = "";
        }
        else if(stringChooseCollegeName.equals("--")){
            stringChooseCollegeName = "";
        }

        TeacherDegree teacherDegree = null;
        if(stringChooseTeacherDegreeField == null){
            stringChooseTeacherDegreeField = "";
        }
        else if(stringChooseTeacherDegreeField.equals("--")){
            stringChooseTeacherDegreeField = "";
        }
        else if(stringChooseTeacherDegreeField.equals("FullProfessor")){
            teacherDegree = TeacherDegree.FullProfessor;
        }
        else if(stringChooseTeacherDegreeField.equals("AssistantProfessor")){
            teacherDegree = TeacherDegree.AssistantProfessor;
        }
        else if(stringChooseTeacherDegreeField.equals("AssociateProfessor")){
            teacherDegree = TeacherDegree.AssociateProfessor;
        }


        LinkedList<Teacher> ForReturn = new LinkedList<>();
        ForReturn = clientNetwork.ShowTeachers(LastnameTextField.getText(), stringChooseCollegeName ,teacherDegree + "");
        PrintForPane(ForReturn);

    }

    public String  chooseTeacherDegreeField (){
        String stringChooseTeacherDegreeField = (String) TeacherDegreeField.getValue();
        return stringChooseTeacherDegreeField ;
    }

    public String  chooseCollegeName (){
        String stringCollegeName = (String) CollegeNameField.getValue();
        return stringCollegeName ;
    }

    public void PrintForPane(LinkedList<Teacher> teachers){

        Font font = Font.font("System", FontWeight.NORMAL, FontPosture.REGULAR, 20);
        pane.getChildren().clear();
        pane.setPrefHeight(teachers.size() * 80 + 30);

        for (int i = 0; i < teachers.size(); i++) {

            Label label5 = new Label(teachers.get(i).getFirstname() + "");
            label5.prefHeight(142);
            label5.prefWidth(57);
            label5.setLayoutX(14);
            label5.setLayoutY(23 + 80 * i);
            label5.setFont(font);
            pane.getChildren().add(label5);

            Label label1 = new Label(teachers.get(i).getLastname());
            label1.prefHeight(142);
            label1.prefWidth(57);
            label1.setLayoutX(217);
            label1.setLayoutY(23 + 80 * i);
            label1.setFont(font);
            pane.getChildren().add(label1);

            Label label2 = new Label(teachers.get(i).getCollege().name());
            label2.prefHeight(142);
            label2.prefWidth(57);
            label2.setLayoutX(536);
            label2.setLayoutY(23 + 80 * i);
            label2.setFont(font);
            pane.getChildren().add(label2);

            Label label3 = new Label(teachers.get(i).getTeacherDegree() + "");
            label3.prefHeight(142);
            label3.prefWidth(57);
            label3.setLayoutX(921);
            label3.setLayoutY(23 + 80 * i);
            label3.setFont(font);
            pane.getChildren().add(label3);


            Label label4 = new Label(teachers.get(i).getRoomNumber() + "");
            label4.prefHeight(142);
            label4.prefWidth(57);
            label4.setLayoutX(1169);
            label4.setLayoutY(23 + 80 * i);
            label4.setFont(font);
            pane.getChildren().add(label4);

        }
    }


    public void HomeButtonFunction(ActionEvent e) {
        
        timeline.stop();
        try{
            stage = ((Stage)(((Scene)timeShowLabel.getScene()).getWindow()));
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Fxml\\Student\\PHD\\PHDFirstPage.fxml"));
            Parent root = loader.load();
            PHDFirstPage PHDFirstPage = loader.getController();
            PHDFirstPage.setStudent(student, clientNetwork);
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();


        }
        catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    public void LogOutFunction(ActionEvent event) {
        
        timeline.stop();

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
            clientNetwork.changeLastLoginStudent(student.getId(), localDateTime.getSecond(), localDateTime.getMinute(), localDateTime.getHour(), localDateTime.getDayOfMonth(), localDateTime.getMonthValue(), localDateTime.getYear());

        }
        catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    public void CoursesListMenuFunction(ActionEvent event) {
        
        timeline.stop();

        try{
            stage = ((Stage)(((Scene)timeShowLabel.getScene()).getWindow()));
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Fxml\\Student\\PHD\\PHD_ShowCourseListMenu.fxml"));
            Parent root = loader.load();
            PHD_ShowCourseListMenu PHD_ShowCourseListMenu = loader.getController();
            PHD_ShowCourseListMenu.setStudent(student, clientNetwork);
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
        catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    public void TeacherListMenuFunction(ActionEvent event) {
        
        timeline.stop();

        try{
            stage = ((Stage)(((Scene)timeShowLabel.getScene()).getWindow()));
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Fxml\\Student\\PHD\\PHD_ShowTeacherListMenu.fxml"));
            Parent root = loader.load();
            PHD_ShowTeacherListMenu PHD_ShowTeacherListMenu = loader.getController();
            PHD_ShowTeacherListMenu.setStudent(student, clientNetwork);
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
        catch (IOException ioException) {
            ioException.printStackTrace();
        }

    }

    public void ScheduleFunction(ActionEvent event) {
        
        timeline.stop();

        try{
            stage = ((Stage)(((Scene)timeShowLabel.getScene()).getWindow()));
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Fxml\\Student\\PHD\\PHD_ShowWeeklySchedule.fxml"));
            Parent root = loader.load();
            PHD_ShowWeeklySchedule PHD_ShowWeeklySchedule = loader.getController();
            PHD_ShowWeeklySchedule.setStudent(student, clientNetwork);
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
        catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    public void ProfileFunction(ActionEvent event) {
        
        timeline.stop();

        try{
            stage = ((Stage)(((Scene)timeShowLabel.getScene()).getWindow()));
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Fxml\\Student\\PHD\\PHD_ShowProfile.fxml"));
            Parent root = loader.load();
            PHD_ShowProfile PHD_ShowProfile = loader.getController();
            PHD_ShowProfile.setStudent(student, clientNetwork);
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
        catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    public void ExamFunction(ActionEvent event) {
        
        timeline.stop();

        try{
            stage = ((Stage)(((Scene)timeShowLabel.getScene()).getWindow()));
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Fxml\\Student\\PHD\\PHD_ShowExams.fxml"));
            Parent root = loader.load();
            PHD_ShowExams PHD_ShowExams = loader.getController();
            PHD_ShowExams.setStudent(student, clientNetwork);
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
        catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    public void certificateStudentFunction(ActionEvent event) {
        
        timeline.stop();

        try{
            stage = ((Stage)(((Scene)timeShowLabel.getScene()).getWindow()));
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Fxml\\Student\\PHD\\PHD_CertificateStudent.fxml"));
            Parent root = loader.load();
            PHD_CertificateStudent PHD_CertificateStudent = loader.getController();
            PHD_CertificateStudent.setStudent(student, clientNetwork);
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
        catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    public void WithdrawalFromEducationFunction(ActionEvent event) {
        
        timeline.stop();

        try{
            stage = ((Stage)(((Scene)timeShowLabel.getScene()).getWindow()));
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Fxml\\Student\\PHD\\PHD_withdrawalFromEducation.fxml"));
            Parent root = loader.load();
            PHD_withdrawalFromEducation PHD_withdrawalFromEducation = loader.getController();
            PHD_withdrawalFromEducation.setStudent(student, clientNetwork);
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
        catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    public void ThesisDefenceFunction(ActionEvent event) {
        
        timeline.stop();

        try{
            stage = ((Stage)(((Scene)timeShowLabel.getScene()).getWindow()));
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Fxml\\Student\\PHD\\PHD_ThesisDefense.fxml"));
            Parent root = loader.load();
            PHD_ThesisDefense PHD_ThesisDefense = loader.getController();
            PHD_ThesisDefense.setStudent(student, clientNetwork);
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
        catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    public void temporaryScoresFunction(ActionEvent event) {
        
        timeline.stop();

        try{
            stage = ((Stage)(((Scene)timeShowLabel.getScene()).getWindow()));
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Fxml\\Student\\PHD\\PHD_temporaryScores.fxml"));
            Parent root = loader.load();
            PHD_temporaryScores PHD_temporaryScores = loader.getController();
            PHD_temporaryScores.setStudent(student, clientNetwork);
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
        catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    public void StatusFunction(ActionEvent event) {
        
        timeline.stop();

        try{
            stage = ((Stage)(((Scene)timeShowLabel.getScene()).getWindow()));
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Fxml\\Student\\PHD\\PHD_StudentEducationalStatus.fxml"));
            Parent root = loader.load();
            PHD_StudentEducationalStatus PHD_StudentEducationalStatus = loader.getController();
            PHD_StudentEducationalStatus.setStudent(student, clientNetwork);
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
        catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
}
