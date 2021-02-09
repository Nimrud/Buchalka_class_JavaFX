package m09_challenge;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.util.Callback;
import m09_challenge.datamodel.Contact;
import m09_challenge.datamodel.ContactData;

import java.io.IOException;
import java.util.Optional;

public class Controller {
    @FXML
    private TableView<Contact> contactTableView;
    @FXML
    private ContextMenu contextMenu;
    @FXML
    private BorderPane mainWindow;

    private ContactData data;

    public void initialize(){
        data = new ContactData();
        data.loadContacts();

        contactTableView.setItems(data.getContacts());
        contactTableView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        contactTableView.getSelectionModel().selectFirst();

        contextMenu = new ContextMenu();

        MenuItem deleteMenuItem = new MenuItem("Delete Contact");
        deleteMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                deleteContact(selectedContact());
            }
        });
        contextMenu.getItems().addAll(deleteMenuItem);

        MenuItem updateMenuItem = new MenuItem("Edit Contact");
        updateMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                // TODO
                editContact(null);
            }
        });
        contextMenu.getItems().addAll(updateMenuItem);


        contactTableView.setRowFactory(new Callback<TableView<Contact>, TableRow<Contact>>() {
            @Override
            public TableRow<Contact> call(TableView<Contact> contactTableView) {
                TableRow<Contact> cell = new TableRow<>();
                cell.emptyProperty().addListener(
                        (observable, wasEmpty, isNowEmpty) -> {
                            if (isNowEmpty){
                                cell.setContextMenu(null);
                            } else {
                                cell.setContextMenu(contextMenu);
                            }
                        }
                );
                return cell;
            }
        });
    }

    @FXML
    public void deleteContact(Contact contact){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete contact");
        alert.setHeaderText("Deleting contact: " + contact.toString());
        alert.setContentText("Are you sure? OK to delete, otherwise Cancel");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && (result.get() == ButtonType.OK)){
            data.deleteContact(contact);
            data.saveContacts();
        }
    }

    public void deleteContact(ActionEvent actionEvent) {
        if (selectedContact() != null){
            deleteContact(selectedContact());
        }
    }

    @FXML
    public void newContactDialog(){
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(mainWindow.getScene().getWindow());
        dialog.setTitle("Add a new Contact");

        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("newContact.fxml"));
        try{
            dialog.getDialogPane().setContent(fxmlLoader.load());
        } catch (IOException e){
            System.out.println("Failed to load new Dialog Window");
            e.printStackTrace();
            return;
        }

        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);

        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && (result.get() == ButtonType.OK)){
            NewContactController controller = fxmlLoader.getController();
            Contact newContact = controller.processNewContact();
            data.addContact(newContact);
            data.saveContacts();
        }
    }

    @FXML
    public void handleExit(){
        Platform.exit();
    }

    public void editContact(ActionEvent actionEvent) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(mainWindow.getScene().getWindow());
        dialog.setTitle("Edit an existing Contact");

        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("editContact.fxml"));
        try{
            dialog.getDialogPane().setContent(fxmlLoader.load());
        } catch (IOException e){
            System.out.println("Failed to load new Dialog Window");
            e.printStackTrace();
            return;
        }
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);

        EditContactController controller = fxmlLoader.getController();
        controller.processEditContact(selectedContact());

        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && (result.get() == ButtonType.OK)){
            controller.updateContact(selectedContact());
            data.saveContacts();
        }
    }

    public Contact selectedContact(){
        Contact selectedContact = contactTableView.getSelectionModel().getSelectedItem();
        return selectedContact;
    }
}
