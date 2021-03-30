package m10_threads;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;

public class Controller {
    private Task<ObservableList<String>> task;

    public void initialize(){
        task = new Task<ObservableList<String>>() {
            @Override
            protected ObservableList<String> call() throws Exception {
                Thread.sleep(1000);  // <- symulacja pozyskania info z bazy danych
                return FXCollections.observableArrayList(
                        "Adam Kowalski",
                        "Anna Kowalska",
                        "Jan Poniedziałek",
                        "Maria Konieczna");
            }
        };
    }

}
