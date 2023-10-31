package service;

import files.CSVFormatHandler;
import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;

import java.io.*;
import java.util.ArrayList;
import java.util.List;


public class FileBackedTasksManager extends InMemoryTaskManager {
    private final File file;
    private static final CSVFormatHandler handler = new CSVFormatHandler();

    public FileBackedTasksManager() {
        super(Managers.getHistoryDefault());
        this.file = new File("file.csv");
    }

    public FileBackedTasksManager(File file) {
        super(Managers.getHistoryDefault());
        this.file = file;
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
            if (handler.historyToString(getHistory()).isEmpty()) {
                writer.newLine();
            } else writer.write(handler.historyToString(getHistory()));

        } catch (IOException exception) {
            throw new IllegalArgumentException("Невозможно прочитать файл!");
        }

    }

    public static FileBackedTasksManager loadFromFile(File file) {
        FileBackedTasksManager manager = new FileBackedTasksManager(file);
        List<String> lines = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String content = reader.readLine();
            while (reader.ready()) {
                lines.add(content);
                content = reader.readLine();
            }
            reader.close();
            lines.add(content);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            int maxId = 0;
            for (int i = 1; i < (lines.size() - 2); i++) {
                String[] parts = lines.get(i).split(",");
                if (parts[1].equals("TASK")) {
                    Task task = handler.taskFromString(lines.get(i));
                    manager.getTasksStorage().put(task.getId(), task);
                    manager.getPriorityTasks().add(task);
                    if (maxId < task.getId()) {
                        maxId = task.getId();
                    }
                }
                if (parts[1].equals("EPIC")) {
                    Epic epic = handler.epicFromString(lines.get(i));
                    manager.getEpicsStorage().put(epic.getId(), epic);
                    manager.getPriorityTasks().add(epic);
                    if (maxId < epic.getId()) {
                        maxId = epic.getId();
                    }
                }
                if (parts[1].equals("SUBTASK")) {
                    Subtask subTask = handler.subTaskFromString(lines.get(i));
                    int ide = subTask.getIndexEpic();
                    manager.getSubtasksStorage().put(subTask.getId(), subTask);
                    manager.getPriorityTasks().add(subTask);
                    if (maxId < subTask.getId()) {
                        maxId = subTask.getId();
                    }
                    if (manager.getEpicsStorage().containsKey(ide)) {
                        Epic epic = manager.getEpicsStorage().get(ide);
                        epic.addSubtask(subTask.getId());
                    } else {
                        System.out.println("Файл повреждён! Не возможно найти Эпик!");
                        break;
                    }
                }
            }
            manager.setIndex(maxId + 1);
            int lineWithHistory = lines.size() - 1;
            String[] ids = lines.get(lineWithHistory).split(",");
            boolean checkHistory = lines.get(lineWithHistory).isEmpty();
            if (!checkHistory) {
                for (String id : ids) {
                    if (manager.getTasksStorage().containsKey(Integer.parseInt(id))) {
                        manager.getHistoryManager().add(manager.getTasksStorage().get(Integer.parseInt(id)));
                    }
                    if (manager.getEpicsStorage().containsKey(Integer.parseInt(id))) {
                        manager.getHistoryManager().add(manager.getEpicsStorage().get(Integer.parseInt(id)));
                    }
                    if (manager.getSubtasksStorage().containsKey(Integer.parseInt(id))) {
                        manager.getHistoryManager().add(manager.getSubtasksStorage().get(Integer.parseInt(id)));
                    }
                }
            }
            return manager;
        } catch (IndexOutOfBoundsException | NullPointerException e) {
            return manager;
        }
    }

    @Override
    public Task createNewTask(Task task) {
        super.createNewTask(task);
        save();
        return task;
    }

    @Override
    public Subtask createNewSubtask(Subtask subtask) {
        super.createNewSubtask(subtask);
        save();
        return subtask;
    }

    @Override
    public Epic createNewEpic(Epic epic) {
        super.createNewEpic(epic);
        save();
        return epic;
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
        save();
    }

    @Override
    public void deleteEpic(int index) {
        super.deleteEpic(index);
        save();
    }

}
