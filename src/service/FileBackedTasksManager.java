package service;

import Files.CSVFormatHandler;

import model.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;


public class FileBackedTasksManager extends InMemoryTaskManager {


    private final File file;
    private static final CSVFormatHandler handler = new CSVFormatHandler();

    public FileBackedTasksManager() {
        super();
        this.file = new File("file.csv");
    }
    public static void main(String[] args) {
        FileBackedTasksManager backedTasksManager = Managers.BackedTasksManager();
        backedTasksManager.loadFromFile(backedTasksManager.getFile());
        backedTasksManager.createNewTask(new Task("Задача №1", "Описание задачи №1",Status.NEW,TypeTask.TASK));
        backedTasksManager.createNewTask(new Task("Задача №2", "Описание задачи №2",Status.NEW,TypeTask.TASK));
        backedTasksManager.createNewEpic(new Epic("Эпик 1", "Описание эпика 1",TypeTask.EPIC));
        backedTasksManager.createNewSubtask(new Subtask("Subtask 1", "descr",Status.NEW,3,TypeTask.SUBTASK));
        backedTasksManager.createNewSubtask(new Subtask("SubTask2", "Priverka2", Status.NEW,3,TypeTask.SUBTASK));
        backedTasksManager.createNewEpic(new Epic("Эпик №2", "Описание эпика №2",TypeTask.EPIC));
        backedTasksManager.createNewSubtask(new Subtask("SubTask3", "Priverka3", Status.NEW,3,TypeTask.SUBTASK));
        backedTasksManager.getTask(1);
        backedTasksManager.getTask(1);
        backedTasksManager.getTask(2);
        backedTasksManager.getEpic(3);
        System.out.println(backedTasksManager.getHistory());



    }
    public void save() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(this.file.getName()))) {

            writer.write(handler.getHeader());
            writer.newLine();

            for (Task task : getTasksStorage().values()) {
                writer.write(handler.toString(task));
                writer.newLine();
            }

            for (Epic epic : getEpicsStorage().values()) {
                writer.write(handler.toString(epic));
                writer.newLine();
            }

            for (Subtask subTask : getSubtasksStorage().values()) {
                writer.write(handler.toString(subTask));
                writer.newLine();
            }

            writer.newLine();
            writer.write(handler.historyToString(getHistoryManager()));

        } catch (IOException exception) {
            throw new IllegalArgumentException("Невозможно прочитать файл!");
        }

    }
    public void loadFromFile(File file) {
        String content = readFileContents(file.getName());
        String[] lines = content.split("\r?\n");
        if (lines[1] != null) {     // Проверка на пустой файл
            for (int i = 1; i < (lines.length - 2); i++) {
                String[] parts = lines[i].split(",");
                if (parts[1].equals("TASK")) {
                    Task task = handler.taskFromString(lines[i]);
                    getTasksStorage().put(task.getId(), task);
                }
                if (parts[1].equals("EPIC")) {
                    Epic epic = handler.epicFromString(lines[i]);
                    getEpicsStorage().put(epic.getId(), epic);
                }
                if (parts[1].equals("SUBTASK")) {
                    Subtask subTask = handler.subTaskFromString(lines[i]);
                    int ide = subTask.getIndexEpic();
                    getSubtasksStorage().put(subTask.getId(), subTask);
                    if (getEpicsStorage().containsKey(ide)) {
                        Epic epic = getEpicsStorage().get(ide);
                        epic.addSubtask(subTask.getId());
                    } else {
                        System.out.println("Файл повреждён! Не возможно найти Эпик!");
                        break;
                    }
                }
            }
            String[] ids = lines[lines.length-1].split(",");
            for (int j = 0; j < ids.length; j++) {
                if (getTasksStorage().containsKey(j)) {
                    getHistoryManager().add(getTasksStorage().get(j));
                }
                if (getEpicsStorage().containsKey(j)) {
                    getHistoryManager().add(getEpicsStorage().get(j));
                }
                if (getSubtasksStorage().containsKey(j)) {
                    getHistoryManager().add(getSubtasksStorage().get(j));
                }
            }


        } else {
            System.out.println("Файл пуст!");
        }
    }
    public String readFileContents(String file) {
        try {
            return Files.readString(Path.of(file));
        } catch (IOException e) {
            System.out.println("Невозможно прочитать файл с месячным отчётом. Возможно файл не находится в нужной директории.");
            return null;
        }
    }
    public File getFile() {
        return file;
    }
    @Override
    public void createNewTask(Task task) {
        super.createNewTask(task);
        save();
    }

    @Override
    public void createNewSubtask(Subtask subtask) {
        super.createNewSubtask(subtask);
        save();
    }

    @Override
    public void createNewEpic(Epic epic) {
        super.createNewEpic(epic);
        save();
    }

    @Override
    public void changeStatusTask(int index, Status status) {
        super.changeStatusTask(index, status);
        save();
    }

    @Override
    public void changeStatusSubtask(int index, Status status) {
        super.changeStatusSubtask(index, status);
        save();
    }

    @Override
    public List<Task> getAllTasks() {
        List<Task> tasks = super.getAllTasks();
        save();
        return tasks;
    }

    @Override
    public void removeAllTask() {
        super.removeAllTask();
        save();
    }

    @Override
    public void removeAllSubtask() {
        super.removeAllSubtask();
        save();
    }

    @Override
    public void removeAllEpic() {
        super.removeAllEpic();
        save();
    }

    @Override
    public List<Subtask> getAllSubtaskByEpic(int epicIndex) {
        List<Subtask> subtasks = super.getAllSubtaskByEpic(epicIndex);
        save();
        return subtasks;
    }

    @Override
    public List<Task> getTasks() {
        List<Task> tasks = super.getTasks();
        save();
        return tasks;
    }

    @Override
    public List<Subtask> getSubtasks() {
        List<Subtask> subtasks = super.getSubtasks();
        save();
        return subtasks;
    }

    @Override
    public List<Epic> getEpics() {
        List<Epic> epics = super.getEpics();
        save();
        return epics;
    }

    @Override
    public Task getTaskById(int index) {
        Task task = super.getTaskById(index);
        save();
        return task;
    }

    @Override
    public Task getTask(int index) {
        Task task = super.getTask(index);
        save();
        return task;
    }

    @Override
    public Subtask getSubtask(int index) {
        Subtask subtask = super.getSubtask(index);
        save();
        return subtask;
    }

    @Override
    public Epic getEpic(int index) {
        Epic epic = super.getEpic(index);
        save();
        return epic;
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void deleteSubtask(int index) {
        super.deleteSubtask(index);
        save();
    }

    @Override
    public void deleteTask(int index) {
        super.deleteTask(index);
    }

    @Override
    public void deleteEpic(int index) {
        super.deleteEpic(index);
        save();
    }

}
