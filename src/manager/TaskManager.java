package manager;

import enums.Status;
import tasks.EpicTask;
import tasks.Subtask;
import tasks.Task;

import java.util.ArrayList;
import java.util.List;

public interface TaskManager {
    Task createTask(Task task);
    Task getTaskById(int id);
    void updateTask(Task task);
    void deleteTaskById(int id);
    ArrayList<Task> getTasks();
    void deleteTasks();
    EpicTask createEpicTask(EpicTask epicTask);
    ArrayList<EpicTask> getEpicTasks();
    EpicTask getEpicById(int id);
    ArrayList<Subtask> getSubtasksByEpicId(int epicId);
    void deleteEpicTasks();
    void deleteEpicById(int id);
    void updateEpic(EpicTask epicTask);
    void statusUpdate(EpicTask epicTask);
    Subtask createSubtask(Subtask subTask);
    ArrayList<Subtask> getSubtasks();
    Subtask getSubtaskById(int id);
    void deleteSubtasks();
    void deleteSubtaskById(int id);
    void updateSubtask(Subtask subtask);
    List<Task> getHistory();
}
