package org.example;

import java.util.*;

public interface Tree<T extends Comparable<T>> {
    boolean insert(T data);
    boolean delete(T data);
    boolean search(T data);
    int getSize();
    int getHeight();

    Node<T> getRoot();  // Needed to access the root from the interface

    // Print tree level by level, each level on a new line
    default void prettyPrint() {
        Node<T> root = getRoot();
        if (root == null) {
            System.out.println("(empty tree)");
            return;
        }

        Queue<Node<T>> queue = new LinkedList<>();
        queue.add(root);
        int level = 0;

        while (!queue.isEmpty()) {
            int size = queue.size();
            System.out.print("Level " + level++ + ": ");
            for (int i = 0; i < size; i++) {
                Node<T> node = queue.poll();
                if (node == null) {
                    System.out.print("null ");
                    queue.add(null);
                    queue.add(null);
                    continue;
                }
                System.out.print(node.data + " ");
                queue.add(node.left);
                queue.add(node.right);
            }
            System.out.println();
            // Optimization: break if all remaining are nulls
            boolean allNull = queue.stream().allMatch(Objects::isNull);
            if (allNull) break;
        }
    }
}


