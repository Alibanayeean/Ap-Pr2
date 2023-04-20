package controller.admin;


import back.persons.Admin;
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


public class Admin_ChatRoom implements Initializable {

    public static final Logger log = LogManager.getLogger(Admin_ChatRoom.class);

    Admin admin;
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
    Button backButton;

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
    Pane downPane;

    @FXML
    Pane upPane;

    @FXML
    Button sendTextButton;

    @FXML
    TextArea textAreaForText;

    String usernameTo = "";

    boolean isOnPanePage = false;


    @FXML
    void ChangingColor(ActionEvent event) {
        Color color = colorPicker.getValue();
        MenuBar.setBackground(new Background(new BackgroundFill(color, null, null)));
        HomeButton.setBackground(new Background(new BackgroundFill(color, null, null)));
        backButton.setBackground(new Background(new BackgroundFill(color, null, null)));
        timeShowLabel.setBackground(new Background(new BackgroundFill(color, null, null)));
        sendTextButton.setBackground(new Background(new BackgroundFill(color, null, null)));
        admin.color = colorPicker.getValue();
        String s = "#" + Integer.toHexString(color.hashCode());
        clientNetwork.changeColorForAdmin(admin.getUsername(), s);

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

        backButton.setVisible(false);

    }

    public void setAdmin(Admin Admin, ClientNetwork clientNetwork){
        this.clientNetwork = clientNetwork;
        this.admin = Admin;

        Color color = Admin.color;
        MenuBar.setBackground(new Background(new BackgroundFill(color, null, null)));
        timeShowLabel.setBackground(new Background(new BackgroundFill(color, null, null)));
        HomeButton.setBackground(new Background(new BackgroundFill(color, null, null)));
        backButton.setBackground(new Background(new BackgroundFill(color, null, null)));
        sendTextButton.setBackground(new Background(new BackgroundFill(color, null, null)));
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


        downPane.setVisible(false);
        upPane.setVisible(false);

        updateListCell();


    }


    public void updateListCell(){

        String c = "#" + Integer.toHexString(admin.getColor().hashCode());

        leftPane.setStyle("-fx-background-color: " + c + ";");

        listView.setStyle("" +
                "-fx-background-color: " + c + ";" +
                "-fx-text-fill: black;");

        LinkedList<String> names = new LinkedList<>();
        LinkedList<String> times = new LinkedList<>();

        if(isOnPanePage){
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


            String address = clientNetwork.getFileNameOfFromUsername(usernameTo);
            File file = new File(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.publicURL) + admin.getUsername() + "\\" + address);
            if(!file.exists()){
                clientNetwork.sendFileFromServer(address , admin.getUsername());
            }
            Image image = new Image(file.toURI().toString());
            imageViewForPanePage.setImage(image);
            imageViewForPanePage.setFitWidth(45);
            imageViewForPanePage.setFitHeight(45 / image.getWidth() * image.getHeight());

            addFileForPanePage.setBackground(new Background(new BackgroundFill(admin.getColor(), null, null)));

            Font font = Font.font("Comic Sans MS", FontWeight.BOLD, FontPosture.ITALIC, 15);

            labelNameForPanePage.setFont(font);
            labelNameForPanePage.setText(s1);

            file = new File(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.publicURL) + "paperHolder.png");
            image = new Image(file.toURI().toString());
            addFileForPanePage.setGraphic(new ImageView(image));

            PrintForPane();
        }
        else{

            LinkedList<LinkedList<String>> linkedLists = clientNetwork.getAllMessagesFromAPerson(admin.getUsername());
            if(linkedLists.size() == 2){
                names = linkedLists.get(0);
                times = linkedLists.get(1);
                for (int i = 0; i < names.size(); i++) {
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
                        String address =clientNetwork.getFileNameOfFromUsername(username);
                        File file = new File(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.publicURL) + admin.getUsername() + "\\" + address); //TODO
                        if(!file.exists()){
                            clientNetwork.sendFileFromServer(address , admin.getUsername());
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
                    if (event.getClickCount() == 2 && (! cell.isEmpty()) ) {
                        name = cell.getItem();
                        scrollPane.setVisible(true);
                        downPane.setVisible(true);
                        upPane.setVisible(true);
                        isOnPanePage = true;
                        listView.setVisible(false);
                        backButton.setVisible(true);
                        updateListCell();
                    }
                });

                return cell ;

            });

        }
    }

    public void backButtonFunction(ActionEvent event) {
        if(isOnPanePage){
            isOnPanePage = false;
            scrollPane.setVisible(false);
            downPane.setVisible(false);
            upPane.setVisible(false);
            listView.setVisible(true);
            backButton.setVisible(false);
            updateListCell();
        }
        else{
            backButton.setVisible(false);
            updateListCell();
        }
    }

    public void PrintForPane(){
        pane.getChildren().clear();


        LinkedList<LinkedList<String>> linkedLists = clientNetwork.getAllMessagesFromTwoPersons(admin.getUsername(), usernameTo);
        if(linkedLists.size() == 0){
            return;
        }
        LinkedList<String> typeFile = linkedLists.get(0);
        LinkedList<String> address = linkedLists.get(1);
        LinkedList<String> whoSent = linkedLists.get(2); // you | front
        LinkedList<String> messageTime = linkedLists.get(3);



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
                    imageView.setOnMouseClicked(event -> {
                        File fileClicked = new File(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.publicURL) + admin.getUsername() + "/" + address.get(m));
                        if(fileClicked.exists()){
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setHeaderText("go to this folder: " + ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.publicURL)  + admin.getUsername() + "/" + address.get(m));
                            alert.showAndWait();
                            PublicMethods.openADesktopFolder(fileClicked);
                        }
                        else{
                            clientNetwork.sendFileFromServer(address.get(m), admin.getUsername());
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
                    imageView.setOnMouseClicked(event -> {
                        File fileClicked = new File(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.publicURL)  + admin.getUsername() + "/" + address.get(m));
                        if(fileClicked.exists()){
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setHeaderText("go to this folder: " + ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.publicURL)  + admin.getUsername() + "/" + address.get(m));
                            alert.showAndWait();
                            PublicMethods.openADesktopFolder(fileClicked);
                        }
                        else{
                            clientNetwork.sendFileFromServer(address.get(m), admin.getUsername());
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

        stage = ((Stage)(((Scene) backButton.getScene()).getWindow()));
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
            address = time + admin.getUsername().hashCode() + address;


            File file1 = new File(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.publicURL) + admin.getUsername() + "/"  +  address);
            try {
                Files.copy(Paths.get(file.getPath()), Paths.get(file1.getPath()));
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }

            clientNetwork.sendFileToServer( admin.getUsername(), address);

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
            clientNetwork.saveMessage(admin.getUsername(), usernameTo, address, typeFile);
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

            clientNetwork.saveMessage(admin.getUsername(), usernameTo, s, "TEXT");

            textAreaForText.setText("");
            PrintForPane();
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
        }
        catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    public void HomeButtonFunction(ActionEvent e) {
        timeline.stop();


        try{
            stage = ((Stage)(((Scene)timeShowLabel.getScene()).getWindow()));
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Fxml\\admin\\AdminFirstPage.fxml"));
            Parent root = loader.load();
            AdminFirstPage AdminFirstPage = loader.getController();
            AdminFirstPage.setAdmin(admin, clientNetwork);
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
        catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    public void ChatRoom(ActionEvent e) {
        timeline.stop();


        try{
            stage = ((Stage)(((Scene)timeShowLabel.getScene()).getWindow()));
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Fxml\\admin\\Admin_ChatRoom.fxml"));
            Parent root = loader.load();
            Admin_ChatRoom Admin_ChatRoom = loader.getController();
            Admin_ChatRoom.setAdmin(admin, clientNetwork);
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
        catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }



}

