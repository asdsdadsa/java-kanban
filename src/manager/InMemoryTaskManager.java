package manager;

import exceptions.ErrorTypeException;
import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;

import java.util.*;

public class InMemoryTaskManager implements TaskManager, Comparator<Task> {
    private HashMap<Integer, Task> tasks;
    private HashMap<Integer, Epic> epics;
    private HashMap<Integer, Subtask> subtasks;
    public static HistoryManager historyManager;
    private Set<Task> prioritizedTasks;
    private int nextid;

    public InMemoryTaskManager() {
        prioritizedTasks = new TreeSet<>(this);
        historyManager = Managers.getDefaultHistoryManager();
        tasks = new HashMap<>();
        epics = new HashMap<>();
        subtasks = new HashMap<>();
    }

    @Override
    public int compare(Task o1, Task o2) {                                                                                 //ТЗ7
        return o1.getStartTime().compareTo(o2.getStartTime());
    }

    @Override
    public HashMap<Integer, Task> getTasks() {
        return tasks;
    }

    @Override
    public HashMap<Integer, Epic> getEpics() {
        return epics;
    }

    @Override
    public HashMap<Integer, Subtask> getSubtasks() {
        return subtasks;
    }

    public int getNextid() {
        return nextid;
    }

    @Override
    public List<Task> getHistory() {                            // История просмотров задач (изменение ТЗ5)
        return historyManager.getHistory();
    }

    @Override
    public void addTask(Task task) {                         // создание (2.4)
        task.setStatus(Status.NEW);
        task.setId(nextid++);
        tasks.put(task.getId(), task);
        addPrioritizedTasks(task);
        System.out.println("Таск создан.");
    }

    @Override
    public void addEpic(Epic epic) {
        epic.setStatus(Status.NEW);
        epic.setId(nextid++);
        epics.put(epic.getId(), epic);
        System.out.println("Эпик создан.");
    }

    @Override
    public void addSubtask(Subtask subtask) {              // и добавление сабтаск в эпик
        Epic addEpic = epics.get(subtask.getEpicId());
        subtask.setStatus(Status.NEW);
        subtask.setId(nextid++);
        subtasks.put(subtask.getId(), subtask);
        addPrioritizedTasks(subtask);
        subtask.setEpicId(addEpic.getId());
        addEpic.getSubtaskList().add(subtask.getId());
        updateEpicStatus(subtask.getEpicId());
        System.out.println("Сабтаск создан.");
    }

    @Override
    public void printAllTask() {                                     //      (2.1)
        for (Integer idForPrTask : tasks.keySet()) {
            Task valueTa = tasks.get(idForPrTask);
            System.out.println("ID " + idForPrTask + " Задача " + valueTa + ".");
        }
    }

    @Override
    public void printAllEpic() {
        for (Integer idForPrEpic : epics.keySet()) {
            Epic valueEp = epics.get(idForPrEpic);
            System.out.println("ID " + idForPrEpic + " Задача " + valueEp + ".");
        }
    }

    @Override
    public void printAllSubtask() {
        for (Integer idForPrSubtask : subtasks.keySet()) {
            Subtask valueSu = subtasks.get(idForPrSubtask);
            System.out.println("ID " + idForPrSubtask + " Задача " + valueSu + ".");
        }
    }

    @Override
    public Task taskById(int id) {                               //получение по айди (2.3)
        historyManager.addHistory(tasks.get(id));
        return tasks.get(id);
    }

    @Override
    public Subtask subtaskById(int id) {
        historyManager.addHistory(subtasks.get(id));
        return subtasks.get(id);
    }

    @Override
    public Epic epicById(int id) {
        historyManager.addHistory(epics.get(id));
        return epics.get(id);
    }

    @Override
    public void updateTask(Task task) {                    //обновление  (2.5)
        tasks.put(task.getId(), task);
        addPrioritizedTasks(task);
        System.out.println("Task обновлен.");
    }

    @Override
    public void updateEpic(Epic epic) {
        epics.put(epic.getId(), epic);
        System.out.println("Epic обновлен.");
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        Epic epicUpdate = epics.get(subtask.getEpicId());
        subtasks.put(subtask.getId(), subtask);
        addPrioritizedTasks(subtask);
        epics.put(epicUpdate.getId(), epicUpdate);
        updateEpicStatus(subtask.getEpicId());
        System.out.println("Subtask обновлен.");
    }

    @Override
    public void deleteTask(int id) {                         // удаление задач по id (2.6)
        tasks.remove(id);
        historyManager.remove(id);
        for (Task taskpri : prioritizedTasks) {
            if (taskpri.getId() == id) {
                prioritizedTasks.remove(taskpri);
            }
        }
        System.out.println("Таск " + id + " удалён.");
    }

    @Override
    public void deleteEpic(int epicId) {
        Epic epicDeleteEpic = epics.get(epicId);
        for (Integer subtasksСycle : epicDeleteEpic.getSubtaskList()) {
            for (Task epicpri : prioritizedTasks) {
                if (epicpri.getId() == epicId) {
                    prioritizedTasks.remove(epicpri);
                }
            }
            subtasks.remove(subtasksСycle);
            historyManager.remove(subtasksСycle);
        }
        epics.remove(epicId);
        historyManager.remove(epicId);
        System.out.println("Эпик " + epicId + " удалён.");
    }

    @Override
    public void deleteSubtask(int id) {
        Subtask subtaskDelete = subtasks.get(id);
        Epic epicDelete = epics.get(subtaskDelete.getEpicId());
        prioritizedTasks.remove(subtaskDelete);
        epicDelete.getSubtaskList().remove(subtaskDelete.getId());
        subtasks.remove(id);
        updateEpicStatus(subtaskDelete.getEpicId());
        historyManager.remove(id);
        System.out.println("Сабтаск " + id + " удалён.");
    }

    @Override
    public void clearAll() {                           // удаление всего       (2.2)
        prioritizedTasks.clear();
        tasks.clear();
        subtasks.clear();
        epics.clear();
        System.out.println("Всё удалено.");
    }

    @Override
    public void subtasksByEpic(int epicId) {                    // (3.1) Получение списка всех подзадач определённого эпика.
        Epic printSub = epics.get(epicId);
        for (Integer subtusks : printSub.getSubtaskList()) {
            System.out.println(subtasks.get(subtusks));
        }
    }

    @Override
    public void updateEpicStatus(int id) {                         //Обновление статуса Эпика
        Epic epicStatus = epics.get(id);
        int newStastus = 0;
        int doneStatus = 0;
        if (epicStatus.getSubtaskList().isEmpty()) {
            epicStatus.setStatus(Status.NEW);
            return;
        }
        for (Integer idSubtusks : epicStatus.getSubtaskList()) {
            if (subtasks.get(idSubtusks).getStatus() == Status.NEW) {
                newStastus = newStastus + 1;
            }
            if (subtasks.get(idSubtusks).getStatus() == Status.DONE) {
                doneStatus = doneStatus + 1;
            }
        }
        if (epicStatus.getSubtaskList().size() == newStastus) {
            epicStatus.setStatus(Status.NEW);
            return;
        } else if (epicStatus.getSubtaskList().size() == doneStatus) {
            epicStatus.setStatus(Status.DONE);
            return;
        }
        epicStatus.setStatus(Status.IN_PROGRESS);
    }

    public List<Task> getPrioritizedTasks() {                                                                           //ТЗ7
        return new ArrayList<>(prioritizedTasks);
    }

    public void addPrioritizedTasks(Task task) {
        prioritizedTasks.add(task);
        foundIntersection();
    }

    public void foundIntersection() {
        List<Task> prioritizedTasks = getPrioritizedTasks();
        for (int i = 1; i < prioritizedTasks.size(); i++) {
            Task prioritizedTask = prioritizedTasks.get(i);
            if (prioritizedTask.getStartTime().isBefore(prioritizedTasks.get(i - 1).getEndTime())) {
                throw new ErrorTypeException("Пересечение " + prioritizedTasks.get(i) + " и " + prioritizedTasks.get(i - 1));
            }
        }
    }
}