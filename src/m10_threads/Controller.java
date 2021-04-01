package m10_threads;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;

public class Controller {
    private Task<ObservableList<String>> task;
    @FXML
    private ListView listView;
    @FXML
    private ProgressBar progressBar;
    @FXML
    private Label progressLabel;

    public void initialize(){
        task = new Task<ObservableList<String>>() {
            @Override
            protected ObservableList<String> call() throws Exception {
                String[] names = {
                        "Adam Kowalski",
                        "Anna Kowalska",
                        "Jan Poniedziałek",
                        "Maria Konieczna",
                        "Marek Łysek"};

                progressBar.setVisible(true);

                ObservableList<String> employees = FXCollections.observableArrayList();

                for (int i = 0; i < names.length; i++){
                    employees.add(names[i]);
                    updateMessage("Dodano osobę (" + names[i] + ") do listy");
                    // powyższe posłuży do aktualizacji napisu (Label)
                    updateProgress(i + 1, names.length);
                    // aktualizujemy metodę updateProgress() po dodaniu każdego imienia
                    // ta metoda ma 2 parametry: bieżący stan postępu oraz maksymalny postęp
                    Thread.sleep(400);
                }

                return employees;
            }
        };
        progressBar.setVisible(false);  // <- domyślne schowanie paska (przed wykonaniem operacji)

        // wykonanie długotrwałego wątku "w tle" (kod na górze),
        // a osobno aktualizujemy wątek na poziomie UI:
        listView.itemsProperty().bind(task.valueProperty());

        // powiązanie obiektu progressBar z funkcjonalnością:
        progressBar.progressProperty().bind(task.progressProperty());

        progressLabel.textProperty().bind(task.messageProperty());
    }

    @FXML
    public void buttonPressed(){
        new Thread(task).start();
    }
}
