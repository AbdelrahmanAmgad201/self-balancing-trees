package org.example;

public interface Tree {
    Node root = null;
    Integer Size = 0;
    boolean insert(Object data);
    boolean delete(Object data);
    boolean search(Object data);
    Integer getSize();
    Integer getHeight();
    static void traverse(){
        return;
    }
}
