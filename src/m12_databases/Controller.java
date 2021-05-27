package m12_databases;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import m12_databases.model.Album;
import m12_databases.model.Artist;
import m12_databases.model.DataSource;

import java.io.IOException;
import java.util.Optional;

public class Controller {

    @FXML
    private TableView artistTable;

    @FXML
    private ProgressBar progressBar;

    @FXML
    private BorderPane mainWindow;

    @FXML
    public void listArtists() {
        Task<ObservableList<Artist>> task = new GetAllArtistsTask();
        // uruchamiamy zapytanie w innym wątku niż UI (tu: wykorzystując Task w GetAllArtistsTask)
        artistTable.itemsProperty().bind(task.valueProperty());
        progressBar.progressProperty().bind(task.progressProperty());

        progressBar.setVisible(true);

        task.setOnSucceeded(e -> progressBar.setVisible(false));
        task.setOnFailed(e -> progressBar.setVisible(false));

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

    @FXML
    public void updateArtistName() {
        // nowe okienko dialogowe - patrz pakiet: m09_challenge
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(mainWindow.getScene().getWindow());
        dialog.setTitle("Edit name of an artist");

        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("editArtist.fxml"));

        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());
        } catch (IOException e) {
            System.out.println("Failed to load a new dialog window: " + e.getMessage());
            return;
        }

        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

        EditArtistController controller = fxmlLoader.getController();

        final Artist artist = (Artist) artistTable.getSelectionModel().getSelectedItem();
        controller.prepareToEditName(artist);

        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && (result.get() == ButtonType.OK)) {
            controller.updateName(artist);   // od tego momentu nazwa artysty jest już zmieniona

            Task<Boolean> task = new Task<Boolean>() {
                @Override
                protected Boolean call() throws Exception {
                    return DataSource.getInstance().updateArtistName(artist.getId(), artist.getName());
                }
            };

            // poniższy kod wynika z buga w JavaFX, przez który aplikacja nie widzi zmian w Table Views i Observable Lists
            // kod wymusza aktualizację
            task.setOnSucceeded(e -> {
                if (task.valueProperty().get()) {   // wartość true, jeśli update w bazie danych się powiódł
                    artist.setName(artist.getName());        // aktualizujemy WYŚWIETLANĄ w UI wartość
                    artistTable.refresh();
                }
            });

            new Thread(task).start();
        }
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