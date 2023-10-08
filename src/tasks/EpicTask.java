package tasks;

import enums.Status;
import enums.Type;

import java.util.ArrayList;

public class EpicTask extends Task {

    protected ArrayList<Integer> subTaskIds = new ArrayList<Integer>();

    public EpicTask(String title, String description) {
        super(title, description);
        this.type = Type.EPIC;
        this.status = Status.NEW;
    }

    public ArrayList<Integer> getSubTaskIds() {
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
}
