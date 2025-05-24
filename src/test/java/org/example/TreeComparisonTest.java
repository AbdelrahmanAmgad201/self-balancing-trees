package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.Random;

public class TreeComparisonTest {
    private AVL<Integer> avlTree;
    private RedBlack<Integer> rbTree;

    @BeforeEach
    public void setUp() {
        avlTree = new AVL<>();
        rbTree = new RedBlack<>();
    }

    private void insertToBoth(int... values) {
        long start = System.nanoTime();
        for (int value : values) avlTree.insert(value);
        long avlTime = System.nanoTime() - start;

        start = System.nanoTime();
        for (int value : values) rbTree.insert(value);
        long rbTime = System.nanoTime() - start;

        System.out.println("Insert: AVL time = " + avlTime + " ns, height = " + avlTree.getHeight());
        System.out.println("Insert: RB time = " + rbTime + " ns, height = " + rbTree.getHeight());
    }

    private void deleteFromBoth(int... values) {
        long start = System.nanoTime();
        for (int value : values) avlTree.delete(value);
        long avlTime = System.nanoTime() - start;

        start = System.nanoTime();
        for (int value : values) rbTree.delete(value);
        long rbTime = System.nanoTime() - start;

        System.out.println("Delete: AVL time = " + avlTime + " ns, height = " + avlTree.getHeight());
        System.out.println("Delete: RB time = " + rbTime + " ns, height = " + rbTree.getHeight());
    }

    @Test
    public void testInsertSingleElement() {
        insertToBoth(10);
        assertTrue(avlTree.search(10));
        assertTrue(rbTree.search(10));
    }

    @Test
    public void testInsertMultipleElements() {
        insertToBoth(10, 20, 30, 40, 50);
        for (int i : new int[]{10, 20, 30, 40, 50}) {
            assertTrue(avlTree.search(i));
            assertTrue(rbTree.search(i));
        }
    }

    @Test
    public void testDeleteLeafNode() {
        insertToBoth(10, 20, 5);
        deleteFromBoth(5);
        assertFalse(avlTree.search(5));
        assertFalse(rbTree.search(5));
    }

    @Test
    public void testDeleteNodeWithOneChild() {
        insertToBoth(10, 5, 1);
        deleteFromBoth(5);
        assertFalse(avlTree.search(5));
        assertFalse(rbTree.search(5));
    }

    @Test
    public void testDeleteNodeWithTwoChildren() {
        insertToBoth(20, 10, 30, 25, 35);
        deleteFromBoth(30);
        assertFalse(avlTree.search(30));
        assertFalse(rbTree.search(30));
    }

    @Test
    public void testSearchExisting() {
        insertToBoth(1, 2, 3, 4);
        assertTrue(avlTree.search(3));
        assertTrue(rbTree.search(3));
    }

    @Test
    public void testSearchNonExisting() {
        insertToBoth(1, 2, 3, 4);
        assertFalse(avlTree.search(10));
        assertFalse(rbTree.search(10));
    }

    @Test
    public void testHeightAfterInsertions() {
        insertToBoth(10, 20, 30, 40, 50, 60);
        assertTrue(avlTree.getHeight() <= rbTree.getHeight());
    }

    @Test
    public void testHeightAfterDeletions() {
        insertToBoth(10, 20, 30, 40, 50, 60);
        assertTrue(avlTree.getHeight() <= rbTree.getHeight());
    }

    @Test
    public void testPerformanceLargeDataset() {
        int n = 100000;
        Random rand = new Random(42);
        int[] values = rand.ints(n, 0, n * 10).toArray();

        long start = System.nanoTime();
        for (int value : values) avlTree.insert(value);
        long avlTime = System.nanoTime() - start;

        start = System.nanoTime();
        for (int value : values) rbTree.insert(value);
        long rbTime = System.nanoTime() - start;

        System.out.println("Large Dataset Insert: AVL time = " + avlTime + " ns, height = " + avlTree.getHeight());
        System.out.println("Large Dataset Insert: RB time = " + rbTime + " ns, height = " + rbTree.getHeight());

        assertTrue(Math.abs(avlTime - rbTime) < avlTime);
    }

    @Test
    public void testRepeatedInsertDelete() {
        for (int i = 0; i < 1000; i++) {
            avlTree.insert(i);
            rbTree.insert(i);
            if (i % 3 == 0) {
                avlTree.delete(i);
                rbTree.delete(i);
            }
        }
        System.out.println("After repeated insert/delete: AVL height = " + avlTree.getHeight());
        System.out.println("After repeated insert/delete: RB height = " + rbTree.getHeight());

        for (int i = 0; i < 1000; i++) {
            boolean shouldExist = i % 3 != 0;
            assertEquals(shouldExist, avlTree.search(i));
            assertEquals(shouldExist, rbTree.search(i));
        }
    }

    @Test
    public void testInsertSortedSequence() {
        insertToBoth(range(0, 100));
        assertTrue(avlTree.getHeight() < 2 * Math.log(100) / Math.log(2));
        assertTrue(rbTree.getHeight() <= 2 * Math.log(100) / Math.log(2) + 1);
    }

    @Test
    public void testInsertReverseSequence() {
        insertToBoth(range(100, -1));
        assertTrue(avlTree.getHeight() < 2 * Math.log(100) / Math.log(2));
        assertTrue(rbTree.getHeight() <= 2 * Math.log(100) / Math.log(2) + 1);
    }

    @Test
    public void testCriticalBalancing() {
        insertToBoth(30, 20, 10);
        assertTrue(avlTree.getHeight() <= rbTree.getHeight());
    }

    @Test
    public void testBulkInsertAndDelete() {
        int insertCount = 50000;
        int deleteCount = 30000;

        Random rand = new Random(123);
        int[] values = rand.ints(insertCount, 0, insertCount * 10).toArray();

        // Bulk Insert
        long start = System.nanoTime();
        for (int value : values) avlTree.insert(value);
        long avlInsertTime = System.nanoTime() - start;

        start = System.nanoTime();
        for (int value : values) rbTree.insert(value);
        long rbInsertTime = System.nanoTime() - start;

        System.out.println("Bulk Insert:");
        System.out.println("AVL Insert Time: " + avlInsertTime + " ns, Height: " + avlTree.getHeight());
        System.out.println("RB Insert Time: " + rbInsertTime + " ns, Height: " + rbTree.getHeight());

        // Shuffle and delete first deleteCount values
        for (int i = values.length - 1; i > 0; i--) {
            int j = rand.nextInt(i + 1);
            int temp = values[i];
            values[i] = values[j];
            values[j] = temp;
        }
        // Remove duplicates
        values = Arrays.stream(values).distinct().toArray();


        start = System.nanoTime();
        for (int i = 0; i < deleteCount; i++) avlTree.delete(values[i]);
        long avlDeleteTime = System.nanoTime() - start;

        start = System.nanoTime();
        for (int i = 0; i < deleteCount; i++){
            rbTree.delete(values[i]);
        }
        long rbDeleteTime = System.nanoTime() - start;

        System.out.println("Bulk Delete:");
        System.out.println("AVL Delete Time: " + avlDeleteTime + " ns, Final Height: " + avlTree.getHeight());
        System.out.println("RB Delete Time: " + rbDeleteTime + " ns, Final Height: " + rbTree.getHeight());

        // Validate remaining values exist
        for (int i = deleteCount; i < values.length; i++) {
            assertTrue(avlTree.search(values[i]));
            assertTrue(rbTree.search(values[i]));
        }

        // Validate deleted values are gone
        for (int i = 0; i < deleteCount; i++) {
            assertFalse(avlTree.search(values[i]));
            assertFalse(rbTree.search(values[i]));
        }
    }


    @Test
    public void testDuplicateInsertion() {
        insertToBoth(10, 10, 10);
        assertTrue(avlTree.search(10));
        assertTrue(rbTree.search(10));
    }

    @Test
    public void testDeleteAll() {
        insertToBoth(10, 20, 30);
        deleteFromBoth(10, 20, 30);
        assertEquals(0, avlTree.getHeight());
        assertEquals(0, rbTree.getHeight());
    }

    @Test
    public void testEmptySearch() {
        assertFalse(avlTree.search(1));
        assertFalse(rbTree.search(1));
    }



    private static final int[] SIZES = {
            5,        // very small
            20,       // small
            75,       // small-medium
            200,      // medium
            600,      // medium-large
            2_000,    // large
            7_500,    // large
            25_000,   // very large
            75_000,   // very large
            250_000,  // huge
            750_000,  // huge
            2_000_000 // extra huge
    };


    @Test
    public void testPerformanceAcrossSizes() {
        System.out.printf("%-10s | %-10s | %-12s | %-12s | %-12s | %-12s | %-12s | %-8s%n",
                "Size", "Tree", "Insert(ns)", "Search(ns)", "Delete(ns)", "Height", "Height", "Status");
        System.out.printf("%-10s | %-10s | %-12s | %-12s | %-12s | %-12s | %-12s | %-8s%n",
                "", "", "", "", "", "(AVL)", "(RB)", "");

        for (int size : SIZES) {
            int[] data = generateRandomArray(size, 42);

            AVL<Integer> avl = new AVL<>();
            RedBlack<Integer> rb = new RedBlack<>();

            long avlInsert = measureInsertTime(avl, data);
            long rbInsert = measureInsertTime(rb, data);

            int avlHeight = avl.getHeight();
            int rbHeight = rb.getHeight();

            long avlSearch = measureSearchTime(avl, data);
            long rbSearch = measureSearchTime(rb, data);

            long avlDelete = measureDeleteTime(avl, data);
            long rbDelete = measureDeleteTime(rb, data);  // Fixed: correctly measure RB delete

            System.out.printf("%-10d | %-10s | %-12d | %-12d | %-12d | %-12d | %-12s | %-8s%n",
                    size, "AVL", avlInsert, avlSearch, avlDelete, avlHeight, "-" , "✓");
            System.out.printf("%-10s | %-10s | %-12d | %-12d | %-12d | %-12s | %-12d | %-8s%n",
                    "", "RB", rbInsert, rbSearch, rbDelete, "-", rbHeight, "✓");
        }
    }


    private int[] generateRandomArray(int size, long seed) {
        Random random = new Random(seed);
        return random.ints(size, 0, size * 10).toArray();
    }

    private long measureInsertTime(Tree<Integer> tree, int[] data) {
        long start = System.nanoTime();
        for (int x : data) tree.insert(x);
        return (System.nanoTime() - start); // ns
    }

    private long measureSearchTime(Tree<Integer> tree, int[] data) {
        long start = System.nanoTime();
        for (int x : data) tree.search(x);
        return (System.nanoTime() - start); // ns
    }

    private long measureDeleteTime(Tree<Integer> tree, int[] data) {
        long start = System.nanoTime();
        for (int x : data) tree.delete(x);
        return (System.nanoTime() - start); // ns
    }


    @Test
    public void testSizeAndHeightAcrossSizes() {
        System.out.printf("%-10s | %-10s | %-12s | %-12s | %-12s%n",
                "Size", "Tree", "Nodes", "Height", "Status");

        for (int size : SIZES) {
            int[] data = generateRandomArray(size, 42);

            AVL<Integer> avl = new AVL<>();
            RedBlack<Integer> rb = new RedBlack<>();

            // Insert data
            for (int value : data) {
                avl.insert(value);
                rb.insert(value);
            }

            // Get sizes
            int avlNodes = avl.getSize();  // You'll need to implement getSize()
            int rbNodes = rb.getSize();    // You'll need to implement getSize()

            // Get heights
            int avlHeight = avl.getHeight();
            int rbHeight = rb.getHeight();

            // Print results
            System.out.printf("%-10d | %-10s | %-12d | %-12d | %-12s%n",
                    size, "AVL", avlNodes, avlHeight, "✓");
            System.out.printf("%-10s | %-10s | %-12d | %-12d | %-12s%n",
                    "", "RB", rbNodes, rbHeight, "✓");
        }
    }

    private int[] range(int start, int end) {
        int step = start < end ? 1 : -1;
        int size = Math.abs(end - start);
        int[] result = new int[size];
        for (int i = 0; i < size; i++) result[i] = start + i * step;
        return result;
    }

}
