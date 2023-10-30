package servers;

import com.sun.net.httpserver.HttpServer;
import service.InMemoryTaskManager;
import service.Managers;
import service.TaskManager;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpTaskServer {
    public static final int PORT = 8080;
    private final HttpServer server;

    public HttpTaskServer(TaskManager manager) throws IOException {
        server = HttpServer.create(new InetSocketAddress("localhost", PORT), 0);
        server.createContext("/tasks", new Handler(manager));
    }

    public void start() {
        System.out.println("Starting server on " + PORT + " port");
        server.start();

    }

    public void stop() {
        server.stop(10);
    }

    public static void main(String[] args) throws IOException {
        HttpTaskServer taskServer = new HttpTaskServer(new InMemoryTaskManager(Managers.getHistoryDefault()));
        taskServer.start();
        taskServer.stop();
    }
}