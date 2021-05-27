package m12_databases.model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

// większość kodu pochodzi z repo Buchalka_class_Databases

public class DataSource {
    // w aplikacjach jest najczęściej wiele kontrolerów
    // aby nie było konfliktów, klasę DataSource uruchamia się jako Singletona,
    // czyli zawsze mamy tylko 1 instancję tej klasy
    // 1) dodajemy prywatny konstruktor w tej klasie - czyli inne klasy nie będą miały do niego dostępu
    //    i nie będą mogły tworzyć nowych instancji
    // 2) tworzymy zmienną (instance), która będzie "trzymać" tę pojedynczą instancję
    //    i do której będą się odwoływały inne klasy; musi ona być statyczna
    // 3) tworzymy metodę, poprzez którą inne klasy będą się komunikowały z tą zmienną
    //    getInstance()
    public static final String DB_NAME = "music.db";
    public static final String CONNECTION_STRING =
            "jdbc:sqlite:/home/krzysztof/workspace/Buchalka/JavaFX_basics/src/m12_databases/" + DB_NAME;

    public static final String TABLE_SONGS = "songs";
    public static final String COLUMN_SONGS_ID = "_id";
    public static final String COLUMN_SONGS_TRACK = "track";
    public static final String COLUMN_SONGS_TITLE = "title";
    public static final String COLUMN_SONGS_ALBUM = "album";
    public static final int INDEX_SONGS_ID = 1;    // Indeksy zaczynają się od 1 (!)
    public static final int INDEX_SONGS_TRACK = 2;
    public static final int INDEX_SONGS_TITLE = 3;
    public static final int INDEX_SONGS_ALBUM = 4;

    public static final String TABLE_ALBUMS = "albums";
    public static final String COLUMN_ALBUMS_ID = "_id";
    public static final String COLUMN_ALBUMS_NAME = "name";
    public static final String COLUMN_ALBUMS_ARTIST = "artist";
    public static final int INDEX_ALBUMS_ID = 1;
    public static final int INDEX_ALBUMS_NAME = 2;
    public static final int INDEX_ALBUMS_ARTIST = 3;

    public static final String TABLE_ARTISTS = "artists";
    public static final String COLUMN_ARTISTS_ID = "_id";
    public static final String COLUMN_ARTISTS_NAME = "name";
    public static final int INDEX_ARTISTS_ID = 1;
    public static final int INDEX_ARTISTS_NAME = 2;

    public static final int ORDER_BY_NONE = 1;
    public static final int ORDER_BY_ASC = 2;
    public static final int ORDER_BY_DESC = 3;

    public static final String QUERY_ALBUMS_BY_ARTIST_START =
            "SELECT * FROM " + TABLE_ALBUMS + " INNER JOIN " + TABLE_ARTISTS +
                    " ON " + TABLE_ALBUMS + "." + COLUMN_ALBUMS_ARTIST + " = " +
                    TABLE_ARTISTS + "." + COLUMN_ARTISTS_ID +
                    " WHERE " + TABLE_ARTISTS + "." + COLUMN_ARTISTS_NAME + " = \"";
    public static final String QUERY_ALBUMS_BY_ARTIST_SORT =
            " ORDER BY " + TABLE_ALBUMS + "." + COLUMN_ALBUMS_NAME + " COLLATE NOCASE ";

    public static final String QUERY_ARTISTS_BY_SONG_NAME_START =
            "SELECT " + TABLE_ARTISTS + "." + COLUMN_ARTISTS_NAME + ", " +
                    TABLE_ALBUMS + "." + COLUMN_ALBUMS_NAME + ", " +
                    TABLE_SONGS + "." + COLUMN_SONGS_TRACK +
                    " FROM " + TABLE_ARTISTS +
                    " INNER JOIN " + TABLE_ALBUMS + " ON " +
                    TABLE_ALBUMS + "." + COLUMN_ALBUMS_ARTIST + " = " + TABLE_ARTISTS + "." + COLUMN_ARTISTS_ID +
                    " INNER JOIN " + TABLE_SONGS + " ON " +
                    TABLE_SONGS + "." + COLUMN_SONGS_ALBUM + " = " + TABLE_ALBUMS + "." + COLUMN_ALBUMS_ID +
                    " WHERE " + TABLE_SONGS + "." + COLUMN_SONGS_TITLE + " LIKE UPPER(\"";
    public static final String QUERY_ARTISTS_BY_SONG_NAME_SORT =
            " ORDER BY " + TABLE_ARTISTS + "." + COLUMN_ARTISTS_NAME +
                    ", " + TABLE_ALBUMS + "." + COLUMN_ALBUMS_NAME + " COLLATE NOCASE ASC";

    public static final String TABLE_ARTISTS_SONGS_VIEW = "artists_list";
    public static final String CREATE_ARTISTS_FOR_SONG_VIEW =
            "CREATE VIEW IF NOT EXISTS " + TABLE_ARTISTS_SONGS_VIEW +
                    " AS SELECT " + TABLE_ARTISTS + "." + COLUMN_ARTISTS_NAME + ", " +
                    TABLE_ALBUMS + "." + COLUMN_ALBUMS_NAME + " AS album, " +
                    TABLE_SONGS + "." + COLUMN_SONGS_TRACK + ", " +
                    TABLE_SONGS + "." + COLUMN_SONGS_TITLE +
                    " FROM " + TABLE_SONGS +
                    " INNER JOIN " + TABLE_ALBUMS + " ON " +
                    TABLE_SONGS + "." + COLUMN_SONGS_ALBUM + " = " + TABLE_ALBUMS + "." + COLUMN_ALBUMS_ID +
                    " INNER JOIN " + TABLE_ARTISTS + " ON " +
                    TABLE_ALBUMS + "." + COLUMN_ALBUMS_ARTIST + " = " + TABLE_ARTISTS + "." + COLUMN_ARTISTS_ID;
    public static final String TABLE_ARTISTS_SONGS_VIEW_SORT =
            " ORDER BY " + TABLE_ARTISTS + "." + COLUMN_ARTISTS_NAME + ", " +
                    TABLE_ALBUMS + "." + COLUMN_ALBUMS_NAME + ", " +
                    TABLE_SONGS + "." + COLUMN_SONGS_TRACK;
    public static final String QUERY_VIEW_SONG_INFO = "SELECT " + COLUMN_ARTISTS_NAME + ", " +
            COLUMN_SONGS_ALBUM + ", " + COLUMN_SONGS_TRACK + " FROM " +
            TABLE_ARTISTS_SONGS_VIEW + " WHERE " + COLUMN_SONGS_TITLE + " = \"";

    // Unikamy SQL Injection przez użycie PreparedStatement
    // całe zapytanie jest prawie takie samo, ale zmienną zastępujemy ? (bez cudzysłowu)
    public static final String QUERY_VIEW_SONG_INFO_PREP = "SELECT " + COLUMN_ARTISTS_NAME + ", " +
            COLUMN_SONGS_ALBUM + ", " + COLUMN_SONGS_TRACK + " FROM " +
            TABLE_ARTISTS_SONGS_VIEW + " WHERE " + COLUMN_SONGS_TITLE + " = ?";
    // następnie deklarujemy dla niego zmienną:
    private PreparedStatement querySongView;
    // i tworzymy jej instancję np. w metodzie open() -> metoda prepareStatement() na połączeniu
    // na koniec w metodzie close() zamykamy zmienną

    public static final String INSERT_ARTIST = "INSERT INTO " + TABLE_ARTISTS +
            "(" + COLUMN_ARTISTS_NAME + ") VALUES(?)";
    public static final String INSERT_ALBUM = "INSERT INTO " + TABLE_ALBUMS +
            "(" + COLUMN_ALBUMS_NAME + ", " + COLUMN_ALBUMS_ARTIST + ") VALUES(?, ?)";
    public static final String INSERT_SONG = "INSERT INTO " + TABLE_SONGS +
            "(" + COLUMN_SONGS_TRACK + ", " + COLUMN_SONGS_TITLE + ", " +
            COLUMN_SONGS_ALBUM + ") VALUES(?, ?, ?)";

    public static final String QUERY_ARTIST = "SELECT " + COLUMN_ARTISTS_ID +
            " FROM " + TABLE_ARTISTS + " WHERE " + COLUMN_ARTISTS_NAME + " = ?";
    public static final String  QUERY_ALBUMS = "SELECT " + COLUMN_ALBUMS_ID +
            " FROM " + TABLE_ALBUMS + " WHERE " + COLUMN_ALBUMS_NAME + " = ?";
    public static final String QUERY_SONGS = "SELECT * FROM " +
            TABLE_ARTISTS_SONGS_VIEW + " WHERE " + COLUMN_SONGS_TITLE + " = ? AND album = ? AND " + COLUMN_ARTISTS_NAME + " = ?";

    public static final String QUERY_ALBUMS_BY_ARTIST_ID = "SELECT * FROM " +
            TABLE_ALBUMS + " WHERE " + COLUMN_ALBUMS_ARTIST + " = ? ORDER BY " +
            COLUMN_ALBUMS_NAME + " COLLATE NOCASE";

    public static final String UPDATE_ARTIST_NAME = "UPDATE " + TABLE_ARTISTS +
            " SET " + COLUMN_ARTISTS_NAME + " = ? WHERE " + COLUMN_ARTISTS_ID + " = ?";

    private PreparedStatement insertIntoArtists;
    private PreparedStatement insertIntoAlbums;
    private PreparedStatement insertIntoSongs;

    private PreparedStatement queryArtists;
    private PreparedStatement queryAlbums;
    private PreparedStatement querySongs;
    private PreparedStatement queryAlbumsByArtistId;

    private PreparedStatement updateArtistName;

    private  Connection conn;

    private static DataSource instance = new DataSource();

    private DataSource() {
    }

    public static DataSource getInstance() {
        return instance;
    }

    public boolean open() {
        try {
            conn = DriverManager.getConnection(CONNECTION_STRING);
            querySongView = conn.prepareStatement(QUERY_VIEW_SONG_INFO_PREP);
            insertIntoArtists = conn.prepareStatement(INSERT_ARTIST, Statement.RETURN_GENERATED_KEYS);
            insertIntoAlbums = conn.prepareStatement(INSERT_ALBUM, Statement.RETURN_GENERATED_KEYS);
            insertIntoSongs = conn.prepareStatement(INSERT_SONG);
            queryArtists = conn.prepareStatement(QUERY_ARTIST);
            queryAlbums = conn.prepareStatement(QUERY_ALBUMS);
            querySongs = conn.prepareStatement(QUERY_SONGS);
            queryAlbumsByArtistId = conn.prepareStatement(QUERY_ALBUMS_BY_ARTIST_ID);
            updateArtistName = conn.prepareStatement(UPDATE_ARTIST_NAME);
            return true;
        } catch (SQLException e) {
            System.out.println("Couldn't open database: " + e.getMessage());
            return false;
        }
    }

    public void close() {
        try {
            if (querySongView != null) {
                querySongView.close();
            }
            if (insertIntoArtists != null) {
                insertIntoArtists.close();
            }
            if (insertIntoAlbums != null) {
                insertIntoAlbums.close();
            }
            if (insertIntoSongs != null) {
                insertIntoSongs.close();
            }
            if (queryArtists != null) {
                queryArtists.close();
            }
            if (queryAlbums != null) {
                queryAlbums.close();
            }
            if (querySongs != null) {
                querySongs.close();
            }
            if (queryAlbumsByArtistId != null) {
                queryAlbumsByArtistId.close();
            }
            if (updateArtistName != null) {
                updateArtistName.close();
            }
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            System.out.println("Couldn't close the connection: " + e.getMessage());
        }
    }

    // Metoda bez wykorzystania try-with-resources:
    public List<Artist> queryArtist(int sortOrder){

        StringBuilder sb = new StringBuilder("SELECT * FROM ");
        sb.append(TABLE_ARTISTS);
        if (sortOrder != ORDER_BY_NONE) {
            sb.append(" ORDER BY ");
            sb.append(COLUMN_ARTISTS_NAME);
            sb.append(" COLLATE NOCASE");
            if (sortOrder == ORDER_BY_ASC) {
                sb.append(" ASC");
            } else {
                sb.append(" DESC");
            }
        }

        Statement statement = null;
        ResultSet rs = null;

        try {
            statement = conn.createStatement();
            rs = statement.executeQuery(sb.toString());

            List<Artist> artists = new ArrayList<>();
            while (rs.next()){
                try {
                    Thread.sleep(8);
                } catch (InterruptedException e) {
                    System.out.println("Interrupted: " + e.getMessage());
                }
                Artist artist = new Artist();
                artist.setId(rs.getInt(INDEX_ARTISTS_ID));    // wykorzystanie indeksów kolumn jest wydajniejsze
                //artist.setId(rs.getInt(COLUMN_ARTISTS_ID));
                artist.setName(rs.getString(INDEX_ARTISTS_NAME));
                artists.add(artist);
            }
            return artists;

        } catch (SQLException e){
            System.out.println("Couldn't get information: " + e.getMessage());
            return null;
        } finally {
            try {
                if (rs != null){
                    rs.close();
                }
            } catch (SQLException e) {
                System.out.println("Error while closing ResultSet: " + e.getMessage());;
            }
            try {
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException e){
                System.out.println("Couldn't close the statement: " + e.getMessage());
            }
        }
    }

    public List<Album> queryAlbumsByArtistId(int artistId) {
        try {
            queryAlbumsByArtistId.setInt(1, artistId);
            ResultSet rs = queryAlbumsByArtistId.executeQuery();

            List<Album> albums = new ArrayList<>();
            while (rs.next()) {
                Album album = new Album();
                album.setId(rs.getInt(COLUMN_ALBUMS_ID));
                album.setName(rs.getString(COLUMN_ALBUMS_NAME));
                album.setArtistId(artistId);
                albums.add(album);
            }
            return albums;
        } catch (SQLException e) {
            System.out.println("Query failed: " + e.getMessage());
            return null;
        }
    }

    public List<Album> queryAlbums(String artistName, int sortOrder) {

        StringBuilder sb = new StringBuilder(QUERY_ALBUMS_BY_ARTIST_START);
        sb.append(artistName);
        sb.append("\" ");
        if (sortOrder != ORDER_BY_NONE) {
            sb.append(QUERY_ALBUMS_BY_ARTIST_SORT);
            if (sortOrder == ORDER_BY_DESC) {
                sb.append("DESC");
            } else {
                sb.append("ASC");
            }
        }

        // PRO-TIP:
        // wydrukowanie całego zapytania SQL:
        System.out.println("SQL Statement: " + sb);

        // tym razem z użyciem try-with-resources:
        try (Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(sb.toString())) {

            List<Album> albums = new ArrayList<>();

            while (rs.next()) {
                Album album = new Album();
                album.setId(rs.getInt(COLUMN_ALBUMS_ID));
                album.setName(rs.getString(COLUMN_ALBUMS_NAME));
                albums.add(album);
            }
            return albums;

        } catch (SQLException e) {
            System.out.println("Error processing request: " + e.getMessage());
            return null;
        }
    }

    public List<SongArtist> performerOfTheSong(String songTitle){

        StringBuilder sb = new StringBuilder(QUERY_ARTISTS_BY_SONG_NAME_START);
        sb.append(songTitle);
        sb.append("\")");
        sb.append(QUERY_ARTISTS_BY_SONG_NAME_SORT);

        System.out.println("SQL Statement: " + sb);

        try(Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(sb.toString())) {

            return resultSetProcessing(rs);

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
            return null;
        }
    }

    // Wyszukiwanie wykonawców piosenki (jak wyżej), ale z wykorzystaniem Widoku:
    public List<SongArtist> performerOfTheSongView(String title) {

        // korzystamy z PreparedStatement (querySongView):
        try {
            querySongView.setString(1, title);
            // powyżej podajemy pozycję(kolejność) znaku zapytania z SQL (może być więcej niż jeden '?')
            ResultSet rs = querySongView.executeQuery();

            return resultSetProcessing(rs);

        } catch (SQLException e) {
            System.out.println("Query failed: " + e.getMessage());
            return null;
        }
    }

    public List<SongArtist> resultSetProcessing(ResultSet rs) throws SQLException {
        List<SongArtist> result = new ArrayList<>();
        while (rs.next()){
            SongArtist sa = new SongArtist();
            sa.setArtistName(rs.getString(1));
            sa.setAlbumName(rs.getString(2));
            sa.setTrackNumber(rs.getInt(3));
            result.add(sa);
        }
        return result;
    }

    // Pozyskiwanie wiadomości o tabelach z bazy danych za pomocą metadanych:
    public void querySongsMetaData() {
        String sql = "SELECT * FROM " + TABLE_SONGS;

        try (Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(sql)) {

            ResultSetMetaData meta = rs.getMetaData();   // pozyskuje informacje podobnie jak .schema
            int numColumns = meta.getColumnCount();
            for (int i = 1; i <= numColumns; i++) {       // pierwsza kolumna znajduje się pod pozycją 1, nie 0!
                System.out.format("Column %d in the Songs Table is named %s\n",
                        i, meta.getColumnName(i));
            }
        } catch (SQLException e) {
            System.out.println("Query failed: " + e.getMessage());
        }
    }

    // Pozyskanie informacji z funkcji, na przykładzie count()
    public int getCount(String table) {
        String sql = "SELECT COUNT(*) AS count, MIN(_id) AS min_id FROM " + table;

        try(Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(sql)) {

            int count = rs.getInt("count");   // zamiast indeksu kolumny stosujemy jej nazwę, nadaną w zapytaniu SQL
            int min = rs.getInt("min_id");
            System.out.format("Count: %d, Min_id: %d\n", count, min);
            return count;

        } catch (SQLException e) {
            System.out.println("Query failed: " + e.getMessage());
            return -1;
        }
    }

    // Tworzenie Widoku:
    public boolean createViewForSongArtists() {
        String sql = CREATE_ARTISTS_FOR_SONG_VIEW + TABLE_ARTISTS_SONGS_VIEW_SORT;
        System.out.println(sql);

        try(Statement statement = conn.createStatement()) {
            statement.execute(sql);
            return true;
        } catch (SQLException e) {
            System.out.println("Create View failed: " + e.getMessage());
            return false;
        }
    }

    // metoda prywatna, bo dostępna jedynie przez insertSong()
    private int insertArtist(String name) throws SQLException {
        queryArtists.setString(1, name);
        ResultSet rs = queryArtists.executeQuery();

        if (rs.next()) {        // jeśli artysta jest już w bazie, to ta metoda zwróci true
            return rs.getInt(1);   // a ta zwróci jego _id
        } else {
            // zapisujemy nowego artystę
            insertIntoArtists.setString(1, name);
            int rowsAffected = insertIntoArtists.executeUpdate();
            // nie używamy metody execute(), bo ona zwraca booleana

            // jeśli z jakiegoś względu zapis się nie uda:
            if (rowsAffected != 1) {
                throw new SQLException("Couldn't insert artist!");
            }

            // pozyskanie _id nowego artysty:
            ResultSet generatedKey = insertIntoArtists.getGeneratedKeys();
            if (generatedKey.next()) {
                return generatedKey.getInt(1);
            } else {
                throw new SQLException("Couldn't get _id for artist");
            }
        }
    }

    // metoda prywatna, bo dostępna jedynie przez insertSong()
    private int insertAlbum(String name, int artistId) throws SQLException {
        queryAlbums.setString(1, name);
        ResultSet rs = queryAlbums.executeQuery();

        if (rs.next()) {
            return rs.getInt(1);
        } else {
            insertIntoAlbums.setString(1, name);
            insertIntoAlbums.setInt(2, artistId);
            int rowsAffected = insertIntoAlbums.executeUpdate();

            if (rowsAffected != 1) {
                throw new SQLException("Couldn't insert album!");
            }

            ResultSet generatedKey = insertIntoArtists.getGeneratedKeys();
            if (generatedKey.next()) {
                return generatedKey.getInt(1);
            } else {
                throw new SQLException("Couldn't get _id for album");
            }
        }
    }

    public void insertSong(String title, String artist, String album, int track) throws SQLException {
        System.out.println("Song: " + title + ", artist: " + artist + ", album: " + album);

        // dodajemy parametry parametry funkcji do zapytania SQL (z PreparedStatement):
        querySongs.setString(1, title);
        querySongs.setString(2, album);
        querySongs.setString(3, artist);

        ResultSet rs = querySongs.executeQuery();

        // sprawdzamy, czy piosenki nie ma już w bazie:
        if(rs.next()) {
            System.out.println("This song is already in DB");
            return;
        } else {
            // jeśli piosenki nie ma w DB, to ją dodajemy, w transakcji:
            try {
                conn.setAutoCommit(false);    // rozpoczynamy transakcję przez wyłączenie auto-commit
                // transakcja obejmuje metody: insertSong(), insertAlbum(), insertArtist()
                // insertAlbum(), insertArtist() są dostępne tylko z niniejszej metody (modyfikator private)

                int artistId = insertArtist(artist);
                int albumId = insertAlbum(album, artistId);

                insertIntoSongs.setInt(1, track);
                insertIntoSongs.setString(2, title);
                insertIntoSongs.setInt(3, albumId);

                int rowsAffected = insertIntoSongs.executeUpdate();

                if (rowsAffected == 1) {
                    conn.commit();         // kończymy tu transakcję - przez zapis do BD
                    System.out.println("New song added: \"" + title + "\" of " + artist + " (album: " + album + ")");
                } else {
                    throw new SQLException("Song insert failed!");
                }

            } catch (Exception e) {
                System.out.println("Song insert exception: " + e.getMessage());
                try {
                    System.out.println("Performing rollback");
                    conn.rollback();         // rollback, jeśli zapis transakcji się nie powiedzie
                } catch (SQLException e2) {
                    System.out.println("Rollback failed: " + e2.getMessage());
                }
            } finally {
                try {
                    System.out.println("Resetting default commit behavior");
                    conn.setAutoCommit(true);
                } catch (SQLException e3) {
                    System.out.println("AutoCommit On failed: " + e3.getMessage());
                }
            }
        }
    }

    public boolean updateArtistName(int artistId, String newName) {
        try {
            updateArtistName.setString(1, newName);
            updateArtistName.setInt(2, artistId);

            int rowsAffected = updateArtistName.executeUpdate();

            return rowsAffected == 1;
        } catch (SQLException e) {
            System.out.println("Error updating artist name: " + e.getMessage());
            return false;
        }
    }
}
