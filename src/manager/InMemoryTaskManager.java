package manager;

import enums.Status;
import exception.IntersectionException;
import tasks.EpicTask;
import tasks.SubTask;
import tasks.Task;

import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

public class InMemoryTaskManager implements TaskManager {

    protected int id;
    public HashMap<Integer, Task> tasks = new HashMap<>();
    public HashMap<Integer, SubTask> subtasks = new HashMap<>();
    public HashMap<Integer, EpicTask> epics = new HashMap<>();
    private InMemoryHistoryManager historyManager;
    protected Set<Task> prioritizedTasks = new TreeSet<>(Comparator.comparing(Task::getStartTime));

    public InMemoryTaskManager(InMemoryHistoryManager historyManager) {
        this.historyManager = historyManager;
    }



    public InMemoryHistoryManager getHistoryManager() {
        return historyManager;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public HashMap<Integer, EpicTask> getEpics() {
        return epics;
    }

    private int increaseId() {
        return ++id;
    }

    @Override
    public Task createTask(Task task) {
        task.setId(increaseId());
        tasks.put(task.getId(), task);
        if (isPriorityTask(task)) {
            addTaskToPrioritizedTasks(task);
        }
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
        for (int id : tasks.keySet()) {
            historyManager.remove(id);
        }
        tasks.clear();
    }

    @Override
    public EpicTask createEpicTask(EpicTask epicTask) {
        epicTask.setId(increaseId());
        epics.put(epicTask.getId(), epicTask);
        if (isPriorityTask(epicTask)) {
            addTaskToPrioritizedTasks(epicTask);
        }
        return epicTask;
    }

    @Override
    public ArrayList<EpicTask> getEpicTasks() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public void deleteEpicTasks() {
        for (int id : epics.keySet()) {
            historyManager.remove(id);
        }
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
    public ArrayList<SubTask> getSubtasksByEpicId(int epicId) {
        ArrayList<SubTask> subtasksList = new ArrayList<>();
        if (epics.containsKey(epicId)) {
            EpicTask epicTask = epics.get(epicId);
            for (Integer subtaskId : epicTask.getSubTaskIds()) {
                subtasksList.add(subtasks.get(subtaskId));
            }
        }
        return subtasksList;
//        subtasksList = epics.entrySet().stream()
//                .filter(e -> e.getKey().equals(epicId))
//                .map(Map.Entry::getValue)
//                .collect(Collectors.toList());
//        return subtasksList;
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
        if (epicTask != null && epics.containsKey(epicTask.getId())) {
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
    public SubTask createSubtask(SubTask subTask) {
        if (epics.containsKey(subTask.getEpicId())) {
            subTask.setId(increaseId());
            subtasks.put(subTask.getId(), subTask);
            EpicTask epicTask = epics.get(subTask.getEpicId());
            epicTask.addSubTaskIds(subTask.getId());
            statusUpdate(epicTask);
            if (isPriorityTask(subTask)) {
                updateTimeEpic(epicTask);
                addTaskToPrioritizedTasks(subTask);
            }
            return subTask;
        } else {
            return null;
        }
    }

    @Override
    public ArrayList<SubTask> getSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    @Override
    public SubTask getSubtaskById(int id) {
        SubTask subTask = subtasks.get(id);
        historyManager.add(subTask);
        return subTask;
    }

    public void deleteSubtasks() {
        for (EpicTask epicTask : epics.values()) {
            epicTask.getSubTaskIds().clear();
            updateTimeEpic(epicTask);
            statusUpdate(epicTask);
        }
        for (int id : subtasks.keySet()) {
            historyManager.remove(id);
        }
        subtasks.clear();
    }

    @Override
    public void deleteSubtaskById(int id) {
        if (subtasks.containsKey(id)) {
            SubTask subtask = subtasks.remove(id);
            EpicTask epicTask = epics.get(subtask.getEpicId());
            historyManager.remove(id);
            subtasks.remove(id);
            epicTask.getSubTaskIds().remove(id);
            updateTimeEpic(epicTask);
            statusUpdate(epicTask);
        }
    }

    @Override
    public void updateSubtask(SubTask subtask) {
        if (subtask != null && subtasks.containsKey(subtask.getId())) {
            EpicTask epicTask = getEpicById(subtask.getEpicId());
            subtasks.put(subtask.getId(), subtask);
            updateTimeEpic(epicTask);
            statusUpdate(epicTask);
        }
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    public void updateTimeEpic(EpicTask epicTask){
        List<SubTask> subTasks= getSubtasksByEpicId(epicTask.getId());
        Instant startTime = subTasks.get(0).getStartTime();
        Instant endTime = subTasks.get(0).getEndTime();

        for (SubTask subTask : subTasks) {
            if (subTask.getStartTime().isBefore(startTime)) {
                startTime = subTask.getStartTime();
            }
            if (subTask.getEndTime().isAfter(endTime)){
                endTime = subTask.getEndTime();
            }
            epicTask.setStartTime(startTime);
            epicTask.setEndTime(endTime);
            epicTask.setDuration(Duration.ofMillis(endTime.toEpochMilli() - startTime.toEpochMilli()));
        }
    }

    public boolean isPriorityTask(Task task){
        return task.getStartTime() != null;
    }

    //методы по добавлению, обработки и проверки Приотиризации задач
    public void addTaskToPrioritizedTasks (Task task){
        if ((task.getStartTime() != null) && isValidatePriorityTasks(task)) {
            prioritizedTasks.add(task);

        } else if (!isValidatePriorityTasks(task)){
            throw new IntersectionException("Задача " + task.getTitle() + " пересекатеся с уже сущесвтующей по времени.");
        }
    }

    public List<Task> getPrioritizedTasks(){
        return prioritizedTasks.stream().toList();
    }

    private boolean isValidatePriorityTasks(Task task){
        List<Task> tasks = new ArrayList<>(getPrioritizedTasks());
        boolean isValid = false;
        if (!tasks.isEmpty()) {
            for (Task listTask : tasks) {
                if (task.getStartTime().isBefore(listTask.getStartTime())
                        && task.getEndTime().isBefore(listTask.getStartTime())
                        || task.getStartTime().isAfter(listTask.getEndTime())
                        && task.getEndTime().isAfter(listTask.getEndTime()))
                {
                    isValid = true;
                } else {
                    isValid = false;
                }
            }
        } else {
            isValid = true;
        }
        return isValid;
    }

    //getPrioritizedTasks
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
        for (SubTask subTask : subtasks.values()) {
            System.out.println(subTask);
        }
    }
}
