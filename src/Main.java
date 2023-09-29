import model.*;
import service.*;

public class Main {

    public static void main(String[] args) {
        TaskManager taskManager = new InMemoryTaskManager();

        /*taskManager.createNewEpic(new Epic("epic", "desc Epic",TypeTask.EPIC));
        taskManager.createNewSubtask(new Subtask("subtask", "descr subtask", Status.NEW, 1,TypeTask.SUBTASK));
        taskManager.createNewSubtask(new Subtask("subtask2", "descr subtask2", Status.NEW, 1,TypeTask.SUBTASK));
        taskManager.createNewSubtask(new Subtask("subtask3", "descr subtask2", Status.NEW, 1,TypeTask.SUBTASK));
        taskManager.createNewTask(new Task("subtask3", "descr subtask2", Status.NEW, TypeTask.TASK));
        taskManager.getSubtask(2);
        taskManager.getEpic(1);
        taskManager.getEpic(1);
        taskManager.getSubtask(3);
        taskManager.getSubtask(4);
        taskManager.getSubtask(2);
        System.out.println(taskManager.getHistory());
        System.out.println(taskManager.getHistory().size());
        taskManager.deleteSubtask(4);
        taskManager.deleteSubtask(3);
        System.out.println(taskManager.getHistory());
        System.out.println(taskManager.getHistory().size());
        taskManager.deleteEpic(1);
        System.out.println(taskManager.getHistory());
        System.out.println(taskManager.getHistory().size());
        taskManager.getTask(5);
        System.out.println(taskManager.getHistory());
        System.out.println(taskManager.getHistory().size());
        taskManager.deleteTask(5);
        System.out.println(taskManager.getHistory());
        System.out.println(taskManager.getHistory().size());*/


    }
}
