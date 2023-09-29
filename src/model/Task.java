package model;


public class Task {
    private String name;
    private String description;
    private int id;
    private TypeTask typeTask;

    private Status status;


    public Task(String name, String description) {
        this.name = name;
        this.description = description;
        this.typeTask = TypeTask.TASK;
    }

    public Task(String name, String description, Status status,TypeTask typeTask) {
        this.name = name;
        this.description = description;
        this.status = status;
        this.typeTask = typeTask;
    }

    public Task(String name, String description, Status status, int id,TypeTask typeTask) {
        this.name = name;
        this.description = description;
        this.status = status;
        this.id = id;
        this.typeTask = typeTask;
    }

    public Task(String name, String description, int id,TypeTask typeTask) {
        this.name = name;
        this.description = description;
        this.id = id;
        this.typeTask = typeTask;
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

    @Override
    public String toString() {
        return "Task{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                ", status='" + status + '\'' +
                '}';
    }

    public void setId(int id) {
        this.id = id;
    }

    public TypeTask getTypeTask() {
        return typeTask;
    }

    public void setTypeTask(TypeTask typeTask) {
        this.typeTask = typeTask;
    }
}
