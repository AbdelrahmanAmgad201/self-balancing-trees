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
    private void flipColors(Node<T> h){
        h.color = true;
        if (h.left != null) h.left.color = false;
        if (h.right != null) h.right.color = false;
    }
    private Node<T> rotateLeft(Node<T> h){
      Node<T> X = h.right;
      Node<T> Y = X.left;
      X.left = h;
      h.right = Y;
      X.color = h.color;
      h.color = true;
      X.height = h.height;
      h.height = 1 + Math.max(getHeight(h.left), getHeight(h.right));
      X.height = 1 + Math.max(getHeight(X.left), getHeight(X.right));
      return X;
    }
    private Node<T> rotateRight(Node<T> h){
      Node<T> X = h.left;
      Node<T> Y = X.right;
      X.right = h;
      h.left = Y;
      X.color = h.color;
      h.color = true;
      X.height = h.height;
      h.height = 1 + Math.max(getHeight(h.left), getHeight(h.right));
      X.height = 1 + Math.max(getHeight(X.left), getHeight(X.right));
      return X;
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
    public boolean delete(T data) {
        if (data == null || search(data) == false) {
            return false; 
        }
        root = deleteRecursive(root, data);
        if (root != null) {
            root.color = false; 
            size--;
        }
        
        
        return true; 
    }
    
    private Node<T> deleteRecursive(Node<T> node, T data) {
        if (node == null) {
            return null; 
        }
        if (data.compareTo(node.data) < 0) {
            if (!isRed(node.left) && !isRed(node.left.left)) {
                node = moveRedLeft(node); 
            }
            node.left = deleteRecursive(node.left, data); 
        } else {
            if (isRed(node.left)) {
                node = rotateRight(node);
            }
            if (data.equals(node.data) && (node.right == null)) {
                return null; 
            }
            if (!isRed(node.right)) {
                node = moveRedRight(node); 
            }
            if (data.equals(node.data)) {
                Node<T> minNode = getMin(node.right); 
                node.data = minNode.data; 
                node.right = deleteMin(node.right); 
            }
            node.right = deleteRecursive(node.right, data); 
        }
    
        return balance(node); 
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
