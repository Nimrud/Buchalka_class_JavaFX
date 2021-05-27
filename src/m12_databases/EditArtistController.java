package m12_databases;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import m12_databases.model.Artist;

public class EditArtistController {

    @FXML
    private TextField name;

    public void processEditName(Artist artist) {
        name.setText(artist.getName());
    }

    public void updateName(Artist artist) {
        artist.setName(name.getText().trim());
    }
}
