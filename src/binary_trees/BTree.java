package binary_trees;

public class BTree<Key extends Comparable<Key>, Value> implements TreeInterface {
    private static final int M = 4;

    private Node root;       // raiz
    private int height;      // altura da arvore
    private int n;           // total de pares de valor-chave na arvore

    /**
     * Nodo da BTree
     */
    private static final class Node {
        private int m;                             // quantidade de filhos
        private Entry[] children = new Entry[M];   // array de filhos

        // construtor para criar arvore com k filhos
        private Node(int k) {
            m = k;
        }
    }

    /**
     * classe para os pares de chave e valor dentro de um Nodo.
     */
    private static class Entry {
        private Comparable key;
        private Object val;
        private Node next;
        public Entry(Comparable key, Object val, Node next) {
            this.key  = key;
            this.val  = val;
            this.next = next;
        }
    }

    public BTree() {
        root = new Node(0);
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    public int size() {
        return n;
    }

    public int height() {
        return height;
    }

    public Value search(Key key) {
        if (key == null) throw new IllegalArgumentException("argument to get() is null");
        return search(root, key, height);
    }

    public void remove(int data){
        Comparable key = data;
        exclude((Key) key);
    }

    public void exclude(Key key) {
        root = remove(root, key, height);
        n--;
    }

    private Node remove(Node x, Key key, int ht) {
        int i;
        Entry[] children = x.children;

        // se for um Nodo externo  (nodo folha)
        if (ht == 0) {
            for (i = 0; i < x.m; i++) {
                if (eq(key, children[i].key)) {
                    // Key found, remove it
                    for (int j = i; j < x.m - 1; j++) {
                        children[j] = children[j + 1];
                    }
                    x.m--;
                    return x;
                }
            }
        } else {
            // se for um Nodo Interno (nodo que não é folha)
            for (i = 0; i < x.m; i++) {
                if (i + 1 == x.m || less(key, children[i + 1].key)) {
                    Node next = children[i].next;
                    Node newNext = remove(next, key, ht - 1);
                    if (newNext == null && i > 0) {
                        // Filho removido redistribuir as chaves
                        children[i - 1].next = children[i].next;
                        for (int j = i; j < x.m - 1; j++) {
                            children[j] = children[j + 1];
                        }
                        x.m--;
                    }
                    return x;
                }
            }
        }
        // Chave não encontrada
        return x;
    }

    @Override
    public int get(int data) {
        Comparable key = data;
        return (int) search((Key) key);
    }

    private Value search(Node x, Key key, int ht) {
        Entry[] children = x.children;

        // Nodo externo
        if (ht == 0) {
            for (int j = 0; j < x.m; j++) {
                if (eq(key, children[j].key)) return (Value) children[j].val;
            }
        }

        // Nodo interno
        else {
            for (int j = 0; j < x.m; j++) {
                if (j+1 == x.m || less(key, children[j+1].key))
                    return search(children[j].next, key, ht-1);
            }
        }
        return null;
    }

    public void insert(int data){
        Comparable value = data;
        this.put((Key) value, (Value) value);
    }

    public void put(Key key, Value val) {
        if (key == null) throw new IllegalArgumentException("argument key to insert() is null");
        Node u = insert(root, key, val, height);
        n++;
        if (u == null) return;

        // need to split root
        Node t = new Node(2);
        t.children[0] = new Entry(root.children[0].key, null, root);
        t.children[1] = new Entry(u.children[0].key, null, u);
        root = t;
        height++;
    }

    private Node insert(Node h, Key key, Value val, int ht) {
        int j;
        Entry t = new Entry(key, val, null);

        // Nodo externo
        if (ht == 0) {
            for (j = 0; j < h.m; j++) {
                if (less(key, h.children[j].key)) break;
            }
        }

        // Nodo interno
        else {
            for (j = 0; j < h.m; j++) {
                if ((j+1 == h.m) || less(key, h.children[j+1].key)) {
                    Node u = insert(h.children[j++].next, key, val, ht-1);
                    if (u == null) return null;
                    t.key = u.children[0].key;
                    t.val = null;
                    t.next = u;
                    break;
                }
            }
        }

        for (int i = h.m; i > j; i--)
            h.children[i] = h.children[i-1];
        h.children[j] = t;
        h.m++;
        if (h.m < M) return null;
        else         return split(h);
    }

    private Node split(Node h) {
        Node t = new Node(M/2);
        h.m = M/2;
        for (int j = 0; j < M/2; j++)
            t.children[j] = h.children[M/2+j];
        return t;
    }

    public void show() {
        System.out.println(toString(root, height, "") + "\n");
    }

    private String toString(Node h, int ht, String indent) {
        StringBuilder s = new StringBuilder();
        Entry[] children = h.children;

        if (ht == 0) {
            for (int j = 0; j < h.m; j++) {
                s.append(indent).append(children[j].key).append(" ").append(children[j].val).append("\n");
            }
        }
        else {
            for (int j = 0; j < h.m; j++) {
                if (j > 0) s.append(indent).append("(").append(children[j].key).append(")\n");
                s.append(toString(children[j].next, ht-1, indent + "     "));
            }
        }
        return s.toString();
    }

    private boolean less(Comparable k1, Comparable k2) {
        return k1.compareTo(k2) < 0;
    }

    private boolean eq(Comparable k1, Comparable k2) {
        return k1.compareTo(k2) == 0;
    }

    public static void main(String[] args) {
        // testar a arvore
        BTree<Integer, Integer> tree = new BTree<Integer, Integer>();
        tree.put(1,1);
        tree.put(3,3);
        tree.put(4,4);
        tree.put(5,5);
        tree.show();
    }

}