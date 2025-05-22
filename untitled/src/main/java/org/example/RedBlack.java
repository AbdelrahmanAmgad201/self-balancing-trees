package org.example;

public class RedBlack<T extends Comparable<T>> implements Tree<T> {
    private Node<T> root;
    private int size;
    public RedBlack() {
        this.root = null;
        this.size = 0;
    }
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

        if (root == null) {
            root = new Node<>(data);
            root.color = false; // root is always black
            size++;
            return true;
        }

        root = insertRecursive(root, data);
        root.color = false; //root is always black
        return true;
    }
    private Node<T> insertRecursive(Node<T> node, T data) {
        if (node == null) {
            size++;
            return new Node<>(data, true); 
        }
        if (data.compareTo(node.data) < 0) {
            node.left = insertRecursive(node.left, data);
        } else if (data.compareTo(node.data) > 0) {
            node.right = insertRecursive(node.right, data);
        } else {
            return node;
        }
        if (isRed(node.right) && !isRed(node.left)) {
            node = rotateLeft(node);
        }
        if (isRed(node.left) && isRed(node.left.left)) {
            node = rotateRight(node);
        }
        if (isRed(node.left) && isRed(node.right)) {
            flipColors(node);
        }

        return node;
    }
    //make inorder traversal that prints the tree in order with the color of the node
    public void inorderTraversal() {
        inorderTraversal(root);
    }
    private void inorderTraversal(Node<T> node) {
        if (node == null) return;
        System.out.println(node.data + " " + (node.color ? "RED" : "BLACK"));
        inorderTraversal(node.left);

        inorderTraversal(node.right);
    }
    // Standard Red-Black tree insert fixup
    private void insertFixup(Node<T> node) {
        while (node != null && node != root && isRed(node.parent)) {
            if (node.parent.parent == null) {
                // Parent is the root, no further fixup needed
                break;
            }
            if (node.parent == node.parent.parent.left) {
                // Parent is left child of grandparent
                Node<T> uncle = node.parent.parent.right;

                if (isRed(uncle)) {
                    // Case 1: Uncle is RED
                    node.parent.color = false;
                    uncle.color = false;
                    node.parent.parent.color = true;
                    node = node.parent.parent;
                } else {
                    // Case 2: Uncle is BLACK
                    if (node == node.parent.right) {
                        // Case 2a: Node is right child
                        node = node.parent;
                        rotateLeft(node);
                    }
                    // Case 2b: Node is left child
                    node.parent.color = false;
                    node.parent.parent.color = true;
                    rotateRight(node.parent.parent);
                }
            } else {
                // Parent is right child of grandparent (mirror cases)
                Node<T> uncle = node.parent.parent.left;

                if (isRed(uncle)) {
                    // Case 1: Uncle is RED
                    node.parent.color = false;
                    uncle.color = false;
                    node.parent.parent.color = true;
                    node = node.parent.parent;
                } else {
                    // Case 2: Uncle is BLACK
                    if (node == node.parent.left) {
                        // Case 2a: Node is left child
                        node = node.parent;
                        rotateRight(node);
                    }
                    // Case 2b: Node is right child
                    node.parent.color = false;
                    node.parent.parent.color = true;
                    rotateLeft(node.parent.parent);
                }
            }
        }
        root.color = false; // Root is always BLACK
    }

    private void flipColors(Node<T> h){
        h.color = true;
        if (h.left != null) h.left.color = false;
        if (h.right != null) h.right.color = false;
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
        if ( node.data.equals(data)) {
            return true;
        }
        if (data.compareTo(node.data) < 0) {
            return searchRecursive(node.left, data);
        } else {
            return searchRecursive(node.right, data);
        }
    }
    // Add the missing getNode method:
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

// Fixed rotation methods that return the new root and update parent pointers correctly:
private Node<T> rotateLeft(Node<T> h) {
    Node<T> x = h.right;
    Node<T> y = x.left;
    
    // Perform rotation
    x.left = h;
    h.right = y;
    
    // Update parent pointers
    x.parent = h.parent;
    h.parent = x;
    if (y != null) {
        y.parent = h;
    }
    
    // Update parent's child pointer
    if (x.parent != null) {
        if (x.parent.left == h) {
            x.parent.left = x;
        } else {
            x.parent.right = x;
        }
    } else {
        root = x; // x is the new root
    }
    
    // Update colors
    x.color = h.color;
    h.color = true;
    
    // Update heights
    h.height = 1 + Math.max(getHeight(h.left), getHeight(h.right));
    x.height = 1 + Math.max(getHeight(x.left), getHeight(x.right));
    
    return x;
}

private Node<T> rotateRight(Node<T> h) {
    Node<T> x = h.left;
    Node<T> y = x.right;
    
    // Perform rotation
    x.right = h;
    h.left = y;
    
    // Update parent pointers
    x.parent = h.parent;
    h.parent = x;
    if (y != null) {
        y.parent = h;
    }
    
    // Update parent's child pointer
    if (x.parent != null) {
        if (x.parent.left == h) {
            x.parent.left = x;
        } else {
            x.parent.right = x;
        }
    } else {
        root = x; // x is the new root
    }
    
    // Update colors
    x.color = h.color;
    h.color = true;
    
    // Update heights
    h.height = 1 + Math.max(getHeight(h.left), getHeight(h.right));
    x.height = 1 + Math.max(getHeight(x.left), getHeight(x.right));
    
    return x;
}

// Fixed transplant method (your version is correct):
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

// Fixed deleteFixup method with proper rotation calls:
private void deleteFixup(Node<T> x) {
    while (x != root && (x == null || !isRed(x))) {
        if (x != null && x == x.parent.left) {
            Node<T> w = x.parent.right;
            if (isRed(w)) {
                w.color = false;
                x.parent.color = true;
                rotateLeft(x.parent);
                w = x.parent.right;
            }
            if (!isRed(w.left) && !isRed(w.right)) {
                w.color = true;
                x = x.parent;
            } else {
                if (!isRed(w.right)) {
                    if (w.left != null) w.left.color = false;
                    w.color = true;
                    rotateRight(w);
                    w = x.parent.right;
                }
                w.color = x.parent.color;
                x.parent.color = false;
                if (w.right != null) w.right.color = false;
                rotateLeft(x.parent);
                x = root;
            }
        } else if (x != null) {
            Node<T> w = x.parent.left;
            if (isRed(w)) {
                w.color = false;
                x.parent.color = true;
                rotateRight(x.parent);
                w = x.parent.left;
            }
            if (!isRed(w.left) && !isRed(w.right)) {
                w.color = true;
                x = x.parent;
            } else {
                if (!isRed(w.left)) {
                    if (w.right != null) w.right.color = false;
                    w.color = true;
                    rotateLeft(w);
                    w = x.parent.left;
                }
                w.color = x.parent.color;
                x.parent.color = false;
                if (w.left != null) w.left.color = false;
                rotateRight(x.parent);
                x = root;
            }
        } else {
            break; // x is null, exit loop
        }
    }
    if (x != null) x.color = false;
}    public boolean delete(T data) {
        Node<T> z = getNode(data);
        if (z == null) {
            return false;
        }
        Node<T> y = z;
        Node<T> x;
        boolean yOriginalColor = y.color;
        if (z.left == null) {
            x = z.right;
            transplant(z, x);
        }
        else if (z.right == null) {
            x = z.left;
            transplant(z, z.left);
        }
        else {
            y = getMin(z.right);
            yOriginalColor = y.color;
            x = y.right;
            if (y.parent == z) {
                if (x != null) x.parent = y;
            }
            else {
                transplant(y, y.right);
                y.right = z.right;
                y.right.parent = y;
            }
            transplant(z, y);
            y.left = z.left;
            y.left.parent = y;
            y.color = z.color;
        }
        if (!yOriginalColor) deleteFixup(x);
        size--;
        return true;
    }

                            
    private Node<T> balance(Node<T> node) {
        if (isRed(node.right)) {
            node = rotateLeft(node);
        }
        if (isRed(node.left) && isRed(node.left.left)) {
            node = rotateRight(node);
        }
        if (isRed(node.left) && isRed(node.right)) {
            flipColors(node);
        }
        node.height = 1 + Math.max(getHeight(node.left), getHeight(node.right));
        return node;
    }
    private Node<T> getMin(Node<T> node) {
        while (node.left != null) {
            node = node.left;
        }
        return node;
    }
    private Node<T> deleteMin(Node<T> node) {
        if (node.left == null) {
            return null;
        }
        if (!isRed(node.left) && !isRed(node.left.left)) {
            node = moveRedLeft(node);
        }
        node.left = deleteMin(node.left);
        return balance(node);
    }
    private Node<T> moveRedLeft(Node<T> node) {
        flipColors(node);
        if (isRed(node.right)) {
            node = rotateLeft(node);
            flipColors(node);
        }
        return node;
    }
    private Node<T> moveRedRight(Node<T> node) {
        flipColors(node);
        if (isRed(node.left)) {
            node = rotateRight(node);
            flipColors(node);
        }
        return node;
    }
    public int getTreeHeight() {
        return getHeight(root);
    }
    public int getHeight(){
        if (root == null) {
            return 0;
        }
        return root.height;
    }
}
