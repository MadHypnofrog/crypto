package utils;

public class Server {

    private static Treap treap = new Treap();

    public static Treap getTreap() {
        return treap;
    }

    public static void clearTreap() {
        treap = new Treap();
    }

    private static CopyPersistentTreap copyTreap = new CopyPersistentTreap();

    public static CopyPersistentTreap getCopyTreap() {
        return copyTreap;
    }

    public static void clearCopyTreap() {
        copyTreap = new CopyPersistentTreap();
    }

    static void insert(GeneralTreap treap, int key, String value) {
        if (key == Integer.MAX_VALUE || key == Integer.MIN_VALUE) {
            throw new IllegalArgumentException("The client provided an incorrect key");
        }
        int version = treap.getVersion();
        Tuple oldTuple = treap.getLower(key, version / 3);
        treap.remove(oldTuple);
        Tuple first = new Tuple(version + 3, oldTuple.getKeyStart(), key, oldTuple.getContent());
        Tuple second = new Tuple(version + 3, key, oldTuple.getKeyEnd(), value);
        treap.add(first);
        treap.add(second);
        CertificateAuthor.signTreap(treap);
    }

    static void remove(GeneralTreap treap, int key) {
        if (key == Integer.MAX_VALUE || key == Integer.MIN_VALUE) {
            throw new IllegalArgumentException("The client provided an incorrect key");
        }
        if (!treap.contains(key, treap.getVersion() / 3)) return;
        int version = treap.getVersion();
        Tuple oldTuple = treap.getExact(key, version / 3);
        treap.remove(oldTuple);
        Tuple lower = treap.getLower(key - 1, version / 3, 1);
        treap.remove(lower);
        treap.add(new Tuple(version + 3, lower.getKeyStart(), oldTuple.getKeyEnd(), lower.getContent()));
        CertificateAuthor.signTreap(treap);
    }

    static Tuple get(GeneralTreap treap, int key, int version) {
        Tuple res = treap.getExact(key, version);
        Tuple newTuple = new Tuple(res);
        newTuple.setFirstVersion(newTuple.getFirstVersion() / 3);
        newTuple.setLastVersion(newTuple.getLastVersion() / 3);
        return newTuple;
    }

}
