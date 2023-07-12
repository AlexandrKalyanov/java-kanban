package service;

import model.*;

import java.util.*;

public interface TaskManager {
    void createNewTask(Task task);

   void createNewSubtask(Subtask subtask);

   void createNewEpic(Epic epic);


    void changeStatusTask(int index, Status status);

    void changeStatusSubtask(int index, Status status);

    List<Task> getAllTasks();

    void removeAllTask();

    void removeAllSubtask();

    void removeAllEpic();
    List<Subtask> getAllSubtaskByEpic(int epicIndex);

    List<Task> getTasks();

    List<Subtask> getSubtasks();

    List<Epic> getEpics();

    Task getTaskById(int index);

    void updateTask(Task task);

    void updateSubtask(Subtask subtask);

    void updateEpic(Epic epic);


    void deleteSubtask(int index);


    void deleteTask(int index);


    void deleteEpic(int index);

}


