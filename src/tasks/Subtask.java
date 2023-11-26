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

    public Subtask(int id, String title, String description, Status status, int epicId){
        super(id, title, description, status);
        this.epicId = epicId;
        this.type = Type.SUBTASK;
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

    public String toStringInFile () {
        return String.format("%s,%s,%s,%s,%s,%s", id, type,title, status, description, getEpicId());
    }

    public int getEpicId() {
        return epicId;
    }

}
