public class AVLTree<K extends Comparable<K>, V> extends BinarySearchTree<K, V> {

    public AVLTree() {
        super();
    }

    public V insert(K key, V value) {
        V answer = find(key);
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
        curr = rebalance(curr, key);

        return curr;
    }



    public TreeNode<K, V> rebalance(TreeNode<K, V> root, K newKey) {
        int leftHeight = root.left == null ? -1 : root.left.height;
        int rightHeight = root.right == null ? -1 : root.right.height;
        int diff = leftHeight - rightHeight;
       
        if (diff == 2) {

            if (newKey.compareTo(root.left.key) > 0)
                root.left = rotateLeft(root.left);    
            root = rotateRight(root);
            

        } else if (diff == -2) {

            // right > left
            if (newKey.compareTo(root.right.key) < 0)
                root.right = rotateRight(root.right);    
            root = rotateLeft(root);
        }

        return root;
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
        newTop.right = top;

        top.updateHeight();
        newTop.updateHeight();

        return newTop;
    }
}
