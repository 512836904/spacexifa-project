package com;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.SocketChannel;
import io.netty.util.ReferenceCountUtil;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

@Sharable
public class NettyServerHandler extends ChannelHandlerAdapter {

//    public String ip;
//    public String ip1;
//    public String connet;
    public Server server;
    public Thread workThread;
    public java.sql.Statement stmt = null;
    public java.sql.Connection conn = null;
    public SocketChannel socketchannel = null;
    public ArrayList<String> dbdata = new ArrayList<String>();
    public ArrayList<String> listarray1 = new ArrayList<String>();
    public ArrayList<String> listarray2 = new ArrayList<String>();
    public ArrayList<String> listarray3 = new ArrayList<String>();
    public ArrayList<String> listarray4 = new ArrayList<String>();
    public ArrayList<String> listarrayplc = new ArrayList<String>();  //鐒婂伐銆佸伐浣嶅搴�
    public HashMap<String, SocketChannel> socketlist = new HashMap<>();
    public HashMap<String, SocketChannel> websocketlist = new HashMap<>();
    public Mysql mysql = new Mysql();
    public Android android = new Android();
    public Websocket websocket = new Websocket();
    public byte[] b;
    public int a = 0;
    public MyMqttClient mqtt;

    public NettyServerHandler(Server server) {
        // TODO Auto-generated constructor stub
        this.server = server;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        String str = "";
        try {
            str = (String) msg;

            //西安处理(实时数据处理)
            if (str.substring(0, 2).equals("7E") && (str.substring(10, 12).equals("22")) && str.length() == 596) {
                //通过MQ发送到前端
                websocket.Websocketbase(str);
                //数据存入oracle数据库
                mysql.Mysqlbase(str);
                //娆у崕绾崕
            }else if (str.substring(0, 2).equals("7E") && (str.substring(10, 12).equals("23")) && str.length() == 596) {
                //通过MQ发送到前端
                //websocket.Websocketbase(str, listarray2, listarray3, websocketlist);
                //数据存入oracle数据库
                mysql.Mysqlbaseoutline(str,server);
                //娆у崕绾崕
            } else if (str.substring(0, 2).equals("fe") && str.length() == 298) {

//                mysql.Mysqlplc(str, listarrayplc);
                System.out.println(str);

//                if (socketchannel != null) {
//                    synchronized (socketchannel) {
//                        try {
//                            socketchannel.writeAndFlush(str).sync();
//                            System.out.println("鍙戦�佹垚鍔�:" + str);
//                        } catch (Exception e) {
//                            try {
//                                socketchannel.close().sync();
//                            } catch (InterruptedException e1) {
//                                e1.printStackTrace();
//                            }
//                            socketchannel = null;
//                            e.printStackTrace();
//                        }
//                    }
//                }
            } else {
                //mqtt处理
                mqtt.publishMessage("weldmes/upparams", str, 0);
            }
            ReferenceCountUtil.release(msg);
            ReferenceCountUtil.release(str);
        } catch (Exception e) {
            e.printStackTrace();
        }

        ctx.flush();
    }

    public class Workspace implements Runnable {

        private String str = "";
        public byte[] req;
        private String socketfail;
        private String websocketfail;
        private String data;

        public Workspace(String str) {
            this.str = str;
        }

        @Override
        public void run() {
            //西安处理(实时数据处理)
            if (str.substring(0, 2).equals("7E") && (str.substring(10, 12).equals("22")) && str.length() == 596) {
                //数据存入oracle数据库
                //mysql.Mysqlbase(str);
                //通过MQ发送到前端
                //websocket.Websocketbase(str, listarray2, listarray3, websocketlist);

                //娆у崕绾崕
            } else if (str.substring(0, 2).equals("fe") && str.length() == 298) {

                mysql.Mysqlplc(str, listarrayplc);
                System.out.println(str);

                if (socketchannel != null) {
                    synchronized (socketchannel) {
                        try {
                            socketchannel.writeAndFlush(str).sync();
                            System.out.println("鍙戦�佹垚鍔�:" + str);
                        } catch (Exception e) {
                            try {
                                socketchannel.close().sync();
                            } catch (InterruptedException e1) {
                                e1.printStackTrace();
                            }
                            socketchannel = null;
                            e.printStackTrace();
                        }
                    }
                }
            } else {
                //mqtt处理
                mqtt.publishMessage("weldmes/upparams", str, 0);
            }
            workThread.interrupt();
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close().sync();
    }

}
