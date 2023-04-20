package controller.TeacherController.EducationalAssistant;


import back.course.Course;
import back.persons.Student;
import back.persons.Teacher;
import controller.publicController.LoginPage;
import controller.publicMethods.PublicMethods;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
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

public class EducationalAssistant_temporaryScoresForAllStudents implements Initializable {

    public static final Logger log = LogManager.getLogger(EducationalAssistant_temporaryScoresForAllStudents.class);

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
    ChoiceBox ChoiceBoxCourseName;

    @FXML
    Label FirstNameLabel;

    @FXML
    Label LastNameLabel;

    @FXML
    Label CollegeLabel;

    @FXML
    Label GradeLabel;

    @FXML
    Label studentNumberLabel;

    @FXML
    Label LabelForSummaryCourse;

    @FXML
    TextField TeacherLastNameTextField;

    @FXML
    TextField StudentNumberTextField;

    @FXML
    Button reconnectionButton;

    @FXML
    ImageView connectionStatus;


    String ChoiceBoxString = "";

    Timeline timeline;


    Stage stage;
    @FXML
    void ChangingColor(ActionEvent event) {
        Color color = colorPicker.getValue();
        MenuBar.setBackground(new Background(new BackgroundFill(color, null, null)));
        timeShowLabel.setBackground(new Background(new BackgroundFill(color, null, null)));
        HomeButton.setBackground(new Background(new BackgroundFill(color, null, null)));
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
    }

    public void setTeacher(Teacher teacher, ClientNetwork clientNetwork){
        this.clientNetwork = clientNetwork;
        this.teacher = teacher;

        Color color = teacher.color;
        MenuBar.setBackground(new Background(new BackgroundFill(color, null, null)));
        timeShowLabel.setBackground(new Background(new BackgroundFill(color, null, null)));
        HomeButton.setBackground(new Background(new BackgroundFill(color, null, null)));
        colorPicker.setValue(color);
        LinkedList<Course> course = clientNetwork.GiveCoursesOfACollege(teacher.getCollege() + "");
        ObservableList<String> list = ChoiceBoxCourseName.getItems();

        for (int i = 0; i < course.size(); i++) {
            list.add(course.get(i).getName());
        }
        list.add("----");

        ChoiceBoxCourseName.setOnAction(event -> {
            String choiceBox = (String) ChoiceBoxCourseName.getValue();
            if(choiceBox == null){

            }
            else if(choiceBox.equals("----")){
                choiceBox = "";
            }
            LinkedList<Course> c = clientNetwork.ShowTemperoryScoresForEA(teacher.getId(), choiceBox, TeacherLastNameTextField.getText());
            PrintForPane(c);
        });

        TeacherLastNameTextField.setOnAction(event -> {
            String choiceBox = (String) ChoiceBoxCourseName.getValue();
            if(choiceBox == null){

            }
            else if(choiceBox.equals("----")){
                choiceBox = "";
            }
            LinkedList<Course> c = clientNetwork.ShowTemperoryScoresForEA(teacher.getId(), choiceBox, TeacherLastNameTextField.getText());
            PrintForPane(c);
        });

        StudentNumberTextField.setOnAction(event -> {
            String choiceBox = (String) ChoiceBoxCourseName.getValue();
            if(choiceBox == null){

            }
            else if(choiceBox.equals("----")){
                choiceBox = "";
            }
            LinkedList<Course> c = clientNetwork.ShowTemperoryScoresForEA(teacher.getId(), choiceBox, TeacherLastNameTextField.getText());
            PrintForPane(c);
        });

        PrintForPane(course);

        timeline = new Timeline(new KeyFrame(Duration.seconds(0.1), new EventHandler<ActionEvent>(){

            int timesForFirst = 0;
            int timesForSecond = 0;

            @Override
            public void handle(ActionEvent event) {
                LinkedList<Course> course = clientNetwork.GiveCoursesOfACollege(teacher.getCollege() + "");
                PrintForPane(course);

                if(clientNetwork.isConnected && timesForFirst == 0){
                    connectionStatus.setVisible(true);
                    reconnectionButton.setVisible(false);
                    File file = new File(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.publicURL) + "connected.png");
                    Image image2 = new Image(file.toURI().toString());
                    connectionStatus.setImage(image2);
                    timesForFirst++;
                    timesForSecond = 0;
                }
                else if(!clientNetwork.isConnected && timesForSecond == 0){
                    reconnectionButton.setVisible(true);
                    connectionStatus.setVisible(false);
                    File file = new File(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.publicURL) + "reconnectionButton.png");
                    Image image2 = new Image(file.toURI().toString());
                    reconnectionButton.setGraphic(new ImageView(image2));
                    timesForSecond++;
                    timesForFirst = 0;

                    reconnectionButton.setOnAction(e -> {
                        clientNetwork.attemptToMakeNewConnection();
                        if(clientNetwork.isConnected){
                            reconnectionButton.setVisible(false);
                            File file1 = new File(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.publicURL) + "connected.png");
                            Image image3 = new Image(file1.toURI().toString());
                            connectionStatus.setImage(image3);
                        }
                    });

                }
            }

        }));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
        
        
        
    }

    public void PrintForPane(LinkedList<Course> courses){
        String choiceBox = (String) ChoiceBoxCourseName.getValue();

        if(choiceBox == null){
            LabelForSummaryCourse.setText("");
        }
        else if(choiceBox.equals("") | choiceBox.equals("----") ){
            LabelForSummaryCourse.setText("");
        }

        else{
            Font font = Font.font("System", FontWeight.NORMAL, FontPosture.REGULAR, 17);

            Course course = clientNetwork.FindACourseFormACollege(choiceBox , teacher.getCollege() + "");
            LinkedList<String> help = clientNetwork.getNumPassForACourse(course.getId());
            if(help.get(3).equals("0")){
                help.set(3, "-----");
            }
            LabelForSummaryCourse.setText("Name: " + choiceBox + "    Id: " + course.getId() + "    Num all students: "+ help.get(0) +"    Num pass: " + help.get(1) + "    Num fails: " + help.get(2) + "   Average: "+ help.get(3));

            LabelForSummaryCourse.setAlignment(Pos.CENTER);
            LabelForSummaryCourse.setFont(font);
        }

        pane.getChildren().clear();

        pane.getChildren().add(FirstNameLabel);
        pane.getChildren().add(LastNameLabel);
        pane.getChildren().add(CollegeLabel);
        pane.getChildren().add(GradeLabel);
        pane.getChildren().add(studentNumberLabel);
        int counter = 0;

        int StudentsSize = 0;
        for (int i = 0; i < courses.size(); i++) {

            LinkedList<Course> getCoursesOfATeacher = clientNetwork.getCoursesOfATeacher(teacher.getId());
            String courseId = "";
            for (int j = 0; j < getCoursesOfATeacher.size(); j++) {
                if(getCoursesOfATeacher.get(j).getName().equals(getChoiceBoxString())){
                    courseId = getCoursesOfATeacher.get(j).getId();
                    break;
                }
            }
            LinkedList<Student> students = clientNetwork.GiveStudentsOfACoursesFromString(courseId);


            StudentsSize += students.size();
            for (int j = 0; j < students.size(); j++) {
                if(StudentNumberTextField.getText() == null){
                    HelpPrintTeacher(counter, students.get(j), courses.get(i));
                    counter++;
                }
                else if(StudentNumberTextField.getText().equals("")){
                    HelpPrintTeacher(counter, students.get(j), courses.get(i));
                    counter++;
                }
                else{
                    if(students.get(j).getStudentNumber().equals(StudentNumberTextField.getText())){
                        HelpPrintTeacher(counter, students.get(j), courses.get(i));
                        counter++;
                    }
                }


            }

        }
        pane.setPrefHeight(StudentsSize * 200 + 30);

        pane.setPrefWidth(1800);



    }

    public void HelpPrintTeacher(int counter, Student student, Course course){

        Font font = Font.font("System", FontWeight.NORMAL, FontPosture.REGULAR, 20);

        Label label1 = new Label(student.getFirstname());
        label1.prefHeight(142);
        label1.prefWidth(57);
        label1.setLayoutX(14);
        label1.setLayoutY(85 + 200 * counter);
        label1.setFont(font);
        pane.getChildren().add(label1);

        Label label2 = new Label(student.getLastname());
        label2.prefHeight(142);
        label2.prefWidth(57);
        label2.setLayoutX(217);
        label2.setLayoutY(85 + 200 * counter);
        label2.setFont(font);
        pane.getChildren().add(label2);

        Label label3 = new Label(course.getName() + "");
        label3.prefHeight(142);
        label3.prefWidth(57);
        label3.setLayoutX(424);
        label3.setLayoutY(85 + 200 * counter);
        label3.setFont(font);
        pane.getChildren().add(label3);

        String string = clientNetwork.getTeacherOfACourse(course.getId());
        Label label4 = new Label(string);
        label4.prefHeight(142);
        label4.prefWidth(57);
        label4.setLayoutX(616);
        label4.setLayoutY(85 + 200 * counter);
        label4.setFont(font);
        pane.getChildren().add(label4);

        Label label6 = new Label(student.getStudentNumber() + "");
        label6.prefHeight(100);
        label6.prefWidth(57);
        label6.setLayoutX(824);
        label6.setLayoutY(85 + 200 * counter);
        label6.setFont(font);
        pane.getChildren().add(label6);

        TextField textFieldForGetScore = new TextField();
        textFieldForGetScore.setPrefHeight(33);
        textFieldForGetScore.setPrefWidth(100);
        textFieldForGetScore.setLayoutX(1064);
        textFieldForGetScore.setLayoutY(85 + 200 * counter);
        textFieldForGetScore.setDisable(true);


        TextField textFieldForProtest = new TextField();
        textFieldForProtest.setPrefHeight(100);
        textFieldForProtest.setPrefWidth(200);
        textFieldForProtest.setLayoutX(1264);
        textFieldForProtest.setLayoutY(60 + 200 * counter);
        textFieldForProtest.setDisable(true);

        TextField textFieldForAnswer = new TextField();
        textFieldForAnswer.setPrefHeight(100);
        textFieldForAnswer.setPrefWidth(200);
        textFieldForAnswer.setLayoutX(1550);
        textFieldForAnswer.setLayoutY(60 + 200 * counter);
        textFieldForAnswer.setDisable(true);


        int index = clientNetwork.FindIndexCourseFormAStudentFromCourseName(student.getId(), course.getId());
        if(index == -1){

        }
        else if(student.getCourses_thisTerm_Score().get(index) == -1){
            textFieldForProtest.setText(student.getProtestForCourseStudent().get(index) + "");
            textFieldForAnswer.setText(student.getAnswerTeacher().get(index) + "");
        }
        else{
            textFieldForGetScore.setText(student.getCourses_thisTerm_Score().get(index) + "");
            textFieldForProtest.setText(student.getProtestForCourseStudent().get(index) + "");
            textFieldForAnswer.setText(student.getAnswerTeacher().get(index) + "");
        }

        pane.getChildren().add(textFieldForGetScore);
        pane.getChildren().add(textFieldForProtest);
        pane.getChildren().add(textFieldForAnswer);


    }

    public String getChoiceBoxString(){
        String s = (String) ChoiceBoxCourseName.getValue();
        if(s == null){
            return "";
        }
        return s;
    }

    public void HomeButtonFunction(ActionEvent e) {
        
        timeline.stop();
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
        
        timeline.stop();
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
        
        timeline.stop();
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
        
        timeline.stop();
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
        
        timeline.stop();
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
        
        timeline.stop();
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
        
        timeline.stop();
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
        
        timeline.stop();
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
        
        timeline.stop();
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
        
        timeline.stop();
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
        
        timeline.stop();
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
        
        timeline.stop();
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
        
        timeline.stop();
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
        
        timeline.stop();
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
