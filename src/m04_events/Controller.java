package m04_events;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class Controller {
    // nazwa pola poniżej musi być taka sama jak fx:id w pliku sample.fxml
    @FXML
    private TextField textField1;

    @FXML
    // adnotacja powyżej nie jest wymagana, ale jest dobrą praktyką (pokazuje, że jest to Event Handler)
    public void onButtonClick(){
        System.out.println("Wprowadzono tekst: \"" + textField1.getText() + "\"");
    }
}
