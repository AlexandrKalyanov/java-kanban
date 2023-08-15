package model;


import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
    private final List<Integer> subtasksIds;

    public Epic(String name, String description) {
        super(name, description);
        this.subtasksIds = new ArrayList<>();
    }

    public Epic(String name, String description, int id) {
        super(name, description, id);
        this.subtasksIds = new ArrayList<>();
    }


    @Override
    public String toString() {
        return "Epic{" +
                "name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", id=" + getId() +
                ", status='" + getStatus() + '\'' +
                ", subtasksIds='" + getSubtasksIds() + '\'' +
                '}';
    }

    public List<Integer> getSubtasksIds() {
        return subtasksIds;
    }

    public void addSubtask(int index) {
        this.subtasksIds.add(index);
    }

    public void addSubtasks(List<Integer> subtasks) {
        this.subtasksIds.addAll(subtasks);
    }

    public void removeOneSubtask(int id) {
        int index = subtasksIds.indexOf(id);
        this.subtasksIds.remove(index);
    }

}

