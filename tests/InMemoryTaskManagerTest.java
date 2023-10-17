import org.junit.jupiter.api.BeforeEach;
import service.InMemoryTaskManager;
import service.Managers;

class InMemoryTaskManagerTest extends TasksManagerTest<InMemoryTaskManager> {

    @Override
    public InMemoryTaskManager createManager() {
        manager = Managers.getDefault();
        return manager;
    }


}