package m06_application_singleton.datamodel;

import javafx.collections.FXCollections;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.List;

public class TodoData {
    // Singleton - tworzy tylko 1 instancję danej klasy
    private static TodoData instance = new TodoData();
    private static String filename = "TodoListItems.txt";

    private List<TodoItem> todoItems;
    private DateTimeFormatter formatter;

    // publiczna metoda do zwracania pojedynczej instancji tej klasy:
    public static TodoData getInstance(){
        return instance;
    }

    // prywatny konstruktor - aby upewnić się, że będzie można stworzyć tylko 1 instancję
    private TodoData(){
        formatter = DateTimeFormatter.ofPattern("d MMMM yyyy");
    }

    public List<TodoItem> getTodoItems() {
        return todoItems;
    }

//    public TodoData setTodoItems(List<TodoItem> todoItems) {
//        this.todoItems = todoItems;
//        return this;
//    }

    public void loadTodoItems() throws IOException{
        todoItems = FXCollections.observableArrayList();
        Path path = Paths.get(filename);
        BufferedReader br = Files.newBufferedReader(path);

        String input;

        try {
            while((input = br.readLine()) != null){
                String[] inputPieces = input.split("\t");

                String shortDescription = inputPieces[0];
                String details = inputPieces[1];
                String dateString = inputPieces[2];
                LocalDate deadline = LocalDate.parse(dateString, formatter);

                TodoItem todoItem = new TodoItem(shortDescription, details, deadline);
                todoItems.add(todoItem);
            }

        } finally {
            // test, czy mamy odpowiedni obiekt do zapisu przed zamknięciem:
            if (br != null){
                br.close();
            }
        }
    }

    public void saveTodoItems() throws IOException{
        Path path = Paths.get(filename);
        BufferedWriter bw = Files.newBufferedWriter(path);

        try {
            Iterator<TodoItem> iter = todoItems.iterator();
            while(iter.hasNext()){
                TodoItem item = iter.next();
                bw.write(String.format("%s\t%s\t%s",
                        item.getShortDescription(),
                        item.getDetails(),
                        item.getDeadline().format(formatter)));
                bw.newLine();
            }
        } finally {
            if (bw != null){
                bw.close();
            }
        }
    }

    public void addTodoItem(TodoItem item){
        todoItems.add(item);
    }
}
