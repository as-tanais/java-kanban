import java.util.ArrayList;
import java.util.HashMap;
public class Manager {

    int id;
    HashMap<Integer, Task> tasks = new HashMap<>();
    protected HashMap<Integer, Subtask> subtasks = new HashMap<>();
    protected HashMap<Integer, EpicTask> epics = new HashMap<>();

    public Manager() {
        id = 0;
    }
    private int increaseId(){
        return id++;
    }

    public Task createTask (Task task) {
        task.setId(increaseId());
        tasks.put(task.getId(), task);
        return task;
    }
    public Task getTaskById (int id) {
        return tasks.get(id);
    }
    public void updateTask (Task task) {
        tasks.put(task.getId(), task);
    }
    public void deleteTaskById (int id) {
        tasks.remove(id);
    }
    public HashMap<Integer, Task> getTasks() {
        if (!tasks.isEmpty()) {
            return tasks;
        } else {
            return new HashMap<>();
        }
    }
    public void deleteTasks() {
        tasks.clear();
    }



    public EpicTask createEpicTask (EpicTask epicTask) {
        epicTask.setId(increaseId());
        epics.put(epicTask.getId(), epicTask);
        return epicTask;
    }

    public HashMap<Integer, EpicTask> getEpicTasks() {
        if (!epics.isEmpty()) {
            return epics;
        } else {
            return new HashMap<>();
        }
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
        if (subtasks.containsKey(epicId)) {
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






    public Subtask createSubtask (Subtask subTask) {
        subTask.setId(increaseId());
        subtasks.put(subTask.getId(), subTask);
        EpicTask epicTask = epics.get(subTask.getEpicId());
        epicTask.setId(subTask.getEpicId());
        epicTask.setSubTaskIds(subTask.getId());
        statusUpdate(epicTask);
        return subTask;
    }

    public HashMap <Integer, Subtask> getSubtasks() {
        if (!subtasks.isEmpty()) {
            return subtasks;
        } else {
            return new HashMap<>();
        }
    }

    public Subtask getSubtaskById(int id) {
        if (subtasks.isEmpty()) {
            return null;
        } else{
            return subtasks.get(id);
        }
    }

    public void deleteSubtasks() {
        for (EpicTask epicTask : epics.values()) {
            for (Integer subtaskId : epicTask.getSubTaskIds()) {
                subtasks.remove(subtaskId);
                Subtask subtask = subtasks.get(subtaskId);
            }
            epicTask.getSubTaskIds().clear();
        }
    }

    public void deleteSubtaskById(int id) {
        if (subtasks.containsKey(id)) {
            Subtask subtask = subtasks.remove(id);
            EpicTask epicTask = epics.get(subtask.getEpicId());
            subtasks.remove(id);
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
    void printTask(){
        for (Task task : tasks.values()){
            System.out.println(task);
        }
    }

    void printEpicTask(){
        for (EpicTask epicTask : epics.values()){
            System.out.println(epicTask);
        }
    }

    void printSubTask(){
        for (Subtask subTask : subtasks.values()){
            System.out.println(subTask);
        }
    }

}
