package m01_sample;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        //Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        // Manipulowanie zawartością wyskakującego okienka bezpośrednio,
        // zamiast w pliku .fxml
        GridPane root = new GridPane();
        root.setAlignment(Pos.CENTER);
        root.setVgap(15);
        root.setHgap(15);

        Label greeting = new Label("Welcome to JavaFX!");
        greeting.setTextFill(Color.BLUE);
        greeting.setFont(Font.font("Times New Roman", FontWeight.BOLD, 20));
        root.getChildren().add(greeting);

        primaryStage.setTitle("Hello JavaFX");
        primaryStage.setScene(new Scene(root, 300, 175));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
