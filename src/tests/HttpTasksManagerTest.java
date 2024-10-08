import manager.HttpTasksManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import servers.KVServer;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import tasks.TaskTypes;

import java.io.IOException;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class HttpTasksManagerTest extends TaskManagerTest<HttpTasksManager> {

    public KVServer server;

    @BeforeEach
    public void load() throws IOException {
        server = new KVServer();
        server.start();
        taskManager = new HttpTasksManager();
    }

    @AfterEach
    void serverStop() {
        server.stop();
    }

    @Test
    void loadFromServerTest() {
        Task task1 = new Task("tl", "1tl", Instant.ofEpochSecond(564545), 54575);
        taskManager.addTask(task1);
        Epic epic1 = new Epic("el", "1el", TaskTypes.EPIC);
        taskManager.addEpic(epic1);
        Subtask subtask1 = new Subtask("sl", "1sl", Instant.EPOCH, 555, epic1.getId());
        taskManager.addSubtask(subtask1);
        Subtask subtask2 = new Subtask("sl1", "2sl", Instant.ofEpochSecond(88888), 888, epic1.getId());
        taskManager.addSubtask(subtask2);

        taskManager.taskById(task1.getId());
        taskManager.epicById(epic1.getId());
        taskManager.subtaskById(subtask1.getId());
        taskManager.subtaskById(subtask2.getId());

        taskManager.load();
        HashMap<Integer, Task> tasks = taskManager.getTasks();

        assertNotNull(tasks);
        assertEquals(1, tasks.size());

        List<Task> history = taskManager.getHistory();

        assertNotNull(history);
        assertEquals(4, history.size());

    }

}
