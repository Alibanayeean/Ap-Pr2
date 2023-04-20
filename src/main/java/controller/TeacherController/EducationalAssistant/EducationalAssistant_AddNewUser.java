package controller.TeacherController.EducationalAssistant;

import back.enums.College;
import back.enums.EducationStatus;
import back.enums.Grade;
import back.persons.Student;
import back.persons.Teacher;
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
import org.apache.log4j.LogManager;import clientNetwork.ClientNetwork;
import config.ConfigIdentifier;
import config.ReadPropertyFile;

public class EducationalAssistant_AddNewUser implements Initializable {

    public static final Logger log = LogManager.getLogger(EducationalAssistant_AddNewUser.class);


    Teacher teacher;
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
    Button AddButton;

    @FXML
    ChoiceBox GradeChoice;

    @FXML
    ChoiceBox TeacherAssistantChoiceBox;

    @FXML
    ChoiceBox CollegeChoiceBox;

    @FXML
    TextField FirstnameTextField;

    @FXML
    TextField LastnameTextField;

    @FXML
    TextField EmailTextField;

    @FXML
    TextField PhoneNumberTextField;

    @FXML
    TextField UsernameTextField;

    @FXML
    PasswordField Password1TextField;

    @FXML
    PasswordField Password2TextField;

    @FXML
    TextField IdentityCodeTextField;

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
        AddButton.setBackground(new Background(new BackgroundFill(color, null, null)));
        teacher.color = colorPicker.getValue();
        String s = "#" + Integer.toHexString(color.hashCode());
        clientNetwork.changeColorForTeacher(teacher.getUsername(), s);


        log.info("change background");

        

    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        PublicMethods.initImageOfLogOutButton(LogOutButton);

        PublicMethods.initImageOfHomeButton(HomeButton);

        PublicMethods.initClock(timeShowLabel);


        String[] choices_Grade = {"PHD" , "MS" , "BC"};
        GradeChoice.getItems().addAll(choices_Grade);

        String[] choices_College = {"EE", "CE", "CS", "Math", "Physics", "Chemistry", "Mechanic", "Public"};
        CollegeChoiceBox.getItems().addAll(choices_College);

    }

    public void setTeacher(Teacher teacher, ClientNetwork clientNetwork){
        this.clientNetwork = clientNetwork;
        this.teacher = teacher;

        Color color = teacher.color;
        MenuBar.setBackground(new Background(new BackgroundFill(color, null, null)));
        timeShowLabel.setBackground(new Background(new BackgroundFill(color, null, null)));
        HomeButton.setBackground(new Background(new BackgroundFill(color, null, null)));
        AddButton.setBackground(new Background(new BackgroundFill(color, null, null)));

        colorPicker.setValue(color);


        String[] teachersForChoiceBoxAdd = clientNetwork.returnAllHelpTeachersFromACollege(teacher.getCollege() + "").split(":::");
        if(teachersForChoiceBoxAdd == null){
            return;
        }
        TeacherAssistantChoiceBox.getItems().addAll(teachersForChoiceBoxAdd);


        timelineForConnected = PublicMethods.reconnectionTimeline(reconnectionButton, clientNetwork, connectionStatus);
    }

    public boolean validInputForAddStudent(String s){
        for (int i = 0; i < s.length() - 3; i++) {
               if(s.charAt(i) == ':' & s.charAt(i + 1) == ':' & s.charAt(i + 2) == ':'){
                   return false;
               }
        }
        return true;
    }

    public void AddFunction(ActionEvent e) {

        String stringChooseGrade = chooseGrade();

        if(stringChooseGrade == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Incorrect");
            alert.setHeaderText("Choose one of grades!!");
            log.error("choose one of grades!!");

            alert.showAndWait();
            return;
        }

        String chooseTeacher = chooseTeacher();
        if(chooseTeacher == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Incorrect");
            alert.setHeaderText("Choose one of supervisor!!");
            log.error("choose one of supervisor!!");

            alert.showAndWait();
            return;
        }

        String chooseCollege = chooseCollege();
        if(chooseCollege == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Incorrect");
            alert.setHeaderText("Choose one of departments!!");
            log.error("choose one of departments!!");

            alert.showAndWait();
            return;
        }

        if(FirstnameTextField.getText().equals("")){

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Incorrect");
            alert.setHeaderText("First name field is empty");
            log.error("First name field is empty!!");

            alert.showAndWait();
            return;
        }
        if(!validInputForAddStudent(FirstnameTextField.getText())){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Incorrect");
            alert.setHeaderText("First name should not have :::");
            log.error("First name should not have :::");

            alert.showAndWait();
            return;
        }

        if(LastnameTextField.getText().equals("")){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Incorrect");
            alert.setHeaderText("Last name field is empty");
            log.error("Last name field is empty");

            alert.showAndWait();
            return;
        }
        if(!validInputForAddStudent(LastnameTextField.getText())){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Incorrect");
            alert.setHeaderText("Last name should not have :::");
            log.error("Last name should not have :::");

            alert.showAndWait();
            return;
        }

        if(EmailTextField.getText().equals("")){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Incorrect");
            alert.setHeaderText("Email field is empty");
            log.error("Email name field is empty");

            alert.showAndWait();
            return;
        }
        if(!validInputForAddStudent(EmailTextField.getText())){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Incorrect");
            alert.setHeaderText("Email should not have :::");
            log.error("Email should not have :::");

            alert.showAndWait();
            return;
        }

        if(true){
            String email = EmailTextField.getText();
            boolean b1 = false;
            boolean b2 = false;
            for (int i = 0; i < email.length(); i++) {
                if(email.charAt(i) == '@'){
                    b1 = true;
                }
                if(b1 & email.charAt(i) == '.'){
                    b2 = true;
                    break;
                }
            }
            if(!(b1 & b2)){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Incorrect");
                alert.setHeaderText("Invalid email format");
                alert.showAndWait();
                log.error("Invalid email format");

                return;
            }
        }
        if(PhoneNumberTextField.getText().equals("")){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Incorrect");
            alert.setHeaderText("Phone number field is empty");
            log.error("Phone number field is empty");

            alert.showAndWait();
            return;
        }
        if(true){
            for (int i = 0; i < PhoneNumberTextField.getText().length() ; i++) {
                if(!(PhoneNumberTextField.getText().charAt(i) >= '0' & PhoneNumberTextField.getText().charAt(i) <= '9')){
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Incorrect");
                    alert.setHeaderText("Phone number should be number!!!");
                    log.error("Phone number should be number!!!");

                    alert.showAndWait();
                    return;
                }
            }
        }

        if(IdentityCodeTextField.getText().equals("")){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Incorrect");
            alert.setHeaderText("Identity code field is empty");
            log.error("Identity code field is empty!!!");

            alert.showAndWait();
            return;
        }
        if(!validInputForAddStudent(IdentityCodeTextField.getText())){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Incorrect");
            alert.setHeaderText("Identity code should not have :::");
            log.error("Identity code should not have :::");

            alert.showAndWait();
            return;
        }

        for (int i = 0; i < IdentityCodeTextField.getText().length() ; i++) {
            if(!(IdentityCodeTextField.getText().charAt(i) >= '0' & IdentityCodeTextField.getText().charAt(i) <= '9')){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Incorrect");
                alert.setHeaderText("Identity code number should be number!!!");
                log.error("Identity code number should be number!!!");

                alert.showAndWait();
                return;
            }
        }

        if(UsernameTextField.getText().equals("")){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Incorrect");
            alert.setHeaderText("Student number field is empty");
            log.error("Student number field is empty");

            alert.showAndWait();
            return;
        }

        if(!validInputForAddStudent(UsernameTextField.getText())){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Incorrect");
            alert.setHeaderText("Student number should not have :::");
            log.error("Student number should not have :::");

            alert.showAndWait();
            return;
        }

        if(true){
            for (int i = 0; i < UsernameTextField.getText().length() ; i++) {
                if(!(UsernameTextField.getText().charAt(i) >= '0' & UsernameTextField.getText().charAt(i) <= '9')){
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Incorrect");
                    alert.setHeaderText("Student number should be number!!!");
                    log.error("Student number should be number!!!");

                    alert.showAndWait();
                    return;
                }
            }
            boolean b = clientNetwork.CanSignUpForDuplicateUsername(UsernameTextField.getText());
            if(!b){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Duplicate username");
                alert.setHeaderText("Sorry, this student number has already been chosen");
                alert.showAndWait();
                return;
            }
        }

        if(Password1TextField.getText().equals("") |  Password2TextField.getText().equals("")){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Incorrect");
            alert.setHeaderText("Password field is empty");
            log.error("Password field is empty");

            alert.showAndWait();
            return;
        }


        if(true){
            if(!Password1TextField.getText().equals(Password2TextField.getText())){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Incorrect");
                alert.setHeaderText("the two Password field aren't equal");
                log.error("the two Password field aren't equal");

                alert.showAndWait();
                return;
            }
        }
        if(!validInputForAddStudent(Password1TextField.getText())){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Incorrect");
            alert.setHeaderText("Password should not have :::");
            log.error("Password should not have :::");

            alert.showAndWait();
            return;
        }

        Grade grade = StringToGrade(stringChooseGrade);
        College college = StringToCollege(chooseCollege);

        Teacher teacher = clientNetwork.getLastNameHelpTeacherFromLastName(chooseTeacher);
        if(teacher == null){

        }
        else{
            LocalDateTime instance = LocalDateTime.now();

            clientNetwork.addStudent(new Student(0, Color.BLUE, null, FirstnameTextField.getText(), LastnameTextField.getText(), EmailTextField.getText(), Password1TextField.getText().hashCode() + "", PhoneNumberTextField.getText() ,IdentityCodeTextField.getText(), college , UsernameTextField.getText(), new LinkedList<>(), new LinkedList<>(), new LinkedList<>(), teacher.getId(), grade, instance.getYear(), EducationStatus.Studying, false, -1));

            

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Added successfully");
            alert.setHeaderText("Student added successfully");
            alert.showAndWait();
            log.info("Student added successfully");


            HomeButtonFunction(e);
        }
    }

    public College StringToCollege(String s){

        if(s.equals("EE")){
            return College.EE;
        }
        else if(s.equals("CE")){
            return College.CE;
        }
        else if(s.equals("CS")){
            return College.CS;
        }
        else if(s.equals("Math")){
            return College.Math;
        }
        else if(s.equals("Physics")){
            return College.Physics;
        }
        else if(s.equals("Ch")){
            return College.Ch;
        }
        else if(s.equals("Mechanic")){
            return College.Mechanic;
        }
        else{
            return null;
        }
    }

    public Grade StringToGrade(String s){

        if(s.equals("BC")){
            return Grade.BC;
        }
        else if(s.equals("MS")){
            return Grade.MS;
        }
        else if(s.equals("PHD")){
            return Grade.PHD;
        }
        else{
            return null;
        }
    }

    public String  chooseGrade (){
        String stringChooseGrade = (String) GradeChoice.getValue();
        if(stringChooseGrade == null){
            return "";
        }
        return stringChooseGrade ;
    }

    public String  chooseTeacher (){
        String stringChooseGrade = (String) TeacherAssistantChoiceBox.getValue();
        if(stringChooseGrade == null){
            return "";
        }
        return stringChooseGrade ;
    }

    public String  chooseCollege (){
        String stringChooseCollege = (String) CollegeChoiceBox.getValue();
        if(stringChooseCollege == null){
            return "";
        }
        return stringChooseCollege ;
    }




    public void HomeButtonFunction(ActionEvent e) {
        timelineForConnected.stop();

        try{
            stage = ((Stage)(((Scene)timeShowLabel.getScene()).getWindow()));
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Fxml\\Teacher\\EducationalAssistant\\EducationalAssistantFirstPage.fxml"));
            Parent root = loader.load();
            EducationalAssistantFirstPage EducationalAssistantFirstPage = loader.getController();
            EducationalAssistantFirstPage.setTeacher(teacher, clientNetwork);
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
            clientNetwork.changeLastLoginTeacher(teacher.getId(), localDateTime.getSecond(), localDateTime.getMinute(), localDateTime.getHour(), localDateTime.getDayOfMonth(), localDateTime.getMonthValue(), localDateTime.getYear());



        }
        catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    public void CoursesListMenuFunction(ActionEvent event) {
        timelineForConnected.stop();

        try{
            stage = ((Stage)(((Scene)timeShowLabel.getScene()).getWindow()));
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Fxml\\Teacher\\EducationalAssistant\\EducationalAssistant_ShowCourseList.fxml"));
            Parent root = loader.load();
            EducationalAssistant_ShowCourseList EducationalAssistant_ShowCourseList = loader.getController();
            EducationalAssistant_ShowCourseList.setTeacher(teacher, clientNetwork);
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
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Fxml\\Teacher\\EducationalAssistant\\EducationalAssistant_ShowTeacherListMenu.fxml"));
            Parent root = loader.load();
            EducationalAssistant_ShowTeacherListMenu EducationalAssistant_ShowTeacherListMenu = loader.getController();
            EducationalAssistant_ShowTeacherListMenu.setTeacher(teacher, clientNetwork);
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
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Fxml\\Teacher\\EducationalAssistant\\EducationalAssistant_ShowWeeklySchedule.fxml"));
            Parent root = loader.load();
            EducationalAssistant_ShowWeeklySchedule EducationalAssistant_ShowWeeklySchedule = loader.getController();
            EducationalAssistant_ShowWeeklySchedule.setTeacher(teacher, clientNetwork);
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
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Fxml\\Teacher\\EducationalAssistant\\EducationalAssistant_ShowExams.fxml"));
            Parent root = loader.load();
            EducationalAssistant_ShowExams EducationalAssistant_ShowExams = loader.getController();
            EducationalAssistant_ShowExams.setTeacher(teacher, clientNetwork);
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
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Fxml\\Teacher\\EducationalAssistant\\EducationalAssistant_Minor.fxml"));
            Parent root = loader.load();
            EducationalAssistant_Minor EducationalAssistant_Minor = loader.getController();
            EducationalAssistant_Minor.setTeacher(teacher, clientNetwork);
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
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Fxml\\Teacher\\EducationalAssistant\\EducationalAssistant_WithdrawalFromEducation.fxml"));
            Parent root = loader.load();
            EducationalAssistant_WithdrawalFromEducation EducationalAssistant_WithdrawalFromEducation = loader.getController();
            EducationalAssistant_WithdrawalFromEducation.setTeacher(teacher, clientNetwork);
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
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Fxml\\Teacher\\EducationalAssistant\\EducationalAssistant_GiveRecome.fxml"));
            Parent root = loader.load();
            EducationalAssistant_GiveRecome EducationalAssistant_GiveRecome = loader.getController();
            EducationalAssistant_GiveRecome.setTeacher(teacher, clientNetwork);
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
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Fxml\\Teacher\\EducationalAssistant\\EducationalAssistant_temporaryScores.fxml"));
            Parent root = loader.load();
            EducationalAssistant_temporaryScores EducationalAssistant_temporaryScores = loader.getController();
            EducationalAssistant_temporaryScores.setTeacher(teacher, clientNetwork);
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
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Fxml\\Teacher\\EducationalAssistant\\EducationalAssistant_StudentsStatus.fxml"));
            Parent root = loader.load();
            EducationalAssistant_StudentsStatus EducationalAssistant_StudentsStatus = loader.getController();
            EducationalAssistant_StudentsStatus.setTeacher(teacher, clientNetwork);
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
        timelineForConnected.stop();
        try{
            stage = ((Stage)(((Scene)timeShowLabel.getScene()).getWindow()));
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Fxml\\Teacher\\EducationalAssistant\\EducationalAssistant_ShowTeacherProfile.fxml"));
            Parent root = loader.load();
            EducationalAssistant_ShowTeacherProfile EducationalAssistant_ShowTeacherProfile = loader.getController();
            EducationalAssistant_ShowTeacherProfile.setTeacher(teacher, clientNetwork);
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
        catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    public void AddNewUserFunction(ActionEvent event) {
        timelineForConnected.stop();

        try{
            stage = ((Stage)(((Scene)timeShowLabel.getScene()).getWindow()));
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Fxml\\Teacher\\EducationalAssistant\\EducationalAssistant_AddNewUser.fxml"));
            Parent root = loader.load();
            EducationalAssistant_AddNewUser EducationalAssistant_AddNewUser = loader.getController();
            EducationalAssistant_AddNewUser.setTeacher(teacher, clientNetwork);
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
        catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    public void temporaryScoresAllStudentsFunction(ActionEvent event) {
        timelineForConnected.stop();
        try{
            stage = ((Stage)(((Scene)timeShowLabel.getScene()).getWindow()));
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Fxml\\Teacher\\EducationalAssistant\\EducationalAssistant_temporaryScoresForAllStudents.fxml"));
            Parent root = loader.load();
            EducationalAssistant_temporaryScoresForAllStudents EducationalAssistant_temporaryScoresForAllStudents = loader.getController();
            EducationalAssistant_temporaryScoresForAllStudents.setTeacher(teacher, clientNetwork);
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
        catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

}

