package Main;

import clientNetwork.ClientNetwork;
import controller.publicController.LoginPage;
import controller.publicMethods.PublicMethods;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileOutputStream;
import java.time.LocalDateTime;
import org.apache.log4j.Logger;
import org.apache.log4j.LogManager;


public class Main extends Application {
    public static final Logger log = LogManager.getLogger(Main.class);



    private static String path = "./src/Load/Classes/ClassesLoad.txt";
    public static String getPath() {
        return path;
    }



    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        log.info("Program started");


        ClientNetwork clientNetwork = new ClientNetwork();

        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("FXML\\Public\\LoginPage.fxml"));
        Parent root = loader.load();
        LoginPage LoginPage = loader.getController();
        LoginPage.setClientNetwork(clientNetwork);
        LoginPage.setImageView();
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();

        primaryStage.setOnCloseRequest(event -> {
            event.consume();
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Close programme");
            alert.setHeaderText("Are you sure?");
            if(alert.showAndWait().get() == ButtonType.OK){
                log.info("Finished");
                primaryStage.close();
            }
        });
    }
    



}
