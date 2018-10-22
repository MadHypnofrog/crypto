package utils;

import java.util.Arrays;

public class User {

    private boolean checkSign(Tuple tuple) throws IllegalCertificateException {
        int curDate = Server.getDate();
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

    public boolean query(int key, int version) throws IllegalCertificateException {
        Tuple res = Server.get(key, version);
        if (res.vStart <= version && res.vEnd >= version && res.keyStart <= key && res.keyEnd > key) {
            return checkSign(res);
        }
        return false;
    }
}
