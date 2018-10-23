package utils;

import java.util.ArrayList;
import java.util.Random;

public class Treap {

    private static final Random rand = new Random();
    private ArrayList<Node> roots = new ArrayList<>();
    private int version = 0;

    Treap() {
        Node first = new Node();
        first.left = new Node(new Tuple(0, Integer.MIN_VALUE, Integer.MAX_VALUE, ""));
        roots.add(first);
        CertificateAuthor.signTreap(this);
    }

    void incrementVersion() {
        version++;
    }

    int getVersion() {
        return version;
    }

    public Node getRoot(int num) {
        return roots.get(num);
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
            if (node.priority > newLeft.priority) {
                return rotateRight(node, newLeft);
            } else {
                return assign(node, newLeft, "left");
            }
        } else if (compare > 0) {
            Node oldRight = node.getRight();
            Node newRight = add(oldRight, data);
            if (node.priority > newRight.priority) {
                return rotateLeft(node, newRight);
            } else {
                return assign(node, newRight, "right");
            }
        }
        return node;
    }

    public Node assign(Node current, Node child, String s) {
//        System.out.println("CURRENT: " + current);
//        System.out.println("CHILD: " + child);
        Node.Modification modCurrent = current.box;
        //if(modCurrent.singleSnapshot) {}
        if (modCurrent != null) {
            if ((s.equals(modCurrent.getModificator()) && modCurrent.content == child) ||
                    (((s.equals("left") && current.left == child) ||
                            (s.equals("right") && current.right == child)) && !s.equals(modCurrent.getModificator()))) {
                return current;
            } else {
//                System.out.println("KEK!");
                Node newCurrent = new Node(current);
                newCurrent.evaluate(modCurrent, modCurrent.getModificator());
//                System.out.println("NEW CURRENT: " + newCurrent);
                newCurrent.evaluate(new Node.Modification(child, s, version), s);
//                System.out.println("NEW CURRENT: " + newCurrent);
                return newCurrent;
            }
        }
        if ((s.equals("left") && current.left == child) || (s.equals("right") && current.right == child)) {
            return current;
        } else {
            current.box = new Node.Modification(child, s, version);
            return current;
        }
    }

    public Node assign(Tuple data, Node current) {
        Node.Modification modCurrent = current.box;
        if (modCurrent != null) {
            return new Node(current, data);
        }
        current.box = new Node.Modification(data, "data", getVersion());
        return current;
    }

    private Node rotateRight(Node node, Node lnode) {
        node = assign(node, lnode.getRight(), "left");
        lnode = assign(lnode, node, "right");
        return lnode;
    }

    private Node rotateLeft(Node node, Node rnode) {
        node = assign(node, rnode.getLeft(), "right");
        rnode = assign(rnode, node, "left");
        return rnode;
    }

    public boolean remove(Tuple data) {
        incrementVersion();
        Node fakeNode = new Node();
        Node oldNode = roots.get(roots.size() - 1);
        if (oldNode.left == null) {
            return false;
        }
        fakeNode.left = remove(oldNode.left, data);
//        System.out.println("FAKE: " + fakeNode.left);
        roots.add(fakeNode);
        return fakeNode.left != null;
    }

    private Node remove(Node node, Tuple data) {
        if (node != null) {
            int compare = data.compareTo(node.getData());
            if (compare < 0) {
                node = assign(node, remove(node.getLeft(), data), "left");
            } else if (compare > 0) {
                node = assign(node, remove(node.getRight(), data), "right");
            } else {
                if (node.getLeft() == null) {
//                    System.out.println("FOUND!! LEFT IS NULL");
                    return node.getRight();
                } else if (node.getRight() == null) {
//                    System.out.println("FOUND!! RIGHT IS NULL");
                    return node.getLeft();
                } else {
                    Node oldNode = node;
                    node = assign(first(node.getRight()), node);
//                    System.out.println("!!!!!VERSION " + (getVersion() - 1) + " \n" + getRoot(getVersion() - 1));
                    if(oldNode != node) {
                        node.right = remove(node.right, node.getData());
                    } else {
                        node = assign(node, remove(node.right, node.getData()), "right");
                    }
                }
            }
        }
        return node;
    }

    /*public Tuple first() {
        return first(roots.getModificator(roots.size() - 1));
    }*/

    private Tuple first(Node searchNode) {
        Node node = searchNode;
        while (node.getLeft() != null) node = node.getLeft();
        return node.getData();
    }

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
        Node node = roots.get(data.getLastVersion()).getLeft();
        Node prev = node;
        while (node != null) {
            prev = node;
            Node.Modification box = node.box;
            Node copy = node.copy();
            if (box != null && box.timestamp <= data.getLastVersion()) {
//                System.out.println("EVALUATED!");
                copy.evaluate(box, box.getModificator());
            }
            int compare = data.compareTo(copy.getData());
            if (compare < 0) node = copy.getLeft();
            else if (compare > 0) node = copy.getRight();
        }
        return prev.getData();
    }

    public boolean contains(int key, int version) {
        return getExact(key, version) != null;
    }

    @Override
    public String toString() {
        return "Treap{" +
                "root ArrayList=" + roots +
                '}';
    }


     static class Node {
        Node right, left;
        int priority = rand.nextInt();
        Tuple data;

        Modification box = null;

        private static class Modification {
            int timestamp;
            Node content;
            Tuple data;
            boolean leftModified, rightModified;
            boolean dataModified;
            boolean singleSnapshot;

            Modification(Node content, boolean leftModified, boolean rightModified, int version) {
                this.content = content;
                this.leftModified = leftModified;
                this.rightModified = rightModified;
                timestamp = version;
            }

            Modification(Node content, String s, int version) {
                this.content = content;
                setModificator(s);
                timestamp = version;
            }

            Modification(Tuple data, String s, int version) {
                this.data = data;
                setModificator(s);
                timestamp = version;
            }
            public void setSingle(boolean flag) {
                singleSnapshot = flag;
            }

            public void setData(Tuple data) {
                this.data = data;
                dataModified = true;
            }

            public void setModificator(String s) {
                if (s.equals("left")) {
                    leftModified = true;
                } else if (s.equals("right")) {
                    rightModified = true;
                } else {
                    dataModified = true;
                }
            }

            public String getModificator() {
                if (leftModified) {
                    return "left";
                } else if (rightModified) {
                    return "right";
                } else {
                    return "data";
                }
            }

            public String toString() {
                return "Modification{" +
                        "data=" + data +
                        ", content=" + content +
                        ", modificator=" + getModificator() +
                        ", timestamp=" + timestamp +
                        '}';
            }
        }

        public Node copy() {
            return new Node(this);
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
            right = input.getRight();
            left = input.getLeft();
            priority = input.priority;
            this.data = data;
        }

        public Tuple getData() {
            if (box != null) {
                if (box.getModificator().equals("data")) {
                    return box.data;
                }
            }
            return data;
        }

        public Node getLeft() {
            if (box != null) {
                if (box.getModificator().equals("left")) {
                    return box.content;
                }
            }
            return left;
        }

        public Node getRight() {
            if (box != null) {
                if (box.getModificator().equals("right")) {
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
                    "\n    item=" + data +
                    "\n    priority=" + priority +
                    "\n    left=" + left +
                    "\n    right=" + right +
                    "\n    modification=" + box +
                    '}';
        }
    }
}