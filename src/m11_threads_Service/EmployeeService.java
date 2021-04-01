package m11_threads_Service;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

public class EmployeeService extends Service<ObservableList<String>> {

    @Override
    protected Task<ObservableList<String>> createTask() {
        return new Task<ObservableList<String>>() {
            @Override
            protected ObservableList<String> call() throws Exception {
                String[] names = {
                        "Adam Kowalski",
                        "Anna Kowalska",
                        "Jan Poniedziałek",
                        "Maria Konieczna",
                        "Marek Łysek"};

                ObservableList<String> employees = FXCollections.observableArrayList();

                for (int i = 0; i < names.length; i++){
                    employees.add(names[i]);
                    updateMessage("Dodano osobę (" + names[i] + ") do listy");
                    updateProgress(i + 1, names.length);
                    Thread.sleep(400);
                }

                return employees;
            }
        };
    }
}