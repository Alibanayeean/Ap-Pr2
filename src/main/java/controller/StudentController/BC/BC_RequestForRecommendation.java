package controller.StudentController.BC;

import back.enums.Request;
import back.persons.Student;
import back.persons.Teacher;
import clientNetwork.ClientNetwork;
import config.ConfigIdentifier;
import config.ReadPropertyFile;
import controller.StudentController.CW.publicPages.CW;
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
import org.apache.log4j.LogManager;

public class BC_RequestForRecommendation implements Initializable{
    public static final Logger log = LogManager.getLogger(BC_RequestForRecommendation.class);

    Student student;
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
    TextField TeacherField;

    @FXML
    Pane pane;

    LinkedList<Integer> idsForButton = new LinkedList<>();

    Timeline timeline;

    Stage stage;

    @FXML
    Button reconnectionButton;

    @FXML
    ImageView connectionStatus;

    @FXML
    void ChangingColor(ActionEvent event) {
        Color color = colorPicker.getValue();
        MenuBar.setBackground(new Background(new BackgroundFill(color, null, null)));
        timeShowLabel.setBackground(new Background(new BackgroundFill(color, null, null)));
        HomeButton.setBackground(new Background(new BackgroundFill(color, null, null)));

        student.color = colorPicker.getValue();
        String s = "#" + Integer.toHexString(color.hashCode());
        clientNetwork.changeColorForStudent(student.getStudentNumber(), s);

        log.info("change background");

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        PublicMethods.initImageOfLogOutButton(LogOutButton);

        PublicMethods.initImageOfHomeButton(HomeButton);

        PublicMethods.initClock(timeShowLabel);
    }

    public void setStudent(Student student, ClientNetwork clientNetwork){
        this.clientNetwork = clientNetwork;
        this.student = student;

        Color color = student.color;
        MenuBar.setBackground(new Background(new BackgroundFill(color, null, null)));
        timeShowLabel.setBackground(new Background(new BackgroundFill(color, null, null)));
        HomeButton.setBackground(new Background(new BackgroundFill(color, null, null)));
        colorPicker.setValue(color);

        LinkedList<Request> requests = clientNetwork.getRequestForRecommendation(student.getId());
        if(requests == null){

        }
        else{
            for (int i = 0; i < requests.size(); i++) {
                if(requests.get(i) == Request.Reject){
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Wait");
                    String teacherLastName = clientNetwork.getLastNameHelpTeacherFromId(clientNetwork.getIdTeacherForRequest(student.getId(), i));
                    alert.setHeaderText("You are Rejected by Mr./Mrs. " + teacherLastName);
                    alert.show();

                    log.info("Rejected for recome");

                    clientNetwork.changeRecomeForStudentBecauseShowed(student.getId(), i);

                }
                else if(requests.get(i) == Request.Accept){
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Wait");
                    String teacherLastName = clientNetwork.getLastNameHelpTeacherFromId(clientNetwork.getIdTeacherForRequest(student.getId(), i));
                    alert.setHeaderText("You are Accepted by Mr./Mrs. " + teacherLastName);log.info("change background");
                    alert.show();

                    log.info("Accepted for recome");

                    clientNetwork.changeRecomeForStudentBecauseShowed(student.getId(), student.getIdTeacherForRequest().get(i));

                }
            }
        }

        TeacherField.setOnAction(event -> {
            String stringChooseTeacherName = chooseTeacherName();
            LinkedList<Teacher> t = clientNetwork.StringToTeacher(stringChooseTeacherName);
            PrintForPane(t);
        });

        timeline = new Timeline(new KeyFrame(Duration.seconds(5), new EventHandler<ActionEvent>(){
            int timesForFirst = 0;
            int timesForSecond = 0;
            int size = 0;

            @Override
            public void handle(ActionEvent event) {
                String stringChooseTeacherName = chooseTeacherName();
                LinkedList<Request> requests = clientNetwork.getRequestForRecommendation(student.getId());
                if(requests == null){

                }
                else{
                    for (int i = 0; i < requests.size(); i++) {
                        if(requests.get(i) == Request.Reject){
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("Wait");
                            String teacherLastName = clientNetwork.getLastNameHelpTeacherFromId(clientNetwork.getIdTeacherForRequest(student.getId(), i));
                            alert.setHeaderText("You are Rejected by Mr./Mrs. " + teacherLastName);
                            alert.show();

                            log.info("Rejected for recome");

                            clientNetwork.changeRecomeForStudentBecauseShowed(student.getId(), i);
                        }
                        else if(requests.get(i) == Request.Accept){
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("Wait");
                            String teacherLastName = clientNetwork.getLastNameHelpTeacherFromId(clientNetwork.getIdTeacherForRequest(student.getId(), i));
                            alert.setHeaderText("You are Accepted by Mr./Mrs. " + teacherLastName);log.info("change background");
                            alert.show();

                            log.info("Accepted for recome");

                            clientNetwork.changeRecomeForStudentBecauseShowedWithIdTeacherShouldAddToAccepted(student.getId(), i, student.getIdTeacherForRequest().get(i));
                        }
                    }
                }

                LinkedList<Teacher> t = clientNetwork.StringToTeacher(stringChooseTeacherName);
                if(size != t.size()){
                    size = t.size();
                    PrintForPane(t);
                }

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

    public void PrintForPane(LinkedList<Teacher> teachers){

        Button[] buttons = new Button[teachers.size()];
        idsForButton.clear();
        pane.getChildren().clear();
        pane.setPrefHeight(teachers.size() * 80 + 30);
        int counter = 0;


        for (int i = 0; i < teachers.size(); i++) {
            idsForButton.add(teachers.get(i).getId());
            buttons[counter] = new Button("Send");
            final int m = i;
            buttons[counter].setOnAction(event -> {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setHeaderText("Are you sure?");

                if(alert.showAndWait().get() == ButtonType.OK){

                    SubmitButtonFunction(idsForButton.get(m));

                    String stringChooseTeacherName = chooseTeacherName();
                    LinkedList<Teacher> t = clientNetwork.StringToTeacher(stringChooseTeacherName);
                    PrintForPane(t);
                }
            });

            buttons[counter].setOnMouseEntered(event -> {
                Scene scene;
                scene = ((Scene)((Node)(event.getSource())).getScene());
                scene.setCursor(Cursor.HAND);
            });

            buttons[counter].setOnMouseExited(event -> {
                Scene scene;
                scene = ((Scene)((Node)(event.getSource())).getScene());
                scene.setCursor(Cursor.DEFAULT);
            });

            buttons[counter].setLayoutX(1190);
            buttons[counter].setLayoutY(12 + 80 * counter);
            buttons[counter].setBackground(new Background(new BackgroundFill(student.getColor() , null, null)));
            pane.getChildren().add(buttons[counter]);



            HelpPrint(counter, teachers.get(i));
            counter++;
        }
    }

    public void HelpPrint(int counter, Teacher teacher){

        Font font = Font.font("System", FontWeight.NORMAL, FontPosture.REGULAR, 20);

        Label label1 = new Label(teacher.getFirstname());
        label1.prefHeight(142);
        label1.prefWidth(57);
        label1.setLayoutX(13);
        label1.setLayoutY(12 + 80 * counter);
        label1.setFont(font);
        pane.getChildren().add(label1);

        Label label2 = new Label(teacher.getLastname());
        label2.prefHeight(142);
        label2.prefWidth(57);
        label2.setLayoutX(274);
        label2.setLayoutY(12 + 80 * counter);
        label2.setFont(font);
        pane.getChildren().add(label2);

        Label label3 = new Label(teacher.getTeacherDegree() + "");
        label3.prefHeight(142);
        label3.prefWidth(57);
        label3.setLayoutX(543);
        label3.setLayoutY(12 + 80 * counter);
        label3.setFont(font);
        pane.getChildren().add(label3);

        Label label4 = new Label(teacher.getEmail() + "");
        label4.prefHeight(142);
        label4.prefWidth(57);
        label4.setLayoutX(795);
        label4.setLayoutY(12 + 80 * counter);
        label4.setFont(font);
        pane.getChildren().add(label4);


        Label label5 = new Label();

        label5.setText(teacher.getCollege() + "");
        label5.minHeight(187);
        label5.minWidth(31);
        label5.setLayoutX(1029);
        label5.setLayoutY(12 + 80 * counter);
        label5.setFont(font);
        pane.getChildren().add(label5);

    }

    @FXML
    void SubmitButtonFunction(int id){
        String stringChooseTeacherName = chooseTeacherName();

        if(stringChooseTeacherName == null){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Incorrect");
            alert.setHeaderText("Write one teacher");

            log.error("no teacher selected for recome");

            alert.showAndWait();
        }

        else{

            if(!clientNetwork.isValidTeacher(id)){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Error");
                alert.setHeaderText("There is no teacher with this last name");

                log.error("no teacher with this last name for recome");

                alert.showAndWait();
                return;
            }

            for (int i = 0; i < student.getIdTeacherForRequest().size(); i++) {
                if(student.getIdTeacherForRequest().get(i) == id){
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Error");
                    alert.setHeaderText("You one time choose this teacher");
                    alert.showAndWait();

                    log.error("one time choose this teacher for recome");

                    return;
                }
            }

            for (int i = 0; i < student.getAcceptedTeacherForRecome().size(); i++) {
                if(student.getAcceptedTeacherForRecome().get(i) == id){
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Error");
                    alert.setHeaderText("This teacher one time accepted you");
                    alert.showAndWait();

                    log.error("This teacher one time accepted you for request");

                    return;
                }
            }

            Alert alert1 = new Alert(Alert.AlertType.CONFIRMATION);
            alert1.setTitle("");

            String firstName = clientNetwork.FindTeacherFirstNameFromId(id);
            String lastName = clientNetwork.FindTeacherLastNameFromId(id);
            String college = clientNetwork.FindTeacherCollegeFromId(id);

            alert1.setHeaderText("your wanted teacher is : " + firstName + " " + lastName + " College:" + college);
            if(alert1.showAndWait().get() == ButtonType.OK){

                student.getIdTeacherForRequest().add(id);
                student.getRequestForRecommendation().add(Request.Wait);
                clientNetwork.addIdTeacherToStudentForRecome(student.getId(), id);

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("in waiting list");
                alert.setHeaderText("Your request will send to the selected teacher.");
                alert.showAndWait();

                log.error("request will send for recome");



            }
        }

    }

    public String chooseTeacherName (){
        String stringCollegeName = TeacherField.getText();
        return stringCollegeName ;
    }


    public void HomeButtonFunction(ActionEvent e) {
        
        timeline.stop();
        try{
            stage = ((Stage)(((Scene)timeShowLabel.getScene()).getWindow()));
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Fxml\\Student\\BC\\BCFirstPage.fxml"));
            Parent root = loader.load();
            BCFirstPage BCFirstPage = loader.getController();
            BCFirstPage.setStudent(student, clientNetwork);
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
            clientNetwork.changeLastLoginStudent(student.getId(), localDateTime.getSecond(), localDateTime.getMinute(), localDateTime.getHour(), localDateTime.getDayOfMonth(), localDateTime.getMonthValue(), localDateTime.getYear());




        }
        catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    public void CoursesListMenuFunction(ActionEvent event) {
        
        timeline.stop();

        try{
            stage = ((Stage)(((Scene)timeShowLabel.getScene()).getWindow()));
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Fxml\\Student\\BC\\BC_ShowCourseListMenu.fxml"));
            Parent root = loader.load();
            BC_ShowCourseListMenu BC_ShowCourseListMenu = loader.getController();
            BC_ShowCourseListMenu.setStudent(student, clientNetwork);
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
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Fxml\\Student\\BC\\BC_ShowTeacherListMenu.fxml"));
            Parent root = loader.load();
            BC_ShowTeacherListMenu BC_ShowTeacherListMenu = loader.getController();
            BC_ShowTeacherListMenu.setStudent(student, clientNetwork);
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
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Fxml\\Student\\BC\\BC_ShowWeeklySchedule.fxml"));
            Parent root = loader.load();
            BC_ShowWeeklySchedule BC_ShowWeeklySchedule = loader.getController();
            BC_ShowWeeklySchedule.setStudent(student, clientNetwork);
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
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Fxml\\Student\\BC\\BC_ShowProfile.fxml"));
            Parent root = loader.load();
            BC_ShowProfile BC_ShowProfile = loader.getController();
            BC_ShowProfile.setStudent(student, clientNetwork);
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
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Fxml\\Student\\BC\\BC_ShowExams.fxml"));
            Parent root = loader.load();
            BC_ShowExams BC_ShowExams = loader.getController();
            BC_ShowExams.setStudent(student, clientNetwork);
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
        catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    public void certificateStudentFunction(ActionEvent event) {
        
        timeline.stop();

        try{
            stage = ((Stage)(((Scene)timeShowLabel.getScene()).getWindow()));
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Fxml\\Student\\BC\\BC_CertificateStudent.fxml"));
            Parent root = loader.load();
            BC_CertificateStudent BC_CertificateStudent = loader.getController();
            BC_CertificateStudent.setStudent(student, clientNetwork);
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
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Fxml\\Student\\BC\\BC_Minor.fxml"));
            Parent root = loader.load();
            BC_Minor BC_Minor = loader.getController();
            BC_Minor.setStudent(student, clientNetwork);
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
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Fxml\\Student\\BC\\BC_withdrawalFromEducation.fxml"));
            Parent root = loader.load();
            BC_withdrawalFromEducation BC_withdrawalFromEducation = loader.getController();
            BC_withdrawalFromEducation.setStudent(student, clientNetwork);
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
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Fxml\\Student\\BC\\BC_RequestForRecommendation.fxml"));
            Parent root = loader.load();
            BC_RequestForRecommendation BC_RequestForRecommendation = loader.getController();
            BC_RequestForRecommendation.setStudent(student, clientNetwork);
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
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Fxml\\Student\\BC\\BC_temporaryScores.fxml"));
            Parent root = loader.load();
            BC_temporaryScores BC_temporaryScores = loader.getController();
            BC_temporaryScores.setStudent(student, clientNetwork);
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
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Fxml\\Student\\BC\\BC_StudentEducationalStatus.fxml"));
            Parent root = loader.load();
            BC_StudentEducationalStatus BC_StudentEducationalStatus = loader.getController();
            BC_StudentEducationalStatus.setStudent(student, clientNetwork);
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
        catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    public void cwFunction(ActionEvent event) {
        timeline.stop();
        try{
            stage = ((Stage)(((Scene)timeShowLabel.getScene()).getWindow()));
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Fxml\\Student\\CW\\publicPages\\CW.fxml"));
            Parent root = loader.load();
            CW cw = loader.getController();
            cw.setStudent(student, clientNetwork);
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
        catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

}
