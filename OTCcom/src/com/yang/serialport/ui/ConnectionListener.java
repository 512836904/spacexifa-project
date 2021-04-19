package com.yang.serialport.ui;

import java.util.concurrent.TimeUnit;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.EventLoop;
import io.netty.channel.socket.SocketChannel;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

/**
 * netty连接服务器(PC),若断开自动检测重连
 */
public class ConnectionListener implements ChannelFutureListener {
    public Clientconnect client;
    public static SocketChannel socketChannel;

    public ConnectionListener(Clientconnect client) {
        this.client = client;
    }

    @Override
    public void operationComplete(ChannelFuture channelFuture) throws Exception {
        if (!channelFuture.isSuccess()) {
            final EventLoop loop = channelFuture.channel().eventLoop();
            loop.schedule(new Runnable() {
                @Override
                public void run() {
                    //开始连接
                    client.createBootstrap(loop);
                }
            }, 1L, TimeUnit.SECONDS);
        } else {
            client.NS.chcli = socketChannel;
        }
    }

}

