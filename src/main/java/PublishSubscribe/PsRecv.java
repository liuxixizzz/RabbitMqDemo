package PublishSubscribe;

import Units.RabbitMqConnectionFactory;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DeliverCallback;

public class PsRecv {
    private static final String EXCHANGE_NAME = "Test_PSExchange";
    //private static final String QUEUE_NAME = "Test_PSQueryOne";
    public static void main(String[] args) throws Exception{
        Connection connection = RabbitMqConnectionFactory.geyConnection();
        //1 通过connection创建一个Channel
        Channel channel = connection.createChannel();
        //2.申明交换机
        channel.exchangeDeclare(EXCHANGE_NAME, "fanout");

        //3.申明队列         1.队列名  2.持久性(durability)
        //channel.queueDeclare(QUEUE_NAME, true, false, false, null);
        //3.随机队列名称
        String queueName = channel.queueDeclare().getQueue();
        //4.绑定交换机  对列名称  交换机名称
        channel.queueBind(queueName,EXCHANGE_NAME , "");
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
        channel.basicConsume(queueName, autoAck, deliverCallback, consumerTag -> { });
    }
}
