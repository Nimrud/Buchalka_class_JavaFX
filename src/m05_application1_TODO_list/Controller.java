package m05_application1_TODO_list;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextArea;
import m05_application1_TODO_list.datamodel.TodoItem;

import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Controller {
    private List<TodoItem> todoItems;
    @FXML
    private ListView<TodoItem> todoListView;
    @FXML
    private TextArea todoDescription;
    @FXML
    private Label todoDeadline;

    public void initialize(){
        TodoItem item1 = new TodoItem("Dzień kobiet", "Kupić kwiaty mamie i siostrze",
                LocalDate.of(2021, Month.MARCH, 8));
        TodoItem item2 = new TodoItem("Imieniny Adama", "Kupić prezent Adamowi", LocalDate.of(2021, 12, 24));
        TodoItem item3 = new TodoItem("Imieniny Ewy", "Kupić prezent Ewie", LocalDate.of(2021, 12, 24));
        TodoItem item4 = new TodoItem("Stomatolog", "Przegląd uzębienia", LocalDate.of(2021, 5, 6));

        todoItems = new ArrayList<TodoItem>();
        todoItems.add(item1);
        todoItems.add(item2);
        todoItems.add(item3);
        todoItems.add(item4);

        // poniższy kod odpowiada za automatyczne wyświetlenie opisu pierwszego rekordu z listy
        // ten kod robi to samo, co metoda handleClickListItem (na dole), ale za pomocą Listenera
        todoListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<TodoItem>() {
            @Override
            public void changed(ObservableValue<? extends TodoItem> observableValue, TodoItem todoItem, TodoItem t1) {
                if (t1 != null){
                    TodoItem item = todoListView.getSelectionModel().getSelectedItem();
                    todoDescription.setText(item.getDetails());

                    // formatowanie daty:
                    DateTimeFormatter date = DateTimeFormatter.ofPattern("d MMMM yyyy");
                    todoDeadline.setText(date.format(item.getDeadline()));
                }
            }
        });

        todoListView.getItems().setAll(todoItems);
        todoListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        // powyższe ogranicza możliwość wybrania tylko jednego rekordu z wyświetlanej listy
        todoListView.getSelectionModel().selectFirst();
        // powyższa metoda automatycznie zaznacza pierwszy rekord przy załadowaniu

    }

    // wyświetlanie zawartości przy pomocy onMouseClicked w fxml
    @FXML
    public void handleClickListItem(){
        TodoItem item = todoListView.getSelectionModel().getSelectedItem();
        //System.out.println("Wybrany rekord to " + item);
        todoDescription.setText(item.getDetails());
        todoDeadline.setText(item.getDeadline().toString());
    }
}
