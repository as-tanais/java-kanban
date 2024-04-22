package tasks;

import enums.Status;
import enums.Type;

import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Task {

    protected String title;
    protected String description;
    protected int id;
    protected Status status;
    protected Type type;
    protected Instant startTime;
    protected Duration duration;

    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy;HH:mm").withZone(ZoneId.systemDefault());

    public Task(String title, String description) {
        this.title = title;
        this.description = description;
        this.status = Status.NEW;
        this.type = Type.TASK;
    }

    public Task(int id, String title, String description, Status status) {
        this.title = title;
        this.description = description;
        this.id = id;
        this.status = status;
        this.type = type.TASK;
    }

    public Task(String title, String description, Instant startTime, int dur) {
        this.title = title;
        this.description = description;
        this.type = type.TASK;
        this.status = Status.NEW;
        this.startTime = startTime;
        this.duration = Duration.ofMinutes(dur);
    }

    public Task(int id, String title, String description, Status status, Instant startTime, int dur) {
        this.title = title;
        this.description = description;
        this.id = id;
        this.status = status;
        this.type = type.TASK;
        this.startTime = startTime;
        this.duration = Duration.ofMinutes(dur);
    }

    public Type getType() {
        return type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Instant getEndTime() {
        return this.startTime.plus(this.duration);
    }

    public Instant getStartTime() {
        return startTime;
    }

    public void setStartTime(Instant startTime) {
        this.startTime = startTime;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public Duration getDuration() {
        return duration;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public String toString() {
        String result =
                "Task{" +
                        "id='" + id +
                        "', title='" + title +
                        "', description='" + description +
                        "', status='" + status;

        if (getStartTime() != null) {
            result = result +
                    "', startTime='" + startTime +
                    "', duration='" + duration +
                    "', endTime='" + getEndTime();
        }
        result = result + "'}";
        return result;
    }

    public String toStringInFile() {
        return String.format("%s,%s,%s,%s,%s", id, type, title, status, description);
    }

    public String toStringInFilePriority() {
        return String.format("%s,%s,%s,%s,%s,%s,%s", id, type, title, status, description, startTime, duration.toMinutes());
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, description, id, status, duration, startTime);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Task)) return false;

        Task task = (Task) obj;

        return id == task.id
                && Objects.equals(this.title, task.title)
                && Objects.equals(this.description, task.description)
                && Objects.equals(this.startTime, task.startTime);
    }
}