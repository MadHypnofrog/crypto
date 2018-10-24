package utils;

import java.util.Random;

abstract class GeneralNode {
    private static final Random rand = new Random();
    GeneralNode right, left;
    int priority = rand.nextInt();
    Tuple data;

    GeneralNode(Tuple data) {
        this.data = data;
    }

    GeneralNode() {
    }

    @Override
    public String toString() {
        return "Node{" +
                "\n    item=" + data +
                "\n    priority=" + priority +
                "\n    left=" + left +
                "\n    right=" + right +
                '}';
    }

    GeneralNode getLeftInternal() {
        return left;
    }

    GeneralNode getRightInternal() {
        return right;
    }

    abstract GeneralNode getLeft();

    abstract GeneralNode getRight();

    Tuple getData() {
        return data;
    }
}
