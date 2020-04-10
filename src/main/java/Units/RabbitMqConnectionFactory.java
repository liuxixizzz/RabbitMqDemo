package Units;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class RabbitMqConnectionFactory {
    private static String host = "localhost";
    private static int port = 5672;
    private static String vhosts = "admin_vhosts";
    private static String username = "admin";
    private static String passwor = "123123";
    private static ConnectionFactory connectionFactory = new ConnectionFactory();

    public static Connection geyConnection() throws Exception{
        //服务器的IP地址
        connectionFactory.setHost(host);
        //端口
        connectionFactory.setPort(port);
        //指定HOST
        connectionFactory.setVirtualHost(vhosts);
        //登录名
        connectionFactory.setUsername(username);
        //密码
        connectionFactory.setPassword(passwor);
        //2.通过连接工厂创建连接
        return connectionFactory.newConnection();
    }
}
