package service;

import model.*;

import java.util.*;

public interface TaskManager {
    Task createNewTask(Task task);

    Subtask createNewSubtask(Subtask subtask);

    Epic createNewEpic(Epic epic);


    void changeStatusTask(int index, Status status);

    void changeStatusSubtask(int index, Status status);
    void removeAllTask();

    void removeAllSubtask();

    void removeAllEpic();

    List<Subtask> getAllSubtaskByEpic(int epicIndex);

    List<Task> getTasks();

    List<Subtask> getSubtasks();

    List<Epic> getEpics();

    Task getTaskById(int index);

    Task getTask(int index);

    Subtask getSubtask(int index);

    Epic getEpic(int index);

    void updateTask(Task task);

    void updateSubtask(Subtask subtask);

    void updateEpic(Epic epic);


    void deleteSubtask(int index);


    void deleteTask(int index);


    void deleteEpic(int index);

    List<Task> getHistory();
    void setEpicTime(int indexEpic);

    TreeSet<Task> getPriorityTasks();

    boolean interSection(Task newTask);


}


