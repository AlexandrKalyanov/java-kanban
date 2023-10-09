package model;


import java.time.Instant;

public class Task {
    protected String name;
    protected String description;
    protected int id;
    protected TypeTask typeTask;
    protected Status status;
    protected Instant startTime;
    protected long duration;


    public Task(String name,
                String description,
                Instant startTime,
                long duration) {

        this.name = name;
        this.description = description;
        this.typeTask = TypeTask.TASK;
        this.status = Status.NEW;
        this.startTime = startTime;
        this.duration = duration;

    }
    public Task(String name,
                String description,
                Status status,
                int id,
                Instant startTime,
                long duration) {

        this.name = name;
        this.description = description;
        this.status = status;
        this.id = id;
        this.typeTask = TypeTask.TASK;
        this.startTime = startTime;
        this.duration = duration;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
    public void setId(int id) {
        this.id = id;
    }

    public TypeTask getTypeTask() {
        return typeTask;
    }

    public Instant getStartTime() {
        return startTime;
    }
    public long getDuration() {
        return duration;
    }

    public void setStartTime(Instant startTime) {
        this.startTime = startTime;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }
    public Instant getEndTime(){
        return this.startTime.plusSeconds(duration * 60);
    }

    @Override
    public String toString() {
        return "Task{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                ", typeTask=" + typeTask +
                ", status=" + status +
                ", startTime=" + startTime +
                ", duration=" + duration +
                ", endTime=" + getEndTime() +
                '}';
    }
}
