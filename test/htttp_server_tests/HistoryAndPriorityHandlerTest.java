package htttp_server_tests;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import servers.HttpTaskServer;
import tasks.EpicTask;
import tasks.SubTask;
import tasks.Task;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HistoryAndPriorityHandlerTest {

    private static HttpTaskServer server;
    private final HttpClient client = HttpClient.newHttpClient();
    private final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .create();
    private final String BASE_URL = "http://localhost:8080/";


    @BeforeEach
    public void start() throws IOException {
        server = new HttpTaskServer();
        server.startServer();
    }

    @AfterEach
    public void stop() throws IOException {
        server.stopServer();
    }

    @Test
    public void getHistoryTask() throws IOException, InterruptedException {

        Task taskOne = new Task("Task 1", "Description of task 1", Instant.now(), 5);
        Task taskTwo = new Task("Task 2", "Description of task 2");

        server.manager.createTask(taskOne);
        server.manager.createTask(taskTwo);

        EpicTask epicTaskOne = new EpicTask("Epic 1", "Des epic 1");

        server.manager.createEpicTask(epicTaskOne);

        SubTask subTaskOne = new SubTask("Sub 1", "Des sub 1", Instant.now().plusSeconds(700), 10, 3);


        server.manager.createSubtask(subTaskOne);

        server.manager.getTaskById(1);
        server.manager.getTaskById(2);
        server.manager.getEpicById(3);
        server.manager.getSubtaskById(4);

        URI uri = URI.create(BASE_URL + "history");
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "application/json")
                .build();

        HttpResponse<String> response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());

        List<Task> tasks = GSON.fromJson(response.body(), new TypeToken<List<Task>>() {
        }.getType());

        assertEquals(4, tasks.size());
        assertEquals(200, response.statusCode());

    }

    @Test
    public void getprioritizedTask() throws IOException, InterruptedException {

        Task taskOne = new Task("Task 1", "Description of task 1", Instant.now(), 5);
        Task taskTwo = new Task("Task 2", "Description of task 2");

        server.manager.createTask(taskOne);
        server.manager.createTask(taskTwo);

        EpicTask epicTaskOne = new EpicTask("Epic 1", "Des epic 1");

        server.manager.createEpicTask(epicTaskOne);

        SubTask subTaskOne = new SubTask("Sub 1", "Des sub 1", Instant.now().plusSeconds(700), 10, 3);


        server.manager.createSubtask(subTaskOne);

        server.manager.getTaskById(1);
        server.manager.getTaskById(2);
        server.manager.getEpicById(3);
        server.manager.getSubtaskById(4);

        URI uri = URI.create(BASE_URL + "prioritized");
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "application/json")
                .build();

        HttpResponse<String> response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());

        List<Task> tasks = GSON.fromJson(response.body(), new TypeToken<List<Task>>() {
        }.getType());

        assertEquals(2, tasks.size());
        assertEquals(200, response.statusCode());

    }

}
