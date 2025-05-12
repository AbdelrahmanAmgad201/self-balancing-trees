package org.example;

public class Node<T> {
    T data;
    Node<T> left;
    Node<T> right;
    int height;

    public Node(T data) {
        this.data = data;
        this.height = 1;
        this.left = null;
        this.right = null;
    }

    // Optional: toString method for easier debugging
    @Override
    public String toString() {
        return data.toString();
    }
}