package model;


import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Epic extends Task {
    private final List<Integer> subtasksIds;
    private Instant endTime = Instant.ofEpochSecond(0);

    public Epic(String name,
                String description,
                TypeTask taskType) {

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

   @Override
    public Instant getEndTime() {
        return endTime;
    }

    public void setEndTime(Instant endTime) {
        this.endTime = endTime;
    }
    public void updateEpicState(Map<Integer, Subtask> subs) {

        var startTime = subs.get(subtasksIds.get(0)).getStartTime();
        var endTime = subs.get(subtasksIds.get(0)).getEndTime();

        int isNew = 0;
        int isDone = 0;

        for (Integer ignored : subtasksIds) {

            Subtask subtask = subs.get(id);

            if (subtask.getStatus() == Status.NEW)
                isNew += 1;

            if (subtask.getStatus() == Status.DONE)
                isDone += 1;

            if (subtask.getStartTime().isBefore(startTime))
                startTime = subtask.getStartTime();

            if (subtask.getEndTime().isAfter(endTime))
                endTime = subtask.getEndTime();
        }

        this.startTime = startTime;
        this.endTime = endTime;
        this.duration = Duration.between(startTime, endTime).toMinutes();

        if (subtasksIds.size() == isNew) {

            setStatus(Status.NEW);

            return;

        } else if (subtasksIds.size() == isDone) {

            setStatus(Status.DONE);

            return;

        }
            setStatus(Status.IN_PROGRESS);

    }

}

