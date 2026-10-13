public class ArrayStack<T> implements MyStack<T> {
    private  T[] arr;
    private int size = 0;

    @SuppressWarnings("unchecked")
    public ArrayStack() {
        this.arr = (T[]) new Object[10];
    }

    public void push(T item) {
        if (this.size == this.arr.length) {
            this.resize();
        }

        arr[size] = item;
        size++;
    }
    
    public T pop() {
        if (this.isEmpty()) {
            throw new IllegalStateException("stack is empty");
        }

        T ret = this.arr[size - 1];
        this.arr[size - 1] = null;
        size--;
        return ret;
    }

    public T peek() {
        if (this.isEmpty()) {
            throw new IllegalStateException("stack is empty");
        }

        return this.arr[size - 1];
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void resize() {
        @SuppressWarnings("unchecked")
        T[] newArr = (T[]) new Object[this.arr.length + 5];
        for (int i = 0; i < this.arr.length; i++) {
            newArr[i] = this.arr[i];
        }
        this.arr = newArr;
    }
}
