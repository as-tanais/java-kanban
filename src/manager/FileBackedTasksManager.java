package manager;

import enums.Status;
import enums.Type;
import exception.ManagerSaveException;
import tasks.EpicTask;
import tasks.Subtask;
import tasks.Task;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
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
        FileBackedTasksManager fileBackedTaskManagerFirst = new FileBackedTasksManager(historyManager, new File(("src/res/backup.csv")));

        Task task1 = new Task("Task 1", "Task Description 1");
        fileBackedTaskManagerFirst.createTask(task1);
        Task task2 = new Task("Task 2", "Task Description 2");
        fileBackedTaskManagerFirst.createTask(task2);

        EpicTask epicTask1 = new EpicTask("EpicTask 1", "EpicTask Description 1");
        EpicTask epicTask2 = new EpicTask("EpicTask 2", "EpicTask Description 2");

        fileBackedTaskManagerFirst.createEpicTask(epicTask1);
        fileBackedTaskManagerFirst.createEpicTask(epicTask2);

        fileBackedTaskManagerFirst.getTaskById(2);
        fileBackedTaskManagerFirst.getEpicById(3);

        fileBackedTaskManagerFirst.getTaskById(1);

        loadFromFile(new File("src/res/backup.csv"));
    }

    private void save() {
        try (Writer writer = new FileWriter(file)) {
            writer.write("id,type,name,status,description,epic\n");

            List<Task> tasks = super.getTasks();
            for (Task task : tasks) {
                writer.write(String.format("%s\n", task.toStringInFile()));
            }

            List<EpicTask> epicTasks = super.getEpicTasks();
            for (EpicTask epicTask : epicTasks) {
                writer.write(String.format("%s\n", epicTask.toStringInFile()));
            }

            List<Subtask> subTasks = super.getSubtasks();
            for (Subtask subTask : subTasks) {
                writer.write(String.format("%s\n", subTask.toStringInFile()));
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

        String[] backupTaks = value.split(",");
        id = Integer.parseInt(backupTaks[0]);
        type = Type.valueOf(backupTaks[1]);
        title = String.valueOf(backupTaks[2]);
        status = Status.valueOf(backupTaks[3]);
        description = String.valueOf(backupTaks[4]);

        if (type == Type.TASK) {
            return new Task(id, title, description, status);
        } else if (type == Type.SUBTASK) {
            int epicId = Integer.parseInt(backupTaks[5]);
            return new Subtask(id, title, description, status, epicId);
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

        int newId = 0;

        try {
            Path path = file.toPath();
            String content = Files.readString(path);
            String[] lines = content.split("\n");
            for (int i = 1; i < lines.length - 2; i++) {
                Task task = fromString(lines[i]);
                newId = fileBackedTasksManager.getId() + 1;
                switch (task.getType()) {
                    case TASK:
                        fileBackedTasksManager.tasks.put(newId, task);
                        break;
                    case EPIC:
                        fileBackedTasksManager.epics.put(newId, (EpicTask) task);
                        break;
                    case SUBTASK:
                        fileBackedTasksManager.subtasks.put(newId, (Subtask) task);
                        break;
                }

            }
            String history = lines[lines.length - 1];
            List<Integer> list = historyFromString(history);
            if (!list.isEmpty()) {
                for (int id : list) {
                    if (fileBackedTasksManager.tasks.containsKey(id)) {
                        fileBackedTasksManager.getTaskById(id);
                    } else if (fileBackedTasksManager.epics.containsKey(id)) {
                        fileBackedTasksManager.getEpicById(id);
                    } else if (fileBackedTasksManager.subtasks.containsKey(id)) {
                        fileBackedTasksManager.getSubtaskById(id);
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return fileBackedTasksManager;
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
    public Subtask createSubtask(Subtask subtask) {
        super.createSubtask(subtask);
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
    public void updateSubtask(Subtask subtask) {
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
        EpicTask epicTask = getEpicById(id);
        save();
        return epicTask;
    }

    @Override
    public Subtask getSubtaskById(int id) {
        Subtask subtask = getSubtaskById(id);
        save();
        return subtask;
    }
}
