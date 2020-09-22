/**
 * Double Linked List class
 * 
 * @author omaralshikh ahmad malik
 * @param <E>
 *            generic
 * @version 8/12/2020
 */
public class DLList<E> {

    private static class Node<E> {
        private Node<E> next;
        private Node<E> previous;
        private E data;


        /**
         * Creates a new node with the given data
         *
         * @param d
         *            the data to put inside the node
         */
        public Node(E d) {
            data = d;
        }


        /**
         * Sets the node after this node
         *
         * @param n
         *            the node after this one
         */
        public void setNext(Node<E> n) {
            next = n;
        }


        /**
         * Sets the node before this one
         *
         * @param n
         *            the node before this one
         */
        public void setPrevious(Node<E> n) {
            previous = n;
        }


        /**
         * Gets the next node
         *
         * @return the next node
         */
        public Node<E> next() {
            return next;
        }


        /**
         * Gets the node before this one
         *
         * @return the node before this one
         */
        public Node<E> previous() {
            return previous;
        }


        /**
         * Gets the data in the node
         *
         * @return the data in the node
         */
        public E getData() {
            return data;
        }
    }

    /**
     * How many nodes are in the list
     */
    private int size;

    /**
     * The first node in the list. THIS IS A SENTINEL NODE AND AS SUCH DOES NOT
     * HOLD ANY DATA. REFER TO init()
     */
    private Node<E> head;

    /**
     * The last node in the list. THIS IS A SENTINEL NODE AND AS SUCH DOES NOT
     * HOLD ANY DATA. REFER TO init()
     */
    private Node<E> tail;


    /**
     * constructor
     * 
     * @param sizee
     *            size given
     */
    public DLList(int sizee) {
        init(sizee);
    }


    /**
     * Initializes the object to have the head and tail nodes
     */
    private void init(int sizee) {
        head = new DLList.Node<E>(null);
        tail = new DLList.Node<E>(null);
        head.setNext(tail);
        tail.setPrevious(head);
        // Populate the list with null entries.
        for (int i = 0; i < sizee; i++) {
            Node<E> nullNode = new Node<E>(null);
            head.next().setPrevious(nullNode);
            nullNode.setNext(head.next());
            nullNode.setPrevious(head);
            head.setNext(nullNode);
        }
    }


    /**
     * Gets the number of elements in the list
     *
     * @return the number of elements
     */
    public int size() {
        return size;
    }


    /**
     * adds elements or moves up elements if it used again in buffer pool
     * 
     * @param data
     *            data to be checked
     * @return adds data to pool or moves it up
     */
    public E moveupOradd(E data) {
        if (data == null) {
            return add(data);
        }

        Node<E> node = head;
        while ((node = node.next()) != tail) {
            if (data.equals(node.getData())) {
                moveup(node);
                return null;
            }
        }

        return add(data);
    }


    /**
     * helper method to add elements
     * 
     * @param data
     *            added data
     * @return data
     */
    private E add(E data) {
        // Allocate and insert a new node.
        Node<E> node = new Node<E>(data);
        node.setPrevious(head);
        node.setNext(head.next());
        head.next().setPrevious(node);
        head.setNext(node);

        // Remove the last element from the list.
        Node<E> last = tail.previous();
        last.previous().setNext(tail);
        tail.setPrevious(last.previous());
        size++;
        // Return the data
        return last.getData();
    }


    /**
     * Removes the first object in the list that .equals(obj)
     *
     * @param obj
     *            the object to remove
     */

    public void remove(E obj) {
        Node<E> current = head.next();
        while (!current.equals(tail)) {

            if (obj.equals(current.getData())) {
                nodeDelete(current);

                Node<E> node = new Node<E>(null);
                node.setNext(tail);
                node.setPrevious(tail.previous());
                tail.previous().setNext(node);
                tail.setPrevious(node);
                size--;
                return;
            }
            current = current.next();
        }
    }


    /**
     * Remove a node from the list
     * 
     * @param node
     *            The node to remove
     */
    private void nodeDelete(Node<E> node) {
        // Remove from the current position
        node.next().setPrevious(node.previous());
        node.previous().setNext(node.next());
    }


    /**
     * helper method to move data up the pool
     * 
     * @param node
     *            node to be moved
     */
    private void moveup(Node<E> node) {
        nodeDelete(node);
        node.setPrevious(head);
        node.setNext(head.next());
        head.next().setPrevious(node);
        head.setNext(node);
    }

}
