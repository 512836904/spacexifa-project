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
//    public Thread workThread;
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
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public NettyServerHandler(Server server) {
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
                //数据存入oracle数据库
                mysql.Mysqlbaseoutline(str,server);
                //娆у崕绾崕
            } else if (str.substring(0, 2).equals("fe") && str.length() == 298) {
                System.out.println(str);
            } else {
                /**
                 * 索取返回，下发返回，控制命令返回，白名单返回
                 */
                mqtt.publishMessage("weldmes/upparams", str, 0);
//                if (str.length() == 112){
//                    System.out.println("索取返回:"+sdf.format(System.currentTimeMillis()));
//                }
//                if (str.length() == 24){
//                    System.out.println("下发返回:"+sdf.format(System.currentTimeMillis()));
//                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            str = "";
            ctx.flush();
            ReferenceCountUtil.release(str);
            ReferenceCountUtil.release(msg);
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("异常：" + cause);
        ctx.close();
    }

}
