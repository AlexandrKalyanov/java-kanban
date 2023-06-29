import model.Epic;
import model.Status;
import model.Subtask;
import service.TaskManager;

public class Main {

    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();
        /*taskManager.createNewTask(new Task("Задача 1", "description"));
        taskManager.createNewTask(new Task("Задача 2", "description"));
        taskManager.createNewTask(new Task("Задача 3", "description"));*/
        /* taskManager.changeStatusTask(1, Status.IN_PROGRESS);*/
        taskManager.createNewEpic(new Epic("epic", "desc Epic"));
        taskManager.createNewSubtask(new Subtask("subtask", "descr subtask", 1));
        taskManager.createNewSubtask(new Subtask("subtask2", "descr subtask2", 1));
        System.out.println(taskManager.getTaskById(1));
        taskManager.changeStatusSubtask(2, Status.IN_PROGRESS);
        System.out.println(taskManager.getTaskById(1));
        taskManager.changeStatusSubtask(2, Status.DONE);
        System.out.println(taskManager.getTaskById(1));
        taskManager.changeStatusSubtask(3, Status.DONE);
        System.out.println(taskManager.getTaskById(1));
//        taskManager.createNewEpic(new Epic("1","2"));
//        taskManager.createNewSubtask(new Subtask("subtask2","descr subtask2",7));
//        taskManager.getTaskById(1);
//        taskManager.updateEpic(new Epic("epic2","desc epic2"), 4);
//        taskManager.deleteTask(1);
//        taskManager.getAllTasks();
//        taskManager.getAllSubtaskByEpic()
//        taskManager.updateTask(1);
//        taskManager.getAllSubtaskByEpic(7);
//        System.out.println(taskManager.getTasks());
//        System.out.println(taskManager.getAllSubtaskByEpic(4));
//        System.out.println(taskManager.getTaskById(4));


    }
}
