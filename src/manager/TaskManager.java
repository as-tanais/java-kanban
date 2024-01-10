package manager;

import tasks.EpicTask;
import tasks.Subtask;
import tasks.Task;

import java.util.HashMap;
import java.util.List;

public interface TaskManager {

    Task createTask(Task task);
    Task getTaskById(int id);
    void updateTask(Task task);
    void deleteTaskById(int id);
    List<Task> getTasks();
    void deleteTasks();
    EpicTask createEpicTask(EpicTask epicTask);
    List<EpicTask> getEpicTasks();
    EpicTask getEpicById(int id);

    void setId(Integer id);

    int getId();

    HashMap<Integer, EpicTask> getEpics();
    List<Subtask> getSubtasksByEpicId(int epicId);
    void deleteEpicTasks();
    void deleteEpicById(int id);
    void updateEpic(EpicTask epicTask);
    void statusUpdate(EpicTask epicTask);
    Subtask createSubtask(Subtask subTask);
    List<Subtask> getSubtasks();
    Subtask getSubtaskById(int id);
    void deleteSubtasks();
    void deleteSubtaskById(int id);
    void updateSubtask(Subtask subtask);
    List<Task> getHistory();

    //методы для проверки
    void printTask();
    void printEpicTask();
    void printSubTask();

}
