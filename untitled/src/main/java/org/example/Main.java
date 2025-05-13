package org.example;

public class Main {
    public static void main(String[] args) {
        Tree<Integer> tree = new RedBlack<>();  // Use the interface type

        // Insert elements
        tree.insert(10);
        tree.insert(20);
        tree.insert(30);
        tree.insert(15);
        tree.insert(5);
        tree.insert(1);
        tree.insert(25);

        // Pretty print the tree
        System.out.println("Tree structure after insertions:");
        tree.prettyPrint();

        // Print size and height
        System.out.println("\nSize of the tree: " + tree.getSize());
        System.out.println("Height of the tree: " + tree.getHeight());

        // Search for some values
        System.out.println("\nSearch for 15: " + tree.search(15));
        System.out.println("Search for 100: " + tree.search(100));

        // Delete some values
        System.out.println("\nDeleting 15...");
        tree.delete(15);
        tree.prettyPrint();
        System.out.println("\nsize of the tree: " + tree.getSize());
        System.out.println("Deleting 1...");
        tree.delete(1);
        tree.prettyPrint();
        System.out.println("\nsize of the tree: " + tree.getSize());
        System.out.println("Deleting 30...");
        tree.delete(30);
        tree.prettyPrint();
        System.out.println("\nsize of the tree: " + tree.getSize());
        System.out.println("Deleting 25...");
        tree.delete(25);
        tree.prettyPrint();
        System.out.println("\nsize of the tree: " + tree.getSize());
        System.out.println("Deleting 5...");
        tree.delete(5);
        tree.prettyPrint();
        System.out.println("\nsize of the tree: " + tree.getSize());
        // Attempt to delete a non-existent value
        System.out.println("Deleting 100 (non-existent)...");
        tree.delete(100);
        tree.prettyPrint();
        System.out.println("\nsize of the tree: " + tree.getSize());
        System.out.println("Deleting 10...");
        tree.delete(10);
        tree.prettyPrint();
        System.out.println("\nsize of the tree: " + tree.getSize());

        System.out.println("\nTree structure after deletions:");
        tree.prettyPrint();

        // Final size and height
        System.out.println("\nFinal size of the tree: " + tree.getSize());
        System.out.println("Final height of the tree: " + tree.getHeight());
    }
}
