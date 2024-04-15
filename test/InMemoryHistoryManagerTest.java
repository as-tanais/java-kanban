import enums.Status;
import manager.HistoryManager;
import manager.Managers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.EpicTask;
import tasks.SubTask;
import tasks.Task;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class InMemoryHistoryManagerTest {

    private HistoryManager manager;


    @BeforeEach
    public void createManager(){
        manager = Managers.getDefaultHistory();
    }

    @Test
    public void addTasksToHistoryTest() {

        Task taskOne = new Task(1, "Task title 1", "Task1", Status.NEW, Instant.EPOCH, 0);
        Task taskTwo = new Task(2, "Task title 1", "Task1", Status.NEW, Instant.EPOCH, 0);
        Task taskThree = new Task(3, "Task title 1", "Task1", Status.NEW, Instant.EPOCH, 0);

        manager.add(taskOne);
        manager.add(taskTwo);
        manager.add(taskThree);

        assertEquals(List.of(taskOne, taskTwo, taskThree), manager.getHistory());

    }

    @Test
    public void removeFromBeginning() {

        Task taskOne = new Task(1, "Task title 1", "Task1", Status.NEW, Instant.EPOCH, 0);
        Task taskTwo = new Task(2, "Task title 1", "Task1", Status.NEW, Instant.EPOCH, 0);

        EpicTask epicTaskOne = new EpicTask(3, "EpicTask title 1", "Task1", Status.NEW);

        SubTask subTaskOne = new SubTask(4, "SubTask title 1", "Task1", Status.NEW, Instant.EPOCH, 0,3);

        manager.add(taskOne);
        manager.add(taskTwo);
        manager.add(epicTaskOne);
        manager.add(subTaskOne);


        manager.remove(1);

        assertEquals(List.of(taskTwo,epicTaskOne,subTaskOne), manager.getHistory());
        assertNotNull(manager.getHistory());
        assertEquals(3, manager.getHistory().size());
    }
}