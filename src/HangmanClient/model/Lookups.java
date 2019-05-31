package HangmanClient.model;

import java.io.IOException;

import HangmanClient.controller.ConnectController;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Lookups {
    private static Lookups ourInstance = new Lookups();

    public static Lookups getInstance() {
        return ourInstance;
    }

    private ImageView hangmanImageView;
    private TextArea missedLettersTextArea;
    private TextArea chatTextArea;
    private TextField guessTextField;
    private Label passwordLabel;
    private Label categoryLabel;
    private int imageNumber = 0;
    private String name;
    private HangmanClient serverClient;
    private Button guessButton;
    private Button voteStartButton;


    private Lookups() {
        hangmanImageView = (ImageView) ConnectController.getGameScene().lookup("#hangmanImageView");
        missedLettersTextArea = (TextArea) ConnectController.getGameScene().lookup("#missedLettersTextArea");
        chatTextArea = (TextArea) ConnectController.getGameScene().lookup("#chatTextArea");
        guessTextField = (TextField) ConnectController.getGameScene().lookup("#guessTextField");
        passwordLabel = (Label) ConnectController.getGameScene().lookup("#passwordLabel");
        guessButton = (Button) ConnectController.getGameScene().lookup("#guessButton");
        voteStartButton = (Button) ConnectController.getGameScene().lookup("#voteStartButton");
        categoryLabel = (Label) ConnectController.getGameScene().lookup("#categoryLabel");

        missedLettersTextArea.setWrapText(true);
    }


    public void updateImage() throws IOException {
        imageNumber++;
        if(imageNumber <= 11) {
            String url = "HangmanClient/img/hang" + imageNumber + ".jpg";
            Image img = new Image(url);
            hangmanImageView.setImage(img);
        }else {
            Platform.runLater(this::showLostAlert);
            guessButton.setDisable(true);
            finishGame();
        }
    }

    public void finishGame() throws IOException {
        resetImage();
        voteStartButton.setDisable(false);
        serverClient.write("finished " + Lookups.getInstance().getName());
        Platform.runLater(()->{
            passwordLabel.setText("");
            categoryLabel.setText("");
            missedLettersTextArea.clear();
        });
    }


    public void resetImage() {
        imageNumber = 0;
        String url = "HangmanClient/img/hang0.jpg";
        Image img = new Image(url);
        hangmanImageView.setImage(img);
    }

    private void showLostAlert() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Congratulations");
        alert.setContentText("Game over");
        alert.setHeaderText("You Lost");
        alert.show();
    }

    public HangmanClient getServerClient() {
        return serverClient;
    }

    public void setServerClient(HangmanClient serverClient) {
        this.serverClient = serverClient;
    }

    public TextField getGuessTextField() {
        return guessTextField;
    }

    public ImageView getHangmanImageView() {
        return hangmanImageView;
    }

    public TextArea getMissedLettersTextArea() {
        return missedLettersTextArea;
    }

    public Label getPasswordLabel() {
        return passwordLabel;
    }

    public int getImageNumber() {
        return imageNumber;
    }

    public Button getVoteStartButton() {
        return voteStartButton;
    }

    public TextArea getChatTextArea() {
        return chatTextArea;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Label getCategoryLabel() {
        return categoryLabel;
    }

    public Button getGuessButton() {
        return guessButton;
    }
}
