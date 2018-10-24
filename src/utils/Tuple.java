package utils;

import java.util.Arrays;

public class Tuple implements Comparable<Tuple> {
    private int vStart, vEnd;
    private int keyStart, keyEnd;
    private String content;
    private Certificate sign = null;

    Tuple(int vStart, int keyStart, int keyEnd, String content) {
        this.vStart = vStart;
        this.vEnd = vStart;
        this.keyStart = keyStart;
        this.keyEnd = keyEnd;
        this.content = content;
    }

    Tuple(Tuple another) {
        if (another != null) {
            this.vStart = another.vStart;
            this.vEnd = another.vEnd;
            this.keyStart = another.keyStart;
            this.keyEnd = another.keyEnd;
            this.content = another.content;
            this.sign = another.sign;
        }
    }

    void update(Certificate sign) throws IllegalCertificateException {
        byte[] input = sign.getCertHash();
        if (this.sign == null) {
            if (!Arrays.equals(sign.getPublicKeyYes(), CertificateAuthor.digester.digest(input))) {
                throw new IllegalCertificateException("The author did not provide a valid certificate: " + sign);
            }
        } else {
            if (!Arrays.equals(sign.getPublicKeyYes(), CertificateAuthor.digester.digest(input))
                    && !Arrays.equals(this.sign.getCertHash(), CertificateAuthor.digester.digest(input))
                    && !Arrays.equals(this.sign.getPublicKeyNo(), CertificateAuthor.digester.digest(input))) {
                throw new IllegalCertificateException("The author did not provide a valid certificate: " + sign);
            }
        }
        this.sign = sign;
    }

    Certificate getCertificate() {
        return sign;
    }

    String getContent() {
        return content;
    }

    int getKeyStart() {
        return keyStart;
    }

    int getKeyEnd() {
        return keyEnd;
    }

    int getFirstVersion() {
        return vStart;
    }

    int getLastVersion() {
        return vEnd;
    }

    void setFirstVersion(int n) {
        vStart = n;
    }

    void setLastVersion(int n) {
        vEnd = n;
    }

    @Override
    public int compareTo(Tuple o) {
        return Integer.compare(this.keyStart, o.keyStart);
    }

    public String toString() {
        return "Tuple{" +
                "keyStart=" + keyStart +
                ", keyEnd=" + keyEnd +
                ", vStart=" + vStart +
                ", vEnd=" + vEnd +
                ", content=" + content +
                '}';
    }
}
