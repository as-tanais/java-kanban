package manager;

import enums.Status;
import tasks.EpicTask;
import tasks.Subtask;
import tasks.Task;

import java.util.ArrayList;

public interface TaskManager {
    public Task createTask(Task task);
    public Task getTaskById(int id);
    public void updateTask(Task task);
    public void deleteTaskById(int id);
    public ArrayList<Task> getTasks();
    public void deleteTasks();
    public EpicTask createEpicTask(EpicTask epicTask);
    public ArrayList<EpicTask> getEpicTasks();
    public EpicTask getEpicById(int id);
    public ArrayList<Subtask> getSubtasksByEpicId(int epicId);
    public void deleteEpicTasks();
    public void deleteEpicById(int id);
    public void updateEpic(EpicTask epicTask);
    private void statusUpdate(EpicTask epicTask) {
    }
    public Subtask createSubtask(Subtask subTask);
    public ArrayList<Subtask> getSubtasks();
    public Subtask getSubtaskById(int id);
    public void deleteSubtasks();
    public void deleteSubtaskById(int id);
    public void updateSubtask(Subtask subtask);
}
