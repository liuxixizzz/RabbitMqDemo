package FailQueue;

import Units.RabbitMqConnectionFactory;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

public class send {
    private static final String QUEUE_NAME = "Test_RoundRobinQuery";

    public static void main(String[] args) throws Exception {
        Connection connection = RabbitMqConnectionFactory.geyConnection();
        //1 通过connection创建一个Channel
        Channel channel = connection.createChannel();
        //2.申明队列
        channel.queueDeclare(QUEUE_NAME, true, false, false, null);
        //3 通过Channel发送数据
        for (int i = 0; i < 50; i++) {
            String msg = "RoundRobinQuery!  " + i;
            Thread.sleep(1000);
            channel.basicPublish("", QUEUE_NAME, null, msg.getBytes());
            System.out.println(msg);
        }
        //4 记得要关闭相关的连接
        channel.close();
        connection.close();
    }
}
