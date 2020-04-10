package RoundRobin;

import Units.RabbitMqConnectionFactory;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DeliverCallback;

public class RoundRobinRecv {
    private static final String QUEUE_NAME = "Test_RoundRobinQuery";
    public static void main(String[] args) throws Exception{
        Connection connection = RabbitMqConnectionFactory.geyConnection();
        //1 通过connection创建一个Channel
        Channel channel = connection.createChannel();
        //2.申明队列         1.队列名  2.持久性(durability)
        channel.queueDeclare(QUEUE_NAME, true, false, false, null);
        channel.basicQos(1); // 每次接受消息条数
        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");
        DeliverCallback deliverCallback = (s, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }finally {
                channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
            }
            System.out.println(" [x] Received '" + message + "'");
        };
        boolean autoAck = false;// true  自动应答  false 手动应答
        channel.basicConsume(QUEUE_NAME, autoAck, deliverCallback, consumerTag -> { });
    }
}
