package utils;

public class Certificate {
    private byte[] certHash;
    private byte[] publicKeyYes;
    private byte[] publicKeyNo;
    private int id, date;

    Certificate(byte[] publicKeyYes, byte[] publicKeyNo, int id, int date) {
        this.publicKeyYes = publicKeyYes;
        this.publicKeyNo = publicKeyNo;
        this.id = id;
        this.date = date;
    }

    void set(byte[] data) {
        this.certHash = data;
    }

    int getDate() {
        return date;
    }

    int getId() {
        return id;
    }

    byte[] getCertHash() {
        return certHash;
    }

    byte[] getPublicKeyYes() {
        return publicKeyYes;
    }

    byte[] getPublicKeyNo() {
        return publicKeyNo;
    }

    Certificate copy() {
        Certificate newCert = new Certificate(this.publicKeyYes, this.publicKeyNo, this.id, this.date);
        newCert.certHash = this.certHash;
        return newCert;
    }
}
