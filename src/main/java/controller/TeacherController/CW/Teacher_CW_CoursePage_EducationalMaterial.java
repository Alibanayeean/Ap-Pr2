package controller.TeacherController.CW;


import back.course.EducationalMaterials;
import back.enums.Grade;
import back.enums.HomeWorkOrExamType;
import back.enums.StatusTeacher;
import back.persons.Teacher;
import clientNetwork.ClientNetwork;
import com.google.gson.Gson;
import config.ConfigIdentifier;
import config.ReadPropertyFile;
import controller.TeacherController.Boss.BossFirstPage;
import controller.TeacherController.EducationalAssistant.EducationalAssistantFirstPage;
import controller.TeacherController.Teacher.TeacherFirstPage;
import controller.publicController.LoginPage;
import controller.publicMethods.PublicMethods;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.InvalidationListener;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.MapChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
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
import java.util.LinkedList;
import java.util.ResourceBundle;


public class Teacher_CW_CoursePage_EducationalMaterial implements Initializable {
    public static final Logger log = LogManager.getLogger(Teacher_CW_CoursePage_EducationalMaterial.class);


    Teacher teacher;
    String courseId;

    ClientNetwork clientNetwork;

    @FXML
    ColorPicker colorPicker;

    @FXML
    Button LogOutButton;

    @FXML
    Button HomeButton;

    Stage stage;

    Timeline timelineForConnected;

    @FXML
    Button reconnectionButton;

    @FXML
    ImageView connectionStatus;

    @FXML
    Button backButton;

    @FXML
    Pane pane;

    @FXML
    Button deleteButton;

    @FXML
    Button addButton;



    LinkedList<EducationalMaterials> educationalMaterials;

    int index;



    @FXML
    void ChangingColor(ActionEvent event) {


        Color color = colorPicker.getValue();
        HomeButton.setBackground(new Background(new BackgroundFill(color, null, null)));
        backButton.setBackground(new Background(new BackgroundFill(color, null, null)));
        deleteButton.setBackground(new Background(new BackgroundFill(color, null, null)));
        addButton.setBackground(new Background(new BackgroundFill(color, null, null)));

        teacher.color = colorPicker.getValue();
        String s = "#" + Integer.toHexString(color.hashCode());
        clientNetwork.changeColorForTeacher(teacher.getUsername(), s);

        ShowFunction();

        log.info("change background");

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        PublicMethods.initImageOfLogOutButton(LogOutButton);

        PublicMethods.initImageOfHomeButton(HomeButton);

        PublicMethods.initImageOfBackButton(backButton);

    }

    public void setTeacher(Teacher Teacher, ClientNetwork clientNetwork, String courseId, LinkedList<EducationalMaterials> educationalMaterials, int index){
        this.clientNetwork = clientNetwork;
        this.teacher = Teacher;
        this.courseId = courseId;
        this.index = index;
        this.educationalMaterials = educationalMaterials;

        Color color = Teacher.color;

        HomeButton.setBackground(new Background(new BackgroundFill(color, null, null)));
        backButton.setBackground(new Background(new BackgroundFill(color, null, null)));
        deleteButton.setBackground(new Background(new BackgroundFill(color, null, null)));
        addButton.setBackground(new Background(new BackgroundFill(color, null, null)));
        colorPicker.setValue(color);

        timelineForConnected = PublicMethods.reconnectionTimeline(reconnectionButton, clientNetwork, connectionStatus);

        ShowFunction();



    }


    public void ShowFunction(){
        PrintForPane();
    }

    public void PrintForPane(){
        pane.getChildren().clear();
        Font font = Font.font("Comic Sans MS", FontWeight.BOLD, FontPosture.ITALIC, 20);
        pane.setPrefHeight(educationalMaterials.get(index).getNumFiles() * 200 + 200);
        int counter = 0;

        Label label = new Label("Name: " + educationalMaterials.get(index).getName());
        label.setFont(font);
        label.setLayoutX(0);
        label.setLayoutY(0 + counter * 200);
        pane.getChildren().add(label);
        counter++;

        for (int i = 0; i < educationalMaterials.get(index).getNumFiles(); i++) {
            if(educationalMaterials.get(index).getFileType().get(i) == HomeWorkOrExamType.FILE){
                Label label1 = new Label(educationalMaterials.get(index).getFileType().get(i) + "");
                label1.setFont(font);
                label1.setLayoutX(0);
                label1.setLayoutY(0 + counter * 200);
                pane.getChildren().add(label1);


                Button button = new Button("Download");
                button.setBackground(new Background(new BackgroundFill(teacher.getColor(), null, null)));
                button.setLayoutX(300);
                button.setLayoutY(0 + 200 * counter);
                button.setCursor(Cursor.HAND);
                final int m = i;
                button.setOnAction(event -> {
                    downloadButtonFunction(m);
                });
                pane.getChildren().add(button);


                Button button2 = new Button("Change");
                button2.setBackground(new Background(new BackgroundFill(teacher.getColor(), null, null)));
                button2.setLayoutX(500);
                button2.setLayoutY(0 + 200 * counter);
                button2.setCursor(Cursor.HAND);
                button2.setOnAction(event -> {
                    openFileChooser(m);
                });
                pane.getChildren().add(button2);

            }
            else{
                Label label1 = new Label(educationalMaterials.get(index).getFileType().get(i) + "");
                label1.setFont(font);
                label1.setLayoutX(0);
                label1.setLayoutY(0 + counter * 200);
                pane.getChildren().add(label1);

                final int m = i;

                TextArea textArea = new TextArea(educationalMaterials.get(index).getAddressesInServerOrText().get(i));
                textArea.setPrefHeight(133);
                textArea.setPrefWidth(342);
                textArea.setLayoutX(300);
                textArea.setLayoutY(0 + 200 * counter);
                textArea.textProperty().addListener(new ChangeListener<String>() {
                    @Override
                    public void changed(final ObservableValue<? extends String> observable, final String oldValue, final String newValue) {
                        educationalMaterials.get(index).getAddressesInServerOrText().set(m, textArea.getText());
                        String s = (new Gson()).toJson(educationalMaterials);
                        clientNetwork.changeEducationalMaterialForACourse(courseId, s);
                    }
                });

                pane.getChildren().add(textArea);
            }

            Button button = new Button("Delete");
            button.setBackground(new Background(new BackgroundFill(teacher.getColor(), null, null)));
            button.setLayoutX(900);
            button.setLayoutY(0 + 200 * counter);
            button.setCursor(Cursor.HAND);
            final int m = i;
            button.setOnAction(event -> {
                educationalMaterials.get(index).getAddressesInServerOrText().remove(m);
                educationalMaterials.get(index).getFileType().remove(m);
                educationalMaterials.get(index).setNumFiles(educationalMaterials.get(index).getNumFiles() - 1);
                String s = (new Gson()).toJson(educationalMaterials);
                clientNetwork.changeEducationalMaterialForACourse(courseId, s);
                if(educationalMaterials.get(index).getNumFiles() == 0){
                    backButtonFunction(event);
                }
                else{
                    PrintForPane();
                }
            });
            pane.getChildren().add(button);

            counter++;
        }
    }

    public void downloadButtonFunction(int i) {
        File fileClicked = new File(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.publicURL) +  teacher.getUsername() + "/" + educationalMaterials.get(index).getAddressesInServerOrText().get(i));
        if(fileClicked.exists()){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("go to this folder: " + ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.publicURL) +  teacher.getUsername() + "/" + educationalMaterials.get(index).getAddressesInServerOrText().get(i));
            alert.showAndWait();
            PublicMethods.openADesktopFolder(fileClicked);
            return;
        }
        clientNetwork.sendFileFromServer(educationalMaterials.get(index).getAddressesInServerOrText().get(i) , teacher.getUsername());
    }

    public void openFileChooser(int m) {
        stage = ((Stage) (((Scene) backButton.getScene()).getWindow()));
        FileChooser fil_chooser = new FileChooser();

        fil_chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image file", "*.pdf", "*.png", "*.mp3", "*.mp4", "*.jpg"));
        File file = fil_chooser.showOpenDialog(stage);

        if (file != null) {
            String ss = file.getPath();
            int index = ss.lastIndexOf('.');
            String address = ss.substring(index);
            String time = LocalDateTime.now().toString();
            time = time.replace(':', '-');
            time = time.replace('.', '-');
            address = "Em-" + time + teacher.getUsername().hashCode() + address;

            File file1 = new File(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.publicURL) + teacher.getUsername() + "/" + address);
            try {
                Files.copy(Paths.get(file.getPath()), Paths.get(file1.getPath()));
            } catch (IOException eo) {
                return;
            }


            clientNetwork.sendFileToServer(teacher.getUsername() ,address);
            clientNetwork.removeAFileFromServerForCw(educationalMaterials.get(this.index).getAddressesInServerOrText().get(m));
            educationalMaterials.get(this.index).getAddressesInServerOrText().set(m, address);
            String s = (new Gson()).toJson(educationalMaterials);
            clientNetwork.changeEducationalMaterialForACourse(courseId, s);
        }
    }

    public void deleteFunction(ActionEvent e){
        educationalMaterials.remove(index);
        String s = (new Gson()).toJson(educationalMaterials);
        clientNetwork.changeEducationalMaterialForACourse(courseId, s);
        backButtonFunction(e);
    }

    public void addFunction(ActionEvent e){
        if(educationalMaterials.get(index).getNumFiles() == 5){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Number of files are 5");
            alert.showAndWait();
        }
        else{
            timelineForConnected.stop();

            try{
                stage = ((Stage)(((Scene)LogOutButton.getScene()).getWindow()));
                FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Fxml\\Teacher\\CW\\Teacher_CW_CoursePage_EducationalMaterial_AddNew.fxml"));
                Parent root = loader.load();
                Teacher_CW_CoursePage_EducationalMaterial_AddNew Teacher_CW_CoursePage_EducationalMaterial_AddNew = loader.getController();
                Teacher_CW_CoursePage_EducationalMaterial_AddNew.setTeacher(teacher, clientNetwork, courseId, educationalMaterials, index);
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            }
            catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }


    public void backButtonFunction(ActionEvent e) {
        timelineForConnected.stop();

        try{
            stage = ((Stage)(((Scene)LogOutButton.getScene()).getWindow()));
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Fxml\\Teacher\\CW\\Teacher_CW_CoursePage.fxml"));
            Parent root = loader.load();
            Teacher_CW_CoursePage Teacher_CW_CoursePage = loader.getController();
            Teacher_CW_CoursePage.setTeacher(teacher, clientNetwork, courseId);
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
            if(teacher.getStatusTeacher() == StatusTeacher.Simple){
                stage = ((Stage)(((Scene)LogOutButton.getScene()).getWindow()));
                FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Fxml\\Teacher\\Teacher\\TeacherFirstPage.fxml"));
                Parent root = loader.load();
                TeacherFirstPage teacherFirstPage = loader.getController();
                teacherFirstPage.setTeacher(teacher, clientNetwork);
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            }
            else if(teacher.getStatusTeacher() == StatusTeacher.Boss){
                stage = ((Stage)(((Scene)LogOutButton.getScene()).getWindow()));
                FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Fxml\\Teacher\\Boss\\BossFirstPage.fxml"));
                Parent root = loader.load();
                BossFirstPage BossFirstPage = loader.getController();
                BossFirstPage.setTeacher(teacher, clientNetwork);
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            }
            else if(teacher.getStatusTeacher() == StatusTeacher.EA){
                stage = ((Stage)(((Scene)LogOutButton.getScene()).getWindow()));
                FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Fxml\\Teacher\\EducationalAssistant\\EducationalAssistantFirstPage.fxml"));
                Parent root = loader.load();
                EducationalAssistantFirstPage EducationalAssistantFirstPage = loader.getController();
                EducationalAssistantFirstPage.setTeacher(teacher, clientNetwork);
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            }


        }
        catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    public void LogOutFunction(ActionEvent event) {
        timelineForConnected.stop();

        try{
            stage = ((Stage)(((Scene)LogOutButton.getScene()).getWindow()));
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

}

