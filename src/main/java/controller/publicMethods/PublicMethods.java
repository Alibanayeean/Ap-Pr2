package controller.publicMethods;

import clientNetwork.ClientNetwork;
import config.ConfigIdentifier;
import config.ReadPropertyFile;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import sun.misc.Cleaner;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class PublicMethods {

    public static void initImageOfLogOutButton(Button button){
        File file1 = new File(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.publicURL) + "logOut.png");
        javafx.scene.image.Image image1 = new Image(file1.toURI().toString());
        button.setGraphic(new ImageView(image1));
        button.setStyle(
                "-fx-background-radius: 50em; " +
                        "-fx-min-width: 50px; " +
                        "-fx-min-height: 50px; " +
                        "-fx-max-width: 50px; " +
                        "-fx-max-height: 50px;"
        );
    }

    public static void initImageOfHomeButton(Button button){
        File file3 = new File(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.publicURL) + "homeIcon2.png");
        Image image3 = new Image(file3.toURI().toString());
        button.setGraphic(new ImageView(image3));
    }

    public static void initImageOfMessageButton(Button button){
        File file3 = new File(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.publicURL) + "messages.png");
        Image image3 = new Image(file3.toURI().toString());
        button.setGraphic(new ImageView(image3));
    }

    public static void initImageOfBackButton(Button button){
        File file = new File(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.publicURL) + "back.png");
        Image image = new Image(file.toURI().toString());
        button.setGraphic(new ImageView(image));
    }

    public static void initClock(Label label){
        Timeline clock = new Timeline(new KeyFrame(Duration.ZERO, e -> {

            LocalDateTime instance = LocalDateTime.now();
            DateTimeFormatter formatter
                    = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss");
            String formattedString = formatter.format(instance);
            label.setText(formattedString);
            label.setAlignment(Pos.CENTER);
        }),
                new KeyFrame(Duration.seconds(1))
        );
        clock.setCycleCount(Animation.INDEFINITE);
        clock.play();
    }

    public static void initImageOfPerson(ClientNetwork clientNetwork, ImageView imageViewUser, String username, String fileName){
        if(fileName == null ){
            File file = new File(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.publicURL) + username + "\\" + "userIcon256.png");
            if(!file.exists()){
                clientNetwork.sendFileFromServer("userIcon256.png", username);
            }
            Image image2 = new Image(file.toURI().toString());
            imageViewUser.setImage(image2);
        }
        else if(fileName.equals("")){
            File file = new File(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.publicURL)  + username + "\\" + "userIcon256.png");
            if(!file.exists()){
                clientNetwork.sendFileFromServer("userIcon256.png", username);
            }
            Image image2 = new Image(file.toURI().toString());
            imageViewUser.setImage(image2);
        }
        else if(fileName.equals("null")){
            File file = new File(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.publicURL)  + username + "\\" + "userIcon256.png");
            if(!file.exists()){
                clientNetwork.sendFileFromServer("userIcon256.png", username);
            }
            Image image2 = new Image(file.toURI().toString());
            imageViewUser.setImage(image2);
        }
        else{
            File file = new File(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.publicURL)  + username + "\\" + "" + fileName);
            if(!file.exists()){
                clientNetwork.sendFileFromServer(fileName, username);
            }
            Image image2 = new Image(file.toURI().toString());
            imageViewUser.setImage(image2);
        }
    }

    public static Timeline reconnectionTimeline (Button reconnectionButton, ClientNetwork clientNetwork, ImageView connectionStatus){
        reconnectionButton.setVisible(false);
        Timeline timelineForConnected = new Timeline(new KeyFrame(Duration.seconds(2), new EventHandler<ActionEvent>(){
            int timesForFirst = 0;
            int timesForSecond = 0;

            @Override
            public void handle(ActionEvent event) {
                clientNetwork.testConnection();
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
        timelineForConnected.setCycleCount(Animation.INDEFINITE);
        timelineForConnected.play();
        return timelineForConnected;
    }

    public static void openADesktopFolder(File file){
        try {
            Desktop.getDesktop().open(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
