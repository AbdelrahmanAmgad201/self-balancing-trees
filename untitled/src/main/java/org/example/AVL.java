package org.example;

public class AVL implements Tree{

    @Override
    public boolean insert(Object data) {
        return false;
    }

    @Override
    public boolean delete(Object data) {
        return false;
    }

    @Override
    public boolean search(Object data) {
        return false;
    }

    @Override
    public Integer getSize() {
        return this.Size;
    }

    @Override
    public Integer getHeight() {
        return 0;
    }
}
