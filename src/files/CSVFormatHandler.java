package files;

import model.*;
import service.HistoryManager;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class CSVFormatHandler {

    private static final String DELIMITER = ",";
    public String toString(Task task) {
        String result = task.getId() + DELIMITER +
                task.getTypeTask() + DELIMITER +
                task.getName() + DELIMITER +
                task.getStatus() + DELIMITER +
                task.getDescription() + DELIMITER +
                task.getStartTime() + DELIMITER +
                task.getDuration() + DELIMITER;
        if (task.getTypeTask() == TypeTask.SUBTASK) {
            result += ((Subtask) task).getIndexEpic();
        }
        return result;
    }

    public Task taskFromString(String value) {
        String[] parts = value.split(",");
        String id = String.valueOf(Integer.parseInt(parts[0]));
        String name = parts[2];
        String status = parts[3];
        String description = parts[4];
        String startTime = parts[5];
        String duration = parts[6];
        Task task = new Task(name,description,Status.valueOf(status),Integer.parseInt(id),Instant.parse(startTime),Long.parseLong(duration));
        task.setId(Integer.parseInt(id));
        return task;
    }

    public Epic epicFromString(String value) {
        String[] parts = value.split(",");
        String id = String.valueOf(Integer.parseInt(parts[0]));
        String typeTask = parts[1];
        String name = parts[2];
        String status = parts[3];
        String description = parts[4];
        String startTime = parts[5];
        String duration = parts[6];
        Epic epic = new Epic(name,description,TypeTask.valueOf(typeTask),Integer.parseInt(id),Instant.parse(startTime),Long.parseLong(duration),Instant.parse(startTime).plusSeconds(Long.parseLong(duration)*60));
        epic.setStatus(Status.valueOf(status));
        return epic;
    }

    public Subtask subTaskFromString(String value) {
        String[] parts = value.split(",");
        String id = String.valueOf(Integer.parseInt(parts[0]));
        String name = parts[2];
        String status = parts[3];
        String description = parts[4];
        String startTime = parts[5];
        String duration = parts[6];
        String epicId = parts[7];
        return new Subtask(Integer.parseInt(id),name,Status.valueOf(status),description,Instant.parse(startTime),Long.parseLong(duration),Integer.parseInt(epicId));
    }

    public String historyToString(HistoryManager manager) {
        List<String> result = new ArrayList<>();

        for (Task task : manager.getHistory()) {
            result.add(String.valueOf(task.getId()));
        }
        return String.join(DELIMITER, result);
    }


    public String getHeader() {
        return "id,type,name,status,description,startTime,duration,epic";
    }
}
