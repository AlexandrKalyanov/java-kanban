package service;

import model.*;

import java.util.*;

public class TaskManager {
    private final Map<Integer, Task> tasksStorage;
    private final Map<Integer, Subtask> subtasksStorage;
    private final Map<Integer, Epic> epicsStorage;
    private int index;

    public TaskManager() {
        this.tasksStorage = new HashMap<>();
        this.subtasksStorage = new HashMap<>();
        this.epicsStorage = new HashMap<>();
    }


    public void createNewTask(Task task) {
        task.setId(generateID());
        this.tasksStorage.put(this.index, task);

    }

    public void createNewSubtask(Subtask subtask) {
        subtask.setId(generateID());
        this.subtasksStorage.put(this.index, subtask);
        this.epicsStorage.get(subtask.getIndexEpic()).addSubtask(this.index);
        checkOrChangeEpicStatus(subtask.getIndexEpic());
    }

    public void createNewEpic(Epic epic) {
        epic.setId(generateID());
        this.epicsStorage.put(index, epic);
        epic.setStatus(Status.NEW);

    }


    public void changeStatusTask(int index, Status status) {
        this.tasksStorage.get(index).setStatus(status);
    }
    //Данного метода не должно существовать, весь расчет статусов эпиков производиться автоматически, при изменении подзадачи @Алексей Чеузов
    // Данный метод для смены статуса Сабтаски, а не для епика, считаю замечание не корректным
    public void changeStatusSubtask(int index, Status status) {
        subtasksStorage.get(index).setStatus(status);
        checkOrChangeEpicStatus(subtasksStorage.get(index).getIndexEpic());
    }

    public List<Task> getAllTasks() {
        List<Task> allTasks = new ArrayList<>();
        allTasks.addAll(this.tasksStorage.values());
        allTasks.addAll(this.subtasksStorage.values());
        allTasks.addAll(this.epicsStorage.values());
        return allTasks;
    }

    public void removeAllTask() {
        this.tasksStorage.clear();
    }

    public void removeAllSubtask() {
        this.subtasksStorage.clear();
        for (Epic epic : epicsStorage.values()) {
            epic.setStatus(Status.NEW);
        }
        // удаление сабтаск у епика
        for (Epic epic: epicsStorage.values()){
            epic.getSubtasksIds().clear();
        }

    }

    public void removeAllEpic() {
        this.epicsStorage.clear();
        this.subtasksStorage.clear();
    }

    public List<Subtask> getAllSubtaskByEpic(int epicIndex) {
        List<Subtask> allSubtasksByEpic = new ArrayList<>();
        List<Integer> subtasksByEpic = this.epicsStorage.get(epicIndex).getSubtasksIds();
        for (int subtasks : subtasksByEpic) {
            allSubtasksByEpic.add(this.subtasksStorage.get(subtasks));
        }
        return allSubtasksByEpic;
    }

    public List<Task> getTasks() {
        return new ArrayList<>(this.tasksStorage.values());
    }

    public List<Subtask> getSubtasks() {
        return new ArrayList<>(this.subtasksStorage.values());
    }

    public List<Epic> getEpics() {
        return new ArrayList<>(this.epicsStorage.values());
    }

    public Task getTaskById(int index) {
        Map<Integer, Task> allTasks = new HashMap<>();
        allTasks.putAll(epicsStorage);
        allTasks.putAll(subtasksStorage);
        allTasks.putAll(tasksStorage);
        return allTasks.get(index);
    }

    public void updateTask(Task task) {
        tasksStorage.put(task.getId(), task);
    }

    public void updateSubtask(Subtask subtask) {
        subtasksStorage.put(subtask.getId(), subtask);
        checkOrChangeEpicStatus(subtask.getIndexEpic());

    }

    public void updateEpic(Epic epic) {
        final Epic savedEpic = epicsStorage.get(epic.getId());
        savedEpic.setName(epic.getName());
        savedEpic.setDescription(epic.getDescription());
    }


    public void deleteSubtask(int index) {
        int indexEpic = this.subtasksStorage.get(index).getIndexEpic();
        this.epicsStorage.get(indexEpic).removeOneSubtask(index);
        this.subtasksStorage.remove(index);
        checkOrChangeEpicStatus(indexEpic);

    }

    public void deleteTask(int index) {
        this.tasksStorage.remove(index);
    }

    public void deleteEpic(int index) {
        subtasksStorage.values().removeIf(subtask -> subtask.getIndexEpic() == index);
        this.epicsStorage.remove(index);

    }
    private int generateID() {
        return ++index;
    }
    private void checkOrChangeEpicStatus(int indexEpic) {
        List<Integer> subtasksIds = epicsStorage.get(indexEpic).getSubtasksIds();
        int countSubtasks = subtasksIds.size();
        int doneCount = 0;
        int newCount = 0;
        for (int subtasksId : subtasksIds) {
            switch (subtasksStorage.get(subtasksId).getStatus()) {
                case NEW:
                    newCount++;
                    break;
                case DONE:
                    doneCount++;
                    break;
                default:
                    break;
            }
        }
        if (doneCount == countSubtasks) {
            epicsStorage.get(indexEpic).setStatus(Status.DONE);
        } else if (newCount == countSubtasks) {
            epicsStorage.get(indexEpic).setStatus(Status.NEW);

        } else epicsStorage.get(indexEpic).setStatus(Status.IN_PROGRESS);

    }
}


