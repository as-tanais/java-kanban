import manager.HistoryManager;
import manager.InMemoryHistoryManager;
import manager.Managers;
import org.junit.jupiter.api.BeforeEach;
import manager.InMemoryTaskManager;

public class InMemoryTasksManagerTest extends TaskManagerTest<InMemoryTaskManager> {

    @BeforeEach
    public void loadInitialConditions() {

        InMemoryHistoryManager historyManager = Managers.getDefaultHistory();
        manager = new InMemoryTaskManager(historyManager);

    }

}

