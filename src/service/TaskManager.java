package service;

import model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class TaskManager { // Класс для управление задачами: удалить, создать, посмотреть все созданнные задачи
    private List<Task> tasks;
    private List<Subtask> subtasks;
    private List<Epic> epics;
    private final Scanner scanner = new Scanner(System.in);
    private int index;

    public TaskManager() {
        index = 0;
        this.tasks = new ArrayList<>();
        this.subtasks = new ArrayList<>();
        this.epics = new ArrayList<>();
    }


    public Tasks createTask() {
        System.out.println("Какой тип задачи вы хотите создать?");
        System.out.println("Задача - 1");
        System.out.println("Подзадача - 2");
        System.out.println("Эпик - 3");
        int typeOfTask = scanner.nextInt();
        if (typeOfTask == 1){
            System.out.println("Введите название задачи:");
            String name = scanner.nextLine();
            System.out.println("Введите описание:");
            String description = scanner.nextLine();
            String status = String.valueOf(Status.NEW);
            this.index = index + 1;
            return new Task(name, description, index, status);
        }
        System.out.println("Введите название задачи:");
        String name = scanner.nextLine();
        System.out.println("Введите описание:");
        String description = scanner.nextLine();
        String status = String.valueOf(Status.NEW);
        this.index = index + 1;
        return new Task(name, description, index, status);
    }



}
