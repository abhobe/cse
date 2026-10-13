public class LinkedQueue<E> implements MyQueue<E>{
    private ListNode<E> head;
    private ListNode<E> tail;
    private int size = 0;
    
    
    private static class ListNode<E>{
        private final E data;
        private ListNode<E> next;

        private ListNode(E data, ListNode<E> next){
            this.data = data;
            this.next = next;
        }

        private ListNode(E data){
            this.data = data;
        }
    }

    public void enqueue(E item) {
        ListNode<E> newNode = new ListNode<E>(item);
        
        if (this.isEmpty()) {
            head = newNode;
            tail = newNode;
        } else {
            tail.next = newNode;
            tail = tail.next;
        }

        size++;
    }

    public E dequeue() {
        if (this.isEmpty()) {
            throw new IllegalStateException("queue is empty");
        }

        E ret = this.head.data;

        if (this.head.next == null) {
            head = null;
            tail = null;
        } else {
            head = head.next;
        }

        size--;
        return ret;
    }

    public E peek() {
        if (this.isEmpty()) {
            throw new IllegalStateException("queue is empty");
        }

        return this.head.data;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return (size == 0);
    }
}
