package m04_events;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class Controller {
    // nazwa pola poniżej musi być taka sama jak fx:id w pliku sample.fxml
    @FXML
    private TextField textField1;
    @FXML
    private Button button1;
    @FXML
    private Button button2;
    @FXML
    private CheckBox checkBox;
    @FXML
    private Label outputLabel;


    @FXML
    // adnotacja powyżej nie jest wymagana, ale jest dobrą praktyką (pokazuje, że jest to Event Handler)
    public void onButtonClick(ActionEvent e){
        //System.out.println("Wprowadzono tekst: \"" + textField1.getText() + "\"");
        //System.out.println("Wciśnięto przycisk nr " + e.getSource());
        if (e.getSource().equals(button1)){
            System.out.println("Wprowadzono tekst: \"" + textField1.getText() + "\", aktywując przycisk #" + button1.getId());
        } else if (e.getSource().equals(button2)){
            System.out.println("Wprowadzono tekst: \"" + textField1.getText() + "\", aktywując przycisk #" + button2.getId());
        } else {
            System.out.println("Błąd! Sformatuj dysk C, aby kontynuować");
        }

        // poniżej stworzenie nowego procesu, który zajmie się obsługą wprowadzonych danych
        // dzięki temu dzieje się to "w tle" i nie spowalnia UI
        Runnable task = new Runnable() {
            @Override
            public void run() {
                try{
                    Thread.sleep(4000);    // symulacja długotrwałego procesu

                    // po 4 sekundach wykona się poniższy kod
                    // w międzyczasie użytkownik może dalej pracować z UI (nie ma zwiechy)
                    Platform.runLater(new Runnable() {
                        // Platform.runLater() jest konieczny, ponieważ JavaFX nie obsługuje wielu równoczesnych wątków
                        @Override
                        public void run() {
                            outputLabel.setText("Proces zakończony");
                        }
                    });

                } catch(InterruptedException event) {
                    // na razie nie ma potrzeby tu nic umieszczać
                }
            }
        };     // średnik jest potrzebny na końcu Runnable
        // poniżej jeszcze trzeba zarządzić, żeby Runnable się wykonał
        new Thread(task).start();

        if (checkBox.isSelected()){
            textField1.clear();
            button1.setDisable(true);
            button2.setDisable(true);
        }
    }

    @FXML
    //funkcja wyłączająca przyciski, jeśli użytkownik nie wprowadził żadnego tekstu
    public void handleKeyReleased(){
        String text = textField1.getText();
        boolean disableButtons = text.isEmpty() || text.trim().isEmpty();
        button1.setDisable(disableButtons);
        button2.setDisable(disableButtons);
    }

    @FXML
    //wyłączenie przycisków przy starcie aplikacji, gdy jeszcze użytkownik nie wprowadził żadnej wartości
    public void initialize(){
        button1.setDisable(true);
        button2.setDisable(true);
    }

    @FXML
    public void handleChange(){
        System.out.println("Checkbox " + (checkBox.isSelected() ? "jest" : "nie jest") + " zaznaczony");
    }
}
