package org.javaee7.websocket.googledocs.client;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import javax.websocket.ClientEndpoint;
import javax.websocket.ContainerProvider;
import javax.websocket.DeploymentException;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;

/**
 * @author Arun Gupta
 */
@ClientEndpoint
public class GoogleDocClient extends Application {

    static TextArea textarea;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        final Session session = connectToServer();
        System.out.println("Connected to server: " + session.getId());
        stage.setTitle("Google Docs Emulator using WebSocket");
        textarea = new TextArea();
        textarea.textProperty().addListener(
            new ChangeListener<String>() {

                @Override
                public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                    System.out.println("New value: " + newValue);

                    try {
                        session.getBasicRemote().sendText(newValue);
                    } catch (IOException ex) {
                        Logger.getLogger(GoogleDocClient.class.getName()).log(Level.SEVERE, null, ex);
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

    @OnOpen
    public void onOpen(Session session) {
        System.out.println("Connected to endpoint: " + session.getBasicRemote());
    }

    @OnMessage
    public void onMessage(String message, Session session) throws IOException {
        final String newMessage = message;
        System.out.println("Received message in client: " + message);
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                textarea.setText(newMessage);
                textarea.positionCaret(newMessage.length());
            }
        });

    }

    private Session connectToServer() throws URISyntaxException, DeploymentException, IOException {
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        return container.connectToServer(GoogleDocClient.class, new URI("ws://localhost:8080/server/websocket"));
    }

}
