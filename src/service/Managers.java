package service;

import servers.KVTaskClient;

import java.io.File;

public final class Managers {

    private Managers() {
    }

    public static TaskManager getDefault(String url) {
        return new HttpTaskManager(new KVTaskClient(url));
    }

    public static InMemoryTaskManager getInMemoryTaskManager() {
        return new InMemoryTaskManager(getHistoryDefault());
    }

    public static HistoryManager getHistoryDefault() {
        return new InMemoryHistoryManager();
    }

    public static FileBackedTasksManager BackedTasksManager(File file) {
        return new FileBackedTasksManager(file);
    }
}