package utils;

import java.util.ArrayList;
import java.util.Random;


public class CopyPersistentTreap {

    private static final Random rand = new Random();
    ArrayList<Node> roots = new ArrayList<>();
    int version = 0;

    public void incrementVersion() {
        version++;
    }
    public int getVersion() {
        return version;
    }
    public Node getRoot(int num) {
        return roots.get(num);
    }

    public Node recursiveCopyTreap() {
        Node currentFake = roots.get(roots.size() - 1);
        return new Node(currentFake);
    }

    CopyPersistentTreap() {
        Node first = new Node();
        first.left = new Node(new Tuple(0, Integer.MIN_VALUE, Integer.MAX_VALUE, ""));
        roots.add(first);
        CertificateAuthor.signTreap(this);
    }
    public void add(Tuple data) {
        roots.add(recursiveCopyTreap());
        Node currentFake = roots.get(roots.size() - 1);
        add(currentFake.left, data);
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
        roots.add(recursiveCopyTreap());
        Node currentFake = roots.get(roots.size() - 1);
        remove(currentFake.left, data);
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
//
//    public boolean contains(Tuple data, int version) {
//        Node node = roots.get(version);
//        while (node != null) {
//            int compare = data.compareTo(node.data);
//            if (compare < 0) node = node.left;
//            else if (compare > 0) node = node.right;
//            else return true;
//        }
//        return false;
//    }
public Tuple getExact(int key, int version) {
    return getExact(new Tuple(3 * version, key, key, ""));
}

    private Tuple getExact(Tuple data) {
        Node node = roots.get(data.getLastVersion()).getLeft();
        while (node != null) {
            Node.Modification box = node.box;
            Node copy = node.copy();
            if (box != null && box.timestamp <= data.getLastVersion()) copy.evaluate(box, box.getModificator());
            int compare = data.compareTo(copy.getData());
            if (compare < 0) node = copy.getLeft();
            else if (compare > 0) node = copy.getRight();
            else return copy.data;
        }
        return null;
    }

    public Tuple getLower(int key, int version) {
        return getLower(key, version, 0);
    }

    public Tuple getLower(int key, int version, int add) {
        return getLower(new Tuple(3 * version + add, key, key, ""));
    }

    private Tuple getLower(Tuple data) {
        Node node = roots.get(data.getLastVersion()).left;
        Node prev = node;
        while (node != null) {
            prev = node;
            int compare = data.compareTo(prev.data);
            if (compare < 0) node = prev.left;
            else if (compare > 0) node = prev.right;
        }
        return prev.data;
    }

    public boolean contains(int key, int version) {
        return getExact(key, version) != null;
    }


    public Tuple first() {
        return first(root);
    }

    private Tuple first(Node searchNode) {
        Node node = searchNode;
        while (node.left != null) node = node.left;
        return node.data;
    }

    @Override
    public String toString() {
        return "Treap{" +
                "root=" + root +
                '}';
    }

    static class Node {
        public Node right, left;
        public int priority = rand.nextInt();
        public Tuple data;

        public Node(Tuple data) {
            this.data = data;
        }
        public Node() {}

        public Node(Node input) {
            if(input.left != null) {
                this.left = new Node(input.left);
            }
            if(input.right != null) {
                this.right = new Node(input.right);
            }
            this.priority = input.priority;
            this.data = input.data;
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
