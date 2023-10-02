package app;

import manager.Manager;
import tasks.EpicTask;
import tasks.Subtask;
import tasks.Task;

public class Main {

    public static void main(String[] args) {
        System.out.println("Поехали!");

        Manager manager = new Manager();

        Task taskOne = new Task("tasks.Task One", "Description of task one");
        Task taskTwo = new Task("tasks.Task Two", "Description of task Two");

        manager.createTask(taskOne);
        manager.createTask(taskTwo);

        EpicTask epicTaskOne = new EpicTask("tasks.EpicTask 1", "Description of the tasks.EpicTask 1");
        EpicTask epicTaskTwo = new EpicTask("tasks.EpicTask 2", "Description of the tasks.EpicTask 2");


        manager.createEpicTask(epicTaskOne);
        manager.createEpicTask(epicTaskTwo);

        Subtask subTaskOne = new Subtask("SubTask 1", "Description of the subtask", manager.getEpics().get(2).getId());
        Subtask subTaskTwo = new Subtask("SubTask 1", "Description of the subtask", manager.getEpics().get(2).getId());
        Subtask subTaskThree = new Subtask("SubTask 1", "Description of the subtask", manager.getEpics().get(3).getId());


        manager.createSubtask(subTaskOne);
        manager.createSubtask(subTaskTwo);
        manager.createSubtask(subTaskThree);


        manager.printTask();
        manager.printEpicTask();
        manager.printSubTask();

        System.out.println("\n");

        subTaskOne.setStatus("IN_PROGRESS");

        manager.updateSubtask(subTaskOne);

        manager.printEpicTask();
        manager.printSubTask();

        System.out.println("\n");
        subTaskThree.setStatus("DONE");
        manager.updateSubtask(subTaskThree);
        manager.printEpicTask();
        manager.printSubTask();

        System.out.println("\n");
        manager.deleteEpicById(manager.getEpics().get(2).getId());
        manager.deleteTasks();


        manager.printTask();
        manager.printEpicTask();
        manager.printSubTask();

    }
}




