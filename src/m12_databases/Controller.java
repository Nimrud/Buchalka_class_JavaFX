package m12_databases;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import m12_databases.model.Artist;
import m12_databases.model.DataSource;

public class Controller {
}

class GetAllArtistsTask extends Task {

    @Override
    public ObservableList<Artist> call() {
        return FXCollections.observableArrayList(
                DataSource.getInstance().queryArtist(DataSource.ORDER_BY_ASC)
        );
    }
}