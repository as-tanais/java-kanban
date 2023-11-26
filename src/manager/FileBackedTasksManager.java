package manager;

import enums.*;
import exception.ManagerSaveException;
import tasks.*;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FileBackedTasksManager extends InMemoryTaskManager{
    private final File file;
    public FileBackedTasksManager (HistoryManager historyManager, String path){
        super(historyManager);
        this.file = new File(path);
    }

    @Override
    public Task createTask(Task task){
        super.createTask(task);
        save();
        return task;
    }

    @Override
    public EpicTask createEpicTask(EpicTask epicTask){
        super.createEpicTask(epicTask);
        save();
        return epicTask;
    }

    @Override
    public Subtask createSubtask(Subtask subtask){
        super.createSubtask(subtask);
        save();
        return subtask;
    }

    public void save(){
        try(Writer writer = new FileWriter(file)) {
            writer.write("id,type,name,status,description,epic\n");
            HashMap<Integer, String> taskCollections = new HashMap<>();

            List<Task> tasks = super.getTasks();
            for(Task task : tasks){
                taskCollections.put(task.getId(), task.toStringInFile());
            }

            List<EpicTask> epicTasks = super.getEpicTasks();
            for(EpicTask epicTask : epicTasks){
                taskCollections.put(epicTask.getId(), epicTask.toStringInFile());
            }

            List<Subtask> subTasks = super.getSubtasks();
            for(Subtask subTask : subTasks){
                taskCollections.put(subTask.getId(), subTask.toStringInFile());
            }

            for (String str : taskCollections.values()) {
                writer.write(String.format("%s\n", str));
            }

            writer.write("\n");

            for (Task task : super.getHistory()) {
                writer.write(task.getId());
            }

        } catch (IOException e) {
            throw new ManagerSaveException("File not saved");
        }

    }

    Task fromString(String value) {
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
            return new Task(id , title, description, status);
        } else if (type == Type.SUBTASK) {
            int epicId = Integer.parseInt(backupTaks[5]);
            return new Subtask(id , title, description, status, epicId);
        } else {
            return new EpicTask(id, title, description, status);
        }
    }

    //todo: Напишите статические методы для сохранения менеджера истории из CSV.
    static String historyToString() {
        return "";
    }

    //todo: Напишите статические методы для восстановления менеджера истории из CSV.
    static List<Integer> historyFromString(String value){
        return new ArrayList<>();
    }

    public void loadFromFile(String file) {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))){
            String line;
            while (reader.ready()) {
                line = reader.readLine();
                if (line.isEmpty()){
                    break;
                } if (line.contains("id")) {
                    continue;
                }

                Task task = fromString(line);
                switch (task.getType()){
                    case TASK:
                        super.createTask(task);
                        break;
                    case EPIC:
                        super.createEpicTask((EpicTask) task);
                        break;
                    case SUBTASK:
                        super.createSubtask((Subtask) task);
                        break;
                }
            }
        }catch (IOException e) {
            throw new ManagerSaveException("Backup not loaded");
        }
    }

}
