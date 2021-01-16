package m06_application_singleton.datamodel;

import java.time.LocalDate;

public class TodoItem {
    private String shortDescription;
    private String details;
    private LocalDate deadline;

    public TodoItem(String shortDescription, String details, LocalDate deadline) {
        this.shortDescription = shortDescription;
        this.details = details;
        this.deadline = deadline;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public TodoItem setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
        return this;
    }

    public String getDetails() {
        return details;
    }

    public TodoItem setDetails(String details) {
        this.details = details;
        return this;
    }

    public LocalDate getDeadline() {
        return deadline;
    }

    public TodoItem setDeadline(LocalDate deadline) {
        this.deadline = deadline;
        return this;
    }

    @Override
    public String toString() {
        return shortDescription;
    }
}
