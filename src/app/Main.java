package app;

import enums.Status;
import manager.*;
import tasks.EpicTask;
import tasks.Subtask;
import tasks.Task;

import java.io.File;

public class Main {

    public static void main(String[] args) {
        System.out.println("Поехали!");


        TaskManager inMemoryTaskManager = Managers.getDefault();
        HistoryManager inMemoryHistoryManager = Managers.getDefaultHistory();

//        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager(inMemoryHistoryManager, new File(("src/res/backup.csv")));
//
//        fileBackedTasksManager.loadFromFile(new File ("src/res/backup.csv"));

        Task taskOne = new Task("tasks.Task One", "Description of task one");
//        Task taskTwo = new Task("tasks.Task Two", "Description of task Two");
//
        inMemoryTaskManager.createTask(taskOne);
//        inMemoryTaskManager.createTask(taskTwo);
//


//        EpicTask epicTaskOne = new EpicTask("tasks.EpicTask 1", "Description of the tasks.EpicTask 1");
//        EpicTask epicTaskTwo = new EpicTask("tasks.EpicTask 2", "Description of the tasks.EpicTask 2");
//
//
//        inMemoryTaskManager.createEpicTask(epicTaskOne);
//        inMemoryTaskManager.createEpicTask(epicTaskTwo);

//        Subtask subTaskOne = new Subtask("SubTask 1", "Description of the subtask", inMemoryTaskManager.getEpics().get(2).getId());
//        Subtask subTaskTwo = new Subtask("SubTask 1", "Description of the subtask", inMemoryTaskManager.getEpics().get(2).getId());
//        Subtask subTaskThree = new Subtask("SubTask 1", "Description of the subtask", inMemoryTaskManager.getEpics().get(3).getId());


//        inMemoryTaskManager.createSubtask(subTaskOne);
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

        inMemoryTaskManager.getTaskById(1);
        inMemoryTaskManager.getTaskById(1);
        inMemoryTaskManager.getTaskById(1);

        System.out.println(inMemoryHistoryManager.getHistory().size());

    }
}




