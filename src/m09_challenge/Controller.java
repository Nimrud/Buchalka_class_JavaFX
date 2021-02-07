package m09_challenge;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import m09_challenge.datamodel.Contact;
import m09_challenge.datamodel.ContactData;

import java.io.IOException;
import java.util.Optional;

public class Controller {
    @FXML
    private TableView<Contact> contactTableView;

    public void initialize(){
        contactTableView.setItems(ContactData.getInstance().getContacts());
        contactTableView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    }

    public void deleteContact(Contact contact){
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
    public void newContactDialog(){
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(contactTableView.getScene().getWindow());
        dialog.setTitle("Add a new contact");

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("newContact.fxml"));
        try{
            dialog.getDialogPane().setContent(loader.load());
        } catch (IOException e){
            System.out.println("Failed to load new Dialog Window");
            e.printStackTrace();
            return;
        }

        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);

        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && (result.get() == ButtonType.OK)){
            NewContactController controller = loader.getController();
            Contact newContact = controller.processNewContact();
            contactTableView.getSelectionModel().select(newContact);
        }
    }

    @FXML
    public void handleExit(){
        Platform.exit();
    }
}
