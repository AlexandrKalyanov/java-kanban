import model.Epic;
import model.Subtask;
import model.Task;
import model.TypeTask;
import servers.HttpTaskServer;
import servers.KVServer;
import service.HttpTaskManager;
import service.Managers;

import java.io.IOException;
import java.time.Instant;

public class Main {

    public static void main(String[] args) throws IOException {
        KVServer kvServer = new KVServer();
        kvServer.start();

        HttpTaskManager httpManager = (HttpTaskManager) Managers.getDefault("http://localhost:8078");
        new HttpTaskServer(httpManager).start();

        httpManager.createNewTask(new Task("Task 1", "Descr", Instant.ofEpochMilli(1686603600000L), 10));
        httpManager.createNewTask(new Task("Task 2", "Descr", Instant.ofEpochMilli(3286603600000L), 10));

        Epic epic1 = new Epic("Epic 1", "Shopping", TypeTask.EPIC);
        httpManager.createNewEpic(epic1);
        httpManager.createNewSubtask(new Subtask("Subtask 1", "Buy milk", Instant.ofEpochMilli(1231313), 10, 3));
        httpManager.createNewSubtask(new Subtask("Subtask 1", "Buy milk", Instant.ofEpochMilli(66313103600000L), 10, 3));
        httpManager.createNewSubtask(new Subtask("Subtask 1", "Buy milk", Instant.ofEpochMilli(66303600000L), 10, 3));

        Epic epic2 = new Epic("Epic 2", "Shopping", TypeTask.EPIC);
        httpManager.createNewEpic(epic2);

        httpManager.getTask(5);
        httpManager.getEpic(3);
        httpManager.getSubtask(11);
        System.out.println(httpManager.getEpics());
        System.out.println(httpManager.getHistory());
        System.out.println(httpManager.getPriorityTasks());
        HttpTaskManager httpManager1 = (HttpTaskManager) Managers.getDefault("http://localhost:8078");
        httpManager1.loadFromServer();

        System.out.println(httpManager1.getEpics());
        System.out.println(httpManager1.getHistory());
        System.out.println(httpManager1.getPriorityTasks());
    }
}
