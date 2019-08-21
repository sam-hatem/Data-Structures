package bearmaps;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ArrayHeapMinPQTest {
    @Test
    public void testAdd() {
        ArrayHeapMinPQ<String> pq = new ArrayHeapMinPQ<>();
        pq.add("c", 3);
        assertEquals("c", pq.contents[1].myItem);

        pq.add("i", 9);
        assertEquals("i", pq.contents[2].myItem);

        pq.add("g", 7);
        pq.add("d", 4);
        assertEquals("d", pq.contents[2].myItem);

        pq.add("a", 1);
        assertEquals("a", pq.contents[1].myItem);

        pq.add("h", 8);
        pq.add("e", 5);
        pq.add("b", 2);
        //pq.add("c", 3);
        //pq.add("d", 4);
        System.out.println("pq after inserting 10 items: ");
        System.out.println(pq);
        //assertEquals(10, pq.size());
        //assertEquals("a", pq.contents[1].myItem);
        //assertEquals("b", pq.contents[2].myItem);
        //assertEquals("e", pq.contents[3].myItem);
        //assertEquals("c", pq.contents[4].myItem);
        //assertEquals("d", pq.contents[5].myItem);
        //assertEquals("h", pq.contents[6].myItem);
        //assertEquals("g", pq.contents[7].myItem);
        //assertEquals("i", pq.contents[8].myItem);
        //assertEquals("c", pq.contents[9].myItem);
        //assertEquals("d", pq.contents[10].myItem);
    }

    @Test
    public void testAddAndRemoveOnce() {
        ArrayHeapMinPQ<String> pq = new ArrayHeapMinPQ<>();
        pq.add("c", 3);
        pq.add("i", 9);
        pq.add("g", 7);
        pq.add("d", 4);
        pq.add("a", 1);
        pq.add("h", 8);
        pq.add("e", 5);
        pq.add("b", 2);
        pq.add("c", 3);
        pq.add("d", 4);
        System.out.println(pq);
        String removed = pq.removeSmallest();
        System.out.println(pq);
        assertEquals("a", removed);
        assertEquals(9, pq.size());
        assertEquals("b", pq.contents[1].myItem);
        assertEquals("c", pq.contents[2].myItem);
        assertEquals("e", pq.contents[3].myItem);
        assertEquals("c", pq.contents[4].myItem);
        assertEquals("d", pq.contents[5].myItem);
        assertEquals("h", pq.contents[6].myItem);
        assertEquals("g", pq.contents[7].myItem);
        assertEquals("i", pq.contents[8].myItem);
        assertEquals("d", pq.contents[9].myItem);
    }

    @Test
    public void testInsertAndRemoveAllButLast() {
        ExtrinsicMinPQ<String> pq = new ArrayHeapMinPQ<>();
        pq.add("c", 3);
        pq.add("i", 9);
        pq.add("g", 7);
        pq.add("d", 4);
        pq.add("a", 1);
        pq.add("h", 8);
        pq.add("e", 5);
        pq.add("b", 2);
        pq.add("c", 3);
        pq.add("d", 4);

        int i = 0;
        String[] expected = {"a", "b", "c", "c", "d", "d", "e", "g", "h", "i"};
        while (pq.size() > 1) {
            assertEquals(expected[i], pq.removeSmallest());
            i += 1;
        }
    }

    @Test
    public void testChangePriority() {
        ExtrinsicMinPQ<String> pq = new ArrayHeapMinPQ<>();

        pq.add("c", 3);
        pq.add("i", 9);
        pq.add("g", 7);
        pq.add("d", 4);
        pq.add("a", 1);
        pq.add("h", 8);
        pq.add("e", 5);
        pq.add("b", 2);


        pq.changePriority("c", 1);
        System.out.print(pq);


    }

    @Test
    public void testContains() {
        ExtrinsicMinPQ<String> pq = new ArrayHeapMinPQ<>();
        pq.add("c", 3);
        pq.add("i", 9);
        pq.add("g", 7);
        pq.add("d", 4);
        pq.add("a", 1);
        pq.add("h", 8);
        pq.add("e", 5);
        pq.add("b", 2);

        System.out.println(pq.contains("f"));

    }


}
