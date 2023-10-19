package service;

import model.Epic;
import model.Task;
import model.TypeTask;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {
    InMemoryHistoryManager inMemoryHistoryManager = new InMemoryHistoryManager();

    protected Task addTask() {
        return new Task("Title", "Description", Instant.now(), 0);
    }

    protected Epic addEpic() {
        return new Epic("Title", "Description", TypeTask.EPIC);
    }


    @Test
    void getHistory() {
        Task task = addTask();
        task.setId(1);
        Epic epic = addEpic();
        epic.setId(2);
        List<Task> tasks = new ArrayList<>();
        tasks.add(task);
        tasks.add(epic);
        inMemoryHistoryManager.add(task);
        inMemoryHistoryManager.add(epic);
        List<Task> listCheck = inMemoryHistoryManager.getHistory();
        assertEquals(tasks, listCheck);

    }

    @Test
    void getNullHistory() {
        List<Task> listCheck = inMemoryHistoryManager.getHistory();
        assertEquals(Collections.EMPTY_LIST, listCheck);

    }

}