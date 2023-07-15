package service;

public class ChoosingManager implements Managers{
    @Override
    public TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    @Override
    public HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
