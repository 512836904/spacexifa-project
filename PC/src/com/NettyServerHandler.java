package com;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelId;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.socket.SocketChannel;
import io.netty.util.ReferenceCountUtil;

import java.net.InetSocketAddress;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * PC端数据处理类，处理OTC发过来的数据
 */
@Sharable
public class NettyServerHandler extends ChannelInboundHandlerAdapter {


    public Server server;
    public SocketChannel socketchannel = null;
    public ArrayList<String> listarray1 = new ArrayList<String>();
    public ArrayList<String> listarray2 = new ArrayList<String>();
    public ArrayList<String> listarray3 = new ArrayList<String>();
    public ArrayList<String> listarray4 = new ArrayList<String>();
    public Mysql mysql = new Mysql();
    public MyMqttClient mqtt;
    /**
     * 保存连接进服务端的通道数量
     */
    private static final ConcurrentHashMap<ChannelId, ChannelHandlerContext> channelList = new ConcurrentHashMap<>();

    public NettyServerHandler(Server server) {
        this.server = server;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        String str = "";
        Date time;
        Timestamp timesql = null;
        try {
            str = (String) msg;
            //西安处理(实时数据处理)
            if (str.substring(0, 2).equals("7E") && (str.substring(10, 12).equals("22")) && str.length() == 596) {
                //通过MQ发送到前端
                Server.cachedThreadPool.execute(new Websocket.Websocketbase(str));
//                String year = Integer.valueOf(str.subSequence(44, 46 ).toString(), 16).toString();   //年份
//                String month = Integer.valueOf(str.subSequence(46 , 48 ).toString(), 16).toString();
//                String day = Integer.valueOf(str.subSequence(48 , 50 ).toString(), 16).toString();
//                String hour = Integer.valueOf(str.subSequence(50, 52 ).toString(), 16).toString();
//                String minute = Integer.valueOf(str.subSequence(52 , 54).toString(), 16).toString();
//                String second = Integer.valueOf(str.subSequence(54 , 56 ).toString(), 16).toString();
//                String strdate = year + "-" + month + "-" + day + " " + hour + ":" + minute + ":" + second;
//                time = DateTools.parse("yy-MM-dd HH:mm:ss", strdate);
//                timesql = new Timestamp(time.getTime());
//                String gather = Integer.valueOf(str.substring(16, 20), 16).toString();
//                if(Integer.valueOf(gather)==28){
//                    System.out.println("采集编号："+Integer.valueOf(str.substring(16, 20), 16).toString()+"当前时间："+timesql);
//                }
                //数据存入oracle数据库
                mysql.Mysqlbase(str);
            } else if (str.substring(0, 2).equals("7E") && (str.substring(10, 12).equals("23")) && str.length() == 596) {
                //历史数据存入oracle数据库
                mysql.Mysqlbaseoutline(str, server);
            } else {
                /**
                 * 索取返回，下发返回，控制命令返回，白名单返回
                 */
                Server.cachedThreadPool.execute(new mqPublistMes(str));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }

    public class mqPublistMes implements Runnable {

        private String str;

        public mqPublistMes(String str) {
            this.str = str;
        }

        @Override
        public void run() {
            mqtt.publishMessage("weldmes/upparams", str, 0);
        }
    }

    /**
     * @param ctx
     * @throws Exception
     * @description: 有客户端连接服务器会触发此函数
     * @return: void
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        InetSocketAddress insocket = (InetSocketAddress) ctx.channel().remoteAddress();
        String clientIp = insocket.getAddress().getHostAddress();
        int clientPort = insocket.getPort();
        // 唯一标识
        ChannelId channelId = ctx.channel().id();
        //如果map中不包含此连接，就保存连接
        if (channelList.containsKey(channelId)) {
            System.out.println("存在连接：" + clientIp + ":" + clientPort + "--->连接通道数量: " + channelList.size());
        } else {
            //保存连接
            channelList.put(channelId, ctx);
            MyMqttClient.channelList = channelList;
            System.out.println("新增连接:" + clientIp + "：" + clientPort + "--->连接通道数量: " + channelList.size());
        }
    }

    /**
     * @param ctx
     * @description: 有客户端终止连接服务器会触发此函数
     * @return: void
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        InetSocketAddress insocket = (InetSocketAddress) ctx.channel().remoteAddress();
        String clientIp = insocket.getAddress().getHostAddress();
        int port = insocket.getPort();
        ChannelId channelId = ctx.channel().id();
        //包含此客户端才去删除
        if (channelList.containsKey(channelId)) {
            //删除连接
            channelList.remove(channelId);
            MyMqttClient.channelList = channelList;
            System.out.println("终止连接:" + clientIp + "：" + port + "--->连接通道数量: " + channelList.size());
        }
        //ctx.channel().close();
        //ctx.close();
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("异常捕捉：" + cause);
        //ctx.close();
    }

}
