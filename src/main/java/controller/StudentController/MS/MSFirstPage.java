package controller.StudentController.MS;

import back.persons.Student;
import clientNetwork.ClientNetwork;
import config.ConfigIdentifier;
import config.ReadPropertyFile;
import controller.StudentController.BC.BC_Messages;
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
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
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
import java.util.ResourceBundle;

import org.apache.log4j.Logger;
import org.apache.log4j.LogManager;



public class MSFirstPage implements Initializable {
    public static final Logger log = LogManager.getLogger(MSFirstPage.class);


    Student student;
    ClientNetwork clientNetwork;

    @FXML
    MenuBar MenuBar;

    @FXML
    ColorPicker colorPicker;

    @FXML
    Button LogOutButton;

    @FXML
    Pane paneFilter;

    @FXML
    Label timeShowLabel;

    @FXML
    ImageView imageViewUser;

    @FXML
    Label Status;

    @FXML
    Label Firstname;

    @FXML
    Label Lastname;

    @FXML
    Label emailLabel;

    @FXML
    Button HomeButton;

    @FXML
    Label lastLoginLabel;

    @FXML
    Label label01;

    @FXML
    Label label11;

    @FXML
    Label label21;

    @FXML
    Label label22;

    @FXML
    Label label20;

    @FXML
    Label label00;

    Timeline timelineForConnected;

    @FXML
    Button reconnectionButton;

    @FXML
    ImageView connectionStatus;

    @FXML
    Button messageButton;


    Stage stage;
    @FXML
    void ChangingColor(ActionEvent event) {
        Color color = colorPicker.getValue();
        paneFilter.setBackground(new Background(new BackgroundFill(color, null, null)));
        MenuBar.setBackground(new Background(new BackgroundFill(color, null, null)));
        timeShowLabel.setBackground(new Background(new BackgroundFill(color, null, null)));
        HomeButton.setBackground(new Background(new BackgroundFill(color, null, null)));
        messageButton.setBackground(new Background(new BackgroundFill(color, null, null)));

        Color color1 = new Color(color.getRed(), color.getGreen(), color.getBlue(), 0.5);
        label00.setBackground(new Background(new BackgroundFill(color1, null, null)));
        label01.setBackground(new Background(new BackgroundFill(color1, null, null)));
        label20.setBackground(new Background(new BackgroundFill(color1, null, null)));
        label21.setBackground(new Background(new BackgroundFill(color1, null, null)));
        label22.setBackground(new Background(new BackgroundFill(color1, null, null)));


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


        PublicMethods.initImageOfMessageButton(messageButton);

    }

    public void setStudent(Student student, ClientNetwork clientNetwork){
        this.clientNetwork = clientNetwork;
        this.student = student;


        PublicMethods.initImageOfPerson(clientNetwork, imageViewUser, student.getStudentNumber(), student.getImage());

        Color color = student.color;
        paneFilter.setBackground(new Background(new BackgroundFill(color, null, null)));
        MenuBar.setBackground(new Background(new BackgroundFill(color, null, null)));
        timeShowLabel.setBackground(new Background(new BackgroundFill(color, null, null)));
        HomeButton.setBackground(new Background(new BackgroundFill(color, null, null)));
        messageButton.setBackground(new Background(new BackgroundFill(color, null, null)));

        Status.setText("Student: " + student.getGrade());
        Firstname.setText("First name: " + student.getFirstname());
        Lastname.setText("Last name: " + student.getLastname());
        emailLabel.setText("Email: " + student.getEmail());
        if(student.getTimesLogin() == 1){
            lastLoginLabel.setText("Last login:");
        }
        else{
            lastLoginLabel.setText("Last login:\n" + student.getLastYearLogin() + "/" + student.getLastMonthLogin() + "/" + student.getLastDayLogin() + '\n' + student.getLastHourLogin() + ":" + student.getLastMinuteLogin() + ":" + student.getLastSecondLogin());
        }
        colorPicker.setValue(color);


        Font font = Font.font("System", FontWeight.NORMAL, FontPosture.REGULAR, 20);

        label01.setText(student.getEducationStatus() + "");
        String teacherHelp = clientNetwork.getLastNameHelpTeacherFromId(student.getHelpTeacher());
        if(teacherHelp == null){
            teacherHelp = "---------------";
        }

        label11.setText(teacherHelp);

        if(student.isRegistrationLicense()){
            label21.setText("Is allowed");
            label22.setText(student.getRegistrationTime() + " AM");
        }
        else{
            label21.setText("Not allowed");
            label22.setText("-------------------------");
        }

        label01.setFont(font);
        label11.setFont(font);
        label21.setFont(font);
        label22.setFont(font);

        label01.setAlignment(Pos.CENTER);
        label11.setAlignment(Pos.CENTER);
        label21.setAlignment(Pos.CENTER);
        label22.setAlignment(Pos.CENTER);

        Color color1 = new Color(color.getRed(), color.getGreen(), color.getBlue(), 0.5);
        label00.setBackground(new Background(new BackgroundFill(color1, null, null)));
        label01.setBackground(new Background(new BackgroundFill(color1, null, null)));
        label20.setBackground(new Background(new BackgroundFill(color1, null, null)));
        label21.setBackground(new Background(new BackgroundFill(color1, null, null)));
        label22.setBackground(new Background(new BackgroundFill(color1, null, null)));

        timelineForConnected = PublicMethods.reconnectionTimeline(reconnectionButton, clientNetwork, connectionStatus);

    }



    public void messageButtonFunction(ActionEvent event) {
        timelineForConnected.stop();
        try{
            stage = ((Stage)(((Scene)emailLabel.getScene()).getWindow()));
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Fxml\\Student\\MS\\MS_Messages.fxml"));
            Parent root = loader.load();
            MS_Messages MS_Messages = loader.getController();
            MS_Messages.setStudent(student, clientNetwork);
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
        catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    public void LogOutFunction(ActionEvent event) {
        timelineForConnected.stop();
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
        timelineForConnected.stop();
        try{
            stage = ((Stage)(((Scene)timeShowLabel.getScene()).getWindow()));
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Fxml\\Student\\MS\\MS_ShowCourseListMenu.fxml"));
            Parent root = loader.load();
            MS_ShowCourseListMenu MS_ShowCourseListMenu = loader.getController();
            MS_ShowCourseListMenu.setStudent(student, clientNetwork);
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
        catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    public void TeacherListMenuFunction(ActionEvent event) {
        timelineForConnected.stop();
        try{
            stage = ((Stage)(((Scene)timeShowLabel.getScene()).getWindow()));
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Fxml\\Student\\MS\\MS_ShowTeacherListMenu.fxml"));
            Parent root = loader.load();
            MS_ShowTeacherListMenu MS_ShowTeacherListMenu = loader.getController();
            MS_ShowTeacherListMenu.setStudent(student, clientNetwork);
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
        catch (IOException ioException) {
            ioException.printStackTrace();
        }

    }

    public void ScheduleFunction(ActionEvent event) {
        timelineForConnected.stop();
        try{
            stage = ((Stage)(((Scene)timeShowLabel.getScene()).getWindow()));
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Fxml\\Student\\MS\\MS_ShowWeeklySchedule.fxml"));
            Parent root = loader.load();
            MS_ShowWeeklySchedule MS_ShowWeeklySchedule = loader.getController();
            MS_ShowWeeklySchedule.setStudent(student, clientNetwork);
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
        catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    public void ProfileFunction(ActionEvent event) {
        timelineForConnected.stop();
        try{
            stage = ((Stage)(((Scene)timeShowLabel.getScene()).getWindow()));
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Fxml\\Student\\MS\\MS_ShowProfile.fxml"));
            Parent root = loader.load();
            MS_ShowProfile MS_ShowProfile = loader.getController();
            MS_ShowProfile.setStudent(student, clientNetwork);
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
        catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    public void ExamFunction(ActionEvent event) {
        timelineForConnected.stop();
        try{
            stage = ((Stage)(((Scene)timeShowLabel.getScene()).getWindow()));
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Fxml\\Student\\MS\\MS_ShowExams.fxml"));
            Parent root = loader.load();
            MS_ShowExams MS_ShowExams = loader.getController();
            MS_ShowExams.setStudent(student, clientNetwork);
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
        catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    public void certificateStudentFunction(ActionEvent event) {
        timelineForConnected.stop();
        try{
            stage = ((Stage)(((Scene)timeShowLabel.getScene()).getWindow()));
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Fxml\\Student\\MS\\MS_CertificateStudent.fxml"));
            Parent root = loader.load();
            MS_CertificateStudent MS_CertificateStudent = loader.getController();
            MS_CertificateStudent.setStudent(student, clientNetwork);
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
        catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    public void DormFunction(ActionEvent event) {
        timelineForConnected.stop();
        try{
            stage = ((Stage)(((Scene)timeShowLabel.getScene()).getWindow()));
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Fxml\\Student\\MS\\MS_Dorm.fxml"));
            Parent root = loader.load();
            MS_Dorm MS_Dorm = loader.getController();
            MS_Dorm.setStudent(student, clientNetwork);
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
        catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    public void WithdrawalFromEducationFunction(ActionEvent event) {
        timelineForConnected.stop();
        try{
            stage = ((Stage)(((Scene)timeShowLabel.getScene()).getWindow()));
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Fxml\\Student\\MS\\MS_withdrawalFromEducation.fxml"));
            Parent root = loader.load();
            MS_withdrawalFromEducation MS_withdrawalFromEducation = loader.getController();
            MS_withdrawalFromEducation.setStudent(student, clientNetwork);
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
        catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    public void RecommendationFunction(ActionEvent event) {
        timelineForConnected.stop();
        try{
            stage = ((Stage)(((Scene)timeShowLabel.getScene()).getWindow()));
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Fxml\\Student\\MS\\MS_RequestForRecommendation.fxml"));
            Parent root = loader.load();
            MS_RequestForRecommendation MS_RequestForRecommendation = loader.getController();
            MS_RequestForRecommendation.setStudent(student, clientNetwork);
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
        catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    public void temporaryScoresFunction(ActionEvent event) {
        timelineForConnected.stop();
        try{
            stage = ((Stage)(((Scene)timeShowLabel.getScene()).getWindow()));
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Fxml\\Student\\MS\\MS_temporaryScores.fxml"));
            Parent root = loader.load();
            MS_temporaryScores MS_temporaryScores = loader.getController();
            MS_temporaryScores.setStudent(student, clientNetwork);
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
        catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    public void StatusFunction(ActionEvent event) {
        timelineForConnected.stop();
        try{
            stage = ((Stage)(((Scene)timeShowLabel.getScene()).getWindow()));
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Fxml\\Student\\MS\\MS_StudentEducationalStatus.fxml"));
            Parent root = loader.load();
            MS_StudentEducationalStatus MS_StudentEducationalStatus = loader.getController();
            MS_StudentEducationalStatus.setStudent(student, clientNetwork);
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
        catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }


}

