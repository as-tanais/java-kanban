package servers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import manager.Managers;
import manager.TaskManager;
import tasks.Task;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Optional;


public class HttpTaskServer {

    private static final int PORT = 8080;
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    private final HttpServer server;
    private final TaskManager manager;

    Gson gson = new GsonBuilder()
            //.registerTypeAdapter(LocalDateTime.class, new LocalDatetimeAdpter())
            .setPrettyPrinting()
            .create();

    public HttpTaskServer(TaskManager manager) throws IOException {
        server = HttpServer.create(new InetSocketAddress(PORT), 0);
        server.createContext("/tasks", new TasksHandler());
        //server.createContext("/epics", new EpicTasksHandler());
        this.manager = manager;
    }

    public void startServer() {
        server.start();
        System.out.println("HTTP-server started");
    }

    public static void main(String[] args) throws IOException {
        TaskManager inMemoryTaskManager = Managers.getDefault();
        HttpTaskServer httpTaskServer = new HttpTaskServer(inMemoryTaskManager);
        Task taskOne = new Task("Task 1", "Description of task 1", Instant.now().plusSeconds(1200), 5);
        Task taskTwo = new Task("Task 2", "Description of task 2");

        inMemoryTaskManager.createTask(taskOne);
        inMemoryTaskManager.createTask(taskTwo);
        httpTaskServer.startServer();
    }

    class TasksHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String method = exchange.getRequestMethod();
            switch (method) {
                case ("GET"): {
                    handelTasks(exchange);
                    break;
                }
                default:
                    writeResponse(exchange, "endpoint not found", 404);
            }
        }

//        private Endpoint getEndpoint(String requestPath, String requestMethod) {
//            String[] pathParts = requestPath.split("/");
//            if (pathParts.length == 2 && pathParts[1].equals("tasks")) {
//                return Endpoint.GET_TASKS;
//            } else
//                return Endpoint.UNKNOWN;
//        }

        private void writeResponse(HttpExchange exchange,
                                   String responseString,
                                   int responseCode) throws IOException {
            try (OutputStream os = exchange.getResponseBody()) {
                exchange.sendResponseHeaders(responseCode, 0);
                os.write(responseString.getBytes(DEFAULT_CHARSET));
            }
            exchange.close();
        }
        private void handelTasks(HttpExchange exchange) throws IOException {
//            Optional<Integer> postIdOpt = getTaskId(exchange);
//            if(postIdOpt.isEmpty()) {
//                writeResponse(exchange, "Wrong Task ID", 400);
//                return;
//            }
//            int postId = postIdOpt.get();
            String response;
            System.out.println(exchange);
            String[] pathParts = exchange.getRequestURI().getPath().split("/");
//            System.out.println(exchange);
//            System.out.println(pathParts.length);
////            System.out.println(pathParts[0]);
////            System.out.println(pathParts[1]);
//            System.out.println(pathParts[2]);

            String param = exchange.getRequestURI().getQuery();
            InputStream inputStream = exchange.getRequestBody();
            String bodyS = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
            System.out.println(param);

            if (pathParts.length == 3) {
//                System.out.println("aga");
//                System.out.println(pathParts[2]);
                int id = Integer.parseInt(pathParts[2]);

                    String r = gson.toJson(manager.getTaskById(id));
                    writeResponse(exchange, r, 200);


            }
            if (pathParts.length == 2) {

                response = gson.toJson(manager.getTasks());
                    System.out.println("tut");
                    writeResponse(exchange, response, 200);

            }


        }

        private Optional<Integer> getTaskId(HttpExchange exchange) {
            String[] pathParts = exchange.getRequestURI().getPath().split("/");
            try {
                return Optional.of(Integer.parseInt(pathParts[2]));
            } catch (NumberFormatException exception) {
                return Optional.empty();
            }
        }

    }


}



