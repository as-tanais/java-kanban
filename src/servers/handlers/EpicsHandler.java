package servers.handlers;

import com.sun.net.httpserver.HttpExchange;
import manager.TaskManager;

import java.io.IOException;

public class EpicsHandler extends TasksHandler{

    public EpicsHandler(TaskManager manager) {
        super(manager);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        getEpicsTaskHandle(exchange);
    }



    private void getEpicsTaskHandle(HttpExchange exchange) throws  IOException{


        String response = manager.getEpicTasks().toString();

        System.out.println(response);

        writeResponse(exchange, response, 200);
    }



}
