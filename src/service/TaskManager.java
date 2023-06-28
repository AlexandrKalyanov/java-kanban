package service;

import model.*;

import java.util.*;

public class TaskManager {

    RepositoryTasks repositoryTasks = new RepositoryTasks();
    private int index;

    public TaskManager() {
        index = 0;

    }

    public void createNewTask(Task task) {
        this.index += 1;
        repositoryTasks.addNewTask(task, index);
    }

    public void createNewSubtask(Subtask subtask) {
        this.index += 1;
        repositoryTasks.addNewSubtask(subtask, index);
    }

    public void createNewEpic(Epic epic) {
        this.index += 1;
        repositoryTasks.addNewEpic(epic, index);
    }

    public void changeStatusTask(int index, Status status) {
        repositoryTasks.getTasks().get(index).setStatus(status);
    }

    public void changeStatusSubtask(int index, Status status) {
        repositoryTasks.getSubtasks().get(index).setStatus(status);
        int indexEpic = repositoryTasks.getSubtasks().get(index).getIndexEpic();
        Status epicStatus = repositoryTasks.getEpics().get(indexEpic).getStatus();
        // TODO: Необходимо реализоваь смену статуса у Эпика
    }

    public Map<Integer, Task> getAllTasks() {
        Map<Integer, Task> allTasks = new HashMap<>();
        allTasks.putAll(repositoryTasks.getEpics());
        allTasks.putAll(repositoryTasks.getSubtasks());
        allTasks.putAll(repositoryTasks.getTasks());
        return allTasks;
    }

    public Map<Integer, Task> getTasks() {
        return repositoryTasks.getTasks();
    }

    public Map<Integer, Subtask> getSubtasks() {
        return repositoryTasks.getSubtasks();
    }

    public Map<Integer, Epic> getEpics() {
        return repositoryTasks.getEpics();
    }
}

