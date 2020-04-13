package PublishSubscribe;

import Units.RabbitMqConnectionFactory;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.client.Delivery;

import java.io.IOException;

public class PsRecv2 {
    private static final String EXCHANGE_NAME = "Test_PSExchange";

    //private static final String QUEUE_NAME = "Test_PSQueryTwo";
    public static void main(String[] args) throws Exception {
        Connection connection = RabbitMqConnectionFactory.geyConnection();
        //1 通过connection创建一个Channel
        Channel channel = connection.createChannel();
        //3.申明交换机
        channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
        /*//2.申明队列        1.队列名  2.持久性(durability)
        channel.queueDeclare(QUEUE_NAME, true, false, false, null);*/
        //3.获取随机队列名称
        String queueName = channel.queueDeclare().getQueue();
        //4.绑定队列
        channel.queueBind(queueName, EXCHANGE_NAME, "");
        channel.basicQos(1);
        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");
        DeliverCallback deliverCallback = (s, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
            System.out.println(" [x] Received '" + message + "'");
        };
        boolean autoAck = false;
        channel.basicConsume(queueName, autoAck, deliverCallback, consumerTag -> {
        });
    }
}
