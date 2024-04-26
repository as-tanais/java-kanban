package htttp_server_tests;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.*;
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

public class SubTasksHandlerTest {

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
    public void stop() {
        server.stopServer();
    }

    @Test
    public void getTasksTest() throws IOException, InterruptedException {

        EpicTask epicTask = new EpicTask("Epic 1", "Des epic 1");

        SubTask subTaskOne = new SubTask("Task 1", "Description task 1", Instant.now().plusSeconds(300), 1, 1);
        SubTask subTaskTwo = new SubTask("Task 2", "Description task 2", Instant.now().plusSeconds(600), 1, 1);

        server.manager.createEpicTask(epicTask);
        server.manager.createSubtask(subTaskOne);
        server.manager.createSubtask(subTaskTwo);

        URI uri = URI.create(BASE_URL + "subtasks");
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "application/json")
                .build();
        HttpResponse<String> response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        List<SubTask> subtasks = GSON.fromJson(response.body(), new TypeToken<List<SubTask>>() {
        }.getType());
        assertEquals(2, subtasks.size());
        assertEquals(200, response.statusCode());

    }

    @Test
    public void getTasksByIdTest() throws IOException, InterruptedException {

        EpicTask epicTask = new EpicTask("Epic 1", "Des epic 1");
        SubTask subTaskOne = new SubTask("Task 1", "Description task 1", Instant.now().plusSeconds(300), 1, 1);

        server.manager.createEpicTask(epicTask);
        server.manager.createSubtask(subTaskOne);

        URI uri = URI.create(BASE_URL + "subtasks/2");
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "application/json")
                .build();

        HttpResponse<String> response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        SubTask task = GSON.fromJson(response.body(), SubTask.class);


        assertEquals(200, response.statusCode());
        assertEquals(task.getId(), subTaskOne.getId());
    }


    @Test
    public void postTaskTest() throws IOException, InterruptedException {

        EpicTask epicTask = new EpicTask("Epic 1", "Des epic 1");
        SubTask subTaskOne = new SubTask("Task 1", "Description task 1", Instant.now().plusSeconds(300), 1, 1);

        server.manager.createEpicTask(epicTask);
        String body = GSON.toJson(subTaskOne);

        URI uri = URI.create(BASE_URL + "subtasks");
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .uri(uri)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "application/json")
                .build();

        HttpResponse<String> response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode());
        assertEquals("Подзадача создана", response.body());

    }

    @Test
    public void crossTimeCreatTest() throws IOException, InterruptedException {
        EpicTask epicTask = new EpicTask("Epic 1", "Des epic 1");
        SubTask subTaskOne = new SubTask("Task 1", "Description task 1", Instant.now().plusSeconds(300), 1, 1);
        SubTask subTaskTwo = new SubTask("Task 1", "Description task 1", Instant.now().plusSeconds(300), 1, 1);

        server.manager.createEpicTask(epicTask);
        server.manager.createSubtask(subTaskOne);
        String body = GSON.toJson(subTaskTwo);



        URI uri = URI.create(BASE_URL + "subtasks");
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .uri(uri)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "application/json")
                .build();

        HttpResponse<String> response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());

        assertEquals(406, response.statusCode());
        assertEquals("Временные рамки новой подзадачи пересекается с уже существующей", response.body());

    }
}
