package htttp_server_tests;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.*;
import servers.HttpTaskServer;
import tasks.Task;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TasksHandlerTest {

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

    protected Task newSimpleTask(int id) {
        return new Task("Task" + id, "Description task " + id);
    }


    @Test
    public void getTasksTest() throws IOException, InterruptedException {

        Task taskOne = newSimpleTask(1);
        Task taskTwo = newSimpleTask(2);

        server.manager.createTask(taskOne);
        server.manager.createTask(taskTwo);

        URI uri = URI.create(BASE_URL + "tasks");
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
    public void getTasksByIdTest() throws IOException, InterruptedException {

        Task taskOne = newSimpleTask(1);
        Task taskTwo = newSimpleTask(2);

        server.manager.createTask(taskOne);
        server.manager.createTask(taskTwo);

        URI uri = URI.create(BASE_URL + "tasks/1");
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
    public void postTaskTest() throws IOException, InterruptedException {

        Task taskOne = newSimpleTask(1);
        String body = GSON.toJson(taskOne);

        URI uri = URI.create(BASE_URL + "tasks");
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .uri(uri)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "application/json")
                .build();

        HttpResponse<String> response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());

        assertEquals(201,response.statusCode());
        assertEquals("Задача создана",response.body());

    }
}
