package service;

class InMemoryTaskManagerTest extends TasksManagerTest<InMemoryTaskManager> {

    @Override
    public InMemoryTaskManager createManager() {
        this.manager = Managers.getInMemoryTaskManager();
        return this.manager;
    }


}