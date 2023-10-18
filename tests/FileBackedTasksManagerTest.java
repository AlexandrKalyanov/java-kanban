import model.Epic;
import model.Task;
import model.TypeTask;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.FileBackedTasksManager;
import service.Managers;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import java.time.Instant;
import java.util.Collections;



import static org.junit.jupiter.api.Assertions.*;


public class FileBackedTasksManagerTest extends TasksManagerTest<FileBackedTasksManager> {

    File file = new File("test_file.csv");

    @Override
    public FileBackedTasksManager createManager() {
        manager = Managers.BackedTasksManager(this.file);
        return manager;
    }
@BeforeEach
public void createFile(){
        try{
            Files.createFile(Path.of("test_file.csv"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
}
@AfterEach
    public void afterEach() {
        try {
            Files.delete(Path.of("test_file.csv"));
        } catch (IOException exception) {
            System.out.println(exception.getMessage());
        }
    }

    @Test
    public void saveAndLoadTest() {
        Task task = new Task("Task 1","Descr",Instant.now(),20);
        manager.createNewTask(task);
        Epic epic = new Epic("Epic 1","Descr",TypeTask.EPIC);
        manager.createNewEpic(epic);
       // manager.getTask(1);
        FileBackedTasksManager fileManager = manager.loadFromFile(this.file);
        assertEquals(manager.getTasks(), fileManager.getTasks());
        assertEquals(manager.getEpics(), fileManager.getEpics());
    }

    @Test
    public void saveAndLoadEmptyTasksEpicsSubtasksTest() {
        FileBackedTasksManager fileManager = new FileBackedTasksManager(file);
        FileBackedTasksManager newManager = fileManager.loadFromFile(file);
        assertEquals(Collections.EMPTY_LIST, newManager.getTasks());
        assertEquals(Collections.EMPTY_LIST, newManager.getEpics());
        assertEquals(Collections.EMPTY_LIST, newManager.getSubtasks());
    }

    @Test
    public void saveAndLoadEmptyHistoryTest() {
        FileBackedTasksManager fileManager = new FileBackedTasksManager(file);
        fileManager.loadFromFile(file);
        Assertions.assertEquals(Collections.EMPTY_LIST, manager.getHistory());
    }
}