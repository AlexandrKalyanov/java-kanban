package service;

import model.*;

import java.util.*;

public class TaskManager {


    RepositoryTasks repositoryTasks = new RepositoryTasks();
    private int index;

    public TaskManager() {
        index = 0;
    }

    private int generateID() {
        return ++index;
    }

    public void createNewTask(Task task) {
        repositoryTasks.addNewTask(task, generateID());
    }

    public void createNewSubtask(Subtask subtask) {
        repositoryTasks.addNewSubtask(subtask, generateID());
        int epicIndex = subtask.getIndexEpic();
        repositoryTasks.getEpics().get(epicIndex).addSubtasks(index, subtask);
    }

    public void createNewEpic(Epic epic) {
        repositoryTasks.addNewEpic(epic, generateID());
    }

    public void deleteSubtask(int index) {
        repositoryTasks.deleteSubtask(index);
    }

    public void deleteTask(int index) {
        repositoryTasks.deleteTask(index);
    }

    public void deleteEpic(int index) {
        repositoryTasks.getEpics().get(index).removeSubtask();
        repositoryTasks.deleteEpic(index);
    }

    public void changeStatusTask(int index, Status status) {
        repositoryTasks.getTasks().get(index).setStatus(status);
    }


    public void changeStatusSubtask(int index, Status status) {
        repositoryTasks.getSubtasks().get(index).setStatus(status);
        int indexEpic = repositoryTasks.getSubtasks().get(index).getIndexEpic();
        Status epicStatus = repositoryTasks.getEpics().get(indexEpic).getStatus();
        Map<Integer, Subtask> subtasksByEpic = getAllSubtaskByEpic(indexEpic);
        Epic epic = (Epic) getTaskById(indexEpic);

        if (status == Status.IN_PROGRESS) {

            epic.setStatus(Status.IN_PROGRESS);
        } else if (status == Status.DONE) {
            boolean hasUncloseTask = false;
            for (Subtask subtask : subtasksByEpic.values()) {
                if (subtask.getStatus() != Status.DONE) {
                    hasUncloseTask = true;
                    break;
                }
            }
            if (!hasUncloseTask) {
                epic.setStatus(Status.DONE);
            }
        }
    }

    public Map<Integer, Task> getAllTasks() {
        Map<Integer, Task> allTasks = new HashMap<>();
        allTasks.putAll(repositoryTasks.getEpics());
        allTasks.putAll(repositoryTasks.getSubtasks());
        allTasks.putAll(repositoryTasks.getTasks());
        return allTasks;
    }

    public void removeAllTask() {
        repositoryTasks.getTasks().clear();
    }

    public void removeAllSubtask() {
        repositoryTasks.getSubtasks().clear();
    }

    public void removeAllEpic() {
        repositoryTasks.getEpics().clear();
        repositoryTasks.getSubtasks().clear();
    }

    public Map<Integer, Subtask> getAllSubtaskByEpic(int epicIndex) {
        return getEpics().get(epicIndex).getSubtasks();
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

    public Task getTaskById(int index) {
        Map<Integer, Task> allTasks = new HashMap<>();
        allTasks.putAll(repositoryTasks.getEpics());
        allTasks.putAll(repositoryTasks.getSubtasks());
        allTasks.putAll(repositoryTasks.getTasks());
        return allTasks.get(index);
    }

    public void updateTask(Task task, int index) {
        repositoryTasks.addNewTask(task, index);
    }

    public void updateSubtask(Subtask subtask, int index) {
        repositoryTasks.addNewSubtask(subtask, index);
    }

    public void updateEpic(Epic epic, int index) {
        Map<Integer, Subtask> subtasksByEpic = repositoryTasks.getEpics().get(index).getSubtasks();
        repositoryTasks.addNewEpic(epic, index);
        repositoryTasks.getEpics().get(index).addSubtasks(subtasksByEpic);
    }

}


