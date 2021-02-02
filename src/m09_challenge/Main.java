package m09_challenge;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("window.fxml"));
        primaryStage.setTitle("JavaFX challenge");
        primaryStage.setScene(new Scene(root, 1200, 800));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}

// CHALLENGE:
//
// For this challenge, you need to create a simple contacts application that has a single main window.
// The app will allow the user to add, edit, and delete a contact.
// You need to display the contacts using the TableView control.
// We didn't cover TableView in the lectures, but we learned almost everything we need to know to use one.
//
// Create a single Contacts menu that contains Add, Edit, and Delete items.
// You will want to be able to load and store the contacts, which isn't a JavaFX topic.
// I've provided a ContactData class that contains a quick and dirty way to load and store contacts using XML.
// You don't have to understand the code now. Don't change it.
// Also, it doesn't do any verification - the values must be provided for the code to work.
// If you leave a value out, the code will throw an exception when we try to load values.
// You'll have to add code to the ContactData class to complete the challenge.
// There are comments that indicate where your code should go.
//
// Hints:
// 1. In addition to the ContactData class, you'll need a Contact class to store individual contacts.
// For each contact, you'll store the first name, last name, phone number, and notes.
// 2. To get data binding to work with the TableView, you'll want to store the contact information as SimpleStringProperty fields.
//