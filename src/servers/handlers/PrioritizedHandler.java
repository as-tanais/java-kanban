package servers.handlers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import manager.TaskManager;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class PrioritizedHandler implements HttpHandler {

    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    private final TaskManager manager;
    private final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .create();


    public PrioritizedHandler(TaskManager manager) {
        this.manager = manager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String response = GSON.toJson(manager.getPrioritizedTasks());

        writeResponse(exchange, response, 200);
    }

    private void writeResponse(HttpExchange exchange,
                               String responseString,
                               int responseCode) throws IOException {
        try (OutputStream os = exchange.getResponseBody()) {
            exchange.sendResponseHeaders(responseCode, 0);
            os.write(responseString.getBytes(DEFAULT_CHARSET));
        }
        exchange.close();
    }
}
