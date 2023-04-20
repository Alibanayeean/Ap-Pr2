package controller.StudentController.BC;


import back.enums.College;
import back.enums.MinorStatus;
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
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class BC_Minor implements Initializable{
    public static final Logger log = LogManager.getLogger(BC_Minor.class);


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
    Button SubmitButton;

    @FXML
    ChoiceBox CollegeNameField;

    Timeline timelineForConnected;

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
        SubmitButton.setBackground(new Background(new BackgroundFill(color, null, null)));

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
        SubmitButton.setBackground(new Background(new BackgroundFill(color, null, null)));
        colorPicker.setValue(color);



        int weighOfCoursesPass = clientNetwork.getWeighOfCoursesPass(student.getId());
        if(weighOfCoursesPass <= 10){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Inaccessible");
            alert.setHeaderText("Inaccessible because little courses!!!");
            alert.showAndWait();

            log.error("Inaccessible for minor because of little courses");

            try{
                stage = ((Stage)(((Scene)HomeButton.getScene()).getWindow()));
                FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Fxml\\Student\\BC\\BCFirstPage.fxml"));
                Parent root = loader.load();
                BCFirstPage BCFirstPage = loader.getController();
                BCFirstPage.setStudent(student, clientNetwork);
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            }
            catch (IOException ioException) {
                ioException.printStackTrace();
            }

            return;

        }
        else if(student.getAverage_score().equals("------------")){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Inaccessible");
            alert.setHeaderText("Inaccessible because of score!!!");
            alert.showAndWait();

            log.error("Inaccessible for minor because of score");

            try{
                stage = ((Stage)(((Scene)timeShowLabel.getScene()).getWindow()));
                FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Fxml\\Student\\BC\\BCFirstPage.fxml"));
                Parent root = loader.load();
                BCFirstPage BCFirstPage = loader.getController();
                BCFirstPage.setStudent(student, clientNetwork);
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            }
            catch (IOException ioException) {
                ioException.printStackTrace();
            }

            return;

        }
        else if(Double.parseDouble(student.getAverage_score()) < 0){

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Inaccessible");
            alert.setHeaderText("Inaccessible because of score!!!");
            alert.showAndWait();

            log.error("Inaccessible for minor because of score");

            try{
                stage = ((Stage)(((Scene)timeShowLabel.getScene()).getWindow()));
                FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Fxml\\Student\\BC\\BCFirstPage.fxml"));
                Parent root = loader.load();
                BCFirstPage BCFirstPage = loader.getController();
                BCFirstPage.setStudent(student, clientNetwork);
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            }
            catch (IOException ioException) {
                ioException.printStackTrace();
            }

            return;

        }



        String second = clientNetwork.getMinorStatus(student.getId(), false);
        String first = clientNetwork.getMinorStatus(student.getId(), true);
        if(first.toLowerCase().equals("Wait".toLowerCase()) && second.toLowerCase().equals("Wait".toLowerCase())){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Wait");
            alert.setHeaderText("You are in waiting list");
            alert.showAndWait();

            log.info("In waiting list!!!");

            try{
                stage = ((Stage)(((Scene)timeShowLabel.getScene()).getWindow()));
                FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Fxml\\Student\\BC\\BCFirstPage.fxml"));
                Parent root = loader.load();
                BCFirstPage BCFirstPage = loader.getController();
                BCFirstPage.setStudent(student, clientNetwork);
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            }
            catch (IOException ioException) {
                ioException.printStackTrace();
            }

        }

        else if(first.toLowerCase().equals("Reject".toLowerCase()) || second.toLowerCase().equals("Reject".toLowerCase())){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Reject");
            alert.setHeaderText("You are rejected.");
            alert.showAndWait();

            student.setSecondMinorStatus(MinorStatus.No);
            student.setFirstMinorStatus(MinorStatus.No);
            student.setMinorCollege(StringToCollege(null));

            clientNetwork.changeMinorForStudentBecauseShowedAndRejected(student.getId());

            log.info("Rejected");



            try{
                stage = ((Stage)(((Scene)timeShowLabel.getScene()).getWindow()));
                FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Fxml\\Student\\BC\\BCFirstPage.fxml"));
                Parent root = loader.load();
                BCFirstPage BCFirstPage = loader.getController();
                BCFirstPage.setStudent(student, clientNetwork);
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            }
            catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }

        else if(first.toLowerCase().equals("Accept".toLowerCase()) && second.toLowerCase().equals("Accept".toLowerCase())){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Reject");
            alert.setHeaderText("You are accepted in " + student.getMinorCollege());
            alert.showAndWait();


            try{
                stage = ((Stage)(((Scene)timeShowLabel.getScene()).getWindow()));
                FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Fxml\\Student\\BC\\BCFirstPage.fxml"));
                Parent root = loader.load();
                BCFirstPage BCFirstPage = loader.getController();
                BCFirstPage.setStudent(student, clientNetwork);
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            }
            catch (IOException ioException) {
                ioException.printStackTrace();
            }


        }



        if(student.getCollege() == null){
            String[] choices_CollegeNameField =  {"EE", "CE", "CS", "Math", "Physics", "Chemistry", "Mechanic", "Public"};
            CollegeNameField.getItems().addAll(choices_CollegeNameField);
        }
        if(student.getCollege() == College.EE){
            String[] choices_CollegeNameField =  {"CE", "CS", "Math", "Physics", "Chemistry", "Mechanic", "Public"};
            CollegeNameField.getItems().addAll(choices_CollegeNameField);
        }
        else if(student.getCollege() == College.CE){
            String[] choices_CollegeNameField =  {"EE", "CS", "Math", "Physics", "Chemistry", "Mechanic", "Public"};
            CollegeNameField.getItems().addAll(choices_CollegeNameField);
        }
        else if(student.getCollege() == College.CS){
            String[] choices_CollegeNameField =  {"EE", "CE", "Math", "Physics", "Chemistry", "Mechanic"};
            CollegeNameField.getItems().addAll(choices_CollegeNameField);
        }
        else if(student.getCollege() == College.Math){
            String[] choices_CollegeNameField =  {"EE", "CE", "CS", "Physics", "Chemistry", "Mechanic", "Public"};
            CollegeNameField.getItems().addAll(choices_CollegeNameField);
        }
        else if(student.getCollege() == College.Physics){
            String[] choices_CollegeNameField =  {"EE", "CE", "CS", "Math", "Chemistry", "Mechanic", "Public"};
            CollegeNameField.getItems().addAll(choices_CollegeNameField);
        }
        else if(student.getCollege() == College.Ch){
            String[] choices_CollegeNameField =  {"EE", "CE", "CS", "Math", "Physics", "Mechanic", "Public"};
            CollegeNameField.getItems().addAll(choices_CollegeNameField);
        }
        else if(student.getCollege() == College.Mechanic){
            String[] choices_CollegeNameField =  {"EE", "CE", "CS", "Math", "Physics", "Chemistry", "Public"};
            CollegeNameField.getItems().addAll(choices_CollegeNameField);
        }
        else if(student.getCollege() == College.Public){
            String[] choices_CollegeNameField =  {"EE", "CE", "CS", "Math", "Physics", "Chemistry", "Mechanic"};
            CollegeNameField.getItems().addAll(choices_CollegeNameField);
        }



        timelineForConnected = PublicMethods.reconnectionTimeline(reconnectionButton, clientNetwork, connectionStatus);

    }

    @FXML
    void SubmitButtonFunction(ActionEvent e){
        Show();
    }

    public void Show(){
        String stringChooseCollegeName = chooseCollegeName();

        if(stringChooseCollegeName == null){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Incorrect");
            alert.setHeaderText("Choose one of departments");
            alert.showAndWait();
        }

        else{
            student.setMinorCollege(StringToCollege(stringChooseCollegeName));
            clientNetwork.setMinorCollegeForAStudent(student.getId(), stringChooseCollegeName);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Wait");
            alert.setHeaderText("Your request will send to the educational assistant.");
            alert.showAndWait();


            log.info("Go to waiting list");


            try{
                stage = ((Stage)(((Scene)timeShowLabel.getScene()).getWindow()));
                FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Fxml\\Student\\BC\\BCFirstPage.fxml"));
                Parent root = loader.load();
                BCFirstPage BCFirstPage = loader.getController();
                BCFirstPage.setStudent(student, clientNetwork);
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            }
            catch (IOException ioException) {
                ioException.printStackTrace();
            }

        }
    }

    public String  chooseCollegeName (){
        String stringCollegeName = (String) CollegeNameField.getValue();
        if(stringCollegeName == null){
            return "";
        }
        return stringCollegeName ;
    }

    public College StringToCollege(String s){

        if(s.toLowerCase().equals("EE".toLowerCase())){
            return College.EE;
        }
        else if(s.toLowerCase().equals("CE".toLowerCase())){
            return College.CE;
        }
        else if(s.toLowerCase().equals("CS".toLowerCase())){
            return College.CS;
        }
        else if(s.toLowerCase().equals("Math".toLowerCase())){
            return College.Math;
        }
        else if(s.toLowerCase().equals("Physics".toLowerCase())){
            return College.Physics;
        }
        else if(s.toLowerCase().equals("Ch".toLowerCase())){
            return College.Ch;
        }
        else if(s.toLowerCase().equals("Mechanic".toLowerCase())){
            return College.Mechanic;
        }
        else if(s.toLowerCase().equals("Public".toLowerCase())){
            return College.Public;
        }
        else{
            return null;
        }
    }


    public void HomeButtonFunction(ActionEvent e) {
        timelineForConnected.stop();
        try{
            stage = ((Stage)(((Scene)timeShowLabel.getScene()).getWindow()));
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Fxml\\Student\\BC\\BCFirstPage.fxml"));
            Parent root = loader.load();
            BCFirstPage BCFirstPage = loader.getController();
            BCFirstPage.setStudent(student, clientNetwork);
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
            stage = ((Stage)(((Scene)timeShowLabel.getScene()).getWindow()));
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
            stage = ((Stage)(((Scene)timeShowLabel.getScene()).getWindow()));
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
            stage = ((Stage)(((Scene)timeShowLabel.getScene()).getWindow()));
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
            stage = ((Stage)(((Scene)timeShowLabel.getScene()).getWindow()));
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

    public void certificateStudentFunction(ActionEvent event) {
        timelineForConnected.stop();

        try{
            stage = ((Stage)(((Scene)timeShowLabel.getScene()).getWindow()));
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

    public void MinorFunction(ActionEvent event) {
        timelineForConnected.stop();

        try{
            stage = ((Stage)(((Scene)timeShowLabel.getScene()).getWindow()));
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

    public void WithdrawalFromEducationFunction(ActionEvent event) {
        timelineForConnected.stop();

        try{
            stage = ((Stage)(((Scene)timeShowLabel.getScene()).getWindow()));
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

    public void RecommendationFunction(ActionEvent event) {
        timelineForConnected.stop();

        try{
            stage = ((Stage)(((Scene)timeShowLabel.getScene()).getWindow()));
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

    public void temporaryScoresFunction(ActionEvent event) {
        timelineForConnected.stop();

        try{
            stage = ((Stage)(((Scene)timeShowLabel.getScene()).getWindow()));
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

    public void StatusFunction(ActionEvent event) {
        timelineForConnected.stop();

        try{
            stage = ((Stage)(((Scene)timeShowLabel.getScene()).getWindow()));
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

    public void cwFunction(ActionEvent event) {
        timelineForConnected.stop();
        try{
            stage = ((Stage)(((Scene)timeShowLabel.getScene()).getWindow()));
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
