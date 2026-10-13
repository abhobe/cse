public class ArrayQueue<T> implements MyQueue<T>{
    // We've provided you several data fields to implement the circular array
    // Feel free to add some more if you need!
    // Please do not change the visibility of [data].
    public T[] data; // store the data
    private int first; // index of the head of the queue
    private int last; // index of the tail of the queue
    private int size; // size of the circular array

    // We've provided you a default implementation of constructor for the ArrayQueue
    public ArrayQueue(){
        this.data = (T[]) new Object[10]; // DO NOT CHANGE INITIAL SIZE HERE
        first = 0;
        last = -1;
        size = 0;
    }

    public void enqueue(T item) {
        if (this.size == this.data.length) {
            this.resize();
        }

        last = (last + 1) % data.length;
        data[last] = item;
        size++;
    }

    public T dequeue() {
        if (this.isEmpty()) {
            throw new IllegalStateException("queue is empty");
        }

        T ret = data[first];
        data[first] = null;
        
        first = (first + 1) % data.length;
        size--;

        return ret;
    }

    public T peek() {
        if (this.isEmpty()) {
            throw new IllegalStateException("queue is empty");
        }

        return data[first];
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return (size == 0);
    }

    public void resize() {
        T[] newArr = (T[]) new Object[2 * data.length];
        
        for (int i = 0; i < size; i++) {
            newArr[i] = data[(first + i) % size];
        }

        first = 0;
        last = size - 1;
        data = newArr;
    }
}
