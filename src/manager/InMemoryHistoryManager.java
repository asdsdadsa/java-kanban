package manager;

import node.Node;
import tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {

    private final CustomLinkedList taskHistory;

    private final Map<Integer, Node<Task>> nodesByTaskId;

    public InMemoryHistoryManager() {
        taskHistory = new CustomLinkedList();
        nodesByTaskId = new HashMap<>();
    }

    @Override
    public void addHistory(Task task) {
        Node<Task> node = taskHistory.linkLast(task);
        if (nodesByTaskId.containsKey(task.getId())) {
            taskHistory.removeNode(nodesByTaskId.get(task.getId()));
        }
        nodesByTaskId.put(task.getId(), node);
    }

    // удаление по id
    @Override
    public void remove(int id) {
        taskHistory.removeNode(nodesByTaskId.get(id));
        nodesByTaskId.remove(id);
    }

    // получение истории
    @Override
    public List<Task> getHistory() {
        return taskHistory.getTasks();
    }

    class CustomLinkedList {

        private Node<Task> head;
        private Node<Task> tail;

        public Node<Task> linkLast(Task task) {
            Node<Task> newNode = new Node<>(tail, task, null);
            if (tail == null) {
                head = newNode;
            } else {
                tail.next = newNode;
            }
            tail = newNode;
            return newNode;
        }

        public List<Task> getTasks() {
            List<Task> tasks = new ArrayList<>();
            Node<Task> element = head;
            while (element != null) {
                tasks.add(element.dataTask);
                element = element.next;
            }
            return tasks;
        }

        public void removeNode(Node<Task> node) {
            if (node.equals(head)) {
                head = node.next;
                if (node.next != null)
                    node.next.prev = null;
            } else {
                node.prev.next = node.next;
                if (node.next != null)
                    node.next.prev = node.prev;
            }
        }

    }
}