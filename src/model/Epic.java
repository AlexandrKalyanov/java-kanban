package model;


import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
    private final List<Integer> subtasksIds;

    public Epic(String name, String description, TypeTask typeTask) {
        super(name, description);
        this.subtasksIds = new ArrayList<>();
        setTypeTask(typeTask);
    }

    public Epic(String name, String description, int id, TypeTask typeTask) {
        super(name, description, id, typeTask);
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

    public void removeOneSubtask(int id) {
        int index = subtasksIds.indexOf(id);
        this.subtasksIds.remove(index);
    }

}

