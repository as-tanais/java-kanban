package manager;


import manager.utils.Node;
import tasks.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {

    public final Map <Integer, Node<Task>> historyTaskMap = new HashMap<>();
    private Node<Task> head;
    private Node<Task> tail;


    @Override
    public void add(Task task) {
        if (historyTaskMap.containsKey(task.getId())) {
            removeNode(historyTaskMap.remove(task.getId()));
        }
        linkLast(task);
        historyTaskMap.put(task.getId(), tail);

    }

    @Override
    public void remove(int id) {
        removeNode(historyTaskMap.get(id));
    }

    @Override
    public List<Task> getHistory() {
        return getTasks();
    }


    private void linkLast (Task task){

        final Node<Task> oldTail = tail;
        final Node<Task> newNode = new Node<>(tail, task, null);

        tail = newNode;
        if(oldTail == null){
            head = newNode;
        } else {
            oldTail.next = newNode;
        }
    }

    private List<Task> getTasks(){
        List<Task> tasks = new ArrayList<>();
        Node<Task> currentNode = head;
        while (currentNode != null){
            tasks.add(currentNode.data);
            currentNode = currentNode.next;
        }
        return tasks;
    }

    private void removeNode(Node<Task> node) {
        if (node != null) {
            final Node<Task> next = node.next;
            final Node<Task> previous = node.prev;

            if (head.equals(node) && tail.equals(node)) {
                head = null;
                tail = null;
            } else if (head.equals(node) && !(tail.equals(node))) {
                head = next;
                head.prev = null;
            } else if (!(head.equals(node)) && tail.equals(node)) {
                tail = previous;
                tail.next = null;
            } else {
                previous.next = next;
                next.prev = previous;
            }
        }
    }

}