package org.javaee7.websocket.googledocs.client;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import javax.websocket.ContainerProvider;
import javax.websocket.DeploymentException;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;

/**
 * @author Arun Gupta
 */
public class GoogleDocClient extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        final Session session = connectToServer();
        System.out.println("Connected to server: " + session.getId());
        stage.setTitle("Google Docs Emulator using WebSocket");
        TextArea textarea = new TextArea();
        textarea.textProperty().addListener(
                new ChangeListener<String>() {

                    @Override
                    public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                        System.out.println("New value: " + newValue);
                        for (Session peer : session.getOpenSessions()) {
                            System.out.println("Trying to send data...");
                            if (!peer.equals(session)) {
                                try {
                                    peer.getBasicRemote().sendText(newValue);
                                } catch (IOException ex) {
                                    Logger.getLogger(GoogleDocClient.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            }
                        }
                    }

                }
        );

        textarea.setPrefSize(500, 300);
        textarea.setWrapText(true);
        Scene scene = new Scene(textarea);
        stage.setScene(scene);
        stage.show();
    }

    private Session connectToServer() throws URISyntaxException, DeploymentException, IOException {
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        return container.connectToServer(MyClient.class, new URI("ws://localhost:8080/server/websocket"));
    }

}
