import exception.ManagerSaveException;
import manager.FileBackedTasksManager;
import manager.InMemoryHistoryManager;
import manager.Managers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.EpicTask;
import tasks.SubTask;
import tasks.Task;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class FileBackedTasksManagerTest extends TaskManagerTest<FileBackedTasksManager>{

    File save = new File("src/res/backup.csv");

    @BeforeEach
    public void loadInitialConditions() {

        InMemoryHistoryManager historyManager = Managers.getDefaultHistory();
        manager = new FileBackedTasksManager(historyManager, save);

    }

    @Test
    public void loadFromFileTest() {

        Task task1 = manager.createTask(newPriorTask());
        EpicTask epic1 = manager.createEpicTask(newSimpleEpicTask());
        SubTask subtask1 = manager.createSubtask(newPriorSubTask(epic1));

        List<Task> tasks = new ArrayList<>();
        tasks.add(task1);
        List<EpicTask> epicTasks = new ArrayList<>();
        epicTasks.add(epic1);
        List<SubTask> subTasks = new ArrayList<>();
        subTasks.add(subtask1);

        manager.getTaskById(task1.getId());
        manager.getEpicById(epic1.getId());
        manager.getSubtaskById(subtask1.getId());

        manager = FileBackedTasksManager.loadFromFile(save);

        assertEquals(tasks, manager.getTasks());
        assertEquals(epicTasks, manager.getEpics());
        assertEquals(subTasks, manager.getSubtasks());
        assertEquals(List.of(task1, epic1, subtask1), manager.getHistory());
    }



    @Test
    public void throwManagerSaveExceptionTest() {

        save = new File("wrongfile.exe");

        assertThrows(ManagerSaveException.class, () -> FileBackedTasksManager.loadFromFile(save));

    }

}
