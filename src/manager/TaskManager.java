package manager;

import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.util.HashMap;
import java.util.List;

public interface TaskManager {


    List<Task> getHistory();

    HashMap<Integer, Task> getTasks();

    HashMap<Integer, Epic> getEpics();

    HashMap<Integer, Subtask> getSubtasks();

    void addTask(Task task);

    void addEpic(Epic epic);

    void addSubtask(Subtask subtask);

    void printAllTask();

    void printAllEpic();

    void printAllSubtask();

    Task taskById(int id);

    Subtask subtaskById(int id);

    Epic epicById(int id);

    void updateTask(Task task);

    void updateEpic(Epic epic);

    void updateSubtask(Subtask subtask);

    void deleteTask(int id);

    void deleteEpic(int epicId);

    void deleteSubtask(int id);

    void clearAll();

    void subtasksByEpic(int epicId);

    void updateEpicStatus(int id);

    void addPrioritizedTasks(Task task);

    List<Task> getPrioritizedTasks();
}
