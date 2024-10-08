import manager.TaskManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import tasks.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public abstract class TaskManagerTest<TTaskManager extends TaskManager> {
    public TTaskManager taskManager;
    HashMap<Integer, Task> empty = new HashMap<>();

    ///////////////////////////////////////////// add
    @Test
    void addTaskTest() {
        //Given
        Task task = new Task("test0", "T", Instant.EPOCH, 0);
        //When
        taskManager.addTask(task);
        final Task savedTask = taskManager.taskById(task.getId());
        //Then
        assertEquals(task, savedTask, "Задачи не совпадают.");
        assertNotNull(task, "Задача не найдена.");

        final HashMap<Integer, Task> tasks = taskManager.getTasks();
        assertNotNull(tasks, "Задачи на возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(task, tasks.get(0), "Задачи не совпадают.");
    }

    @Test
    void addEpicTest() {
        Epic epic = new Epic("test0", "E", TaskTypes.EPIC);

        taskManager.addEpic(epic);
        final Epic savedEpic = taskManager.epicById(epic.getId());

        assertEquals(epic, savedEpic, "Задачи не совпадают.");
        assertNotNull(epic, "Задача не найдена.");

        final HashMap<Integer, Epic> epics = taskManager.getEpics();
        assertNotNull(epics, "Задачи на возвращаются.");
        assertEquals(1, epics.size(), "Неверное количество задач.");
        assertEquals(epic, epics.get(0), "Задачи не совпадают.");
    }

    @Test
    void addSubtaskTest() {
        Epic epic = new Epic("test1", "E", TaskTypes.EPIC);
        taskManager.addEpic(epic);
        Subtask subtask = new Subtask("test0", "S", Instant.EPOCH, 0, epic.getId());
        taskManager.addSubtask(subtask);
        final Subtask savedSubtask = taskManager.subtaskById(subtask.getId());

        assertEquals(subtask, savedSubtask, "Задачи не совпадают.");
        assertNotNull(subtask, "Задача не найдена.");

        final HashMap<Integer, Subtask> subtasks = taskManager.getSubtasks();
        assertNotNull(subtasks, "Задачи на возвращаются.");
        assertEquals(1, subtasks.size(), "Неверное количество задач.");
        assertEquals(subtask, subtasks.get(1), "Задачи не совпадают.");
    }

    ////////////////////////////////////////// removeAll
    @Test
    void removeAllTest() {
        Task task = new Task("test0", "T", Instant.EPOCH, 0);
        taskManager.addTask(task);
        Epic epic = new Epic("test0", "E", TaskTypes.EPIC);
        taskManager.addEpic(epic);
        Subtask subtask = new Subtask("test0", "S", Instant.EPOCH, 0, epic.getId());
        taskManager.addSubtask(subtask);
        taskManager.clearAll();
        assertEquals(empty, taskManager.getTasks(), "Не удалено");
        assertEquals(empty, taskManager.getEpics(), "Не удалено");
        assertEquals(empty, taskManager.getSubtasks(), "Не удалено");
    }

    ///////////////////////////////////////byId
    @Test
    void taskByIdTest() {
        Task task = new Task("test0", "T", Instant.EPOCH, 0);
        Task task1 = new Task("test1", "T", Instant.EPOCH, 0);
        taskManager.addTask(task);
        taskManager.addTask(task1);
        assertEquals(taskManager.taskById(task1.getId()), task1, "Задачи не совпадают.");
        assertNotNull(taskManager.taskById(task1.getId()), "Задачи нет.");
    }


    @Test
    void epicByIdTest() {
        Epic epic = new Epic("test0", "E", TaskTypes.EPIC);
        Epic epic1 = new Epic("test1", "E", TaskTypes.EPIC);
        taskManager.addEpic(epic);
        taskManager.addEpic(epic1);
        assertEquals(taskManager.epicById(epic1.getId()), epic1, "Задачи не совпадают.");
        assertNotNull(taskManager.epicById(epic1.getId()), "Задачи нет.");
    }

    @Test
    void subtaskByIdTest() {
        Epic epic = new Epic("test0", "E", TaskTypes.EPIC);
        taskManager.addEpic(epic);
        Subtask subtask = new Subtask("test0", "S", Instant.EPOCH, 0, epic.getId());
        Subtask subtask1 = new Subtask("test1", "S", Instant.EPOCH, 0, epic.getId());
        taskManager.addSubtask(subtask);
        taskManager.addSubtask(subtask1);
        assertEquals(taskManager.subtaskById(subtask1.getId()), subtask1, "Задачи не совпадают.");
        assertNotNull(taskManager.subtaskById(subtask1.getId()), "Задачи нет.");
    }

    @Test
    void epicByIdIncorrectTest() {
        final NullPointerException exception = Assertions.assertThrows(
                NullPointerException.class,
                new Executable() {
                    @Override
                    public void execute() {
                        Epic epic = new Epic("test0", "E", TaskTypes.EPIC);
                        taskManager.addEpic(epic);
                        taskManager.epicById(50);
                    }
                });
        assertEquals(null, exception.getMessage(), "Некорректный id.");
    }

    @Test
    void subtaskByIdIncorrectTest() {
        final NullPointerException exception = Assertions.assertThrows(
                NullPointerException.class,
                new Executable() {
                    @Override
                    public void execute() {
                        Epic epic = new Epic("test0", "E", TaskTypes.EPIC);
                        taskManager.addEpic(epic);
                        Subtask subtask = new Subtask("test0", "S", Instant.EPOCH, 0, epic.getId());
                        taskManager.addSubtask(subtask);
                        taskManager.subtaskById(50);
                    }
                });
        assertEquals(null, exception.getMessage(), "Некорректный id.");
    }

    @Test
    void taskByIdIncorrectTest() {
        final NullPointerException exception = Assertions.assertThrows(
                NullPointerException.class,
                new Executable() {
                    @Override
                    public void execute() {
                        Task task = new Task("test0", "T", Instant.EPOCH, 0);
                        taskManager.addTask(task);
                        taskManager.taskById(50);
                    }
                });
        assertEquals(null, exception.getMessage(), "Некорректный id.");
    }

    ////////////////////////////////////////////// remove
    @Test
    void removeTaskTest() {
        Task task = new Task("test0", "T", Instant.EPOCH, 0);
        taskManager.addTask(task);
        taskManager.taskById(task.getId());
        taskManager.deleteTask(task.getId());
        assertEquals(empty, taskManager.getTasks(), "Не удалено.");
    }

    @Test
    void removeEpicTestNoSubtask() {
        Epic epic = new Epic("test0", "E", TaskTypes.EPIC);
        taskManager.addEpic(epic);
        taskManager.epicById(epic.getId());
        taskManager.deleteEpic(epic.getId());
        assertEquals(empty, taskManager.getEpics(), "Не удалено.");
    }

    @Test
    void removeEpicTestHaveSubtask() {
        Epic epic = new Epic("test0", "E", TaskTypes.EPIC);
        taskManager.addEpic(epic);
        Subtask subtask = new Subtask("test0", "S", Instant.EPOCH, 0, epic.getId());
        taskManager.addSubtask(subtask);
        taskManager.epicById(epic.getId());
        taskManager.subtaskById(subtask.getId());
        taskManager.deleteEpic(epic.getId());
        assertEquals(empty, taskManager.getEpics(), "Не удалено.");
        assertEquals(empty, taskManager.getSubtasks(), "Не удалено.");
    }

    @Test
    void removeSubtaskTest() {
        Epic epic = new Epic("test0", "E", TaskTypes.EPIC);
        taskManager.addEpic(epic);
        Subtask subtask = new Subtask("test0", "S", Instant.EPOCH, 0, epic.getId());
        taskManager.addSubtask(subtask);
        taskManager.subtaskById(subtask.getId());
        taskManager.epicById(epic.getId());
        taskManager.deleteSubtask(subtask.getId());
        assertEquals(empty, taskManager.getSubtasks(), "Не удалено.");
    }

    @Test
    void noEpicRemoveIfIncorrectIDTest() {
        final NullPointerException exception = Assertions.assertThrows(
                NullPointerException.class,
                new Executable() {
                    @Override
                    public void execute() {
                        Epic epic = new Epic("test0", "E", TaskTypes.EPIC);
                        taskManager.addEpic(epic);
                        taskManager.deleteEpic(50);
                    }
                });
        assertEquals(null, exception.getMessage(), "Некорректный id.");
    }

    @Test
    void noTaskRemoveIfIncorrectIDTest() {
        final NullPointerException exception = Assertions.assertThrows(
                NullPointerException.class,
                new Executable() {
                    @Override
                    public void execute() {
                        Task task = new Task("test0", "T", Instant.EPOCH, 0);
                        taskManager.addTask(task);
                        taskManager.deleteTask(50);
                    }
                });
        assertEquals(null, exception.getMessage(), "Некорректный id.");
    }

    @Test
    void noSubtaskRemoveIfIncorrectIDTest() {
        final NullPointerException exception = Assertions.assertThrows(
                NullPointerException.class,
                new Executable() {
                    @Override
                    public void execute() {
                        Epic epic = new Epic("test0", "E", TaskTypes.EPIC);
                        taskManager.addEpic(epic);
                        Subtask subtask = new Subtask("test0", "S", Instant.EPOCH, 0, epic.getId());
                        taskManager.addSubtask(subtask);
                        taskManager.deleteSubtask(50);
                    }
                });
        assertEquals(null, exception.getMessage(), "Некорректный id.");
    }

    //////////////////////////////////////////////////////////// subtasksByEpic
    @Test
    void subtasksByEpicTest() {
        Epic epic = new Epic("test0", "E", TaskTypes.EPIC);
        taskManager.addEpic(epic);
        Subtask subtask = new Subtask("test0", "S", Instant.EPOCH, 0, epic.getId());
        Subtask subtask1 = new Subtask("test1", "S", Instant.EPOCH, 0, epic.getId());
        taskManager.addSubtask(subtask);
        taskManager.addSubtask(subtask1);
        assertEquals(List.of(subtask.getId(), subtask1.getId()), epic.getSubtaskList());
    }

    @Test
    void subtasksByEpicEmptyTest() {
        List<Task> emptyList = new ArrayList<>();
        Epic epic = new Epic("test0", "E", TaskTypes.EPIC);
        assertEquals(emptyList, epic.getSubtaskList());
    }

    ///////////////////////////////////////////////////// update
    @Test
    void updateTaskStatusTest() {
        Task task = new Task("test0", "T", Instant.EPOCH, 0);
        taskManager.addTask(task);
        task.setStatus(Status.IN_PROGRESS);
        taskManager.updateTask(task);
        assertEquals(Status.IN_PROGRESS, task.getStatus());
    }

    @Test
    void updateSubtaskStatusTest() {
        Epic epic = new Epic("test0", "E", TaskTypes.EPIC);
        taskManager.addEpic(epic);
        Subtask subtask = new Subtask("test0", "S", Instant.EPOCH, 0, epic.getId());
        taskManager.addSubtask(subtask);

        subtask.setStatus(Status.IN_PROGRESS);
        taskManager.updateSubtask(subtask);

        assertEquals(Status.IN_PROGRESS, subtask.getStatus());
    }

    @Test
    void epicStatusIfEmpty() {
        Epic epic = new Epic("test0", "E", TaskTypes.EPIC);
        taskManager.addEpic(epic);
        assertEquals(Status.NEW, epic.getStatus());
    }

    @Test
    void updateEpicStateNewTest() {
        Epic epic = new Epic("test0", "E", TaskTypes.EPIC);
        taskManager.addEpic(epic);
        Subtask subtask = new Subtask("test0", "S", Instant.EPOCH, 0, epic.getId());
        taskManager.addSubtask(subtask);

        subtask.setStatus(Status.NEW);
        taskManager.updateSubtask(subtask);

        assertEquals(Status.NEW, taskManager.epicById(epic.getId()).getStatus());
    }

    @Test
    void updateEpicStateInProgressTest() {
        Epic epic = new Epic("test0", "E", TaskTypes.EPIC);
        taskManager.addEpic(epic);
        Subtask subtask = new Subtask("test0", "S", Instant.EPOCH, 0, epic.getId());
        taskManager.addSubtask(subtask);

        subtask.setStatus(Status.IN_PROGRESS);
        taskManager.updateSubtask(subtask);

        assertEquals(Status.IN_PROGRESS, taskManager.epicById(epic.getId()).getStatus());
    }

    @Test
    void updateEpic2StateInProgressTest() {
        Epic epic = new Epic("test0", "E", TaskTypes.EPIC);
        taskManager.addEpic(epic);
        Subtask subtask = new Subtask("test0", "S", Instant.EPOCH, 0, epic.getId());
        taskManager.addSubtask(subtask);
        Subtask subtask1 = new Subtask("test1", "S", Instant.EPOCH, 0, epic.getId());
        taskManager.addSubtask(subtask1);

        subtask.setStatus(Status.NEW);
        subtask1.setStatus(Status.DONE);
        taskManager.updateSubtask(subtask);
        taskManager.updateSubtask(subtask1);

        assertEquals(Status.IN_PROGRESS, taskManager.epicById(epic.getId()).getStatus());
    }

    @Test
    void updateEpictatusDoneTest() {
        Epic epic = new Epic("test0", "E", TaskTypes.EPIC);
        taskManager.addEpic(epic);
        Subtask subtask = new Subtask("test0", "S", Instant.EPOCH, 0, epic.getId());
        taskManager.addSubtask(subtask);
        subtask.setStatus(Status.DONE);

        taskManager.updateSubtask(subtask);

        assertEquals(Status.DONE, taskManager.epicById(epic.getId()).getStatus());
    }
///////////////////////////////////////////////////////////////////////////////// history

    @Test
    void returnHistoryEmptyTest() {
        List<Task> emptyList = new ArrayList<>();
        assertEquals(emptyList, taskManager.getHistory());
    }

    @Test
    void returnHistoryTest() {
        Task task = new Task("test0", "T", Instant.EPOCH, 0);
        Epic epic = new Epic("test0", "E", TaskTypes.EPIC);
        taskManager.addTask(task);
        taskManager.addEpic(epic);
        Subtask subtask = new Subtask("test0", "S", Instant.EPOCH, 0, epic.getId());
        taskManager.addSubtask(subtask);
        taskManager.taskById(task.getId());
        taskManager.epicById(epic.getId());
        taskManager.subtaskById(subtask.getId());
        assertEquals(List.of(task, epic, subtask), taskManager.getHistory());
    }

    /////////////////////////////////////////////////////////////////////////////////////// priority
    @Test
    void timeEpicWithSubtasksTest() {
        Epic epic = new Epic("test0", "E", TaskTypes.EPIC);
        taskManager.addEpic(epic);
        Subtask subtask = new Subtask("test0", "S", Instant.EPOCH, 0, epic.getId());
        Subtask subtask1 = new Subtask("test1", "S", Instant.EPOCH, 0, epic.getId());
        taskManager.addSubtask(subtask);
        taskManager.addSubtask(subtask1);

        assertEquals(subtask.getStartTime(), epic.getStartTime());
        assertEquals(subtask1.getEndTime(), epic.getEndTime());
    }

    @Test
    void addPrioritizedTasksTest() {
        Task task = new Task("test0", "T", Instant.EPOCH, 0);
        taskManager.addPrioritizedTasks(task);
        Assertions.assertEquals(task, taskManager.getPrioritizedTasks().get(0));
    }
}