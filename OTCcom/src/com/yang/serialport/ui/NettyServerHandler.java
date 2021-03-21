package com.yang.serialport.ui;

import com.DateTools;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.socket.SocketChannel;
import io.netty.util.ReferenceCountUtil;

import javax.swing.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.*;

/**
 * Netty 业务处理类，处理焊机发过来的数据，
 * 对其进行协议解析和业务处理
 */
@Sharable
public class NettyServerHandler extends SimpleChannelInboundHandler<ByteBuf> {

    public String fitemid = "17";
    public SocketChannel chcli = null;


    //创建缓存线程池，处理OTC实时数据
    public static ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
//    private static final ThreadPoolExecutor cachedThreadPool =
//            new ThreadPoolExecutor(0, Integer.MAX_VALUE,60, TimeUnit.SECONDS,new SynchronousQueue<>());
    /**
     * 保存连接进服务端的通道数量
     */
    private static final ConcurrentHashMap<ChannelId, ChannelHandlerContext> channelList = new ConcurrentHashMap<>();

    /**
     * 收到焊机发送的数据
     * @param ctx
     * @param buf
     * @throws Exception
     */
    @Override
    protected void messageReceived(ChannelHandlerContext ctx, ByteBuf buf) throws Exception {
        byte[] req = new byte[buf.readableBytes()];
        buf.readBytes(req);
        cachedThreadPool.execute(new Workspace(req));
    }

    class Workspace implements Runnable {
        private byte[] req;
        private String str = "";
        Date time;
        Timestamp timesql = null;

        public Workspace(byte[] req) {
            this.req = req;
        }

        @Override
        public void run() {
            try {
                /**
                 * 144:实时数据包
                 * 11：控制命令返回，密码返回
                 * 56：索取返回
                 * 12：下发返回
                 * 13：白名单下发返回
                 */
                if (req.length == 144 || req.length == 56 || req.length == 12 || req.length == 11 || req.length == 13) {
                    for (int i = 0; i < req.length; i++) {
                        //判断为数字还是字母，若为字母+256取正数
                        if (req[i] < 0) {
                            String r = Integer.toHexString(req[i] + 256);
                            String rr = r.toUpperCase();
                            //数字补为两位数
                            if (rr.length() == 1) {
                                rr = '0' + rr;
                            }
                            //strdata为总接收数据
                            str += rr;

                        } else {
                            String r = Integer.toHexString(req[i]);
                            if (r.length() == 1)
                                r = '0' + r;
                            r = r.toUpperCase();
                            str += r;
                        }
                    }
                }
                if (str.length() >= 6) {
                    //基本版
                    if (str.substring(0, 2).equals("7E") && (str.substring(10, 12).equals("22") || str.substring(10, 12).equals("23")) && str.length() == 288) {
                        /**
                         * 数据发送
                         * str。length:596
                         */
                        String charStr = "";
                        for (int i = 0; i < 102; i++) {
                            charStr += 0;
                        }
                        String headData = str.substring(0, 44);                    //头部数据
                        String oneData = str.substring(44, 124) + charStr;        //第一组数据
                        String towData = str.substring(124, 204) + charStr;    //第二组数据
                        String threeData = str.substring(204, 284) + charStr;    //第三组数据
                        String footerData = str.substring(284, 286);                //尾部数据
                        str = headData + oneData + towData + threeData + footerData + fitemid + "7D";


//                        String year = Integer.valueOf(str.subSequence(44, 46 ).toString(), 16).toString();   //年份
//                        String month = Integer.valueOf(str.subSequence(46 , 48 ).toString(), 16).toString();
//                        String day = Integer.valueOf(str.subSequence(48 , 50 ).toString(), 16).toString();
//                        String hour = Integer.valueOf(str.subSequence(50, 52 ).toString(), 16).toString();
//                        String minute = Integer.valueOf(str.subSequence(52 , 54).toString(), 16).toString();
//                        String second = Integer.valueOf(str.subSequence(54 , 56 ).toString(), 16).toString();
//                        String strdate = year + "-" + month + "-" + day + " " + hour + ":" + minute + ":" + second;
//                        time = DateTools.parse("yy-MM-dd HH:mm:ss", strdate);
//                        timesql = new Timestamp(time.getTime());
//                        String gather = Integer.valueOf(str.substring(16, 20), 16).toString();
//                        if(Integer.valueOf(gather)==28){
//                            System.out.println("采集编号："+Integer.valueOf(str.substring(16, 20), 16).toString()+"当前时间："+timesql);
//                        }
                        try {
                            chcli.writeAndFlush(str);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                        str = "";
                    } else {
                        /**
                         * 索取返回，下发返回，控制命令返回，白名单返回
                         */
                        try {
                            chcli.writeAndFlush(str);
                            System.out.println(str);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                        str = "";
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                System.out.println("Workspace工作线程异常！" + ex);
            }
        }
    }

    /**
     * 通道读取数据完成出发此方法，做刷新操作
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    /**
     * 焊机连接时会出发此方法
     *
     * @param ctx
     * @throws Exception
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
            //System.out.println("存在连接：" + clientIp + ":" + clientPort + "--->连接通道数量: " + channelList.size());
        } else {
            //保存连接
            channelList.put(channelId, ctx);
            MainFrame.channelList = channelList;
            TcpClientHandler.channelList = channelList;
            //System.out.println("新增连接:" + clientIp + "：" + clientPort + "--->连接通道数量: " + channelList.size());
        }
    }

    /**
     * 焊机终止连接会出发此方法
     *
     * @param ctx
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
            MainFrame.channelList = channelList;
            TcpClientHandler.channelList = channelList;
            System.out.println("终止连接:" + clientIp + "：" + port + "--->连接通道数量: " + channelList.size());
        }
        ctx.channel().closeFuture();
        ctx.close();
    }

    /**
     * 异常捕捉
     *
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("异常：" + cause);
        ctx.close();
    }
}
