package tasks;

import enums.Status;
import enums.Type;

import java.time.Instant;

public class SubTask extends Task {

    protected int epicId;

    public SubTask(String title, String description, int epicId) {
        super(title, description);
        this.status = Status.NEW;
        this.type = Type.SUBTASK;
        this.epicId = epicId;
    }

    public SubTask(String title, String description, int epicId, Status status) {
        super(title, description);
        this.type = Type.SUBTASK;
        this.epicId = epicId;
        this.status = status;
    }

    public SubTask(int id, String title, String description, Status status, int epicId){
        super(id, title, description, status);
        this.epicId = epicId;
        this.type = Type.SUBTASK;
    }

    public SubTask(String title, String description, Instant startTime, int dur, int epicId) {
        super(title, description, startTime, dur);
        this.status = Status.NEW;
        this.type = Type.SUBTASK;
        this.epicId = epicId;
    }

    public SubTask(int id, String title, String description, Status status, Instant startTime, int dur, int epicId){
        super(id, title, description, status, startTime, dur);
        this.epicId = epicId;
        this.type = Type.SUBTASK;
    }

    public String toString() {
        String result = "SubTask{" +
                "id='" + id +
                "', title='" + title +
                "', description='" + description +
                "', status='" + status +
                "', startTime='" + FORMATTER.format(startTime) +
                "', duration='" + duration +
                "', endTime='" + FORMATTER.format(getEndTime()) +
                "', epic task ID " + epicId +
                "'}";
        return result;
    }

    public String toStringInFile () {
        return String.format("%s,%s,%s,%s,%s,%s", id, type,title, status, description, getEpicId());
    }

    public String toStringInFilePriority () {
        return String.format("%s,%s,%s,%s,%s,%s,%s,%s", id, type,title, status, description, getEpicId(),startTime,duration.toMinutes());
    }

    public int getEpicId() {
        return epicId;
    }

}
