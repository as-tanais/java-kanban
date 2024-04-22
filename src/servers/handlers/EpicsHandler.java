package servers.handlers;

import com.sun.net.httpserver.HttpExchange;
import exception.IntersectionException;
import manager.TaskManager;
import servers.Endpoint;
import tasks.EpicTask;

import java.io.IOException;
import java.util.Optional;

public class EpicsHandler extends TasksHandler {

    public EpicsHandler(TaskManager manager) {
        super(manager);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Endpoint endpoint = getEndpoint(exchange.getRequestURI().getPath(), exchange.getRequestMethod());

        switch (endpoint) {
            case GET_TASKS: {
                getEpicsTaskHandle(exchange);
                break;
            }
            case GET_TASK_ID: {
                getEpicByIdHandle(exchange);
                break;
            }
            case POST_TASK: {
                createEpicHandle(exchange);
                break;
            }
            case POST_TASK_ID: {
                updateEpicHandle(exchange);
                break;
            }
            case DELETE_TASK: {
                deleteEpicByIdHandle(exchange);
                break;
            }
            case UNKNOWN: {
                writeResponse(exchange, "NOT_FOUND", 404);
                break;
            }
            case GET_SUBTASK_BY_EPIC:{
                getSubTasksByEpic(exchange);
                break;
            }
        }
    }

    private void getSubTasksByEpic(HttpExchange exchange) throws IOException{
        Optional<Integer> taskIdOpt = getTaskId(exchange);
        String response;
        int statusCode;
        if (taskIdOpt.isEmpty()) {
            writeResponse(exchange, "Некорректный идентификатор поста", 400);
            return;
        }
        int taskId = taskIdOpt.get();

        if (manager.getEpicById(taskId) != null){
            response = gson.toJson(manager.getSubtasksByEpicId(taskId));
            statusCode =200;
        } else {
            response = "Задача с ID " + taskId + " не найдена";
            statusCode = 404;
        }

        writeResponse(exchange, response, statusCode);
    }

    private void deleteEpicByIdHandle(HttpExchange exchange) throws IOException {
        Optional<Integer> taskIdOpt = getTaskId(exchange);

        if (taskIdOpt.isEmpty()) {
            writeResponse(exchange, "Некорректный идентификатор поста", 400);
            return;
        }
        int taskId = taskIdOpt.get();


        manager.deleteEpicById(taskId);

        writeResponse(exchange, "Задача удалена", 200);
    }

    private void createEpicHandle(HttpExchange exchange) throws IOException {
        String data = new String(exchange.getRequestBody().readAllBytes(), DEFAULT_CHARSET);
        EpicTask task = gson.fromJson(data, EpicTask.class);
        try {
            manager.createEpicTask(task);
            writeResponse(exchange, "Суперзадача создана", 201);
        } catch (IntersectionException e) {
            writeResponse(exchange, "Task time cross", 406);
        }
    }

    private void updateEpicHandle(HttpExchange exchange) throws IOException {

        Optional<Integer> taskIdOpt = getTaskId(exchange);

        if (taskIdOpt.isEmpty()) {
            writeResponse(exchange, "Некорректный идентификатор поста", 404);
            return;
        }
        int taskId = taskIdOpt.get();

        if (manager.getEpicById(taskId) != null) {

            String data = new String(exchange.getRequestBody().readAllBytes(), DEFAULT_CHARSET);
            EpicTask task = gson.fromJson(data, EpicTask.class);

            manager.updateEpic(task);
            writeResponse(exchange, "Задача обновлена", 200);

        } else {
            writeResponse(exchange, "Задача с id " + taskId + " не найдена", 404);
        }

    }

    private void getEpicsTaskHandle(HttpExchange exchange) throws IOException {
        String response = gson.toJson(manager.getEpicTasks());
        writeResponse(exchange, response, 200);
    }

    private void getEpicByIdHandle(HttpExchange exchange) throws IOException {
        Optional<Integer> taskIdOpt = getTaskId(exchange);
        String response;
        int resCode;
        if (taskIdOpt.isEmpty()) {
            writeResponse(exchange, "Некорректный идентификатор поста", 400);
            return;
        }
        int taskId = taskIdOpt.get();
        EpicTask task = manager.getEpicById(taskId);

        if (task == null) {
            response = "Задача с id " + taskId + " не найдена";
            resCode = 404;
        } else {
            response = gson.toJson(task);
            resCode = 200;
        }
        writeResponse(exchange, response, resCode);
    }


    @Override
    protected Endpoint getEndpoint(String requestPath, String requestMethod) {
        String[] pathParts = requestPath.split("/");
        if (pathParts.length == 4 && pathParts[3].equals("subtasks")) {
            return Endpoint.GET_SUBTASK_BY_EPIC;
        }else {
            return super.getEndpoint(requestPath, requestMethod);
        }
    }
}
