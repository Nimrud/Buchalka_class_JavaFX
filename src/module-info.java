module JavaFX.basics {
    requires javafx.fxml;
    requires javafx.controls;
    requires oracleIcons;

    opens m01_sample;
    opens m02_layouts;
    opens m03_controls;
    opens m04_events;
}