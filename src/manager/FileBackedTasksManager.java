package manager;

import enums.Status;
import enums.Type;
import exception.ManagerSaveException;
import tasks.EpicTask;
import tasks.SubTask;
import tasks.Task;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class FileBackedTasksManager extends InMemoryTaskManager {
    private final File file;

    public FileBackedTasksManager(InMemoryHistoryManager historyManager, File file) {
        super(historyManager);
        this.file = file;
    }

    public static void main(String[] args) {
        InMemoryHistoryManager historyManager = Managers.getDefaultHistory();
        FileBackedTasksManager fileBackedTaskManagerFirst = new FileBackedTasksManager(historyManager, new File("src/res/backup.csv"));

        Task task1 = new Task("Task 1", "Task Description 1");
        fileBackedTaskManagerFirst.createTask(task1);
        Task task2 = new Task("Task 2", "Task Description 2");
        fileBackedTaskManagerFirst.createTask(task2);

        Task task3 = new Task("Task 3", "Task Description 3", Instant.now().plusSeconds(3600), 30);
        fileBackedTaskManagerFirst.createTask(task3);

        EpicTask epicTask1 = new EpicTask("EpicTask 1", "EpicTask Description 1");
        EpicTask epicTask2 = new EpicTask("EpicTask 2", "EpicTask Description 2");


        fileBackedTaskManagerFirst.createEpicTask(epicTask1);
        fileBackedTaskManagerFirst.createEpicTask(epicTask2);

        SubTask subtask1 = new SubTask("Sub 1", "Sub des 1", Instant.now().plusSeconds(300), 5, 4);

        fileBackedTaskManagerFirst.createSubtask(subtask1);



        fileBackedTaskManagerFirst.getTaskById(1);
        fileBackedTaskManagerFirst.getTaskById(2);
        System.out.println(fileBackedTaskManagerFirst.getHistory().size());

////        SubTask subtask2 = new SubTask("Sub 1", "Sub des 1", Instant.now().plusSeconds(600), 5, 4);
////
////        fileBackedTaskManagerFirst.createSubtask(subtask2);
//
//        // fileBackedTaskManagerFirst.getTaskById(1);
//
        FileBackedTasksManager fileBackedTasksManagerS = loadFromFile(new File("src/res/backup.csv"));
        System.out.println(fileBackedTasksManagerS.getHistory().size());
////        System.out.println(fileBackedTasksManagerS.getSubtasksByEpicId(4).size());
////        System.out.println(fileBackedTasksManagerS.subtasks.size());
////        System.out.println(fileBackedTasksManagerS.subtasks.get(6));
////        System.out.println(fileBackedTasksManagerS.epics.get(4));
////        System.out.println(fileBackedTasksManager.getSubtasks().size());
////        System.out.println(fileBackedTasksManager.getSubtaskById(6).getEpicId());
    }

    private void save() {
        try (Writer writer = new FileWriter(file)) {
            writer.write("id,type,name,status,description,start_time,duration,epic\n");

            List<Task> tasks = super.getTasks();
            for (Task task : tasks) {
                if (task.getStartTime() != null) {
                    writer.write(String.format("%s\n", task.toStringInFilePriority()));
                } else {
                    writer.write(String.format("%s\n", task.toStringInFile()));
                }
            }

            List<EpicTask> epicTasks = super.getEpicTasks();
            for (EpicTask epicTask : epicTasks) {
                if (epicTask.getStartTime() != null) {
                    writer.write(String.format("%s\n", epicTask.toStringInFilePriority()));
                } else {
                    writer.write(String.format("%s\n", epicTask.toStringInFile()));
                }
            }

            List<SubTask> subTasks = super.getSubtasks();
            for (SubTask subTask : subTasks) {
                if (subTask.getStartTime() != null) {
                    writer.write(String.format("%s\n", subTask.toStringInFilePriority()));
                } else {
                    writer.write(String.format("%s\n", subTask.toStringInFile()));
                }
            }

            writer.write("\n");

            writer.write(historyToString(this.getHistoryManager()));



        } catch (IOException e) {
            throw new ManagerSaveException("File not saved");
        }

    }

    private static Task fromString(String value) {
        int id;
        Type type;
        String title;
        Status status;
        String description;
        boolean isPriorityTask = false;
        Instant startTime = null;
        int duration = 0;

        String[] backupTaks = value.split(",");
        id = Integer.parseInt(backupTaks[0]);
        type = Type.valueOf(backupTaks[1]);
        title = String.valueOf(backupTaks[2]);
        status = Status.valueOf(backupTaks[3]);
        description = String.valueOf(backupTaks[4]);

        if (backupTaks.length > 6) {
            startTime = Instant.parse(String.valueOf(backupTaks[5]));
            duration = Integer.parseInt((String.valueOf(backupTaks[6])));
            isPriorityTask = true;
        }

        if (type == Type.TASK) {
            if (isPriorityTask) {
                return new Task(id, title, description, status, startTime, duration);
            } else {
                return new Task(id, title, description, status);
            }
        } else if (type == Type.SUBTASK) {
            int epicId = Integer.parseInt(backupTaks[backupTaks.length - 1]);
            if (isPriorityTask) {
                return new SubTask(id, title, description, status, startTime, duration, epicId);
            } else {
                return new SubTask(id, title, description, status, epicId);
            }
        } else {
            return new EpicTask(id, title, description, status);
        }
    }

    private static String historyToString(HistoryManager manager) {
        StringBuilder str = new StringBuilder();
        List<Task> history = manager.getHistory();
        boolean isFirstTask = true;
        for (Task task : history) {
            if (isFirstTask) {
                str.append(task.getId());
                isFirstTask = false;
                continue;
            }
            str.append(",");
            str.append(task.getId());
        }
        return str.toString();
    }

    private static List<Integer> historyFromString(String value) {
        List<Integer> listTasks = new ArrayList<>();

        for (String line : value.split(",")) {
            listTasks.add(Integer.parseInt(line));
        }
        return listTasks;
    }

    public static FileBackedTasksManager loadFromFile(File file) {
        InMemoryHistoryManager historyManager = Managers.getDefaultHistory();
        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager(historyManager, file);

        int backupId = 1;
        int newId = 0;

        try {
            Path path = file.toPath();
            String content = Files.readString(path);
            String[] lines = content.split("\n");
            for (int i = 1; i < lines.length - 2; i++) {
                Task task = fromString(lines[i]);
                backupId = task.getId();
                if (backupId > newId) {
                    newId = backupId;
                }

                switch (task.getType()) {
                    case TASK:
                        fileBackedTasksManager.tasks.put(backupId, task);
                        break;
                    case EPIC:
                        fileBackedTasksManager.epics.put(backupId, (EpicTask) task);
                        break;
                    case SUBTASK:
                        fileBackedTasksManager.subtasks.put(backupId, (SubTask) task);
                        fileBackedTasksManager.epics.get(((SubTask) task).getEpicId()).getSubTaskIds().add(backupId);
                        if (task.getStartTime() != null) {
                            fileBackedTasksManager.updateTimeEpic(fileBackedTasksManager.epics.get(((SubTask) task).getEpicId()));
                        }
                        break;
                }

            }
            String history = lines[lines.length - 1];
            if (!history.equals("")) {
                List<Integer> list = historyFromString(history);
                if (!list.isEmpty()) {
                    for (int id : list) {
                        historyManager.add(findTask(id, fileBackedTasksManager));
                    }
                }
            }


        } catch (IOException e) {
            throw new ManagerSaveException("File not loaded");
        }
        fileBackedTasksManager.setId(newId + 1);
        return fileBackedTasksManager;
    }

    private static Task findTask(int id, FileBackedTasksManager manager) {

        if (manager.tasks.containsKey(id)) {
            return manager.tasks.get(id);
        } else if (manager.epics.containsKey(id)) {
            return manager.epics.get(id);
        } else {
            return manager.subtasks.get(id);
        }

    }

    @Override
    public Task createTask(Task task) {
        super.createTask(task);
        save();
        return task;
    }

    @Override
    public EpicTask createEpicTask(EpicTask epicTask) {
        super.createEpicTask(epicTask);
        save();
        return epicTask;
    }

    @Override
    public SubTask createSubtask(SubTask subtask) {
        SubTask subTask = super.createSubtask(subtask);
        save();
        return subtask;
    }

    @Override
    public void deleteTasks() {
        super.deleteTasks();
        save();
    }

    @Override
    public void deleteSubtasks() {
        super.deleteSubtasks();
        save();
    }

    @Override
    public void deleteEpicTasks() {
        super.deleteEpicTasks();
        save();
    }

    @Override
    public void deleteTaskById(int id) {
        super.deleteTaskById(id);
        save();
    }

    @Override
    public void deleteSubtaskById(int id) {
        super.deleteSubtaskById(id);
        save();
    }

    @Override
    public void deleteEpicById(int id) {
        super.deleteEpicById(id);
        save();
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateEpic(EpicTask epicTask) {
        super.updateEpic(epicTask);
        save();
    }

    @Override
    public void updateSubtask(SubTask subtask) {
        super.updateSubtask(subtask);
        save();
    }

    @Override
    public Task getTaskById(int id) {
        Task task = super.getTaskById(id);
        save();
        return task;
    }

    @Override
    public EpicTask getEpicById(int id) {
        EpicTask epicTask = super.getEpicById(id);
        save();
        return epicTask;
    }

    @Override
    public SubTask getSubtaskById(int id) {
        SubTask subtask = super.getSubtaskById(id);
        save();
        return subtask;
    }
}
