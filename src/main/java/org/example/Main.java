package org.example;

public class Main {
    public static void main(String[] args) {
        RedBlack<Integer> tree = new RedBlack<>();  // Use the interface type

        for (int i = 0; i < 200; i++) {
            tree.insert(i);
        }
        tree.printHierarchical();;
        System.out.println("Tree size: " + tree.getSize());
        System.out.println("Tree height: " + tree.getHeight());


        for(int i = 0; i < 200; i+=2) {
            tree.delete(i);
        }
        tree.printHierarchical();;
        System.out.println("Tree size: " + tree.getSize());
        System.out.println("Tree height: " + tree.getHeight());

    }
}
