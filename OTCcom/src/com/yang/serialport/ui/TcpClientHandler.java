package com.yang.serialport.ui;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelId;
import io.netty.channel.EventLoop;
import io.netty.channel.socket.SocketChannel;
import io.netty.util.CharsetUtil;

/**
 * OTC客户端，接收PC下发的数据
 */
public class TcpClientHandler extends ChannelHandlerAdapter {

    public Clientconnect client;
    //存当前在线焊机的通道，用于索取和下发
    public static ConcurrentHashMap<ChannelId, ChannelHandlerContext> channelList;

    public TcpClientHandler(Clientconnect client) {
        this.client = client;
    }

    public TcpClientHandler() {}

    /**
     * 接收PC下发的数据
     * @param ctx
     * @param msg
     * @throws Exception
     */
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        String str = (String) msg;
        //System.out.println("len:"+str.length()+"-->str:"+str);
        MainFrame.cachedThreadPool.execute(new writeMessageForOTCByMq(str));
    }

    class writeMessageForOTCByMq implements Runnable {

        private String str = "";
        writeMessageForOTCByMq(String str){
            this.str = str;
        }

        @Override
        public void run() {
            //加锁，保证数据下发过程中不被修改
            synchronized (channelList) {
                Iterator<Entry<ChannelId, ChannelHandlerContext>> iterator = channelList.entrySet().iterator();
                while (iterator.hasNext()) {
                    try {
                        Entry<ChannelId, ChannelHandlerContext> next = iterator.next();
                        ChannelHandlerContext value = next.getValue();
                        byte[] data = new byte[str.length() / 2];
                        for (int i1 = 0; i1 < data.length; i1++) {
                            String tstr1 = str.substring(i1 * 2, i1 * 2 + 2);
                            if (tstr1.contains("N")){
                                continue;
                            }
                            Integer k = Integer.valueOf(tstr1, 16);
                            data[i1] = (byte) k.byteValue();
                        }
                        ByteBuf byteBuf = Unpooled.buffer();
                        byteBuf.writeBytes(data);
                        if (value.channel().isOpen() && value.channel().isActive() && value.channel().isWritable()) {
                            value.channel().writeAndFlush(byteBuf);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        continue;
                    }
                }
           }
        }
    }

    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    /**
     * PC终止连接后重连
     * @param ctx
     * @throws Exception
     */
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        final EventLoop eventLoop = ctx.channel().eventLoop();
        eventLoop.schedule(new Runnable() {
            @Override
            public void run() {
                client.createBootstrap(eventLoop);
            }
        }, 1L, TimeUnit.SECONDS);
    }

    /**
     * 异常捕捉
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
       // ctx.close();
    }

}
