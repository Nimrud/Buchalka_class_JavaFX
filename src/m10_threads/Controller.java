package m10_threads;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;

public class Controller {
    private Task<ObservableList<String>> task;
    @FXML
    private ListView listView;

    public void initialize(){
        task = new Task<ObservableList<String>>() {
            @Override
            protected ObservableList<String> call() throws Exception {
                Thread.sleep(500);  // <- symulacja pozyskania info z bazy danych
                ObservableList<String> employees = FXCollections.observableArrayList(
                        "Adam Kowalski",
                        "Anna Kowalska",
                        "Jan Poniedziałek",
                        "Maria Konieczna",
                        "Marek Łysek");

                return employees;
            }
        };
        // wykonanie długotrwałego wątku "w tle" (kod na górze),
        // a osobno aktualizujemy wątek na poziomie UI:
        listView.itemsProperty().bind(task.valueProperty());
    }

    @FXML
    public void buttonPressed(){
        new Thread(task).start();
    }
}
