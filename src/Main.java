import model.Epic;
import model.Status;
import model.Subtask;
import service.*;

public class Main {

    public static void main(String[] args) {
        ChoosingManager choosingManager = new ChoosingManager();
        TaskManager taskManager = choosingManager.getDefault();
        HistoryManager inMemoryHistoryManager = taskManager.getHistoryManager();
        taskManager.createNewEpic(new Epic("epic", "desc Epic"));
        taskManager.createNewSubtask(new Subtask("subtask", "descr subtask", Status.NEW, 1));
        taskManager.createNewSubtask(new Subtask("subtask2", "descr subtask2", Status.NEW, 1));
        taskManager.getSubtask(2);
        taskManager.getEpic(1);
        taskManager.getEpic(1);
        taskManager.getSubtask(3);
        taskManager.getEpic(1);
        taskManager.getEpic(1);
        taskManager.getEpic(1);
        taskManager.getEpic(1);
        taskManager.getEpic(1);
        taskManager.getEpic(1);
        System.out.println(inMemoryHistoryManager.getHistory());
        System.out.println(inMemoryHistoryManager.getHistory().size());

    }
}
