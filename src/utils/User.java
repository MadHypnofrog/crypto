package utils;

import java.util.Arrays;

public class User {

    public boolean checkSign(Tuple tuple) throws IllegalCertificateException {
//        System.out.println(tuple);
        int curDate = tuple.getCertificate().getDate() / 3;
        int certDate = tuple.getLastVersion();
        byte[] hashCert = tuple.getCertificate().getCertHash();

        if (Arrays.equals(tuple.getCertificate().getPublicKeyNo(), (hashCert = CertificateAuthor.digester.digest(hashCert)))) {
            return false;
        }

        for (int i = 0; i < certDate - curDate; i++) {
            hashCert = CertificateAuthor.digester.digest(hashCert);
        }

        if (Arrays.equals(tuple.getCertificate().getPublicKeyYes(), hashCert)) {
            return true;
        }
        throw new IllegalCertificateException("The server provided an illegal certificate");
    }

    public String query(GeneralTreap treap, int key, int version) throws IllegalCertificateException {
        Tuple res = Server.get(treap, key, version);
        if (res != null && res.getFirstVersion() <= version && res.getLastVersion() >= version
                && res.getKeyStart() <= key && res.getKeyEnd() > key) {
            if (checkSign(res)) return res.getContent();
        }
        return null;
    }

    public void insert(GeneralTreap treap, int key, String value) {
        Server.insert(treap, key, value);
    }

    public void remove(GeneralTreap treap, int key) {
        Server.remove(treap, key);
    }

}
