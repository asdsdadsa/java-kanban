import manager.HistoryManager;
import manager.Managers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Status;
import tasks.Task;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class InMemoryHistoryManagerTest {
    private HistoryManager historyManager;

    @BeforeEach
    void setUp() {
        historyManager = Managers.getDefaultHistoryManager();
    }

    ////////////////////////////////////////////////////// add and get
    @Test
    void addHistoryTest() {
        Task task = new Task(0, "test0", Status.NEW, "T", Instant.EPOCH, 0);
        Task task1 = new Task(1, "test1", Status.NEW, "T", Instant.EPOCH, 0);
        historyManager.addHistory(task);
        historyManager.addHistory(task1);
        assertEquals(List.of(task, task1), historyManager.getHistory());
    }

    @Test
    void emtyHistoryTest() {
        final List<Task> history = historyManager.getHistory();
        assertNotNull(history, "История не пустая.");
        assertEquals(0, history.size(), "История не пустая.");
    }


    @Test
    void noDuplicateTest() {
        Task task = new Task(0, "test0", Status.NEW, "T", Instant.EPOCH, 0);
        Task task1 = new Task(1, "test0", Status.NEW, "T", Instant.EPOCH, 0);
        historyManager.addHistory(task);
        historyManager.addHistory(task1);
        historyManager.addHistory(task);
        historyManager.addHistory(task1);
        assertEquals(List.of(task, task1), historyManager.getHistory());
    }

    ////////////////////////////////////////////////////////// remove
    @Test
    void removeTest() {
        Task task = new Task(0, "test0", Status.NEW, "T", Instant.EPOCH, 0);
        Task task1 = new Task(1, "test1", Status.NEW, "T", Instant.EPOCH, 0);
        Task task2 = new Task(2, "test2", Status.NEW, "T", Instant.EPOCH, 0);
        historyManager.addHistory(task);
        historyManager.addHistory(task1);
        historyManager.addHistory(task2);
        historyManager.remove(task.getId());
        assertEquals(List.of(task1, task2), historyManager.getHistory());
    }

    @Test
    void emtyrempveHistoryTest() {
        Task task = new Task(0, "test0", Status.NEW, "T", Instant.EPOCH, 0);
        historyManager.addHistory(task);
        historyManager.remove(task.getId());
        final List<Task> history = historyManager.getHistory();
        assertNotNull(history, "История не пустая.");
        assertEquals(0, history.size(), "История не пустая.");
    }

    @Test
    void removeStartMidEndTest() {
        Task task = new Task(0, "test0", Status.NEW, "T", Instant.EPOCH, 0);
        Task task1 = new Task(1, "test1", Status.NEW, "T", Instant.EPOCH, 0);
        Task task2 = new Task(2, "test2", Status.NEW, "T", Instant.EPOCH, 0);
        Task task3 = new Task(3, "test0", Status.NEW, "T", Instant.EPOCH, 0);
        Task task4 = new Task(4, "test1", Status.NEW, "T", Instant.EPOCH, 0);
        historyManager.addHistory(task);
        historyManager.addHistory(task1);
        historyManager.addHistory(task2);
        historyManager.addHistory(task3);
        historyManager.addHistory(task4);
        historyManager.remove(task.getId());
        historyManager.remove(task2.getId());
        historyManager.remove(task4.getId());
        Assertions.assertEquals(List.of(task1, task3), historyManager.getHistory());
    }
}