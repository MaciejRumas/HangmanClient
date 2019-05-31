package HangmanClient.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Modality;
import javafx.stage.Stage;


public class HangmanClient {

    private final String serverName;
    private final int serverPort;
    private Socket socket;
    private InputStream serverIn;
    private OutputStream serverOut;
    private BufferedReader bufferedReader;
    private List<Character> passwd = new ArrayList<>();
    private Stage choosingStage = new Stage();
    private Scene choosingScene;

    public HangmanClient(String serverName, int serverPort) {
        this.serverName = serverName;
        this.serverPort = serverPort;

        try {
            URL url = new File("src/HangmanClient/view/choosingScene.fxml").toURL();

            choosingScene = new Scene(FXMLLoader.load(url));
            choosingStage.setTitle("Choose a password");
            choosingStage.setScene(choosingScene);
            choosingStage.setResizable(false);
            choosingStage.initModality(Modality.APPLICATION_MODAL);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean connect() {
        try {
            this.socket = new Socket(serverName, serverPort);
            this.serverOut = socket.getOutputStream();
            this.serverIn = socket.getInputStream();
            this.bufferedReader = new BufferedReader(new InputStreamReader(serverIn));
            //System.out.println(bufferedReader.readLine());
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void startMessageReader() {
        new Thread(() -> {
            String line;
            try {
                while ((line = bufferedReader.readLine()) != null) {
                    System.out.println("<in>" + line + "<in>");
                    String[] tokens = line.split(" ", 2);
                    if (tokens.length == 2) {
                        String cmd = tokens[0];
                        if ("missed".equals(cmd)) {
                            Lookups.getInstance().updateImage();
                            Lookups.getInstance().getMissedLettersTextArea().appendText(tokens[1]);

                        } else if ("correctLetter".equals(cmd)) {
                            handleCorrectLetters(tokens[1]);
                        } else if ("passwd".equals(cmd)) {
                            setPassword(tokens[1]);
                            Lookups.getInstance().getGuessButton().setDisable(false);
                        } else if ("msg".equals(cmd)) {
                            handleMessageReceived(tokens[1]);
                        } else if ("draw".equals(cmd)) {
                            chooseNewPassword(tokens[1]);
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void chooseNewPassword(String token) {
        Lookups.getInstance().getChatTextArea().appendText(token + "\n");
        Platform.runLater(() -> choosingStage.showAndWait());
        choosingStage.setAlwaysOnTop(true);
        //Platform.runLater(()->ConnectController.getGameStage().hide());
    }

    private void handleMessageReceived(String token) {
        Lookups.getInstance().getChatTextArea().appendText(token + "\n");
    }

    private void setPassword(String token) {
        passwd.clear();
        String[] tokens = token.split(",",2);
        String category = tokens[0];
        String password = tokens[1];
        //System.out.println("Password: " + password);
        Platform.runLater(()->Lookups.getInstance().getCategoryLabel().setText(category));
        for (int i = 0; i < password.length(); i++) {
            if(password.charAt(i) == ' '){
                passwd.add(' ');
            }
            else {
                passwd.add('_');
            }
        }
        Platform.runLater(() -> Lookups.getInstance().getPasswordLabel().setText(characterListToString(passwd)));
    }

    private String characterListToString(List<Character> list) {
        String out = "";
        for (Character c : list) {
            out += c.toString();
            out += " ";
        }
        return out;
    }

    private void handleCorrectLetters(String token) throws IOException {
        String[] tokens = token.split(",");
        String letter = tokens[0];
        int passwordLength = Integer.parseInt(tokens[1]);

        for (int i = 0; i < passwordLength; i++) {
            for (int j = 2; j < tokens.length; j++) {
                if (Integer.parseInt(tokens[j]) == i) {
                    passwd.set(i, letter.charAt(0));
                }
            }
        }
        if (!passwd.contains('_')) {
            Platform.runLater(()->{
                showWonAlert();
                Lookups.getInstance().getPasswordLabel().setText("");
                try {
                    Lookups.getInstance().finishGame();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            Lookups.getInstance().getVoteStartButton().setDisable(false);
            Lookups.getInstance().getGuessButton().setDisable(true);
        }

        System.out.println(characterListToString(passwd));
        Platform.runLater(() -> Lookups.getInstance().getPasswordLabel().setText(characterListToString(passwd)));
    }

    public void sendPasswordToServer (String category, String password) throws IOException {
        String msg = "passwd " + category + "," + password;
        write(msg);
    }

    public void write(String msg) throws IOException {
        System.out.println("<out>" + msg + "<out>");
        msg += "\n";
        serverOut.write(msg.getBytes());
    }

    private void showWonAlert() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Congratulations");
        alert.setContentText("Game over");
        alert.setHeaderText("You won");
        alert.show();
    }

    public void closeSocket() throws IOException {
        socket.close();
    }

    public Stage getChoosingStage() {
        return choosingStage;
    }


}
