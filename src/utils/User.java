package utils;

import java.util.Arrays;

public class User {

    private boolean checkSign(Tuple tuple) throws IllegalCertificateException {
//        System.out.println(tuple);
        int curDate = tuple.getCertificate().date / 3;
        int certDate = tuple.getLastVersion();
        byte[] hashCert = tuple.getCertificate().getCertHash();

        if (Arrays.equals(tuple.getCertificate().getPublicKeyNo(), (hashCert = CertificateAuthor.digester.digest(hashCert)))) {
            return false;
        }

        for (int i = 0; i < curDate - certDate - 1; i++) {
            hashCert = CertificateAuthor.digester.digest(hashCert);
        }

        if (Arrays.equals(tuple.getCertificate().getPublicKeyYes(), hashCert)) {
            return true;
        }
        throw new IllegalCertificateException("The server provided an illegal certificate");
    }

    public String query(int key, int version) throws IllegalCertificateException {
        Tuple res = Server.get(key, version);
        if (res != null && res.vStart <= version && res.vEnd >= version && res.keyStart <= key && res.keyEnd > key) {
            if (checkSign(res)) return res.content;
        }
        return null;
    }

    public void insert(int key, String value) {
        Server.insert(key, value);
    }

    public void remove(int key) {
        Server.remove(key);
    }

}
