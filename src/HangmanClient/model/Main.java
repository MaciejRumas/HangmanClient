package HangmanClient.model;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import HangmanClient.controller.GameController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    private static Stage connectStage;

    @Override
    public void start(Stage primaryStage) throws Exception{
        URL url = new File("src/HangmanClient/view/connectScene.fxml").toURL();
        Parent root = FXMLLoader.load(url);

        Scene connectScene = new Scene(root);

        connectStage = primaryStage;
        connectStage.setTitle("Hangman Client");
        connectStage.setScene(connectScene);
        connectStage.setResizable(false);
        connectStage.show();
        /*connectStage.setOnCloseRequest(event -> {
            try {
                Lookups.getInstance().getServerClient().closeSocket();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });*/

    }

    public static void main(String[] args) {
        launch(args);
    }

    public static Stage getConnectStage() {
        return connectStage;
    }

}
