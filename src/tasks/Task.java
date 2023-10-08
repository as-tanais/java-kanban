package tasks;

import enums.Status;
import enums.Type;

public class Task {

    protected String title;
    protected String description;
    protected int id;
    protected Status status;
    protected Type type;

    public Task(String title, String description) {
        this.title = title;
        this.description = description;
        this.status = Status.NEW;
        this.type = Type.TASK;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public String toString() {
        String result = "tasks.Task{" +
                "id='" + id +
                "', title='" + title +
                "', description='" + description +
                "', status='" + status + "'}";
        return result;
    }
}