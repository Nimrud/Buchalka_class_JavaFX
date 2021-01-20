package m06_application_singleton;

import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import m06_application_singleton.datamodel.TodoData;
import m06_application_singleton.datamodel.TodoItem;

import java.time.LocalDate;

public class DialogController {
    @FXML
    private TextField shortDescriptionField;
    @FXML
    private TextArea detailsArea;
    @FXML
    private DatePicker deadlinePicker;

    public TodoItem processResults(){
        String shortDescription = shortDescriptionField.getText().trim();
        String details = detailsArea.getText().trim();
        LocalDate deadline = deadlinePicker.getValue();

        TodoItem newItemOnList = new TodoItem(shortDescription, details, deadline);
        TodoData.getInstance().addTodoItem(newItemOnList);
        return newItemOnList;
    }
}
