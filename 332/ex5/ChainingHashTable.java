import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ChainingHashTable<K, V> implements DeletelessDictionary<K, V> {
    public List<Item<K, V>>[] table; // The table itself is an array of linked lists of items.
    private int size;
    private static int[] primes = {11, 23, 47, 97, 197, 397};

    public ChainingHashTable() {
        table = (LinkedList<Item<K, V>>[]) Array.newInstance(LinkedList.class, primes[0]);
        for (int i = 0; i < table.length; i++) {
            table[i] = new LinkedList<>();
        }
        size = 0;
    }

    private void rehash() {
        // TODO
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public V insert(K key, V value) {
        // TODO
    }

    public V find(K key) {
        // TODO
    }

    public boolean contains(K key) {
        if (find(key) != null) {
            return true;
        }

        return false;
    }

    public List<K> getKeys() {
        List<K> keys = new ArrayList<>();
        for (int i = 0; i < table.length; i++) {
            for (Item<K, V> item : table[i]) {
                keys.add(item.key);
            }
        }
        return keys;
    }
    
    public List<V> getValues() {
        List<V> values = new ArrayList<>();
        for (int i = 0; i < table.length; i++) {
            for (Item<K, V> item : table[i]) {
                values.add(item.value);
            }
        }
        return values;
    }

    public String toString() {
        String s = "{";
        s += table[0];
        for (int i = 1; i < table.length; i++) {
            s += "," + table[i];
        }
        return s + "}";
    }

}
