package model;

import java.util.HashMap;
import java.util.Map;

public class RepositoryTasks {
    private Map<Integer,Task> tasks;
    private Map<Integer,Subtask> subtasks;
    private Map<Integer,Epic> epics;

    public RepositoryTasks() {
        this.tasks = new HashMap<>();
        this.subtasks = new HashMap<>();
        this.epics = new HashMap<>();
    }

    public void addNewTask(Task task,int index){
        task.setId(index);
        tasks.put(index,task);
    }
    public void addNewSubtask(Subtask subtask,int index){
        subtask.setId(index);
        subtasks.put(index,subtask);
    }
    public void addNewEpic(Epic epic,int index){
        epic.setId(index);
        epics.put(index,epic);
    }

    @Override
    public String toString() {
        return "RepositoryTasks{" +
                "tasks=" + tasks +
                ", subtasks=" + subtasks +
                ", epics=" + epics +
                '}';
    }

    public Map<Integer, Task> getTasks() {
        return tasks;
    }

    public Map<Integer, Subtask> getSubtasks() {
        return subtasks;
    }

    public Map<Integer, Epic> getEpics() {
        return epics;
    }
}
