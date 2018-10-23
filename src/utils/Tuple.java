package utils;

import java.util.Arrays;

class Tuple implements Comparable<Tuple> {
    int vStart, vEnd;
    int keyStart, keyEnd;
    String content;
    Certificate sign = null;

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
            this.vEnd = another.vStart;
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
        } else if (!Arrays.equals(this.sign.getCertHash(), CertificateAuthor.digester.digest(input))
                && !Arrays.equals(this.sign.getPublicKeyNo(), CertificateAuthor.digester.digest(input))) {
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
