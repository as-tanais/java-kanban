import enums.Status;
import exception.IntersectionException;
import manager.Managers;
import manager.TaskManager;
import org.junit.jupiter.api.Test;
import tasks.EpicTask;
import tasks.SubTask;
import tasks.Task;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public abstract class TaskManagerTest<T extends TaskManager> {


    protected T manager;

    protected Task newSimpleTask() {
        return new Task("Task1", "Task1");
    }

    protected Task newPriorTask() {
        return new Task("PriorTask1", "PriorTask1", Instant.now().plusSeconds(60), 5);
    }

    protected EpicTask newSimpleEpicTask() {
        return new EpicTask("EpicTask1", "EpicTask1");
    }

    protected SubTask newSimpleSubTask(EpicTask epicTask) {
        return new SubTask("SimpleSubTask1", "SimpleSubTask", epicTask.getId());
    }

    protected SubTask newPriorSubTask(EpicTask epicTask) {
        return new SubTask("SimpleSubTask1", "SimpleSubTask", Instant.now().plusSeconds(600), 5,epicTask.getId());
    }



    @Test
    public void createSimpleTaskTest(){
        Task task1 = manager.createTask(newSimpleTask());
        List<Task> createdTask = new ArrayList<>();
        createdTask.add(task1);

        List<Task> tasks = manager.getTasks();
        assertEquals(createdTask, tasks);
    }

    @Test
    public void createPriorTaskTest(){
        Task task1 = manager.createTask(newPriorTask());
        List<Task> createdTask = new ArrayList<>();
        createdTask.add(task1);

        List<Task> tasks = manager.getTasks();
        assertEquals(createdTask, tasks);
    }

    @Test
    public void createSimpleEpicTest(){
        EpicTask epicTask1 = manager.createEpicTask(newSimpleEpicTask());
        List<Task> createdTask = new ArrayList<>();
        createdTask.add(epicTask1);

        List<EpicTask> tasks = manager.getEpicTasks();
        assertEquals(createdTask, tasks);
    }

    @Test
    public void createSimpleSubTest(){
        EpicTask epicTask = manager.createEpicTask(newSimpleEpicTask());
        SubTask subSimpPriorleTask = manager.createSubtask(newPriorSubTask(epicTask));

        List<Task> createdTask = new ArrayList<>();
        createdTask.add(subSimpPriorleTask);

        List<SubTask> tasks = manager.getSubtasks();
        assertEquals(createdTask, tasks);
    }

    @Test
    public void throwIntersectionExceptionTest() {

        assertThrows(IntersectionException.class, () -> {

            manager.createTask(new Task("Task 1", "Description of task 1", Instant.now().plusSeconds(30), 5));

            manager.createTask(new Task("Tasl 2", "Description of task 2", Instant.now().plusSeconds(60), 5));

        });
    }

    @Test
    public void statusChangeEpicNewToDoneTest (){

        EpicTask epicTask = manager.createEpicTask(newSimpleEpicTask());
        SubTask subTask = manager.createSubtask(newSimpleSubTask(epicTask));

        subTask.setStatus(Status.DONE);
        manager.updateSubtask(subTask);

        assertEquals(epicTask.getStatus(), Status.DONE);

    }

    @Test
    public void statusChangeEpicNewToInProgressTest (){

        EpicTask epicTask = manager.createEpicTask(newSimpleEpicTask());
        SubTask subTask1 = manager.createSubtask(newSimpleSubTask(epicTask));
        SubTask subTask2 = manager.createSubtask(newSimpleSubTask(epicTask));

        subTask1.setStatus(Status.IN_PROGRESS);
        manager.updateSubtask(subTask1);

        assertEquals(epicTask.getStatus(), Status.IN_PROGRESS);

    }

    @Test
    public void statusCheckEpicAllSubNew (){

        EpicTask epicTask = manager.createEpicTask(newSimpleEpicTask());
        SubTask subTask1 = manager.createSubtask(newSimpleSubTask(epicTask));
        SubTask subTask2 = manager.createSubtask(newSimpleSubTask(epicTask));

        assertEquals(subTask1.getStatus(), Status.NEW);
        assertEquals(subTask2.getStatus(), Status.NEW);
        assertEquals(epicTask.getStatus(), Status.NEW);

    }

}
