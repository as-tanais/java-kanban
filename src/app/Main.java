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
        //HistoryManager inMemoryHistoryManager = Managers.getDefaultHistory();

//        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager(inMemoryHistoryManager, new File(("src/res/backup.csv")));
//
//        fileBackedTasksManager.loadFromFile(new File ("src/res/backup.csv"));

        Task taskOne = new Task("tasks.Task 1", "Description of task 1", Instant.now().plusSeconds(600), 5);
        Task taskTwo = new Task("tasks.Task 2", "Description of task 2",Instant.now(), 5);
        Task taskThree = new Task("Task 3", "Task 3 desc", Instant.now(), 30);
        Task taskFour = new Task("tasks.Task 4", "Description of task 4");
//
        inMemoryTaskManager.createTask(taskOne);
        inMemoryTaskManager.createTask(taskTwo);

//        inMemoryTaskManager.createTask(taskThree);
        inMemoryTaskManager.createTask(taskFour);

        System.out.println(inMemoryTaskManager.getPrioritizedTasks());


        EpicTask epicTaskOne = new EpicTask("tasks.EpicTask 1", "Description of the tasks.EpicTask 1");
        EpicTask epicTaskTwo = new EpicTask("tasks.EpicTask 2", "Description of the tasks.EpicTask 2");
//
//
        inMemoryTaskManager.createEpicTask(epicTaskOne);
        inMemoryTaskManager.createEpicTask(epicTaskTwo);
//
       SubTask subTaskOne = new SubTask("SubTask 1", "Description of the subtask",Instant.now(),30, inMemoryTaskManager.getEpics().get(5).getId());
////        Subtask subTaskTwo = new Subtask("SubTask 1", "Description of the subtask", inMemoryTaskManager.getEpics().get(2).getId());
////        Subtask subTaskThree = new Subtask("SubTask 1", "Description of the subtask", inMemoryTaskManager.getEpics().get(3).getId());
//
//
        inMemoryTaskManager.createSubtask(subTaskOne);
//        inMemoryTaskManager.createSubtask(subTaskTwo);
//        inMemoryTaskManager.createSubtask(subTaskThree);

//
//        inMemoryTaskManager.printTask();
//        inMemoryTaskManager.printEpicTask();
//        inMemoryTaskManager.printSubTask();

        System.out.println("\n");

        //проверка fileManger

//        fileBackedTasksManager.createTask(taskOne);
//        fileBackedTasksManager.createTask(taskTwo);
//        fileBackedTasksManager.createEpicTask(epicTaskOne);
//        fileBackedTasksManager.createEpicTask(epicTaskTwo);
//        fileBackedTasksManager.createSubtask(subTaskOne);

//        subTaskOne.setStatus(Status.IN_PROGRESS);
//
//        inMemoryTaskManager.updateSubtask(subTaskOne);
//
//        inMemoryTaskManager.printEpicTask();
//        inMemoryTaskManager.printSubTask();
//
//        System.out.println("\n");
////        subTaskThree.setStatus(Status.DONE);
////        inMemoryTaskManager.updateSubtask(subTaskThree);
//        inMemoryTaskManager.printEpicTask();
//        inMemoryTaskManager.printSubTask();
//
//        System.out.println("\n");
////        inMemoryTaskManager.deleteEpicById(inMemoryTaskManager.getEpics().get(2).getId());
//        inMemoryTaskManager.deleteTasks();
//
//
//        inMemoryTaskManager.printTask();
//        inMemoryTaskManager.printEpicTask();
//        inMemoryTaskManager.printSubTask();
//
//        inMemoryTaskManager.getTaskById(1);
//        inMemoryTaskManager.getTaskById(2);
//        inMemoryTaskManager.getEpicById(3);
////        inMemoryTaskManager.getTaskById(1);
//
//        System.out.println("\n");
//        System.out.println("\n");

//        inMemoryTaskManager.printTask();
//        System.out.println("\n");
//        inMemoryTaskManager.printEpicTask();
//        System.out.println("\n");
//        inMemoryTaskManager.printSubTask();

        //System.out.println(inMemoryTaskManager.getHistory());



        //System.out.println(inMemoryHistoryManager.getHistory().size());

    }
}




