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
        return String.format("SubTask %d(epicId: %d): %s, %s, %s \n " +
                        "Начать: %s \n " + "Время на выполнение: %s \n " +
                        "Закончить: %s ", this.id,
                this.indexEpic, this.name,
                this.description, this.status,
                this.startTime, this.duration,
                getEndTime());
    }


    public int getIndexEpic() {
        return this.indexEpic;
    }
}
