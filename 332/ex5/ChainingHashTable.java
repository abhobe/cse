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
        int newSize = -1;
        for (int i = 0; i < primes.length; i++ ) {
            if (primes[i] > table.length) {
                newSize = primes[i];
                break;
            }
        }

        if (newSize == -1) {
            newSize = (int)Math.pow(2, Math.ceil(Math.log(table.length) / Math.log(2)) + 1) + 1;
        }


        List<Item<K, V>>[] oldTable = table;
        table = (LinkedList<Item<K, V>>[]) Array.newInstance(LinkedList.class, newSize);
        for (int i = 0; i < table.length; i++) {
            table[i] = new LinkedList<>();
        }
        size = 0;

        for (int i = 0; i < oldTable.length; i++) {
            List<Item<K, V>> list = oldTable[i];
            for (Item<K, V> item : list) {
                insert(item.key, item.value);
            }
        }
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public double load() {
        return size/table.length;
    }

    public int getIndex(K key) {
        return (key.hashCode() % table.length + table.length) % table.length;
    }

    public V insert(K key, V value) {
        if (load() >= 2) {
            rehash();
        }

        int index = getIndex(key);
        List<Item<K, V>> list = table[index];

        
        for (Item<K, V> item : list) {
            if (key.equals(item.key)) {
                int i = list.indexOf(item);
                list.set(i, new Item<K, V>(key, value));
                return item.value;
            }
        }
        table[index].add(new Item<K, V>(key, value));
        size++;
        return null;
    }

    public V find(K key) {
        int index = getIndex(key);
        List<Item<K, V>> list = table[index];
        for (Item<K, V> item : list) {
            if (key.equals(item.key)) {
                return item.value;
            }
        }
        return null;
    }

    public boolean contains(K key) {
        return find(key) != null;
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
