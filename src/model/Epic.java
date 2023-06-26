package model;


import java.util.ArrayList;
import java.util.List;

public class Epic extends Task implements Tasks{
List<Subtask> subtasks;


    public Epic(String name, String description, int id, String status) {
        super(name, description, id, status);
        this.subtasks = new ArrayList<>();

    }
}

