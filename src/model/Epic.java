package model;


import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
    private final List<Integer> subtasks;

    public Epic(String name, String description) {
        super(name, description);
        this.subtasks = new ArrayList<>();
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

    public List<Integer> getSubtasks() {
        return subtasks;
    }

    public void addSubtask(int index) {
        this.subtasks.add(index);
    }
    public void addSubtasks(List<Integer> subtasks) {
        this.subtasks.addAll(subtasks);
    }
    public void removeOneSubtask(int index){
        this.subtasks.remove(index);
    }

}

