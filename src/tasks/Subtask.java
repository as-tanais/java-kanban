package tasks;

import enums.Status;
import enums.Type;

public class Subtask extends Task {

    protected int epicId;


    public Subtask(String title, String description, int epicId) {
        super(title, description);
        this.status = Status.NEW;
        this.type = Type.SUBTASK;
        this.epicId = epicId;
    }

    public Subtask(String title, String description, int epicId, Status status) {
        super(title, description);

        this.type = Type.SUBTASK;
        this.epicId = epicId;

        this.status = status;
    }

    public String toString() {
        String result = "SubTask{" +
                "id='" + id +
                "', title='" + title +
                "', description='" + description +
                "', status='" + status +
                "', epic task ID " + epicId + "'}";
        return result;
    }

    public int getEpicId() {
        return epicId;
    }
}
