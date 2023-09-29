package model;

public class Subtask extends Task {
    private final int indexEpic;

    public Subtask(String name, String description, Status status, int indexEpic,TypeTask typeTask) {
        super(name, description, status,typeTask);
        this.indexEpic = indexEpic;
        setTypeTask(typeTask);

    }

    public Subtask(String name, String description, Status status, int id, int indexEpic,TypeTask typeTask) {
        super(name, description, status, id,typeTask);
        this.indexEpic = indexEpic;
        setTypeTask(typeTask);
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
