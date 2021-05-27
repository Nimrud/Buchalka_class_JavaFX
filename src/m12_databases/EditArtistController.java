package m12_databases;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import m12_databases.model.Artist;

public class EditArtistController {

    @FXML
    private TextField name;

    public void prepareToEditName(Artist artist) {
        name.setText(artist.getName());   // pobiera starą nazwę i wstawia do pola name w pliku fxml
    }

    public Artist updateName(Artist artist) {
        artist.setName(name.getText().trim());
        return artist;
    }
}
