package utils;

import java.util.ArrayList;


public class CopyPersistentTreap extends GeneralTreap {

    CopyPersistentTreap() {
        Node first = new Node();
        first.left = new Node(new Tuple(0, Integer.MIN_VALUE, Integer.MAX_VALUE, ""));
        ArrayList<GeneralNode> roots = new ArrayList<>();
        roots.add(first);
        setRoots(roots);
        CertificateAuthor.signTreap(this);
    }

    private GeneralNode recursiveCopyTreap() {
        GeneralNode currentFake = getRoots().get(getRootsSize() - 1);
        return new Node(currentFake);
    }

    public void add(Tuple data) {
        incrementVersion();
        ArrayList<GeneralNode> roots = getRoots();
        roots.add(recursiveCopyTreap());
        GeneralNode currentFake = roots.get(roots.size() - 1);
        currentFake.left = add(currentFake.left, data);
        setRoots(roots);
    }

    private GeneralNode add(GeneralNode node, Tuple data) {
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

    private GeneralNode rotateRight(GeneralNode node) {
        GeneralNode lnode = node.left;
        node.left = lnode.right;
        lnode.right = node;
        return lnode;
    }

    private GeneralNode rotateLeft(GeneralNode node) {
        GeneralNode rnode = node.right;
        node.right = rnode.left;
        rnode.left = node;
        return rnode;
    }

    public void remove(Tuple data) {
        incrementVersion();
        ArrayList<GeneralNode> roots = getRoots();
        roots.add(recursiveCopyTreap());
        GeneralNode currentFake = roots.get(roots.size() - 1);
        currentFake.left = remove(currentFake.left, data);
        setRoots(roots);
    }

    private GeneralNode remove(GeneralNode node, Tuple data) {
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

    public Tuple getExact(int key, int version) {
    return getExact(new Tuple(3 * version, key, key, ""));
}

    private Tuple getExact(Tuple data) {
        GeneralNode node = getRoots().get(data.getLastVersion()).left;
        while (node != null) {
            int compare = data.compareTo(node.data);
            if (compare < 0) node = node.left;
            else if (compare > 0) node = node.right;
            else return node.data;
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
        GeneralNode node = getRoots().get(data.getLastVersion()).left;
        GeneralNode prev = node;
        while (node != null) {
            prev = node;
            int compare = data.compareTo(prev.data);
            if (node.data.getKeyStart() <= data.getKeyStart() && node.data.getKeyEnd() > data.getKeyEnd())
                return node.data;
            if (compare < 0) node = prev.left;
            else if (compare > 0) node = prev.right;
        }
        return prev.data;
    }

    public boolean contains(int key, int version) {
        return getExact(key, version) != null;
    }


    private Tuple first(GeneralNode searchNode) {
        GeneralNode node = searchNode;
        while (node.left != null) node = node.left;
        return node.data;
    }

    @Override
    public String toString() {
        return "Treap{" +
                "ArrayList root=" + getRoots() +
                '}';
    }

    static class Node extends GeneralNode {
        Node(Tuple data) {
            super(data);
        }

        Node(GeneralNode input) {
            if(input.left != null) {
                this.left = new Node(input.left);
            }
            if(input.right != null) {
                this.right = new Node(input.right);
            }
            this.priority = input.priority;
            this.data = input.data;
        }

        Node() {
        }

        GeneralNode getLeft() {
            return getLeftInternal();
        }

        GeneralNode getRight() {
            return getRightInternal();
        }
    }
}
