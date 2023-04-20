package controller.TeacherController.Boss;


import back.enums.StatusTeacher;
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

public class Boss_ShowTeacherListMenuBoss implements Initializable{

    public static final Logger log = LogManager.getLogger(Boss_ShowTeacherListMenuBoss.class);


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

    LinkedList<TextField> textFields = new LinkedList<>();
    LinkedList<Integer> IDForTextFields = new LinkedList<>();

    Timeline timelineForConnected;

    @FXML
    Button reconnectionButton;

    @FXML
    ImageView connectionStatus;

    int numClicked = 0;

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


        log.info("change background");

        PrintForPane(clientNetwork.getAllTeacherFormACollege(teacher.getId(),teacher.getCollege() + ""));

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
        PrintForPane(clientNetwork.getAllTeacherFormACollege(teacher.getId(),teacher.getCollege() + ""));

        timelineForConnected = PublicMethods.reconnectionTimeline(reconnectionButton, clientNetwork, connectionStatus);

    }

    public void PrintForPane(LinkedList<Teacher> teachers){

        textFields.clear();
        IDForTextFields.clear();

        Button[] buttons = new Button[teachers.size()];

        pane.getChildren().clear();
        pane.setPrefHeight(teachers.size() * 80 + 30);
        int counter = 0;
        int help = 0;
        for (int i = 0; i < teachers.size(); i++) {
            if(teachers.get(i).getCollege() == teacher.getCollege()){
                if(teachers.get(i).getStatusTeacher() == StatusTeacher.Boss){
                    continue;
                }
                if(teachers.get(i).getStatusTeacher() == StatusTeacher.EA){
                    help = 1;
                    break;
                }
            }
        }

        for (int i = 0; i < teachers.size(); i++) {
            if(teachers.get(i).getCollege() == teacher.getCollege()){
                if(teachers.get(i).getStatusTeacher() == StatusTeacher.Boss){
                    continue;
                }
                if(teachers.get(i).getStatusTeacher() == StatusTeacher.EA){
                    HelpPrintEducationalAssistant(counter, teachers.get(i));

                    if(numClicked == 1){
                        if(help == 1){
                            Button b = new Button("Down");
                            final int m = i;
                            b.setOnAction(event -> {

                                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                                alert.setHeaderText("Are you sure?");
                                if(alert.showAndWait().get() == ButtonType.OK){
                                    teachers.get(m).setStatusTeacher(StatusTeacher.Simple);

                                    clientNetwork.changeStatusTeacher(m, "Simple");


                                    PrintForPane(clientNetwork.getAllTeacherFormACollege(teacher.getId(),teacher.getCollege() + ""));


                                    log.info("down teacher");

                                }
                            });

                            b.setOnMouseEntered(event -> {
                                Scene scene;
                                scene = ((Scene)((Node)(event.getSource())).getScene());
                                scene.setCursor(Cursor.HAND);
                            });

                            b.setOnMouseExited(event -> {
                                Scene scene;
                                scene = ((Scene)((Node)(event.getSource())).getScene());
                                scene.setCursor(Cursor.DEFAULT);
                            });

                            b.setLayoutX(1146);
                            b.setLayoutY(12 + 80 * counter);
                            b.setBackground(new Background(new BackgroundFill(teacher.getColor() , null, null)));
                            pane.getChildren().add(b);
                        }
                    }

                }
                else{
                    HelpPrintTeacher(counter, teachers.get(i));
                }

                if(numClicked == 1){
                    buttons[counter] = new Button("Remove");
                    final int m = i;
                    buttons[counter].setOnAction(event -> {

                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                        alert.setHeaderText("Are you sure?");
                        if(alert.showAndWait().get() == ButtonType.OK){

                            teachers.remove(m);
                            clientNetwork.removeATeacher(m);

                            log.info("remove teacher");

                            PrintForPane(clientNetwork.getAllTeacherFormACollege(teacher.getId(),teacher.getCollege() + ""));


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

                    buttons[counter].setLayoutX(1239);
                    buttons[counter].setLayoutY(12 + 80 * counter);
                    buttons[counter].setBackground(new Background(new BackgroundFill(teacher.getColor() , null, null)));


                    pane.getChildren().add(buttons[counter]);

                }

                if(numClicked == 1){
                    if(help == 0){
                        Button b = new Button("Up");
                        final int m = i;
                        b.setOnAction(event -> {
                            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                            alert.setHeaderText("Are you sure?");
                            if(alert.showAndWait().get() == ButtonType.OK){
                                teachers.get(m).setStatusTeacher(StatusTeacher.EA);
                                clientNetwork.changeStatusTeacher(m, "EA");
                                log.info("up teacher");

                                PrintForPane(clientNetwork.getAllTeacherFormACollege(teacher.getId(),teacher.getCollege() + ""));

                            }
                        });

                        b.setOnMouseEntered(event -> {
                            Scene scene;
                            scene = ((Scene)((Node)(event.getSource())).getScene());
                            scene.setCursor(Cursor.HAND);
                        });

                        b.setOnMouseExited(event -> {
                            Scene scene;
                            scene = ((Scene)((Node)(event.getSource())).getScene());
                            scene.setCursor(Cursor.DEFAULT);
                        });

                        b.setLayoutX(1146);
                        b.setLayoutY( 12 + 80 * counter);
                        b.setBackground(new Background(new BackgroundFill(teacher.getColor() , null, null)));
                        pane.getChildren().add(b);

                    }
                }

                counter++;

            }
        }

    }

    public void HelpPrintTeacher(int counter, Teacher t){

        Font font = Font.font("System", FontWeight.NORMAL, FontPosture.REGULAR, 20);

        Label label1 = new Label(t.getFirstname());
        label1.prefHeight(142);
        label1.prefWidth(57);
        label1.setLayoutX(13);
        label1.setLayoutY(12 + 80 * counter);
        label1.setFont(font);
        pane.getChildren().add(label1);

        Label label2 = new Label(t.getLastname());
        label2.prefHeight(142);
        label2.prefWidth(57);
        label2.setLayoutX(220);
        label2.setLayoutY(12 + 80 * counter);
        label2.setFont(font);
        pane.getChildren().add(label2);

        Label label3 = new Label(t.getTeacherDegree() + "");
        label3.prefHeight(142);
        label3.prefWidth(57);
        label3.setLayoutX(450);
        label3.setLayoutY(12 + 80 * counter);
        label3.setFont(font);
        pane.getChildren().add(label3);

        Label label4 = new Label(t.getPhoneNumber() + "");
        label4.prefHeight(142);
        label4.prefWidth(57);
        label4.setLayoutX(676);
        label4.setLayoutY(12 + 80 * counter);
        label4.setFont(font);
        pane.getChildren().add(label4);

        if(numClicked == 0){
            TextField textFieldForRoomNumber = new TextField();
            textFieldForRoomNumber.setText(t.getRoomNumber() + "");
            textFieldForRoomNumber.minHeight(187);
            textFieldForRoomNumber.minWidth(31);
            textFieldForRoomNumber.setLayoutX(833);
            textFieldForRoomNumber.setLayoutY(12 + 80 * counter);
            textFieldForRoomNumber.setDisable(true);
            pane.getChildren().add(textFieldForRoomNumber);
            textFields.add(textFieldForRoomNumber);
            IDForTextFields.add(t.getId());
        }
        else{
            TextField textFieldForRoomNumber = new TextField();
            textFieldForRoomNumber.setText(t.getRoomNumber() + "");
            textFieldForRoomNumber.minHeight(187);
            textFieldForRoomNumber.minWidth(31);
            textFieldForRoomNumber.setLayoutX(833);
            textFieldForRoomNumber.setLayoutY(12 + 80 * counter);
            textFieldForRoomNumber.setDisable(false);
            pane.getChildren().add(textFieldForRoomNumber);
            textFields.add(textFieldForRoomNumber);
            IDForTextFields.add(t.getId());

        }

        Label label6 = new Label(t.getId() + "");
        label6.prefHeight(142);
        label6.prefWidth(57);
        label6.setLayoutX(1083);
        label6.setLayoutY(12 + 80 * counter);
        label6.setFont(font);
        pane.getChildren().add(label6);
    }

    public void HelpPrintEducationalAssistant(int counter, Teacher t){

        Font font = Font.font("System", FontWeight.NORMAL, FontPosture.REGULAR, 20);

        Label label1 = new Label(t.getFirstname());
        label1.prefHeight(142);
        label1.prefWidth(57);
        label1.setLayoutX(13);
        label1.setLayoutY(12 + 80 * counter);
        label1.setFont(font);
        pane.getChildren().add(label1);

        Label label2 = new Label(t.getLastname());
        label2.prefHeight(142);
        label2.prefWidth(57);
        label2.setLayoutX(220);
        label2.setLayoutY(12 + 80 * counter);
        label2.setFont(font);
        pane.getChildren().add(label2);

        Label label3 = new Label(t.getTeacherDegree() + "");
        label3.prefHeight(142);
        label3.prefWidth(57);
        label3.setLayoutX(450);
        label3.setLayoutY(12 + 80 * counter);
        label3.setFont(font);
        pane.getChildren().add(label3);

        Label label4 = new Label(t.getPhoneNumber() + "");
        label4.prefHeight(142);
        label4.prefWidth(57);
        label4.setLayoutX(676);
        label4.setLayoutY(12 + 80 * counter);
        label4.setFont(font);
        pane.getChildren().add(label4);

        if(numClicked == 0){
            TextField textFieldForRoomNumber = new TextField();
            textFieldForRoomNumber.setText(t.getRoomNumber() + "");
            textFieldForRoomNumber.minHeight(187);
            textFieldForRoomNumber.minWidth(31);
            textFieldForRoomNumber.setLayoutX(833);
            textFieldForRoomNumber.setLayoutY(12 + 80 * counter);
            textFieldForRoomNumber.setDisable(true);
            pane.getChildren().add(textFieldForRoomNumber);
            textFields.add(textFieldForRoomNumber);
            IDForTextFields.add(t.getId());




        }
        else{
            TextField textFieldForRoomNumber = new TextField();
            textFieldForRoomNumber.setText(t.getRoomNumber() + "");
            textFieldForRoomNumber.minHeight(187);
            textFieldForRoomNumber.minWidth(31);
            textFieldForRoomNumber.setLayoutX(833);
            textFieldForRoomNumber.setLayoutY(12 + 80 * counter);
            textFieldForRoomNumber.setDisable(false);
            pane.getChildren().add(textFieldForRoomNumber);
            textFields.add(textFieldForRoomNumber);
            IDForTextFields.add(t.getId());
        }


        Label label6 = new Label(t.getId() + "");
        label6.prefHeight(142);
        label6.prefWidth(57);
        label6.setLayoutX(1083);
        label6.setLayoutY(12 + 80 * counter);
        label6.setFont(font);
        pane.getChildren().add(label6);


        Color color = teacher.getColor();
        label1.setTextFill(color);
        label2.setTextFill(color);
        label3.setTextFill(color);
        label4.setTextFill(color);
        label6.setTextFill(color);
    }

    public void AddFunction(ActionEvent e) {
        try{
            stage = ((Stage)(((Scene)AddButton.getScene()).getWindow()));
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Fxml\\Teacher\\Boss\\Boss_ShowTeacherList_AddNewTeacher.fxml"));
            Parent root = loader.load();
            Boss_ShowTeacherList_AddNewTeacher Boss_ShowTeacherList_AddNewTeacher = loader.getController();
            Boss_ShowTeacherList_AddNewTeacher.setTeacher(teacher, clientNetwork);
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
            LinkedList<Teacher> teachers = clientNetwork.getAllTeachers();
            for (int i = 0; i < textFields.size(); i++) {
                for (int j = 0; j < textFields.get(i).getText().length(); j++) {
                    if (!(textFields.get(i).getText().charAt(i) >= '0' && textFields.get(i).getText().charAt(i) <= '9')) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Incorrect");
                        alert.setHeaderText("Id field should be number");
                        alert.showAndWait();
                        return;
                    }
                }
                for (Teacher teacher : teachers) {
                    if(teacher.getCollege() != this.teacher.getCollege()){
                        continue;
                    }
                    if(teacher.getId() == IDForTextFields.get(i)){

                    }
                    else if(teacher.getRoomNumber() == Integer.parseInt(textFields.get(i).getText())){
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Incorrect");
                        alert.setHeaderText("This room is full !!!!!");
                        alert.showAndWait();
                        return;
                    }
                }

                Teacher t = clientNetwork.FindTeacher(IDForTextFields.get(i));
                t.setRoomNumber(Integer.parseInt(textFields.get(i).getText()));
                clientNetwork.setRoomNumberForATeacher(IDForTextFields.get(i), Integer.parseInt(textFields.get(i).getText()));
                

            }
            textFields.clear();
            IDForTextFields.clear();
            EditButton.setText("Edit");
        }
        numClicked++;
        numClicked = numClicked % 2;

        PrintForPane(clientNetwork.getAllTeacherFormACollege(teacher.getId(),teacher.getCollege() + ""));

    }


    public void HomeButtonFunction(ActionEvent e) {
        timelineForConnected.stop();
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
        timelineForConnected.stop();
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
        timelineForConnected.stop();
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
        timelineForConnected.stop();
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
        timelineForConnected.stop();
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
        timelineForConnected.stop();
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
        timelineForConnected.stop();
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
