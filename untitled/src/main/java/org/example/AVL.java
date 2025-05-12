package org.example;


public class AVL<T extends Comparable<T>> implements Tree<T> {
    private Node<T> root;
    private int size;

    @Override
    public boolean insert(T data) {
        if (data == null) {
            return false;
        }

        if (root == null) {
            root = new Node<>(data);
            size++;
            return true;
        }

        root = insertRecursive(root, data);
        return true;
    }

    private Node<T> insertRecursive(Node<T> node, T data) {
        if (node == null) {
            size++;
            return new Node<>(data);
        }
        if (data.compareTo(node.data) < 0) {
            node.left = insertRecursive(node.left, data);
        } else if (data.compareTo(node.data) > 0) {
            node.right = insertRecursive(node.right, data);
        } else {
            return node;
        }

        node.height = 1 + Math.max(getHeight(node.left), getHeight(node.right));

        int balance = getBalance(node);

        // Left Left Case
        if (balance > 1 && data.compareTo(node.left.data) < 0) {
            return rotateRight(node);
        }
        // Right Right Case
        if (balance < -1 && data.compareTo(node.right.data) > 0) {
            return rotateLeft(node);
        }
        // Left Right Case
        if (balance > 1 && data.compareTo(node.left.data) > 0) {
            node.left = rotateLeft(node.left);
            return rotateRight(node);
        }
        if (balance < -1 && data.compareTo(node.right.data) < 0) {
            node.right = rotateRight(node.right);
            return rotateLeft(node);
        }
        return node;
    }

    @Override
    public boolean delete(T data) {
        if (data == null || root == null) {
            return false;
        }

        root = deleteRecursive(root, data);
        return true;
    }

    private Node<T> deleteRecursive(Node<T> node, T data) {
        if (node == null) {
            return null;
        }

        if (data.compareTo(node.data) < 0) {
            node.left = deleteRecursive(node.left, data);
        } else if (data.compareTo(node.data) > 0) {
            node.right = deleteRecursive(node.right, data);
        } else {
            // Node with only one child or no child
            if (node.left == null) {
                size--;
                return node.right;
            } else if (node.right == null) {
                size--;
                return node.left;
            }

            Node<T> successor = findMinNode(node.right);
            node.data = successor.data;
            node.right = deleteRecursive(node.right, successor.data);
        }

        if (node == null) {
            return null;
        }

        node.height = 1 + Math.max(getHeight(node.left), getHeight(node.right));
        int balance = getBalance(node);

        // Left Left Case
        if (balance > 1 && getBalance(node.left) >= 0) {
            return rotateRight(node);
        }

        // Left Right Case
        if (balance > 1 && getBalance(node.left) < 0) {
            node.left = rotateLeft(node.left);
            return rotateRight(node);
        }

        // Right Right Case
        if (balance < -1 && getBalance(node.right) <= 0) {
            return rotateLeft(node);
        }

        // Right Left Case
        if (balance < -1 && getBalance(node.right) > 0) {
            node.right = rotateRight(node.right);
            return rotateLeft(node);
        }

        return node;
    }

    // Find the node with the minimum value
    private Node<T> findMinNode(Node<T> node) {
        Node<T> current = node;
        while (current.left != null) {
            current = current.left;
        }
        return current;
    }

    @Override
    public boolean search(T data) {
        return searchRecursive(root, data);
    }

    private boolean searchRecursive(Node<T> node, T data) {
        if (node == null) return false;
        if (node.data.equals(data)) return true;

        if (data.compareTo(node.data) < 0) {
            return searchRecursive(node.left, data);
        } else {
            return searchRecursive(node.right, data);
        }
    }

    @Override
    public int getSize() {
        return size;
    }

    @Override
    public int getHeight() {
        return root == null ? 0 : root.height;
    }

    // Helper method to get height of a node
    private int getHeight(Node<T> node) {
        return node == null ? 0 : node.height;
    }

    // Calculate balance factor
    private int getBalance(Node<T> node) {
        if (node == null) return 0;
        return getHeight(node.left) - getHeight(node.right);
    }

    // Right rotation
    private Node<T> rotateRight(Node<T> y) {
        Node<T> x = y.left;
        Node<T> T2 = x.right;

        // Perform rotation
        x.right = y;
        y.left = T2;

        // Update heights
        y.height = 1 + Math.max(getHeight(y.left), getHeight(y.right));
        x.height = 1 + Math.max(getHeight(x.left), getHeight(x.right));

        // Return new root
        return x;
    }

    // Left rotation
    private Node<T> rotateLeft(Node<T> x) {
        Node<T> y = x.right;
        Node<T> T2 = y.left;

        // Perform rotation
        y.left = x;
        x.right = T2;

        // Update heights
        x.height = 1 + Math.max(getHeight(x.left), getHeight(x.right));
        y.height = 1 + Math.max(getHeight(y.left), getHeight(y.right));

        // Return new root
        return y;
    }

    @Override
    public Node<T> getRoot() {
        return root;
    }
}