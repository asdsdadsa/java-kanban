package node;

public class Node<E> {

    public E dataTask;
    public Node<E> next;
    public Node<E> prev;

    public Node(Node<E> prev, E dataTask, Node<E> next) {
        this.dataTask = dataTask;
        this.next = next;
        this.prev = prev;
    }
}