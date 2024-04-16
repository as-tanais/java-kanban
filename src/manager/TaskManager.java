package manager;

import tasks.EpicTask;
import tasks.SubTask;
import tasks.Task;

import java.util.List;
import java.util.Map;

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

    Map<Integer, EpicTask> getEpics();

    List<SubTask> getSubtasksByEpicId(int epicId);

    void deleteEpicTasks();

    void deleteEpicById(int id);

    void updateEpic(EpicTask epicTask);

    void statusUpdate(EpicTask epicTask);

    SubTask createSubtask(SubTask subTask);

    List<SubTask> getSubtasks();

    SubTask getSubtaskById(int id);

    void deleteSubtasks();

    void deleteSubtaskById(int id);

    void updateSubtask(SubTask subtask);

    List<Task> getHistory();

    public List<Task> getPrioritizedTasks();

    //методы для проверки
    void printTask();

    void printEpicTask();

    void printSubTask();

}
