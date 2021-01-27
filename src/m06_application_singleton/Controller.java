package m06_application_singleton;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.util.Callback;
import m06_application_singleton.datamodel.TodoData;
import m06_application_singleton.datamodel.TodoItem;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.Optional;
import java.util.function.Predicate;

public class Controller {
    @FXML
    private ListView<TodoItem> todoListView;
    @FXML
    private TextArea todoDescription;
    @FXML
    private Label todoDeadline;
    @FXML
    private BorderPane mainBorderPane;
    @FXML
    private ContextMenu listContextMenu;
    @FXML
    private ToggleButton filterToggleButton;

    private FilteredList<TodoItem> filteredList;

    public void initialize(){
        listContextMenu = new ContextMenu();
        MenuItem deleteMenuItem = new MenuItem("Skasuj notatkę");
        deleteMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                // wybieramy zaznaczony rekord:
                TodoItem item = todoListView.getSelectionModel().getSelectedItem();
                deleteItem(item);
            }
        });

        // dodajemy do menu kontekstowego stworzony wyżej obiekt deleteMenuItem
        listContextMenu.getItems().addAll(deleteMenuItem);

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

        // dodanie filtra do listy:
        filteredList = new FilteredList<TodoItem>(TodoData.getInstance().getTodoItems(),
                new Predicate<TodoItem>() {
                    @Override
                    // jeśli w poniższej metodzie zwracane jest true, to rekord przechodzi przez filtr (tu: jest wyświetlany)
                    public boolean test(TodoItem item) {
                        return true;
                    }
                });


        // dodanie opcji sortowania wyświetlanych rekordów pod kątem deadline'ów:
        SortedList<TodoItem> sortedList = new SortedList<TodoItem>(filteredList,
                new Comparator<TodoItem>() {
                    @Override
                    public int compare(TodoItem o1, TodoItem o2) {
                        // musi zwracać wartość ujemną, jeśli o1 jest mniejsze od o2
                        return o1.getDeadline().compareTo(o2.getDeadline());
                    }
                });

        //todoListView.setItems(TodoData.getInstance().getTodoItems());
        todoListView.setItems(sortedList);
        todoListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        todoListView.getSelectionModel().selectFirst();

        // dodanie Cell Factory, która automatycznie zmieni kolor notatki z dzisiejszą datą:
        todoListView.setCellFactory(new Callback<ListView<TodoItem>, ListCell<TodoItem>>() {
            @Override
            public ListCell<TodoItem> call(ListView<TodoItem> todoItemListView) {
                ListCell<TodoItem> cell = new ListCell<>() {

                    @Override
                    protected void updateItem(TodoItem item, boolean empty) {
                        super.updateItem(item, empty);   // zachowuje wygląd komórek jak w klasie Parent
                        if (empty){
                            setText(null);
                        } else {
                            setText(item.getShortDescription());
                            if (item.getDeadline().equals(LocalDate.now())){
                                setTextFill(Color.RED);
                            } else if (item.getDeadline().equals(LocalDate.now().plusDays(1))){
                                setTextFill(Color.ORANGE);
                            } else if (item.getDeadline().isBefore(LocalDate.now())){
                                setTextFill(Color.GRAY);
                            }
                        }
                    }
                };

                // dodanie menu kontekstowego do rekordów:
                cell.emptyProperty().addListener(
                        // lambda expression (anonymous method):
                        (obs, wasEmpty, isNowEmpty) -> {
                            if (isNowEmpty){
                                cell.setContextMenu(null);
                            } else {
                                cell.setContextMenu(listContextMenu);
                            }
                        }
                );

                return cell;
            }
        });
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

    public void deleteItem(TodoItem item){
        // dodanie potwierdzenia chęci skasowania rekordu:
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Skasowanie notatki");
        alert.setHeaderText("Usuwanie notatki: " + item.getShortDescription());
        alert.setContentText("Czy na pewno? \nWciśnij OK, aby skasować / Anuluj, aby pozostawić");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && (result.get() == ButtonType.OK)){
            TodoData.getInstance().deleteTodoItem(item);
        }
    }

    @FXML
    public void handleKeyPressed(KeyEvent keyEvent){
        TodoItem selectedItem = todoListView.getSelectionModel().getSelectedItem();
        if (selectedItem != null){
            if (keyEvent.getCode().equals(KeyCode.DELETE)){
                deleteItem(selectedItem);
            }
        }
    }

    @FXML
    public void handleFilterButton(){
        if (filterToggleButton.isSelected()){
            filteredList.setPredicate(new Predicate<TodoItem>() {
                @Override
                public boolean test(TodoItem item) {
                    return (item.getDeadline().equals(LocalDate.now()));
                }
            });
        } else {
            filteredList.setPredicate(new Predicate<TodoItem>() {
                @Override
                public boolean test(TodoItem item) {
                    // jeśli filterToggleButton jest odznaczony, to wyświetlamy wszystkie rekordy (return true)
                    return true;
                }
            });
        }
    }

    @FXML
    public void handleExit(){
        Platform.exit();
    }
}
