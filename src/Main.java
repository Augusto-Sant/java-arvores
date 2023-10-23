import binary_trees.AVLTree;
import binary_trees.BTree;
import binary_trees.BinaryTree;
import binary_trees.TreeInterface;

public class Main {
    public static void main(String[] args) {
        double binaryTreeTime = measureTime(Main::testBinaryTree);
        double avlBinaryTreeTime = measureTime(Main::testAvlBinaryTree);
        double bTreeTime = measureTime(Main::testBTree);

        System.out.println(">> Tempo total de Arvore Binaria (ms): " + binaryTreeTime);
        System.out.println(">> Tempo total de Arvore Binaria AVL (ms): " + avlBinaryTreeTime);
        System.out.println(">> Tempo total de Arvore B (ms): " + bTreeTime);
    }

    public static long measureTime(Runnable function) {
        long startTime = System.nanoTime();
        function.run();
        long endTime = System.nanoTime();
        return (endTime - startTime) / 1_000_000;
    }

    public static void testTree(TreeInterface tree){
        long insertTime100k = measureTime(() -> insertElements(tree, 100_000));
        System.out.println(" 1- Tempo de insercao de 100,000 elementos: " + insertTime100k + " ms");
        long removeTime100k = measureTime(() -> removeElements(tree, 100_000));
        System.out.println(" 2- Tempo de remocao de 100,000 elementos: " + removeTime100k + " ms");

        long insertTime1M = measureTime(() -> insertElements(tree, 1_000_000));
        System.out.println(" 3- Tempo de insercao 1,000,000 elementos: " + insertTime1M + " ms");

        long removeTime1M = measureTime(() -> removeElements(tree, 1_000_000));
        System.out.println(" 4- Tempo de remocao de 1,000,000 elementos: " + removeTime1M + " ms");
    }
    public static void insertElements(TreeInterface tree, int elements) {
        for (int i = 1; i <= elements; i++) {
            tree.insert(i);
        }
    }

    public static void removeElements(TreeInterface tree, int elements) {
        for (int i = 1; i <= elements; i++) {
            tree.remove(i);
        }
    }
    public static void testBinaryTree() {
        System.out.println(">> Testando Arvore Binaria");
        testTree(new BinaryTree());
    }

    public static void testAvlBinaryTree() {
        System.out.println(">> Testando Arvore Binaria AVL");
        testTree(new AVLTree());
    }

    public static void testBTree() {
        System.out.println(">> Testing B Tree");
        BTree<Integer, String> tree = new BTree<Integer, String>();
        testTree(new BTree<Integer, Integer>());
    }
}
