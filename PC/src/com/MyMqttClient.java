package com;

import com.alibaba.fastjson.JSONObject;
import io.netty.channel.socket.SocketChannel;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

public class MyMqttClient {
    public static MqttClient mqttClient = null;
    private static MemoryPersistence memoryPersistence = null;
    private static MqttConnectOptions mqttConnectOptions = null;
    private String socketfail;
    public HashMap<String, SocketChannel> socketlist = new HashMap<>();
    private String ip;
    private String ip1;
    //新加
    public ArrayList<String> taskarray = new ArrayList<String>();   //存取手持终端下发的任务信息
    public Server server;

    /*
     * static { init("MQTT_FX_Client"); }
     */

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
                } else {
                    ip1 = line;
                    writetime = 0;
                }
            }

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        String[] values = ip.split(",");

        //��ʼ���������ö���
        mqttConnectOptions = new MqttConnectOptions();
        //��ʼ��MqttClient
        if (null != mqttConnectOptions) {
            /*
             * mqttConnectOptions.setUserName("815137651@qq.com");
             * mqttConnectOptions.setPassword("shgwth4916.".toCharArray());
             */
            //			true���԰�ȫ��ʹ���ڴ�־�����Ϊ�ͻ��˶Ͽ�����ʱ���������״̬
            mqttConnectOptions.setCleanSession(true);
            //			�������ӳ�ʱ������
            mqttConnectOptions.setConnectionTimeout(3000);
            mqttConnectOptions.setKeepAliveInterval(3000);
            //			���ó־û���ʽ
            memoryPersistence = new MemoryPersistence();
            if (null != memoryPersistence && null != clientId) {
                try {
                    mqttClient = new MqttClient("tcp://" + values[0] + ":1883", clientId, memoryPersistence);
                } catch (MqttException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            } else {

            }
        } else {
            System.out.println("mqttConnectOptions����Ϊ��");
        }

        //�������Ӻͻص�
        if (null != mqttClient) {
            if (!mqttClient.isConnected()) {

                //			�����ص���������
                MqttReceriveCallback mqttReceriveCallback = new MqttReceriveCallback(this);
                //			�ͻ�����ӻص�����
                mqttClient.setCallback(mqttReceriveCallback);
                //			��������
                try {
                    mqttClient.connect(mqttConnectOptions);
                } catch (MqttException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }
        } else {
            System.out.println("mqttClientΪ is null");
        }
        System.out.println(mqttClient.isConnected());
    }

    //	�ر�����
    public void closeConnect() {
        //�رմ洢��ʽ
        if (null != memoryPersistence) {
            try {
                memoryPersistence.close();
            } catch (MqttPersistenceException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else {
            System.out.println("memoryPersistence is null");
        }

        //		�ر�����
        if (null != mqttClient) {
            if (mqttClient.isConnected()) {
                try {
                    mqttClient.disconnect();
                    mqttClient.close();
                } catch (MqttException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            } else {
                System.out.println("mqttClient is not connect");
            }
        } else {
            System.out.println("mqttClient is null");
        }
    }

    //	������Ϣ
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
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

        } else {
            reConnect();
        }

    }

    //	��������
    public void reConnect() {
        if (null != mqttClient) {
            if (!mqttClient.isConnected()) {
                if (null != mqttConnectOptions) {
                    try {
                        mqttClient.connect(mqttConnectOptions);
                    } catch (MqttException e) {
                        // TODO Auto-generated catch block
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

    //	��������
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

    //	�������
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
            try {
                JSONObject taskstr = JSONObject.parseObject(str);

                if (taskstr.getString("type").equals("starttask") || taskstr.getString("type").equals("nexttask")) {
                    if (taskarray.isEmpty()) {
                        taskarray.add(taskstr.getString("machine"));  //设备号
                        taskarray.add(taskstr.getString("welderid")); //焊工id
                        taskarray.add(taskstr.getString("cardid"));   //电子跟踪卡id
                        taskarray.add(taskstr.getString("wpsid"));    //工艺id
                        taskarray.add(taskstr.getString("productid")); //产品号id
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

                    this.server.NS.mysql.db.taskarray = taskarray;
                    this.server.NS.websocket.taskarray = taskarray;
                } else if (taskstr.getString("type").equals("overtask")) {
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
                    }

                    this.server.NS.mysql.db.taskarray = taskarray;
                    this.server.NS.websocket.taskarray = taskarray;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            ArrayList<String> listarraybuf = new ArrayList<String>();
            boolean ifdo = false;
            HashMap<String, SocketChannel> socketlist_cl;
            Iterator<Entry<String, SocketChannel>> webiter = socketlist.entrySet().iterator();
            while (webiter.hasNext()) {
                try {
                    Entry<String, SocketChannel> entry = (Entry<String, SocketChannel>) webiter.next();
                    socketfail = entry.getKey();
                    SocketChannel socketcon = entry.getValue();
                    if (socketcon.isOpen() && socketcon.isActive() && socketcon.isWritable()) {
                        socketcon.writeAndFlush(str);
                    } else {
                        listarraybuf.add(socketfail);
                        ifdo = true;
                    }

                } catch (Exception e) {
                    listarraybuf.add(socketfail);
                    ifdo = true;
                    e.getStackTrace();
                }
            }
            if (ifdo) {
                //socketlist_cl = (HashMap<String, SocketChannel>) socketlist.clone();
                for (int i = 0; i < listarraybuf.size(); i++) {
                    socketlist.remove(listarraybuf.get(i));
                }
            }
        }
    }
}