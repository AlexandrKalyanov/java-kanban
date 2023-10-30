package service;

import model.Epic;
import model.Subtask;
import model.Task;
import model.TypeTask;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import servers.KVServer;

import java.io.IOException;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HttpTaskManagerTest extends TasksManagerTest<HttpTaskManager> {
    private static KVServer server;
    private final Task task = new Task("Task 1", "Descr", Instant.ofEpochMilli(1685998800000L), 10);
    private final Epic epic = new Epic("Epic 1", "Descr", TypeTask.EPIC);
    private final Subtask subtask = new Subtask("Subtask 1", "descr", Instant.ofEpochMilli(168599880002200L), 10, 2);

    @Override
    public HttpTaskManager createManager() {
        return this.manager = (HttpTaskManager) Managers.getDefault("http://localhost:8078");
    }

    @BeforeAll
    static void startServer() throws IOException {
        server = new KVServer();
        server.start();
    }

    @Test
    void shouldLoadFromServer() {
        manager.createNewTask(task);
        manager.createNewEpic(epic);
        manager.createNewSubtask(subtask);
        manager.getTask(task.getId());
        manager = (HttpTaskManager) Managers.getDefault("http://localhost:8078");

        manager.loadFromServer();

        assertEquals(2, manager.getPriorityTasks().size());
        assertEquals(1, manager.getHistory().size());
        assertEquals(task.toStringFromFile(), manager.getTask(task.getId()).toStringFromFile());
        assertEquals(epic.toStringFromFile(), manager.getEpic(epic.getId()).toStringFromFile());
        assertEquals(subtask.toStringFromFile(), manager.getSubtask(subtask.getId()).toStringFromFile());
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }


}