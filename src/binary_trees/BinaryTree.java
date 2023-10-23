package binary_trees;
import java.util.Stack;

public class BinaryTree implements  TreeInterface{
    private TreeNode root;
    private static class TreeNode {
        int data;
        TreeNode left;
        TreeNode right;

        public TreeNode(int data) {
            this.data = data;
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("TreeNode{");
            sb.append("data=").append(data);
            sb.append('}');
            return sb.toString();
        }
    }
    public BinaryTree() {
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

        TreeNode current = root;

        while (true) {
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
    }
    @Override
    public void remove(int data) {
        if (root == null) {
            return;
        }

        TreeNode current = root;
        TreeNode parent = null;

        // Search for the node to remove and keep track of its parent
        while (current != null && current.data != data) {
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