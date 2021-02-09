package m09_challenge;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import m09_challenge.datamodel.Contact;

public class EditContactController {
    @FXML
    private TextField newFirstName;
    @FXML
    private TextField newLastName;
    @FXML
    private TextField newPhoneNumber;
    @FXML
    private TextArea newNotes;
    @FXML
    private TextField oldFirstName;
    @FXML
    private TextField oldLastName;
    @FXML
    private TextField oldPhoneNumber;
    @FXML
    private TextArea oldNotes;


    public void processEditContact(Contact contact){
        oldFirstName.setText(contact.getFirstName());
        oldLastName.setText(contact.getLastName());
        oldPhoneNumber.setText(contact.getPhoneNumber());
        oldNotes.setText(contact.getNotes());
    }

    public void updateContact(Contact contact) {
        contact.setFirstName(newFirstName.getText().trim());
        contact.setLastName(newLastName.getText().trim());
        contact.setPhoneNumber(newPhoneNumber.getText().trim());
        contact.setNotes(newNotes.getText().trim());
    }
}
