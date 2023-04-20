package controller.TeacherController.Boss;

import back.enums.StatusTeacher;
import back.enums.TeacherDegree;
import back.persons.Student;
import back.persons.Teacher;
import clientNetwork.ClientNetwork;
import config.ConfigIdentifier;
import config.ReadPropertyFile;
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
import org.apache.log4j.LogManager;


public class Boss_ShowTeacherList_AddNewTeacher implements Initializable {

    public static final Logger log = LogManager.getLogger(Boss_ShowTeacherList_AddNewTeacher.class);


    Teacher teacher;
    ClientNetwork clientNetwork;


    @FXML
    ColorPicker colorPicker;

    @FXML
    Label timeShowLabel;

    @FXML
    Button AddButton;

    @FXML
    ChoiceBox TeacherDegreeChoice;

    @FXML
    TextField FirstnameTextField;

    @FXML
    TextField LastnameTextField;

    @FXML
    TextField IdentityCodeTextField;

    @FXML
    TextField EmailTextField;

    @FXML
    TextField PhoneNumberTextField;

    @FXML
    TextField RoomNumberTextField;

    @FXML
    TextField UsernameTextField;

    @FXML
    PasswordField Password1TextField;

    @FXML
    PasswordField Password2TextField;

    @FXML
    RadioButton RadioButtonEducationalAssistant;

    @FXML
    Button BackButton;

    Timeline timelineForConnected;

    @FXML
    Button reconnectionButton;

    @FXML
    ImageView connectionStatus;

    Stage stage;

    @FXML
    void ChangingColor(ActionEvent event) {
        Color color = colorPicker.getValue();
        timeShowLabel.setBackground(new Background(new BackgroundFill(color, null, null)));
        AddButton.setBackground(new Background(new BackgroundFill(color, null, null)));
        BackButton.setBackground(new Background(new BackgroundFill(color, null, null)));
        teacher.color = colorPicker.getValue();
        String s = "#" + Integer.toHexString(color.hashCode());
        clientNetwork.changeColorForTeacher(teacher.getUsername(), s);
        log.info("change background");

    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        PublicMethods.initClock(timeShowLabel);

        String[] choices_College = {"FullProfessor", "AssistantProfessor", "AssociateProfessor"};
        TeacherDegreeChoice.getItems().addAll(choices_College);

    }

    public void setTeacher(Teacher teacher, ClientNetwork clientNetwork){
        this.clientNetwork = clientNetwork;
        this.teacher = teacher;

        Color color = teacher.color;
        timeShowLabel.setBackground(new Background(new BackgroundFill(color, null, null)));
        AddButton.setBackground(new Background(new BackgroundFill(color, null, null)));
        BackButton.setBackground(new Background(new BackgroundFill(color, null, null)));

        colorPicker.setValue(color);

        timelineForConnected = PublicMethods.reconnectionTimeline(reconnectionButton, clientNetwork, connectionStatus);

    }


    public void AddNewUserFunction(ActionEvent e) {

        String stringChooseTeacherDegree = chooseTeacherDegree();

        if(stringChooseTeacherDegree == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Incorrect");
            alert.setHeaderText("Choose one of degrees!!");
            alert.showAndWait();
            return;
        }


        if(FirstnameTextField.getText().equals("")){

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Incorrect");
            alert.setHeaderText("First name field is empty");
            alert.showAndWait();
            log.error("First name field is empty");

            return;
        }

        if(!validInputForAddTeacher(FirstnameTextField.getText())){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Incorrect");
            alert.setHeaderText("First name should not have :::");
            alert.showAndWait();

            log.error("First name should not have :::");

            return;
        }

        if(LastnameTextField.getText().equals("")){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Incorrect");
            alert.setHeaderText("Last name field is empty");
            alert.showAndWait();

            log.error("Last name field is empty");

            return;
        }
        if(!validInputForAddTeacher(LastnameTextField.getText())){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Incorrect");
            alert.setHeaderText("Last name should not have :::");
            alert.showAndWait();

            log.error("First name should not have :::");

            return;
        }


        if(IdentityCodeTextField.getText().equals("")){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Incorrect");
            alert.setHeaderText("Identity code field is empty");
            alert.showAndWait();

            log.error("Identity code is empty");


            return;
        }
        if(!validInputForAddTeacher(IdentityCodeTextField.getText())){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Incorrect");
            alert.setHeaderText("Identity code should not have :::");
            alert.showAndWait();

            log.error("Identity code should not have :::");

            return;
        }

        for (int i = 0; i < IdentityCodeTextField.getText().length() ; i++) {
            if(!(IdentityCodeTextField.getText().charAt(i) >= '0' & IdentityCodeTextField.getText().charAt(i) <= '9')){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Incorrect");
                alert.setHeaderText("Identity code should be number!!!");
                alert.showAndWait();

                log.error("Identity code should be number!!!");



                return;
            }
        }

        if(RoomNumberTextField.getText().equals("")){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Incorrect");
            alert.setHeaderText("Room number field is empty");
            alert.showAndWait();

            log.error("Room number field is empty");


            return;
        }
        if(!validInputForAddTeacher(RoomNumberTextField.getText())){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Incorrect");
            alert.setHeaderText("Room number should not have :::");
            alert.showAndWait();

            log.error("Room number should not have :::");


            return;
        }

        if(true){
            for (int i = 0; i < RoomNumberTextField.getText().length() ; i++) {
                if(!(RoomNumberTextField.getText().charAt(i) >= '0' & RoomNumberTextField.getText().charAt(i) <= '9')){
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Incorrect");
                    alert.setHeaderText("Room number should be number!!!");

                    log.error("Room number should be number!!!");

                    alert.showAndWait();
                    return;
                }
            }
        }

        for (Teacher te : clientNetwork.getAllTeachers() ) {
            if(te.getRoomNumber() == Integer.parseInt(RoomNumberTextField.getText())){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Incorrect");
                alert.setHeaderText("This room is Full!!!!!");
                alert.showAndWait();

                log.error("This room is Full!!!!!");


                return;
            }
        }


        if(EmailTextField.getText().equals("")){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Incorrect");
            alert.setHeaderText("Email field is empty");
            alert.showAndWait();

            log.error("Email field is empty");
            return;
        }

        if(!validInputForAddTeacher(EmailTextField.getText())){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Incorrect");
            alert.setHeaderText("Email should not have :::");
            alert.showAndWait();

            log.error("Email should not have :::");


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
                log.error("Invalid email format");

                alert.showAndWait();
                return;
            }
        }

        if(PhoneNumberTextField.getText().equals("")){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Incorrect");
            alert.setHeaderText("Phone number field is empty");
            alert.showAndWait();

            log.error("Phone number field is empty");


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

        if(UsernameTextField.getText().equals("")){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Incorrect");
            alert.setHeaderText("username field is empty");
            alert.showAndWait();

            log.error("username field is empty");

            return;
        }
        if(!validInputForAddTeacher(UsernameTextField.getText())){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Incorrect");
            alert.setHeaderText("username should not have :::");
            alert.showAndWait();

            log.error("username should not have :::");

            return;
        }

        boolean b = clientNetwork.CanSignUpForDuplicateUsername(UsernameTextField.getText());
        if (!b) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Duplicate username");
            alert.setHeaderText("Sorry, this username has already been chosen");
            alert.showAndWait();

            log.error("this username has already been chosen");

            return;
        }


        if(Password1TextField.getText().equals("") |  Password2TextField.getText().equals("")){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Incorrect");
            alert.setHeaderText("Password field is empty");
            alert.showAndWait();

            log.error("password field is empty");

            return;
        }


        if(true){
            if(!Password1TextField.getText().equals(Password2TextField.getText())){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Incorrect");
                alert.setHeaderText("the two Password field aren't equal");
                alert.showAndWait();

                log.error("the two Password field aren't equal");


                return;
            }
        }

        if(!validInputForAddTeacher(Password1TextField.getText())){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Incorrect");
            alert.setHeaderText("Password should not have :::");
            alert.showAndWait();

            log.error("Password should not have :::");

            return;
        }

        if(RadioButtonEducationalAssistant.isSelected()){
            for (Teacher te : clientNetwork.getAllTeachers()) {
                if(te.getCollege() == teacher.getCollege()){
                    if(te.getStatusTeacher() == StatusTeacher.EA){
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Incorrect");
                        alert.setHeaderText("There is also a educational assistant");
                        alert.showAndWait();

                        log.error("there is also a educational assistant");


                        return;
                    }
                }
            }
        }

        StatusTeacher statusTeacher = StatusTeacher.Simple;
        if(RadioButtonEducationalAssistant.isSelected()){
            statusTeacher = StatusTeacher.EA;
        }
        else{
            statusTeacher = StatusTeacher.Simple;
        }

        TeacherDegree teacherDegree = StringToTeacherDegree(stringChooseTeacherDegree);

        clientNetwork.addTeacher(new Teacher(0, Color.BLUE, null, FirstnameTextField.getText(), LastnameTextField.getText(), EmailTextField.getText(), Password1TextField.getText().hashCode() + "", PhoneNumberTextField.getText() ,IdentityCodeTextField.getText(), teacher.getCollege() , statusTeacher, new LinkedList<>(), teacherDegree ,UsernameTextField.getText(), Integer.parseInt(RoomNumberTextField.getText())));
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Added successfully");
        alert.setHeaderText("Teacher added successfully");
        alert.showAndWait();

        log.info("teacher added successfully");

        try{
            stage = ((Stage)(((Scene)AddButton.getScene()).getWindow()));
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Fxml\\Teacher\\Boss\\Boss_ShowTeacherListMenuBoss.fxml"));
            Parent root = loader.load();
            Boss_ShowTeacherListMenuBoss Boss_ShowTeacherListMenuBoss = loader.getController();
            Boss_ShowTeacherListMenuBoss.setTeacher(teacher, clientNetwork);
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
        catch (IOException ioException) {
            ioException.printStackTrace();
        }


    }

    public String  chooseTeacherDegree (){
        String stringChooseGrade = (String) TeacherDegreeChoice.getValue();
        if(stringChooseGrade == null){
            return "";
        }
        return stringChooseGrade ;
    }

    public boolean validInputForAddTeacher(String s){
        for (int i = 0; i < s.length() - 3; i++) {
            if(s.charAt(i) == ':' & s.charAt(i + 1) == ':' & s.charAt(i + 2) == ':'){
                return false;
            }
        }
        return true;
    }

    public TeacherDegree StringToTeacherDegree(String s){

        if(s.equals("AssistantProfessor")){
            return TeacherDegree.AssistantProfessor;
        }
        else if(s.equals("AssociateProfessor")){
            return TeacherDegree.AssociateProfessor;
        }
        else if(s.equals("FullProfessor")){
            return TeacherDegree.FullProfessor;
        }
        else{
            return null;
        }
    }


    public void BackButton(ActionEvent event) {
        timelineForConnected.stop();

        try{
            stage = ((Stage)(((Scene)AddButton.getScene()).getWindow()));
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Fxml\\Teacher\\Boss\\Boss_ShowTeacherListMenuBoss.fxml"));
            Parent root = loader.load();
            Boss_ShowTeacherListMenuBoss Boss_ShowTeacherListMenuBoss = loader.getController();
            Boss_ShowTeacherListMenuBoss.setTeacher(teacher, clientNetwork);
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
        catch (IOException ioException) {
            ioException.printStackTrace();
        }


    }

}

