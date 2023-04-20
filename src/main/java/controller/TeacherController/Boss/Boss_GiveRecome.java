package controller.TeacherController.Boss;



import back.course.Course;
import back.enums.Request;
import back.persons.Student;
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
import javafx.geometry.Pos;
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
import org.apache.log4j.LogManager;


public class Boss_GiveRecome implements Initializable {
    public static final Logger log = LogManager.getLogger(Boss_GiveRecome.class);


    Teacher teacher;
    ClientNetwork clientNetwork;

    @FXML
    Pane back;

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
    Label labelForMassage;

    Button buttonAccept = new Button("Accept");

    Button buttonReject = new Button("Reject");

    Stage stage;

    Timeline timeline;


    @FXML
    Button reconnectionButton;

    @FXML
    ImageView connectionStatus;

    boolean cabShowButtons = false;

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


        LinkedList<Student> students = clientNetwork.FindStudentsWantsToRecome(teacher.getId());
        PrintForPane(students);
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


        LinkedList<Student> students = clientNetwork.FindStudentsWantsToRecome(teacher.getId());
        PrintForPane(students);

        timeline = new Timeline(new KeyFrame(Duration.seconds(1), new EventHandler<ActionEvent>(){

            int timesForFirst = 0;
            int timesForSecond = 0;
            int size = 0;

            @Override
            public void handle(ActionEvent event) {

                LinkedList<Student> students = clientNetwork.FindStudentsWantsToRecome(teacher.getId());

                if(students.size() != size){
                    size = students.size();
                    PrintForPane(students);
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

    public void PrintForPane(LinkedList<Student> students){


        Button[] buttons = new Button[students.size()];

        if(!cabShowButtons){
            back.getChildren().clear();
            back.getChildren().add(labelForMassage);
        }

        pane.getChildren().clear();
        pane.setPrefHeight(students.size() * 80 + 30);
        int counter = 0;


        for (int i = 0; i < students.size(); i++) {

            buttons[counter] = new Button("Show");
            final int m = i;
            buttons[counter].setOnAction(event -> {
                ShowLabel(students.get(m));
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
            buttons[counter].setBackground(new Background(new BackgroundFill(teacher.getColor() , null, null)));
            pane.getChildren().add(buttons[counter]);


            HelpPrint(counter, students.get(i));
            counter++;
        }

    }

    public void HelpPrint(int counter, Student student){

        Font font = Font.font("System", FontWeight.NORMAL, FontPosture.REGULAR, 20);

        Label label1 = new Label(student.getFirstname());
        label1.prefHeight(142);
        label1.prefWidth(57);
        label1.setLayoutX(13);
        label1.setLayoutY(12 + 80 * counter);
        label1.setFont(font);
        pane.getChildren().add(label1);

        Label label2 = new Label(student.getLastname());
        label2.prefHeight(142);
        label2.prefWidth(57);
        label2.setLayoutX(181);
        label2.setLayoutY(12 + 80 * counter);
        label2.setFont(font);
        pane.getChildren().add(label2);

        Label label3 = new Label(student.getStudentNumber() + "");
        label3.prefHeight(142);
        label3.prefWidth(57);
        label3.setLayoutX(380);
        label3.setLayoutY(12 + 80 * counter);
        label3.setFont(font);
        pane.getChildren().add(label3);

        Label label4 = new Label(student.getCollege() + "");
        label4.prefHeight(142);
        label4.prefWidth(57);
        label4.setLayoutX(634);
        label4.setLayoutY(12 + 80 * counter);
        label4.setFont(font);
        pane.getChildren().add(label4);


        Label labelForMinor = new Label();
        String minor = "------------";
        if(student.getMinorCollege() == null){

        }
        else if(student.getMinorCollege().equals("null")){

        }
        else{
            minor = student.getMinorCollege() + "";
        }

        labelForMinor.setText(minor);
        labelForMinor.minHeight(187);
        labelForMinor.minWidth(31);
        labelForMinor.setLayoutX(857);
        labelForMinor.setLayoutY(12 + 80 * counter);
        labelForMinor.setFont(font);
        pane.getChildren().add(labelForMinor);


        String average = student.getAverage_score() + "";
        if(average.length() <= 5){

        }
        else{
            average = average.substring(0, 6);
        }
        Label label6 = new Label(average);
        label6.prefHeight(142);
        label6.prefWidth(57);
        label6.setLayoutX(1050);
        label6.setLayoutY(12 + 80 * counter);
        label6.setFont(font);
        pane.getChildren().add(label6);
    }

    public void ShowLabel(Student student){
        cabShowButtons = true;
        back.getChildren().clear();
        back.getChildren().add(labelForMassage);

        String s = "it is certified that Mr. / Mrs \n" + student.getFirstname() + " " + student.getLastname() + "\n , with studnet number " + student.getStudentNumber() + ", \n has succufuly pass the following courses:";
        s = s + '\n';
        s = s + clientNetwork.getPassesCourseFromAStudent(student.getId()) + '\n' + "gozarande ";

        s = s + " with average score: " + student.getAverage_score();

        labelForMassage.setText(s);
        Font font = Font.font("System", FontWeight.NORMAL, FontPosture.REGULAR, 20);
        labelForMassage.setFont(font);
        labelForMassage.setAlignment(Pos.CENTER);


        buttonAccept.setOnAction(event -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setHeaderText("Are you sure?");
            if(alert.showAndWait().get() == ButtonType.OK){
                int index = clientNetwork.getIndexRequest(student.getId(), teacher.getId());
                student.getRequestForRecommendation().set(index, Request.Accept);

                clientNetwork.submitRecomeForAStudent(student.getId(), index, "Accept");

                log.info("accepted " + student.getStudentNumber());
                back.getChildren().clear();
                labelForMassage.setText("");
                cabShowButtons = false;
                LinkedList<Student> students = clientNetwork.FindStudentsWantsToRecome(teacher.getId());
                PrintForPane(students);
            }
        });

        buttonAccept.setOnMouseEntered(event -> {
            Scene scene;
            scene = ((Scene)((Node)(event.getSource())).getScene());
            scene.setCursor(Cursor.HAND);
        });

        buttonAccept.setOnMouseExited(event -> {
            Scene scene;
            scene = ((Scene)((Node)(event.getSource())).getScene());
            scene.setCursor(Cursor.DEFAULT);
        });

        buttonAccept.setLayoutX(470);
        buttonAccept.setLayoutY(309);
        buttonAccept.setBackground(new Background(new BackgroundFill(teacher.getColor() , null, null)));
        back.getChildren().add(buttonAccept);

        buttonReject.setOnAction(event -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setHeaderText("Are you sure?");
            if(alert.showAndWait().get() == ButtonType.OK){
                int index = clientNetwork.getIndexRequest(student.getId(), teacher.getId());
                student.getRequestForRecommendation().set(index, Request.Reject);

                clientNetwork.submitRecomeForAStudent(student.getId(), index, "Reject");

                log.info("rejected " + student.getStudentNumber());
                cabShowButtons = false;
                back.getChildren().clear();
                labelForMassage.setText("");
                LinkedList<Student> students = clientNetwork.FindStudentsWantsToRecome(teacher.getId());
                PrintForPane(students);
            }
        });

        buttonReject.setOnMouseEntered(event -> {
            Scene scene;
            scene = ((Scene)((Node)(event.getSource())).getScene());
            scene.setCursor(Cursor.HAND);
        });

        buttonReject.setOnMouseExited(event -> {
            Scene scene;
            scene = ((Scene)((Node)(event.getSource())).getScene());
            scene.setCursor(Cursor.DEFAULT);
        });

        buttonReject.setLayoutX(366);
        buttonReject.setLayoutY(309);
        buttonReject.setBackground(new Background(new BackgroundFill(teacher.getColor() , null, null)));

        back.getChildren().add(buttonReject);

    }


    public void HomeButtonFunction(ActionEvent e) {
        

        timeline.stop();
        try{
            stage = ((Stage)(((Scene)timeShowLabel.getScene()).getWindow()));
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Fxml\\Teacher\\Boss\\BossFirstPage.fxml"));
            Parent root = loader.load();
            BossFirstPage BossFirstPage = loader.getController();
            BossFirstPage.setTeacher(teacher, clientNetwork);
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
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Fxml\\Teacher\\Boss\\Boss_ShowCourseListMenu.fxml"));
            Parent root = loader.load();
            Boss_ShowCourseListMenu Boss_ShowCourseListMenu = loader.getController();
            Boss_ShowCourseListMenu.setTeacher(teacher, clientNetwork);
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
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Fxml\\Teacher\\Boss\\Boss_ShowTeacherListMenuBoss.fxml"));
            Parent root = loader.load();
            Boss_ShowTeacherListMenuBoss Boss_ShowTeacherListMenuBoss = loader.getController();
            Boss_ShowTeacherListMenuBoss.setTeacher(teacher, clientNetwork);
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
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Fxml\\Teacher\\Boss\\Boss_ShowWeeklySchedule.fxml"));
            Parent root = loader.load();
            Boss_ShowWeeklySchedule Boss_ShowWeeklySchedule = loader.getController();
            Boss_ShowWeeklySchedule.setTeacher(teacher, clientNetwork);
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
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Fxml\\Teacher\\Boss\\Boss_ShowExams.fxml"));
            Parent root = loader.load();
            Boss_ShowExams Boss_ShowExams = loader.getController();
            Boss_ShowExams.setTeacher(teacher, clientNetwork);
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
        catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    public void RecomeFunction(ActionEvent event) {
        

        timeline.stop();
        try{
            stage = ((Stage)(((Scene)timeShowLabel.getScene()).getWindow()));
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Fxml\\Teacher\\Boss\\Boss_GiveRecome.fxml"));
            Parent root = loader.load();
            Boss_GiveRecome Boss_GiveRecome = loader.getController();
            Boss_GiveRecome.setTeacher(teacher, clientNetwork);
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
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Fxml\\Teacher\\Boss\\Boss_temporaryScores.fxml"));
            Parent root = loader.load();
            Boss_temporaryScores Boss_temporaryScores = loader.getController();
            Boss_temporaryScores.setTeacher(teacher, clientNetwork);
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
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Fxml\\Teacher\\Boss\\Boss_ShowTeacherProfile.fxml"));
            Parent root = loader.load();
            Boss_ShowTeacherProfile Boss_ShowTeacherProfile = loader.getController();
            Boss_ShowTeacherProfile.setTeacher(teacher, clientNetwork);
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
        catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }



}
