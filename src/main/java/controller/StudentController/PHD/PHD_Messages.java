package controller.StudentController.PHD;


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
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.Duration;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.ResourceBundle;

public class PHD_Messages implements Initializable {
    public static final Logger log = LogManager.getLogger(PHD_StudentEducationalStatus.class);

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
    ListView<String> listView;

    Stage stage;

    Timeline timeline;

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

        LinkedList<String> names = clientNetwork.getMessagesForAStudent(student.getId());
        updateListCell(names);

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


        reconnectionButton.setVisible(false);
        timeline = new Timeline(new KeyFrame(Duration.seconds(5), new EventHandler<ActionEvent>(){
            int timesForFirst = 0;
            int timesForSecond = 0;
            int size = 0;

            @Override
            public void handle(ActionEvent event) {
                LinkedList<String> names = clientNetwork.getMessagesForAStudent(student.getId());
                if(size != names.size()){
                    size = names.size();
                    updateListCell(names);
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

        LinkedList<String> names = clientNetwork.getMessagesForAStudent(student.getId());
        updateListCell(names);
    }

    public void updateListCell(LinkedList<String> names){
        String co = "#" + Integer.toHexString(student.getColor().hashCode());

        if(names.size() != 0){
            listView.setStyle("" +
                    "-fx-background-color: " + co + ";" +
                    "-fx-text-fill: black;");

        }
        else{
            listView.setStyle("" +
                    "-fx-background-color: white ;" +
                    "-fx-text-fill: black;");

        }

        String[] s = new String[names.size()];
        names.toArray(s);

        ObservableList<String> items = FXCollections.observableArrayList(s);
        listView.setItems(items);


        listView.setCellFactory(new Callback<ListView<String>, ListCell<String>>() {
            @Override
            public ListCell<String> call(ListView<String> param) {
                Button buttonOK;
                buttonOK = new Button("ok");
                buttonOK.setBackground(new Background(new BackgroundFill(student.getColor(), null, null)));
                buttonOK.setCursor(Cursor.HAND);

                Button buttonAccept;
                buttonAccept = new Button("accept");
                buttonAccept.setBackground(new Background(new BackgroundFill(student.getColor(), null, null)));
                buttonAccept.setCursor(Cursor.HAND);

                Button buttonReject;
                buttonReject = new Button("reject");
                buttonReject.setBackground(new Background(new BackgroundFill(student.getColor(), null, null)));
                buttonReject.setCursor(Cursor.HAND);


                return new XCell(buttonOK, buttonAccept, buttonReject);
            }
        });

    }


    public void HomeButtonFunction(ActionEvent e) {
        timeline.stop();
        try{
            stage = ((Stage)(((Scene)timeShowLabel.getScene()).getWindow()));
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Fxml\\Student\\PHD\\PHDFirstPage.fxml"));
            Parent root = loader.load();
            PHDFirstPage PHDFirstPage = loader.getController();
            PHDFirstPage.setStudent(student, clientNetwork);
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
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Fxml\\Student\\PHD\\PHD_ShowCourseListMenu.fxml"));
            Parent root = loader.load();
            PHD_ShowCourseListMenu PHD_ShowCourseListMenu = loader.getController();
            PHD_ShowCourseListMenu.setStudent(student, clientNetwork);
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
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Fxml\\Student\\PHD\\PHD_ShowTeacherListMenu.fxml"));
            Parent root = loader.load();
            PHD_ShowTeacherListMenu PHD_ShowTeacherListMenu = loader.getController();
            PHD_ShowTeacherListMenu.setStudent(student, clientNetwork);
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
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Fxml\\Student\\PHD\\PHD_ShowWeeklySchedule.fxml"));
            Parent root = loader.load();
            PHD_ShowWeeklySchedule PHD_ShowWeeklySchedule = loader.getController();
            PHD_ShowWeeklySchedule.setStudent(student, clientNetwork);
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
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Fxml\\Student\\PHD\\PHD_ShowProfile.fxml"));
            Parent root = loader.load();
            PHD_ShowProfile PHD_ShowProfile = loader.getController();
            PHD_ShowProfile.setStudent(student, clientNetwork);
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
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Fxml\\Student\\PHD\\PHD_ShowExams.fxml"));
            Parent root = loader.load();
            PHD_ShowExams PHD_ShowExams = loader.getController();
            PHD_ShowExams.setStudent(student, clientNetwork);
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
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Fxml\\Student\\PHD\\PHD_CertificateStudent.fxml"));
            Parent root = loader.load();
            PHD_CertificateStudent PHD_CertificateStudent = loader.getController();
            PHD_CertificateStudent.setStudent(student, clientNetwork);
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
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Fxml\\Student\\PHD\\PHD_withdrawalFromEducation.fxml"));
            Parent root = loader.load();
            PHD_withdrawalFromEducation PHD_withdrawalFromEducation = loader.getController();
            PHD_withdrawalFromEducation.setStudent(student, clientNetwork);
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
        catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    public void ThesisDefenceFunction(ActionEvent event) {
        timeline.stop();
        try{
            stage = ((Stage)(((Scene)timeShowLabel.getScene()).getWindow()));
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Fxml\\Student\\PHD\\PHD_ThesisDefense.fxml"));
            Parent root = loader.load();
            PHD_ThesisDefense PHD_ThesisDefense = loader.getController();
            PHD_ThesisDefense.setStudent(student, clientNetwork);
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
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Fxml\\Student\\PHD\\PHD_temporaryScores.fxml"));
            Parent root = loader.load();
            PHD_temporaryScores PHD_temporaryScores = loader.getController();
            PHD_temporaryScores.setStudent(student, clientNetwork);
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
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Fxml\\Student\\PHD\\PHD_StudentEducationalStatus.fxml"));
            Parent root = loader.load();
            PHD_StudentEducationalStatus PHD_StudentEducationalStatus = loader.getController();
            PHD_StudentEducationalStatus.setStudent(student, clientNetwork);
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
        catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }


    class XCell extends ListCell<String> {
        HBox hbox = new HBox();
        Label label = new Label("(empty)");
        Pane pane = new Pane();
        Button buttonOk;
        Button buttonAccept;
        Button buttonReject;


        public XCell(Button buttonOk, Button buttonAccept, Button buttonReject) {
            super();
            hbox.getChildren().addAll(label, pane, buttonOk, buttonAccept, buttonReject);
            Font font = Font.font("Comic Sans MS", FontWeight.BOLD, FontPosture.ITALIC, 20);
            label.setFont(font);
            HBox.setHgrow(pane, Priority.ALWAYS);
            buttonOk.setOnAction(event -> {
                clientNetwork.removeAMessageFromAStudent(student.getId(), label.getText(), false, false);
                LinkedList<String> names = clientNetwork.getMessagesForAStudent(student.getId());
                updateListCell(names);
            });

            buttonAccept.setOnAction(event -> {
                clientNetwork.removeAMessageFromAStudent(student.getId(), label.getText(), true, true);
                LinkedList<String> names = clientNetwork.getMessagesForAStudent(student.getId());
                updateListCell(names);
            });

            buttonReject.setOnAction(event -> {
                clientNetwork.removeAMessageFromAStudent(student.getId(), label.getText(), true, false);
                LinkedList<String> names = clientNetwork.getMessagesForAStudent(student.getId());
                updateListCell(names);
            });

            this.buttonAccept = buttonAccept;
            this.buttonReject = buttonReject;
            this.buttonOk = buttonOk;
        }

        @Override
        protected void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);
            setText(null);  // No text in label of super class
            if (empty) {
                setGraphic(new ImageView());
            } else {
                label.setText(item!=null ? item : "<null>");
                System.out.println(hbox.getChildren().size());
                if(item.substring(0, 30).toLowerCase().equals("student with student number : ")){
                    hbox.getChildren().remove(buttonOk);
                }
                else{
                    hbox.getChildren().remove(buttonAccept);
                    hbox.getChildren().remove(buttonReject);
                }

                setGraphic(hbox);
            }
        }
    }

}


