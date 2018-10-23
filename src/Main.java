import utils.Server;
import utils.User;

public class Main {

    public static void main(String[] args) throws Exception {
        User user1 = new User();
        user1.insert(3, "test");
        System.out.println(user1.query(3, 0));
        System.out.println(user1.query(3, 1));
        user1.insert(7, "test2");
        user1.remove(3);
        System.out.println(Server.treap.getRoot(6));
        System.out.println(user1.query(3, 2));
    }

}
