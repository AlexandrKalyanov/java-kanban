import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;
import service.TaskManager;

public class Main {

    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();
        taskManager.createNewEpic(new Epic("epic", "desc Epic"));
        taskManager.createNewSubtask(new Subtask("subtask", "descr subtask", Status.NEW, 1));
        taskManager.createNewSubtask(new Subtask("subtask2", "descr subtask2", Status.NEW, 1));
        System.out.println(taskManager.getTaskById(1));
        taskManager.changeStatusSubtask(2, Status.IN_PROGRESS);
        System.out.println(taskManager.getTaskById(1));
        taskManager.deleteSubtask(2);
        System.out.println(taskManager.getTaskById(1));
        taskManager.changeStatusSubtask(3, Status.DONE);
        System.out.println(taskManager.getTaskById(1));
        taskManager.createNewSubtask(new Subtask("subtask", "descr subtask", Status.IN_PROGRESS, 1));
        System.out.println(taskManager.getTaskById(1));
        taskManager.createNewTask(new Task("task", "descr subtask", Status.NEW));
        System.out.println(taskManager.getAllTasks());
        taskManager.updateTask(new Task("name", "test", Status.IN_PROGRESS, 5));
        taskManager.updateSubtask(new Subtask("SubTask_new", "descr", Status.IN_PROGRESS, 2, 1));
        System.out.println(taskManager.getTaskById(1));
        taskManager.updateEpic(new Epic("Epic_NEW","descr Epic_NEW",1));
        System.out.println(taskManager.getEpics());
        System.out.println(taskManager.getAllTasks());
        taskManager.removeAllSubtask();
        System.out.println(taskManager.getAllTasks());

    }
}
