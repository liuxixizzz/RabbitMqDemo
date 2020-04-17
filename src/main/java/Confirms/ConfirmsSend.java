package Confirms;

import Units.RabbitMqConnectionFactory;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmListener;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ConfirmsSend {
    private static final String QUEUE_NAME = "Test_Confirm";

    public static void main(String[] args) throws Exception {
        Connection connection = RabbitMqConnectionFactory.geyConnection();
        //1 通过connection创建一个Channel
        Channel channel = connection.createChannel();
        //2 启用发布者确认
        channel.confirmSelect();
        //3 申明队列
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        System.out.println("[send]:");
        Map<Long, String> map = new HashMap();
        //4 通过Channel发送数据
        for (int i = 0; i < 5; i++) {
            String msg = "ConfirmQuery!  " + i;
            Thread.sleep(1000);
            //发布之之气那可以获得消息号
            long sequenceNumber = channel.getNextPublishSeqNo();
            //1.交换机   2.RoutingKey  3. 是否持久化消息
            channel.basicPublish("", QUEUE_NAME, null, msg.getBytes());
            map.put(sequenceNumber, msg);
            /*if (!channel.waitForConfirms(5000))
                System.out.println("not send" + i);*/
            channel.addConfirmListener(new ConfirmListener() {
                @Override//确认消息时的代码
                public void handleAck(long l, boolean b) throws IOException {
                    if(!b&map.containsKey(l)) {
                        System.out.println("发送成功：" + map.get(l) + "。  消息号：" + l);
                        map.remove(l); //清空
                    }
                }

                @Override// 消息未确认时的代码
                public void handleNack(long l, boolean b) throws IOException {
                    System.out.println("发送失败："+ map.get(l) +"。  消息号："+l);
                }
            });
            System.out.println(msg);
        }
        //4 记得要关闭相关的连接
        channel.close();
        connection.close();
    }
}
