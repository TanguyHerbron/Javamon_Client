package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import sample.Controllers.StartScreenController;

public class MainNetwork extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Views/StartScreen.fxml"));
        Parent root = fxmlLoader.load();
        primaryStage.setTitle("Javamon - Client");
        primaryStage.setScene(new Scene(root, 512, 512));
        StartScreenController startScreenController = fxmlLoader.<StartScreenController>getController();
        primaryStage.getIcons().add(new Image("/icon.png"));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
