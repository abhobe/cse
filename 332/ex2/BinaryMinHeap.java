import java.lang.reflect.Array;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

public class BinaryMinHeap <T extends Comparable<T>> implements MyPriorityQueue<T> {
    private int size; // Maintains the size of the data structure
    private T[] arr; // The array containing all items in the data structure
                     // index 0 must be utilized
    private Map<T, Integer> itemToIndex; // Keeps track of which index of arr holds each item.

    public BinaryMinHeap(){
        // This line just creates an array of type T. We're doing it this way just
        // because of weird java generics stuff (that I frankly don't totally understand)
        // If you want to create a new array anywhere else (e.g. to resize) then
        // You should mimic this line. The second argument is the size of the new array.
        arr = (T[]) Array.newInstance(Comparable.class, 10);
        size = 0;
        itemToIndex = new HashMap<>();
    }

    // swap items at two indices
    private void swap(int a, int b) {
        T A = arr[a];
        T B = arr[b];

        arr[a] = B;
        arr[b] = A;
        itemToIndex.put(A, b);
        itemToIndex.put(B, a);

    }

    // move the item at index i "rootward" until
    // the heap property holds
    private void percolateUp(int i){
        if (i == 0) {
            return;
        }

        // parent index
        int p = (i-1)/2;
        
        // if child < parent, swap
        if (arr[i].compareTo(arr[p]) < 0) {
            swap(i, p);
            percolateUp(p);
        }
    }

    // move the item at index i "leafward" until
    // the heap property holds
    private void percolateDown(int i){
        int c = 2*i + 1;

        if (c >= size) {
            return;
        }

        // if there are 2 child nodes select the smaller one
        if (c+1 < size && (arr[c].compareTo(arr[c+1]) > 0)) {
            c++;
        }

        // if parent > child, swap
        if (arr[i].compareTo(arr[c]) > 0) {
            swap(i, c);
            percolateDown(c);
        }
    }

    // copy all items into a larger array to make more room.
    private void resize(){
        T[] larger = (T[]) Array.newInstance(Comparable.class, arr.length*2);
        for(int i = 0; i < arr.length; i++){
            larger[i] = arr[i];
        }
        arr = larger;
    }

    public void insert(T item){
        if (size == arr.length) {
            resize();
        }

        arr[size] = item;
        itemToIndex.put(item, size);
        size++;
        percolateUp(size - 1);
    }


    public T extract(){
        if(isEmpty()){
            throw new IllegalStateException();
        }

        T ret = arr[0];
        itemToIndex.remove(ret);
        size--;
        swap(0, size);
        arr[size] = null;
        percolateDown(0);
        return ret;
    }

    // Remove the item at the given index.
    // Make sure to maintain the heap property!
    private T remove(int index){
        if(index >= size){
            throw new IndexOutOfBoundsException();
        }

        T ret = arr[index];
        itemToIndex.remove(ret);
        size--;
        swap(index, size);
        arr[size] = null;
        

        // if removing the last element, skip percolation
        if (index != size) {
            percolateUp(index);
            percolateDown(index);
        }
        
        return ret;
    }

    // We have provided a recommended implementation
    // You're welcome to do something different, though!
    public void remove(T item){
        if (!itemToIndex.containsKey(item)){
            throw new IllegalArgumentException();
        }
        remove(itemToIndex.get(item));
    }

    // Determine whether to percolate up/down
    // the item at the given index, then do it!
    private void updatePriority(int index){
        if (index >= size) {
            throw new IndexOutOfBoundsException();
        }
        percolateUp(index);
        percolateDown(index);
    }

    // This method gets called after the client has 
    // changed an item in a way that may change its
    // priority. In this case, the client should call
    // updatePriority on that changed item so that 
    // the heap can restore the heap property.
    // Throws an IllegalArgumentException if the given
    // item is not an element of the priority queue.
    // We have provided a recommended implementation
    // You're welcome to do something different, though!
    public void updatePriority(T item){
	    if(!itemToIndex.containsKey(item)){
            throw new IllegalArgumentException("Given item is not present in the priority queue!");
	    }
        updatePriority(itemToIndex.get(item));
    }

    // We have provided a recommended implementation
    // You're welcome to do something different, though!
    public boolean isEmpty(){
        return size == 0;
    }

    // We have provided a recommended implementation
    // You're welcome to do something different, though!
    public int size(){
        return size;
    }

    // We have provided a recommended implementation
    // You're welcome to do something different, though!
    public T peek(){
        if(isEmpty()){
            throw new IllegalStateException();
        }
        return arr[0];
    }
    
    // We have provided a recommended implementation
    // You're welcome to do something different, though!
    public List<T> toList(){
        List<T> copy = new ArrayList<>();
        for(int i = 0; i < size; i++){
            copy.add(i, arr[i]);
        }
        return copy;
    }

    // For debugging
    public String toString(){
        if(size == 0){
            return "[]";
        }
        String str = "[(" + arr[0] + " " + itemToIndex.get(arr[0]) + ")";
        for(int i = 1; i < size; i++ ){
            str += ",(" + arr[i] + " " + itemToIndex.get(arr[i]) + ")";
        }
        return str + "]";
    }
    
}
