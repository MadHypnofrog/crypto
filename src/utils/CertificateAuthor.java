package utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Random;

public class CertificateAuthor {
    public static MessageDigest digester;
    public static final int VALIDITY = 1337;
    public static HashMap<Integer, byte[]> yesKeys = new HashMap<>();
    public static HashMap<Integer, byte[]> noKeys = new HashMap<>();

    static {
        try {
            digester = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    static void signTreap(Treap treap) {
        Treap.Node root = treap.getRoot(treap.getVersion()).getLeft();
        signTreapRecursive(root, treap.getVersion());
    }

    static void signTreapRecursive(Treap.Node node, int version) {
        Tuple data = node.getData();
        if (data.getCertificate() == null) {
            int id = yesKeys.size();
            yesKeys.put(id, generate(32));
            noKeys.put(id, generate(32));
            Certificate cert = new Certificate(hashNTimes(yesKeys.get(id), VALIDITY), hashNTimes(noKeys.get(id), 1), id, version);
            cert.set(hashNTimes(yesKeys.get(id), VALIDITY - 1));
            try {
                data.update(cert);
            } catch (Exception ignored) {
            }
        } else {
            data.vEnd += 3;
            Certificate cert = data.getCertificate();
            int passed = (version - cert.getDate()) / 3;
            int id = data.getCertificate().getId();
            if (passed != VALIDITY - 1) {
                cert.set(hashNTimes(yesKeys.get(id), VALIDITY - passed - 1));
            } else {
                id = yesKeys.size();
                yesKeys.put(id, generate(32));
                noKeys.put(id, generate(32));
                cert = new Certificate(hashNTimes(yesKeys.get(id), VALIDITY), hashNTimes(noKeys.get(id), 1), id, version);
                cert.set(hashNTimes(yesKeys.get(id), VALIDITY - 1));
            }
            try {
                data.update(cert);
            } catch (Exception ignored) {
            }
        }
        if (node.getLeft() != null) signTreapRecursive(node.getLeft(), version);
        if (node.getRight() != null) signTreapRecursive(node.getRight(), version);
    }

    static byte[] generate(int n) {
        byte[] ans = new byte[n];
        new Random().nextBytes(ans);
        return ans;
    }

    static byte[] hashNTimes(byte[] data, int n) {
        for (int i = 0; i < n; i++) data = digester.digest(data);
        return data;
    }

}
