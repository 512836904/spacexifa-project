package com.yang.serialport.ui;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;

import java.io.*;

/**
 * 采集器【OTC】作为客户端连接转发器【PC】
 */
public class Clientconnect {
    public MainFrame mainFrame;
    public NettyServerHandler NS;
    private final EventLoopGroup loop = new NioEventLoopGroup();
    //PC服务端的IP地址和端口
    private static final String inetIp = "localhost";
    private static final int inetPort = 5551;
    public ConnectionListener CL = new ConnectionListener(this);

    public Clientconnect(NettyServerHandler NS, MainFrame mainFrame) {
        this.NS = NS;
        this.mainFrame = mainFrame;
    }

    public void createBootstrap(EventLoopGroup eventLoop) {
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(eventLoop);
            bootstrap.channel(NioSocketChannel.class);
            bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
            bootstrap.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel socketChannel) throws Exception {
                    socketChannel.pipeline().addLast("frameDecoder", new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4));
                    socketChannel.pipeline().addLast("frameEncoder", new LengthFieldPrepender(4));
                    socketChannel.pipeline().addLast("decoder", new StringDecoder(CharsetUtil.UTF_8));
                    socketChannel.pipeline().addLast("encoder", new StringEncoder(CharsetUtil.UTF_8));
                    socketChannel.pipeline().addLast(new TcpClientHandler());
                    ConnectionListener.socketChannel = socketChannel;
                    NettyServerHandler.chcli = socketChannel;
                }
            });
            ChannelFuture channelFuture = bootstrap.remoteAddress(inetIp, inetPort).connect().sync();
            if (channelFuture.isSuccess()) {
                System.out.println("OTC连接PC成功！");
            } else {
                System.out.println("OTC连接PC失败！");
            }
            bootstrap.connect().addListener(CL).sync();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void start() {
        createBootstrap(loop);
    }
}
