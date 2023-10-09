package manager;

import tasks.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager{

    private ArrayList<Task> history = new ArrayList<Task>();

    @Override
    public void add(Task task) {
        if (task != null) {
            if (history.size() > 10) {
                history.remove(0);
                history.add(task);
            } else {
                history.add(task);
            }
        }

    }

    @Override
    public List<Task> getHistory() {
        return history;
    }

}
