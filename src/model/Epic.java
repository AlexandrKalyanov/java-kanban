package model;


import java.time.Instant;
import java.util.ArrayList;
import java.util.List;


public class Epic extends Task {
    private final List<Integer> subtasksIds;
    private Instant endTime = Instant.ofEpochSecond(0);

    public Epic(String name, String description, TypeTask taskType) {
        super(name, description, Instant.ofEpochSecond(0), 0);
        this.subtasksIds = new ArrayList<>();
        this.typeTask = taskType;
    }

    public Epic(String name,
                String description,
                TypeTask taskType,
                int id,
                Instant startTime,
                long duration,
                Instant endTime) {
        super(name, description, startTime, duration);
        this.subtasksIds = new ArrayList<>();
        this.endTime = endTime;
        this.typeTask = taskType;
        this.id = id;
    }

    @Override
    public String toString() {
        return String.format("Epic %d: %s, %s. %s \n " +
                        "Начать: %s \n " +
                        "Время на выполнение: %s \n " +
                        "Закончить: %s",
                this.id,
                this.name,
                this.description,
                this.status,
                this.startTime,
                this.duration,
                this.endTime);
    }


    public List<Integer> getSubtasksIds() {
        return this.subtasksIds;
    }

    public void addSubtask(int index) {
        this.subtasksIds.add(index);
    }

    public void removeOneSubtask(int id) {
        int index = this.subtasksIds.indexOf(id);
        this.subtasksIds.remove(index);
    }

    @Override
    public Instant getEndTime() {
        return this.endTime;
    }

    public void setEndTime(Instant endTime) {
        this.endTime = endTime;
    }


}

