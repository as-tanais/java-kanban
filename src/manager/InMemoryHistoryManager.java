package manager;

import tasks.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager{

    private final int HISTORY_LOG_SIZE = 10;

    private ArrayList<Task> history = new ArrayList<Task>();

    @Override
    public void add(Task task) {
        if (task != null) {
            if (history.size() > HISTORY_LOG_SIZE) {
                history.remove(0);
            }
        }history.add(task);

    }

    @Override
    public List<Task> getHistory() {
        return List.copyOf(history);
    }

}
