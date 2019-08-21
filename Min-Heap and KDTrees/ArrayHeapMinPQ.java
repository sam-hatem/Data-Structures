package bearmaps;

import org.junit.Test;

import java.util.HashMap;
import java.util.NoSuchElementException;

import static org.junit.Assert.assertEquals;


public class ArrayHeapMinPQ<T> implements ExtrinsicMinPQ<T> {
    Node[] contents;
    int size;
    HashMap<T, Integer> map;

    public ArrayHeapMinPQ() {


        contents = new ArrayHeapMinPQ.Node[16];
        /* Add a dummy at front of heap to make math nicer */

        contents[0] = null;

        /* nothing's inserted so size is zero */
        size = 0;

        map = new HashMap<>();


    }


    /**
     * returns index of node at the left of i
     */
    private static int leftIndex(int i) {
        return 2 * i;
    }

    /**
     * returns index of node at the right of i
     */

    private static int rightIndex(int i) {
        return 2 * i + 1;
    }


    /**
     * return index of parent of node at i
     */

    private static int parentIndex(int i) {
        return i / 2;

    }


    /**
     * return node at ith index or null if index is out of bounds
     */

    private Node getNode(int index) {
        if (!valid(index)) {
            return null;
        }
        return contents[index];
    }

    /**
     * returns true if item is in bounds
     */

    private boolean valid(int index) {
        return (index <= size) && (index >= 1);
    }


    /**
     * swap nodes at two indicies
     */

    private void swap(int index1, int index2) {
        Node node1 = getNode(index1);
        Node node2 = getNode(index2);

        contents[index1] = node2;
        contents[index2] = node1;

    }

    /**
     * return index with lower priority
     */

    private int min(int index1, int index2) {


        Node node1 = getNode(index1);
        Node node2 = getNode(index2);

        if (node1 == null) {
            return index2;

        } else if (node2 == null) {
            return index1;
        } else if (node1.myPriority < node2.myPriority) {
            return index1;
        } else {
            return index2;
        }

    }

    /**
     * move node up at a given index
     */

    private void swim(int index) {

        while (index > 1 && min(index, parentIndex(index)) == index && valid(index)) {
            T key = getNode(index).item();
            swap(index, parentIndex(index));
            index = parentIndex(index);
            map.put(key, index);

        }

    }


    /**
     * move down node at given index
     */
    private void sink(int index) {


        while ((min(index, leftIndex(index)) != index || min(index, rightIndex(index)) != index)
                && valid(index)) {
            T key = getNode(index).item();
            if (min(leftIndex(index), rightIndex(index)) == leftIndex(index)) {
                swap(index, leftIndex(index));
                index = leftIndex(index);
                map.put(key, index);
            } else {
                swap(index, rightIndex(index));
                index = rightIndex(index);
                map.put(key, index);
            }
        }

    }

    /* Adds an item with the given priority value. Throws an
     * IllegalArgumentExceptionb if item is already present. */
    @Override
    public void add(T item, double priority) {
        if (size + 1 == contents.length) {
            resize(contents.length * 2);
        }

        if (contains(item)) {
            throw new IllegalArgumentException("item is already present");

        } else {
            size++;
            contents[size] = new Node(item, priority);
            swim(size);
            map.put(item, size);


        }

    }

    /* Returns true if the PQ contains the given item. */
    @Override
    public boolean contains(T item) {
        return map.containsKey(item);


    }


    /* Returns the minimum item. Throws NoSuchElementException if the PQ is empty. *

     */

    @Override
    public T getSmallest() {
        if (size == 0) {
            throw new NoSuchElementException("PQ IS EMPTY NO ELEMENT");
        }
        return getNode(1).item();
    }


    /* Removes and returns the minimum item. Throws NoSuchElementException if the PQ is empty. */
    @Override
    public T removeSmallest() {
        if (size == 0) {
            throw new NoSuchElementException("PQ IS EMPTY NO ELEMENT");
        }

        T smallest = getSmallest();
        contents[1] = getNode(size);
        sink(1);
        size--;
        map.remove(smallest);
        return smallest;

    }


    /* Returns the number of items in the PQ. */
    @Override
    public int size() {
        return size;
    }

    /* Changes the priority of the given item. Throws NoSuchElementException if the item
     * doesn't exist. */

    @Override
    public void changePriority(T item, double priority) {

        if (contains(item)) {
            Integer index = map.get(item);
            if (valid(index)) {
                double oldPriority = getNode(index).myPriority;
                getNode(index).myPriority = priority;
                if (priority > oldPriority) {
                    sink(index);
                } else {
                    swim(index);
                }
            }


        }
    }


    /**
     * resize helper
     */

    private void resize(int capacity) {
        Node[] temp = new ArrayHeapMinPQ.Node[capacity];
        for (int i = 0; i < contents.length; i++) {

            temp[i] = contents[i];
        }
        contents = temp;
    }

    @Test
    public void testIndexing() {
        assertEquals(6, leftIndex(3));
        assertEquals(10, leftIndex(5));
        assertEquals(7, rightIndex(3));
        assertEquals(11, rightIndex(5));

        assertEquals(3, parentIndex(6));
        assertEquals(5, parentIndex(10));
        assertEquals(3, parentIndex(7));
        assertEquals(5, parentIndex(11));
    }


    //Testing private methods

    @Test
    public void testSwim() {
        ArrayHeapMinPQ<String> pq = new ArrayHeapMinPQ<>();
        pq.size = 7;
        for (int i = 1; i <= 7; i += 1) {
            pq.contents[i] = new ArrayHeapMinPQ<String>.Node("x" + i, i);
        }
        // Change item x6's priority to a low value.

        pq.contents[6].myPriority = 0;
        System.out.println("PQ before swimming:");
        System.out.println(pq);

        // Swim x6 upwards. It should reach the root.

        pq.swim(6);
        System.out.println("PQ after swimming:");
        System.out.println(pq);
        assertEquals("x6", pq.contents[1].myItem);
        assertEquals("x2", pq.contents[2].myItem);
        assertEquals("x1", pq.contents[3].myItem);
        assertEquals("x4", pq.contents[4].myItem);
        assertEquals("x5", pq.contents[5].myItem);
        assertEquals("x3", pq.contents[6].myItem);
        assertEquals("x7", pq.contents[7].myItem);

    }

    @Test
    public void testSink() {
        ArrayHeapMinPQ<String> pq = new ArrayHeapMinPQ<>();
        pq.size = 7;
        for (int i = 1; i <= 7; i += 1) {
            pq.contents[i] = new ArrayHeapMinPQ<String>.Node("x" + i, i);
        }
        // Change root's priority to a large value.
        pq.contents[1].myPriority = 10;
        System.out.println("PQ before sinking:");
        System.out.println(pq);

        // Sink the root.
        pq.sink(1);
        System.out.println("PQ after sinking:");
        System.out.println(pq);
        assertEquals("x2", pq.contents[1].myItem);
        assertEquals("x4", pq.contents[2].myItem);
        assertEquals("x3", pq.contents[3].myItem);
        assertEquals("x1", pq.contents[4].myItem);
        assertEquals("x5", pq.contents[5].myItem);
        assertEquals("x6", pq.contents[6].myItem);
        assertEquals("x7", pq.contents[7].myItem);
    }

    class Node {
        T myItem;
        double myPriority;

        private Node(T item, double priority) {
            myItem = item;
            myPriority = priority;
        }

        public T item() {
            return myItem;
        }

        public double priority() {
            return myPriority;
        }
    }


}
