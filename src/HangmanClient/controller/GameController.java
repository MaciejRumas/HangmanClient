package HangmanClient.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import HangmanClient.model.Lookups;
import HangmanClient.model.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class GameController implements Initializable {

    @FXML
    public Button voteStartButton;

    @FXML
    private TextField chatTextField;

    @FXML
    private ImageView hangmanImageView;

    @FXML
    private Button guessButton;

    public void handleDisconnectButton(ActionEvent event) {
        try {
            disconnectUser();
         } catch (IOException e) {
            e.printStackTrace();
        }

        //serverClient.closeSocket();
    }

    public void disconnectUser() throws IOException {
        Lookups.getInstance().getServerClient().write("disconnect " + Lookups.getInstance().getName());
        ConnectController.getGameStage().close();
        clearAllFields();
        Main.getConnectStage().show();
    }

    public void handleGuessButton(ActionEvent event) {
        if(!Lookups.getInstance().getGuessTextField().getText().isEmpty() && Lookups.getInstance().getGuessTextField().getText().length() == 1) {
            try {
                Lookups.getInstance().getServerClient().write("try " + Lookups.getInstance().getGuessTextField().getText());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Lookups.getInstance().getGuessTextField().setText("");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Image hang0 = new Image("HangmanClient/img/hang0.jpg");
        hangmanImageView.setImage(hang0);
        guessButton.setDisable(true);
    }

    public void handleSendButton(ActionEvent event) {
        if(!chatTextField.getText().isEmpty()) {
            try {
                Lookups.getInstance().getServerClient().write("msg " + Lookups.getInstance().getName() + ": " + chatTextField.getText());
                chatTextField.setText("");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void handleVoteStartButton(ActionEvent event) {
        try {
            Lookups.getInstance().getServerClient().write("vote " + Lookups.getInstance().getName());
            voteStartButton.setDisable(true);
            Lookups.getInstance().getMissedLettersTextArea().clear();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void clearAllFields() {
        Lookups.getInstance().getChatTextArea().clear();
        Lookups.getInstance().resetImage();
        Lookups.getInstance().getMissedLettersTextArea().clear();
        Lookups.getInstance().getGuessTextField().setText("");
        Lookups.getInstance().getPasswordLabel().setText("");
    }
}
