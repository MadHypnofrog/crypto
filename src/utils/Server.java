package utils;

public class Server {

    private static Treap treap;

    public static void insert(Tuple data) {
        treap.incrementVersion();
        treap.add(data);
        CertificateAuthor.signTreap(treap);
    }
    public static void remove(Tuple data) {
        treap.incrementVersion();
        treap.remove(data);
        CertificateAuthor.signTreap(treap);
    }
    public static int getDate() {
        return treap.getVersion();
    }

}
