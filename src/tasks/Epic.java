package tasks;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Objects;

public class Epic extends Task {
    private ArrayList<Integer> idSubtaskList;
    private Instant endTime = Instant.ofEpochSecond(0);

    public Epic(String name, String description, TaskTypes taskTypes) {
        super(name, description, Instant.ofEpochSecond(0), 0);
        this.idSubtaskList = new ArrayList<>();
        this.setType(taskTypes);
    }

    public Epic(int id, String name, Status status, String description, Instant startTime, long duration) {
        super(name, description, startTime, duration);
        this.endTime = super.getEndTime();
        this.idSubtaskList = new ArrayList<>();
        this.setType(TaskTypes.EPIC);
        this.setStatus(status);
        this.setId(id);
    }

    public ArrayList<Integer> getSubtaskList() {
        return idSubtaskList;
    }

    public void setSubtaskList(ArrayList<Integer> idSubtaskList) {
        this.idSubtaskList = idSubtaskList;
    }


    @Override
    public String toString() {
        return getId() + ","
                + getType() + ","
                + getName() + ","
                + getStatus() + ","
                + getDescription() + ","
                + getStartTime() + ","
                + getDuration() + ","
                + getEndTime();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Epic epic = (Epic) o;
        return Objects.equals(idSubtaskList, epic.idSubtaskList) && Objects.equals(endTime, epic.endTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), idSubtaskList, endTime);
    }
}

