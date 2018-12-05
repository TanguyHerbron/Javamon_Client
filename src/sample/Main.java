package sample;

import fr.ensim.lemeeherbron.Terrain;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("J'EN AI MARRE");
        primaryStage.setScene(new Scene(root, 512, 512));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
