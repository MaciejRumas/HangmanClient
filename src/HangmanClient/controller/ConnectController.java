package HangmanClient.controller;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import HangmanClient.model.HangmanClient;
import HangmanClient.model.Lookups;
import HangmanClient.model.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ConnectController implements Initializable {

    private static Scene gameScene;
    private static Stage gameStage;

    @FXML
    private TextField serverIpTextField;

    @FXML
    private TextField portTextField;

    @FXML
    private Label portErrorLabel;

    @FXML
    private Label ipErrorLabel;

    @FXML
    private TextField nameTextField;

    public void handleConnectButton(ActionEvent event) {
        if (portFieldOk()) {
            int port = Integer.parseInt(portTextField.getText());
            String serverAddress = serverIpTextField.getText();

            if (ipValidation(serverAddress)) {

                Lookups.getInstance().setServerClient(new HangmanClient(serverAddress, port));
                if (Lookups.getInstance().getServerClient().connect()) {
                    try {
                        gameStage.show();
                        Lookups.getInstance().getServerClient().startMessageReader();
                        Main.getConnectStage().hide();

                        Lookups.getInstance().setName(nameTextField.getText());
                        Lookups.getInstance().getServerClient().write("connect " + Lookups.getInstance().getName());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }
        }
    }

    private boolean portFieldOk() {
        try {
            int port = Integer.parseInt(portTextField.getText());
            if (port >= 0 && port <= 65535) {
                portErrorLabel.setText("");
                return true;
            }
        } catch (NumberFormatException e) {
            portErrorLabel.setText("port must be between 0 and 65535");
        }
        return false;
    }

    private boolean ipValidation(String address) {
        if (address.equals("localhost") || address.matches("^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$")) {
            ipErrorLabel.setText("");
            return true;
        }
        ipErrorLabel.setText("Wrong IP");
        return false;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            URL url = new File("src/HangmanClient/view/gameScene.fxml").toURL();
            gameScene = new Scene(FXMLLoader.load(url));
            gameStage = new Stage();
            gameStage.setTitle("Hangman");
            gameStage.setScene(gameScene);
            gameStage.setResizable(false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Stage getGameStage() {
        return gameStage;
    }

    public static Scene getGameScene() {
        return gameScene;
    }
}