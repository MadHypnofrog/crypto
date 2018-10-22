package utils;

import com.sun.org.apache.xpath.internal.operations.Mod;
import utils.Tuple;

import java.util.ArrayList;
import java.util.Random;

public class Treap {

    private static final Random rand = new Random();
    private ArrayList<Node> roots = new ArrayList<>();
    private int version = 0;

    Treap() {
        roots.add(new Node());
    }

    void incrementVersion() {
        version++;
    }

    int getVersion() {
        return version;
    }

    public void add(Tuple data) {
        incrementVersion();
        Node fakeNode = new Node();
        Node oldRoot = roots.get(roots.size() - 1).left;
        roots.add(fakeNode);
        fakeNode.left = add(oldRoot, data);
    }

    private Node add(Node node, Tuple data) {
        if (node == null)
            return new Node(data);
        int compare = data.compareTo(node.getData());

        if (compare < 0) {
            Node oldLeft = node.getLeft();
            Node newLeft = add(oldLeft, data);
            Node rotated = null;
            if (node.priority > node.getLeft().priority) {
                return rotateRight(node);
            } else {
                return assign(node, newLeft, "left");
            }
        } else if (compare > 0) {
            Node oldRight = node.getRight();
            Node newRight = add(oldRight, data);
            Node rotated = null;
            node.right = add(node.getRight(), data);
            if (node.priority > node.getRight().priority) {
                return rotateLeft(node);
            } else {
                return assign(node, newRight, "right");
            }
        }
        return node;
    }

    public Node assign(Node current, Node child, String s) {
        Node.Modification modCurrent = current.box;
        if (modCurrent != null) {
            if ((s.equals(modCurrent.get()) && modCurrent.content == child) ||
                    (((s.equals("left") && current.left == child) ||
                            (s.equals("right") && current.right == child)) && !s.equals(modCurrent.get()))) {
                return current;
            } else {
                Node newCurrent = new Node(current);
                newCurrent.evaluate(modCurrent, modCurrent.get());
                newCurrent.evaluate(new Node.Modification(child, s), s);
                return newCurrent;
            }
        }
        if ((s.equals("left") && current.left == child) || (s.equals("right") && current.right == child)) {
            return current;
        } else {
            current.box = new Node.Modification(child, s);
            return current;
        }
    }
    public Node assign(Tuple data, Node current) {
        Node.Modification modCurrent = current.box;
        if(modCurrent != null) {
            return new Node(current, data);
        }
        current.evaluate(new Node.Modification(data, "data"), "data");
        return current;
    }

    private Node rotateRight(Node node) {
        Node lnode = node.getLeft();
        node = assign(node, lnode.getRight(), "left");
        lnode = assign(lnode, node, "right");
        return lnode;
    }

    private Node rotateLeft(Node node) {
        Node rnode = node.getRight();
        node = assign(node, rnode.getLeft(), "right");
        rnode = assign(rnode, node, "left");
        return rnode;
    }

    public boolean remove(Tuple data) {
        incrementVersion();
        Node fakeNode = new Node();
        Node oldNode = roots.get(roots.size() - 1);
        if(oldNode.left == null) {
            return false;
        }
        roots.add(fakeNode);
        fakeNode.left = remove(oldNode, data);
        return fakeNode.left != null;
    }

    private Node remove(Node node, Tuple data) {
        if (node != null) {
            int compare = data.compareTo(node.getData());
            if (compare < 0) {
                node.left = assign(node, remove(node.getLeft(), data), "left");
            } else if (compare > 0) {
                node.right = assign(node, remove(node.getRight(), data), "right");
            } else {
                if (node.getLeft() == null) {
                    return node.getRight();
                } else if (node.getRight() == null) {
                    return node.getLeft();
                } else {
                    node = assign(first(node.getRight()), node);
                    node = assign(node, remove(node.right, node.getData()), "right");
                }
            }
        }
        return node;
    }

    /*public Tuple first() {
        return first(roots.get(roots.size() - 1));
    }*/

    private Tuple first(Node searchNode) {
        Node node = searchNode;
        while (node.getLeft() != null) node = node.getLeft();
        return node.getData();
    }

    public Tuple contains(int key, int version) {
        return contains(new Tuple(version, key,key, ""));
    }

    private Tuple contains(Tuple data) {
        Node node = roots.get(data.getLastVersion());
        while (node != null) {
            int compare = data.compareTo(node.getData());
            if (compare < 0) node = node.getLeft();
            else if (compare > 0) node = node.getRight();
            else return node.data;
        }
        return null;
    }

    @Override
    public String toString() {
        return "utils.Treap{" +
                "root ArrayList=" + roots +
                '}';
    }


    private static class Node {
        public Node right, left;
        public int priority = rand.nextInt();
        public Tuple data;

        Modification box = null;

        private static class Modification {
            int timestamp = 0;
            Node content;
            Tuple data;
            boolean leftModified, rightModified;
            boolean dataModified;

            Modification(Node content, boolean leftModified, boolean rightModified) {
                this.content = content;
                this.leftModified = leftModified;
                this.rightModified = rightModified;
            }

            Modification(Node content, String s) {
                this.content = content;
                set(s);
            }
            Modification(Tuple data, String s) {
                this.data = data;
                set(s);
            }

            public void setData(Tuple data) {
                this.data = data;
                dataModified = true;
            }

            public void set(String s) {
                if (s.equals("left")) {
                    leftModified = true;
                } else if(s.equals("right")){
                    rightModified = true;
                } else {
                    dataModified = true;
                }
            }

            public String get() {
                if (leftModified) {
                    return "left";
                } else if (rightModified) {
                    return "right";
                } else {
                    return "data";
                }
            }
        }

        public Node() {
        }

        public Node(Tuple data) {
            this.data = data;
        }

        public Node(Node input) {
            right = input.right;
            left = input.left;
            priority = input.priority;
            data = input.data;
        }
        public Node(Node input, Tuple data) {
            right = input.right;
            left = input.left;
            priority = input.priority;
            this.data = data;
        }

        public Tuple getData() {
            if(box != null) {
                if(box.get().equals("data")) {
                    return box.data;
                }
            }
            return data;
        }
        public Node getLeft() {
            if (box != null) {
                if (box.get().equals("left")) {
                    return box.content;
                }
            }
            return left;
        }

        public Node getRight() {
            if (box != null) {
                if (box.get().equals("right")) {
                    return box.content;
                }
            }
            return right;
        }

        public void evaluate(Modification nodeMod, String s) {
            if (s.equals("left")) {
                left = nodeMod.content;
            } else if (s.equals("right")) {
                right = nodeMod.content;
            } else {
                data = nodeMod.data;
            }
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