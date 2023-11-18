package manager;

import enums.Status;
import tasks.EpicTask;
import tasks.Subtask;
import tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryTaskManager implements TaskManager{

    private int id = 0;
    private HashMap<Integer, Task> tasks = new HashMap<>();
    private HashMap<Integer, Subtask> subtasks = new HashMap<>();
    private HashMap<Integer, EpicTask> epics = new HashMap<>();
    private HistoryManager historyManager;

    public InMemoryTaskManager(HistoryManager historyManager) {
        this.historyManager = historyManager;
    }

    @Override
    public HashMap<Integer, EpicTask> getEpics() {
        return epics;
    }

    private int increaseId() {
        return id++;
    }

    @Override
    public Task createTask(Task task) {
        task.setId(increaseId());
        tasks.put(task.getId(), task);
        return task;
    }

    @Override
    public Task getTaskById(int id) {
        Task task = tasks.get(id);
        historyManager.add(task);
        return task;
    }

    @Override
    public void updateTask(Task task) {
        if (task != null && tasks.containsKey(task.getId())) {
            tasks.put(task.getId(), task);
        }
    }

    @Override
    public void deleteTaskById(int id) {
        historyManager.remove(id);
        tasks.remove(id);
    }

    @Override
    public ArrayList<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public void deleteTasks() {
        historyManager.remove(id);
        tasks.clear();
    }

    @Override
    public EpicTask createEpicTask(EpicTask epicTask) {
        epicTask.setId(increaseId());
        epics.put(epicTask.getId(), epicTask);
        return epicTask;
    }

    @Override
    public ArrayList<EpicTask> getEpicTasks() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public void deleteEpicTasks() {
        historyManager.remove(id);
        subtasks.clear();
        epics.clear();
    }

    @Override
    public EpicTask getEpicById(int id) {
        EpicTask epicTask = epics.get(id);
        historyManager.add(epicTask);
        return epicTask;
    }

    @Override
    public ArrayList<Subtask> getSubtasksByEpicId(int epicId) {
        ArrayList<Subtask> subtasksList = new ArrayList<>();
        if (epics.containsKey(epicId)) {
            EpicTask epicTask = epics.get(epicId);
            for (Integer subtaskId : epicTask.getSubTaskIds()) {
                subtasksList.add(subtasks.get(subtaskId));
            }
        }
        return subtasksList;
    }

    @Override
    public void deleteEpicById(int id) {
        if (epics.containsKey(id)) {
            EpicTask epicTask = epics.get(id);
            for (Integer subtaskId : epicTask.getSubTaskIds()) {
                historyManager.remove(id);
                subtasks.remove(subtaskId);
            }
            historyManager.remove(id);
            epics.remove(id);
        }
    }

    @Override
    public void updateEpic(EpicTask epicTask) {
        if (epicTask != null && epics.containsKey(epicTask.getId())){
            epics.put(epicTask.getId(), epicTask);
            statusUpdate(epicTask);
        }
    }

    public void statusUpdate(EpicTask epicTask) {

        boolean isNew = true;
        boolean isDone = true;

        if (epicTask.getSubTaskIds().isEmpty()) {
            epicTask.setStatus(Status.NEW);
            return;
        }

        for (Integer epicSubtask : epicTask.getSubTaskIds()) {
            Status status = subtasks.get(epicSubtask).getStatus();
            if (status != Status.NEW) {
                isNew = false;
            }
            if (status != Status.DONE) {
                isDone = false;
            }
        }

        if (isNew) {
            epicTask.setStatus(Status.NEW);
        } else if (isDone) {
            epicTask.setStatus(Status.DONE);
        } else {
            epicTask.setStatus(Status.IN_PROGRESS);
        }
    }

    @Override
    public Subtask createSubtask(Subtask subTask) {
        if (epics.containsKey(subTask.getEpicId())) {
            subTask.setId(increaseId());
            subtasks.put(subTask.getId(), subTask);
            EpicTask epicTask = epics.get(subTask.getEpicId());
            epicTask.addSubTaskIds(subTask.getId());
            statusUpdate(epicTask);
            return subTask;
        } else {
            return null;
        }
    }

    @Override
    public ArrayList<Subtask> getSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    @Override
    public Subtask getSubtaskById(int id) {
        Subtask subTask = subtasks.get(id);
        historyManager.add(subTask);
        return subTask;
    }

    public void deleteSubtasks() {
        for (EpicTask epicTask : epics.values()) {
            epicTask.getSubTaskIds().clear();
            statusUpdate(epicTask);
        }
        historyManager.remove(id);
        subtasks.clear();
    }

    @Override
    public void deleteSubtaskById(int id) {
        if (subtasks.containsKey(id)) {
            Subtask subtask = subtasks.remove(id);
            EpicTask epicTask = epics.get(subtask.getEpicId());
            historyManager.remove(id);
            subtasks.remove(id);
            epicTask.getSubTaskIds().remove(id);
            statusUpdate(epicTask);
        }
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        if (subtask != null && subtasks.containsKey(subtask.getId())) {
            EpicTask epicTask = getEpicById(subtask.getEpicId());
            subtasks.put(subtask.getId(), subtask);
            statusUpdate(epicTask);
        }
    }
    @Override
    public List<Task> getHistory(){
        return historyManager.getHistory();
    }

    //методы для проверки
    @Override
    public void printTask() {
        for (Task task : tasks.values()) {
            System.out.println(task);
        }
    }

    @Override
    public void printEpicTask() {
        for (EpicTask epicTask : epics.values()) {
            System.out.println(epicTask);
        }
    }

    @Override
    public void printSubTask() {
        for (Subtask subTask : subtasks.values()) {
            System.out.println(subTask);
        }
    }
}
