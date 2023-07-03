import org.quanta.im.client.IMClient;

/**
 * Description:
 * Param:
 * return:
 * Author: wzf
 * Date: 2023/7/2
 */
public class TestClient2 {
    public static void main(String[] args) {
        IMClient client = new IMClient("127.0.0.1",8080);
        client.login("wzf2");
        client.sendMessage("hello");
    }
}
