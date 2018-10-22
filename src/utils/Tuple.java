package utils;

import java.util.Arrays;

class Tuple implements Comparable<Tuple>{
    int vStart, vEnd;
    int keyStart, keyEnd;
    String content;
    Certificate sign;

    Tuple(int vStart, int keyStart, int keyEnd, String content) {
        this.vStart = vStart;
        this.keyStart = keyStart;
        this.keyEnd = keyEnd;
        this.content = content;
    }

    void update(Certificate sign) throws IllegalCertificateException {
        byte[] input = sign.getCertHash();
        if (!Arrays.equals(this.sign.getCertHash(), CertificateAuthor.digester.digest(input))) {
            throw new IllegalCertificateException("The author did not provide a valid certificate: " + sign);
        }
        this.sign = sign;
    }
    public Certificate getCertificate() {
        return sign;
    }
    public int getLastVersion() {
        return vEnd;
    }

    void incrementVersion() {
        vEnd++;
    }

    @Override
    public int compareTo(Tuple o) {
        return Integer.compare(this.keyStart, o.keyStart);
    }
}
