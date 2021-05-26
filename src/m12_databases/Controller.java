package m12_databases;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import m12_databases.model.Album;
import m12_databases.model.Artist;
import m12_databases.model.DataSource;

public class Controller {

    @FXML
    private TableView artistTable;

    public void listArtists() {
        Task<ObservableList<Artist>> task = new GetAllArtistsTask();
        artistTable.itemsProperty().bind(task.valueProperty());

        new Thread(task).start();
    }

    @FXML
    public void listAlbumsByArtist() {
        final Artist artist = (Artist) artistTable.getSelectionModel().getSelectedItem();
            if (artist == null) {
                System.out.println("No artist selected");
                return;
            }
            Task<ObservableList<Album>> task = new Task<ObservableList<Album>>() {
                @Override
                protected ObservableList<Album> call() throws Exception {
                    return FXCollections.observableArrayList(
                            DataSource.getInstance().queryAlbumsByArtistId(artist.getId())
                    );
                }
            };
            artistTable.itemsProperty().bind(task.valueProperty());

            new Thread(task).start();
    }
}

class GetAllArtistsTask extends Task {

    @Override
    public ObservableList<Artist> call() {
        return FXCollections.observableArrayList(
                DataSource.getInstance().queryArtist(DataSource.ORDER_BY_ASC)
        );
    }
}