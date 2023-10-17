package service;

import model.*;

import java.util.*;

public interface TaskManager {
    Task createNewTask(Task task); //done

    Subtask createNewSubtask(Subtask subtask); //done

    Epic createNewEpic(Epic epic); //done


    void changeStatusTask(int index, Status status); //done

    void changeStatusSubtask(int index, Status status); //done

    List<Task> getPrioritizedTasks(); //done

    void removeAllTask(); //done

    void removeAllSubtask(); //done

    void removeAllEpic(); //done

    List<Subtask> getAllSubtaskByEpic(int epicIndex); //done

    List<Task> getTasks();

    List<Subtask> getSubtasks();

    List<Epic> getEpics();

    Task getTaskById(int index);

    Task getTask(int index); //done

    Subtask getSubtask(int index); //done

    Epic getEpic(int index); //done

    void updateTask(Task task); //done

    void updateSubtask(Subtask subtask);//done

    void updateEpic(Epic epic);//done


    void deleteSubtask(int index);//done


    void deleteTask(int index);//done


    void deleteEpic(int index);

    List<Task> getHistory();
    void setEpicTime(int indexEpic);


}


