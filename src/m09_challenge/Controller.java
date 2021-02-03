package m09_challenge;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableView;
import m09_challenge.datamodel.Contact;
import m09_challenge.datamodel.ContactData;

import java.util.Optional;

public class Controller {
    @FXML
    private TableView<Contact> contactTableView;

    // tymczasowo:
    private ObservableList<Contact> contacts;

    public void initialize(){

        Contact contact1 = new Contact("Jan", "Kowalski", "509654987", "brak notatek");
        contacts.add(contact1);

        contactTableView.setItems(contacts);
    }

    public void deleteItem(Contact contact){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete contact");
        alert.setHeaderText("Deleting contact: " + contact.toString());
        alert.setContentText("Are you sure? OK to delete, otherwise Cancel");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && (result.get() == ButtonType.OK)){
            ContactData.getInstance().deleteContact(contact);
        }
    }

    @FXML
    public void handleExit(){
        Platform.exit();
    }
}
