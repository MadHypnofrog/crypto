package utils;

import java.util.ArrayList;

abstract class GeneralTreap {

    private ArrayList<GeneralNode> roots = new ArrayList<>();
    private int version = 0;

    void incrementVersion() {
        version++;
    }

    int getVersion() {
        return version;
    }

    GeneralNode getRoot(int num) {
        return roots.get(num);
    }

    ArrayList<GeneralNode> getRoots() {
        return roots;
    }

    void setRoots(ArrayList<GeneralNode> roots) {
        this.roots = roots;
    }

    int getRootsSize() {
        return roots.size();
    }

    abstract Tuple getLower(int key, int version);

    abstract Tuple getLower(int key, int version, int add);

    abstract Tuple getExact(int key, int version);

    abstract void add(Tuple data);

    abstract void remove(Tuple data);

    abstract boolean contains(int key, int version);
}
