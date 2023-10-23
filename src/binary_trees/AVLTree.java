package binary_trees;
import java.util.Stack;

public class AVLTree implements  TreeInterface{
    private TreeNode root;
    private static class TreeNode {
        int data;
        TreeNode left;
        TreeNode right;
        int height;

        public TreeNode(int data) {
            this.data = data;
            this.height = 1;
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("TreeNode{");
            sb.append("data=").append(data);
            sb.append(", height=").append(height);
            sb.append('}');
            return sb.toString();
        }
    }
    public AVLTree() {
        root = null;
    }

    @Override
    public void show(){
        show(this.root);
    }

    public void show(TreeNode root) {
        System.out.println("-----------");

        if (root == null) {
            return;
        }

        Stack<TreeNode> stack = new Stack<>();
        Stack<String> prefixes = new Stack<>();
        stack.push(root);
        prefixes.push("");

        while (!stack.isEmpty()) {
            TreeNode currentNode = stack.pop();
            String currentPrefix = prefixes.pop();

            if (currentNode != null) {
                System.out.println(currentPrefix + "-- (" + currentNode + ")");
                if (currentNode.left != null) {
                    stack.push(currentNode.left);
                    prefixes.push(currentPrefix + "-    ");
                }
                if (currentNode.right != null) {
                    stack.push(currentNode.right);
                    prefixes.push(currentPrefix + "-   ");
                }
            }
        }
    }

    @Override
    public void insert(int data) {
        if (root == null) {
            root = new TreeNode(data);
            return;
        }

        Stack<TreeNode> stack = new Stack<>();
        TreeNode current = root;

        while (true) {
            stack.push(current);
            if (data < current.data) {
                if (current.left == null) {
                    current.left = new TreeNode(data);
                    break;
                }
                current = current.left;
            } else {
                if (current.right == null) {
                    current.right = new TreeNode(data);
                    break;
                }
                current = current.right;
            }
        }

        while (!stack.isEmpty()) {
            current = stack.pop();
            updateHeight(current);
            balance(current, stack);
        }
    }
    @Override
    public void remove(int data) {
        if (root == null) {
            return;
        }

        Stack<TreeNode> stack = new Stack<>();
        TreeNode current = root;
        TreeNode parent = null;

        // Search for the node to remove and keep track of its parent
        while (current != null && current.data != data) {
            stack.push(current);
            parent = current;
            if (data < current.data) {
                current = current.left;
            } else {
                current = current.right;
            }
        }

        if (current == null) {
            // The element was not found in the tree
            return;
        }

        // Case 1: Node has no children
        if (current.left == null && current.right == null) {
            if (parent == null) {
                // The root is the only node, so set it to null
                root = null;
            } else if (parent.left == current) {
                parent.left = null;
            } else {
                parent.right = null;
            }
        }
        // Case 2: Node has one child
        else if (current.left == null || current.right == null) {
            TreeNode child = (current.left != null) ? current.left : current.right;
            if (parent == null) {
                root = child;
            } else if (parent.left == current) {
                parent.left = child;
            } else {
                parent.right = child;
            }
        }
        // Case 3: Node has two children
        else {
            TreeNode successor = findMin(current.right);
            current.data = successor.data;
            remove(successor.data, current.right, current);
        }
        while (!stack.isEmpty()) {
            parent = stack.pop();
            updateHeight(parent);
            balance(parent, stack);
        }
    }

    @Override
    public int get(int data) {
        return search(this.root, data).data;
    }

    private TreeNode search(TreeNode node, int data) {
        if (node == null || node.data == data) {
            return node;
        }

        if (data < node.data) {
            return search(node.left, data);
        } else {
            return search(node.right, data);
        }
    }

    private void remove(int data, TreeNode node, TreeNode parent) {
        if (node == null) {
            return;
        }

        if (data < node.data) {
            remove(data, node.left, node);
        } else if (data > node.data) {
            remove(data, node.right, node);
        } else {
            if (node.left == null && node.right == null) {
                if (parent.left == node) {
                    parent.left = null;
                } else {
                    parent.right = null;
                }
            } else if (node.left == null || node.right == null) {
                TreeNode child = (node.left != null) ? node.left : node.right;
                if (parent.left == node) {
                    parent.left = child;
                } else {
                    parent.right = child;
                }
            } else {
                TreeNode successor = findMin(node.right);
                node.data = successor.data;
                remove(successor.data, node.right, node);
            }
        }
    }

    private TreeNode findMin(TreeNode node) {
        while (node.left != null) {
            node = node.left;
        }
        return node;
    }

    private int height(TreeNode node) {
        return (node != null) ? node.height : 0;
    }

    private void updateHeight(TreeNode node) {
        node.height = Math.max(height(node.left), height(node.right)) + 1;
    }

    private void balance(TreeNode node, Stack<TreeNode> stack) {
        int balanceFactor = height(node.left) - height(node.right);

        if (balanceFactor > 1) {
            if (height(node.left.left) < height(node.left.right)) {
                node.left = rotateLeft(node.left);
            }
            node = rotateRight(node);
        } else if (balanceFactor < -1) {
            if (height(node.right.right) >= height(node.right.left)) {
                node = rotateLeft(node);
            } else {
                node.right = rotateRight(node.right);
                node = rotateLeft(node);
            }

        }

        if (!stack.isEmpty()) {
            TreeNode parent = stack.peek();
            if (parent.left == node) {
                parent.left = node;
            } else {
                parent.right = node;
            }
        } else {
            root = node;
        }
    }

    private TreeNode rotateRight(TreeNode y) {
        TreeNode x = y.left;
        TreeNode T2 = x.right;

        x.right = y;
        y.left = T2;

        updateHeight(y);
        updateHeight(x);

        return x;
    }

    private TreeNode rotateLeft(TreeNode x) {
        TreeNode y = x.right;
        TreeNode T2 = y.left;

        y.left = x;
        x.right = T2;

        updateHeight(x);
        updateHeight(y);

        return y;
    }

    public void inOrderTraversal() {
        inOrder(root);
    }

    private void inOrder(TreeNode node) {
        if (node != null) {
            inOrder(node.left);
            System.out.print(node.data + " ");
            inOrder(node.right);
        }
    }
}