package controller.TeacherController.EducationalAssistant;



import back.course.Course;
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

public class EducationalAssistant_ShowCourseList implements Initializable {
    public static final Logger log = LogManager.getLogger(EducationalAssistant_ShowCourseList.class);

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
    Button AddButton;

    @FXML
    Button EditButton;

    Timeline timelineForConnected;

    @FXML
    Button reconnectionButton;

    @FXML
    ImageView connectionStatus;

    LinkedList<TextField> textFieldsForExam = new LinkedList<>();
    LinkedList<TextField> textFieldsForTeachers = new LinkedList<>();
    LinkedList<String> IDForTextFields = new LinkedList<>();

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
        AddButton.setBackground(new Background(new BackgroundFill(color, null, null)));
        teacher.color = colorPicker.getValue();
        String s = "#" + Integer.toHexString(color.hashCode());
        clientNetwork.changeColorForTeacher(teacher.getUsername(), s);

        PrintForPane(clientNetwork.getAllCoursesFromACollege(teacher.getCollege() + "", "No"));
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
        AddButton.setBackground(new Background(new BackgroundFill(color, null, null)));
        colorPicker.setValue(color);


        PrintForPane(clientNetwork.getAllCoursesFromACollege(teacher.getCollege() + "", "No"));


        timelineForConnected = PublicMethods.reconnectionTimeline(reconnectionButton, clientNetwork, connectionStatus);

    }

    public void PrintForPane(LinkedList<Course> courses){

        textFieldsForExam.clear();
        IDForTextFields.clear();
        textFieldsForTeachers.clear();

        Button[] buttons = new Button[courses.size()];

        pane.getChildren().clear();
        pane.setPrefHeight(courses.size() * 80 + 30);
        int counter = 0;
        int help = 0;


        for (int i = 0; i < courses.size(); i++) {

            HelpPrintTeacher(counter, courses.get(i));


            if(numClicked == 1){
                buttons[counter] = new Button("Remove");
                final int m = i;
                buttons[counter].setOnAction(event -> {

                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setHeaderText("Are you sure?");
                    if(alert.showAndWait().get() == ButtonType.OK){
                        clientNetwork.removeACourse(courses.get(m).getId());
                        log.info("course remove");
                        PrintForPane(clientNetwork.getAllCoursesFromACollege(teacher.getCollege() + "", "No"));
                    }
                });
                buttons[counter].setCursor(Cursor.HAND);
                buttons[counter].setLayoutX(1239);
                buttons[counter].setLayoutY(12 + 80 * counter);
                buttons[counter].setBackground(new Background(new BackgroundFill(teacher.getColor() , null, null)));


                pane.getChildren().add(buttons[counter]);

            }


            counter++;
        }
    }

    public void HelpPrintTeacher(int counter, Course course){

        Font font = Font.font("System", FontWeight.NORMAL, FontPosture.REGULAR, 20);

        Label label1 = new Label(course.getName());
        label1.prefHeight(142);
        label1.prefWidth(57);
        label1.setLayoutX(13);
        label1.setLayoutY(12 + 80 * counter);
        label1.setFont(font);
        pane.getChildren().add(label1);

        Label label2 = new Label(course.getId() + "");
        label2.prefHeight(142);
        label2.prefWidth(57);
        label2.setLayoutX(255);
        label2.setLayoutY(12 + 80 * counter);
        label2.setFont(font);
        pane.getChildren().add(label2);


        String teacherName = clientNetwork.getTeacherOfACourse(course.getId());

        TextField textFieldForTeacher = new TextField();
        textFieldForTeacher.setText(teacherName);
        textFieldForTeacher.minHeight(187);
        textFieldForTeacher.minWidth(31);
        textFieldForTeacher.setLayoutX(460);
        textFieldForTeacher.setLayoutY(12 + 80 * counter);
        pane.getChildren().add(textFieldForTeacher);


        TextField textFieldForExam = new TextField();
        String month = course.getMonthExam() + "";
        if(course.getMonthExam() >= 0 && course.getMonthExam() <= 9){
            month =  "0" + course.getMonthExam();
        }

        String day = course.getDayExam() + "";
        if(course.getDayExam() >= 0 && course.getDayExam() <= 9){
            day =  "0" + course.getDayExam();
        }

        textFieldForExam.setText(course.getYearExam() +"-" + month + "-" + day);
        textFieldForExam.minHeight(187);
        textFieldForExam.minWidth(31);
        textFieldForExam.setLayoutX(684);
        textFieldForExam.setLayoutY(12 + 80 * counter);


        pane.getChildren().add(textFieldForExam);
        textFieldsForExam.add(textFieldForExam);
        IDForTextFields.add(course.getId());
        textFieldsForTeachers.add(textFieldForTeacher);


        if(numClicked == 0){
            textFieldForExam.setDisable(true);
            textFieldForTeacher.setDisable(true);
        }
        else{
            textFieldForExam.setDisable(false);
            textFieldForTeacher.setDisable(false);
        }

    }

    public void AddFunction(ActionEvent e) {
        try{
            stage = ((Stage)(((Scene)AddButton.getScene()).getWindow()));
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Fxml\\Teacher\\EducationalAssistant\\EducationalAssistant_ShowCourseList_AddNewCourse.fxml"));
            Parent root = loader.load();
            EducationalAssistant_ShowCourseList_AddNewCourse EducationalAssistant_ShowCourseList_AddNewCourse = loader.getController();
            EducationalAssistant_ShowCourseList_AddNewCourse.setTeacher(teacher, clientNetwork);
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
        catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    public void EditButtonFunction(ActionEvent e) {
        if(numClicked == 0){
            EditButton.setText("OK");
        }
        else{
            for (int i = 0; i < textFieldsForExam.size(); i++) {

                if(textFieldsForExam.get(i).getText().length() != 10){
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Incorrect");
                    alert.setHeaderText("Incorrect exam for course with id  " + IDForTextFields.get(i));
                    log.error("Incorrect exam");

                    alert.showAndWait();
                    return;
                }

                for (int j = 0; j < textFieldsForExam.get(i).getText().length(); j++) {

                    if(j == 0 || j == 1 || j == 2 || j == 3 | j == 5 || j == 6 || j == 8 || j == 9){
                        if (!(textFieldsForExam.get(i).getText().charAt(j) >= '0' && textFieldsForExam.get(i).getText().charAt(j) <= '9')) {
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Incorrect");
                            alert.setHeaderText("Exam incorrect");
                            log.error("Incorrect exam");

                            alert.showAndWait();
                            return;
                        }
                    }
                    else if(j == 4 || j == 7){
                        if (!(textFieldsForExam.get(i).getText().charAt(j) == '-')) {
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Incorrect");
                            alert.setHeaderText("Exam incorrect");
                            log.error("Incorrect exam");

                            alert.showAndWait();
                            return;
                        }
                    }
                }

                if(textFieldsForTeachers.get(i).getText() == null){
                    clientNetwork.RemoveACourseFormTeachers(IDForTextFields.get(i));

                }
                else if(textFieldsForTeachers.get(i).getText().equals("")){
                    clientNetwork.RemoveACourseFormTeachers(IDForTextFields.get(i));

                }
                Teacher teacher1 = clientNetwork.getLastNameHelpTeacherFromLastName(textFieldsForTeachers.get(i).getText());
                if(teacher1 == null){
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Incorrect");
                    alert.setHeaderText("invalid teacher");
                    log.error("invalid teacher");

                    alert.showAndWait();
                    return;
                }

                clientNetwork.AddAndRemoveACourseFormTeachers(IDForTextFields.get(i), teacher1.getId());
                Course course = clientNetwork.FindCourse(IDForTextFields.get(i));
                String s = textFieldsForExam.get(i).getText();

                int yearExam = Integer.parseInt(s.substring(0, 4));
                s = textFieldsForExam.get(i).getText();
                int monthExam =  Integer.parseInt(s.substring(5, 7));
                s = textFieldsForExam.get(i).getText();
                int dayExam = Integer.parseInt(s.substring(8, 10));
                clientNetwork.setExamDayForACourse(course.getId(), dayExam, monthExam, yearExam);
                course.setDayExam(dayExam);
                course.setMonthExam(monthExam);
                course.setYearExam(yearExam);
                log.info("edited succussfully");

                

            }

            textFieldsForExam.clear();
            IDForTextFields.clear();
            textFieldsForTeachers.clear();
            EditButton.setText("Edit");
        }

        numClicked++;
        numClicked = numClicked % 2;

        PrintForPane(clientNetwork.getAllCoursesFromACollege(teacher.getCollege() + "", "No"));

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
