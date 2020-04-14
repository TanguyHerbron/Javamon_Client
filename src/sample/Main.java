package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.net.URL;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        URL windowURL = Main.class.getClassLoader().getResource("sample/window.fxml");

        if(windowURL != null) {
            Parent root = FXMLLoader.load(windowURL);
            primaryStage.setTitle("Javamon");
            primaryStage.setScene(new Scene(root, 512, 512));
            primaryStage.getIcons().add(new Image("/icon.png"));
            primaryStage.show();
        } else {
            System.out.println("Could not load window ressource");
            System.exit(0);
        }
    }


    public static void main(String[] args) {
        launch(args);
    }
}
