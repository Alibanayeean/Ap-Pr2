package controller.TeacherController.EducationalAssistant;


import back.persons.Teacher;
import clientNetwork.ClientNetwork;
import config.ConfigIdentifier;
import config.ReadPropertyFile;
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
import javafx.scene.Cursor;
import javafx.scene.Node;
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
import javafx.stage.Stage;
import javafx.util.Duration;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class EducationalAssistant_SelectionUnit implements Initializable {
    public static final Logger log = LogManager.getLogger(EducationalAssistant_SelectionUnit.class);

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
    Pane pane;

    @FXML
    Button EditButton;

    Timeline timelineForConnected;

    @FXML
    Button reconnectionButton;

    @FXML
    ImageView connectionStatus;

    LinkedList<TextField> textFieldsForTime = new LinkedList<>();


    int numClicked = 0;

    Timeline timeline;

    Stage stage;
    @FXML
    void ChangingColor(ActionEvent event) {
        Color color = colorPicker.getValue();
        MenuBar.setBackground(new Background(new BackgroundFill(color, null, null)));
        timeShowLabel.setBackground(new Background(new BackgroundFill(color, null, null)));
        HomeButton.setBackground(new Background(new BackgroundFill(color, null, null)));
        EditButton.setBackground(new Background(new BackgroundFill(color, null, null)));
        teacher.color = colorPicker.getValue();
        String s = "#" + Integer.toHexString(color.hashCode());
        clientNetwork.changeColorForTeacher(teacher.getUsername(), s);

        PrintForPane();
        log.info("change background");


    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        PublicMethods.initImageOfLogOutButton(LogOutButton);

        PublicMethods.initImageOfHomeButton(HomeButton);

        PublicMethods.initClock(timeShowLabel);
    }

    public void setTeacher(Teacher teacher, ClientNetwork clientNetwork){
        this.clientNetwork = clientNetwork;
        this.teacher = teacher;

        Color color = teacher.color;
        MenuBar.setBackground(new Background(new BackgroundFill(color, null, null)));
        timeShowLabel.setBackground(new Background(new BackgroundFill(color, null, null)));
        HomeButton.setBackground(new Background(new BackgroundFill(color, null, null)));
        EditButton.setBackground(new Background(new BackgroundFill(color, null, null)));
        colorPicker.setValue(color);


        PrintForPane();

        timelineForConnected = PublicMethods.reconnectionTimeline(reconnectionButton, clientNetwork, connectionStatus);

    }

    public void PrintForPane(){
        LinkedList<LinkedList<String>> strings = clientNetwork.getYearsToUniversityForEA(teacher.getCollege() + "");
        LinkedList<String> years = strings.get(0);
        LinkedList<String> status = strings.get(1);

        for (int i = 0; i < years.size(); i++) {
            for (int j = i + 1; j < years.size(); j++) {
                if(years.get(i).equals(years.get(j))){
                    if(status.get(i).length() == 10){
                        status.remove(j);
                        years.remove(j);
                    }
                    else{
                        status.remove(i);
                        years.remove(i);
                    }
                    i = -1;
                    break;
                }
            }
        }

        TreeMap<String, String> treeMap = new TreeMap();
        for (int i = 0; i < years.size(); i++) {
            treeMap.put(years.get(i), status.get(i));
        }

        years.clear();
        status.clear();
        for (Map.Entry<String, String> entry : treeMap.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();

            years.add(key);
            status.add(value);
        }


        textFieldsForTime.clear();

        Button[] buttons = new Button[years.size()];
        Button[] buttonsFinish = new Button[years.size()];

        pane.getChildren().clear();
        pane.setPrefHeight(years.size() * 80 + 30);
        int counter = 0;
        int helpCounter = 0;
        for (int i = 0; i < years.size(); i++) {
            HelpPrintTeacher(years.get(i), status.get(i), counter);
            if(numClicked == 1){
                buttons[counter] = new Button("Submit");
                final int m = i;
                buttons[counter].setOnAction(event -> {
                    if(textFieldsForTime.get(m).getText() == null){
                        clientNetwork.saveTimeForSelectionUnit(teacher.getCollege() + "", Integer.parseInt(years.get(m)), "");
                        PrintForPane();
                        return;
                    }
                    else if(textFieldsForTime.get(m).getText().equals("")){
                        clientNetwork.saveTimeForSelectionUnit(teacher.getCollege() + "", Integer.parseInt(years.get(m)), textFieldsForTime.get(m).getText());
                        PrintForPane();
                        return;
                    }

                    if(textFieldsForTime.get(m).getText().length() != 10){
                        Alert alert2 = new Alert(Alert.AlertType.ERROR);
                        alert2.setTitle("Incorrect");
                        alert2.setHeaderText("Selected day format incorrect");
                        log.error("Incorrect selected day format");

                        alert2.showAndWait();
                        return;
                    }
                    for (int j = 0; j < textFieldsForTime.get(m).getText().length(); j++) {

                        if(j == 0 || j == 1 || j == 2 || j == 3 | j == 5 || j == 6 || j == 8 || j == 9){
                            if (!(textFieldsForTime.get(m).getText().charAt(j) >= '0' && textFieldsForTime.get(m).getText().charAt(j) <= '9')) {
                                Alert alert2 = new Alert(Alert.AlertType.ERROR);
                                alert2.setTitle("Incorrect");
                                alert2.setHeaderText("Selected day format incorrect");
                                log.error("Incorrect selected day format");

                                alert2.showAndWait();
                                return;
                            }
                        }
                        else if(j == 4 || j == 7){
                            if (!(textFieldsForTime.get(m).getText().charAt(j) == '-')) {
                                Alert alert2 = new Alert(Alert.AlertType.ERROR);
                                alert2.setTitle("Incorrect");
                                alert2.setHeaderText("Selected day format incorrect");
                                log.error("Incorrect selected day format");

                                alert2.showAndWait();
                                return;
                            }
                        }
                    }

                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setHeaderText("Are you sure?");
                    if(alert.showAndWait().get() == ButtonType.OK){
                        clientNetwork.saveTimeForSelectionUnit(teacher.getCollege() + "", Integer.parseInt(years.get(m)), textFieldsForTime.get(m).getText());
                        PrintForPane();
                    }
                });
                buttons[counter].setCursor(Cursor.HAND);
                buttons[counter].setLayoutX(460);
                buttons[counter].setLayoutY(12 + 80 * counter);
                buttons[counter].setBackground(new Background(new BackgroundFill(teacher.getColor() , null, null)));

                pane.getChildren().add(buttons[counter]);
                if (status.get(i).equals("")) {

                }
                else{
                    String[] s = status.get(i).split("-");
                    LocalDateTime localDateTime = LocalDateTime.now();
                    if ((Integer.parseInt(s[0]) == localDateTime.getYear() && Integer.parseInt(s[1]) == localDateTime.getMonthValue() && Integer.parseInt(s[2]) == localDateTime.getDayOfMonth())) {
                        buttonsFinish[helpCounter] = new Button("Finish");
                        buttonsFinish[helpCounter].setOnAction(event -> {
                            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                            alert.setHeaderText("Are you sure?");
                            if(alert.showAndWait().get() == ButtonType.OK){
                                clientNetwork.finishTimeSelection(years.get(m), teacher.getCollege() + "");
                                PrintForPane();
                            }
                        });
                        buttonsFinish[helpCounter].setCursor(Cursor.HAND);
                        buttonsFinish[helpCounter].setLayoutX(660);
                        buttonsFinish[helpCounter].setLayoutY(12 + 80 * counter);
                        buttonsFinish[helpCounter].setBackground(new Background(new BackgroundFill(teacher.getColor() , null, null)));
                        pane.getChildren().add(buttonsFinish[helpCounter]);
                        helpCounter++;
                    }
                }

            }
            counter++;
        }
    }

    public void HelpPrintTeacher(String year, String status, int counter){

        Font font = Font.font("System", FontWeight.NORMAL, FontPosture.REGULAR, 20);

        Label label1 = new Label(year);
        label1.prefHeight(142);
        label1.prefWidth(57);
        label1.setLayoutX(13);
        label1.setLayoutY(12 + 80 * counter);
        label1.setFont(font);
        pane.getChildren().add(label1);

        TextField textFieldForExam = new TextField();
        textFieldForExam.setText(status);
        textFieldForExam.minHeight(187);
        textFieldForExam.minWidth(31);
        textFieldForExam.setLayoutX(255);
        textFieldForExam.setLayoutY(12 + 80 * counter);


        pane.getChildren().add(textFieldForExam);
        textFieldsForTime.add(textFieldForExam);


        if(numClicked == 0){
            textFieldForExam.setDisable(true);
        }
        else{
            textFieldForExam.setDisable(false);
        }

    }

    public void EditButtonFunction(ActionEvent e) {
        if(numClicked == 0){
            EditButton.setText("OK");
        }
        else{
            textFieldsForTime.clear();
            EditButton.setText("Edit");
        }

        numClicked++;
        numClicked = numClicked % 2;

        PrintForPane();
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

