package com.minjer.smarthome.utils;

import android.os.AsyncTask;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class RabbitmqUtil {
    private final static String HOST = "116.205.244.2";
    private final static int PORT = 5672;
    private final static String QUEUE_NAME = "hdwork.queue";
    private final static String EXCHANGE_NAME = "hdwork.exchange.direct";
    private final static String ROUTING_KEY = "hello_routing_key";
    public static final String USERNAME = "minjer";
    public static final String PASSWORD = "123456";


    public static void publishMessage(String message) {
        new PublishMessageTask().execute(message);
    }

    private static class PublishMessageTask extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... messages) {
            String message = messages[0];
            try {
                // 设置RabbitMQ连接参数
                ConnectionFactory factory = new ConnectionFactory();
                factory.setHost(HOST); // 替换为你的RabbitMQ服务器地址
                factory.setPort(PORT);
                factory.setUsername(USERNAME); // 替换为你的RabbitMQ用户名
                factory.setPassword(PASSWORD); // 替换为你的RabbitMQ密码

                // 建立连接
                Connection connection = factory.newConnection();
                Channel channel = connection.createChannel();

                // 声明交换器和队列
                channel.exchangeDeclare(EXCHANGE_NAME, "direct", false);
                channel.queueDeclare(QUEUE_NAME, true, false, false, null);
                channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, ROUTING_KEY);

                // 发布消息
                channel.basicPublish(EXCHANGE_NAME, ROUTING_KEY, null, message.getBytes("UTF-8"));
                System.out.println(" [x] Sent '" + message + "'");

                // 关闭通道和连接
                channel.close();
                connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}

