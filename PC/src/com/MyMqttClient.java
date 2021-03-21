package com;

import com.alibaba.fastjson.JSONObject;
import com.sun.xml.xsom.impl.scd.SCDImpl;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelId;
import io.netty.channel.socket.SocketChannel;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

public class MyMqttClient {
    public static MqttClient mqttClient = null;
    private static MemoryPersistence memoryPersistence = null;
    private static MqttConnectOptions mqttConnectOptions = null;
    //在线OTC连接通道
    public static ConcurrentHashMap<ChannelId, ChannelHandlerContext> channelList;
    private String ip;
    //新加
    public ArrayList<String> taskarray = new ArrayList<String>();   //存取手持终端下发的任务信息
    public Server server;
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    //新加
    public MyMqttClient(Server server) {
        // TODO Auto-generated constructor stub
        this.server = server;
    }

    public void init(String clientId) {
        try {
            File file = new File("PC/IPconfig.txt");

//			FileInputStream in = new FileInputStream("IPconfig.txt");
            FileInputStream in = new FileInputStream(file.getCanonicalPath());
            InputStreamReader inReader = new InputStreamReader(in, "UTF-8");
            BufferedReader bufReader = new BufferedReader(inReader);
            String line = null;
            int writetime = 0;

            while ((line = bufReader.readLine()) != null) {
                if (writetime == 0) {
                    ip = line;
                    writetime++;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        String[] values = ip.split(",");

        mqttConnectOptions = new MqttConnectOptions();
        if (null != mqttConnectOptions) {
            mqttConnectOptions.setCleanSession(true);
            mqttConnectOptions.setConnectionTimeout(3000);
            mqttConnectOptions.setKeepAliveInterval(3000);
            memoryPersistence = new MemoryPersistence();
            if (null != memoryPersistence && null != clientId) {
                try {
                    mqttClient = new MqttClient("tcp://" + values[0] + ":1883", clientId, memoryPersistence);
                } catch (MqttException e) {
                    e.printStackTrace();
                }
            }
        } else {
            System.out.println("mqttConnectOptions");
        }

        if (null != mqttClient) {
            if (!mqttClient.isConnected()) {
                MqttReceriveCallback mqttReceriveCallback = new MqttReceriveCallback(this);
                mqttClient.setCallback(mqttReceriveCallback);
                try {
                    mqttClient.connect(mqttConnectOptions);
                } catch (MqttException e) {
                    e.printStackTrace();
                }
            }else {
                System.out.println("MQTT服务连接成功！");
            }
        } else {
            System.out.println("mqttClientΪ is null");
        }
    }

    public void closeConnect() {
        if (null != memoryPersistence) {
            try {
                memoryPersistence.close();
            } catch (MqttPersistenceException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("memoryPersistence is null");
        }

        if (null != mqttClient) {
            if (mqttClient.isConnected()) {
                try {
                    mqttClient.disconnect();
                    mqttClient.close();
                } catch (MqttException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("mqttClient is not connect");
            }
        } else {
            System.out.println("mqttClient is null");
        }
    }

    //	消息发送
    public void publishMessage(String pubTopic, String message, int qos) {
        if (null != mqttClient && mqttClient.isConnected()) {
            MqttMessage mqttMessage = new MqttMessage();
            mqttMessage.setQos(qos);
            mqttMessage.setPayload(message.getBytes());

            MqttTopic topic = mqttClient.getTopic(pubTopic);

            if (null != topic) {
                try {
                    MqttDeliveryToken publish = topic.publish(mqttMessage);
                    if (!publish.isComplete()) {
                        //System.out.println("��Ϣ�����ɹ�:"+message);
                    }
                } catch (MqttException e) {
                    e.printStackTrace();
                }
            }

        } else {
            reConnect();
        }

    }

    //	重连
    public void reConnect() {
        if (null != mqttClient) {
            if (!mqttClient.isConnected()) {
                if (null != mqttConnectOptions) {
                    try {
                        mqttClient.connect(mqttConnectOptions);
                    } catch (MqttException e) {
                        e.printStackTrace();
                    }
                } else {
                    System.out.println("mqttConnectOptions is null");
                }
            } else {
                System.out.println("mqttClient is null or connect");
            }
        } else {
            init("123");
        }

    }

    //	订阅主题
    public void subTopic(String topic) {
        if (null != mqttClient && mqttClient.isConnected()) {
            try {
                mqttClient.subscribe(topic, 0);
            } catch (MqttException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else {
            System.out.println("mqttClient is error subTopic");
        }
    }

    //	清空主题
    public void cleanTopic(String topic) {
        if (null != mqttClient && !mqttClient.isConnected()) {
            try {
                mqttClient.unsubscribe(topic);
            } catch (MqttException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else {
            System.out.println("mqttClient is error cleanTopic");
        }
    }

    //替换(接收前端消息，发送到OTC采集器)
    public void webdata(String str) {
        /**
         * 判断是手持终端下发的任务信息还是前端mq下发的信息
         */
        if (str.charAt(0) == '{') {
            System.out.println("手持终端下发了任务");
            try {
                JSONObject taskstr = JSONObject.parseObject(str);
                if ("starttask".equals(taskstr.getString("type"))) {
                    if (taskarray.isEmpty()) {
                        taskarray.add(taskstr.getString("machine"));  //设备号
                        taskarray.add(taskstr.getString("welderid")); //焊工id
                        taskarray.add(taskstr.getString("cardid"));   //电子跟踪卡id
                        taskarray.add(taskstr.getString("wpsid"));    //工艺id
                        taskarray.add(taskstr.getString("productid")); //产品号id(返修状态)
                        taskarray.add(taskstr.getString("workprocedureid")); //工序号id
                        taskarray.add(taskstr.getString("workstepid")); //工步号id
                        taskarray.add(taskstr.getString("weldlineid")); //焊缝号id
                    } else {
                        if (taskarray.contains(taskstr.getString("machine"))) {
                            int index = taskarray.indexOf(taskstr.getString("machine"));
                            taskarray.set(index + 1, taskstr.getString("welderid"));
                            taskarray.set(index + 2, taskstr.getString("cardid"));
                            taskarray.set(index + 3, taskstr.getString("wpsid"));
                            taskarray.set(index + 4, taskstr.getString("productid"));
                            taskarray.set(index + 5, taskstr.getString("workprocedureid"));
                            taskarray.set(index + 6, taskstr.getString("workstepid"));
                            taskarray.set(index + 7, taskstr.getString("weldlineid"));

                        } else {
                            taskarray.add(taskstr.getString("machine"));
                            taskarray.add(taskstr.getString("welderid"));
                            taskarray.add(taskstr.getString("cardid"));
                            taskarray.add(taskstr.getString("wpsid"));
                            taskarray.add(taskstr.getString("productid"));
                            taskarray.add(taskstr.getString("workprocedureid"));
                            taskarray.add(taskstr.getString("workstepid"));
                            taskarray.add(taskstr.getString("weldlineid"));
                        }
                    }
                    System.out.println("手持终端下发了任务：" + taskstr.getString("machine") + ",时间：" + sdf.format(System.currentTimeMillis()));
                    this.server.NS.mysql.db.taskarray = taskarray;
                    this.server.websocket.taskarray = taskarray;
                } else if ("overtask".equals(taskstr.getString("type"))) {
                    if (!taskarray.isEmpty() && taskarray.contains(taskstr.getString("machine"))) {
                        int index = taskarray.indexOf(taskstr.getString("machine"));
                        taskarray.remove(index);
                        taskarray.remove(index);
                        taskarray.remove(index);
                        taskarray.remove(index);
                        taskarray.remove(index);
                        taskarray.remove(index);
                        taskarray.remove(index);
                        taskarray.remove(index);
                        System.out.println("手持终端结束了任务：" + taskstr.getString("machine") + ",时间：" + sdf.format(System.currentTimeMillis()));
                    }
                    this.server.NS.mysql.db.taskarray = taskarray;
                    this.server.websocket.taskarray = taskarray;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            //System.out.println("网页下发了任务");
            //System.out.println("len:"+str.length()+"-->str:"+str);
            Server.cachedThreadPool.execute(new writeMessageByMq(str));
        }
    }

    class writeMessageByMq implements Runnable {

        private String str = "";

        writeMessageByMq(String str) {
            this.str = str;
        }

        @Override
        public void run() {
            //synchronized (channelList) {
                Iterator<Entry<ChannelId, ChannelHandlerContext>> iterator = channelList.entrySet().iterator();
                while (iterator.hasNext()) {
                    try {
                        Entry<ChannelId, ChannelHandlerContext> next = iterator.next();
                        ChannelHandlerContext value = next.getValue();
                        if (value.channel().isOpen() && value.channel().isActive() && value.channel().isWritable()) {
                            value.channel().writeAndFlush(str);
                        }
                    } catch (Exception e) {
                        e.getStackTrace();
                    }
                }
            //}
        }
    }
}