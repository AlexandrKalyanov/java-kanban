import model.Task;
import service.TaskManager;

public class Main {

    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();
        Task newTask = taskManager.createTask();
        System.out.println(newTask);
        Task task2 = taskManager.createTask();
        System.out.println(task2);
    }


}
