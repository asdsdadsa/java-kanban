import manager.HttpTasksManager;
import manager.TaskManager;
import servers.KVServer;
import manager.Managers;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import tasks.TaskTypes;

import java.io.IOException;
import java.time.Instant;

public class Main {
    public static void main(String[] args) throws IOException {
      /*  TaskManager taskManager = Managers.getDefault();
        Task task = new Task("Урок", "12-00"); // проверка прошлых ТЗ
        taskManager.addTask(task);
        System.out.println(task);
        task.setDescription("12-01");
        taskManager.updateTask(task);
        System.out.println(task);

        Task task1 = new Task("Отдых", "16-00");
        taskManager.addTask(task1);
        System.out.println(task);
        System.out.println(task1);

        Epic epic = new Epic("Купить машину", "15-00");
        taskManager.addEpic(epic);
        System.out.println(epic);

        Subtask subtask = new Subtask("Заехать в автосалон", "14-00", 2);
        taskManager.addSubtask(subtask);
        Subtask subtask1 = new Subtask("Заехать в кафе", "13-00", 2);
        taskManager.addSubtask(subtask1);
        System.out.println(epic);

        Epic epic1 = new Epic("Заехать домой", "15-00");
        taskManager.addEpic(epic1);
        Subtask subtask2 = new Subtask("Взять ноутбук", "15-10", 5);
        taskManager.addSubtask(subtask2);
        taskManager.printAllSubtask();
        taskManager.subtasksByEpic(5);
        subtask2.setStatus(Status.DONE);
        taskManager.updateSubtask(subtask2);
        System.out.println(subtask2);
        System.out.println(epic1);

        // ТЗ5 проверки
        System.out.println("1 часть");
        System.out.println(taskManager.getHistory());
        System.out.println(taskManager.getHistory().size());
        Epic epic3 = new Epic("1", "3");
        taskManager.addEpic(epic3);
        Epic epic4 = new Epic("2", "0");
        taskManager.addEpic(epic4);
        Subtask subtask3 = new Subtask("q", "q", 7);
        Subtask subtask4 = new Subtask("W", "W", 7);
        Subtask subtask5 = new Subtask("E", "E", 7);
        taskManager.addSubtask(subtask3);
        taskManager.addSubtask(subtask4);
        taskManager.addSubtask(subtask5);
        System.out.println(epic3);
        System.out.println(epic4);
        System.out.println(subtask3);
        System.out.println(subtask4);
        System.out.println(subtask5);
        System.out.println("2 часть");
        taskManager.subtaskById(10);
        taskManager.subtaskById(9);
        taskManager.subtaskById(11);
        taskManager.epicById(7);
        taskManager.epicById(8);
        System.out.println("3 часть");
        taskManager.epicById(8);
        System.out.println(taskManager.getHistory());
        System.out.println(taskManager.getHistory().size());
        taskManager.epicById(7);
        System.out.println(taskManager.getHistory());
        System.out.println(taskManager.getHistory().size());
        taskManager.subtaskById(9);
        System.out.println(taskManager.getHistory());
        System.out.println(taskManager.getHistory().size());
        taskManager.subtaskById(10);
        System.out.println(taskManager.getHistory());
        System.out.println(taskManager.getHistory().size());
        taskManager.subtaskById(11);
        System.out.println(taskManager.getHistory());
        System.out.println(taskManager.getHistory().size());
        System.out.println("4 часть");
        taskManager.deleteEpic(8);
        System.out.println(taskManager.getHistory());
        System.out.println(taskManager.getHistory().size());
        System.out.println("5 часть");
        taskManager.deleteEpic(7);
        System.out.println(taskManager.getHistory());
        System.out.println(taskManager.getHistory().size());
    }
   */
        new KVServer().start();
        TaskManager httpManager = Managers.getDefault();

        Task task1 = new Task("tl", "1tl", Instant.ofEpochSecond(564545), 54575);
        httpManager.addTask(task1);
        Epic epic1 = new Epic("el", "1el", TaskTypes.EPIC);
        httpManager.addEpic(epic1);
        Subtask subtask1 = new Subtask("sl", "1sl", Instant.EPOCH, 555, epic1.getId());
        httpManager.addSubtask(subtask1);
        Subtask subtask2 = new Subtask("sl1", "2sl", Instant.ofEpochSecond(88888), 888, epic1.getId());
        httpManager.addSubtask(subtask2);
        httpManager.taskById(task1.getId());
        httpManager.epicById(epic1.getId());
        httpManager.subtaskById(subtask1.getId());
        httpManager.subtaskById(subtask2.getId());
        httpManager.getHistory();

        HttpTasksManager qwe = new HttpTasksManager();
        qwe.load();
        qwe.getPrioritizedTasks();
    }
}
