package tasks;

import java.time.Instant;
import java.util.Objects;

public class Task {
    private String name;
    private String description;
    private Integer id;
    private Status status;
    protected TaskTypes taskTypes;
    private long duration;
    private Instant startTime;


    public Task(String name, String description, Instant startTime, long duration) {
        this.taskTypes = TaskTypes.TASK;
        this.name = name;
        this.description = description;
        this.status = Status.NEW;
        this.startTime = startTime;
        this.duration = duration;
    }

    public Task(Integer id, String name, Status status, String description, Instant startTime, long duration) {
        this.taskTypes = TaskTypes.TASK;
        this.name = name;
        this.description = description;
        this.status = status;
        this.id = id;
        this.startTime = startTime;
        this.duration = duration;
    }

    public Instant getEndTime() {
        int seconds = 60;
        return startTime.plusSeconds(duration * seconds);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public TaskTypes getType() {
        return taskTypes;
    }

    public void setType(TaskTypes taskTypes) {
        this.taskTypes = taskTypes;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public Instant getStartTime() {
        return startTime;
    }

    public void setStartTime(Instant startTime) {
        this.startTime = startTime;
    }

    @Override
    public String toString() {
        return id + ","
                + taskTypes + ","
                + name + ","
                + status + ","
                + description + ","
                + getStartTime() + ","
                + duration + ","
                + getEndTime();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return duration == task.duration && Objects.equals(name, task.name) && Objects.equals(description, task.description) && Objects.equals(id, task.id) && status == task.status && taskTypes == task.taskTypes && Objects.equals(startTime, task.startTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, id, status, taskTypes, duration, startTime);
    }
}



