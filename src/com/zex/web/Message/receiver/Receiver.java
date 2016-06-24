package com.zex.web.Message.receiver;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.zex.web.Goods;
import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class Receiver {
    public static void main(String[] args) {
        // ConnectionFactory ：连接工厂，JMS 用它创建连接
        ConnectionFactory connectionFactory;
        // Connection ：JMS 客户端到JMS Provider 的连接
        Connection connection = null;
        // Session： 一个发送或接收消息的线程
        Session session;
        // Destination ：消息的目的地;消息发送给谁.
        //Destination destination;
        Topic destination;
        // 消费者，消息接收者
        MessageConsumer consumer;
        connectionFactory = new ActiveMQConnectionFactory(
                ActiveMQConnection.DEFAULT_USER,
                ActiveMQConnection.DEFAULT_PASSWORD,
                "tcp://localhost:61616");
        try {
            // 构造从工厂得到连接对象
            connection = connectionFactory.createConnection();
            connection.setClientID("abc");
            // 启动
            connection.start();
            // 获取操作连接
            session = connection.createSession(Boolean.FALSE,
                    Session.AUTO_ACKNOWLEDGE);
            // 获取session注意参数值xingbo.xu-queue是一个服务器的queue，须在在ActiveMq的console配置
            //destination = session.createQueue("FirstQueue");
            destination = session.createTopic("topic");
            //consumer = session.createConsumer(destination);
            consumer = session.createDurableSubscriber(destination,"abc","ID='1'",false);
            /*while (true) {
                //设置接收者接收消息的时间，为了便于测试，这里谁定为100s
                TextMessage message = (TextMessage) consumer.receive(60*60*1000);
                if (null != message) {
                    System.out.println("收到消息" + message.getText());
                } else {
                    break;
                }
            }*/
            //consumer.receive(60*60*1000);
            consumer.setMessageListener(new MessageListener() {
                @Override
                public void onMessage(Message message) {
                    try {
                        System.out.println("收到消息" + ((TextMessage) message).getText());
                        Gson gson = new Gson();
                        JsonObject data = gson.fromJson(msg,null);
                        String code = data.get("code").getAsString();
                        if (code.equals("1")){
                            String goodsName = data.get("goodsName").getAsString();
                            int warehourse = data.get("warehourse").getAsInt();
                            int actionUserId = data.get("actionUserId").getAsInt();
                            int num = data.get("num").getAsInt();

                            Goods goods = new Goods();
                            int result = goods.updateOutGoodsByHbm(goodsName,actionUserId,num,1,warehourse);
                        } else if(code.equals("0")){

                        } else if (code.equals("-1")){

                        }
                    } catch (JMSException e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
