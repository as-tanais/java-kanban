package servers.handlers;

import com.sun.net.httpserver.HttpExchange;
import exception.IntersectionException;
import manager.TaskManager;
import servers.Endpoint;
import tasks.SubTask;

import java.io.IOException;
import java.util.Optional;

public class SubTaksHandler extends TasksHandler {

    public SubTaksHandler(TaskManager manager) {
        super(manager);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Endpoint endpoint = getEndpoint(exchange.getRequestURI().getPath(), exchange.getRequestMethod());

        switch (endpoint) {
            case GET_TASKS: {
                getSubTasksHandle(exchange);
                break;
            }
            case GET_TASK_ID: {
                getSubTaskByIdHandle(exchange);
                break;
            }
            case POST_TASK: {
                createSubTaskHandle(exchange);
                break;
            }
            case POST_TASK_ID: {
                updateSubTaskHandle(exchange);
                break;
            }
            case DELETE_TASK: {
                deleteSubTaskByIdHandle(exchange);
                break;
            }
            case UNKNOWN: {
                writeResponse(exchange, "NOT_FOUND", 404);
                break;
            }
        }
    }

    private void deleteSubTaskByIdHandle(HttpExchange exchange) throws IOException {
        Optional<Integer> taskIdOpt = getTaskId(exchange);

        if (taskIdOpt.isEmpty()) {
            writeResponse(exchange, "Некорректный идентификатор поста", 400);
            return;
        }
        int taskId = taskIdOpt.get();
        manager.deleteSubtaskById(taskId);
        writeResponse(exchange, "Задача удалена", 200);
    }

    private void updateSubTaskHandle(HttpExchange exchange) throws IOException {
        Optional<Integer> taskIdOpt = getTaskId(exchange);

        if (taskIdOpt.isEmpty()) {
            writeResponse(exchange, "Некорректный идентификатор поста", 404);
            return;
        }
        int taskId = taskIdOpt.get();

        if (manager.getEpicById(taskId) != null) {

            String data = new String(exchange.getRequestBody().readAllBytes(), DEFAULT_CHARSET);
            SubTask task = gson.fromJson(data, SubTask.class);

            manager.updateSubtask(task);
            writeResponse(exchange, "Task update", 200);

        } else {
            writeResponse(exchange, "Задача с id " + taskId + " не найдена", 404);
        }
    }

    private void createSubTaskHandle(HttpExchange exchange) throws IOException {
        String data = new String(exchange.getRequestBody().readAllBytes(), DEFAULT_CHARSET);
        SubTask task = gson.fromJson(data, SubTask.class);
        try {
            manager.createSubtask(task);
            writeResponse(exchange, "Task created", 201);
        } catch (IntersectionException e) {
            writeResponse(exchange, "Task time cross", 406);
        }
    }

    private void getSubTaskByIdHandle(HttpExchange exchange) throws IOException {
        Optional<Integer> taskIdOpt = getTaskId(exchange);
        String response;
        int resCode;
        if (taskIdOpt.isEmpty()) {
            writeResponse(exchange, "Некорректный идентификатор поста", 400);
            return;
        }
        int taskId = taskIdOpt.get();
        SubTask task = manager.getSubtaskById(taskId);

        if (task == null) {
            response = "Задача с id " + taskId + " не найдена";
            resCode = 404;
        } else {
            response = gson.toJson(task);
            resCode = 200;
        }
        writeResponse(exchange, response, resCode);
    }

    private void getSubTasksHandle(HttpExchange exchange) throws IOException {
        String response = gson.toJson(manager.getSubtasks());
        writeResponse(exchange, response, 200);
    }
}
