import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import service.TaskManager;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.time.Instant;
import java.util.*;

public abstract class TasksManagerTest<T extends TaskManager> {
    private final Map<Integer, Task> emptyMap = new HashMap<>();
    private final List<Task> emptyList = new ArrayList<>();
    protected T manager;

    public abstract T createManager() throws IOException, InterruptedException;

    @BeforeEach
    void getManager() throws IOException, InterruptedException {
        manager = createManager();
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
        manager.createNewTask(task);
        List<Task> tasks = manager.getPrioritizedTasks();
        assertNotNull(task.getStatus());
        assertEquals(Status.NEW, task.getStatus());
        assertEquals(List.of(task), tasks);
    }

    @Test
    public void createNullTaskTest() {
        Task task = null;
        manager.createNewTask(task);
        assertNull(task);
    }

    @Test
    public void getNullTaskTest() {
        assertNull(manager.getTask(1));
    }

    @Test
    public void getTaskTest() {
        Task task = addTask();
        manager.createNewTask(task);
        assertEquals(task, manager.getTask(1));
    }

    @Test
    public void getTaskMinusOneTest() {
        Task task = addTask();
        manager.createNewTask(task);
        assertNull(manager.getTask(-1));
    }


    @Test
    public void createNewSubtaskTest() {
        Epic epic = addEpic();
        manager.createNewEpic(epic);
        Subtask SubTask = addSubTask(epic);
        manager.createNewSubtask(SubTask);
        List<Subtask> SubTasks = manager.getSubtasks();
        assertNotNull(SubTask.getStatus());
        assertEquals(epic.getId(), SubTask.getIndexEpic());
        assertEquals(Status.NEW, SubTask.getStatus());
        assertEquals(List.of(SubTask), SubTasks);
        assertEquals(List.of(SubTask.getId()), epic.getSubtasksIds());

    }

    @Test
    public void getNullSubtaskTest() {
        assertNull(manager.getSubtask(1));
    }

    @Test
    public void getSubtaskTest() {
        Epic epic = addEpic();
        Subtask subtask = addSubTask(epic);
        manager.createNewEpic(epic);
        manager.createNewSubtask(subtask);
        assertEquals(subtask, manager.getSubtask(2));
    }

    @Test
    public void getSubtaskMinusOneTest() {
        Epic epic = addEpic();
        Subtask subtask = addSubTask(epic);
        manager.createNewEpic(epic);
        manager.createNewSubtask(subtask);
        assertNull(manager.getTask(-1));
    }

    @Test
    public void createNewEpicTest() {
        Epic epic = addEpic();
        manager.createNewEpic(epic);
        List<Epic> epics = manager.getEpics();
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
        manager.createNewEpic(epic);
        manager.createNewSubtask(subtask1);
        manager.createNewSubtask(subtask2);
        assertEquals(Status.NEW, manager.getEpic(1).getStatus());
    }

    @Test
    public void checkStatusEpicDoneTest() {
        Epic epic = addEpic();
        manager.createNewEpic(epic);
        Subtask subtask1 = addSubTask(epic);
        Subtask subtask2 = addSubTask(epic);
        manager.createNewSubtask(subtask1);
        manager.createNewSubtask(subtask2);
        manager.changeStatusSubtask(2, Status.DONE);
        manager.changeStatusSubtask(3, Status.DONE);
        assertEquals(Status.DONE, manager.getEpic(1).getStatus());
    }

    @Test
    public void checkStatusEpicNewAndDoneTest() {
        Epic epic = addEpic();
        manager.createNewEpic(epic);
        Subtask subtask1 = addSubTask(epic);
        Subtask subtask2 = addSubTask(epic);
        manager.createNewSubtask(subtask1);
        manager.createNewSubtask(subtask2);
        manager.changeStatusSubtask(2, Status.DONE);
        assertEquals(Status.IN_PROGRESS, manager.getEpic(1).getStatus());
    }

    @Test
    public void checkStatusEpicInProgressTest() {
        Epic epic = addEpic();
        manager.createNewEpic(epic);
        Subtask subtask1 = addSubTask(epic);
        Subtask subtask2 = addSubTask(epic);
        manager.createNewSubtask(subtask1);
        manager.createNewSubtask(subtask2);
        manager.changeStatusSubtask(2, Status.IN_PROGRESS);
        manager.changeStatusSubtask(3, Status.IN_PROGRESS);
        assertEquals(Status.IN_PROGRESS, manager.getEpic(1).getStatus());
    }

    @Test
    public void checkStatusEpicNullSubtaskTest() {
        Epic epic = addEpic();
        manager.createNewEpic(epic);
        assertEquals(Status.NEW, manager.getEpic(1).getStatus());
    }

    @Test
    void getTaskByIdNullTest() {
        Task task = manager.getTaskById(0);
        assertNull(task, "Task not found");
    }


    @Test
    public void changeStatusTaskToInProgressTest() {
        Task task = addTask();
        manager.createNewTask(task);
        task.setStatus(Status.IN_PROGRESS);
        manager.updateTask(task);
        assertEquals(Status.IN_PROGRESS, manager.getTaskById(task.getId()).getStatus());
    }


    @Test
    public void changeStatusSubtaskToInProgressTest() {
        Epic epic = addEpic();
        manager.createNewEpic(epic);
        Subtask subTask = addSubTask(epic);
        manager.createNewSubtask(subTask);
        subTask.setStatus(Status.IN_PROGRESS);
        manager.updateSubtask(subTask);
        assertEquals(Status.IN_PROGRESS, manager.getSubtask(subTask.getId()).getStatus());
        assertEquals(Status.IN_PROGRESS, manager.getEpic(epic.getId()).getStatus());
    }

    @Test
    public void changeStatusTaskToInDoneTest() {
        Task task = addTask();
        manager.createNewTask(task);
        task.setStatus(Status.DONE);
        manager.updateTask(task);
        assertEquals(Status.DONE, manager.getTaskById(task.getId()).getStatus());
    }

    @Test
    public void changeStatusStatusToInDoneTest() {
        Epic epic = addEpic();
        manager.createNewEpic(epic);
        epic.setStatus(Status.DONE);
        manager.updateEpic(epic);
        assertEquals(Status.DONE, manager.getEpic(epic.getId()).getStatus());
    }


    @Test
    public void changeStatusSubtaskToInDoneTest() {
        Epic epic = addEpic();
        manager.createNewEpic(epic);
        Subtask SubTask = addSubTask(epic);
        manager.createNewSubtask(SubTask);
        SubTask.setStatus(Status.DONE);
        manager.updateSubtask(SubTask);
        assertEquals(Status.DONE, manager.getSubtask(SubTask.getId()).getStatus());
        assertEquals(Status.DONE, manager.getEpic(epic.getId()).getStatus());
    }


    @Test
    public void notUpdateTaskIfNullTest() {
        Task task = addTask();
        manager.createNewTask(task);
        manager.updateTask(null);
        assertEquals(task, manager.getTaskById(task.getId()));
    }
}
/*
    @Test
    public void notUpdateEpicIfNullTest() {
        Epic epic = addEpic();
        manager.addEpic(epic);
        manager.updateEpic(null);
        assertEquals(epic, manager.getEpicById(epic.getId()));
    }

    @Test
    public void notUpdateSubTaskIfNullTest() {
        Epic epic = addEpic();
        manager.addEpic(epic);
        SubTask SubTask = addSubTask(epic);
        manager.addSubTask(SubTask);
        manager.updateSubTask(null);
        assertEquals(SubTask, manager.getSubTaskById(SubTask.getId()));
    }

    @Test
    public void deleteAllTasksTest() {
        Task task = addTask();
        manager.addTask(task);
        manager.deleteAllTasks();
        assertEquals(Collections.EMPTY_LIST, manager.getAllTasks());
    }

    @Test
    public void deleteAllEpicsTest() {
        Epic epic = addEpic();
        manager.addEpic(epic);
        manager.deleteAllEpics();
        assertEquals(Collections.EMPTY_LIST, manager.getAllEpics());
    }

    @Test
    public void deleteAllSubTasksTest() {
        Epic epic = addEpic();
        manager.addEpic(epic);
        SubTask SubTask = addSubTask(epic);
        manager.addSubTask(SubTask);
        manager.deleteAllSubtasks();
        assertTrue(epic.getSubtaskIds().isEmpty());
        assertTrue(manager.getAllSubtasks().isEmpty());
    }

    @Test
    public void deleteAllSubTasksByEpicTest() {
        Epic epic = addEpic();
        manager.addEpic(epic);
        SubTask SubTask = addSubTask(epic);
        manager.addSubTask(SubTask);
        manager.deleteAllSubtasksByEpic(epic);
        assertTrue(epic.getSubtaskIds().isEmpty());
        assertTrue(manager.getAllSubtasks().isEmpty());
    }

    @Test
    public void deleteTaskByIdTest() {
        Task task = addTask();
        manager.addTask(task);
        manager.deleteTaskById(task.getId());
        assertEquals(Collections.EMPTY_LIST, manager.getAllTasks());
    }

    @Test
    public void deleteEpicById() {
        Epic epic = addEpic();
        manager.addEpic(epic);
        manager.deleteEpicById(epic.getId());
        assertEquals(Collections.EMPTY_LIST, manager.getAllEpics());
    }

    @Test
    public void notDeleteTaskIfBadIdTest() {
        Task task = addTask();
        manager.addTask(task);
        manager.deleteTaskById(-1);
        assertEquals(List.of(task), manager.getAllTasks());
    }

    @Test
    public void notDeleteEpicIfBadIdTest() {
        Epic epic = addEpic();
        manager.addEpic(epic);
        manager.deleteEpicById(-1);
        assertEquals(List.of(epic), manager.getAllEpics());
    }

    @Test
    public void notDeleteSubTaskIfBadIdTest() {
        Epic epic = addEpic();
        manager.addEpic(epic);
        SubTask SubTask = addSubTask(epic);
        manager.addSubTask(SubTask);
        manager.deleteSubtaskById(-1);
        assertEquals(List.of(SubTask), manager.getAllSubtasks());
        assertEquals(List.of(SubTask.getId()), manager.getEpicById(epic.getId()).getSubtaskIds());
    }

    @Test
    public void doNothingIfTaskHashMapIsEmptyTest() {
        manager.deleteAllTasks();
        manager.deleteTaskById(-1);
        assertEquals(0, manager.getAllTasks().size());
    }

    @Test
    public void doNothingIfEpicHashMapIsEmptyTest() {
        manager.deleteAllEpics();
        manager.deleteEpicById(-1);
        assertTrue(manager.getAllEpics().isEmpty());
    }

    @Test
    public void doNothingIfSubTaskHashMapIsEmptyTest() {
        manager.deleteAllEpics();
        manager.deleteSubtaskById(-1);
        assertEquals(0, manager.getAllSubtasks().size());
    }

    @Test
    void returnEmptyListWhenGetSubTaskByEpicIdIsEmptyTest() {
        Epic epic = addEpic();
        manager.addEpic(epic);
        List<SubTask> SubTasks = manager.getAllSubtasksByEpicId(epic.getId());
        assertTrue(SubTasks.isEmpty());
    }

    @Test
    public void returnEmptyListTasksIfNoTasksTest() {
        assertTrue(manager.getAllTasks().isEmpty());
    }

    @Test
    public void returnEmptyListEpicsIfNoEpicsTest() {
        assertTrue(manager.getAllEpics().isEmpty());
    }

    @Test
    public void returnEmptyListSubTasksIfNoSubTasksTest() {
        assertTrue(manager.getAllSubtasks().isEmpty());
    }

    @Test
    public void returnNullIfTaskDoesNotExist() {
        assertNull(manager.getTaskById(-1));
    }

    @Test
    public void returnNullIfEpicDoesNotExist() {
        assertNull(manager.getEpicById(-1));
    }

    @Test
    public void returnNullIfSubtaskDoesNotExist() {
        assertNull(manager.getSubTaskById(-1));
    }

    @Test
    public void returnEmptyHistoryTest() {
        assertEquals(Collections.EMPTY_LIST, manager.getHistory());
    }

    @Test
    public void returnEmptyHistoryIfTasksNotExist() {
        manager.getTaskById(-1);
        manager.getSubTaskById(-1);
        manager.getEpicById(-1);
        assertTrue(manager.getHistory().isEmpty());
    }

    @Test
    public void returnHistoryTasksTest() {
        Epic epic = addEpic();
        manager.addEpic(epic);
        SubTask SubTask = addSubTask(epic);
        manager.addSubTask(SubTask);
        manager.getEpicById(epic.getId());
        manager.getSubTaskById(SubTask.getId());
        List<Task> list = manager.getHistory();
        assertEquals(2, list.size());
        assertTrue(list.contains(SubTask));
        assertTrue(list.contains(epic));
    }
}


*/
