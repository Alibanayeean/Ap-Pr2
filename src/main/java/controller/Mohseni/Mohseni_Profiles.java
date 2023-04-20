package controller.Mohseni;


import back.persons.Mohseni;
import back.persons.Student;
import clientNetwork.ClientNetwork;
import config.ConfigIdentifier;
import config.ReadPropertyFile;
import controller.publicController.LoginPage;
import controller.publicMethods.PublicMethods;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.ResourceBundle;


public class Mohseni_Profiles implements Initializable {

    public static final Logger log = LogManager.getLogger(Mohseni_Profiles.class);

    Mohseni mohseni;
    ClientNetwork clientNetwork;

    @FXML
    javafx.scene.control.MenuBar MenuBar;

    @FXML
    ColorPicker colorPicker;

    @FXML
    Button LogOutButton;

    @FXML
    Button HomeButton;


    @FXML
    Label timeShowLabel;


    Stage stage;

    Timeline timelineForConnected;

    @FXML
    Button reconnectionButton;

    @FXML
    ImageView connectionStatus;

    @FXML
    ListView<String> listView;

    @FXML
    TextField textFieldForSearch;

    @FXML
    void ChangingColor(ActionEvent event) {
        Color color = colorPicker.getValue();
        MenuBar.setBackground(new Background(new BackgroundFill(color, null, null)));
        HomeButton.setBackground(new Background(new BackgroundFill(color, null, null)));
        timeShowLabel.setBackground(new Background(new BackgroundFill(color, null, null)));
        mohseni.color = colorPicker.getValue();
        String s = "#" + Integer.toHexString(color.hashCode());
        clientNetwork.changeColorForMohseni(mohseni.getUsername(), s);

        updateListCell();

        log.info("change background");

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        PublicMethods.initImageOfLogOutButton(LogOutButton);

        PublicMethods.initImageOfHomeButton(HomeButton);

        PublicMethods.initClock(timeShowLabel);

        textFieldForSearch.setOnAction(event -> {
            updateListCell();
        });

    }

    public void setMohseni(Mohseni mohseni, ClientNetwork clientNetwork){
        this.clientNetwork = clientNetwork;
        this.mohseni = mohseni;

        Color color = mohseni.color;
        MenuBar.setBackground(new Background(new BackgroundFill(color, null, null)));
        timeShowLabel.setBackground(new Background(new BackgroundFill(color, null, null)));
        HomeButton.setBackground(new Background(new BackgroundFill(color, null, null)));
        colorPicker.setValue(color);


        timelineForConnected = PublicMethods.reconnectionTimeline(reconnectionButton, clientNetwork, connectionStatus);

        updateListCell();


    }

    public void updateListCell(){

        String c = "#" + Integer.toHexString(mohseni.getColor().hashCode());

        listView.setStyle("" +
                "-fx-background-color: " + c + ";" +
                "-fx-text-fill: black;");



        LinkedList<String> names = clientNetwork.getStudentsWithFilterForMohseni(textFieldForSearch.getText());

        String[] s = new String[names.size()];
        names.toArray(s);

        ObservableList<String> items = FXCollections.observableArrayList (s);
        listView.setItems(items);

        listView.setCellFactory(param -> {
            ListCell<String> cell = new ListCell<String>(){
                @Override
                public void updateItem(String name, boolean empty){
                    super.updateItem(name, empty);
                    if (empty) {
                        setText(null);
                        setGraphic(null);
                        return;
                    }

                    Font font = Font.font("Comic Sans MS", FontWeight.BOLD, FontPosture.ITALIC, 15);
                    setFont(font);
                    setText(name);
                }
            };


            cell.addEventFilter(MouseEvent.MOUSE_ENTERED, event -> {
                if(!cell.isEmpty()){
                    cell.setCursor(Cursor.HAND);
                }
            });

            cell.addEventFilter(MouseEvent.MOUSE_EXITED, event -> {
                if(!cell.isEmpty()){
                    cell.setCursor(Cursor.DEFAULT);
                }
            });

            cell.setOnMouseClicked(event -> {

                if (event.getClickCount() == 2 && (! cell.isEmpty()) ) {
                    String name = cell.getItem();
                    String[] sn = name.split("... ... ...");
                    String studentNumber = sn[1];
                    Student student = clientNetwork.findStudentFromStudentNumber(studentNumber);
                    StudentProfile(student);
                }

            });

            return cell ;

        });


    }

    public void StudentProfile(Student student){
        timelineForConnected.stop();
        try{
            stage = ((Stage)(((Scene)timeShowLabel.getScene()).getWindow()));
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Fxml\\Mohseni\\Mohseni_Profiles_StudentProfile.fxml"));
            Parent root = loader.load();
            Mohseni_Profiles_StudentProfile Mohseni_Profiles_StudentProfile = loader.getController();
            Mohseni_Profiles_StudentProfile.setMohseni(mohseni, student, clientNetwork);
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
        catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }


    public void HomeButtonFunction(ActionEvent e) {
        timelineForConnected.stop();
        try{
            stage = ((Stage)(((Scene)timeShowLabel.getScene()).getWindow()));
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Fxml\\Mohseni\\MohseniFirstPage.fxml"));
            Parent root = loader.load();
            MohseniFirstPage MohseniFirstPage = loader.getController();
            MohseniFirstPage.setMohseni(mohseni, clientNetwork);
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



}

