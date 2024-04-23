package htttp_server_tests;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
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

public class EpicsHandlerTest {

    private static HttpTaskServer server;
    HttpClient client = HttpClient.newHttpClient();
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

    protected EpicTask newSimpleTask(int id) {
        return new EpicTask("Task" + id, "Description task " + id);
    }


    @Test
    public void getEpicTasksTest() throws IOException, InterruptedException {

        EpicTask taskOne = newSimpleTask(1);
        EpicTask taskTwo = newSimpleTask(2);

        server.manager.createEpicTask(taskOne);
        server.manager.createEpicTask(taskTwo);

        URI uri = URI.create(BASE_URL + "epics");
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

    @Test
    public void getEpicTasksByIdTest() throws IOException, InterruptedException {

        EpicTask taskOne = newSimpleTask(1);
        EpicTask taskTwo = newSimpleTask(2);

        server.manager.createEpicTask(taskOne);
        server.manager.createEpicTask(taskTwo);

        URI uri = URI.create(BASE_URL + "epics/1");
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "application/json")
                .build();

        HttpResponse<String> response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        Task task = GSON.fromJson(response.body(), Task.class);

        assertEquals(200,response.statusCode());
        assertEquals(task.getId(),taskOne.getId());
    }


    @Test
    public void postEpicTaskTest() throws IOException, InterruptedException {

        EpicTask taskOne = newSimpleTask(1);
        String body = GSON.toJson(taskOne);

        URI uri = URI.create(BASE_URL + "epics");
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .uri(uri)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "application/json")
                .build();

        HttpResponse<String> response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());

        assertEquals(201,response.statusCode());
        assertEquals("Суперзадача создана",response.body());

    }


    @Test
    public void getAllSubTaskByEpic () throws IOException, InterruptedException {
        EpicTask epicTask = new EpicTask("Epic 1", "Des epic 1");
        SubTask subTaskOne = new SubTask("Task 1", "Description task 1", Instant.now().plusSeconds(300), 1, 1);
        SubTask subTaskTwo = new SubTask("Task 1", "Description task 1", Instant.now().plusSeconds(700), 1, 1);

        server.manager.createEpicTask(epicTask);
        server.manager.createSubtask(subTaskOne);
        server.manager.createSubtask(subTaskTwo);

        URI uri = URI.create(BASE_URL + "epics/1/subtasks");
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "application/json")
                .build();

        HttpResponse<String> response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        assertEquals(200,response.statusCode());
    }
}
