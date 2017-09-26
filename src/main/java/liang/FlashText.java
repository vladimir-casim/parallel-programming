package liang;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 * Created by Vladimir on 25.09.2017.
 */
public class FlashText extends Application {
    private String text = "";

    @Override
    public void start(Stage primaryStage) {
        StackPane pane = new StackPane();
        final Label lblText = new Label("Programming is fun");
        pane.getChildren().add(lblText);

        Thread thread = new Thread(() -> {
                try {
                    while (true) {
                        if (lblText.getText().trim().length() == 0)
                            text = "Welocme";
                        else
                            text = "";

                        Platform.runLater(() -> {
                                lblText.setText(text);
                        });

                        Thread.sleep(200);
                    }
                } catch (InterruptedException ex) {
                }
        });

        thread.start();

        Scene scene = new Scene(pane, 200, 50);
        primaryStage.setTitle("FlashText");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
