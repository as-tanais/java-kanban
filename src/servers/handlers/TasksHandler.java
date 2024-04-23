package servers.handlers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import exception.IntersectionException;
import manager.TaskManager;
import servers.Endpoint;
import tasks.Task;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

public class TasksHandler implements HttpHandler {

    protected static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    protected final TaskManager manager;
    protected Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .create();

    public TasksHandler(TaskManager manager) {
        this.manager = manager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Endpoint endpoint = getEndpoint(exchange.getRequestURI().getPath(), exchange.getRequestMethod());

        switch (endpoint) {
            case GET_TASKS: {
                getTaskHandle(exchange);
                break;
            }
            case GET_TASK_ID: {
                getTaskByIdHandle(exchange);
                break;
            }
            case POST_TASK: {
                createTaskHandle(exchange);
                break;
            }
            case POST_TASK_ID: {
                updateTaskHandle(exchange);
                break;
            }
            case DELETE_TASK: {
                deleteTaskByIdHandle(exchange);
                break;
            }
            case UNKNOWN: {
                writeResponse(exchange, "NOT_FOUND", 404);
                break;
            }
        }

    }

    private void getTaskHandle(HttpExchange exchange) throws IOException {


        String response = gson.toJson(manager.getTasks());
        writeResponse(exchange, response, 200);

    }

    private void getTaskByIdHandle(HttpExchange exchange) throws IOException {
        Optional<Integer> taskIdOpt = getTaskId(exchange);
        String response;
        int resCode;
        if (taskIdOpt.isEmpty()) {
            writeResponse(exchange, "Некорректный идентификатор поста", 400);
            return;
        }
        int taskId = taskIdOpt.get();
        Task task = manager.getTaskById(taskId);

        if (task == null) {
            response = "Задача с id " + taskId + " не найдена";
            resCode = 404;
        } else {
            response = gson.toJson(task);
            resCode = 200;
        }
        writeResponse(exchange, response, resCode);
    }

    private void deleteTaskByIdHandle(HttpExchange exchange) throws IOException {
        Optional<Integer> taskIdOpt = getTaskId(exchange);

        if (taskIdOpt.isEmpty()) {
            writeResponse(exchange, "Некорректный идентификатор поста", 400);
            return;
        }
        int taskId = taskIdOpt.get();
        manager.deleteTaskById(taskId);
        writeResponse(exchange, "Задача удалена", 200);
    }

    private void createTaskHandle(HttpExchange exchange) throws IOException {
        String data = new String(exchange.getRequestBody().readAllBytes(), DEFAULT_CHARSET);
        Task task = gson.fromJson(data, Task.class);
        try {
            manager.createTask(task);
            writeResponse(exchange, "Задача создана", 201);
        } catch (IntersectionException e) {
            writeResponse(exchange, "Временные рамки новой задачи пересекается с уже существующей", 406);
        }
    }

    private void updateTaskHandle(HttpExchange exchange) throws IOException {
        Optional<Integer> taskIdOpt = getTaskId(exchange);

        if (taskIdOpt.isEmpty()) {
            writeResponse(exchange, "Некорректный идентификатор поста", 404);
            return;
        }
        int taskId = taskIdOpt.get();

        if (manager.getTaskById(taskId) != null) {

            String data = new String(exchange.getRequestBody().readAllBytes(), DEFAULT_CHARSET);
            Task task = gson.fromJson(data, Task.class);

            manager.updateTask(task);
            writeResponse(exchange, "Задача обновлена", 200);

        } else {
            writeResponse(exchange, "Задача с id " + taskId + " не найдена", 404);
        }
    }

    protected Optional<Integer> getTaskId(HttpExchange exchange) {
        String[] pathParts = exchange.getRequestURI().getPath().split("/");
        try {
            return Optional.of(Integer.parseInt(pathParts[2]));
        } catch (NumberFormatException exception) {
            return Optional.empty();
        }
    }

    protected Endpoint getEndpoint(String requestPath, String requestMethod) {
        String[] pathParts = requestPath.split("/");

        if (pathParts.length == 2) {
            if (requestMethod.equals("GET")) {
                return Endpoint.GET_TASKS;
            }
            if (requestMethod.equals("POST")) {
                return Endpoint.POST_TASK;

            }
        }
        if (pathParts.length == 3) {
            if (requestMethod.equals("GET")) {
                return Endpoint.GET_TASK_ID;
            }
            if (requestMethod.equals("POST")) {
                return Endpoint.POST_TASK_ID;
            }
            if (requestMethod.equals("DELETE")) {
                return Endpoint.DELETE_TASK;
            }
        }

        return Endpoint.UNKNOWN;
    }

    protected void writeResponse(HttpExchange exchange,
                                 String responseString,
                                 int responseCode) throws IOException {
        try (OutputStream os = exchange.getResponseBody()) {
            exchange.sendResponseHeaders(responseCode, 0);
            os.write(responseString.getBytes(DEFAULT_CHARSET));
        }
        exchange.close();
    }
}
