package Files;

import model.*;
import service.HistoryManager;

import java.util.ArrayList;
import java.util.List;

public class CSVFormatHandler {

    private static final String DELIMITER = ",";

    public String toString(Task task) {
        String result = task.getId() + DELIMITER +
                task.getTypeTask() + DELIMITER +
                task.getName() + DELIMITER +
                task.getStatus() + DELIMITER +
                task.getDescription() + DELIMITER;
        if (task.getTypeTask() == TypeTask.SUBTASK) {
            result = result + ((Subtask) task).getIndexEpic();
        }
        return result;
    }

    public Task taskFromString(String value) {
        String[] parts = value.split(",");
        String id = String.valueOf(Integer.parseInt(parts[0]));
        String taskType = parts[1];
        String name = parts[2];
        String status = parts[3];
        String description = parts[4];

        Task task = new Task(name, description, Status.valueOf(status), TypeTask.valueOf(taskType));
        task.setId(Integer.parseInt(id));
        return task;
    }

    public Epic epicFromString(String value) {
        String[] parts = value.split(",");
        String id = String.valueOf(Integer.parseInt(parts[0]));
        String name = parts[2];
        String status = parts[3];
        String description = parts[4];


        Epic epic = new Epic(name, description, Integer.parseInt(id), TypeTask.EPIC);
        epic.setStatus(Status.valueOf(status));
        return epic;
    }

    public Subtask subTaskFromString(String value) {
        String[] parts = value.split(",");
        String id = String.valueOf(Integer.parseInt(parts[0]));
        String name = parts[2];
        String status = parts[3];
        String description = parts[4];
        String epicId = parts[5];

        Subtask subTask = new Subtask(name, description, Status.valueOf(status), Integer.parseInt(id), Integer.parseInt(epicId), TypeTask.SUBTASK);
        subTask.setStatus(Status.valueOf(status));
        return subTask;
    }

    public String historyToString(HistoryManager manager) {
        List<String> result = new ArrayList<>();

        for (Task task : manager.getHistory()) {
            result.add(String.valueOf(task.getId()));
        }
        return String.join(DELIMITER, result);
    }


    public String getHeader() {
        return "id,type,name,status,description,epic";
    }
}
