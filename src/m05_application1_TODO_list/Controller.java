package m05_application1_TODO_list;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextArea;
import m05_application1_TODO_list.datamodel.TodoItem;

import java.time.LocalDate;
import java.time.Month;
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

        todoListView.getItems().setAll(todoItems);
        todoListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        // powyższe ogranicza możliwość wybrania tylko jednego rekordu z wyświetlanej listy

    }

    @FXML
    public void handleClickListItem(){
        TodoItem item = todoListView.getSelectionModel().getSelectedItem();
        //System.out.println("Wybrany rekord to " + item);
        todoDescription.setText(item.getDetails());
        todoDeadline.setText(item.getDeadline().toString());
    }
}
