package service;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import model.Epic;
import model.Subtask;
import model.Task;
import servers.KVTaskClient;

import java.util.List;
import java.util.stream.Collectors;

public class HttpTaskManager extends FileBackedTasksManager {
    private final KVTaskClient taskClient;
    private static final Gson gson = new Gson();

    public HttpTaskManager(KVTaskClient taskClient) {
        this.taskClient = taskClient;
    }

    @Override
    public void save() {
        taskClient.save("task", gson.toJson(getTasksStorage().values()));
        taskClient.save("subtask", gson.toJson(getSubtasksStorage().values()));
        taskClient.save("epic", gson.toJson(getEpicsStorage().values()));
        taskClient.save("tasks", gson.toJson(getPriorityTasks()));
        List<Integer> historyIds = getHistory()
                .stream()
                .map(Task::getId)
                .collect(Collectors.toList());
        taskClient.save("history", gson.toJson(historyIds));
    }

    public void loadFromServer() {
        loadTasks("task");
        loadTasks("subtask");
        loadTasks("epic");
        loadHistory();
    }

    private void loadTasks(String key) {
        JsonElement jsonElement = JsonParser.parseString(taskClient.load(key));
        JsonArray jsonTasksArray = jsonElement.getAsJsonArray();
        int maxId = 0;
        for (JsonElement element : jsonTasksArray) {
            Task task;
            Epic epic;
            Subtask subtask;
            switch (key) {
                case "task":
                    task = gson.fromJson(element.getAsJsonObject(), Task.class);
                    getTasksStorage().put(task.getId(), task);
                    getPriorityTasks().add(task);
                    if (maxId < task.getId()) {
                        maxId = task.getId();
                    }
                    break;
                case "subtask":
                    subtask = gson.fromJson(element.getAsJsonObject(), Subtask.class);
                    int ide = subtask.getIndexEpic();
                    getSubtasksStorage().put(subtask.getId(), subtask);
                    getPriorityTasks().add(subtask);
                    if (maxId < subtask.getId()) {
                        maxId = subtask.getId();
                    }
                    if (getEpicsStorage().containsKey(ide)) {
                        Epic epicWithSubtask = getEpicsStorage().get(ide);
                        epicWithSubtask.addSubtask(subtask.getId());
                    } else {
                        System.out.println("The file is corrupted! It is not possible to find an Epic!");
                        break;
                    }

                case "epic":
                    epic = gson.fromJson(element.getAsJsonObject(), Epic.class);
                    getEpicsStorage().put(epic.getId(), epic);
                    getPriorityTasks().add(epic);
                    if (maxId < epic.getId()) {
                        maxId = epic.getId();
                    }
                    break;
                default:
                    System.out.println("Unable to upload tasks");
                    return;
            }

        }
        setIndex(maxId + 1);
    }

    private void loadHistory() {
        JsonElement jsonElement = JsonParser.parseString(taskClient.load("history"));
        JsonArray jsonHistoryArray = jsonElement.getAsJsonArray();
        for (JsonElement element : jsonHistoryArray) {
            int id = element.getAsInt();
            if (getTasksStorage().containsKey(id)) {
                getHistoryManager().add(getTasksStorage().get(id));
            } else if (getEpicsStorage().containsKey(id)) {
                getHistoryManager().add(getEpicsStorage().get(id));
            } else if (getSubtasksStorage().containsKey(id)) {
                getHistoryManager().add(getSubtasksStorage().get(id));
            }
        }
    }

}