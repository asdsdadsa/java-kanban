package tasks;

import java.time.Instant;
import java.util.Objects;

public class Subtask extends Task {
    private int epicId = 0;

    public Subtask(String name, String description, Instant startTime, long duration, int epicId) {
        super(name, description, startTime, duration);
        this.taskTypes = TaskTypes.SUBTASK;
        this.epicId = epicId;
    }

    public Subtask(int id, String name, Status status, String description, Instant startTime, long duration, int epicId) {
        super(name, description, startTime, duration);
        this.epicId = epicId;
        this.taskTypes = TaskTypes.SUBTASK;
        setStatus(status);
        setId(id);
    }

    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }

    @Override
    public String toString() {
        return getId() + ","
                + taskTypes + ","
                + getName() + ","
                + getStatus() + ","
                + getDescription() + ","
                + getStartTime() + ","
                + getDuration() + ","
                + getEndTime() + ","
                + epicId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Subtask subtask = (Subtask) o;
        return epicId == subtask.epicId && taskTypes == subtask.taskTypes;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), epicId, taskTypes);
    }
}
