package org.example;

import java.util.LinkedList;
import java.util.Objects;
import java.util.Queue;

public class RedBlack<T extends Comparable<T>> implements Tree<T> {
    private Node<T> root;
    private int size;

    public RedBlack() {
        this.root = null;
        this.size = 0;
    }

    private static final String ANSI_BLACK = "\u001B[30m";
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_RED = "\u001B[31m";

    @Override
    public int getSize() {
        return size;
    }

    private boolean isRed(Node<T> node) {
        return node != null && node.color;
    }

    public boolean insert(T data) {
        if (data == null) {
            return false;
        }

        // Check if data already exists
        if (search(data)) {
            return false; // Don't insert duplicates
        }

        // Standard BST insertion
        Node<T> newNode = new Node<>(data);
        newNode.color = true; // New nodes are always red
        newNode.height = 1;

        if (root == null) {
            root = newNode;
            root.color = false; // Root is always black
            size++;
            return true;
        }

        // Find the position to insert
        Node<T> current = root;
        Node<T> parent = null;

        while (current != null) {
            parent = current;
            int cmp = data.compareTo(current.data);
            if (cmp < 0) {
                current = current.left;
            } else if (cmp > 0) {
                current = current.right;
            } else {
                // Duplicate found (shouldn't happen due to check above)
                return false;
            }
        }

        // Insert the new node
        newNode.parent = parent;
        int cmp = data.compareTo(parent.data);
        if (cmp < 0) {
            parent.left = newNode;
        } else {
            parent.right = newNode;
        }

        // Fix Red-Black tree violations
        insertFixup(newNode);

        // Update heights from root
        updateAllHeights();

        size++;
        return true;
    }

    private void insertFixup(Node<T> z) {
        while (z.parent != null && isRed(z.parent)) {
            if (z.parent == z.parent.parent.left) {
                // Parent is left child of grandparent
                Node<T> uncle = z.parent.parent.right;

                if (uncle != null && isRed(uncle)) {
                    // Case 1: Uncle is red - recolor
                    z.parent.color = false;
                    uncle.color = false;
                    z.parent.parent.color = true;
                    z = z.parent.parent;
                } else {
                    // Uncle is black or null
                    if (z == z.parent.right) {
                        // Case 2: z is right child - left rotate to make it left child
                        z = z.parent;
                        leftRotate(z);
                    }
                    // Case 3: z is left child - right rotate and recolor
                    z.parent.color = false;
                    z.parent.parent.color = true;
                    rightRotate(z.parent.parent);
                }
            } else {
                // Parent is right child of grandparent (symmetric cases)
                Node<T> uncle = z.parent.parent.left;

                if (uncle != null && isRed(uncle)) {
                    // Case 1: Uncle is red - recolor
                    z.parent.color = false;
                    uncle.color = false;
                    z.parent.parent.color = true;
                    z = z.parent.parent;
                } else {
                    // Uncle is black or null
                    if (z == z.parent.left) {
                        // Case 2: z is left child - right rotate to make it right child
                        z = z.parent;
                        rightRotate(z);
                    }
                    // Case 3: z is right child - left rotate and recolor
                    z.parent.color = false;
                    z.parent.parent.color = true;
                    leftRotate(z.parent.parent);
                }
            }
        }
        root.color = false; // Root is always black
    }

    private void leftRotate(Node<T> x) {
        Node<T> y = x.right;

        // Turn y's left subtree into x's right subtree
        x.right = y.left;
        if (y.left != null) {
            y.left.parent = x;
        }

        // Link x's parent to y
        y.parent = x.parent;
        if (x.parent == null) {
            root = y;
        } else if (x == x.parent.left) {
            x.parent.left = y;
        } else {
            x.parent.right = y;
        }

        // Put x on y's left
        y.left = x;
        x.parent = y;

        // Update heights
        x.height = 1 + Math.max(getHeight(x.left), getHeight(x.right));
        y.height = 1 + Math.max(getHeight(y.left), getHeight(y.right));
    }

    private void rightRotate(Node<T> y) {
        Node<T> x = y.left;

        // Turn x's right subtree into y's left subtree
        y.left = x.right;
        if (x.right != null) {
            x.right.parent = y;
        }

        // Link y's parent to x
        x.parent = y.parent;
        if (y.parent == null) {
            root = x;
        } else if (y == y.parent.left) {
            y.parent.left = x;
        } else {
            y.parent.right = x;
        }

        // Put y on x's right
        x.right = y;
        y.parent = x;

        // Update heights
        y.height = 1 + Math.max(getHeight(y.left), getHeight(y.right));
        x.height = 1 + Math.max(getHeight(x.left), getHeight(x.right));
    }

    public boolean delete(T data) {
        Node<T> z = getNode(data);
        if (z == null) {

            return false;
        }

        Node<T> y = z;
        Node<T> x;
        Node<T> xParent = null;
        boolean yOriginalColor = y.color;

        if (z.left == null) {
            x = z.right;
            xParent = z.parent;
            transplant(z, x);
        } else if (z.right == null) {
            x = z.left;
            xParent = z.parent;
            transplant(z, x);
        } else {
            y = getMin(z.right);
            yOriginalColor = y.color;
            x = y.right;
            xParent = y.parent;

            if (y.parent == z) {
                xParent = y;
            } else {
                transplant(y, y.right);
                y.right = z.right;
                if (y.right != null) y.right.parent = y;
            }
            transplant(z, y);
            y.left = z.left;
            if (y.left != null) y.left.parent = y;
            y.color = z.color;
        }

        if (!yOriginalColor) {
            deleteFixup(x, xParent);
        }

        // Update heights from root
        updateAllHeights();

        size--;
        return true;
    }

    private void deleteFixup(Node<T> x, Node<T> xParent) {
        while (x != root && (x == null || !isRed(x))) {
            if (x == (xParent != null ? xParent.left : null)) {
                Node<T> w = xParent.right;

                // Case 1: w is red
                if (w != null && isRed(w)) {
                    w.color = false;
                    xParent.color = true;
                    leftRotate(xParent);
                    w = xParent.right;
                }

                // Case 2: w is black and both children are black
                if (w == null || ((w.left == null || !isRed(w.left)) && (w.right == null || !isRed(w.right)))) {
                    if (w != null) w.color = true;
                    x = xParent;
                    xParent = x.parent;
                } else {
                    // Case 3: w is black, w.right is black, w.left is red
                    if (w.right == null || !isRed(w.right)) {
                        if (w.left != null) w.left.color = false;
                        w.color = true;
                        rightRotate(w);
                        w = xParent.right;
                    }
                    // Case 4: w is black, w.right is red
                    w.color = xParent.color;
                    xParent.color = false;
                    if (w.right != null) w.right.color = false;
                    leftRotate(xParent);
                    x = root;
                    break;
                }
            } else {
                Node<T> w = xParent.left;

                // Case 1: w is red
                if (w != null && isRed(w)) {
                    w.color = false;
                    xParent.color = true;
                    rightRotate(xParent);
                    w = xParent.left;
                }

                // Case 2: w is black and both children are black
                if (w == null || ((w.left == null || !isRed(w.left)) && (w.right == null || !isRed(w.right)))) {
                    if (w != null) w.color = true;
                    x = xParent;
                    xParent = x.parent;
                } else {
                    // Case 3: w is black, w.left is black, w.right is red
                    if (w.left == null || !isRed(w.left)) {
                        if (w.right != null) w.right.color = false;
                        w.color = true;
                        leftRotate(w);
                        w = xParent.left;
                    }
                    // Case 4: w is black, w.left is red
                    w.color = xParent.color;
                    xParent.color = false;
                    if (w.left != null) w.left.color = false;
                    rightRotate(xParent);
                    x = root;
                    break;
                }
            }
        }
        if (x != null) x.color = false;
    }

    private void transplant(Node<T> u, Node<T> v) {
        if (u.parent == null) {
            root = v;
        } else if (u == u.parent.left) {
            u.parent.left = v;
        } else {
            u.parent.right = v;
        }
        if (v != null) {
            v.parent = u.parent;
        }
    }

    private Node<T> getMin(Node<T> node) {
        while (node.left != null) {
            node = node.left;
        }
        return node;
    }

    private void updateAllHeights() {
        if (root != null) {
            updateHeightsPostOrder(root);
        }
    }

    private void updateHeightsPostOrder(Node<T> node) {
        if (node == null) return;

        updateHeightsPostOrder(node.left);
        updateHeightsPostOrder(node.right);
        node.height = 1 + Math.max(getHeight(node.left), getHeight(node.right));
    }

    // Fixed inorder traversal - should visit left, then node, then right
    public void inorderTraversal() {
        inorderTraversal(root);
    }

    private void inorderTraversal(Node<T> node) {
        if (node == null) return;

        inorderTraversal(node.left);    // Visit left subtree first
        System.out.println(node.data + " " + (node.color ? "RED" : "BLACK"));
        inorderTraversal(node.right);   // Visit right subtree last
    }

    private int getHeight(Node<T> node) {
        if (node == null) {
            return 0;
        }
        return node.height;
    }

    public Node<T> getRoot() {
        return root;
    }

    @Override
    public boolean search(T data) {
        return searchRecursive(root, data);
    }

    private boolean searchRecursive(Node<T> node, T data) {
        if (node == null) {
            return false;
        }
        if (node.data.equals(data)) {
            return true;
        }
        if (data.compareTo(node.data) < 0) {
            return searchRecursive(node.left, data);
        } else {
            return searchRecursive(node.right, data);
        }
    }

    private Node<T> getNode(T data) {
        Node<T> current = root;
        while (current != null) {
            int cmp = data.compareTo(current.data);
            if (cmp == 0) {
                return current;
            } else if (cmp < 0) {
                current = current.left;
            } else {
                current = current.right;
            }
        }
        return null; // Node not found
    }

    public int getTreeHeight() {
        return getHeight(root);
    }

    public int getHeight() {
        if (root == null) {
            return 0;
        }
        return root.height;
    }

    public void printHierarchical() {
        printHierarchicalHelper(root, "", true);
    }

    private void printHierarchicalHelper(Node<T> node, String prefix, boolean isLast) {
        if (node != null) {
            String color = node.color ? ANSI_RED : ANSI_BLACK;
            System.out.println(prefix + (isLast ? "└── " : "├── ") + color + node.data + ANSI_RESET);

            String childPrefix = prefix + (isLast ? "    " : "│   ");

            if (node.left != null || node.right != null) {
                if (node.right != null) {
                    printHierarchicalHelper(node.right, childPrefix, node.left == null);
                } else {
                    System.out.println(childPrefix + (node.left == null ? "" : "├── ") + "null");
                }

                if (node.left != null) {
                    printHierarchicalHelper(node.left, childPrefix, true);
                } else if (node.right != null) {
                    System.out.println(childPrefix + "└── null");
                }
            }
        }
    }
}