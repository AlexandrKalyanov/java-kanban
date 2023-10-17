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
        if (task != null && !this.tasksStorage.containsKey(task.getId())) {
            task.setId(generateID());
            this.tasksStorage.put(task.getId(), task);
        } else {
            return null;
        }
        return task;


    }

    @Override
    public Subtask createNewSubtask(Subtask subtask) {
        if (subtask != null && !this.subtasksStorage.containsKey(subtask.getId())) {
            subtask.setId(generateID());
            this.subtasksStorage.put(this.index, subtask);

            Epic epic = epicsStorage.get(subtask.getIndexEpic());
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
        subtasksStorage.get(index).setStatus(status);
        checkOrChangeEpicStatus(subtasksStorage.get(index).getIndexEpic());
    }

    @Override
    public List<Task> getPrioritizedTasks() {
        List<Task> allTasks = new ArrayList<>();
        allTasks.addAll(this.tasksStorage.values());
        allTasks.addAll(this.subtasksStorage.values());
        allTasks.addAll(this.epicsStorage.values());
        allTasks.sort(Comparator.comparing(Task::getStartTime));
        return allTasks;
    }

    @Override
    public void removeAllTask() {
        tasksStorage.forEach(((integer, task) -> historyManager.remove(task.getId())));
        this.tasksStorage.clear();
    }

    @Override
    public void removeAllSubtask() {
        subtasksStorage.forEach((integer, subtask) -> historyManager.remove(subtask.getId()));
        this.subtasksStorage.clear();
        for (Epic epic : epicsStorage.values()) {
            epic.setStatus(Status.NEW);
        }
        // удаление сабтаск у епика
        for (Epic epic : epicsStorage.values()) {
            epic.getSubtasksIds().clear();
        }

    }

    @Override
    public void removeAllEpic() {
        epicsStorage.forEach((integer, epic) -> historyManager.remove(epic.getId()));
        for (Map.Entry<Integer, Epic> entry : epicsStorage.entrySet()) {
            List<Integer> subtasks = entry.getValue().getSubtasksIds();
            for (Integer subtask : subtasks) {
                historyManager.remove(subtask);
            }
        }
        this.epicsStorage.clear();
        this.subtasksStorage.clear();
    }

    @Override
    public List<Subtask> getAllSubtaskByEpic(int epicIndex) {
        if (epicsStorage.containsKey(epicIndex)) {
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
            allTasks.putAll(epicsStorage);
            allTasks.putAll(subtasksStorage);
            allTasks.putAll(tasksStorage);
            historyManager.add(allTasks.get(index));
            return allTasks.get(index);
        } catch (NullPointerException e) {
            System.out.println("Task not found");
        }
        return null;
    }

    @Override
    public Task getTask(int index) {
        Task task = tasksStorage.get(index);
        if (task != null) {
            historyManager.add(task);
            return task;
        } else {
            return null;
        }
    }

    @Override
    public Subtask getSubtask(int index) {
        Subtask subtask = subtasksStorage.get(index);
        if (subtask != null) {
            historyManager.add(subtask);
            return subtask;
        } else {
            return null;
        }

    }

    @Override
    public Epic getEpic(int index) {
        Epic epic = epicsStorage.get(index);
        if (epic != null) {
            historyManager.add(epic);
            return epic;
        } else {
            return null;
        }
    }

    @Override
    public void updateTask(Task task) {
        if (task != null) {
            tasksStorage.put(task.getId(), task);
        }
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        subtasksStorage.put(subtask.getId(), subtask);
        setEpicTime(subtask.getIndexEpic());
        checkOrChangeEpicStatus(subtask.getIndexEpic());

    }

    @Override
    public void updateEpic(Epic epic) {
        final Epic savedEpic = epicsStorage.get(epic.getId());
        savedEpic.setName(epic.getName());
        savedEpic.setDescription(epic.getDescription());
    }

    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    @Override
    public void deleteSubtask(int index) {
        if (subtasksStorage.containsKey(index)) {
            historyManager.remove(index);
            int indexEpic = this.subtasksStorage.get(index).getIndexEpic();
            subtasksStorage.remove(index);
            this.epicsStorage.get(indexEpic).removeOneSubtask(index);
            setEpicTime(indexEpic);
            checkOrChangeEpicStatus(indexEpic);
        }else
            System.out.printf("Сабтаска № %d отсутсвует \n",index);


    }

    @Override
    public void deleteTask(int index) {
        this.tasksStorage.remove(index);
        historyManager.remove(index);
    }

    @Override
    public void deleteEpic(int index) {
        ArrayList<Integer> subtasksForRemove = new ArrayList<>();
        for (Subtask value : subtasksStorage.values()) {
            if (value.getIndexEpic() == index) {
                subtasksForRemove.add(value.getId());
            }
        }
        for (Integer id : subtasksForRemove) {
            subtasksStorage.remove(id);
            historyManager.remove(id);
        }
        historyManager.remove(index);
        epicsStorage.remove(index);
    }

    private int generateID() {
        return ++index;
    }

    private void checkOrChangeEpicStatus(int indexEpic) {
        Epic epic = epicsStorage.get(indexEpic);
        List<Integer> subtasksIds = epic.getSubtasksIds();
        int countSubtasks = subtasksIds.size();
        int doneCount = 0;
        int newCount = 0;
        for (int subtasksId : subtasksIds) {
            switch (subtasksStorage.get(subtasksId).getStatus()) {
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
        Epic epic = epicsStorage.get(indexEpic);
        List<Integer> subtasksIds = epic.getSubtasksIds();
        if (epic.getSubtasksIds() == Collections.EMPTY_LIST){
            Instant startTime = subtasksStorage.get(subtasksIds.get(0)).getStartTime();
        Instant endTime = subtasksStorage.get(subtasksIds.get(0)).getEndTime();
        for (Integer subtasksId : subtasksIds) {
            if (subtasksStorage.get(subtasksId).getStartTime().isBefore(startTime))
                startTime = subtasksStorage.get(subtasksId).getStartTime();

            if (subtasksStorage.get(subtasksId).getEndTime().isAfter(endTime))
                endTime = subtasksStorage.get(subtasksId).getEndTime();
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

    public Map<Integer, Task> getTasksStorage() {
        return tasksStorage;
    }

    public Map<Integer, Subtask> getSubtasksStorage() {
        return subtasksStorage;
    }

    public Map<Integer, Epic> getEpicsStorage() {
        return epicsStorage;
    }

    public HistoryManager getHistoryManager() {
        return historyManager;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}

