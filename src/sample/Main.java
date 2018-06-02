package sample;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;


public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));

        primaryStage.setScene(new Scene(root, 600, 600));
        primaryStage.setTitle("AIKULKI");


        primaryStage.show();

        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(6), e->run()));
        timeline.setCycleCount(timeline.INDEFINITE);
        timeline.play();
    }

    private void run()
    {
//


    }

    public static void main(String[] args)
    {
        launch(args);
    }
}

