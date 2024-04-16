import exception.IntersectionException;
import manager.Managers;
import manager.TaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Task;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TaskManagerTest<T extends TaskManager> {


    TaskManager inMemoryTaskManager = Managers.getDefault();

    @Test
    public void addTaskTest() {
        Task task = new Task("Test addNewTask", "Test addNewTask description", Instant.now(), 60);
        inMemoryTaskManager.createTask(task);
        final int taskId = task.getId();

        final Task savedTask = inMemoryTaskManager.getTaskById(taskId);

        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task, savedTask, "Задачи не совпадают.");

        final List<Task> tasks = inMemoryTaskManager.getTasks();

        assertNotNull(tasks, "Задачи на возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(task, tasks.get(0), "Задачи не совпадают.");

    }

    @Test
    public void throwIntersectionExceptionTest() {

        assertThrows(IntersectionException.class, () -> {

            inMemoryTaskManager.createTask(new Task("Task 1", "Description of task 1", Instant.now().plusSeconds(30), 5));

            inMemoryTaskManager.createTask(new Task("Tasl 2", "Description of task 2", Instant.now().plusSeconds(60), 5));

        });

    }

}
