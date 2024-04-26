package servers;

import com.sun.net.httpserver.HttpServer;
import manager.Managers;
import manager.TaskManager;
import servers.handlers.*;
import tasks.EpicTask;
import tasks.SubTask;
import tasks.Task;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.time.Instant;


public class HttpTaskServer {

    private static final int PORT = 8080;
    private final HttpServer server;
    private static TaskManager manager;

    public HttpTaskServer() throws IOException {
        manager = Managers.getDefault();
        server = HttpServer.create(new InetSocketAddress(PORT), 0);
        server.createContext("/tasks", new TasksHandler(manager));
        server.createContext("/epics", new EpicsHandler(manager));
        server.createContext("/subtasks", new SubTaskHandler(manager));
        server.createContext("/history", new HistoryHandler(manager));
        server.createContext("/prioritized", new PrioritizedHandler(manager));
    }

    public void startServer() {
        server.start();
        System.out.println("HTTP-server started");
    }

    public static TaskManager getManager() {
        return manager;
    }

    public void stopServer() {
        server.stop(0);
        System.out.println("HTTP-server stoped");
    }


    public static void main(String[] args) throws IOException {
        HttpTaskServer httpTaskServer = new HttpTaskServer();

        Task taskOne = new Task("Task 1", "Description of task 1", Instant.now(), 5);
        Task taskTwo = new Task("Task 2", "Description of task 2");

        getManager().createTask(taskOne);
        getManager().createTask(taskTwo);

        EpicTask epicTaskOne = new EpicTask("Epic 1", "Des epic 1");
        getManager().createEpicTask(epicTaskOne);

        SubTask subTaskOne = new SubTask("Sub 1", "Des sub 1", Instant.now().plusSeconds(700), 10, 3);
        getManager().createSubtask(subTaskOne);

        getManager().getTaskById(1);
        getManager().getTaskById(2);
        getManager().getEpicById(3);


        httpTaskServer.startServer();
    }
}



