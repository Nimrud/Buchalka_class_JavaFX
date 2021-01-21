package m06_application_singleton;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import m06_application_singleton.datamodel.TodoData;
import m06_application_singleton.datamodel.TodoItem;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

public class Controller {
    private List<TodoItem> todoItems;
    @FXML
    private ListView<TodoItem> todoListView;
    @FXML
    private TextArea todoDescription;
    @FXML
    private Label todoDeadline;
    @FXML
    private BorderPane mainBorderPane;

    public void initialize(){
//        TodoItem item1 = new TodoItem("Dzień kobiet", "Kupić kwiaty mamie i siostrze",
//                LocalDate.of(2021, Month.MARCH, 8));
//        TodoItem item2 = new TodoItem("Imieniny Adama", "Kupić prezent Adamowi", LocalDate.of(2021, 12, 24));
//        TodoItem item3 = new TodoItem("Imieniny Ewy", "Kupić prezent Ewie", LocalDate.of(2021, 12, 24));
//        TodoItem item4 = new TodoItem("Stomatolog", "Przegląd uzębienia", LocalDate.of(2021, 5, 6));
//
//        todoItems = new ArrayList<TodoItem>();
//        todoItems.add(item1);
//        todoItems.add(item2);
//        todoItems.add(item3);
//        todoItems.add(item4);
//
//        TodoData.getInstance().setTodoItems(todoItems);

        // poniższy kod odpowiada za automatyczne wyświetlenie opisu pierwszego rekordu z listy
        todoListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<TodoItem>() {
            @Override
            public void changed(ObservableValue<? extends TodoItem> observableValue, TodoItem todoItem, TodoItem t1) {
                if (t1 != null){
                    TodoItem item = todoListView.getSelectionModel().getSelectedItem();
                    todoDescription.setText(item.getDetails());

                    DateTimeFormatter date = DateTimeFormatter.ofPattern("d MMMM yyyy");
                    todoDeadline.setText(date.format(item.getDeadline()));
                }
            }
        });

        todoListView.setItems(TodoData.getInstance().getTodoItems());
        todoListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        todoListView.getSelectionModel().selectFirst();
    }

    @FXML
    public void showNewItemDialog() {
        Dialog<ButtonType> dialog = new Dialog<>();
        // dobrą praktyką jest ustawianie rodzica (parent/owner) dla okna dialogowego, które jest z niego otwierane:
        // owner musi być typu "window"
        // trzeba wię przypisać mu ID i odwołać się do niego w kontrolerze za pomocą @FXML
        dialog.initOwner(mainBorderPane.getScene().getWindow());

        dialog.setTitle("Dodanie nowej notatki do listy");

        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("todoItemDialog.fxml"));

        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());
        } catch (IOException e) {
            System.out.println("Nie udało się załadować okienka dialogowego");
            e.printStackTrace();
            return;
        }

        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

        // poniższe polecenie wyświetla okienko, a następnie CZEKA na akcję użytkownika
        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            //System.out.println("wciśnięto OK");
            DialogController controller = fxmlLoader.getController();
            TodoItem newItemOnList = controller.processResults();
            // poniższy kod odświeża zawartość listy:
            //todoListView.getItems().setAll(TodoData.getInstance().getTodoItems());
            // nie jest już konieczny, po zmianie Listy na ObservableList i wykorzystanie Data Binding
            todoListView.getSelectionModel().select(newItemOnList);
        } else {
            //System.out.println("wciśnięto Anuluj");
        }
    }
}
