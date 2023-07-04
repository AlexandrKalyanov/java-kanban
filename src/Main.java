import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;
import service.TaskManager;

public class Main {

    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();
        taskManager.createNewEpic(new Epic("epic", "desc Epic"));
        taskManager.createNewSubtask(new Subtask("subtask", "descr subtask", Status.NEW,1));
        taskManager.createNewSubtask(new Subtask("subtask2", "descr subtask2", Status.NEW,1));
        System.out.println(taskManager.getTaskById(1));
        taskManager.changeStatusSubtask(2,Status.IN_PROGRESS);
        System.out.println(taskManager.getTaskById(1));

        System.out.println(taskManager.getAllSubtaskByEpic(1));
        taskManager.changeStatusSubtask(2, Status.DONE);
        System.out.println(taskManager.getTaskById(1));
        taskManager.changeStatusSubtask(3, Status.DONE);
        System.out.println(taskManager.getTaskById(1));
//

    }
}
