package tasks;

import enums.Status;
import enums.Type;

import java.util.ArrayList;
import java.util.List;

public class EpicTask extends Task {

    protected List<Integer> subTaskIds = new ArrayList<Integer>();

    public EpicTask(String title, String description) {
        super(title, description);
        this.type = Type.EPIC;
        this.status = Status.NEW;
    }

    public EpicTask(int id, String title, String description, Status status) {
        super(id, title, description, status);
        this.type = Type.EPIC;
    }

    public List<Integer> getSubTaskIds() {
        return subTaskIds;
    }

    public void addSubTaskIds(int subTaskId) {
        subTaskIds.add(subTaskId);
    }

    @Override
    public String toString() {
        String result = "tasks.EpicTask{" +
                "id='" + id +
                "', title='" + title +
                "', description='" + description +
                "', status='" + status +
                "', subTaskId.size='" + subTaskIds.size() + "'}";
        return result;
    }

    public String toStringInFile () {
        return String.format("%s,%s,%s,%s,%s", id,type,title, status, description);
    }
}
