package HangmanClient.controller;

import java.io.IOException;

import HangmanClient.model.Lookups;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class ChoosingController {

    @FXML
    public TextField categoryTextField;

    @FXML
    public TextField passwordTextField;

    public void handleDoneButton(ActionEvent event) {
        try {
            if (!categoryTextField.getText().isEmpty() && !passwordTextField.getText().isEmpty()) {
                Lookups.getInstance().getServerClient().sendPasswordToServer(categoryTextField.getText(), passwordTextField.getText());
                Lookups.getInstance().getServerClient().getChoosingStage().close();
                Lookups.getInstance().getGuessButton().setDisable(false);
                categoryTextField.setText("");
                passwordTextField.setText("");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
