package m09_challenge;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import m09_challenge.datamodel.Contact;
import m09_challenge.datamodel.ContactData;

public class NewContactController {
    @FXML
    private TextField newFirstName;
    @FXML
    private TextField newLastName;
    @FXML
    private TextField newPhoneNumber;
    @FXML
    private TextArea newNotes;

    public Contact processNewContact(){
        String firstName = newFirstName.getText().trim();
        String lastName = newLastName.getText().trim();
        String phoneNumber = newPhoneNumber.getText().trim();
        String notes = newNotes.getText().trim();

        Contact newContact = new Contact(firstName, lastName, phoneNumber, notes);
        ContactData.getInstance().addContact(newContact);
        return newContact;
    }
}
