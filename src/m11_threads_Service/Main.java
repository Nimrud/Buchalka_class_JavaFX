package m11_threads_Service;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        stage.setTitle("Threads in JavaFX");
        stage.setScene(new Scene(root, 500, 600));
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
