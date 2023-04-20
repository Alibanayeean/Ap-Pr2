package controller.StudentController.BC;

import back.persons.Student;
import clientNetwork.ClientNetwork;
import config.ConfigIdentifier;
import config.ReadPropertyFile;
import controller.StudentController.CW.publicPages.CW;
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




public class BCFirstPage implements Initializable {
    public static final Logger log = LogManager.getLogger(BCFirstPage.class);

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

    @FXML
    Button HomeButton;

    @FXML
    Button reconnectionButton;

    @FXML
    ImageView connectionStatus;

    @FXML
    Button messageButton;

    Timeline timelineForConnected;
    Stage stage;
    @FXML
    void ChangingColor(ActionEvent event) {
        log.info("change background");

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

    public void messageButtonFunction(ActionEvent event) {
        timelineForConnected.stop();
        try{
            stage = ((Stage)(((Scene)emailLabel.getScene()).getWindow()));
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Fxml\\Student\\BC\\BC_Messages.fxml"));
            Parent root = loader.load();
            BC_Messages BC_Messages = loader.getController();
            BC_Messages.setStudent(student, clientNetwork);
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
        catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    public void CoursesListMenuFunction(ActionEvent event) {
        timelineForConnected.stop();
        try{
            stage = ((Stage)(((Scene)emailLabel.getScene()).getWindow()));
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Fxml\\Student\\BC\\BC_ShowCourseListMenu.fxml"));
            Parent root = loader.load();
            BC_ShowCourseListMenu BC_ShowCourseListMenu = loader.getController();
            BC_ShowCourseListMenu.setStudent(student, clientNetwork);
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
            stage = ((Stage)(((Scene)emailLabel.getScene()).getWindow()));
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Fxml\\Student\\BC\\BC_ShowTeacherListMenu.fxml"));
            Parent root = loader.load();
            BC_ShowTeacherListMenu BC_ShowTeacherListMenu = loader.getController();
            BC_ShowTeacherListMenu.setStudent(student, clientNetwork);
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
            stage = ((Stage)(((Scene)Firstname.getScene()).getWindow()));
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Fxml\\Student\\BC\\BC_ShowWeeklySchedule.fxml"));
            Parent root = loader.load();
            BC_ShowWeeklySchedule BC_ShowWeeklySchedule = loader.getController();
            BC_ShowWeeklySchedule.setStudent(student, clientNetwork);
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
            stage = ((Stage)(((Scene)Firstname.getScene()).getWindow()));
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Fxml\\Student\\BC\\BC_ShowProfile.fxml"));
            Parent root = loader.load();
            BC_ShowProfile BC_ShowProfile = loader.getController();
            BC_ShowProfile.setStudent(student, clientNetwork);
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
            stage = ((Stage)(((Scene)Firstname.getScene()).getWindow()));
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Fxml\\Student\\BC\\BC_ShowExams.fxml"));
            Parent root = loader.load();
            BC_ShowExams BC_ShowExams = loader.getController();
            BC_ShowExams.setStudent(student, clientNetwork);
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
        catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    public void certificateStudentFunction(ActionEvent e) {
        timelineForConnected.stop();
        try{
            stage = ((Stage)(((Scene)Firstname.getScene()).getWindow()));
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Fxml\\Student\\BC\\BC_CertificateStudent.fxml"));
            Parent root = loader.load();
            BC_CertificateStudent BC_CertificateStudent = loader.getController();
            BC_CertificateStudent.setStudent(student, clientNetwork);
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
        catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    public void MinorFunction(ActionEvent e) {
        timelineForConnected.stop();
        try{
            stage = ((Stage)(((Scene)Firstname.getScene()).getWindow()));
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Fxml\\Student\\BC\\BC_Minor.fxml"));
            Parent root = loader.load();
            BC_Minor BC_Minor = loader.getController();
            BC_Minor.setStudent(student, clientNetwork);
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
        catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    public void WithdrawalFromEducationFunction(ActionEvent e) {
        timelineForConnected.stop();
        try{
            stage = ((Stage)(((Scene)Firstname.getScene()).getWindow()));
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Fxml\\Student\\BC\\BC_withdrawalFromEducation.fxml"));
            Parent root = loader.load();
            BC_withdrawalFromEducation BC_withdrawalFromEducation = loader.getController();
            BC_withdrawalFromEducation.setStudent(student, clientNetwork);
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
        catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    public void RecommendationFunction(ActionEvent e) {
        timelineForConnected.stop();
        try{
            stage = ((Stage)(((Scene)Firstname.getScene()).getWindow()));
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Fxml\\Student\\BC\\BC_RequestForRecommendation.fxml"));
            Parent root = loader.load();
            BC_RequestForRecommendation BC_RequestForRecommendation = loader.getController();
            BC_RequestForRecommendation.setStudent(student, clientNetwork);
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
        catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    public void temporaryScoresFunction(ActionEvent e) {
        timelineForConnected.stop();
        try{
            stage = ((Stage)(((Scene)Firstname.getScene()).getWindow()));
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Fxml\\Student\\BC\\BC_temporaryScores.fxml"));
            Parent root = loader.load();
            BC_temporaryScores BC_temporaryScores = loader.getController();
            BC_temporaryScores.setStudent(student, clientNetwork);
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
        catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    public void StatusFunction(ActionEvent e) {
        timelineForConnected.stop();
        try{
            stage = ((Stage)(((Scene)Firstname.getScene()).getWindow()));
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Fxml\\Student\\BC\\BC_StudentEducationalStatus.fxml"));
            Parent root = loader.load();
            BC_StudentEducationalStatus BC_StudentEducationalStatus = loader.getController();
            BC_StudentEducationalStatus.setStudent(student, clientNetwork);
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
        catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    public void chatRoom(ActionEvent e) {
        timelineForConnected.stop();
        try{
            stage = ((Stage)(((Scene)Firstname.getScene()).getWindow()));
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Fxml\\Student\\BC\\BC_ChatRoom.fxml"));
            Parent root = loader.load();
            BC_ChatRoom BC_ChatRoom = loader.getController();
            BC_ChatRoom.setStudent(student, clientNetwork);
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
        catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    public void selectUnit(ActionEvent e) {
        timelineForConnected.stop();
        try{
            stage = ((Stage)(((Scene)Firstname.getScene()).getWindow()));
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Fxml\\Student\\BC\\BC_SelectedUnit.fxml"));
            Parent root = loader.load();
            BC_SelectedUnit BC_SelectedUnit = loader.getController();
            BC_SelectedUnit.setStudent(student, clientNetwork);
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
        catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    public void cwFunction(ActionEvent event) {
        timelineForConnected.stop();
        try{
            stage = ((Stage)(((Scene)emailLabel.getScene()).getWindow()));
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Fxml\\Student\\CW\\publicPages\\CW.fxml"));
            Parent root = loader.load();
            CW cw = loader.getController();
            cw.setStudent(student, clientNetwork);
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
        catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

}

