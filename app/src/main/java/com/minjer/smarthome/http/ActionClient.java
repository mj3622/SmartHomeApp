package com.minjer.smarthome.http;

import android.content.Context;
import android.util.Log;

import com.minjer.smarthome.pojo.Action;
import com.minjer.smarthome.pojo.ActionPack;
import com.minjer.smarthome.utils.DialogUtil;
import com.minjer.smarthome.utils.JsonUtil;
import com.minjer.smarthome.utils.ParamUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.security.Key;
import java.util.List;

public class ActionClient extends Thread {
    private static final String BASE_EXCHANGE = "hdwork.exchange.";
    private static final String BASE_QUEUE = "hdwork.queue.";

private static String exchange;
    private static String queue;

    private static String Key;
    private static final String TAG = "ActionClient";


    private final String message;

    private final String HOST = "116.205.244.2";
    private final int PORT = 5672;
    private final String USERNAME = "minjer";
    private final String PASSWORD = "123456";

    public ActionClient(String message, String key) {
        this.message = message;
        Key = key;
        queue = BASE_QUEUE + key;
        exchange = BASE_EXCHANGE + key;
    }

    public static void sendAction(Context context, Action action) {
        String key = ParamUtil.getString(context, ParamUtil.GATEWAT_CODE, null);
        if (key == null) {
            DialogUtil.showToastShort(context, "未绑定网关");
            return;
        }
        new ActionClient(JsonUtil.toJson(action), key).start();
    }

    public static void executeActionPack(Context context, ActionPack ap) {
        List<Action> actions = ap.getActions();
        for (Action action : actions) {
            sendAction(context, action);
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void run() {

        try {
            // 设置RabbitMQ连接参数
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost(HOST);
            factory.setPort(PORT);
            factory.setUsername(USERNAME);
            factory.setPassword(PASSWORD);

            // 建立连接
            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();

            // 声明交换机
            channel.exchangeDeclare(exchange, "direct", false);
            // 声明队列
            channel.queueDeclare(queue, true, false, false, null);
            // 绑定队列到交换机
            channel.queueBind(queue, exchange, Key);
            // 发送消息
            channel.basicPublish(exchange, Key, null, message.getBytes());

            // 关闭通道和连接
            channel.close();
            connection.close();
        } catch (Exception e) {
            Log.e(TAG, message);
            Log.e(TAG, "Failed to send message", e);
        }
    }
}

