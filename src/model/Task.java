package model;


import java.time.Instant;
import java.util.Objects;

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
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getId() {
        return this.id;
    }

    public Status getStatus() {
        return this.status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
    public void setId(int id) {
        this.id = id;
    }

    public TypeTask getTypeTask() {
        return this.typeTask;
    }

    public Instant getStartTime() {
        return this.startTime;
    }
    public long getDuration() {
        return this.duration;
    }

    public void setStartTime(Instant startTime) {
        this.startTime = startTime;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }
    public Instant getEndTime(){
        return this.startTime.plusSeconds(this.duration * 60);
    }

    @Override
    public String toString() {
        return String.format("Task %d: %s, %s. (%s) \n " +
                        "Начать: %s \n " +
                        "Время на выполнение: %s \n " +
                        "Закончить: %s", this.id,
                this.name,
                this.description,
                this.status,
                this.startTime,
                this.duration,
                getEndTime());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return this.id == task.id && this.duration == task.duration && Objects.equals(this.name, task.name) && Objects.equals(this.description, task.description) && this.typeTask == task.typeTask && this.status == task.status && Objects.equals(this.startTime, task.startTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.name, this.description, this.id, this.typeTask, this.status, this.startTime, this.duration);
    }
}
