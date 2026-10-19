public class AVLTree<K extends Comparable<K>, V> extends BinarySearchTree<K, V> {

    public AVLTree() {
        super();
    }

    public V insert(K key, V value) {
        V answer = find(key, root);
        if (answer == null) {
            size++;
        }
        root = insert(key, value, root);
        root.updateHeight();
        return answer;
    }

    private TreeNode<K, V> insert(K key, V value, TreeNode<K, V> curr) {
        if (curr == null) {
            return new TreeNode<>(key, value);
        }
        int currMinusNew = curr.key.compareTo(key);
        if (currMinusNew == 0) {
            curr.value = value;
        } else if (currMinusNew < 0) {
            curr.right = insert(key, value, curr.right);
        } else {
            curr.left = insert(key, value, curr.left);
        }
        curr.updateHeight();



        return curr;
    }


    public void rebalance(TreeNode<K, V> root) {
        int balanceFactor = root.left.key.compareTo(root.right.key);

        if (balanceFactor >= 2)
    }


    private TreeNode<K,V> rotateLeft(TreeNode<K, V> top) {
        TreeNode<K, V> newTop = top.right;
        top.right = newTop.left;
        newTop.left = top;

        top.updateHeight();
        newTop.updateHeight();

        return newTop;
    }

    private TreeNode<K,V> rotateRight(TreeNode<K, V> top) {
        TreeNode<K, V> newTop = top.left;
        top.left = newTop.right;
        newTop.left = top;

        top.updateHeight();
        newTop.updateHeight();

        return newTop;
    }
}
