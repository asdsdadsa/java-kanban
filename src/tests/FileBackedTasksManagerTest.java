import manager.FileBackedTasksManager;
import manager.Managers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import tasks.TaskTypes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FileBackedTasksManagerTest extends TaskManagerTest<FileBackedTasksManager> {
    private Path filePath = Path.of("file");

    @BeforeEach
    void setUp() {
        taskManager = Managers.getDefaultFileBackedManager();
    }

    ////////////////////////////////////////////////////// save
    @Test
    void emptyTasksTest() throws IOException {
        List<String> resoult = Files.readAllLines(filePath);
        resoult.clear();
        assertEquals("[]", resoult.toString());
    }

    @Test
    void saveTaskTest() throws IOException {
        Task task = new Task("test0", "TFILE", Instant.EPOCH, 0);
        taskManager.addTask(task);
        taskManager.taskById(task.getId());
        String resoult = Files.readAllLines(filePath).toString();

        assertEquals("[id,type,name,status,description,startTime,duration,endTime,epic, " +
                "0,TASK,test0,NEW,TFILE,1970-01-01T00:00:00Z,0,1970-01-01T00:00:00Z, , 0,]", resoult);
    }

    @Test
    void saveNoHistoryTest() throws IOException {
        Task task = new Task("test0", "TFILE", Instant.EPOCH, 0);
        taskManager.addTask(task);
        String resoult = Files.readAllLines(filePath).toString();
        assertEquals("[id,type,name,status,description,startTime,duration,endTime,epic, " +
                "0,TASK,test0,NEW,TFILE," + "1970-01-01T00:00:00Z,0,1970-01-01T00:00:00Z, ]", resoult);
    }

    @Test
    void saveEpicNpSubtasksTest() throws IOException {
        Epic epic = new Epic("test0", "E", TaskTypes.EPIC);
        taskManager.addEpic(epic);
        String resoult = Files.readAllLines(filePath).toString();
        assertEquals("[id,type,name,status,description,startTime,duration,endTime,epic, " +
                "0,EPIC,test0,NEW,E,1970-01-01T00:00:00Z,0,1970-01-01T00:00:00Z, ]", resoult);
    }

    ///////////////////////////////////////////////// load
    @Test
    void loadTest() {
        Task task = new Task("test0", "TFILE", Instant.EPOCH, 0);
        taskManager.addTask(task);
        Epic epic = new Epic("test0", "E", TaskTypes.EPIC);
        taskManager.addEpic(epic);
        Subtask subtask = new Subtask("test0", "S", Instant.EPOCH, 0, epic.getId());
        taskManager.addSubtask(subtask);
        taskManager.taskById(task.getId());
        taskManager.epicById(epic.getId());
        taskManager.subtaskById(subtask.getId());
        taskManager = FileBackedTasksManager.load(filePath);
        assertEquals("[0,TASK,test0,NEW,TFILE,1970-01-01T00:00:00Z,0,1970-01-01T00:00:00Z]",
                taskManager.getTasks().values().toString());
        assertEquals("[1,EPIC,test0,NEW,E,1970-01-01T00:00:00Z,0,1970-01-01T00:00:00Z]",
                taskManager.getEpics().values().toString());
        assertEquals("[2,SUBTASK,test0,NEW,S,1970-01-01T00:00:00Z,0,1970-01-01T00:00:00Z,1]",
                taskManager.getSubtasks().values().toString());
        assertEquals(List.of(task, epic, subtask), taskManager.getHistory());
    }

    @Test
    void loadEmpyTest() throws IOException {
        Task task = new Task("test0", "TFILE", Instant.EPOCH, 0);
        taskManager.addTask(task);
        taskManager.taskById(task.getId());
        taskManager.deleteTask(task.getId());
        taskManager = FileBackedTasksManager.load(filePath);
        assertEquals(true, taskManager.getTasks().isEmpty());
    }

    @Test
    void loadNoHistoryTest() {
        Task task = new Task("test0", "TFILE", Instant.EPOCH, 0);
        taskManager.addTask(task);
        taskManager = FileBackedTasksManager.load(filePath);
        assertEquals(true, taskManager.getHistory().isEmpty());
    }

    @Test
    void loadEpicNoSubtasksTest() {
        Epic epic = new Epic("test0", "E", TaskTypes.EPIC);
        taskManager.addEpic(epic);
        taskManager.epicById(epic.getId());
        taskManager = FileBackedTasksManager.load(filePath);
        assertEquals("[0,EPIC,test0,NEW,E,1970-01-01T00:00:00Z,0,1970-01-01T00:00:00Z]",
                taskManager.getEpics().values().toString());
    }

    ////////////////////////////////////////////////////////////// formatting
    @Test
    void historyFromStringTest() {
        Task task = new Task("test0", "T", Instant.EPOCH, 0);
        Task task1 = new Task("test1", "T", Instant.EPOCH, 0);
        taskManager.addTask(task);
        taskManager.addTask(task1);
        Integer id = task.getId();
        Integer id1 = task1.getId();
        assertEquals(List.of(id, id1), taskManager.historyFromString("0,1"));
    }

    @Test
    void historyToStringTest() {
        Task task = new Task("test0", "T", Instant.EPOCH, 0);
        Task task1 = new Task("test1", "T", Instant.EPOCH, 0);
        taskManager.addTask(task);
        taskManager.taskById(task.getId());
        taskManager.addTask(task1);
        taskManager.taskById(task1.getId());
        assertEquals(task.getId() + "," + task1.getId() + ",", taskManager.historyToString());
    }

    @Test
    void tasksFromStringTest() {
        Task task = new Task("test0", "T", Instant.EPOCH, 0);
        taskManager.addTask(task);
        Task formTask = taskManager.taskFromString("0,TASK,test0,NEW,T,1970-01-01T00:00:00Z,0");
        assertEquals(formTask, task);
    }

    @Test
    void tasksToStringTest() {
        Task task = new Task("test0", "T", Instant.EPOCH, 0);
        Task task1 = new Task("test1", "T", Instant.EPOCH, 0);
        taskManager.addTask(task);
        taskManager.addTask(task1);
        assertEquals(task + "\n" + task1 + "\n", taskManager.taskToString(taskManager));
    }
}