package service;

import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.time.Instant;
import java.util.*;

public abstract class TasksManagerTest<T extends TaskManager> {
    protected T manager;

    public abstract T createManager() throws IOException, InterruptedException;

    @BeforeEach
    void getManager() throws IOException, InterruptedException {
        this.manager = createManager();
    }

    protected Task addTask() {
        return new Task("Title", "Description", Instant.now(), 0);
    }

    protected Epic addEpic() {
        return new Epic("Title", "Description", TypeTask.EPIC);
    }

    protected Subtask addSubTask(Epic epic) {
        return new Subtask("Title", "Description", Instant.now(), 0, epic.getId());
    }


    @Test
    public void createNewTaskTest() {
        Task task = addTask();
        this.manager.createNewTask(task);
        List<Task> tasks = this.manager.getPriorityTasks();
        assertNotNull(task.getStatus());
        assertEquals(Status.NEW, task.getStatus());
        assertEquals(List.of(task), tasks);
    }

    @Test
    public void createNullTaskTest() {
        this.manager.createNewTask(null);
        assertEquals(Collections.EMPTY_LIST, this.manager.getTasks());
    }

    @Test
    public void getNullTaskTest() {
        assertNull(this.manager.getTask(1));
    }

    @Test
    public void getTaskTest() {
        Task task = addTask();
        this.manager.createNewTask(task);
        assertEquals(task, this.manager.getTask(1));
    }

    @Test
    public void getTaskMinusOneTest() {
        Task task = addTask();
        this.manager.createNewTask(task);
        assertNull(this.manager.getTask(-1));
    }


    @Test
    public void createNewSubtaskTest() {
        Epic epic = addEpic();
        this.manager.createNewEpic(epic);
        Subtask SubTask = addSubTask(epic);
        this.manager.createNewSubtask(SubTask);
        List<Subtask> SubTasks = this.manager.getSubtasks();
        assertNotNull(SubTask.getStatus());
        assertEquals(epic.getId(), SubTask.getIndexEpic());
        assertEquals(Status.NEW, SubTask.getStatus());
        assertEquals(List.of(SubTask), SubTasks);
        assertEquals(List.of(SubTask.getId()), epic.getSubtasksIds());

    }

    @Test
    public void getNullSubtaskTest() {
        assertNull(this.manager.getSubtask(1));
    }

    @Test
    public void getSubtaskTest() {
        Epic epic = addEpic();
        Subtask subtask = addSubTask(epic);
        this.manager.createNewEpic(epic);
        this.manager.createNewSubtask(subtask);
        assertEquals(subtask, this.manager.getSubtask(2));
    }

    @Test
    public void getSubtaskMinusOneTest() {
        Epic epic = addEpic();
        Subtask subtask = addSubTask(epic);
        this.manager.createNewEpic(epic);
        this.manager.createNewSubtask(subtask);
        assertNull(this.manager.getTask(-1));
    }

    @Test
    public void createNewEpicTest() {
        Epic epic = addEpic();
        this.manager.createNewEpic(epic);
        List<Epic> epics = this.manager.getEpics();
        assertNotNull(epic.getStatus());
        assertEquals(Status.NEW, epic.getStatus());
        assertEquals(Collections.EMPTY_LIST, epic.getSubtasksIds());
        assertEquals(List.of(epic), epics);
    }

    @Test
    public void checkStatusEpicNewTest() {
        Epic epic = addEpic();
        Subtask subtask1 = addSubTask(epic);
        Subtask subtask2 = addSubTask(epic);
        this.manager.createNewEpic(epic);
        this.manager.createNewSubtask(subtask1);
        this.manager.createNewSubtask(subtask2);
        assertEquals(Status.NEW, this.manager.getEpic(1).getStatus());
    }

    @Test
    public void checkStatusEpicDoneTest() {
        Epic epic = addEpic();
        this.manager.createNewEpic(epic);
        Subtask subtask1 = addSubTask(epic);
        Subtask subtask2 = addSubTask(epic);
        this.manager.createNewSubtask(subtask1);
        this.manager.createNewSubtask(subtask2);
        this.manager.changeStatusSubtask(2, Status.DONE);
        this.manager.changeStatusSubtask(3, Status.DONE);
        assertEquals(Status.DONE, this.manager.getEpic(1).getStatus());
    }


    @Test
    public void checkStatusEpicNewAndDoneTest() {
        Epic epic = addEpic();
        this.manager.createNewEpic(epic);
        Subtask subtask1 = addSubTask(epic);
        Subtask subtask2 = addSubTask(epic);
        this.manager.createNewSubtask(subtask1);
        this.manager.createNewSubtask(subtask2);
        this.manager.changeStatusSubtask(2, Status.DONE);
        assertEquals(Status.IN_PROGRESS, this.manager.getEpic(1).getStatus());

    }

    @Test
    public void checkStatusEpicInProgressTest() {
        Epic epic = addEpic();
        this.manager.createNewEpic(epic);
        Subtask subtask1 = addSubTask(epic);
        Subtask subtask2 = addSubTask(epic);
        this.manager.createNewSubtask(subtask1);
        this.manager.createNewSubtask(subtask2);
        this.manager.changeStatusSubtask(2, Status.IN_PROGRESS);
        this.manager.changeStatusSubtask(3, Status.IN_PROGRESS);
        assertEquals(Status.IN_PROGRESS, this.manager.getEpic(1).getStatus());
    }

    @Test
    public void checkStatusEpicNullSubtaskTest() {
        Epic epic = addEpic();
        this.manager.createNewEpic(epic);
        assertEquals(Status.NEW, this.manager.getEpic(1).getStatus());
    }

    @Test
    void getTaskByIdTest() {
        Task task = addTask();
        this.manager.createNewTask(task);
        assertEquals(task, this.manager.getTaskById(1));
    }

    @Test
    void getTaskByIdNegativeTest() {
        assertNull(this.manager.getTaskById(-1));
    }

    @Test
    void getTaskByIdNullTest() {
        Task task = this.manager.getTaskById(0);
        assertNull(task, "Task not found");
    }


    @Test
    public void changeStatusTaskTest() {
        Task task = addTask();
        this.manager.createNewTask(task);
        assertEquals(Status.NEW, this.manager.getTask(1).getStatus());
        this.manager.changeStatusTask(1, Status.IN_PROGRESS);
        assertEquals(Status.IN_PROGRESS, this.manager.getTask(1).getStatus());
        this.manager.changeStatusTask(1, Status.DONE);
        assertEquals(Status.DONE, this.manager.getTask(1).getStatus());
    }


    @Test
    public void changeStatusSubtaskTest() {
        Epic epic = addEpic();
        this.manager.createNewEpic(epic);
        Subtask subTask = addSubTask(epic);
        this.manager.createNewSubtask(subTask);
        assertEquals(Status.NEW, this.manager.getSubtask(subTask.getId()).getStatus());
        this.manager.changeStatusSubtask(2, Status.IN_PROGRESS);
        assertEquals(Status.IN_PROGRESS, this.manager.getSubtask(subTask.getId()).getStatus());
        this.manager.changeStatusSubtask(2, Status.DONE);
        assertEquals(Status.DONE, this.manager.getSubtask(subTask.getId()).getStatus());
    }

    @Test
    public void notUpdateTaskIfNullTest() {
        Task task = addTask();
        this.manager.createNewTask(task);
        this.manager.updateTask(null);
        assertEquals(task, this.manager.getTaskById(task.getId()));
    }

    @Test
    void getPrioritizedTasksTest() {
        Epic epic = addEpic();
        Subtask subtask = addSubTask(epic);
        Task task = addTask();
        this.manager.createNewTask(task);
        this.manager.createNewEpic(epic);
        this.manager.createNewSubtask(subtask);
        TreeSet<Task> set = new TreeSet<>(Comparator.comparing(Task::getStartTime));
        set.add(this.manager.getTask(1));
        set.add(this.manager.getSubtask(3));
        List<Task> list = new ArrayList<>(set);
        assertEquals(list, this.manager.getPriorityTasks());

    }

    @Test
    public void removeAllTaskTest() {
        this.manager.createNewTask(addTask());
        this.manager.createNewTask(addTask());
        this.manager.createNewTask(addTask());
        this.manager.removeAllTask();
        assertEquals(Collections.EMPTY_LIST, this.manager.getTasks());
    }

    @Test
    public void removeAllSubtaskTest() {
        Epic epic = addEpic();
        this.manager.createNewEpic(epic);
        this.manager.createNewSubtask(addSubTask(epic));
        this.manager.createNewSubtask(addSubTask(epic));
        this.manager.createNewSubtask(addSubTask(epic));
        this.manager.removeAllSubtask();
        assertEquals(Collections.EMPTY_LIST, this.manager.getSubtasks());
    }

    @Test
    public void removeAllEpicTest() {
        Epic epic = addEpic();
        Epic epic1 = addEpic();
        Epic epic2 = addEpic();
        this.manager.createNewEpic(epic);
        this.manager.createNewEpic(epic1);
        this.manager.createNewEpic(epic2);
        this.manager.removeAllEpic();
        assertEquals(Collections.EMPTY_LIST, this.manager.getSubtasks());
    }

    @Test
    void getAllSubtaskByEpicTest() {
        Epic epic = addEpic();
        this.manager.createNewEpic(epic);
        Subtask subtask = addSubTask(epic);
        Subtask subtask1 = addSubTask(epic);
        subtask1.setStartTime(Instant.now().plusSeconds(10));
        Subtask subtask2 = addSubTask(epic);
        subtask2.setStartTime(Instant.now().plusSeconds(10));
        this.manager.createNewSubtask(subtask);
        this.manager.createNewSubtask(subtask1);
        this.manager.createNewSubtask(subtask2);
        List<Subtask> list = new ArrayList<>(List.of(this.manager.getSubtask(2), this.manager.getSubtask(3), this.manager.getSubtask(4)));
        assertEquals(list, this.manager.getAllSubtaskByEpic(1));

    }

    @Test
    void getAllSubtaskByEpicNullTest() {
        this.manager.createNewEpic(addEpic());
        assertEquals(Collections.EMPTY_LIST, this.manager.getAllSubtaskByEpic(1));
    }

    @Test
    void getAllSubtaskByEpicNegativeTest() {
        assertNull(this.manager.getAllSubtaskByEpic(-1));
    }

    @Test
    void getTasksTest() {
        Task task = addTask();
        Task task1 = addTask();
        Task task2 = addTask();
        this.manager.createNewTask(task);
        this.manager.createNewTask(task1);
        this.manager.createNewTask(task2);
        List<Task> list = new ArrayList<>(List.of(task, task1, task2));
        assertEquals(list, this.manager.getTasks());
    }

    @Test
    void getTasksNullTest() {
        assertEquals(Collections.EMPTY_LIST, this.manager.getTasks());
    }

    @Test
    void getSubtasksTest() {
        Epic epic = addEpic();
        this.manager.createNewEpic(epic);
        Subtask subtask = addSubTask(epic);
        Subtask subtask1 = addSubTask(epic);
        this.manager.createNewSubtask(subtask);
        this.manager.createNewSubtask(subtask1);
        List<Task> list = new ArrayList<>(List.of(subtask, subtask1));
        assertEquals(list, this.manager.getSubtasks());
    }

    @Test
    void getSubtasksNullTest() {
        assertEquals(Collections.EMPTY_LIST, this.manager.getSubtasks());
    }

    @Test
    void getEpicsTest() {
        Epic epic = addEpic();
        this.manager.createNewEpic(epic);
        assertEquals(List.of(epic), this.manager.getEpics());
    }

    @Test
    void getEpicsNullTest() {
        assertEquals(Collections.EMPTY_LIST, this.manager.getEpics());
    }

    @Test
    void updateTaskTest() {
        Task task = addTask();
        this.manager.createNewTask(task);
        task.setStatus(Status.IN_PROGRESS);
        this.manager.updateTask(task);
        assertEquals(task, this.manager.getTask(1));
    }

    @Test
    void updateSubtaskTest() {
        Epic epic = addEpic();
        this.manager.createNewEpic(epic);
        Subtask subtask = addSubTask(epic);
        this.manager.createNewSubtask(subtask);
        subtask.setStatus(Status.IN_PROGRESS);
        this.manager.updateTask(subtask);
        assertEquals(subtask, this.manager.getSubtask(2));
    }

    @Test
    void updateEpicTest() {
        Epic epic = addEpic();
        this.manager.createNewEpic(epic);
        epic.setName("new name");
        this.manager.updateTask(epic);
        assertEquals(epic, this.manager.getEpic(1));
    }

    @Test
    void deleteSubtaskTest() {
        Epic epic = addEpic();
        this.manager.createNewEpic(epic);
        Subtask subtask = addSubTask(epic);
        this.manager.createNewSubtask(subtask);
        assertEquals(List.of(subtask), this.manager.getSubtasks());
        this.manager.deleteSubtask(2);
        assertEquals(Collections.EMPTY_LIST, this.manager.getSubtasks());
    }

    @Test
    void deleteSubtaskNullOrNegativeTest() {
        Epic epic = addEpic();
        this.manager.createNewEpic(epic);
        this.manager.deleteSubtask(2);
        this.manager.deleteSubtask(-1);
        assertEquals(Collections.EMPTY_LIST, this.manager.getSubtasks());
    }

    @Test
    void deleteTaskTest() {
        Task task = addTask();
        this.manager.createNewTask(task);
        assertEquals(List.of(task), this.manager.getTasks());
        this.manager.deleteTask(1);
        assertEquals(Collections.EMPTY_LIST, this.manager.getTasks());
    }

    @Test
    void deleteTaskNullOrNegativeTest() {
        this.manager.deleteTask(1);
        this.manager.deleteTask(-1);
        assertEquals(Collections.EMPTY_LIST, this.manager.getTasks());
    }

    @Test
    void deleteEpicTest() {
        Epic epic = addEpic();
        this.manager.createNewEpic(epic);
        assertEquals(List.of(epic), this.manager.getEpics());
        this.manager.deleteEpic(1);
        assertEquals(Collections.EMPTY_LIST, this.manager.getEpics());
    }

    @Test
    void deleteEpicNullOrNegativeTest() {
        this.manager.deleteEpic(1);
        assertEquals(Collections.EMPTY_LIST, this.manager.getEpics());
    }

    @Test
    void getHistoryTest() {
        Task task = addTask();
        task.setStartTime(Instant.now().plusSeconds(10));
        Task task1 = addTask();
        task1.setStartTime(Instant.now().plusSeconds(10));
        Task task2 = addTask();
        task2.setStartTime(Instant.now().plusSeconds(10));
        this.manager.createNewTask(task);
        this.manager.createNewTask(task1);
        this.manager.createNewTask(task2);
        this.manager.getTask(1);
        this.manager.getTask(2);
        this.manager.getTask(3);
        this.manager.getTask(3);
        assertEquals(List.of(task, task1, task2), this.manager.getHistory());

    }
}
