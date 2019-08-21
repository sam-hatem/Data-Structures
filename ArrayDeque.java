public class ArrayDeque<T> {
    //creating member varibles:
    private T[] items;

    //size of the array
    private int size;
    //first and last refrences
    private int first;
    private int last;
    //REFACTOR
    private static int REFACTOR = 2;

    //constrructorr
    public ArrayDeque() {
        size = 0;
        last = 1;
        first = 0;
        //must start with 8 items
        items = (T[]) new Object[8];

    }
    /** Helper method; finding index before a given index
     */
    private int indexB4(int index) {
        return (index + items.length - 1) % items.length;

    }
    /** Helper; finding indeex after a given index */
    private int indexA4(int index) {
        return (index + 1) % items.length;

    }

    /** Helper; Resizing array to capacity */
    private void resize(int capacity) {
        T[] newArray = (T[]) new Object[capacity];
        int indexFirst = indexA4(first);
        //special case:
        if (indexFirst + size > items.length) {


            System.arraycopy(items, indexFirst, newArray, 0, items.length - indexFirst);
            System.arraycopy(items, 0, newArray, items.length - indexFirst, size - items.length
                    + indexFirst);

        } else {
            System.arraycopy(items, indexFirst, newArray, 0, size);
        }
        items = newArray;
        first = items.length - 1;
        last = size;

    }

    /** Helper: checking if usage ratio is less than 0.25 when array length >= 16
     */
    private void checkRatio() {
        if (items.length >= 16) {
            double ratio = 0.25;
            if ((double) size / items.length < ratio) {
                resize(items.length / 2);
            }
        }
    }

    /** add an item to the front of the deque*/

    public void addFirst(T item) {

        //if size goes over resize
        if (size == items.length) {
            resize(size * REFACTOR);
        }
        items[first] = item;
        first = indexB4(first);
        size += 1;


    }
    /** add an item to the end of the deque*/

    public void addLast(T item) {
        //if size goes over
        if (size == items.length) {
            resize(size * REFACTOR);
        }
        items[last] = item;
        last = indexA4(last);
        size += 1;


    }

    /** returns true if deque is empty, false otherwise*/

    public boolean isEmpty() {
        return size == 0;

    }
    /** returns the size of the deque */
    public int size() {

        return size;
    }
    /** prints the items in the deque from front to back
     */
    public void printDeque() {
        for (int i = 0; i < size; i++) {
            System.out.print(get(i) + " ");

        }


        System.out.println();
    }

    /** removes and returns the first item in the deque. if no such item exists return null */

    public T removeFirst() {
        //no item case
        if (isEmpty()) {
            return null;
        }
        int indexFirst = indexA4(first);
        T removed = items[indexFirst];
        items[indexFirst] = null;
        first  = indexFirst;

        size -= 1;
        checkRatio();
        return removed;

    }

    /** removes and returns the last item in the Deque, returns null if no such item exists */

    public T removeLast() {
        //no item case
        if (isEmpty()) {
            return null;
        }

        int indexLast = indexB4(last);
        T removed = items[indexLast];
        items[indexLast] = null;
        last = indexLast;

        size -= 1;
        checkRatio();
        return removed;


    }

    /** gets the item at the given index, where 0 is at the front.
     * return null if item doesn't exist.

     don't alter Deque */

    public T get(int index) {
        if (index >= size) {
            return null;

        }

        int actualIndex = (first + 1 + index) % items.length;
        return items[actualIndex];

    }

    public ArrayDeque(ArrayDeque other) {
        size = 0;
        last = 1;
        first = 0;
        //must start with 8 items
        items = (T[]) new Object[8];

        for (int i = 0; i < other.size(); i++) {
            addLast((T) other.get(i));
        }
    }








}
