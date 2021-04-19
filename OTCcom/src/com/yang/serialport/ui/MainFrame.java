package com.yang.serialport.ui;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.util.Calendar;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.concurrent.*;

/**
 * 系统启动类
 */
public class MainFrame {
    public NettyServerHandler NS = new NettyServerHandler();
    public Clientconnect clientconnect = new Clientconnect(NS, this);
    //存当前在线焊机的通道，用于索取和下发
    public static ConcurrentHashMap<ChannelId, ChannelHandlerContext> channelList;

    //创建缓存线程池，处理OTC实时数据
    public static ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
    //定时线程池处理
    public static ScheduledExecutorService scheduledThreadPool = Executors.newScheduledThreadPool(10);

    public MainFrame() {
        // OTC开启客户端连接PC服务端
        startClientConnect();
        //启动Netty服务端，开启5555端口，供焊机链接
        startNettyWorkGroup();
        //开启定时现场处理任务
        timingUpdateForWeld();
    }

    public void timingUpdateForWeld() {
        Runnable scheduleWork = new Runnable() {
            @Override
            public void run() {
                //十分钟发送时间校准
                Calendar d = Calendar.getInstance();
                String year = Integer.toString(d.get(Calendar.YEAR));
                String month = Integer.toString(d.get(Calendar.MONTH) + 1);
                int length2 = 2 - month.length();
                if (length2 != 2) {
                    for (int i = 0; i < length2; i++) {
                        month = "0" + month;
                    }
                }
                String day = Integer.toString(d.get(Calendar.DAY_OF_MONTH));
                int length3 = 2 - day.length();
                if (length3 != 2) {
                    for (int i = 0; i < length3; i++) {
                        day = "0" + day;
                    }
                }
                String hour = Integer.toString(d.get(Calendar.HOUR_OF_DAY));
                int length4 = 2 - hour.length();
                if (length4 != 2) {
                    for (int i = 0; i < length4; i++) {
                        hour = "0" + hour;
                    }
                }
                String minutes = Integer.toString(d.get(Calendar.MINUTE));
                int length5 = 2 - minutes.length();
                if (length5 != 2) {
                    for (int i = 0; i < length5; i++) {
                        minutes = "0" + minutes;
                    }
                }
                String seconds = Integer.toString(d.get(Calendar.SECOND));
                int length6 = 2 - seconds.length();
                if (length6 != 2) {
                    for (int i = 0; i < length6; i++) {
                        seconds = "0" + seconds;
                    }
                }
                String time = "7E0F010101450000" + year + month + day + hour + minutes + seconds + "007D";
                //加锁，保证数据下发过程中不被修改
                synchronized (channelList) {
                    Iterator<Entry<ChannelId, ChannelHandlerContext>> iterator = channelList.entrySet().iterator();
                    while (iterator.hasNext()) {
                        try {
                            Entry<ChannelId, ChannelHandlerContext> next = iterator.next();
                            ChannelHandlerContext value = next.getValue();
                            byte[] b = new byte[time.length() / 2];
                            for (int i = 0; i < time.length(); i += 2) {
                                if (i <= 14 || i >= 30) {
                                    String buf = time.substring(0 + i, 2 + i);
                                    b[i / 2] = (byte) Integer.parseInt(buf, 16);
                                } else {
                                    String buf = time.substring(0 + i, 2 + i);
                                    b[i / 2] = (byte) Integer.parseInt(buf);
                                }
                            }
                            ByteBuf byteBuf = Unpooled.buffer();
                            byteBuf.writeBytes(b);
                            if (value.channel().isOpen() && value.channel().isActive() && value.channel().isWritable()) {
                                value.writeAndFlush(byteBuf).sync();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            System.out.println("Exception:" + e.getMessage());
                        }
                    }
                }
            }
        };
        scheduledThreadPool.schedule(scheduleWork, 600, TimeUnit.SECONDS);
    }

    private void startClientConnect() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                clientconnect.start();
            }
        }).start();
    }

    private void startNettyWorkGroup() {
        new Thread(new Runnable() {
            final EventLoopGroup bossGroup = new NioEventLoopGroup();
            final EventLoopGroup workerGroup = new NioEventLoopGroup();
            @Override
            public void run() {
                try {
                    ServerBootstrap b = new ServerBootstrap();
                    b.group(bossGroup, workerGroup)
                            .channel(NioServerSocketChannel.class)
                            .option(ChannelOption.SO_BACKLOG, 128)
                            .childOption(ChannelOption.SO_KEEPALIVE, true)
                            .childHandler(new ChannelInitializer<SocketChannel>() {
                                @Override
                                public void initChannel(SocketChannel channel) throws Exception {
                                    //焊机连接上后,存入list数组中
                                    channel.pipeline().addLast(NS);
                                }
                            });
                    //绑定端口，等待同步成功
                    ChannelFuture channelFuture = b.bind(5555).sync();
                    if (channelFuture.isSuccess()) {
                        //如果没有成功结束就处理一些事情,结束了就执行关闭服务端等操作
                        System.out.println("OTC服务端启动成功,监听端口是：" + 5555);
                    }
                    //等待服务端关闭监听端口
                    channelFuture.channel().closeFuture().sync();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    //释放线程池资源
                    bossGroup.shutdownGracefully();
                    workerGroup.shutdownGracefully();
                    System.out.println("OTC服务端已关闭！");
                }
            }
        }).start();
    }

    public static void main(String args[]) {
        new MainFrame();
    }
}  
	 
