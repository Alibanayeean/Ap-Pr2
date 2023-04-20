package controller.publicController;


import back.enums.EducationStatus;
import back.enums.Grade;
import back.enums.StatusTeacher;
import back.persons.Admin;
import back.persons.Mohseni;
import back.persons.Student;
import back.persons.Teacher;
import clientNetwork.ClientNetwork;
import config.ConfigIdentifier;
import config.ReadPropertyFile;
import controller.Mohseni.MohseniFirstPage;
import controller.StudentController.BC.BCFirstPage;
import controller.StudentController.MS.MSFirstPage;
import controller.StudentController.PHD.PHDFirstPage;
import controller.TeacherController.Boss.BossFirstPage;
import controller.TeacherController.EducationalAssistant.EducationalAssistantFirstPage;
import controller.TeacherController.Teacher.TeacherFirstPage;
import controller.admin.AdminFirstPage;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.Random;
import java.util.ResourceBundle;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;


public class LoginPage implements Initializable {
    public static final Logger log = LogManager.getLogger(LoginPage.class);

    public ClientNetwork clientNetwork;

    @FXML
    Button LoginButton;

    @FXML
    TextField usernameField;

    @FXML
    PasswordField passwordField;

    @FXML
    TextField captchaField;

    @FXML
    ImageView imageView;

    @FXML
    ImageView base;

    @FXML
    Button Recaptcha;

    @FXML
    Label passwordLabel;

    @FXML
    Label usernameLabel;

    @FXML
    Button reconnectionButton;

    int input = 0;
    Stage stage;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void setClientNetwork(ClientNetwork clientNetwork){
        this.clientNetwork = clientNetwork;
    }

    @FXML
    void Login(ActionEvent e) {
        mainMethodForLogin(e);
    }

    public void setImageView() {
        clientNetwork.testConnection();
        if(clientNetwork.isConnected){
            passwordField.setVisible(true);
            usernameField.setVisible(true);
            captchaField.setVisible(true);
            Recaptcha.setVisible(true);
            LoginButton.setVisible(true);
            imageView.setVisible(true);
            usernameLabel.setVisible(true);
            passwordLabel.setVisible(true);
            reconnectionButton.setVisible(false);

            File fileBase = new File(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.publicURL) + "backGround.png");
            Image image = new Image(fileBase.toURI().toString());
            base.setImage(image);

            Random random = new Random();
            int anInt = random.nextInt(5);
            anInt++;
            if(input == anInt){
                setImageView();
                return;
            }
            else{
                input = anInt;
            }



            File file = new File(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.publicURL) + "captcha"+ input +".png");
            Image image2 = new Image(file.toURI().toString());
            imageView.setImage(image2);
            File file1 = new File(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.publicURL) + "recaptcha.png");
            Image image1 = new Image(file1.toURI().toString());
            Recaptcha.setGraphic(new ImageView(image1));
        }
        else{
            File fileBase = new File(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.publicURL) + "nowifi.png");
            Image image = new Image(fileBase.toURI().toString());
            base.setImage(image);

            File file = new File(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.publicURL)  + "reconnectionButton.png");
            Image image1 = new Image(file.toURI().toString());
            reconnectionButton.setGraphic(new ImageView(image1));

            passwordField.setVisible(false);
            usernameField.setVisible(false);
            captchaField.setVisible(false);
            Recaptcha.setVisible(false);
            LoginButton.setVisible(false);
            imageView.setVisible(false);
            usernameLabel.setVisible(false);
            passwordLabel.setVisible(false);
            reconnectionButton.setVisible(true);
        }

    }

    public void ChangeCaptchaButton(ActionEvent event) {
        setImageView();
    }

    public boolean ValidCaptchaInput(){
        String s = captchaField.getText();
        if(input == 1){
            if(s.equals("74853"))
                return true;
        }
        else if(input == 2){
            if(s.equals("cg5dd"))
                return true;
        }
        else if(input == 3){
            if(s.equals("b5nmm"))
                return true;
        }
        else if(input == 4){
            if(s.equals("mxyxw"))
                return true;
        }
        else if(input == 5){
            if(s.equals("c7gb3"))
                return true;
        }
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Incorrect captcha input");
        alert.show();
        return false;
    }

    @FXML
    public void LoginByEnter(KeyEvent e) {
        if(e.getCode() == KeyCode.ENTER){
            mainMethodForLogin(e);
        }
    }

    @FXML
    public void reconnectionFunction(ActionEvent e) {
        clientNetwork.attemptToMakeNewConnection();
        setImageView();
    }

    public void mainMethodForLogin(Event e){
        boolean b = ValidCaptchaInput();
        if(!b){
            setImageView();
            log.error("Captcha incorrect");
            return;
        }


        Student studentLogin = clientNetwork.getStudentLogin(usernameField.getText(), passwordField.getText().hashCode() + "");
        Teacher teacherLogin = clientNetwork.getTeacherLogin(usernameField.getText(), passwordField.getText().hashCode() + "");
        Mohseni mohseni = clientNetwork.getMohseniLogin(usernameField.getText(), passwordField.getText().hashCode() + "");
        Admin admin = clientNetwork.getAdminLogin(usernameField.getText(), passwordField.getText().hashCode() + "");


        if(teacherLogin == null && studentLogin == null && mohseni == null && admin == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Incorrect");
            alert.setHeaderText("Username or password incorrect");
            alert.showAndWait();
            setImageView();

            log.error("Username or password incorrect");

            return;
        }



        if(studentLogin != null){
            File file = new File(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.publicURL) + studentLogin.getStudentNumber());
            if(!file.exists()){
                file.mkdir();
            }

            file = new File(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.publicURL) + usernameField.getText() + "\\" + usernameField.getText() + ".txt");
            try {
                file.createNewFile();
            } catch (IOException ioException) {

            }

            clientNetwork.saveMessagesBecauseNotConnected(usernameField.getText());


            LocalDateTime localDateTime = LocalDateTime.now();
            long DeltaLastSecond = (localDateTime.getSecond() - studentLogin.getLastSecondLogin()) + (localDateTime.getMinute() - studentLogin.getLastMinuteLogin()) * 60 +
                    (localDateTime.getHour() - studentLogin.getLastHourLogin()) * 3600 + (localDateTime.getDayOfMonth() - studentLogin.getLastDayLogin()) * 60 * 60 * 24 +
                    (localDateTime.getMonthValue() - studentLogin.getLastMonthLogin()) * 60 * 60 * 24 * 30 + (localDateTime.getYear() - studentLogin.getLastYearLogin()) * 3600 * 24 * 365;
            if(studentLogin.getEducationStatus() == EducationStatus.Wait){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Wait");
                alert.setHeaderText("You are in waiting list for withdrawal from education. You can not sign it.");
                alert.showAndWait();

                log.info("in waiting list");

                return;
            }
            if(studentLogin.getTimesLogin() == 0){

            }
            else if(DeltaLastSecond >= 10){

                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("change password");
                alert.setHeaderText("From your last login " + DeltaLastSecond + " seconds pass. You should change your password");
                alert.showAndWait();

                log.error("Should change password");


                try {
                    stage = ((Stage)(((Scene)((Node)(e.getSource())).getScene()).getWindow()));
                    FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Fxml\\Public\\Public_ChangePassword.fxml"));
                    Parent root = loader.load();
                    Public_ChangePassword Public_ChangePassword = loader.getController();
                    Public_ChangePassword.setUserName(usernameField.getText(), clientNetwork);
                    Scene scene = new Scene(root);
                    stage.setScene(scene);
                    stage.show();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
                return;
            }

            clientNetwork.setTimesLoginForAStudent(studentLogin.getId(), studentLogin.getTimesLogin() + 1);

            if(studentLogin.getGrade() == Grade.BC){
                try {
                    stage = ((Stage)(((Scene)((Node)(e.getSource())).getScene()).getWindow()));
                    FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Fxml\\Student\\BC\\BCFirstPage.fxml"));
                    Parent root = loader.load();
                    BCFirstPage BCFirstPage = loader.getController();


                    BCFirstPage.setStudent(studentLogin, clientNetwork);
                    Scene scene = new Scene(root);
                    stage.setScene(scene);
                    stage.show();


                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
            else if(studentLogin.getGrade() == Grade.MS){
                try {
                    stage = ((Stage)(((Scene)((Node)(e.getSource())).getScene()).getWindow()));
                    FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Fxml\\Student\\MS\\MSFirstPage.fxml"));
                    Parent root = loader.load();
                    MSFirstPage MSFirstPage = loader.getController();
                    MSFirstPage.setStudent(studentLogin, clientNetwork);
                    Scene scene = new Scene(root);
                    stage.setScene(scene);
                    stage.show();



                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
            else if(studentLogin.getGrade() == Grade.PHD){
                try {
                    stage = ((Stage)(((Scene)((Node)(e.getSource())).getScene()).getWindow()));
                    FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Fxml\\Student\\PHD\\PHDFirstPage.fxml"));
                    Parent root = loader.load();
                    PHDFirstPage PHDFirstPage = loader.getController();
                    PHDFirstPage.setStudent(studentLogin, clientNetwork);
                    Scene scene = new Scene(root);
                    stage.setScene(scene);
                    stage.show();



                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }

            return;
        }

        else if(teacherLogin != null){

            File file = new File(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.publicURL) + teacherLogin.getUsername());
            if(!file.exists()){
                file.mkdir();
            }

            file = new File(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.publicURL) + usernameField.getText() + "\\" + usernameField.getText() + ".txt");
            try {
                file.createNewFile();
            } catch (IOException ioException) {

            }

            LocalDateTime localDateTime = LocalDateTime.now();
            long DeltaLastSecond = (localDateTime.getSecond() - teacherLogin.getLastSecondLogin()) + (localDateTime.getMinute() - teacherLogin.getLastMinuteLogin()) * 60 +
                    (localDateTime.getHour() - teacherLogin.getLastHourLogin()) * 3600 + (localDateTime.getDayOfMonth() - teacherLogin.getLastDayLogin()) * 60 * 60 * 24 +
                    + (localDateTime.getMonthValue() - teacherLogin.getLastMonthLogin()) * 60 * 60 * 24 * 30 + (localDateTime.getYear() - teacherLogin.getLastYearLogin()) * 3600 * 24 * 365;

            if(teacherLogin.getTimesLogin() == 0){

            }
            else if(DeltaLastSecond >= 10){

                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("change password");
                alert.setHeaderText("From your last login " + DeltaLastSecond + " seconds pass. You should change your password");
                alert.showAndWait();

                log.error("Should change password");

                try {
                    stage = ((Stage)(((Scene)((Node)(e.getSource())).getScene()).getWindow()));
                    FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Fxml\\Public\\Public_ChangePassword.fxml"));
                    Parent root = loader.load();
                    Public_ChangePassword Public_ChangePassword = loader.getController();
                    Public_ChangePassword.setUserName(usernameField.getText(), clientNetwork);
                    Scene scene = new Scene(root);
                    stage.setScene(scene);
                    stage.show();

                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
                return;
            }

            clientNetwork.setTimesLoginForATeacher(teacherLogin.getId(), teacherLogin.getTimesLogin() + 1);

            try {
                if(teacherLogin.getStatusTeacher() == StatusTeacher.EA){
                    stage = ((Stage)(((Scene)((Node)(e.getSource())).getScene()).getWindow()));
                    FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Fxml\\Teacher\\EducationalAssistant\\EducationalAssistantFirstPage.fxml"));
                    Parent root = loader.load();
                    EducationalAssistantFirstPage EducationalAssistantPage = loader.getController();
                    EducationalAssistantPage.setTeacher(teacherLogin, clientNetwork);
                    Scene scene = new Scene(root);
                    stage.setScene(scene);
                    stage.show();


                }
                else if(teacherLogin.getStatusTeacher() == StatusTeacher.Boss){

                    stage = ((Stage)(((Scene)((Node)(e.getSource())).getScene()).getWindow()));
                    FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Fxml\\Teacher\\Boss\\BossFirstPage.fxml"));
                    Parent root = loader.load();
                    BossFirstPage BossFirstPage = loader.getController();
                    BossFirstPage.setTeacher(teacherLogin, clientNetwork);
                    Scene scene = new Scene(root);
                    stage.setScene(scene);
                    stage.show();


                }
                else{
                    stage = ((Stage)(((Scene)((Node)(e.getSource())).getScene()).getWindow()));
                    FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Fxml\\Teacher\\Teacher\\TeacherFirstPage.fxml"));
                    Parent root = loader.load();
                    TeacherFirstPage TeacherFirstPage = loader.getController();
                    TeacherFirstPage.setTeacher(teacherLogin, clientNetwork);
                    Scene scene = new Scene(root);
                    stage.setScene(scene);
                    stage.show();


                }


                return;

            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }

        else if(mohseni != null){
            File file = new File(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.publicURL) + mohseni.getUsername());
            if(!file.exists()){
                file.mkdir();
            }

            file = new File(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.publicURL) + usernameField.getText() + "\\" + usernameField.getText() + ".txt");
            try {
                file.createNewFile();
            } catch (IOException ioException) {

            }

            try {
                stage = ((Stage)(((Scene)((Node)(e.getSource())).getScene()).getWindow()));
                FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Fxml\\Mohseni\\MohseniFirstPage.fxml"));
                Parent root = loader.load();
                MohseniFirstPage MohseniFirstPage = loader.getController();
                MohseniFirstPage.setMohseni(mohseni, clientNetwork);
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }

        }

        else if(admin != null){
            File file = new File(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.publicURL) + admin.getUsername());
            if(!file.exists()){
                file.mkdir();
            }

            file = new File(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.publicURL) + usernameField.getText() + "\\" + usernameField.getText() + ".txt");
            try {
                file.createNewFile();
            } catch (IOException ioException) {

            }

            try {
                stage = ((Stage)(((Scene)((Node)(e.getSource())).getScene()).getWindow()));
                FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Fxml\\admin\\AdminFirstPage.fxml"));
                Parent root = loader.load();
                AdminFirstPage AdminFirstPage = loader.getController();
                AdminFirstPage.setAdmin(admin, clientNetwork);
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }

        }
    }


}



