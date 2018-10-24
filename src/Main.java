import sun.jvm.hotspot.utilities.Assert;
import utils.IllegalCertificateException;
import utils.Server;
import utils.User;

import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

public class Main {

    public static void main(String[] args) throws Exception {
        testAdd();
        System.out.println("Add test passed!");
        testAddAndRemove();
        System.out.println("Add and remove test passed!");
        for (int i = 1000; i < 6000; i += 1000) {
            System.out.println("Running tests for " + i + " iterations:");
            Date d = new Date();
            testAddOnTreap(i);
            System.out.println("Time elapsed on treap: " + (new Date().getTime() - d.getTime()) + "ms");
            d = new Date();
            testAddOnCopiedTreap(i);
            System.out.println("Time elapsed on copied treap: " + (new Date().getTime() - d.getTime()) + "ms");
        }
    }

    static void testAdd() throws IllegalCertificateException {
        Server.clearCopyTreap();
        Server.clearTreap();
        Random rand = new Random();
        int currentVersion = 0;
        User user = new User();

        ArrayList<Integer> values = new ArrayList<>();

        for (int i = 0; i < 2000; i++) {
            if (Math.random() > 0.7) {
                currentVersion++;
                int val = rand.nextInt();
                values.add(val);
                String content = "key: " + val;
                user.insert(Server.getTreap(), val, content);
                user.insert(Server.getCopyTreap(), val, content);
            } else {
                if (Math.random() > 0.5 && values.size() != 0) {
                    int val = values.get((int) (Math.random() * values.size()));
                    int version = (int) (Math.random() * currentVersion);
                    String resTreap = user.query(Server.getTreap(), val, version);
                    String copiedTreap = user.query(Server.getCopyTreap(), val, version);
                    Assert.that((resTreap == null && copiedTreap == null) || resTreap.equals(copiedTreap), "Strings are not equal!");
                } else {
                    int val = rand.nextInt();
                    int version = (int) (Math.random() * currentVersion);
                    String resTreap = user.query(Server.getTreap(), val, version);
                    String copiedTreap = user.query(Server.getCopyTreap(), val, version);
                    Assert.that((resTreap == null && copiedTreap == null) || resTreap.equals(copiedTreap), "Strings are not equal!");
                }
            }
        }
    }

    static void testAddAndRemove() throws IllegalCertificateException {
        Server.clearCopyTreap();
        Server.clearTreap();
        Random rand = new Random();
        int currentVersion = 0;
        User user = new User();

        ArrayList<Integer> values = new ArrayList<>();

        for (int i = 0; i < 2000; i++) {
            double r = Math.random();
            if (r < 0.5) {
                currentVersion++;
                int val = rand.nextInt();
                values.add(val);
                String content = "key: " + val;
                user.insert(Server.getTreap(), val, content);
                user.insert(Server.getCopyTreap(), val, content);
            } else if (r > 0.7) {
                if (Math.random() > 0.5 && values.size() != 0) {
                    int val = values.get((int) (Math.random() * values.size()));
                    int version = (int) (Math.random() * currentVersion);
                    String resTreap = user.query(Server.getTreap(), val, version);
                    String copiedTreap = user.query(Server.getCopyTreap(), val, version);
                    Assert.that((resTreap == null && copiedTreap == null) || resTreap.equals(copiedTreap), "Strings are not equal!");
                } else {
                    int val = rand.nextInt();
                    int version = (int) (Math.random() * currentVersion);
                    String resTreap = user.query(Server.getTreap(), val, version);
                    String copiedTreap = user.query(Server.getCopyTreap(), val, version);
                    Assert.that((resTreap == null && copiedTreap == null) || resTreap.equals(copiedTreap), "Strings are not equal!");
                }
            } else {
                if (Math.random() > 0.5 && values.size() != 0) {
                    int val = values.get((int) (Math.random() * values.size()));
                    user.remove(Server.getCopyTreap(), val);
                    user.remove(Server.getTreap(), val);
                } else {
                    int val = rand.nextInt();
                    user.remove(Server.getCopyTreap(), val);
                    user.remove(Server.getTreap(), val);
                }
            }
        }
    }

    static void testAddOnTreap(int n) throws IllegalCertificateException {
        Server.clearTreap();
        Random rand = new Random();
        int currentVersion = 0;
        User user = new User();

        ArrayList<Integer> values = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            currentVersion++;
            int val = rand.nextInt();
            values.add(val);
            String content = "key: " + val;
            user.insert(Server.getTreap(), val, content);
        }
        for (int i = 0; i < n; i++) {
            double r = Math.random();
            if (r > 0.7) {
                if (Math.random() > 0.5 && values.size() != 0) {
                    int val = values.get((int) (Math.random() * values.size()));
                    int version = (int) (Math.random() * currentVersion);
                    user.query(Server.getTreap(), val, version);
                } else {
                    int val = rand.nextInt();
                    int version = (int) (Math.random() * currentVersion);
                    user.query(Server.getTreap(), val, version);
                }
            }
        }
    }

    static void testAddOnCopiedTreap(int n) throws IllegalCertificateException {
        Server.clearCopyTreap();
        Random rand = new Random();
        int currentVersion = 0;
        User user = new User();

        ArrayList<Integer> values = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            currentVersion++;
            int val = rand.nextInt();
            values.add(val);
            String content = "key: " + val;
            user.insert(Server.getCopyTreap(), val, content);
        }
        for (int i = 0; i < n; i++) {
            double r = Math.random();
            if (r > 0.7) {
                if (Math.random() > 0.5 && values.size() != 0) {
                    int val = values.get((int) (Math.random() * values.size()));
                    int version = (int) (Math.random() * currentVersion);
                    user.query(Server.getCopyTreap(), val, version);
                } else {
                    int val = rand.nextInt();
                    int version = (int) (Math.random() * currentVersion);
                    user.query(Server.getCopyTreap(), val, version);
                }
            }
        }
    }

}
