package m07_css;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));

        // zmiana motywu (wyglądu) całej aplikacji - wszystkich okienek:
        setUserAgentStylesheet(STYLESHEET_CASPIAN);
        // więcej motywów można znaleźć w internecie (np. Metro, AeroFX)

        primaryStage.setTitle("Hello CSS!");
        primaryStage.setScene(new Scene(root, 800, 275));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
