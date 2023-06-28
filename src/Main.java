import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;
import service.TaskManager;

public class Main {

    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();
        taskManager.createNewTask(new Task("Задача 1", "description"));
        taskManager.createNewTask(new Task("Задача 2", "description"));
        taskManager.createNewTask(new Task("Задача 3", "description"));
        taskManager.changeStatusTask(1, Status.IN_PROGRESS);
        taskManager.createNewEpic(new Epic("epic","desc Epic"));
        taskManager.createNewSubtask(new Subtask("subtask","descr subtask",4));
        System.out.println(taskManager.getTasks());
        System.out.println(taskManager.getEpics());
        System.out.println(taskManager.getSubtasks());


    }
}
