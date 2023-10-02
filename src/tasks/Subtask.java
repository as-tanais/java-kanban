package tasks;

public class Subtask extends Task {

    protected int epicId;


    public Subtask(String title, String description, int epicId) {
        super(title, description);
        this.status = "NEW";
        this.type = "SUBTASK";
        this.epicId = epicId;
    }

    public Subtask(String title, String description, int epicId, String status) {
        super(title, description);

        this.type = "SUBTASK";
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
