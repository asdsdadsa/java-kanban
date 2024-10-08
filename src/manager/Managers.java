package manager;


public abstract class Managers {

    public static TaskManager getDefault() {
        return new HttpTasksManager();
    }

    public static HistoryManager getDefaultHistoryManager() {
        return new InMemoryHistoryManager();
    }

    public static FileBackedTasksManager getDefaultFileBackedManager() {

        return new FileBackedTasksManager();

    }
}