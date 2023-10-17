import service.FileBackedTasksManager;
import service.InMemoryTaskManager;
import service.Managers;

import java.io.IOException;

public class FileBackedTasksManagerTest extends TasksManagerTest<FileBackedTasksManager> {

    @Override
    public FileBackedTasksManager createManager(){
        manager = Managers.BackedTasksManager();
        return manager;
    }
}
