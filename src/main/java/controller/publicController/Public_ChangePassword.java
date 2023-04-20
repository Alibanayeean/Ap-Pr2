package controller.publicController;

import back.persons.Student;
import back.persons.Teacher;
import clientNetwork.ClientNetwork;
import config.ConfigIdentifier;
import config.ReadPropertyFile;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class Public_ChangePassword implements Initializable {
    String username;
    ClientNetwork clientNetwork;

    public static final Logger log = LogManager.getLogger(Public_ChangePassword.class);

    @FXML
    Button Submit;

    @FXML
    PasswordField OldPasswordField;

    @FXML
    PasswordField NewPasswordField1;

    @FXML
    PasswordField NewPasswordField2;

    @FXML
    ImageView base;

    int input = 0;
    Stage stage;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        File fileBase = new File(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.publicURL) + "backGround.png");
        Image image = new Image(fileBase.toURI().toString());
        base.setImage(image);
    }

    @FXML
    void Submit(ActionEvent e) {
        String oldPassword = (OldPasswordField.getText()).hashCode() + "";
        Teacher teacher = clientNetwork.getTeacherLogin(username, oldPassword);
        Student student = clientNetwork.getStudentLogin(username, oldPassword);
        if(teacher == null & student != null){

            if(!student.getPassword().equals(oldPassword)){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Incorrect");
                alert.setHeaderText("Old password is not correct");
                alert.showAndWait();

                log.error("Old password incorrect");

                return;
            }
        }

        else if (teacher != null & student == null){
            if(!teacher.getPassword().equals(oldPassword)){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Incorrect");
                alert.setHeaderText("Old password is not correct");
                alert.showAndWait();

                log.error("Old password incorrect");

                return;
            }
        }

        if(!NewPasswordField2.getText().equals(NewPasswordField1.getText())){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Incorrect");
            alert.setHeaderText("Two new password aren't equal");
            alert.showAndWait();

            log.error("Two new password aren't equal");

            return;
        }

        if(NewPasswordField1.getText().equals(OldPasswordField.getText())){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Incorrect");
            alert.setHeaderText("your password shouldn't be last password");
            alert.showAndWait();

            log.error("Password shouldn't be last password");

            return;
        }

        if(student != null){

            clientNetwork.changePasswordForAStudent(username, (NewPasswordField2.getText()).hashCode() + "");
            clientNetwork.setTimesLoginForAStudent(student.getId(), 0);
            log.info("Set new password :)");
            log.info("times login zero");

        }

        else if(teacher != null){

            clientNetwork.changePasswordAForTeacher(username, (NewPasswordField2.getText()).hashCode() + "");
            clientNetwork.setTimesLoginForATeacher(teacher.getId(), 0);

            log.info("Set new password :)");
            log.info("times login zero");
        }


        try {
            stage = ((Stage)(((Scene)((Node)(e.getSource())).getScene()).getWindow()));
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Fxml\\Public\\LoginPage.fxml"));
            Parent root = loader.load();
            LoginPage LoginPage = loader.getController();
            LoginPage.setClientNetwork(clientNetwork);
            LoginPage.setImageView();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

        }
        catch (IOException ioException) {
            ioException.printStackTrace();
        }

    }

    public void setUserName(String username, ClientNetwork clientNetwork) {
        this.username = username;
        this.clientNetwork = clientNetwork;
    }

    public void mouseMovedSubmit(MouseEvent e) {
        Scene scene;
        scene = ((Scene)((Node)(e.getSource())).getScene());
        scene.setCursor(Cursor.HAND);
    }

    public void mouseExitSubmit(MouseEvent e) {
        Scene scene;
        scene = ((Scene)((Node)(e.getSource())).getScene());
        scene.setCursor(Cursor.DEFAULT);
    }


}



