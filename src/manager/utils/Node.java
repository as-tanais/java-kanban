package manager.utils;

public class Node<E> {
     public E data;
     public Node<E> prev;
     public Node<E> next;

    public Node(Node<E> prev, E elemet, Node<E> next) {
        this.prev = prev;
        this.data = elemet;
        this.next = next;
    }

}