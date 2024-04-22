package servers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpServer;
import manager.Managers;
import manager.TaskManager;
import servers.handlers.*;
import tasks.EpicTask;
import tasks.SubTask;
import tasks.Task;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.Instant;


public class HttpTaskServer {

    private static final int PORT = 8080;
    private final HttpServer server;
    public static TaskManager manager;

    Gson gson = new GsonBuilder()
            //.registerTypeAdapter(LocalDateTime.class, new LocalDatetimeAdpter())
            .setPrettyPrinting()
            .create();

    public HttpTaskServer() throws IOException {
        manager = Managers.getDefault();
        server = HttpServer.create(new InetSocketAddress(PORT), 0);
        server.createContext("/tasks", new TasksHandler(manager));
        server.createContext("/epics", new EpicsHandler(manager));
        server.createContext("/subtasks", new SubTaksHandler(manager));
        server.createContext("/history", new HistoryHandler(manager));
        server.createContext("/prioritized", new PrioritizedHandler(manager));
    }

    public void startServer() {
        server.start();
        System.out.println("HTTP-server started");
    }

    public void  stopServer(){
        server.stop(0);
        System.out.println("HTTP-server stoped");
    }




    public static void main(String[] args) throws IOException {
//        TaskManager inMemoryTaskManager = Managers.getDefault();
        HttpTaskServer httpTaskServer = new HttpTaskServer();

        Task taskOne = new Task("Task 1", "Description of task 1", Instant.now(), 5);
        Task taskTwo = new Task("Task 2", "Description of task 2");

        manager.createTask(taskOne);
        manager.createTask(taskTwo);

        EpicTask epicTaskOne = new EpicTask("Epic 1", "Des epic 1");

        manager.createEpicTask(epicTaskOne);

        SubTask subTaskOne = new SubTask("Sub 1", "Des sub 1", Instant.now().plusSeconds(700), 10,3);


        manager.createSubtask(subTaskOne);



        manager.getTaskById(1);
        manager.getTaskById(2);
        manager.getEpicById(3);

        httpTaskServer.startServer();
    }
}



