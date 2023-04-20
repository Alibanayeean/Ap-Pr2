package controller.TeacherController.CW;


import back.course.EducationalMaterials;
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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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

public class Teacher_CW_CoursePage_AddEducationalMaterial implements Initializable {

    public static final Logger log = LogManager.getLogger(controller.TeacherController.EducationalAssistant.EducationalAssistant_AddNewUser.class);


    Teacher teacher;
    ClientNetwork clientNetwork;



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
    Button SubmitButton;

    @FXML
    ChoiceBox typeChoiceBox;


    @FXML
    TextField nameTextField;

    @FXML
    TextArea descriptionTextArea;

    @FXML
    Button fileButton;

    Timeline timelineForConnected;

    @FXML
    Button reconnectionButton;

    @FXML
    ImageView connectionStatus;

    @FXML
    Button backButton;

    @FXML
    ListView<String> listView;

    String courseId;

    LinkedList<EducationalMaterials> educationalMaterials;
    LinkedList<String> items = new LinkedList<>();


    Stage stage;
//    LinkedList<File> files = new LinkedList<>();
    LinkedList<String> addressesOrText = new LinkedList<>();
    LinkedList<HomeWorkOrExamType> types = new LinkedList<>();

    File file = null;
    String address = "";

    int numAdds = 0;

    @FXML
    void ChangingColor(ActionEvent event) {
        Color color = colorPicker.getValue();
        timeShowLabel.setBackground(new Background(new BackgroundFill(color, null, null)));
        HomeButton.setBackground(new Background(new BackgroundFill(color, null, null)));
        backButton.setBackground(new Background(new BackgroundFill(color, null, null)));
        AddButton.setBackground(new Background(new BackgroundFill(color, null, null)));
        fileButton.setBackground(new Background(new BackgroundFill(color, null, null)));
        SubmitButton.setBackground(new Background(new BackgroundFill(color, null, null)));
        teacher.color = colorPicker.getValue();
        String s = "#" + Integer.toHexString(color.hashCode());
        clientNetwork.changeColorForTeacher(teacher.getUsername(), s);


        log.info("change background");



    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        PublicMethods.initImageOfLogOutButton(LogOutButton);

        PublicMethods.initImageOfHomeButton(HomeButton);

        PublicMethods.initImageOfBackButton(backButton);

        PublicMethods.initClock(timeShowLabel);


        String[] choices_Grade = {"Text" , "File"};
        typeChoiceBox.getItems().addAll(choices_Grade);

        fileButton.setVisible(false);
        descriptionTextArea.setVisible(false);
        listView.setVisible(false);
        SubmitButton.setVisible(false);


        typeChoiceBox.setOnAction(event -> {
            if(getChoiceBoxResult().equals("File")){
                fileButton.setVisible(true);
                descriptionTextArea.setVisible(false);
            }
            else{
                fileButton.setVisible(false);
                descriptionTextArea.setVisible(true);
            }
        });

    }

    public void setTeacher(Teacher teacher, ClientNetwork clientNetwork, String courseId, LinkedList<EducationalMaterials> educationalMaterials){
        this.clientNetwork = clientNetwork;
        this.teacher = teacher;
        this.courseId = courseId;
        this.educationalMaterials = educationalMaterials;

        Color color = teacher.color;
        timeShowLabel.setBackground(new Background(new BackgroundFill(color, null, null)));
        backButton.setBackground(new Background(new BackgroundFill(color, null, null)));
        HomeButton.setBackground(new Background(new BackgroundFill(color, null, null)));
        AddButton.setBackground(new Background(new BackgroundFill(color, null, null)));
        SubmitButton.setBackground(new Background(new BackgroundFill(color, null, null)));
        fileButton.setBackground(new Background(new BackgroundFill(color, null, null)));

        colorPicker.setValue(color);

        timelineForConnected = PublicMethods.reconnectionTimeline(reconnectionButton, clientNetwork, connectionStatus);
    }

    public void AddFunction(ActionEvent e) {

        String stringChooseGrade = getChoiceBoxResult();
        if(numAdds == 5){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("number of files is 5");
            alert.showAndWait();
            return;
        }
        if(stringChooseGrade.equals("")){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Incorrect");
            alert.setHeaderText("Choose one of field of choice box");
            log.error("Choose one of field of choice box");

            alert.showAndWait();
            return;
        }

        if(stringChooseGrade.toLowerCase().equals("text")){
            addressesOrText.add(descriptionTextArea.getText());
            types.add(HomeWorkOrExamType.TEXT);
            descriptionTextArea.setText("");
        }
        else{
            if(file == null){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Incorrect");
                alert.setHeaderText("Choose one file");
                log.error("Choose one file");
                return;
            }
            addressesOrText.add(address);
            types.add(HomeWorkOrExamType.FILE);
            clientNetwork.sendFileToServer(teacher.getUsername() ,address);
            file = null;
            address = "";
        }

        items.add(types.getLast() + "");
        SubmitButton.setVisible(true);
        listView.setVisible(true);
        fileButton.setVisible(false);
        descriptionTextArea.setVisible(false);
        typeChoiceBox.setValue(null);
        numAdds++;
        updateListView();
    }

    public void updateListView(){
        String[] s = new String[items.size()];
        items.toArray(s);

        ObservableList<String> strings = FXCollections.observableArrayList (s);
        listView.setItems(strings);
    }

    public void SubmitFunction(ActionEvent event){
        if(nameTextField.getText() == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Incorrect");
            alert.setHeaderText("Name field is empty");
            log.error("Name field is empty!!");

            alert.showAndWait();
            return;
        }
        else if(nameTextField.getText().equals("")){

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Incorrect");
            alert.setHeaderText("Name field is empty");
            log.error("Name field is empty!!");

            alert.showAndWait();
            return;
        }

        LocalDateTime localDateTime = LocalDateTime.now();
        String beginTime = localDateTime.getYear() + "-" + localDateTime.getMonthValue() + "-" + localDateTime.getDayOfMonth() + "-" +
                localDateTime.getHour() + "-" + localDateTime.getMinute();
        educationalMaterials.add(new EducationalMaterials(0, nameTextField.getText(), beginTime, addressesOrText, types, addressesOrText.size()));
        String s = (new Gson()).toJson(educationalMaterials);
        clientNetwork.changeEducationalMaterialForACourse(courseId, s);
        backButtonFunction(event);
    }

    public String getChoiceBoxResult(){
        String string = (String) typeChoiceBox.getValue();
        if(string == null){
            return "";
        }
        return string ;
    }

    public void selectFile(){
        if(numAdds == 5){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("number of files is 5");
            alert.showAndWait();
            return;
        }
        stage = ((Stage)(((Scene)timeShowLabel.getScene()).getWindow()));
        FileChooser fil_chooser = new FileChooser();

        fil_chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image file", "*.pdf" , "*.png", "*.mp3", "*.mp4", "*.jpg"));
        File file = fil_chooser.showOpenDialog(stage);

        if (file != null) {
            String ss = file.getPath();
            int index = ss.lastIndexOf('.');
            String address = ss.substring(index);
            String time = LocalDateTime.now().toString();
            time = time.replace(':', '-');
            time = time.replace('.', '-');
            address = "Em-" + time + teacher.getUsername().hashCode() + address;

            File file1 = new File(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.publicURL) + teacher.getUsername() + "/"  +  address);
            try {
                Files.copy(Paths.get(file.getPath()), Paths.get(file1.getPath()));
            } catch (IOException eo) {
                return;
            }

            if(this.file != null){
                if(this.file.exists()){
                    this.file.delete();
                    clientNetwork.removeAFileFromServerForCw(this.address);
                    this.file = null;
                }
            }

            this.address = address;
            this.file = file;
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

