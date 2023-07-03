package model;

public class Subtask extends Task {
    private final int indexEpic;

    public Subtask(String name, String description, Status status, int indexEpic) {
        super(name, description, status);
        this.indexEpic = indexEpic;
    }

    public Subtask(String name, String description, Status status, int id, int indexEpic) {
        super(name, description, status, id);
        this.indexEpic = indexEpic;
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
