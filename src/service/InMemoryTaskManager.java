package service;

import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;

import java.time.Duration;
import java.time.Instant;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    private final Map<Integer, Task> tasksStorage;
    private final Map<Integer, Subtask> subtasksStorage;
    private final Map<Integer, Epic> epicsStorage;
    private final Set<Task> priorityTasks = new TreeSet<>(Comparator.comparing(Task::getStartTime));
    private int index;
    private final HistoryManager historyManager;


    public InMemoryTaskManager() {
        this.tasksStorage = new HashMap<>();
        this.subtasksStorage = new HashMap<>();
        this.epicsStorage = new HashMap<>();
        this.historyManager = Managers.getHistoryDefault();

    }


    @Override
    public Task createNewTask(Task task) {
        if (this.interSection(task)) {
            System.out.println("Задачи пересекаются");
            return null;
        }
        if (task != null && !this.tasksStorage.containsKey(task.getId())) {
            task.setId(generateID());
            this.tasksStorage.put(task.getId(), task);
            this.priorityTasks.add(task);
        } else {
            return null;
        }
        return task;


    }

    @Override
    public Subtask createNewSubtask(Subtask subtask) {
        if (this.interSection(subtask)) {
            System.out.println("Задачи пересекаются");
            return null;
        }
        if (subtask != null && !this.subtasksStorage.containsKey(subtask.getId())) {
            subtask.setId(generateID());
            this.subtasksStorage.put(this.index, subtask);
            this.priorityTasks.add(subtask);

            Epic epic = this.epicsStorage.get(subtask.getIndexEpic());
            if (epic != null) {
                this.epicsStorage.get(subtask.getIndexEpic()).addSubtask(subtask.getId());
                checkOrChangeEpicStatus(subtask.getIndexEpic());
                setEpicTime(subtask.getIndexEpic());
            }
        } else {
            return null;
        }
        return subtask;
    }

    @Override
    public Epic createNewEpic(Epic epic) {
        if (epic != null && !this.epicsStorage.containsKey(epic.getId())) {
            epic.setId(generateID());
            this.epicsStorage.put(this.index, epic);
        } else {
            return null;
        }
        return epic;
    }

    @Override
    public void changeStatusTask(int index, Status status) {
        this.tasksStorage.get(index).setStatus(status);
    }


    public void changeStatusSubtask(int index, Status status) {
        this.subtasksStorage.get(index).setStatus(status);
        checkOrChangeEpicStatus(this.subtasksStorage.get(index).getIndexEpic());
    }


    @Override
    public void removeAllTask() {

        this.tasksStorage.forEach((integer, task) -> {
            this.historyManager.remove(task.getId());
            this.priorityTasks.remove(task);
        });
        this.tasksStorage.clear();

    }

    @Override
    public void removeAllSubtask() {
        this.subtasksStorage.forEach((integer, subtask) -> {
            this.historyManager.remove(subtask.getId());
            this.priorityTasks.remove(subtask);
        });
        this.subtasksStorage.clear();
        for (Epic epic : this.epicsStorage.values()) {
            epic.setStatus(Status.NEW);
        }
        // удаление сабтаск у епика
        for (Epic epic : this.epicsStorage.values()) {
            epic.getSubtasksIds().clear();
        }

    }

    @Override
    public void removeAllEpic() {
        removeAllSubtask();
        this.epicsStorage.forEach((integer, epic) -> this.historyManager.remove(epic.getId()));
        this.epicsStorage.clear();

    }

    @Override
    public List<Subtask> getAllSubtaskByEpic(int epicIndex) {
        if (this.epicsStorage.containsKey(epicIndex)) {
            List<Subtask> allSubtasksByEpic = new ArrayList<>();
            List<Integer> subtasksByEpic = this.epicsStorage.get(epicIndex).getSubtasksIds();
            for (int subtasks : subtasksByEpic) {
                allSubtasksByEpic.add(this.subtasksStorage.get(subtasks));
            }
            return allSubtasksByEpic;
        } else return null;
    }

    @Override
    public List<Task> getTasks() {
        return new ArrayList<>(this.tasksStorage.values());
    }

    @Override
    public List<Subtask> getSubtasks() {
        return new ArrayList<>(this.subtasksStorage.values());
    }

    @Override
    public List<Epic> getEpics() {
        return new ArrayList<>(this.epicsStorage.values());
    }

    @Override
    public Task getTaskById(int index) {
        try {
            Map<Integer, Task> allTasks = new HashMap<>();
            allTasks.putAll(this.epicsStorage);
            allTasks.putAll(this.subtasksStorage);
            allTasks.putAll(this.tasksStorage);
            this.historyManager.add(allTasks.get(index));
            return allTasks.get(index);
        } catch (NullPointerException e) {
            System.out.println("Task not found");
        }
        return null;
    }

    @Override
    public Task getTask(int index) {
        Task task = this.tasksStorage.get(index);
        if (task != null) {
            this.historyManager.add(task);
            return task;
        } else {
            return null;
        }
    }

    @Override
    public Subtask getSubtask(int index) {
        Subtask subtask = this.subtasksStorage.get(index);
        if (subtask != null) {
            this.historyManager.add(subtask);
            return subtask;
        } else {
            return null;
        }

    }

    @Override
    public Epic getEpic(int index) {
        Epic epic = this.epicsStorage.get(index);
        if (epic != null) {
            this.historyManager.add(epic);
            return epic;
        } else {
            return null;
        }
    }

    @Override
    public void updateTask(Task task) {
        if (task != null && !interSection(task)) {
            Task deleteTask = this.tasksStorage.get(task.getId());
            this.priorityTasks.remove(deleteTask);
            this.tasksStorage.put(task.getId(), task);
            this.priorityTasks.add(task);
        }
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        if (subtask != null && !interSection(subtask)) {
            Task deleteTask = this.tasksStorage.get(subtask.getId());
            this.priorityTasks.remove(deleteTask);
            this.subtasksStorage.put(subtask.getId(), subtask);
            this.priorityTasks.add(subtask);
            setEpicTime(subtask.getIndexEpic());
            checkOrChangeEpicStatus(subtask.getIndexEpic());
        }

    }

    @Override
    public void updateEpic(Epic epic) {
        final Epic savedEpic = this.epicsStorage.get(epic.getId());
        savedEpic.setName(epic.getName());
        savedEpic.setDescription(epic.getDescription());
    }

    public List<Task> getHistory() {
        return this.historyManager.getHistory();
    }

    @Override
    public void deleteSubtask(int index) {
        if (this.subtasksStorage.containsKey(index)) {
            this.historyManager.remove(index);
            int indexEpic = this.subtasksStorage.get(index).getIndexEpic();
            Subtask subTaskForRemove = this.subtasksStorage.remove(index);
            this.priorityTasks.remove(subTaskForRemove);
            this.epicsStorage.get(indexEpic).removeOneSubtask(index);
            setEpicTime(indexEpic);
            checkOrChangeEpicStatus(indexEpic);
        } else
            System.out.printf("Сабтаска № %d отсутсвует \n", index);


    }

    @Override
    public void deleteTask(int index) {
        Task task = this.tasksStorage.remove(index);
        this.priorityTasks.remove(task);
        this.historyManager.remove(index);

    }

    @Override
    public void deleteEpic(int index) {
        ArrayList<Integer> subtasksForRemove = new ArrayList<>();
        for (Subtask value : this.subtasksStorage.values()) {
            if (value.getIndexEpic() == index) {
                subtasksForRemove.add(value.getId());
            }
        }
        for (Integer id : subtasksForRemove) {
            this.subtasksStorage.remove(id);
            this.historyManager.remove(id);
        }
        this.historyManager.remove(index);
        this.epicsStorage.remove(index);
    }

    private int generateID() {
        return ++this.index;
    }

    private void checkOrChangeEpicStatus(int indexEpic) {
        Epic epic = this.epicsStorage.get(indexEpic);
        List<Integer> subtasksIds = epic.getSubtasksIds();
        int countSubtasks = subtasksIds.size();
        int doneCount = 0;
        int newCount = 0;
        for (int subtasksId : subtasksIds) {
            switch (this.subtasksStorage.get(subtasksId).getStatus()) {
                case NEW:
                    newCount++;
                    break;
                case DONE:
                    doneCount++;
                    break;
                default:
                    break;
            }
        }
        if (doneCount == countSubtasks) {
            epic.setStatus(Status.DONE);
        } else if (newCount == countSubtasks) {
            epic.setStatus(Status.NEW);

        } else epic.setStatus(Status.IN_PROGRESS);


    }

    @Override
    public void setEpicTime(int indexEpic) {
        Epic epic = this.epicsStorage.get(indexEpic);
        List<Integer> subtasksIds = epic.getSubtasksIds();
        if (!subtasksIds.isEmpty()) {
            Instant startTime = this.subtasksStorage.get(subtasksIds.get(0)).getStartTime();
            Instant endTime = this.subtasksStorage.get(subtasksIds.get(0)).getEndTime();
            for (Integer subtasksId : subtasksIds) {
                if (this.subtasksStorage.get(subtasksId).getStartTime().isBefore(startTime))
                    startTime = this.subtasksStorage.get(subtasksId).getStartTime();

                if (this.subtasksStorage.get(subtasksId).getEndTime().isAfter(endTime))
                    endTime = this.subtasksStorage.get(subtasksId).getEndTime();
            }
            epic.setStartTime(startTime);
            epic.setEndTime(endTime);
            epic.setDuration(Duration.between(startTime, endTime).toMinutes());
        } else {
            epic.setStartTime(Instant.MIN);
            epic.setEndTime(Instant.MAX);
            epic.setDuration(0);
        }

    }

    protected Map<Integer, Task> getTasksStorage() {
        return this.tasksStorage;
    }

    protected Map<Integer, Subtask> getSubtasksStorage() {
        return this.subtasksStorage;
    }

    protected Map<Integer, Epic> getEpicsStorage() {
        return this.epicsStorage;
    }

    protected HistoryManager getHistoryManager() {
        return this.historyManager;
    }

    private HistoryManager historyManager() {
        return this.historyManager;
    }

    public int getIndex() {
        return this.index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    @Override
    public List<Task> getPriorityTasks() {
        return new ArrayList<>(priorityTasks);
    }


    private boolean interSection(Task newTask) {
        for (Task oldTask : this.priorityTasks) {
            if (!((newTask.getStartTime().isBefore(oldTask.getStartTime()) && newTask.getEndTime().isBefore(oldTask.getStartTime()))
                    || (newTask.getStartTime().isAfter(oldTask.getStartTime()) && newTask.getEndTime().isAfter(oldTask.getStartTime())))) {
                return true;
            }
        }
        return false;
    }
}


