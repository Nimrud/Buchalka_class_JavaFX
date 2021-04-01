module JavaFX.basics {
    requires javafx.fxml;
    requires javafx.controls;
    requires oracleIcons;
    requires java.desktop;
    requires javafx.web;

    opens m01_sample;
    opens m02_layouts;
    opens m03_controls;
    opens m04_events;
    opens m05_application1_TODO_list;
    opens m06_application_singleton;
    opens m07_css_transformation_choosers;
    opens m08_sceneBuilder;
    opens m09_challenge;
    opens m09_challenge.datamodel;
    opens m10_threads;
    opens m11_threads_Service;
}