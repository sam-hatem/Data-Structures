public class LinkedListDeque<T> {

    //building a linked list based class


    private class Node {
        //class where members are declared
        private Node prev;
        private T item;
        private Node next;
        //make a constructor so that it's easier to call stuff
        Node(Node p, T i, Node n) {

            prev = p;
            item = i;
            next = n;


        }
    }

    //member variables:
    private int size;
    private Node sentinel;

    //done with private class
    //now on to constructor

    public LinkedListDeque() {

        //constructor; what should a deque object have?
        sentinel = new Node(null, null, null);
        sentinel.prev = sentinel;
        sentinel.next = sentinel;

        size = 0;


    }

    //time to make methods!!

    /**
     * this method Adds an item to the front of the Deque
     *
     * @param T item
     *            adds the item to front of Deque
     */
    public void addFirst(T item) {
        sentinel.next.prev = new Node(sentinel, item, sentinel.next);
        sentinel.next = sentinel.next.prev;

        size += 1;


    }

    /**
     * this method adds an item to the back of the list
     *
     * @param is an T item to be added to the end of the Deque
     */
    public void addLast(T item) {
        sentinel.prev.next = new Node(sentinel.prev, item, sentinel);
        sentinel.prev = sentinel.prev.next;

        size += 1;
    }

    /**
     * this method Returns true if deque is empty, false otherwise. no parameters
     */

    public boolean isEmpty() {

        return size == 0;
    }

    /**
     * this method returns the size of the Deque
     */

    public int size() {

        return size;
    }

    /**
     * Prints the items in the deque from first to last,
     * separated by a space. Once all the items have been printed, print out a new line
     */
    public void printDeque() {


        Node p = sentinel.next;
        while (p != null) {
            //print out  current item

            System.out.println(p.item + " ");
            //update the loop
            p = p.next;

        }
        System.out.println();

    }

    /**this method Removes and returns the item at the front of the deque.
     If no such item exists, returns null.*/

    public T removeFirst() {

        if (isEmpty()) {
            return null;
        }
        //create a variable that points to the front of the list:
        T frontItem = sentinel.next.item;
        //updating the elements of the Deque

        sentinel.next = sentinel.next.next;
        sentinel.next.prev = sentinel;
        size -= 1;
        return frontItem;


    }

    /**
     * Removes and returns the item at the back of the deque. If no such item exists, returns null.
     */
    public T removeLast() {
        //list is empty

        if (isEmpty()) {
            return null;

        }
        //first remove the last  item:
        T last = sentinel.prev.item;
        //update the items
        sentinel.prev = sentinel.prev.prev;
        //new sentinel should be the item which we dont care about
        sentinel.prev.next = sentinel;
        size -= 1;
        return last;


    }

    /**gets the item at the given index. . If no such item exists,
     returns null. Must not alter the deque*/
    public T get(int index) {
        //out of range so return null
        if (index >= size) {
            return null;

        }
        //want to create variable that shows the start point
        Node p = sentinel.next;

        while (index > 0) {
            p = p.next;
            index -= 1;

        }
        return p.item;


    }

    /**private helper method to do the get recursively*/


    private T getRecursiveHelper(int index, Node p) {
        if (index >= size) {
            return null;

            //if the call is for the first item return that item

        } else if (index == 0) {
            return p.item;
        } else {
            return getRecursiveHelper(index - 1, p.next);
        }
    }
    /** doing get recursively */

    public T getRecursive(int index) {

        return getRecursiveHelper(index, sentinel.next);

    }
    public LinkedListDeque(LinkedListDeque other) {
        sentinel = new Node(null, null, null);
        sentinel.prev = sentinel;
        sentinel.next = sentinel;
        size = 0;

        for (int i = 0; i < other.size(); i++) {
            addLast((T) other.get(i));
        }



    }


}
