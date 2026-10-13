public class LinkedStack<T> implements MyStack<T> {
    private ListNode<T> head;
    private int size = 0;

    private static class ListNode<T>{
        private final T data;
        private ListNode<T> next;

        private ListNode(T data, ListNode<T> next){
            this.data = data;
            this.next = next;
        }

        private ListNode(T data){
            this.data = data;
        }
    }

    public void push(T item) {
        ListNode<T> newNode = new ListNode<T>(item);
        if (this.isEmpty()) {
            this.head = newNode;
        } else {
            newNode.next = this.head;
            this.head = newNode;
        }
        size++;
    }

    public T pop() {
        if (this.isEmpty()) {
            throw new IllegalStateException("stack is empty");
        }
        T ret = this.head.data; 

        if (this.head.next == null) {
            this.head = null;
        } else {
            head = this.head.next;
        }

        size--;
        return ret;
    }

    public T peek() {
        if (this.isEmpty()) {
            throw new IllegalStateException("stack is empty");
        }
        return this.head.data;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }
}
