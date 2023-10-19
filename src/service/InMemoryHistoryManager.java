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
        if (this.first == null) {
            return result;
        }
        Node current = this.first;

        while (current != null) {
            result.add(current.getValue());
            current = current.next;
        }
        return result;
    }

    @Override
    public void add(Task task) {
        if (this.map.containsKey(task.getId())) {
            remove(task.getId());
        }
        if (task == null) {
            return;
        }
        Node node = new Node(task);

        if (this.last != null) {
            this.last.next = node;
            node.prev = this.last;

        } else {
            this.first = node;

        }
        this.last = node;
        this.map.put(task.getId(), node);
    }

    @Override
    public void remove(int id) {
        Node current = this.map.remove(id);
        if (current == this.last && current == this.first) {
            this.last = null;
            this.first = null;
        } else if (current == this.last) {
            this.last = current.prev;
        } else if (current == this.first) {
            this.first = current.next;
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
            return this.value;
        }

        public Node getNext() {
            return this.next;
        }

        public void setNext(Node next) {
            this.next = next;
        }

        public Node getPrev() {
            return this.prev;
        }

        public void setPrev(Node prev) {
            this.prev = prev;
        }
    }
}