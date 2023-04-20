package controller.Mohseni;


import back.persons.Mohseni;
import back.persons.Teacher;
import clientNetwork.ClientNetwork;
import config.ConfigIdentifier;
import config.ReadPropertyFile;
import controller.TeacherController.CW.Teacher_CW;
import controller.TeacherController.Teacher.*;
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
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class MohseniFirstPage implements Initializable {
    public static final Logger log = LogManager.getLogger(MohseniFirstPage.class);


    Mohseni mohseni;
    ClientNetwork clientNetwork;



    @FXML
    javafx.scene.control.MenuBar MenuBar;

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
    Label Firstname;

    @FXML
    Label Lastname;

    @FXML
    Label emailLabel;

    @FXML
    Button HomeButton;

    @FXML
    Label lastLoginLabel;

    Timeline timelineForConnected;

    @FXML
    Button reconnectionButton;

    @FXML
    ImageView connectionStatus;

    Stage stage;
    @FXML
    void ChangingColor(ActionEvent event) {
        Color color = colorPicker.getValue();
        paneFilter.setBackground(new Background(new BackgroundFill(color, null, null)));
        MenuBar.setBackground(new Background(new BackgroundFill(color, null, null)));
        timeShowLabel.setBackground(new Background(new BackgroundFill(color, null, null)));
        HomeButton.setBackground(new Background(new BackgroundFill(color, null, null)));
        mohseni.color = colorPicker.getValue();
        String s = "#" + Integer.toHexString(color.hashCode());
        clientNetwork.changeColorForMohseni(mohseni.getUsername(), s);

        colorPicker.setValue(color);
        log.info("change background");

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        PublicMethods.initImageOfLogOutButton(LogOutButton);

        PublicMethods.initImageOfHomeButton(HomeButton);

        PublicMethods.initClock(timeShowLabel);
    }

    public void setMohseni(Mohseni mohseni, ClientNetwork clientNetwork){
        this.clientNetwork = clientNetwork;
        this.mohseni = mohseni;


        PublicMethods.initImageOfPerson(clientNetwork, imageViewUser, mohseni.getUsername(), mohseni.getImage());

        Color color = mohseni.color;
        paneFilter.setBackground(new Background(new BackgroundFill(color, null, null)));
        MenuBar.setBackground(new Background(new BackgroundFill(color, null, null)));
        timeShowLabel.setBackground(new Background(new BackgroundFill(color, null, null)));
        HomeButton.setBackground(new Background(new BackgroundFill(color, null, null)));

        colorPicker.setValue(color);

        if(mohseni.getTimesLogin() == 1){
            lastLoginLabel.setText("Last login:");
        }
        else{
            lastLoginLabel.setText("Last login:\n" + mohseni.getLastYearLogin() + "/" + mohseni.getLastMonthLogin() + "/" + mohseni.getLastDayLogin() + '\n' + mohseni.getLastHourLogin() + ":" + mohseni.getLastMinuteLogin() + ":" + mohseni.getLastSecondLogin());
        }

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
        }
        catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    public void ProfilesFunction(ActionEvent event) {
        timelineForConnected.stop();
        try{
            stage = ((Stage)(((Scene)timeShowLabel.getScene()).getWindow()));
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Fxml\\Mohseni\\Mohseni_Profiles.fxml"));
            Parent root = loader.load();
            Mohseni_Profiles Mohseni_Profiles = loader.getController();
            Mohseni_Profiles.setMohseni(mohseni, clientNetwork);
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
        catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }


    public void ChatRoom(ActionEvent event) {
        timelineForConnected.stop();
        try{
            stage = ((Stage)(((Scene)timeShowLabel.getScene()).getWindow()));
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Fxml\\Mohseni\\Mohseni_ChatRoom.fxml"));
            Parent root = loader.load();
            Mohseni_ChatRoom Mohseni_ChatRoom = loader.getController();
            Mohseni_ChatRoom.setMohseni(mohseni, clientNetwork);
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
        catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

}
