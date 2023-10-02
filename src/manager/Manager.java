package manager;

import tasks.EpicTask;
import tasks.Subtask;
import tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;

public class Manager {

    private int id = 0;
    private HashMap<Integer, Task> tasks = new HashMap<>();
    private HashMap<Integer, Subtask> subtasks = new HashMap<>();
    private HashMap<Integer, EpicTask> epics = new HashMap<>();

    public HashMap<Integer, EpicTask> getEpics() {
        return epics;
    }

    private int increaseId() {
        return id++;
    }

    public Task createTask(Task task) {
        task.setId(increaseId());
        tasks.put(task.getId(), task);
        return task;
    }

    public Task getTaskById(int id) {
        return tasks.get(id);
    }

    public void updateTask(Task task) {
        if (!tasks.isEmpty()) {
            tasks.put(task.getId(), task);
        }

    }

    public void deleteTaskById(int id) {
        tasks.remove(id);
    }

    public ArrayList<Task> getTasks() {
        ArrayList<Task> tasksList = new ArrayList<>();
        if (!tasks.isEmpty()) {
            for (Task task : tasks.values()) {
                tasksList.add(task);
            }
        }
        return tasksList;
    }

    public void deleteTasks() {
        tasks.clear();
    }


    public EpicTask createEpicTask(EpicTask epicTask) {
        epicTask.setId(increaseId());
        epics.put(epicTask.getId(), epicTask);
        return epicTask;
    }

    public ArrayList<EpicTask> getEpicTasks() {
        ArrayList<EpicTask> tasksEpicList = new ArrayList<>();
        if (!epics.isEmpty()) {
            for (EpicTask epicTask : epics.values()) {
                tasksEpicList.add(epicTask);
            }
        }
        return tasksEpicList;
    }

    public void deleteEpicTasks() {
        subtasks.clear();
        epics.clear();
    }

    public EpicTask getEpicById(int id) {
        EpicTask epicTask = epics.get(id);
        return epicTask;
    }


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

    public void deleteEpicById(int id) {
        if (epics.containsKey(id)) {
            EpicTask epicTask = epics.get(id);
            for (Integer subtaskId : epicTask.getSubTaskIds()) {
                subtasks.remove(subtaskId);
            }
            epics.remove(id);
        }
    }


    public void updateEpic(EpicTask epicTask) {
        epics.put(epicTask.getId(), epicTask);
        statusUpdate(epicTask);
    }


    private void statusUpdate(EpicTask epicTask) {

        boolean isNew = true;
        boolean isDone = true;

        if (epicTask.getSubTaskIds().isEmpty()) {
            epicTask.setStatus("NEW");
            return;
        }

        for (Integer epicSubtask : epicTask.getSubTaskIds()) {
            String status = subtasks.get(epicSubtask).getStatus();
            if (!status.equals("NEW")) {
                isNew = false;
            }
            if (!status.equals("DONE")) {
                isDone = false;
            }
        }

        if (isNew) {
            epicTask.setStatus("NEW");
        } else if (isDone) {
            epicTask.setStatus("DONE");
        } else {
            epicTask.setStatus("IN_PROGRESS");
        }
    }


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

    public ArrayList<Subtask> getSubtasks() {
        ArrayList<Subtask> subtasksList = new ArrayList<>();
        if (!subtasks.isEmpty()) {
            for (Subtask subtask : subtasks.values()) {
                subtasksList.add(subtask);
            }
        }
        return subtasksList;
    }

    public Subtask getSubtaskById(int id) {
        return subtasks.get(id);
    }

    public void deleteSubtasks() {
        for (EpicTask epicTask : epics.values()) {
            epicTask.getSubTaskIds().clear();
            statusUpdate(epicTask);
        }
        subtasks.clear();
    }

    public void deleteSubtaskById(int id) {
        if (subtasks.containsKey(id)) {
            Subtask subtask = subtasks.remove(id);
            EpicTask epicTask = epics.get(subtask.getEpicId());
            epicTask.getSubTaskIds().remove(id);
            statusUpdate(epicTask);
        }
    }

    public void updateSubtask(Subtask subtask) {
        if (subtask != null && subtasks.containsKey(subtask.getId())) {
            EpicTask epicTask = getEpicById(subtask.getEpicId());

            subtasks.put(subtask.getId(), subtask);
            statusUpdate(epicTask);
        }
    }


    //методы для проверки
    public void printTask() {
        for (Task task : tasks.values()) {
            System.out.println(task);
        }
    }

    public void printEpicTask() {
        for (EpicTask epicTask : epics.values()) {
            System.out.println(epicTask);
        }
    }

    public void printSubTask() {
        for (Subtask subTask : subtasks.values()) {
            System.out.println(subTask);
        }
    }
}
