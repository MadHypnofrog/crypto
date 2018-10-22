package utils;

public class Certificate {
    byte[] certHash;
    byte[] publicKeyYes;
    byte[] publicKeyNo;
    int id, date;

    Certificate(byte[] PublicKeyYes, byte[] PublicKeyNo, int id, int date) {
        this.publicKeyYes = PublicKeyYes;
        this.publicKeyNo = PublicKeyNo;
        this.id = id;
        this.date = date;
    }

    public void set(byte[] data) {
        this.certHash = data;
    }

    public int getDate() {
        return date;
    }

    public int getId() {
        return id;
    }

    public byte[] getCertHash() {
        return certHash;
    }

    public byte[] getPublicKeyYes() {
        return publicKeyYes;
    }

    public byte[] getPublicKeyNo() {
        return publicKeyNo;
    }
}
