package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class RedBlackTest {

    private RedBlack<Integer> tree;

    @BeforeEach
    void setUp() {
        tree = new RedBlack<>();
    }

    @Test
    void testInsertIntoEmptyTree() {
        assertTrue(tree.insert(10));
        assertEquals(1, tree.getSize());

        Node<Integer> root = tree.getRoot();
        assertEquals(10, root.data);
        assertFalse(root.color); // Root should be black
    }

    @Test
    void testMultipleInsertions() {
        tree.insert(10);
        tree.insert(5);
        tree.insert(15);
        tree.insert(3);
        tree.insert(7);

        assertEquals(5, tree.getSize());
        assertTrue(tree.search(10));
        assertTrue(tree.search(5));
        assertTrue(tree.search(15));
        assertTrue(tree.search(3));
        assertTrue(tree.search(7));
        assertFalse(tree.search(100));
    }

    @Test
    void testInsertDuplicate() {
        tree.insert(10);
        tree.insert(10); // Duplicate

        assertEquals(1, tree.getSize());
    }

    @Test
    void testLeftRotation() {
        // Create a scenario that should trigger a left rotation
        tree.insert(10);
        tree.insert(15);
        tree.insert(20);

        Node<Integer> root = tree.getRoot();
        assertEquals(15, root.data); // 15 should be the new root after rotation
        assertEquals(10, root.left.data);
        assertEquals(20, root.right.data);
    }

    @Test
    void testRightRotation() {
        // Create a scenario that should trigger a right rotation
        tree.insert(20);
        tree.insert(15);
        tree.insert(10);

        Node<Integer> root = tree.getRoot();
        assertEquals(15, root.data); // 15 should be the new root after rotation
        assertEquals(10, root.left.data);
        assertEquals(20, root.right.data);
    }

    @Test
    void testDeleteLeafNode() {
        tree.insert(10);
        tree.insert(5);
        tree.insert(15);

        assertTrue(tree.delete(5));
        assertEquals(2, tree.getSize());
        assertFalse(tree.search(5));
    }

    @Test
    void testDeleteNodeWithOneChild() {
        tree.insert(10);
        tree.insert(5);
        tree.insert(15);
        tree.insert(3);

        assertTrue(tree.delete(5));
        assertEquals(3, tree.getSize());
        assertFalse(tree.search(5));
        assertTrue(tree.search(3));
    }

    @Test
    void testDeleteNodeWithTwoChildren() {
        tree.insert(10);
        tree.insert(5);
        tree.insert(15);
        tree.insert(3);
        tree.insert(7);

        assertTrue(tree.delete(5));
        assertEquals(4, tree.getSize());
        assertFalse(tree.search(5));
        assertTrue(tree.search(3));
        assertTrue(tree.search(7));
    }

    @Test
    void testDeleteRootNode() {
        tree.insert(10);
        tree.insert(5);
        tree.insert(15);

        assertTrue(tree.delete(10));
        assertEquals(2, tree.getSize());
        assertFalse(tree.search(10));
    }

    @Test
    void testDeleteNonExistentNode() {
        tree.insert(10);
        tree.insert(5);
        tree.insert(15);

        assertFalse(tree.delete(100));
        assertEquals(3, tree.getSize());
    }

    @Test
    void testInsertAndDeleteManyNodes() {
        tree=new RedBlack<Integer>();

        for (int i = 0; i < 10; i++) {
            tree.insert(i);
        }


        assertEquals(10, tree.getSize());
        assertFalse(tree.getRoot().color);
        // Delete half the nodes
        for (int i = 0; i < 5; i++) {
            tree.delete(i);
        }
        System.out.println("After deletion:");

        assertEquals(5, tree.getSize());


        // Verify remaining nodes
        for (int i = 0; i < 5; i++) {
            assertFalse(tree.search(i));
        }

        for (int i = 5; i < 10; i++) {
            assertTrue(tree.search(i));
        }
    }

}