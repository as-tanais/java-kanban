package app;

import manager.*;
import tasks.EpicTask;
import tasks.SubTask;
import tasks.Task;

import java.time.Instant;

public class Main {

    public static void main(String[] args) {
        System.out.println("Поехали!");


        TaskManager inMemoryTaskManager = Managers.getDefault();

        Task taskOne = new Task("tasks.Task 1", "Description of task 1", Instant.now().plusSeconds(600), 5);
        Task taskTwo = new Task("tasks.Task 2", "Description of task 2", Instant.now(), 5);
        Task taskThree = new Task("Task 3", "Task 3 desc", Instant.now(), 30);
        Task taskFour = new Task("tasks.Task 4", "Description of task 4");

        inMemoryTaskManager.createTask(taskOne);
        inMemoryTaskManager.createTask(taskTwo);


        inMemoryTaskManager.createTask(taskFour);

        System.out.println(inMemoryTaskManager.getPrioritizedTasks());


        EpicTask epicTaskOne = new EpicTask("tasks.EpicTask 1", "Description of the tasks.EpicTask 1");
        EpicTask epicTaskTwo = new EpicTask("tasks.EpicTask 2", "Description of the tasks.EpicTask 2");

        inMemoryTaskManager.createEpicTask(epicTaskOne);
        inMemoryTaskManager.createEpicTask(epicTaskTwo);

        SubTask subTaskOne = new SubTask("SubTask 1", "Description of the subtask", Instant.now(), 30, inMemoryTaskManager.getEpics().get(5).getId());

        inMemoryTaskManager.createSubtask(subTaskOne);

        System.out.println("\n");

    }
}




