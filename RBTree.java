public class RBTree<Key extends Comparable<Key>, Value> {
    private enum  Color {
        BLACK,
        RED
    }
    // Корень дерева
    private Node root;

    // Класс ветки дерева
    private class Node {
        Key key;
        Value value;
        Node left, right;
        Color color;
        int size;

        Node(Key key, Value value, Color color, int size) {
            this.key = key;
            this.value = value;
            this.color = color;
            this.size = size;
        }
    }

    // Конструктор
    public RBTree() {}

    // Проверка на "красность"
    private boolean is_red(Node node) {
        if (node == null) return false;
        return node.color == Color.RED;
    }

    private int size(Node node) {
        if (node == null) return 0;
        return node.size;
    }

    // Подсчет размера
    public int size() {
        return size(root);
    }

    public boolean isEmpty() {
        return root == null;
    }

    //получение значения по ключу
    public Value get(Key key) {
        if (key == null) throw new IllegalArgumentException("Key cannot be null");
        return get(root, key);
    }

    private Value get(Node node, Key key) {
        while (node != null) {
            int cmp = key.compareTo(node.key);
            if (cmp < 0)
                node = node.left;
            else if (cmp > 0)
                node = node.right;
            else
                return node.value;
        }
        return null;
    }

    // Поиск на соответствие ключу
    public boolean contains(Key key) {
        return get(key) != null;
    }

    //Вставка заначения по ключу
    public void insert(Key key, Value value) {
        if (key == null)
            throw new IllegalArgumentException("Key cannot be null");
        if (value == null) {
            remove(key);
            return;
        }
        root = insert(root, key, value);
        root.color = Color.BLACK;
    }

    private Node insert(Node node, Key key, Value value) {
        if (node == null) return new Node(key, value, Color.RED, 1);

        int cmp = key.compareTo(node.key);
        if      (cmp < 0)
            node.left  = insert(node.left,  key, value);
        else if (cmp > 0)
            node.right = insert(node.right, key, value);
        else
            node.value = value;

        if (is_red(node.right) && !is_red(node.left))
            node = rotate_left(node);
        if (is_red(node.left)  && is_red(node.left.left))
            node = rotate_right(node);
        if (is_red(node.left)  && is_red(node.right))
            change_colors(node);

        node.size = size(node.left) + size(node.right) + 1;
        return node;
    }

    // Удаление минимального элемента
    public void pop_back() {
        if (isEmpty())
            throw new IllegalStateException("Tree is empty");
        if (!is_red(root.left) && !is_red(root.right))
            root.color = Color.RED;

        root = pop_back(root);

        if (!isEmpty()) root.color = Color.BLACK;
    }

    private Node pop_back(Node node) {
        if (node.left == null) return null;

        if (!is_red(node.left) && !is_red(node.left.left))
            node = moveRedLeft(node);

        node.left = pop_back(node.left);
        return balance(node);
    }

    // Удаление максимального элемента
    public void pop_front() {
        if (isEmpty())
            throw new IllegalStateException("Tree is empty");
        if (!is_red(root.left) && !is_red(root.right))
            root.color = Color.RED;

        root = pop_front(root);

        if (!isEmpty()) root.color = Color.BLACK;
    }

    private Node pop_front(Node node) {
        if (is_red(node.left))
            node = rotate_right(node);

        if (node.right == null)
            return null;

        if (!is_red(node.right) && !is_red(node.right.left))
            node = moveRedRight(node);

        node.right = pop_front(node.right);
        return balance(node);
    }

    // Удаление по ключу
    public void remove(Key key) {
        if (key == null)
            throw new IllegalArgumentException("Key cannot be null");
        if (!contains(key))
            return;
        if (!is_red(root.left) && !is_red(root.right))
            root.color = Color.RED;

        root = remove(root, key);

        if (!isEmpty()) root.color = Color.BLACK;
    }

    private Node remove(Node node, Key key) {
        if (key.compareTo(node.key) < 0) {
            if (!is_red(node.left) && !is_red(node.left.left))
                node = moveRedLeft(node);
            node.left = remove(node.left, key);
        } else {
            if (is_red(node.left))
                node = rotate_right(node);
            if (key.compareTo(node.key) == 0 && (node.right == null))
                return null;
            if (!is_red(node.right) && !is_red(node.right.left))
                node = moveRedRight(node);
            if (key.compareTo(node.key) == 0) {
                Node x = min(node.right);
                node.key = x.key;
                node.value = x.value;
                node.right = pop_back(node.right);
            }
            else node.right = remove(node.right, key);
        }
        return balance(node);
    }

    private Node rotate_left(Node node) {
        Node x = node.right;
        node.right = x.left;
        x.left = node;
        x.color = node.color;
        node.color = Color.RED;
        x.size = node.size;
        node.size = size(node.left) + size(node.right) + 1;
        return x;
    }

    private Node rotate_right(Node node) {
        Node x = node.left;
        node.left = x.right;
        x.right = node;
        x.color = node.color;
        node.color = Color.RED;
        x.size = node.size;
        node.size = size(node.left) + size(node.right) + 1;
        return x;
    }

    private void change_colors(Node node) {
        node.color = node.color == Color.RED ? Color.BLACK: Color.RED;
        node.left.color = node.left.color == Color.RED ? Color.BLACK: Color.RED;
        node.right.color = node.right.color == Color.RED ? Color.BLACK: Color.RED;
    }

    private Node moveRedLeft(Node node) {
        change_colors(node);
        if (is_red(node.right.left)) {
            node.right = rotate_right(node.right);
            node = rotate_left(node);
            change_colors(node);
        }
        return node;
    }

    private Node moveRedRight(Node node) {
        change_colors(node);
        if (is_red(node.left.left)) {
            node = rotate_right(node);
            change_colors(node);
        }
        return node;
    }

    private Node balance(Node node) {
        if (is_red(node.right))
            node = rotate_left(node);
        if (is_red(node.left) && is_red(node.left.left))
            node = rotate_right(node);
        if (is_red(node.left) && is_red(node.right))
            change_colors(node);

        node.size = size(node.left) + size(node.right) + 1;
        return node;
    }

    // Возвращение ключа минимального элемента
    public Key min() {
        if (isEmpty())
            throw new IllegalStateException("Tree is empty");
        return min(root).key;
    }

    private Node min(Node node) {
        if (node.left == null)
            return node;
        return min(node.left);
    }

    // Возвращение ключа максимального элемента
    public Key max() {
        if (isEmpty())
            throw new IllegalStateException("Tree is empty");
        return max(root).key;
    }

    private Node max(Node node) {
        if (node.right == null)
            return node;
        return max(node.right);
    }
}

