package service;

import java.io.File;

public final class Managers {

    private Managers() {
    }


    public static InMemoryTaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public static HistoryManager getHistoryDefault() {
        return new InMemoryHistoryManager();
    }
    public static FileBackedTasksManager BackedTasksManager(File file){
        return new FileBackedTasksManager(file);
    }
}