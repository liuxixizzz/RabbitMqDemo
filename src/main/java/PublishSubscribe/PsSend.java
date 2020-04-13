package PublishSubscribe;

import Units.RabbitMqConnectionFactory;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.MessageProperties;

public class PsSend {
    private static final String EXCHANGE_NAME = "Test_PSExchange";

    public static void main(String[] args) throws Exception {
        Connection connection = RabbitMqConnectionFactory.geyConnection();
        //1 通过connection创建一个Channel
        Channel channel = connection.createChannel();
        //2.申明交换机
        channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
        //3 通过Channel发送数据
        for (int i = 0; i < 5; i++) {
            String msg = "RoundRobinQuery!  " + i;
            Thread.sleep(1000);
            //1.交换机   2.队列名称  3. 是否持久化消息
            channel.basicPublish(EXCHANGE_NAME, "", null, msg.getBytes());
            System.out.println(msg);
        }
        //4 记得要关闭相关的连接
        channel.close();
        connection.close();
    }
}
