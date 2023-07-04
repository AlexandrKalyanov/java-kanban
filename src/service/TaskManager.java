package service;

import model.*;

import java.util.*;

public class TaskManager {
    private Map<Integer, Task> getTasksStorage() {
        return tasksStorage;
    }

    private void setTasksStorage(Map<Integer, Task> tasksStorage) {
        this.tasksStorage = tasksStorage;
    }

    private Map<Integer, Subtask> getSubtasksStorage() {
        return subtasksStorage;
    }

    private void setSubtasksStorage(Map<Integer, Subtask> subtasksStorage) {
        this.subtasksStorage = subtasksStorage;
    }

    private Map<Integer, Epic> getEpicsStorage() {
        return epicsStorage;
    }

    private void setEpicsStorage(Map<Integer, Epic> epicsStorage) {
        this.epicsStorage = epicsStorage;
    }

    private Map<Integer, Task> tasksStorage;
    private Map<Integer, Subtask> subtasksStorage;
    private Map<Integer, Epic> epicsStorage;

    private int index;

    public TaskManager() {
        this.tasksStorage = new HashMap<>();
        this.subtasksStorage = new HashMap<>();
        this.epicsStorage = new HashMap<>();
        index = 0;
    }

    private int generateID() {
        return ++index;
    }

    public void createNewTask(Task task) {
        this.tasksStorage.put(generateID(),task);
        task.setId(this.index);
    }

    public void createNewSubtask(Subtask subtask) {
        this.subtasksStorage.put(generateID(),subtask);
        subtask.setId(this.index);
        this.epicsStorage.get(subtask.getIndexEpic()).addSubtask(this.index);
    }

    public void createNewEpic(Epic epic) {
        this.epicsStorage.put(generateID(), epic);
        epic.setId(index);
        epic.setStatus(Status.NEW);

    }



    public void changeStatusTask(int index, Status status) {
        this.tasksStorage.get(index).setStatus(status);
    }


    public void changeStatusSubtask(int index, Status status) {
        subtasksStorage.get(index).setStatus(status);
        int indexEpic = subtasksStorage.get(index).getIndexEpic();
        Status epicStatus = epicsStorage.get(indexEpic).getStatus();
        List<Subtask> subtasksByEpic = getAllSubtaskByEpic(indexEpic);
        Epic epic = epicsStorage.get(indexEpic);

        if (status == Status.IN_PROGRESS) {

            epic.setStatus(Status.IN_PROGRESS);
        } else if (status == Status.DONE) {
            boolean hasUncloseTask = false;
            for (Subtask subtask : subtasksByEpic) {
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
        for (Epic epic : epicsStorage.values()){
            epic.setStatus(Status.NEW);
        }

    }

    public void removeAllEpic() {
        this.epicsStorage.clear();
        this.subtasksStorage.clear();
    }

    public List<Subtask> getAllSubtaskByEpic(int epicIndex) {
        List<Subtask> allSubtasksByEpic = new ArrayList<>();
        List<Integer> subtasksByEpic = this.epicsStorage.get(epicIndex).getSubtasks();
        for (int subtasks: subtasksByEpic){
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
        tasksStorage.put(task.getId(),task);
    }

    public void updateSubtask(Subtask subtask) {
       subtasksStorage.put(subtask.getId(),subtask);
        // TODO: change epic status
    }

    public void updateEpic(Epic epic) {
        List<Integer> subtasksByEpic = epicsStorage.get(epic.getId()).getSubtasks();
        epicsStorage.put(epic.getId(),epic);
        epicsStorage.get(epic.getId()).addSubtasks(subtasksByEpic);
    }



    public void deleteSubtask(int index){
        int indexEpic = this.subtasksStorage.get(index).getIndexEpic();
        this.epicsStorage.get(indexEpic).removeOneSubtask(index);
        this.subtasksStorage.remove(index);
        //todo change epic status
    }
    public void deleteTask(int index){
        this.tasksStorage.remove(index);
    }
    public void deleteEpic(int index){
        subtasksStorage.values().removeIf(subtask -> subtask.getIndexEpic() == index);
        this.epicsStorage.remove(index);

    }
}


