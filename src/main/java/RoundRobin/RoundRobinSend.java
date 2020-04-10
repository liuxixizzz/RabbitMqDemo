package RoundRobin;

import Units.RabbitMqConnectionFactory;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;

public class RoundRobinSend {
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
            //1.   2.队列名称  3. 是否持久化消息
            channel.basicPublish("", QUEUE_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN, msg.getBytes());
            System.out.println(msg);
        }
        //4 记得要关闭相关的连接
        channel.close();
        connection.close();
    }
}
