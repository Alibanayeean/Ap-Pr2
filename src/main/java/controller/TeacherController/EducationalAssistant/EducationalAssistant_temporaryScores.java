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
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
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

public class EducationalAssistant_temporaryScores implements Initializable{
    public static final Logger log = LogManager.getLogger(EducationalAssistant_temporaryScores.class);


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
    Button SubmitButton;

    @FXML
    Button FinalButton;

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

    Timeline timelineForConnected;

    @FXML
    Button reconnectionButton;

    @FXML
    ImageView connectionStatus;

    String courseId = "";



    LinkedList<TextField> textFields = new LinkedList<>();
    LinkedList<TextField> textFieldsForAnswer = new LinkedList<>();
    LinkedList<Integer> IDForTextFields = new LinkedList<>();

    Stage stage;

    @FXML
    void ChangingColor(ActionEvent event) {
        Color color = colorPicker.getValue();
        MenuBar.setBackground(new Background(new BackgroundFill(color, null, null)));
        timeShowLabel.setBackground(new Background(new BackgroundFill(color, null, null)));
        HomeButton.setBackground(new Background(new BackgroundFill(color, null, null)));
        SubmitButton.setBackground(new Background(new BackgroundFill(color, null, null)));
        FinalButton.setBackground(new Background(new BackgroundFill(color, null, null)));
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
        SubmitButton.setBackground(new Background(new BackgroundFill(color, null, null)));
        FinalButton.setBackground(new Background(new BackgroundFill(color, null, null)));
        colorPicker.setValue(color);
        LinkedList<Course> course = clientNetwork.GiveCoursesOfATeacher(teacher.getCourses());
        ObservableList<String> list = ChoiceBoxCourseName.getItems();

        for (int i = 0; i < course.size(); i++) {
            list.add(course.get(i).getName());
        }

        ChoiceBoxCourseName.setOnAction(event -> {

            PrintForPane(getChoiceBoxResult());

        });

        timelineForConnected = PublicMethods.reconnectionTimeline(reconnectionButton, clientNetwork, connectionStatus);

    }

    public void PrintForPane(String string){

        textFields.clear();
        IDForTextFields.clear();
        textFieldsForAnswer.clear();

        pane.getChildren().clear();

        pane.getChildren().add(FirstNameLabel);
        pane.getChildren().add(LastNameLabel);
        pane.getChildren().add(CollegeLabel);
        pane.getChildren().add(GradeLabel);
        pane.getChildren().add(studentNumberLabel);

        LinkedList<Course> getCoursesOfATeacher = clientNetwork.getCoursesOfATeacher(teacher.getId());
        String courseId = "";
        for (int i = 0; i < getCoursesOfATeacher.size(); i++) {
            if(getCoursesOfATeacher.get(i).getName().equals(getChoiceBoxString())){
                courseId = getCoursesOfATeacher.get(i).getId();;
                this.courseId = courseId;
                break;
            }
        }
        LinkedList<Student> students = clientNetwork.GiveStudentsOfACoursesFromString(courseId);


        pane.setPrefHeight(students.size() * 200 + 30);
        pane.setPrefWidth(1800);

        int counter = 0;

        for (int i = 0; i < students.size(); i++) {
            HelpPrintTeacher(counter, students.get(i));
            counter++;
        }

    }

    public String getChoiceBoxString(){
        String s = (String) ChoiceBoxCourseName.getValue();
        if(s == null){
            return "";
        }
        return s;
    }

    public void HelpPrintTeacher(int counter, Student student){

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

        Label label3 = new Label(student.getCollege() + "");
        label3.prefHeight(142);
        label3.prefWidth(57);
        label3.setLayoutX(402);
        label3.setLayoutY(85 + 200 * counter);
        label3.setFont(font);
        pane.getChildren().add(label3);

        Label label4 = new Label(student.getGrade() + "");
        label4.prefHeight(142);
        label4.prefWidth(57);
        label4.setLayoutX(630);
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


        int index = clientNetwork.FindIndexCourseFormAStudentFromCourseName(student.getId(),courseId);
        if(index == -1){

        }
        else if(clientNetwork.getScoreForCourseStudent(student.getId(), index) == -1){
            textFieldForProtest.setText(student.getProtestForCourseStudent().get(index) + "");
            textFieldForAnswer.setText(student.getAnswerTeacher().get(index) + "");
        }
        else{
            textFieldForGetScore.setText(student.getCourses_thisTerm_Score().get(index) + "");
            textFieldForProtest.setText(clientNetwork.getProtestForCourseStudent(student.getId(), index));
            textFieldForAnswer.setText(clientNetwork.getAnswerForCourseStudent(student.getId(), index));
        }

        pane.getChildren().add(textFieldForGetScore);
        pane.getChildren().add(textFieldForProtest);
        pane.getChildren().add(textFieldForAnswer);


        textFields.add(textFieldForGetScore);
        textFieldsForAnswer.add(textFieldForAnswer);
        IDForTextFields.add(student.getId());

    }

    public void SubmitButtonFunction(ActionEvent e) {
        Submit();
    }

    public void Submit(){
        String s = ChoiceBoxCourseName.getValue() + "";
        if(s.equals("")){
            return;
        }
        for (int i = 0; i < textFields.size(); i++) {
            Student student = clientNetwork.FindStudentFromId(IDForTextFields.get(i));
            int indexCourseFormAStudent = clientNetwork.FindIndexCourseFormAStudentFromCourseName(student.getId(), courseId);
            if(indexCourseFormAStudent == -1){
                continue;
            }
            else if(textFields.get(i).getText() == null){
                student.getCourses_thisTerm_Score().set(indexCourseFormAStudent, -1.0);
                student.getAnswerTeacher().set(indexCourseFormAStudent, textFieldsForAnswer.get(i).getText());
                clientNetwork.setAnswerAndScoreForStudent(student.getId(), indexCourseFormAStudent, -1.0, textFieldsForAnswer.get(i).getText());

                log.info("score set");

                continue;
            }
            else if(textFields.get(i).getText().equals("")){
                if(textFieldsForAnswer.get(i).getText().equals("")){
                    textFieldsForAnswer.get(i).setText(" ");
                }
                student.getCourses_thisTerm_Score().set(indexCourseFormAStudent, -1.0);
                student.getAnswerTeacher().set(indexCourseFormAStudent, textFieldsForAnswer.get(i).getText());
                clientNetwork.setAnswerAndScoreForStudent(student.getId(), indexCourseFormAStudent, -1.0, textFieldsForAnswer.get(i).getText());

                log.info("score set");

                continue;
            }
            boolean b = checkValidScore(textFields.get(i).getText());
            if(!b){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("ERROR");
                alert.setHeaderText("Score student with student number " + student.getStudentNumber() + " is not correct");
                alert.showAndWait();
                log.error("invalid score");

                continue;
            }
            boolean b1 = validInputForProtestAndAnswer(textFieldsForAnswer.get(i).getText());
            if(!b1){
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning");
                alert.setHeaderText("Your answer shouldn't have :::");
                alert.showAndWait();
                log.error("invalid answer input");


                continue;
            }
            else{
                int aInteger = (int) Double.parseDouble(textFields.get(i).getText());
                double aDouble = Double.parseDouble(textFields.get(i).getText());
                double score = 0.0;
                if(aInteger >= 20){
                    score = 20.0;
//                    student.getCourses_thisTerm_Score().set(indexCourseFormAStudent, (double) aInteger);
                    log.info("score set");

                }
                else if(aDouble - aInteger == 0){
                    score = aDouble;
//                    student.getCourses_thisTerm_Score().set(indexCourseFormAStudent, aDouble);
                    log.info("score set");

                }
                else if(aDouble - aInteger >= 0 & aDouble - aInteger <= 0.25){
                    score = aInteger + 0.25;
//                    student.getCourses_thisTerm_Score().set(indexCourseFormAStudent, aInteger + 0.25);
                    log.info("score set");

                }
                else if(aDouble - aInteger > 0.25 & aDouble - aInteger <= 0.5){
                    score = aInteger + 0.5;
//                    student.getCourses_thisTerm_Score().set(indexCourseFormAStudent, aInteger + 0.5);
                    log.info("score set");

                }
                else if(aDouble - aInteger > 0.5 & aDouble - aInteger <= 0.75){
                    score = aInteger + 0.75;
//                    student.getCourses_thisTerm_Score().set(indexCourseFormAStudent, aInteger + 0.75);
                    log.info("score set");

                }
                else if(aDouble - aInteger > 0.75 & aDouble - aInteger < 1){
                    score = aInteger + 1;
                    student.getCourses_thisTerm_Score().set(indexCourseFormAStudent, (double) (aInteger + 1));
                    log.info("score set");

                }

                if(textFieldsForAnswer.get(i).getText().equals("")){
                    textFieldsForAnswer.get(i).setText(" ");
                }

                student.getCourses_thisTerm_Score().set(indexCourseFormAStudent, score);
                student.getAnswerTeacher().set(indexCourseFormAStudent, textFieldsForAnswer.get(i).getText());
                clientNetwork.setAnswerAndScoreForStudent(student.getId(), indexCourseFormAStudent, score, textFieldsForAnswer.get(i).getText());

            }
        }

        PrintForPane(getChoiceBoxResult());


    }

    public void FinalButtonFunction(ActionEvent event) {
        Submit();

        LinkedList<Course> getCoursesOfATeacher = clientNetwork.getCoursesOfATeacher(teacher.getId());
        String courseId = "";
        for (int i = 0; i < getCoursesOfATeacher.size(); i++) {
            if(getCoursesOfATeacher.get(i).getName().equals(getChoiceBoxString())){
                courseId = getCoursesOfATeacher.get(i).getId();
                this.courseId = courseId;
                break;
            }
        }
        LinkedList<Student> students = clientNetwork.GiveStudentsOfACoursesFromString(courseId);


        for (Student student : students) {
            int index = clientNetwork.FindIndexCourseFormAStudentFromCourseName(student.getId(),courseId);
            if(!(student.getCourses_thisTerm_Score().get(index) >= 0 && student.getCourses_thisTerm_Score().get(index) <= 20)){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("ERROR");
                alert.setHeaderText("Student with student number " + student.getStudentNumber() + " don't have score!!!");
                alert.showAndWait();
                log.error("all students don't have score");

                return;
            }
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("Are you sure?");
        if(alert.showAndWait().get() == ButtonType.OK){

            clientNetwork.RemoveCourseFromATeachersAndStudents(ChoiceBoxCourseName.getValue() + "", teacher);
            pane.getChildren().clear();

        }
    }

    public boolean checkValidScore(String s){
        if(s == null)
            return false;
        if(s.equals("")){
            return false;
        }
        if(s.charAt(0) == '.'){
            return false;
        }
        for (int i = 0; i < s.length(); i++) {
            if(!((s.charAt(i) >= '0' & s.charAt(i) <= '9') | s.charAt(i) == '.')){
                return false;
            }
        }
        double a = Double.parseDouble(s);
        if(!(a >= 0 & a <= 20.0)){
            return false;
        }
        return true;

    }

    public boolean validInputForProtestAndAnswer(String s){
        for (int i = 0; i < s.length() - 3; i++) {
            if(s.charAt(i) == ':' & s.charAt(i + 1) == ':' & s.charAt(i + 2) == ':'){
                return false;
            }
        }
        return true;
    }

    public String getChoiceBoxResult(){
        String s = (String) ChoiceBoxCourseName.getValue();
        if(s == null){
            return "";
        }
        return s;
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
