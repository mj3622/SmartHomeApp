package com.minjer.smarthome.http;

import android.content.Context;
import android.util.Log;

import com.google.gson.reflect.TypeToken;
import com.minjer.smarthome.pojo.Action;
import com.minjer.smarthome.pojo.ActionPack;
import com.minjer.smarthome.pojo.Device;
import com.minjer.smarthome.utils.DialogUtil;
import com.minjer.smarthome.utils.JsonUtil;
import com.minjer.smarthome.utils.ParamUtil;
import com.minjer.smarthome.utils.TimeUtil;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import java.io.IOException;
import java.security.Key;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

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

    // 获取设备列表（设备状态）
    public static ArrayList<Device> getDeviceList(Context context) throws Exception {

        String key = ParamUtil.getString(context, ParamUtil.GATEWAT_CODE, null);
        Action action = new Action();
        action.setDeviceType(Device.TYPE_RASPBERRY);
        action.setActionType(Action.ACTION_TYPE_OPEN);
        action.setTime(TimeUtil.getNowMillis());
        action.setDeviceId(Device.RASPBERRY_ID);
        action.setInfo("device_list");
        Log.d(TAG, "Action: " + JsonUtil.toJson(action));
        String result = new ActionClient(JsonUtil.toJson(action), key).sendMessageAndGetResponse();
        ArrayList<Device> deviceList = JsonUtil.parseToObject(result, new TypeToken<List<Device>>() {
        }.getType());
        for (Device device : deviceList) {
            String status = device.getStatus();
            if (status.equals("0")) {
                device.setStatus(Device.STATUS_OFFLINE);
            } else if (status.equals("1")) {
                device.setStatus(Device.STATUS_ERROR);
            } else if (status.equals("2")) {
                device.setStatus(Device.STATUS_ONLINE);
            } else {
                device.setStatus(Device.STATUS_UNKNOWN);
            }
            // TODO 添加对设备类型的处理，转换成本地类型（便于后续自动添加）
            device.setType(Device.TYPE_LIGHT);
        }

        return deviceList;
    }

    // 获取光照强度
    public static String getLightIntensity(Context context, String id) {
        String key = ParamUtil.getString(context, ParamUtil.GATEWAT_CODE, null);
        Action action = new Action();
        action.setDeviceType(Device.TYPE_RASPBERRY);
        action.setActionType(Action.ACTION_TYPE_OPEN);
        action.setTime(TimeUtil.getNowMillis());
        action.setDeviceId(id);
        action.setInfo("light");
        Log.d(TAG, "Action: " + JsonUtil.toJson(action));
        String result = null;
        try {
            result = new ActionClient(JsonUtil.toJson(action), key).sendMessageAndGetResponse();
            Log.d(TAG, "Light result: " + result);
            Map<String, String> res_map = JsonUtil.parseToObject(result, new TypeToken<Map<String, String>>() {
            }.getType());
            if (res_map != null) {
                return res_map.get("brightness");
            } else {
                return "获取失败";
            }
        } catch (Exception e) {
            Log.e(TAG, "获取光照强度失败", e);
            return "获取失败";
        }
    }

    // 获取温湿度
    public static Map<String, String> getTempertureAndHumidity(Context context, String id) {
        Map<String, String> map = new HashMap<>();

        String key = ParamUtil.getString(context, ParamUtil.GATEWAT_CODE, null);
        Action action = new Action();
        action.setDeviceType(Device.TYPE_RASPBERRY);
        action.setActionType(Action.ACTION_TYPE_OPEN);
        action.setTime(TimeUtil.getNowMillis());
        action.setDeviceId(id);
        action.setInfo("temp_hum");
        Log.d(TAG, "Action: " + JsonUtil.toJson(action));
        String result = null;
        try {
            result = new ActionClient(JsonUtil.toJson(action), key).sendMessageAndGetResponse();
            Log.d(TAG, "Temp and Hum result: " + result);
            Map<String, String> res_map = JsonUtil.parseToObject(result, new TypeToken<Map<String, String>>() {
            }.getType());

            if (res_map != null) {
                return res_map;
            } else {
                return map;
            }

        } catch (Exception e) {
            Log.e(TAG, "获取温湿度信息失败", e);
            return map;
        }
    }

    // 获取雷达数据
    public static Map<String, String> getRadarData(Context context, String id) {
        // TODO 获取雷达数据，准备测试
        Map<String, String> map = new HashMap<>();

        String key = ParamUtil.getString(context, ParamUtil.GATEWAT_CODE, null);
        Action action = new Action();
        action.setDeviceType(Device.TYPE_RASPBERRY);
        action.setActionType(Action.ACTION_TYPE_OPEN);
        action.setTime(TimeUtil.getNowMillis());
        action.setDeviceId(id);
        action.setInfo("radar");

        try {
            String result = new ActionClient(JsonUtil.toJson(action), key).sendMessageAndGetResponse();
            Map<String, String> res_map = JsonUtil.parseToObject(result, new TypeToken<Map<String, String>>() {
            }.getType());
            return res_map;
        } catch (Exception e) {
            Log.e(TAG, "获取雷达数据失败", e);
        }

        return map;
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

            // 声明一个临时队列
            String replyQueueName = channel.queueDeclare().getQueue();
            String correlationId = UUID.randomUUID().toString();

            // 设置消息属性
            AMQP.BasicProperties props = new AMQP.BasicProperties
                    .Builder()
                    .correlationId(correlationId)
                    .replyTo(replyQueueName)
                    .build();

            // 发送消息
            channel.basicPublish(exchange, Key, props, message.getBytes());

            // 创建队列用于接收响应
            final BlockingQueue<String> response = new ArrayBlockingQueue<>(1);

            // 监听响应队列
            String ctag = channel.basicConsume(replyQueueName, true, new DefaultConsumer(channel) {
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    if (properties.getCorrelationId().equals(correlationId)) {
                        response.offer(new String(body, "UTF-8"));
                    }
                }
            });

            // 获取响应
            String result = response.take();
            Log.i(TAG, "Received response: " + result);

            // 取消消费队列
            channel.basicCancel(ctag);

            // 关闭通道和连接
            channel.close();
            connection.close();
        } catch (Exception e) {
            Log.e(TAG, message);
            Log.e(TAG, "Failed to send message", e);
        }
    }

    // 发送带返回值的消息
    public String sendMessageAndGetResponse() throws IOException, TimeoutException, InterruptedException {
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

        // 声明临时队列用于接收响应
        String replyQueueName = channel.queueDeclare().getQueue();
        Log.d(TAG, "Reply queue: " + replyQueueName);

        // 生成Correlation ID
        String correlationId = UUID.randomUUID().toString();
        Log.d(TAG, "Generated correlation ID: " + correlationId);

        // 设置消息属性
        AMQP.BasicProperties props = new AMQP.BasicProperties
                .Builder()
                .correlationId(correlationId)
                .replyTo(replyQueueName)
                .build();

        // 发送消息
        channel.basicPublish(exchange, Key, props, message.getBytes("UTF-8"));

        // 创建队列用于接收响应
        final BlockingQueue<String> response = new ArrayBlockingQueue<>(1);

        // 监听响应队列
        String ctag = channel.basicConsume(replyQueueName, true, new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                if (properties.getCorrelationId().equals(correlationId)) {
                    response.offer(new String(body, "UTF-8"));
                    Log.d(TAG, "Response received and offered to queue");
                }
            }
        });

        // 获取响应
        String result = null;
        try {
            result = response.poll(2000, TimeUnit.MILLISECONDS); // 等待10秒
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            // 处理中断异常
        }
        if (result == null) {
            Log.e(TAG, "No response received from RabbitMQ within the timeout period.");
        }

        // 取消消费队列
        channel.basicCancel(ctag);

        // 关闭通道和连接
        channel.close();
        connection.close();

        // 返回响应
        return result;
    }
}

