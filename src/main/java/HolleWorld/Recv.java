package HolleWorld;

import com.rabbitmq.client.*;

import java.io.IOException;

public class Recv {
    private static final String QUEUE_NAME = "Test_SimpleQuery";
    public static void main(String[] args) throws Exception{
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
        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");
        DeliverCallback deliverCallback = (s, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println(" [x] Received '" + message + "'");
        };
        channel.basicConsume(QUEUE_NAME, true, deliverCallback, consumerTag -> { });
    }
}
