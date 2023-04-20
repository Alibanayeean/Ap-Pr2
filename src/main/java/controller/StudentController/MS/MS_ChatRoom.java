package controller.StudentController.MS;


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
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.ResourceBundle;

public class MS_ChatRoom implements Initializable {

    public static final Logger log = LogManager.getLogger(MS_ChatRoom.class);

    Student student;
    ClientNetwork clientNetwork;

    @FXML
    javafx.scene.control.MenuBar MenuBar;

    @FXML
    ColorPicker colorPicker;

    @FXML
    Button LogOutButton;

    @FXML
    Button HomeButton;


    @FXML
    Label timeShowLabel;


    Stage stage;

    Timeline timeline;

    @FXML
    Button reconnectionButton;

    @FXML
    ImageView connectionStatus;

    @FXML
    ListView<String> listView;

    @FXML
    ListView<String> listViewForSendFile;

    @FXML
    Button plusButton;

    @FXML
    ScrollPane scrollPane;

    @FXML
    Pane pane;

    @FXML
    Button addFileForPanePage;

    @FXML
    ImageView imageViewForPanePage;

    @FXML
    Label labelNameForPanePage;

    @FXML
    Pane leftPane;

    String name = "";

    @FXML
    ImageView whatsApp;

    @FXML
    ImageView telegram;

    @FXML
    ImageView sharif;

    @FXML
    ImageView twitter;


    @FXML
    TextField textFieldForSearch;

    @FXML
    ChoiceBox<String> choiceBox;

    @FXML
    Pane downPane;

    @FXML
    Pane upPane;

    @FXML
    Button sendButton;

    @FXML
    Button sendTextButton;

    @FXML
    Button adminButton;

    @FXML
    TextArea textAreaForText;

    String usernameTo = "";


    int numClicked = 0;
    boolean isOnPanePage = false;

    LinkedList<String> wantsToSendFileInGroups = new LinkedList<>();

    @FXML
    void ChangingColor(ActionEvent event) {
        Color color = colorPicker.getValue();
        MenuBar.setBackground(new Background(new BackgroundFill(color, null, null)));
        HomeButton.setBackground(new Background(new BackgroundFill(color, null, null)));
        plusButton.setBackground(new Background(new BackgroundFill(color, null, null)));
        timeShowLabel.setBackground(new Background(new BackgroundFill(color, null, null)));
        sendButton.setBackground(new Background(new BackgroundFill(color, null, null)));
        sendTextButton.setBackground(new Background(new BackgroundFill(color, null, null)));
        adminButton.setBackground(new Background(new BackgroundFill(color, null, null)));
        student.color = colorPicker.getValue();
        String s = "#" + Integer.toHexString(color.hashCode());
        clientNetwork.changeColorForStudent(student.getStudentNumber(), s);

        updateListCell();

        log.info("change background");

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        PublicMethods.initImageOfLogOutButton(LogOutButton);

        PublicMethods.initImageOfHomeButton(HomeButton);

        PublicMethods.initClock(timeShowLabel);

        File file3 = new File(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.publicURL) + "send.png");
        Image image3 = new Image(file3.toURI().toString());
        ImageView imageView = new ImageView(image3);
        sendTextButton.setGraphic(imageView);

        file3 = new File(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.publicURL) + "sharifClipart.png");
        image3 = new Image(file3.toURI().toString());
        sharif.setImage(image3);

        file3 = new File(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.publicURL) + "telegram.png");
        image3 = new Image(file3.toURI().toString());
        telegram.setImage(image3);

        file3 = new File(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.publicURL) + "whatsapp.png");
        image3 = new Image(file3.toURI().toString());
        whatsApp.setImage(image3);

        file3 = new File(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.publicURL) + "twitter.png");
        image3 = new Image(file3.toURI().toString());
        twitter.setImage(image3);

        file3 = new File(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.publicURL) + "headset.png");
        image3 = new Image(file3.toURI().toString());
        adminButton.setGraphic(new ImageView(image3));

        String[] items = {"Student", "Teacher", "Admin"};
        choiceBox.getItems().addAll(items);

        textFieldForSearch.setOnAction(event -> {
            updateListCell();
        });

        choiceBox.setOnAction(event -> {
            updateListCell();
        });


    }

    public void setStudent(Student student, ClientNetwork clientNetwork){
        this.clientNetwork = clientNetwork;
        this.student = student;

        Color color = student.color;
        MenuBar.setBackground(new Background(new BackgroundFill(color, null, null)));
        timeShowLabel.setBackground(new Background(new BackgroundFill(color, null, null)));
        HomeButton.setBackground(new Background(new BackgroundFill(color, null, null)));
        plusButton.setBackground(new Background(new BackgroundFill(color, null, null)));
        sendButton.setBackground(new Background(new BackgroundFill(color, null, null)));
        sendTextButton.setBackground(new Background(new BackgroundFill(color, null, null)));
        adminButton.setBackground(new Background(new BackgroundFill(color, null, null)));
        colorPicker.setValue(color);


        reconnectionButton.setVisible(false);
        timeline = new Timeline(new KeyFrame(Duration.seconds(5), new EventHandler<ActionEvent>(){
            int timesForFirst = 0;
            int timesForSecond = 0;

            @Override
            public void handle(ActionEvent event) {
                updateListCell();
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


        String s = "#" + Integer.toHexString(color.hashCode());
        leftPane.setStyle("-fx-background-color: " + s + ";");
        scrollPane.setVisible(false);



        choiceBox.setVisible(false);
        textFieldForSearch.setVisible(false);
        
        downPane.setVisible(false);
        upPane.setVisible(false);


        sendButton.setVisible(false);
        listViewForSendFile.setVisible(false);

        updateListCell();


    }

    public void updateListCell(){

        String c = "#" + Integer.toHexString(student.getColor().hashCode());

        leftPane.setStyle("-fx-background-color: " + c + ";");

        listView.setStyle("" +
                "-fx-background-color: " + c + ";" +
                "-fx-text-fill: black;");

        LinkedList<String> names = new LinkedList<>();
        LinkedList<String> times = new LinkedList<>();

        if(isOnPanePage){
            sendButton.setVisible(false);
            listViewForSendFile.setVisible(false);
            wantsToSendFileInGroups = new LinkedList<>();

            plusButton.setText("<-");

            String s1 = "";
            for (int i = 0; i < name.length(); i++) {
                if(name.charAt(i) == '.'){
                    s1 = name.substring(0, i);
                    break;
                }
            }

            if(s1.equals("")){
                s1 = name;
            }

            int first = name.indexOf('(');
            int second = name.indexOf(')');
            usernameTo = name.substring(first + 1, second);


            String address =clientNetwork.getFileNameOfFromUsername(usernameTo);
            File file = new File(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.publicURL) + student.getStudentNumber() + "\\" + address);
            if(!file.exists()){
                clientNetwork.sendFileFromServer(address , student.getStudentNumber());
            }
            Image image = new Image(file.toURI().toString());
            imageViewForPanePage.setImage(image);
            imageViewForPanePage.setFitWidth(45);
            imageViewForPanePage.setFitHeight(45 / image.getWidth() * image.getHeight());

            addFileForPanePage.setBackground(new Background(new BackgroundFill(student.getColor(), null, null)));

            Font font = Font.font("Comic Sans MS", FontWeight.BOLD, FontPosture.ITALIC, 15);

            labelNameForPanePage.setFont(font);
            labelNameForPanePage.setText(s1);

            file = new File(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.publicURL) + "paperHolder.png");
            image = new Image(file.toURI().toString());
            addFileForPanePage.setGraphic(new ImageView(image));

            PrintForPane();
        }
        else{
            if(sendButton.isVisible()){
                listViewForSendFile.setVisible(true);
                String[] s = new String[wantsToSendFileInGroups.size()];
                wantsToSendFileInGroups.toArray(s);
                ObservableList<String> items = FXCollections.observableArrayList(s);
                listViewForSendFile.setItems(items);
            }
            else{
                listViewForSendFile.setVisible(false);
            }

            if(clientNetwork.isConnected){
                if(!getResultOfChoiceBox().equals("")){
                    if(textFieldForSearch.getText() == null){
                        sendButton.setVisible(true);
                        listViewForSendFile.setVisible(true);
                        if(numClicked == 0){
                            LinkedList<LinkedList<String>> linkedLists = clientNetwork.getAllMessagesFromAPerson(student.getStudentNumber());
                            names = linkedLists.get(0);
                            times = linkedLists.get(1);
                            for (int i = 0; i < times.size(); i++) {
                                names.set(i, names.get(i) + "... ... ..." + times.get(i));
                            }
                        }
                        else{
                            names = clientNetwork.getAllStudentsFromOneCollegeAndFromOneYear(student.getCollege() + "", student.getYearComeToUniversity(), student.getId(), student.getHelpTeacher());
                        }

                    }
                    else if(!textFieldForSearch.getText().equals("")){
                        sendButton.setVisible(false);
                        listViewForSendFile.setVisible(false);
                        names = clientNetwork.FindATeacherOrStudentForChatRoom(student.getStudentNumber(), getResultOfChoiceBox(), textFieldForSearch.getText());
                    }
                    else{
                        sendButton.setVisible(true);
                        listViewForSendFile.setVisible(true);
                        if(numClicked == 0){
                            LinkedList<LinkedList<String>> linkedLists = clientNetwork.getAllMessagesFromAPerson(student.getStudentNumber());
                            names = linkedLists.get(0);
                            times = linkedLists.get(1);
                            for (int i = 0; i < times.size(); i++) {
                                names.set(i, names.get(i) + "... ... ..." + times.get(i));
                            }
                        }
                        else{
                            names = clientNetwork.getAllStudentsFromOneCollegeAndFromOneYear(student.getCollege() + "", student.getYearComeToUniversity(), student.getId(), student.getHelpTeacher());
                        }
                    }

                }
                else if(numClicked == 0){
                    LinkedList<LinkedList<String>> linkedLists = clientNetwork.getAllMessagesFromAPerson(student.getStudentNumber());
                    names = linkedLists.get(0);
                    times = linkedLists.get(1);
                    for (int i = 0; i < times.size(); i++) {
                        names.set(i, names.get(i) + "... ... ..." + times.get(i));
                    }
                }
                else{
                    names = clientNetwork.getAllStudentsFromOneCollegeAndFromOneYear(student.getCollege() + "", student.getYearComeToUniversity(), student.getId(), student.getHelpTeacher());
                }
            }
            else{
                names.addAll(clientNetwork.frontName);
                times.addAll(clientNetwork.messageTime);

                for (int i = 0; i < names.size(); i++) {
                    for (int j = i + 1; j < names.size(); j++) {
                        if(names.get(i).equals(names.get(j))){
                            names.remove(j);
                            times.remove(j);
                            i = -1;
                            break;
                        }
                    }
                }
                for (int i = 0; i < times.size(); i++) {
                    names.set(i, names.get(i) + "... ... ..." + times.get(i));
                }
            }

            String[] s = new String[names.size()];
            names.toArray(s);

            ObservableList<String> items = FXCollections.observableArrayList (s);
            listView.setItems(items);

            listView.setCellFactory(param -> {
                ListCell<String> cell = new ListCell<String>(){

                    ImageView imageView;
                    @Override
                    public void updateItem(String name, boolean empty){
                        super.updateItem(name, empty);
                        if (empty) {
                            setText(null);
                            setGraphic(null);
                            return;
                        }


                        int first = name.indexOf('(');
                        int second = name.indexOf(')');
                        String username = name.substring(first + 1, second);
                        String address = clientNetwork.getFileNameOfFromUsername(username);
                        File file = new File(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.publicURL) + student.getStudentNumber() + "\\" + address); //TODO
                        if(!file.exists()){
                            clientNetwork.sendFileFromServer(address , student.getStudentNumber());
                        }

                        Image image = new Image(file.toURI().toString());
                        imageView = new ImageView();

                        imageView.setImage(image);
                        imageView.setFitWidth(50);
                        imageView.setFitHeight(50 / image.getWidth() * image.getHeight());


                        Font font = Font.font("Comic Sans MS", FontWeight.BOLD, FontPosture.ITALIC, 20);

                        setFont(font);
                        setText(name);
                        setGraphic(imageView);

                    }
                };

                cell.addEventFilter(MouseEvent.MOUSE_ENTERED, event -> {
                    if(!cell.isEmpty()){
                        cell.setCursor(Cursor.HAND);
                    }
                });

                cell.addEventFilter(MouseEvent.MOUSE_EXITED, event -> {
                    if(!cell.isEmpty()){
                        cell.setCursor(Cursor.DEFAULT);
                    }
                });

                cell.setOnMouseClicked(event -> {
                    if(!getResultOfChoiceBox().equals("")){
                        if(textFieldForSearch.getText() == null){

                            if (event.getClickCount() == 2 && (! cell.isEmpty()) ) {
                                name = cell.getItem();
                                scrollPane.setVisible(true);
                                downPane.setVisible(true);
                                upPane.setVisible(true);
                                isOnPanePage = true;
                                listView.setVisible(false);
                                sendButton.setVisible(false);
                                updateListCell();
                            }
                            else if (event.getClickCount() == 1 && (! cell.isEmpty()) ) {
                                name = cell.getItem();
                                sendButton.setVisible(true);
                                int first = name.indexOf('(');
                                int second = name.indexOf(')');
                                String username = name.substring(first + 1, second);
                                boolean b = true;
                                for (int i = 0; i < wantsToSendFileInGroups.size(); i++) {
                                    if(wantsToSendFileInGroups.get(i).equals(username)){
                                        b = false;
                                    }
                                }
                                if(b){
                                    wantsToSendFileInGroups.add(username);
                                }
                                updateListCell();
                            }

                        }
                        else if(!textFieldForSearch.getText().equals("")){
                            if (event.getClickCount() == 2 && (! cell.isEmpty()) ) {
                                if(clientNetwork.sendMessageToAPersonForRequestChatRoom(getResultOfChoiceBox(), textFieldForSearch.getText(), student.getStudentNumber())){
                                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                    alert.setHeaderText("Your request send");
                                    alert.showAndWait();
                                }
                                else{
                                    Alert alert = new Alert(Alert.AlertType.ERROR);
                                    alert.setHeaderText("You one time send request. Wait for answer.");
                                    alert.showAndWait();
                                }
                            }

                        }
                        else{
                            if (event.getClickCount() == 2 && (! cell.isEmpty()) ) {
                                name = cell.getItem();
                                scrollPane.setVisible(true);
                                downPane.setVisible(true);
                                upPane.setVisible(true);
                                isOnPanePage = true;
                                listView.setVisible(false);
                                sendButton.setVisible(false);
                                updateListCell();
                            }
                            else if (event.getClickCount() == 1 && (! cell.isEmpty()) ) {
                                name = cell.getItem();
                                sendButton.setVisible(true);
                                int first = name.indexOf('(');
                                int second = name.indexOf(')');
                                String username = name.substring(first + 1, second);
                                boolean b = true;
                                for (int i = 0; i < wantsToSendFileInGroups.size(); i++) {
                                    if(wantsToSendFileInGroups.get(i).equals(username)){
                                        b = false;
                                    }
                                }
                                if(b){
                                    wantsToSendFileInGroups.add(username);
                                }
                                updateListCell();
                            }
                        }
                    }
                    else if (event.getClickCount() == 2 && (! cell.isEmpty()) ) {
                        name = cell.getItem();
                        scrollPane.setVisible(true);
                        downPane.setVisible(true);
                        upPane.setVisible(true);
                        isOnPanePage = true;
                        listView.setVisible(false);
                        sendButton.setVisible(false);
                        updateListCell();
                    }
                    else if (event.getClickCount() == 1 && (! cell.isEmpty()) ) {
                        name = cell.getItem();
                        sendButton.setVisible(true);
                        int first = name.indexOf('(');
                        int second = name.indexOf(')');
                        String username = name.substring(first + 1, second);
                        boolean b = true;
                        for (int i = 0; i < wantsToSendFileInGroups.size(); i++) {
                            if(wantsToSendFileInGroups.get(i).equals(username)){
                                b = false;
                            }
                        }
                        if(b){
                            wantsToSendFileInGroups.add(username);
                        }
                        updateListCell();
                    }
                });

                return cell ;

            });

        }
    }

    public String  getResultOfChoiceBox(){
        String stringChoiceBox = (String) choiceBox.getValue();
        if(stringChoiceBox == null){
            return "";
        }
        return stringChoiceBox ;
    }

    public void plusButtonFunction(ActionEvent event) {
        if(clientNetwork.isConnected){
            if(isOnPanePage){
                isOnPanePage = false;
                scrollPane.setVisible(false);
                downPane.setVisible(false);
                upPane.setVisible(false);
                listView.setVisible(true);
                choiceBox.setVisible(false);
                textFieldForSearch.setVisible(false);
                sendButton.setVisible(false);
                listViewForSendFile.setVisible(false);
                wantsToSendFileInGroups = new LinkedList<>();

                if(numClicked == 0){
                    choiceBox.setVisible(false);
                    textFieldForSearch.setVisible(false);
                    textFieldForSearch.setText(null);
                    choiceBox.setValue(null);
                    plusButton.setText("+");
                }
                else{
                    plusButton.setText("<-");
                    choiceBox.setVisible(true);
                    textFieldForSearch.setVisible(true);
                }

                updateListCell();
            }
            else{
                numClicked++;
                numClicked = numClicked % 2;
                wantsToSendFileInGroups = new LinkedList<>();
                sendButton.setVisible(false);
                listViewForSendFile.setVisible(false);
                if(numClicked == 0){
                    choiceBox.setVisible(false);
                    textFieldForSearch.setVisible(false);
                    textFieldForSearch.setText(null);
                    choiceBox.setValue(null);
                    plusButton.setText("+");
                }
                else{
                    plusButton.setText("<-");
                    choiceBox.setVisible(true);
                    textFieldForSearch.setVisible(true);
                }
                updateListCell();
            }
        }
        else{
            isOnPanePage = false;
        }
    }

    public void PrintForPane(){
        pane.getChildren().clear();
        LinkedList<String> typeFile = new LinkedList<>();
        LinkedList<String> address = new LinkedList<>();
        LinkedList<String> whoSent = new LinkedList<>();
        LinkedList<String> messageTime = new LinkedList<>();
        if(clientNetwork.isConnected) {
            LinkedList<LinkedList<String>> linkedLists = clientNetwork.getAllMessagesFromTwoPersons(student.getStudentNumber(), usernameTo);
            if(linkedLists.size() != 4){
                return;
            }
            typeFile = linkedLists.get(0);
            address = linkedLists.get(1);
            whoSent = linkedLists.get(2); // you | front
            messageTime = linkedLists.get(3);
        }
        else{
            typeFile = clientNetwork.typeFile;
            address = clientNetwork.fileAddress;
            whoSent = clientNetwork.whoSend;
            messageTime = clientNetwork.messageTime;
        }





        pane.setPrefHeight(messageTime.size() * 120 + 30);


        for (int i = 0; i < messageTime.size(); i++) {
            String fileName = "";
            if(typeFile.get(i).toLowerCase().equals("PICTURE".toLowerCase())){
                fileName = typeFile.get(i).toLowerCase() + ".png";
            }
            else if(typeFile.get(i).toLowerCase().equals("PDF".toLowerCase())){
                fileName = typeFile.get(i).toLowerCase() + ".png";
            }
            else if(typeFile.get(i).toLowerCase().equals("MUSIC".toLowerCase())){
                fileName = typeFile.get(i).toLowerCase() + ".png";
            }
            else if(typeFile.get(i).toLowerCase().equals("VIDEO".toLowerCase())){
                fileName = typeFile.get(i).toLowerCase() + ".png";
            }


            Font font1 = Font.font("Comic Sans MS", FontWeight.BOLD, FontPosture.ITALIC, 15);
            Font font2 = Font.font("System", FontWeight.NORMAL, FontPosture.ITALIC, 10);

            if(whoSent.get(i).equals("you")){
                if(typeFile.get(i).toLowerCase().equals("text")){
                    Label label = new Label(address.get(i));
                    label.setFont(font1);
                    label.setLayoutX(513);
                    label.setLayoutY(i * 120);
                    pane.getChildren().add(label);

                    String s = messageTime.get(i);
                    Label labelTime = new Label(s);
                    labelTime.prefHeight(40);
                    labelTime.prefWidth(120);
                    labelTime.setLayoutX(513 - s.length() * 4);
                    labelTime.setLayoutY(100 + i * 120 + label.getHeight());
                    labelTime.setFont(font2);
                    pane.getChildren().add(labelTime);
                }
                else{
                    File file = new File(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.publicURL) + fileName);
                    Image image = new Image(file.toURI().toString());
                    ImageView imageView = new ImageView(image);
                    imageView.setLayoutX(513);
                    imageView.setLayoutY(i * 120);
                    pane.getChildren().add(imageView);

                    String s = address.get(i) + " | " + messageTime.get(i);
                    Label labelTime = new Label(s);
                    labelTime.prefHeight(40);
                    labelTime.prefWidth(120);
                    labelTime.setLayoutX(513 - s.length() * 4);
                    labelTime.setLayoutY(70 + i * 120);
                    labelTime.setFont(font2);
                    pane.getChildren().add(labelTime);

                    final int m = i;
                    String addressForImage = address.get(m);
                    imageView.setOnMouseClicked(event -> {
                        File fileClicked = new File(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.publicURL) + student.getStudentNumber() + "/" + addressForImage);
                        if(fileClicked.exists()){
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setHeaderText("go to this folder: " + ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.publicURL)  + student.getStudentNumber() + "/" + addressForImage);
                            alert.showAndWait();
                            PublicMethods.openADesktopFolder(fileClicked);
                        }
                        else{
                            clientNetwork.sendFileFromServer(addressForImage, student.getStudentNumber());
                        }
                    });

                    imageView.setCursor(Cursor.HAND);
                }
            }
            else{
                if(typeFile.get(i).toLowerCase().equals("text")){
                    Label label = new Label(address.get(i));
                    label.setFont(font1);
                    label.setLayoutX(10);
                    label.setLayoutY(i * 120);
                    pane.getChildren().add(label);

                    String s = messageTime.get(i);
                    Label labelTime = new Label(s);
                    labelTime.prefHeight(40);
                    labelTime.prefWidth(120);
                    labelTime.setLayoutX(10);
                    labelTime.setLayoutY(100 + i * 120 + label.getHeight());
                    labelTime.setFont(font2);
                    pane.getChildren().add(labelTime);
                }
                else{
                    File file = new File(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.publicURL) + fileName);
                    Image image = new Image(file.toURI().toString());
                    ImageView imageView = new ImageView(image);
                    imageView.setLayoutX(0);
                    imageView.setLayoutY(i * 120);
                    pane.getChildren().add(imageView);

                    Label labelTime = new Label(address.get(i) + " | " + messageTime.get(i));
                    labelTime.prefHeight(40);
                    labelTime.prefWidth(120);
                    labelTime.setLayoutX(0);
                    labelTime.setLayoutY(70 + i * 120);
                    labelTime.setFont(font2);
                    pane.getChildren().add(labelTime);


                    final int m = i;
                    String addressForImage = address.get(m);

                    imageView.setOnMouseClicked(event -> {
                        File fileClicked = new File(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.publicURL)  + student.getStudentNumber() + "/" + addressForImage);
                        if(fileClicked.exists()){
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setHeaderText("go to this folder: " + ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.publicURL)  + student.getStudentNumber() + "/" + addressForImage);
                            alert.showAndWait();
                            PublicMethods.openADesktopFolder(fileClicked);
                        }
                        else{
                            clientNetwork.sendFileFromServer(addressForImage, student.getStudentNumber());
                        }
                    });

                    imageView.setCursor(Cursor.HAND);
                }
            }
        }

        scrollPane.layout();
        scrollPane.setVvalue(scrollPane.getVmax());
    }

    public void addFileForPanePageFunction(ActionEvent event) {

        stage = ((Stage)(((Scene)plusButton.getScene()).getWindow()));
        FileChooser fil_chooser = new FileChooser();

        fil_chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image file", "*.png", "*.jpg", "*.pdf", "*.mp3", "*.mp4"));
        File file = fil_chooser.showOpenDialog(stage);

        if (file != null) {
            String ss = file.getPath();
            int index = ss.lastIndexOf('.');
            String address = ss.substring(index);
            String time = LocalDateTime.now().toString();
            time = time.replace(':', '-');
            time = time.replace('.', '-');
            address = time + student.getStudentNumber().hashCode() + address;


            File file1 = new File(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.publicURL) + student.getStudentNumber() + "/"  +  address);
            try {
                Files.copy(Paths.get(file.getPath()), Paths.get(file1.getPath()));
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }

            clientNetwork.sendFileToServer( student.getStudentNumber(), address);

            String typeFile = "";
            index = address.lastIndexOf('.');
            String suffix = address.substring(index + 1);

            if(suffix.toLowerCase().equals("png")){
                typeFile = "PICTURE";
            }
            else if(suffix.toLowerCase().equals("jpg")){
                typeFile = "PICTURE";
            }
            else if(suffix.toLowerCase().equals("pdf")){
                typeFile = "PDF";
            }
            else if(suffix.toLowerCase().equals("mp3")){
                typeFile = "MUSIC";
            }
            else if(suffix.toLowerCase().equals("mp4")){
                typeFile = "VIDEO";
            }
            clientNetwork.saveMessage(student.getStudentNumber(), usernameTo, address, typeFile);
            PrintForPane();
        }


    }

    public void sendTextFunction(ActionEvent event) {
        if(textAreaForText.getText() == null){
            return;
        }
        else if(textAreaForText.getText().equals("")){
            return;
        }
        else{


            String s = "";
            for (int i = 0; i < textAreaForText.getText().length(); i = i + 10) {
                if(i + 10 > textAreaForText.getText().length()){
                    s = s + textAreaForText.getText().substring(i);
                    break;
                }
                s = s + textAreaForText.getText().substring(i, i + 10) + '\n';
            }

            clientNetwork.saveMessage(student.getStudentNumber(), usernameTo, s, "TEXT");

            textAreaForText.setText("");
            PrintForPane();
        }
    }

    public void sendFileAction(){
        sendButton.setVisible(false);
        listViewForSendFile.setVisible(false);

        stage = ((Stage)(((Scene)plusButton.getScene()).getWindow()));
        FileChooser fil_chooser2 = new FileChooser();
        fil_chooser2.getExtensionFilters().add(new FileChooser.ExtensionFilter("Select your file", "*.png", "*.jpg", "*.pdf", "*.mp3", "*.mp4"));
        File file = fil_chooser2.showOpenDialog(stage);
        
        if (file != null) {
            String ss = file.getPath();
            int index = ss.lastIndexOf('.');
            String address = ss.substring(index);

            String time = LocalDateTime.now().toString();
            time = time.replace(':', '-');
            time = time.replace('.', '-');
            address = time + student.getStudentNumber().hashCode() + address;

            File file1 = new File(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.publicURL) + student.getStudentNumber() + "/"  +  address);
            try {
                Files.copy(Paths.get(file.getPath()), Paths.get(file1.getPath()));
            } catch (IOException e) {
                e.printStackTrace();
            }
            clientNetwork.sendFileToServer(student.getStudentNumber(), address);


            String typeFile = "";
            index = address.lastIndexOf('.');
            String suffix = address.substring(index + 1);
            if(suffix.toLowerCase().equals("png")){
                typeFile = "PICTURE";
            }
            else if(suffix.toLowerCase().equals("jpg")){
                typeFile = "PICTURE";
            }
            else if(suffix.toLowerCase().equals("pdf")){
                typeFile = "PDF";
            }
            else if(suffix.toLowerCase().equals("mp3")){
                typeFile = "MUSIC";
            }
            else if(suffix.toLowerCase().equals("mp4")){
                typeFile = "VIDEO";
            }

            for (int i = 0; i < wantsToSendFileInGroups.size(); i++) {
                String s1 = "";
                for (int j = 0; j < wantsToSendFileInGroups.get(i).length(); j++) {
                    if(wantsToSendFileInGroups.get(i).charAt(j) == '.'){
                        s1 = wantsToSendFileInGroups.get(i).substring(0, j);
                        break;
                    }
                }

                if(s1.equals("")){
                    s1 = wantsToSendFileInGroups.get(i);
                }

                clientNetwork.saveMessage(student.getStudentNumber(), wantsToSendFileInGroups.get(i), address, typeFile);
            }

            wantsToSendFileInGroups = new LinkedList<>();

        }
        else{
            wantsToSendFileInGroups = new LinkedList<>();
        }

    }

    public void adminButtonFunction(ActionEvent e){
        isOnPanePage = true;
        usernameTo = "1";
        name = "admin(1)";
        scrollPane.setVisible(true);
        downPane.setVisible(true);
        upPane.setVisible(true);
        listView.setVisible(false);
        choiceBox.setVisible(false);
        textFieldForSearch.setVisible(false);
        sendButton.setVisible(false);
        listViewForSendFile.setVisible(false);
        wantsToSendFileInGroups = new LinkedList<>();
        updateListCell();
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

