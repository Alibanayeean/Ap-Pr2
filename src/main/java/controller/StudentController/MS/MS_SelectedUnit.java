package controller.StudentController.MS;


import back.course.Course;
import back.enums.Grade;
import back.persons.Student;
import clientNetwork.ClientNetwork;
import config.ConfigIdentifier;
import config.ReadPropertyFile;
import controller.StudentController.BC.BCFirstPage;
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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
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

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Optional;
import java.util.ResourceBundle;


public class MS_SelectedUnit implements Initializable {
    public static final Logger log = LogManager.getLogger(MS_SelectedUnit.class);


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

    @FXML
    RadioButton fromCollegeRadioButton;

    @FXML
    RadioButton suggestedRadioButton;

    @FXML
    CheckBox neshanShodeChoiceBox;

    @FXML
    CheckBox allCourses;

    Timeline timeline;

    @FXML
    ChoiceBox choiceBoxForCollege;

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

        ShowButtonFunction();

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        PublicMethods.initImageOfLogOutButton(LogOutButton);

        PublicMethods.initImageOfHomeButton(HomeButton);

        PublicMethods.initClock(timeShowLabel);

        String[] choices_CollegeNameField = {"--", "EE", "CE", "CS", "Math", "Physics", "Chemistry", "Mechanic", "Public"};
        choiceBoxForCollege.getItems().addAll(choices_CollegeNameField);

        choiceBoxForCollege.setOnAction(event -> {
            ShowButtonFunction();
        });

        allCourses.setSelected(true);
        fromCollegeRadioButton.setSelected(true);

    }

    public void setStudent(Student student, ClientNetwork clientNetwork){
        this.clientNetwork = clientNetwork;
        this.student = student;

        Color color = student.color;

        MenuBar.setBackground(new Background(new BackgroundFill(color, null, null)));
        timeShowLabel.setBackground(new Background(new BackgroundFill(color, null, null)));
        HomeButton.setBackground(new Background(new BackgroundFill(color, null, null)));
        colorPicker.setValue(color);

        String time = clientNetwork.getTimeSelectUnitForAStudent(student.getId());
        if(time.equals("")){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Inaccessible");
            alert.setHeaderText("Inaccessible");
            alert.show();

            log.error("Inaccessible because of time");

            try{
                stage = ((Stage)(((Scene)HomeButton.getScene()).getWindow()));
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

            return;
        }
        String[] s = time.split("-");
        LocalDateTime localDateTime = LocalDateTime.now();
        if(!(Integer.parseInt(s[0]) == localDateTime.getYear() && Integer.parseInt(s[1]) == localDateTime.getMonthValue() && Integer.parseInt(s[2]) == localDateTime.getDayOfMonth())){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Inaccessible");
            alert.setHeaderText("Inaccessible");
            alert.show();

            log.error("Inaccessible because of time");

            try{
                stage = ((Stage)(((Scene)HomeButton.getScene()).getWindow()));
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

            return;

        }

        reconnectionButton.setVisible(false);
        timeline = new Timeline(new KeyFrame(Duration.seconds(2), new EventHandler<ActionEvent>(){
            int timesForFirst = 0;
            int timesForSecond = 0;

            @Override
            public void handle(ActionEvent event) {
                ShowButtonFunction();
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


        ShowButtonFunction();



    }

    void ShowButtonFunction(){
        String time = clientNetwork.getTimeSelectUnitForAStudent(student.getId());
        System.out.println(time);
        if(time.equals("")){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Inaccessible");
            alert.setHeaderText("Inaccessible");
            alert.show();
            timeline.stop();


            log.error("Inaccessible because of time");

            try{
                stage = ((Stage)(((Scene)HomeButton.getScene()).getWindow()));
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

            return;
        }
        String[] s = time.split("-");
        LocalDateTime localDateTime = LocalDateTime.now();
        if(!(Integer.parseInt(s[0]) == localDateTime.getYear() && Integer.parseInt(s[1]) == localDateTime.getMonthValue() && Integer.parseInt(s[2]) == localDateTime.getDayOfMonth())){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Inaccessible");
            alert.setHeaderText("Inaccessible");
            alert.showAndWait();

            log.error("Inaccessible because of time");

            try{
                stage = ((Stage)(((Scene)HomeButton.getScene()).getWindow()));
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

            return;

        }


        LinkedList<Course> c = new LinkedList<>();
        if(allCourses.isSelected()){
            if(fromCollegeRadioButton.isSelected()){
                String college = getResultOfChoiceBoxForCollege();
                c = clientNetwork.getAllCoursesFromACollegeForSelectUnit(college, student.getGrade() + "");
//                sorting
                for (int i = 0; i < c.size(); i++) {
                    for (int j = i + 1; j < c.size(); j++) {
                        if(c.get(i).getYearExam() > c.get(j).getYearExam()){
                            Collections.swap(c, i, j);

                        }
                        else if(c.get(j).getYearExam() > c.get(i).getYearExam()){

                        }
                        else{
                            if(c.get(i).getMonthExam() > c.get(j).getMonthExam()){
                                Collections.swap(c, i, j);
                            }
                            else if(c.get(j).getMonthExam() > c.get(i).getMonthExam()){

                            }

                            else{
                                if(c.get(i).getDayExam() > c.get(j).getDayExam()){
                                    Collections.swap(c, i, j);

                                }
                                else if(c.get(j).getDayExam() > c.get(i).getDayExam()){

                                }
                                else{
                                    if(c.get(i).getPublicId() > c.get(j).getPublicId()){
                                        Collections.swap(c, i, j);

                                    }
                                    else{
                                        if((c.get(i).getGrade() == Grade.MS | c.get(i).getGrade() == Grade.PHD) & (c.get(j).getGrade() == Grade.BC)){
                                            Collections.swap(c, i, j);

                                        }
                                        else if((c.get(i).getGrade() == Grade.PHD) & (c.get(j).getGrade() == Grade.MS)){
                                            Collections.swap(c, i, j);

                                        }
                                    }
                                }
                            }
                        }
                    }
                }

            }
            else if(suggestedRadioButton.isSelected()){
                c = clientNetwork.getSuggestedCourses(student.getId(), student.getCollege() + "", student.getGrade() + "");
            }
        }
        else if(neshanShodeChoiceBox.isSelected()){
            if(!(fromCollegeRadioButton.isSelected() | suggestedRadioButton.isSelected())){
                c = clientNetwork.getLikedCourses(student.getId());
            }
        }

        PrintForPane(c);


    }

    public String  getResultOfChoiceBoxForCollege (){
        String stringChoiceBoxForCollege = (String) choiceBoxForCollege.getValue();
        if(stringChoiceBoxForCollege == null){
            return "";
        }
        return stringChoiceBoxForCollege ;
    }

    public void PrintForPane(LinkedList<Course> courses){
        pane.getChildren().clear();
        pane.setPrefHeight(courses.size() * 80 + 30);

        Button[] buttonsForNeshanDarKardan = new Button[courses.size()];
        Button[] buttonsForGetOrRemoveCourse = new Button[courses.size()];
        Button[] buttonsForChangeGroup = new Button[courses.size()];
        Button[] buttonsForRequestToEA = new Button[courses.size()];


        for (int i = 0; i < courses.size(); i++) {

            final int m = i;
            LinkedList<Boolean> booleans = clientNetwork.getStatusOfACourseForUnitSelection(student.getId(), courses.get(i).getId());
            //baraye mark kardan dars;
            buttonsForNeshanDarKardan[i] = new Button();
            if(booleans.get(0)){
                File file = new File(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.publicURL) + "nostar.png");
                Image image = new Image(file.toURI().toString());
                ImageView imageView = new ImageView(image);
                imageView.setFitWidth(30);
                imageView.setFitHeight(30);
                buttonsForNeshanDarKardan[i].setGraphic(imageView);
            }
            else{
                File file = new File(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.publicURL) + "star.png");
                Image image = new Image(file.toURI().toString());
                ImageView imageView = new ImageView(image);
                imageView.setFitWidth(30);
                imageView.setFitHeight(30);
                buttonsForNeshanDarKardan[i].setGraphic(imageView);
            }
            buttonsForNeshanDarKardan[i].setOnAction(event -> {
                if(booleans.get(0)){
                    clientNetwork.removeACourseFromLikedCourses(student.getId(), courses.get(m).getId());
                }
                else{
                    clientNetwork.addACourseToLikedCourses(student.getId(), courses.get(m).getId());
                }

                ShowButtonFunction();
            });
            buttonsForNeshanDarKardan[i].setOnMouseEntered(event -> {
                Scene scene;
                scene = ((Scene)((Node)(event.getSource())).getScene());
                scene.setCursor(Cursor.HAND);
            });
            buttonsForNeshanDarKardan[i].setOnMouseExited(event -> {
                Scene scene;
                scene = ((Scene)((Node)(event.getSource())).getScene());
                scene.setCursor(Cursor.DEFAULT);
            });
            buttonsForNeshanDarKardan[i].setLayoutX(1190);
            buttonsForNeshanDarKardan[i].setLayoutY(12 + 80 * i);
            buttonsForNeshanDarKardan[i].setBackground(new Background(new BackgroundFill(student.getColor() , null, null)));
            pane.getChildren().add(buttonsForNeshanDarKardan[i]);


            if(booleans.get(1) != null){
                if(booleans.get(1)){
//                    Dars ra gerefte
                    buttonsForGetOrRemoveCourse[i] = new Button();
                    File file = new File(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.publicURL) + "cross.png");
                    Image image = new Image(file.toURI().toString());
                    ImageView imageView = new ImageView(image);
                    imageView.setFitWidth(30);
                    imageView.setFitHeight(30);
                    buttonsForGetOrRemoveCourse[i].setGraphic(imageView);
                    buttonsForGetOrRemoveCourse[i].setOnAction(event -> {
                        clientNetwork.removeACourseFromCoursesWantToGetThisTerm(student.getId(), courses.get(m).getId());
                        ShowButtonFunction();
                    });
                    buttonsForGetOrRemoveCourse[i].setCursor(Cursor.HAND);
                    buttonsForGetOrRemoveCourse[i].setLayoutX(1260);
                    buttonsForGetOrRemoveCourse[i].setLayoutY(12 + 80 * i);
                    buttonsForGetOrRemoveCourse[i].setBackground(new Background(new BackgroundFill(student.getColor() , null, null)));
                    pane.getChildren().add(buttonsForGetOrRemoveCourse[i]);


                    buttonsForChangeGroup[i] = new Button();
                    file = new File(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.publicURL) + "regroup.png");
                    image = new Image(file.toURI().toString());
                    ImageView imageView2 = new ImageView(image);
                    imageView2.setFitWidth(30);
                    imageView2.setFitHeight(30);
                    buttonsForChangeGroup[i].setGraphic(imageView2);
                    buttonsForChangeGroup[i].setOnAction(event -> {
                        LinkedList<Integer> coursesGroup = clientNetwork.getCoursesGroup(courses.get(m).getPublicId());
                        ButtonType[] buttons = new ButtonType[coursesGroup.size()];
                        for (int j = 0; j < coursesGroup.size(); j++) {
                            buttons[j] = new ButtonType(coursesGroup.get(j) + "");
                        }
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                        alert.getButtonTypes().clear();
                        alert.getButtonTypes().addAll(buttons);
                        Optional<ButtonType> option = alert.showAndWait();

                        if (option == null) {

                        }
                        else if(option.get() == null){

                        }
                        else {
                            clientNetwork.changeGroupForSelectionUnit(student.getStudentNumber(), student.getCollege() + "", student.getId(), courses.get(m).getId());
                        }
                    });
                    buttonsForChangeGroup[i].setCursor(Cursor.HAND);
                    buttonsForChangeGroup[i].setLayoutX(1120);
                    buttonsForChangeGroup[i].setLayoutY(12 + 80 * i);
                    buttonsForChangeGroup[i].setBackground(new Background(new BackgroundFill(student.getColor() , null, null)));
                    pane.getChildren().add(buttonsForChangeGroup[i]);
                }
                else{
                    buttonsForGetOrRemoveCourse[i] = new Button();
                    File file = new File(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.publicURL) + "tick.png");
                    Image image = new Image(file.toURI().toString());
                    ImageView imageView = new ImageView(image);
                    imageView.setFitWidth(30);
                    imageView.setFitHeight(30);
                    buttonsForGetOrRemoveCourse[i].setGraphic(imageView);
                    buttonsForGetOrRemoveCourse[i].setOnAction(event -> {
                        if(!clientNetwork.addACourseFromCoursesWantToGetThisTerm(student.getId(), courses.get(m).getId())){
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setHeaderText("Error");
                            alert.showAndWait();
                        }
                        ShowButtonFunction();
                    });
                    buttonsForGetOrRemoveCourse[i].setCursor(Cursor.HAND);
                    buttonsForGetOrRemoveCourse[i].setLayoutX(1260);
                    buttonsForGetOrRemoveCourse[i].setLayoutY(12 + 80 * i);
                    buttonsForGetOrRemoveCourse[i].setBackground(new Background(new BackgroundFill(student.getColor() , null, null)));
                    pane.getChildren().add(buttonsForGetOrRemoveCourse[i]);


                    buttonsForRequestToEA[i] = new Button();
                    file = new File(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.publicURL) + "rocket.png");
                    image = new Image(file.toURI().toString());
                    ImageView imageView2 = new ImageView(image);
                    imageView2.setFitWidth(30);
                    imageView2.setFitHeight(30);
                    buttonsForRequestToEA[i].setGraphic(imageView2);
                    buttonsForRequestToEA[i].setOnAction(event -> {
                        boolean b = clientNetwork.sendMessageToEAToGetACourse(student.getStudentNumber(), student.getCollege() + "", courses.get(m).getId());
                        if(b){
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setHeaderText("Your request send.");
                            alert.showAndWait();
                            ShowButtonFunction();
                        }
                        else{
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setHeaderText("You one time sent this message. Please wait for answer.");
                            alert.showAndWait();
                        }
                    });
                    buttonsForRequestToEA[i].setCursor(Cursor.HAND);
                    buttonsForRequestToEA[i].setLayoutX(1120);
                    buttonsForRequestToEA[i].setLayoutY(12 + 80 * i);
                    buttonsForRequestToEA[i].setBackground(new Background(new BackgroundFill(student.getColor() , null, null)));
                    pane.getChildren().add(buttonsForRequestToEA[i]);
                }
            }



            HelpPrint(courses.get(i), i);
        }
    }

    public void HelpPrint(Course course, int counter){
        Font font = Font.font("System", FontWeight.NORMAL, FontPosture.REGULAR, 20);

        Label label1 = new Label(course.getName());
        label1.prefHeight(142);
        label1.prefWidth(57);
        label1.setLayoutX(14);
        label1.setLayoutY(23 + 80 * counter);
        label1.setFont(font);
        pane.getChildren().add(label1);

        Label label2 = new Label(course.getYearExam() + "//" + course.getMonthExam() + "//" + course.getDayExam());
        label2.prefHeight(142);
        label2.prefWidth(57);
        label2.setLayoutX(226);
        label2.setLayoutY(23 + 80 * counter);
        label2.setFont(font);
        pane.getChildren().add(label2);

        Label label3 = new Label(course.getWeight() + "");
        label3.prefHeight(142);
        label3.prefWidth(57);
        label3.setLayoutX(380);
        label3.setLayoutY(23 + 80 * counter);
        label3.setFont(font);
        pane.getChildren().add(label3);

        Label label4 = new Label(course.getGrade().name());
        label4.prefHeight(142);
        label4.prefWidth(57);
        label4.setLayoutX(667);
        label4.setLayoutY(23 + 80 * counter);
        label4.setFont(font);
        pane.getChildren().add(label4);

        Label label5 = new Label(course.getId() + "");
        label5.prefHeight(142);
        label5.prefWidth(57);
        label5.setLayoutX(760);
        label5.setLayoutY(23 + 80 * counter);
        label5.setFont(font);
        pane.getChildren().add(label5);

        String teacherString = clientNetwork.getTeacherOfACourse(course.getId());
        if(teacherString == null){
            teacherString = "------------";
        }
        else if(teacherString.equals("")){
            teacherString = "------------";
        }
        else{
            teacherString = "MR/MS. " + teacherString;
        }
        Label label6 = new Label(teacherString);
        label6.prefHeight(142);
        label6.prefWidth(57);
        label6.setLayoutX(855);
        label6.setLayoutY(23 + 80 * counter);
        label6.setFont(font);
        pane.getChildren().add(label6);



        Label label7 = new Label(course.getNumGetCoursesUntilNow() + " / " + course.getCourseCapacity());
        label7.prefHeight(142);
        label7.prefWidth(57);
        label7.setLayoutX(1030);
        label7.setLayoutY(23 + 80 * counter);
        label7.setFont(font);
        pane.getChildren().add(label7);
    }

    public void suggestedRadioButtonFunction(ActionEvent event) {
        if(fromCollegeRadioButton.isSelected()){
            fromCollegeRadioButton.setSelected(false);
            choiceBoxForCollege.setDisable(true);

        }

        ShowButtonFunction();
    }

    public void fromCollegeRadioButtonFunction(ActionEvent event) {
        if(suggestedRadioButton.isSelected()){
            suggestedRadioButton.setSelected(false);
        }
        choiceBoxForCollege.setDisable(false);

        ShowButtonFunction();
    }

    public void neshanShodeFunction(ActionEvent event) {
        if(allCourses.isSelected()){
            allCourses.setSelected(false);
        }
        fromCollegeRadioButton.setSelected(false);
        suggestedRadioButton.setSelected(false);
        choiceBoxForCollege.setDisable(true);
        suggestedRadioButton.setDisable(true);
        fromCollegeRadioButton.setDisable(true);

        ShowButtonFunction();

    }

    public void allCoursesFunction(ActionEvent event) {
        if(neshanShodeChoiceBox.isSelected()){
            neshanShodeChoiceBox.setSelected(false);
        }
        fromCollegeRadioButton.setSelected(true);
        choiceBoxForCollege.setDisable(false);
        suggestedRadioButton.setDisable(false);
        fromCollegeRadioButton.setDisable(false);

        ShowButtonFunction();

    }






    public void HomeButtonFunction(ActionEvent e) {
        timeline.stop();
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
        timeline.stop();
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
        timeline.stop();
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
        timeline.stop();
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
        timeline.stop();
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
        timeline.stop();
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
        timeline.stop();
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
        timeline.stop();
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
        timeline.stop();
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
        timeline.stop();
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
        timeline.stop();
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
