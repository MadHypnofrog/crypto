package utils;

public class Certificate {
    byte[] certHash;
    byte[] publicKeyYes;
    byte[] publicKeyNo;
    int id, date;

    Certificate(byte[] publicKeyYes, byte[] publicKeyNo, int id, int date) {
        this.publicKeyYes = publicKeyYes;
        this.publicKeyNo = publicKeyNo;
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
