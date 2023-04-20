package controller.StudentController.MS;


import back.course.Course;
import back.enums.Days;
import back.persons.Student;
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
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Line;
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


public class MS_ShowWeeklySchedule implements Initializable{
    public static final Logger log = LogManager.getLogger(MS_ShowWeeklySchedule.class);

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
    Pane pane;

    Timeline timelineForConnected;

    @FXML
    Button reconnectionButton;

    @FXML
    ImageView connectionStatus;


    Stage stage;
    @FXML
    void ChangingColor(ActionEvent event) {
        Color color = colorPicker.getValue();
        MenuBar.setBackground(new Background(new BackgroundFill(color, null, null)));
        timeShowLabel.setBackground(new Background(new BackgroundFill(color, null, null)));
        HomeButton.setBackground(new Background(new BackgroundFill(color, null, null)));


        student.color = colorPicker.getValue();
        String s = "#" + Integer.toHexString(color.hashCode());
        clientNetwork.changeColorForStudent(student.getStudentNumber(), s);


        LinkedList<Course> c = clientNetwork.GiveCoursesFromAStudent(student.getId());
        log.info("change background");

        PrintForPane(c);

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

        LinkedList<Course> c = clientNetwork.GiveCoursesFromAStudent(student.getId());
        PrintForPane(c);


        timelineForConnected = PublicMethods.reconnectionTimeline(reconnectionButton, clientNetwork, connectionStatus);
    }

    public void PrintForPane(LinkedList<Course> courses){

        Font font = Font.font("System", FontWeight.NORMAL, FontPosture.REGULAR, 17);
        Color color = student.color;

        pane.getChildren().clear();

        if(true){
            Line line1 = new Line(-100, -67, -100, 609);
            line1.setLayoutX(265);
            line1.setLayoutY(67);

            Line line2 = new Line(-100, -67, -100, 609);
            line2.setLayoutX(430);
            line2.setLayoutY(67);

            Line line3 = new Line(-100, -67, -100, 609);
            line3.setLayoutX(595);
            line3.setLayoutY(67);

            Line line4 = new Line(-100, -67, -100, 609);
            line4.setLayoutX(760);
            line4.setLayoutY(67);

            Line line5 = new Line(-100, -67, -100, 609);
            line5.setLayoutX(925);
            line5.setLayoutY(67);

            Line line6 = new Line(-100, -67, -100, 609);
            line6.setLayoutX(1090);
            line6.setLayoutY(67);


            Paint paint = Color.web("#b5a6a6");
            line1.setStroke(paint);
            line2.setStroke(paint);
            line3.setStroke(paint);
            line4.setStroke(paint);
            line5.setStroke(paint);
            line6.setStroke(paint);


            Line line7 = new Line(-100, 0, 1055, 0);
            line7.setLayoutX(100);
            line7.setLayoutY(52);

            Line line8 = new Line(-100, 0, 1055, 0);
            line8.setLayoutX(100);
            line8.setLayoutY(104);

            Line line9 = new Line(-100, 0, 1055, 0);
            line9.setLayoutX(100);
            line9.setLayoutY(156);

            Line line10 = new Line(-100, 0, 1055, 0);
            line10.setLayoutX(100);
            line10.setLayoutY(208);

            Line line11 = new Line(-100, 0, 1055, 0);
            line11.setLayoutX(100);
            line11.setLayoutY(260);

            Line line12 = new Line(-100, 0, 1055, 0);
            line12.setLayoutX(100);
            line12.setLayoutY(312);

            Line line13 = new Line(-100, 0, 1055, 0);
            line13.setLayoutX(100);
            line13.setLayoutY(364);

            Line line14 = new Line(-100, 0, 1055, 0);
            line14.setLayoutX(100);
            line14.setLayoutY(416);

            Line line15 = new Line(-100, 0, 1055, 0);
            line15.setLayoutX(100);
            line15.setLayoutY(468);

            Line line16 = new Line(-100, 0, 1055, 0);
            line16.setLayoutX(100);
            line16.setLayoutY(520);

            Line line17 = new Line(-100, 0, 1055, 0);
            line17.setLayoutX(100);
            line17.setLayoutY(572);

            Line line18 = new Line(-100, 0, 1055, 0);
            line18.setLayoutX(100);
            line18.setLayoutY(624);

            Line line19 = new Line(-100, 0, 1055, 0);
            line19.setLayoutX(100);
            line19.setLayoutY(676);

            Font font1 = Font.font("System", FontWeight.NORMAL, FontPosture.REGULAR , 20);

            Label label1 = new Label("Friday");
            label1.setLayoutY(-2);
            label1.setLayoutX(0);
            label1.setAlignment(Pos.CENTER);
            label1.setMinWidth(166);
            label1.setMinHeight(54);
            label1.setFont(font1);

            Label label2 = new Label("Thursday");
            label2.setLayoutY(-2);
            label2.setLayoutX(165);
            label2.setAlignment(Pos.CENTER);
            label2.setMinWidth(165);
            label2.setMinHeight(54);
            label2.setFont(font1);

            Label label3 = new Label("Wednesday");
            label3.setLayoutY(-2);
            label3.setLayoutX(330);
            label3.setAlignment(Pos.CENTER);
            label3.setMinWidth(165);
            label3.setMinHeight(54);
            label3.setFont(font1);

            Label label4 = new Label("Tuesday");
            label4.setLayoutY(-2);
            label4.setLayoutX(495);
            label4.setAlignment(Pos.CENTER);
            label4.setMinWidth(165);
            label4.setMinHeight(54);
            label4.setFont(font1);

            Label label5 = new Label("Monday");
            label5.setLayoutY(-2);
            label5.setLayoutX(660);
            label5.setAlignment(Pos.CENTER);
            label5.setMinWidth(165);
            label5.setMinHeight(54);
            label5.setFont(font1);

            Label label6 = new Label("Sunday");
            label6.setLayoutY(-2);
            label6.setLayoutX(825);
            label6.setAlignment(Pos.CENTER);
            label6.setMinWidth(165);
            label6.setMinHeight(54);
            label6.setFont(font1);

            Label label7 = new Label("Saturday");
            label7.setLayoutY(-2);
            label7.setLayoutX(990);
            label7.setAlignment(Pos.CENTER);
            label7.setMinWidth(165);
            label7.setMinHeight(54);
            label7.setFont(font1);

            pane.getChildren().add(line1);
            pane.getChildren().add(line2);
            pane.getChildren().add(line3);
            pane.getChildren().add(line4);
            pane.getChildren().add(line5);
            pane.getChildren().add(line6);
            pane.getChildren().add(line7);
            pane.getChildren().add(line8);
            pane.getChildren().add(line9);
            pane.getChildren().add(line10);
            pane.getChildren().add(line11);
            pane.getChildren().add(line12);
            pane.getChildren().add(line13);
            pane.getChildren().add(line14);
            pane.getChildren().add(line15);
            pane.getChildren().add(line16);
            pane.getChildren().add(line17);
            pane.getChildren().add(line18);
            pane.getChildren().add(line19);

            pane.getChildren().add(label1);
            pane.getChildren().add(label2);
            pane.getChildren().add(label3);
            pane.getChildren().add(label4);
            pane.getChildren().add(label5);
            pane.getChildren().add(label6);
            pane.getChildren().add(label7);

        }

        for (int i = 0; i < courses.size(); i++) {

            for (int j = 0; j < courses.get(i).getDays().size(); j++) {
                Label label = new Label(courses.get(i).getName() + '\n' + courses.get(i).getId());
                if(courses.get(i).getDays().get(j) == Days.Saturday){
                    label.setLayoutX(991);
                }
                else if(courses.get(i).getDays().get(j) == Days.Sunday){
                    label.setLayoutX(825);
                }
                else if(courses.get(i).getDays().get(j) == Days.Monday){
                    label.setLayoutX(661);
                }
                else if(courses.get(i).getDays().get(j) == Days.Tuesday){
                    label.setLayoutX(495);
                }
                else if(courses.get(i).getDays().get(j) == Days.Wednesday){
                    label.setLayoutX(330);
                }
                else if(courses.get(i).getDays().get(j) == Days.Thursday){
                    label.setLayoutX(165);
                }
                else if(courses.get(i).getDays().get(j) == Days.Friday){
                    label.setLayoutX(0);
                }

                label.setLayoutY(52 * (courses.get(i).getHourEndClass() - courses.get(i).getHourBeginClass()) + 52 * (courses.get(i).getHourBeginClass() - 9));
                label.setAlignment(Pos.CENTER);
                label.setMinWidth(166);
                label.setMinHeight((courses.get(i).getHourEndClass() - courses.get(i).getHourBeginClass()) * 52.0 );
                label.setFont(font);
                pane.getChildren().add(label);
                Color color1 = new Color(color.getRed(), color.getGreen(), color.getBlue(), 0.7);
                label.setBackground(new Background(new BackgroundFill(color1, null, null)));
            }
        }
    }


    public void HomeButtonFunction(ActionEvent e) {
        timelineForConnected.stop();
        try{
            stage = ((Stage)(((Scene)timeShowLabel.getScene()).getWindow()));
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Fxml\\Student\\MS\\MSFirstPage.fxml"));
            Parent root = loader.load();
            MSFirstPage MSFirstPage = loader.getController();
            MSFirstPage.setStudent(student, clientNetwork);
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
            clientNetwork.changeLastLoginStudent(student.getId(), localDateTime.getSecond(), localDateTime.getMinute(), localDateTime.getHour(), localDateTime.getDayOfMonth(), localDateTime.getMonthValue(), localDateTime.getYear());

        }
        catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    public void CoursesListMenuFunction(ActionEvent event) {
        timelineForConnected.stop();
        try{
            stage = ((Stage)(((Scene)timeShowLabel.getScene()).getWindow()));
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Fxml\\Student\\MS\\MS_ShowCourseListMenu.fxml"));
            Parent root = loader.load();
            MS_ShowCourseListMenu MS_ShowCourseListMenu = loader.getController();
            MS_ShowCourseListMenu.setStudent(student, clientNetwork);
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
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Fxml\\Student\\MS\\MS_ShowTeacherListMenu.fxml"));
            Parent root = loader.load();
            MS_ShowTeacherListMenu MS_ShowTeacherListMenu = loader.getController();
            MS_ShowTeacherListMenu.setStudent(student, clientNetwork);
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
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Fxml\\Student\\MS\\MS_ShowWeeklySchedule.fxml"));
            Parent root = loader.load();
            MS_ShowWeeklySchedule MS_ShowWeeklySchedule = loader.getController();
            MS_ShowWeeklySchedule.setStudent(student, clientNetwork);
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
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Fxml\\Student\\MS\\MS_ShowProfile.fxml"));
            Parent root = loader.load();
            MS_ShowProfile MS_ShowProfile = loader.getController();
            MS_ShowProfile.setStudent(student, clientNetwork);
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
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Fxml\\Student\\MS\\MS_ShowExams.fxml"));
            Parent root = loader.load();
            MS_ShowExams MS_ShowExams = loader.getController();
            MS_ShowExams.setStudent(student, clientNetwork);
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
        catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    public void certificateStudentFunction(ActionEvent event) {
        timelineForConnected.stop();
        try{
            stage = ((Stage)(((Scene)timeShowLabel.getScene()).getWindow()));
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Fxml\\Student\\MS\\MS_CertificateStudent.fxml"));
            Parent root = loader.load();
            MS_CertificateStudent MS_CertificateStudent = loader.getController();
            MS_CertificateStudent.setStudent(student, clientNetwork);
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
        catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    public void DormFunction(ActionEvent event) {
        timelineForConnected.stop();
        try{
            stage = ((Stage)(((Scene)timeShowLabel.getScene()).getWindow()));
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Fxml\\Student\\MS\\MS_Dorm.fxml"));
            Parent root = loader.load();
            MS_Dorm MS_Dorm = loader.getController();
            MS_Dorm.setStudent(student, clientNetwork);
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
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Fxml\\Student\\MS\\MS_withdrawalFromEducation.fxml"));
            Parent root = loader.load();
            MS_withdrawalFromEducation MS_withdrawalFromEducation = loader.getController();
            MS_withdrawalFromEducation.setStudent(student, clientNetwork);
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
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Fxml\\Student\\MS\\MS_RequestForRecommendation.fxml"));
            Parent root = loader.load();
            MS_RequestForRecommendation MS_RequestForRecommendation = loader.getController();
            MS_RequestForRecommendation.setStudent(student, clientNetwork);
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
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Fxml\\Student\\MS\\MS_temporaryScores.fxml"));
            Parent root = loader.load();
            MS_temporaryScores MS_temporaryScores = loader.getController();
            MS_temporaryScores.setStudent(student, clientNetwork);
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
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Fxml\\Student\\MS\\MS_StudentEducationalStatus.fxml"));
            Parent root = loader.load();
            MS_StudentEducationalStatus MS_StudentEducationalStatus = loader.getController();
            MS_StudentEducationalStatus.setStudent(student, clientNetwork);
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
        catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
}
