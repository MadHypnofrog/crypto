package utils;

public class Server {

    public static Treap treap = new Treap();

    public static void insert(int key, String value) {
        if (key == Integer.MAX_VALUE || key == Integer.MIN_VALUE) {
            throw new IllegalArgumentException("The client provided an incorrect key");
        }
        int version = treap.getVersion();
        Tuple oldTuple = treap.getLower(key, version / 3);
        treap.remove(oldTuple);
//        System.out.println("VERSION" + (version + 1) + ":\n" + treap.getRoot(version + 1));
        Tuple first = new Tuple(version + 3, oldTuple.keyStart, key, oldTuple.content);
        Tuple second = new Tuple(version + 3, key, oldTuple.keyEnd, value);
        treap.add(first);
//        System.out.println("VERSION" + (version + 2) + ":\n" + treap.getRoot(version + 2));
        treap.add(second);
//        System.out.println("VERSION" + (version + 3) + ":\n" + treap.getRoot(version + 3));
        CertificateAuthor.signTreap(treap);
    }
    public static void remove(int key) {
        if (key == Integer.MAX_VALUE || key == Integer.MIN_VALUE) {
            throw new IllegalArgumentException("The client provided an incorrect key");
        }
        if (!treap.contains(key, treap.getVersion() / 3)) return;
        int version = treap.getVersion();
//        System.out.println(version);
        Tuple oldTuple = treap.getExact(key, version / 3);
//        System.out.println("A");
//        System.out.println(oldTuple);
        treap.remove(oldTuple);
//        System.out.println("VERSION" + (version) + ":\n" + treap.getRoot(version));
//        System.out.println("VERSION" + (version + 1) + ":\n" + treap.getRoot(version + 1));
        Tuple lower = treap.getLower(key - 1, version / 3, 1);
//        System.out.println(lower);
//        System.out.println("B");
        treap.remove(lower);
//        System.out.println("VERSION" + (version + 2) + ":\n" + treap.getRoot(version + 2));
//        System.out.println("C");
        treap.add(new Tuple(version + 3, lower.keyStart, oldTuple.keyEnd, lower.content));
//        System.out.println("VERSION" + (version + 3) + ":\n" + treap.getRoot(version + 3));
//        System.out.println("D");
        CertificateAuthor.signTreap(treap);
    }
    public static int getDate() {
        return treap.getVersion();
    }

    public static Tuple get(int key, int version) {
        Tuple res = treap.getExact(key, version);
        Tuple newTuple = new Tuple(res);
        if (newTuple != null) {
            newTuple.vStart /= 3;
            newTuple.vEnd /= 3;
        }
        return newTuple;
    }

}
