package org.example;

public class Node<T> {
    T data;
    Node<T> left;
    Node<T> right;
    int height;
    boolean color; // true for red, false for black

    public Node(T data) {
        this.data = data;
        this.height = 1;
        this.left = null;
        this.right = null;
        this.color = true; // new nodes are red by default
    }
    public Node(T data, boolean color) {
        this.data = data;
        this.height = 1;
        this.left = null;
        this.right = null;
        this.color = color; 
    }
    

    // Optional: toString method for easier debugging
    @Override
    public String toString() {
        return data.toString();
    }
}