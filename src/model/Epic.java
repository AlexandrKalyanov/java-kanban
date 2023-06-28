package model;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Epic extends Task {
    private Map<Integer, Subtask> subtasks;


    public Epic(String name, String description) {
        super(name, description);
        this.subtasks = new HashMap<>();

    }

    @Override
    public String getName() {
        return super.getName();
    }

    @Override
    public String getDescription() {
        return super.getDescription();
    }

    @Override
    public String toString() {
        return "Epic{" +
                "name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", id=" + getId() +
                ", status='" + getStatus() + '\'' +
                '}';
    }

    @Override
    public int getId() {
        return super.getId();
    }

    @Override
    public Status getStatus() {
        return super.getStatus();
    }

    @Override
    public void setId(int id) {
        super.setId(id);
    }

    public Map<Integer, Subtask> getSubtasks() {
        return subtasks;
    }

    public void setSubtasks(Map<Integer, Subtask> subtasks) {
        this.subtasks = subtasks;
    }
}

