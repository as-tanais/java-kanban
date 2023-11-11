package manager;

import enums.Node;
import tasks.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager{

    private final Map<Integer, Node<Task>> receivedTasksMap;
    private final int HISTORY_LOG_SIZE = 10;
    private ArrayList<Task> history = new ArrayList<Task>();
    private Node<Task> head;
    private Node<Task> tail;

    public InMemoryHistoryManager() {
        receivedTasksMap = new HashMap<>();
    }

    @Override
    public void add(Task task) {
        if (task != null) {
            if (history.size() > HISTORY_LOG_SIZE) {
                history.remove(0);
            }
        }
        history.add(task);

    }

    @Override
    public void remove(int id) {
        removeNode(receivedTasksMap.get(id));
    }


    @Override
    public List<Task> getHistory() {
        return List.copyOf(history);
    }

    private void linkLast(Task task) {
        if (receivedTasksMap.containsKey(task.getId())) {
            removeNode(receivedTasksMap.get(task.getId()));
        }
        final Node<Task> oldTail = tail;
        final Node<Task> newNode = new Node<>(task, tail, null);
        tail = newNode;
        receivedTasksMap.put(task.getId(), newNode);
        if (oldTail == null) {
            head = newNode;
        } else {
            oldTail.setNext(newNode);
        }
    }

    private List<Task> getTasks() {
        List<Task> tasks = new LinkedList<>();
        Node<Task> currentNode = head;
        while (currentNode != null) {
            tasks.add(currentNode.getData());
            currentNode = currentNode.getNext();
        }
        return tasks;
    }

    private void removeNode(Node<Task> node) {
        if (node != null) {
            final Node<Task> next = node.getNext();
            final Node<Task> previous = node.getPrevious();
            node.setData(null);

            if (head == node && tail == node) {
                head = null;
                tail = null;
            } else if (head == node && tail != node) {
                head = next;
                head.setPrevious(null);
            } else if (head != node && tail == node) {
                tail = previous;
                tail.setNext(null);
            } else {
                previous.setNext(next);
                next.setPrevious(previous);
            }
        }
    }

}
