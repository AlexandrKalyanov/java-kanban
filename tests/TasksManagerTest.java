import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.TaskManager;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.time.Instant;
import java.util.*;

public abstract class TasksManagerTest<T extends TaskManager> {
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
        manager.createNewTask(null);
        assertEquals(Collections.EMPTY_LIST, manager.getTasks());
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
    void getTaskByIdTest() {
        Task task = addTask();
        manager.createNewTask(task);
        assertEquals(task, manager.getTaskById(1));
    }

    @Test
    void getTaskByIdNegativeTest() {
        assertNull(manager.getTaskById(-1));
    }

    @Test
    void getTaskByIdNullTest() {
        Task task = manager.getTaskById(0);
        assertNull(task, "Task not found");
    }


    @Test
    public void changeStatusTaskTest() {
        Task task = addTask();
        manager.createNewTask(task);
        assertEquals(Status.NEW, manager.getTask(1).getStatus());
        manager.changeStatusTask(1, Status.IN_PROGRESS);
        assertEquals(Status.IN_PROGRESS, manager.getTask(1).getStatus());
        manager.changeStatusTask(1, Status.DONE);
        assertEquals(Status.DONE, manager.getTask(1).getStatus());
    }


    @Test
    public void changeStatusSubtaskTest() {
        Epic epic = addEpic();
        manager.createNewEpic(epic);
        Subtask subTask = addSubTask(epic);
        manager.createNewSubtask(subTask);
        assertEquals(Status.NEW, manager.getSubtask(subTask.getId()).getStatus());
        manager.changeStatusSubtask(2, Status.IN_PROGRESS);
        assertEquals(Status.IN_PROGRESS, manager.getSubtask(subTask.getId()).getStatus());
        manager.changeStatusSubtask(2, Status.DONE);
        assertEquals(Status.DONE, manager.getSubtask(subTask.getId()).getStatus());
    }

    @Test
    public void notUpdateTaskIfNullTest() {
        Task task = addTask();
        manager.createNewTask(task);
        manager.updateTask(null);
        assertEquals(task, manager.getTaskById(task.getId()));
    }

    @Test
    void getPrioritizedTasksTest() {
        Epic epic = addEpic();
        Subtask subtask = addSubTask(epic);
        Task task = addTask();
        manager.createNewTask(task);
        manager.createNewEpic(epic);
        manager.createNewSubtask(subtask);
        List<Task> list = new ArrayList<>(List.of(manager.getTask(1), manager.getEpic(2), manager.getTaskById(3)));
        list.sort(Comparator.comparing(Task::getStartTime));
        assertEquals(list, manager.getPrioritizedTasks());

    }

    @Test
    public void removeAllTaskTest() {
        manager.createNewTask(addTask());
        manager.createNewTask(addTask());
        manager.createNewTask(addTask());
        manager.removeAllTask();
        assertEquals(Collections.EMPTY_LIST, manager.getTasks());
    }

    @Test
    public void removeAllSubtaskTest() {
        Epic epic = addEpic();
        manager.createNewEpic(epic);
        manager.createNewSubtask(addSubTask(epic));
        manager.createNewSubtask(addSubTask(epic));
        manager.createNewSubtask(addSubTask(epic));
        manager.removeAllSubtask();
        assertEquals(Collections.EMPTY_LIST, manager.getSubtasks());
    }

    @Test
    public void removeAllEpicTest() {
        Epic epic = addEpic();
        Epic epic1 = addEpic();
        Epic epic2 = addEpic();
        manager.createNewEpic(epic);
        manager.createNewEpic(epic1);
        manager.createNewEpic(epic2);
        manager.removeAllEpic();
        assertEquals(Collections.EMPTY_LIST, manager.getSubtasks());
    }

    @Test
    void getAllSubtaskByEpicTest() {
        Epic epic = addEpic();
        manager.createNewEpic(epic);
        Subtask subtask = addSubTask(epic);
        Subtask subtask1 = addSubTask(epic);
        Subtask subtask2 = addSubTask(epic);
        manager.createNewSubtask(subtask);
        manager.createNewSubtask(subtask1);
        manager.createNewSubtask(subtask2);
        List<Subtask> list = new ArrayList<>(List.of(manager.getSubtask(2), manager.getSubtask(3), manager.getSubtask(4)));
        assertEquals(list, manager.getAllSubtaskByEpic(1));

    }

    @Test
    void getAllSubtaskByEpicNullTest() {
        manager.createNewEpic(addEpic());
        assertEquals(Collections.EMPTY_LIST, manager.getAllSubtaskByEpic(1));
    }

    @Test
    void getAllSubtaskByEpicNegativeTest() {
        assertNull(manager.getAllSubtaskByEpic(-1));
    }

    @Test
    void getTasksTest() {
        Task task = addTask();
        Task task1 = addTask();
        Task task2 = addTask();
        manager.createNewTask(task);
        manager.createNewTask(task1);
        manager.createNewTask(task2);
        List<Task> list = new ArrayList<>(List.of(task, task1, task2));
        assertEquals(list, manager.getTasks());
    }

    @Test
    void getTasksNullTest() {
        assertEquals(Collections.EMPTY_LIST, manager.getTasks());
    }

    @Test
    void getSubtasksTest() {
        Epic epic = addEpic();
        manager.createNewEpic(epic);
        Subtask subtask = addSubTask(epic);
        Subtask subtask1 = addSubTask(epic);
        manager.createNewSubtask(subtask);
        manager.createNewSubtask(subtask1);
        List<Task> list = new ArrayList<>(List.of(subtask, subtask1));
        assertEquals(list, manager.getSubtasks());
    }

    @Test
    void getSubtasksNullTest() {
        assertEquals(Collections.EMPTY_LIST, manager.getSubtasks());
    }

    @Test
    void getEpicsTest() {
        Epic epic = addEpic();
        manager.createNewEpic(epic);
        assertEquals(List.of(epic), manager.getEpics());
    }

    @Test
    void getEpicsNullTest() {
        assertEquals(Collections.EMPTY_LIST, manager.getEpics());
    }

    @Test
    void updateTaskTest() {
        Task task = addTask();
        manager.createNewTask(task);
        task.setStatus(Status.IN_PROGRESS);
        manager.updateTask(task);
        assertEquals(task, manager.getTask(1));
    }

    @Test
    void updateSubtaskTest() {
        Epic epic = addEpic();
        manager.createNewEpic(epic);
        Subtask subtask = addSubTask(epic);
        manager.createNewSubtask(subtask);
        subtask.setStatus(Status.IN_PROGRESS);
        manager.updateTask(subtask);
        assertEquals(subtask, manager.getSubtask(2));
    }

    @Test
    void updateEpicTest() {
        Epic epic = addEpic();
        manager.createNewEpic(epic);
        epic.setName("new name");
        manager.updateTask(epic);
        assertEquals(epic, manager.getEpic(1));
    }

    @Test
    void deleteSubtaskTest() {
        Epic epic = addEpic();
        manager.createNewEpic(epic);
        Subtask subtask = addSubTask(epic);
        manager.createNewSubtask(subtask);
        assertEquals(List.of(subtask), manager.getSubtasks());
        manager.deleteSubtask(2);
        assertEquals(Collections.EMPTY_LIST, manager.getSubtasks());
    }

    @Test
    void deleteSubtaskNullOrNegativeTest() {
        Epic epic = addEpic();
        manager.createNewEpic(epic);
        manager.deleteSubtask(2);
        manager.deleteSubtask(-1);
        assertEquals(Collections.EMPTY_LIST, manager.getSubtasks());
    }

    @Test
    void deleteTaskTest() {
        Task task = addTask();
        manager.createNewTask(task);
        assertEquals(List.of(task), manager.getTasks());
        manager.deleteTask(1);
        assertEquals(Collections.EMPTY_LIST, manager.getTasks());
    }

    @Test
    void deleteTaskNullOrNegativeTest() {
        manager.deleteTask(1);
        manager.deleteTask(-1);
        assertEquals(Collections.EMPTY_LIST, manager.getTasks());
    }

    @Test
    void deleteEpicTest() {
        Epic epic = addEpic();
        manager.createNewEpic(epic);
        assertEquals(List.of(epic), manager.getEpics());
        manager.deleteEpic(1);
        assertEquals(Collections.EMPTY_LIST, manager.getEpics());
    }

    @Test
    void deleteEpicNullorNegativeTest() {
        manager.deleteEpic(1);
        assertEquals(Collections.EMPTY_LIST, manager.getEpics());
    }

    @Test
    void getHistoryTest() {
        Task task = addTask();
        Task task1 = addTask();
        Task task2 = addTask();
        manager.createNewTask(task);
        manager.createNewTask(task1);
        manager.createNewTask(task2);
        manager.getTask(1);
        manager.getTask(2);
        manager.getTask(3);
        manager.getTask(3);
        assertEquals(List.of(task, task1, task2), manager.getHistory());

    }
}
