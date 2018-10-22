package utils;

import utils.Tuple;

import java.util.ArrayList;
import java.util.Random;

public class Treap {

    private static final Random rand = new Random();
    private ArrayList<Node> roots = new ArrayList<>();
    private int version = 0;

    void incrementVersion() {
        version++;
    }

    int getVersion() {
        return version;
    }

    public void add(Tuple data) {
        root = add(root, data);
    }

    private Node add(Node node, Tuple data) {
        if (node == null)
            return new Node(data);

        int compare = data.compareTo(node.data);
        if (compare < 0) {
            node.left = add(node.left, data);
            if (node.priority > node.left.priority)
                return rotateRight(node);
        } else if (compare > 0) {
            node.right = add(node.right, data);
            if (node.priority > node.right.priority)
                return rotateLeft(node);
        }
        return node;
    }

    private Node rotateRight(Node node) {
        Node lnode = node.left;
        node.left = lnode.right;
        lnode.right = node;
        return lnode;
    }

    private Node rotateLeft(Node node) {
        Node rnode = node.right;
        node.right = rnode.left;
        rnode.left = node;
        return rnode;
    }

    public void remove(Tuple data) {
        root = remove(root, data);
    }

    private Node remove(Node node, Tuple data) {
        if (node != null) {
            int compare = data.compareTo(node.data);
            if (compare < 0) {
                node.left = remove(node.left, data);
            } else if (compare > 0) {
                node.right = remove(node.right, data);
            } else {
                if (node.left == null) {
                    return node.right;
                } else if (node.right == null) {
                    return node.left;
                } else {
                    node.data = first(node.right);
                    node.right = remove(node.right, node.data);
                }
            }
        }
        return node;
    }

    public Tuple first() {
        return first(root);
    }

    private Tuple first(Node searchNode) {
        Node node = searchNode;
        while (node.left != null) node = node.left;
        return node.data;
    }

    public boolean contains(Tuple data) {
        Node node = root;
        while (node != null) {
            int compare = data.compareTo(node.data);
            if (compare < 0) node = node.left;
            else if (compare > 0) node = node.right;
            else return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return "utils.Treap{" +
                "root=" + root +
                '}';
    }

    private static class Node {
        public Node right, left;
        public final int priority = rand.nextInt();
        public Tuple data;

        public Node(Tuple data) {
            this.data = data;
        }

        @Override
        public String toString() {
            return "Node{" +
                    "item=" + data +
                    ", priority=" + priority +
                    ", left=" + left +
                    ", right=" + right +
                    '}';
        }
    }
}