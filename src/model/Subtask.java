package model;

import java.time.Instant;

public class Subtask extends Task {
    private final int indexEpic;

    public Subtask(String name,
                   String description,
                   Instant startTime,
                   long duration,
                   int indexEpic) {

        super(name, description, startTime, duration);
        this.typeTask = TypeTask.SUBTASK;
        this.indexEpic = indexEpic;

    }

    public Subtask(int id,
                   String name,
                   Status status,
                   String description,
                   Instant startTime,
                   long duration,
                   int indexEpic) {

        super(name, description, startTime, duration);
        this.typeTask = TypeTask.SUBTASK;
        this.status = status;
        this.indexEpic = indexEpic;
        this.id = id;

    }


    @Override
    public String toString() {
        return "Subtask{" +
                "name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", id=" + getId() +
                ", status='" + getStatus() + '\'' +
                '}';
    }


    public int getIndexEpic() {
        return indexEpic;
    }
}
