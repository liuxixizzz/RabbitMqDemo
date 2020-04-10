package HolleWorld;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class send {
    private static final String QUEUE_NAME = "Test_SimpleQuery";

    public static void main(String[] args) throws Exception {
        //1 创建一个ConnectionFactory, 并进行配置
        ConnectionFactory connectionFactory = new ConnectionFactory();
        //服务器的IP地址
        connectionFactory.setHost("localhost");
        //端口
        connectionFactory.setPort(5672);
        //指定HOST
        connectionFactory.setVirtualHost("admin_vhosts");
        //登录名
        connectionFactory.setUsername("admin");
        //密码
        connectionFactory.setPassword("123");

        //2.通过连接工厂创建连接
        Connection connection = connectionFactory.newConnection();

        //3 通过connection创建一个Channel
        Channel channel = connection.createChannel();

        //4.申明队列
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);

        //5 通过Channel发送数据
        for (int i = 0; i < 5; i++) {
            String msg = "Hello RabbitMQ!" + i;
            Thread.sleep(1000);
            channel.basicPublish("", QUEUE_NAME, null, msg.getBytes());
            System.out.println(msg);
        }
        //6 记得要关闭相关的连接
        channel.close();
        connection.close();
    }
}
