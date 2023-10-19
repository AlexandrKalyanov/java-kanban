package service;

class InMemoryTaskManagerTest extends TasksManagerTest<InMemoryTaskManager> {

    @Override
    public InMemoryTaskManager createManager() {
        this.manager = Managers.getDefault();
        return this.manager;
    }


}