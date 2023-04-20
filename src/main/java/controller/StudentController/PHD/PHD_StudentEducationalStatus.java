package controller.StudentController.PHD;

import back.course.Course;
import back.persons.Student;
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
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
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

public class PHD_StudentEducationalStatus implements Initializable {

    public static final Logger log = LogManager.getLogger(PHD_StudentEducationalStatus.class);


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
    Pane pane;


    @FXML
    Label AverageScoreLabel;

    @FXML
    Label NumOfCoursesPassLabel;

    @FXML
    Label NameLabel;

    @FXML
    Button reconnectionButton;

    @FXML
    ImageView connectionStatus;


    Timeline timeline;

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
    }

    public void setStudent(Student student, ClientNetwork clientNetwork){
        this.clientNetwork = clientNetwork;
        this.student = student;

        Color color = student.color;
        MenuBar.setBackground(new Background(new BackgroundFill(color, null, null)));
        timeShowLabel.setBackground(new Background(new BackgroundFill(color, null, null)));
        HomeButton.setBackground(new Background(new BackgroundFill(color, null, null)));
        colorPicker.setValue(color);

        NameLabel.setText("Name: " + student.getFirstname() + " " + student.getLastname());
        NumOfCoursesPassLabel.setText("Number of Courses Pass: " + student.getNumberOfCoursesPass());
        AverageScoreLabel.setText("Average score:  " + student.getAverage_score());


        timeline = new Timeline(new KeyFrame(Duration.seconds(2), new EventHandler<ActionEvent>(){
            int timesForFirst = 0;
            int timesForSecond = 0;
            int size = 0;

            @Override
            public void handle(ActionEvent event) {

                LinkedList<Course> c = clientNetwork.ShowPastCoursesForAStudent(student.getId());
                if(size != c.size()){
                    PrintForPane(c);
                    size = c.size();
                }
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


    public void PrintForPane(LinkedList<Course> courses){

        Font font = Font.font("System", FontWeight.NORMAL, FontPosture.REGULAR, 20);
        pane.getChildren().clear();
        pane.setPrefHeight(courses.size() * 80 + 30);

        for (int i = 0; i < courses.size(); i++) {

            Label label1 = new Label(courses.get(i).getName());
            label1.prefHeight(142);
            label1.prefWidth(57);
            label1.setLayoutX(14);
            label1.setLayoutY(23 + 80 * i);
            label1.setFont(font);
            pane.getChildren().add(label1);

            Label label2 = new Label(courses.get(i).getCollege().name());
            label2.prefHeight(142);
            label2.prefWidth(57);
            label2.setLayoutX(226);
            label2.setLayoutY(23 + 80 * i);
            label2.setFont(font);
            pane.getChildren().add(label2);

            Label label3 = new Label(courses.get(i).getWeight() + "");
            label3.prefHeight(142);
            label3.prefWidth(57);
            label3.setLayoutX(536);
            label3.setLayoutY(23 + 80 * i);
            label3.setFont(font);
            pane.getChildren().add(label3);

            Label label4 = new Label(courses.get(i).getId() + "");
            label4.prefHeight(142);
            label4.prefWidth(57);
            label4.setLayoutX(760);
            label4.setLayoutY(23 + 80 * i);
            label4.setFont(font);
            pane.getChildren().add(label4);


            double score = clientNetwork.getScoreACourseFromAStudent(student.getId() , courses.get(i).getId());
            if(score == -1.0){

            }
            Label label5 = new Label(score + "");
            label5.prefHeight(142);
            label5.prefWidth(57);
            label5.setLayoutX(970);
            label5.setLayoutY(23 + 80 * i);
            label5.setFont(font);
            pane.getChildren().add(label5);


            String statusCourse = clientNetwork.getStatusACourseFromAStudent(student.getId(), courses.get(i).getId());
            Label label6 = new Label(statusCourse);
            label6.prefHeight(142);
            label6.prefWidth(57);
            label6.setLayoutX(1150);
            label6.setLayoutY(23 + 80 * i);
            label6.setFont(font);
            pane.getChildren().add(label6);

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
