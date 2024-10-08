package manager;

import exceptions.ErrorTypeException;
import exceptions.ManagerSaveException;
import tasks.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;


public class FileBackedTasksManager extends InMemoryTaskManager implements TaskManager {
    private final Path file = Path.of("file");


    @Override
    public void addTask(Task task) {
        super.addTask(task);
        save();
    }

    @Override
    public void addEpic(Epic epic) {
        super.addEpic(epic);
        save();
    }

    @Override
    public void addSubtask(Subtask subtask) {
        super.addSubtask(subtask);
        save();
    }

    @Override
    public Task taskById(int id) {
        Task savedTask = super.taskById(id);
        save();
        return savedTask;
    }

    @Override
    public Subtask subtaskById(int id) {
        Subtask savedSubtask = super.subtaskById(id);
        save();
        return savedSubtask;
    }

    @Override
    public Epic epicById(int id) {
        Epic savedEpic = super.epicById(id);
        save();
        return savedEpic;
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
        save();
    }

    @Override
    public void deleteTask(int id) {
        super.deleteTask(id);
        save();
    }

    @Override
    public void deleteEpic(int epicId) {
        super.deleteEpic(epicId);
        save();
    }

    @Override
    public void deleteSubtask(int id) {
        super.deleteSubtask(id);
        save();
    }

    @Override
    public void clearAll() {
        super.clearAll();
        save();
    }

    @Override
    public void subtasksByEpic(int epicId) {
        super.subtasksByEpic(epicId);
        save();
    }

    @Override
    public void updateEpicStatus(int id) {
        super.updateEpicStatus(id);
        save();
    }


    public void save() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file.toFile()));
             BufferedReader br = new BufferedReader(new FileReader(file.toFile()))) {
            if (br.readLine() == null) {
                String graphs = "id,type,name,status,description,startTime,duration,endTime,epic" + "\n";
                bw.write(graphs);
            }
            String values = taskToString(this) + "\n" + historyToString();
            bw.write(values);
        } catch (IOException e) {
            e.printStackTrace();
            throw new ManagerSaveException("Ошибка в сохранении");
        }
    }


    public String taskToString(InMemoryTaskManager inMemoryTaskManager) {
        StringBuilder tasksBuild = new StringBuilder();
        List<Task> tasksToString = new ArrayList<>();
        tasksToString.addAll(inMemoryTaskManager.getTasks().values());
        tasksToString.addAll(inMemoryTaskManager.getEpics().values());
        tasksToString.addAll(inMemoryTaskManager.getSubtasks().values());
        for (Task tasks : tasksToString) {
            tasksBuild.append(tasks.toString()).append("\n");
        }
        return tasksBuild.toString();
    }

    public static Task taskFromString(String value) {                                                                     //ТЗ7
        String[] elements = value.split(",");
        int id = Integer.parseInt(elements[0]);
        TaskTypes taskTypes = TaskTypes.valueOf(elements[1]);
        String name = elements[2];
        Status status = Status.valueOf(elements[3]);
        String description = elements[4];
        Instant startTime = Instant.parse(elements[5]);
        long duration = Long.parseLong(elements[6]);
        int epicID = 0;
        if (taskTypes.equals(TaskTypes.SUBTASK)) {
            epicID = Integer.parseInt(elements[8]);
        }
        if (taskTypes.equals(TaskTypes.TASK)) {
            return new Task(id, name, status, description, startTime, duration);
        }
        if (taskTypes.equals(TaskTypes.EPIC)) {
            return new Epic(id, name, status, description, startTime, duration);
        }
        if (taskTypes.equals(TaskTypes.SUBTASK)) {
            return new Subtask(id, name, status, description, startTime, duration, epicID);
        } else {
            throw new ErrorTypeException("Данный формат таска не поддерживается");
        }
    }

    public static String historyToString() {
        StringBuilder historyBuild = new StringBuilder();
        for (Task value : historyManager.getHistory()) {
            historyBuild.append(value.getId()).append(",");
        }
        return historyBuild.toString();
    }

    public static List<Integer> historyFromString(String value) {
        List<Integer> hitoryString = new ArrayList<>();
        String[] historyElements = value.split(",");
        for (String line : historyElements) {
            hitoryString.add(Integer.parseInt(line));
        }
        return hitoryString;
    }

    public static FileBackedTasksManager load(Path path) {
        FileBackedTasksManager fileBackedTasksManager = Managers.getDefaultFileBackedManager();
        try {
            String fileName = Files.readString(path);
            String[] lines = fileName.split("\n");
            for (int i = 1; i < lines.length - 2; i++) {
                Task task = taskFromString(lines[i]);
                String type = lines[i].split(",")[1];
                if (TaskTypes.valueOf(type).equals(TaskTypes.TASK)) {
                    fileBackedTasksManager.addTask(task);
                    historyManager.addHistory(fileBackedTasksManager.taskById(task.getId()));
                }
                if (TaskTypes.valueOf(type).equals(TaskTypes.EPIC)) {
                    Epic epic = (Epic) task;
                    fileBackedTasksManager.addEpic(epic);
                    historyManager.addHistory(fileBackedTasksManager.epicById(epic.getId()));
                }
                if (TaskTypes.valueOf(type).equals(TaskTypes.SUBTASK)) {
                    Subtask subtask = (Subtask) task;
                    fileBackedTasksManager.addSubtask(subtask);
                    historyManager.addHistory(fileBackedTasksManager.subtaskById(subtask.getId()));
                }
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка загрузки из файла");
        }
        return fileBackedTasksManager;
    }
}



