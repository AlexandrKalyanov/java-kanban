package service;

import model.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {
    private Node first;
    private Node last;

    private final Map<Integer, Node> map = new HashMap<>();


    @Override
    public List<Task> getHistory() {
        List<Task> result = new ArrayList<>();
        if (first == null) {
            System.out.println("List is empty");
            return result;
        }
        Node current = first;

        while (current != null) {
            result.add(current.getValue());
            current = current.next;
        }
        return result;
    }

    @Override
    public void add(Task task) {
        if (map.containsKey(task.getId())) {
            remove(task.getId());
        }
        if (task == null) {
            return;
        }
        Node node = new Node(task);

        if (last != null) {
            last.next = node;
            node.prev = last;

        } else {
            first = node;

        }
        last = node;
        map.put(task.getId(), node);
    }

    @Override
    public void remove(int id) {
        Node current = map.remove(id);
        ;
        if (current == last && current == first) {
            last = null;
            first = null;
        } else if (current == last) {
            last = current.prev;
        } else if (current == first) {
            first = current.next;
        } else {
            current.prev.next = current.next;
            current.next.prev = current.prev;
        }

    }


    private static class Node {

        private Task value;
        private Node next;
        private Node prev;

        public Node(Task value) {
            this.value = value;
        }

        public Task getValue() {
            return value;
        }

        public Node getNext() {
            return next;
        }

        public void setNext(Node next) {
            this.next = next;
        }

        public Node getPrev() {
            return prev;
        }

        public void setPrev(Node prev) {
            this.prev = prev;
        }
    }
}