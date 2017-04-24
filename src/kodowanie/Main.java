package kodowanie;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("codingView.fxml"));
        primaryStage.setTitle("Kodowanie 2 Projekt");
        primaryStage.getIcons().add(new Image(getClass().getResource("icon.png").toString())); // icon from icons8.com
        primaryStage.setScene(new Scene(root));
        primaryStage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }
}
