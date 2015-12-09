package com.oddymobstar.server.netty.core;

import com.oddymobstar.server.netty.util.Configuration;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.util.Collections;


/**
 * Created by timmytime on 01/01/15.
 */
public class NettyServer {

    private final Configuration configuration;

    private final NettyChannelInitializer channelInitializer = new NettyChannelInitializer();

    public NettyServer(Configuration configuration) {
        this.configuration = configuration;
        channelInitializer.init(configuration);
    }

    public void run() throws Exception {


        EventLoopGroup bossGroup = new NioEventLoopGroup(configuration.getBossThreads());
        EventLoopGroup workerGroup = new NioEventLoopGroup(configuration.getWorkerThreads());
        try {

            ServerBootstrap b = new ServerBootstrap();

            //sort out the mode soon
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(channelInitializer)
                    .option(ChannelOption.SO_BACKLOG, configuration.getBacklog())
                    .option(ChannelOption.SO_LINGER, configuration.getSoLinger())
                            //    .option(ChannelOption.SO_RCVBUF, configuration.getSoRcvBuf())
                            //    .option(ChannelOption.SO_SNDBUF, configuration.getSoSndBuf())
                    .option(ChannelOption.SO_TIMEOUT, configuration.getSoTimeout())
                    .childOption(ChannelOption.TCP_NODELAY, configuration.getNodeDelay())
                    .childOption(ChannelOption.SO_KEEPALIVE, configuration.getKeepAlive());

            ChannelFuture f = b.bind(configuration.getPort()).sync();


            f.channel().closeFuture().sync();


        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
            channelInitializer.stop();
        }

    }

    //this is entry point
    public static void main(String[] args) throws Exception {

        //need to set up the configuration manually...need to force configuration to load
        Configuration config = new Configuration();


        new NettyServer(config).run();
    }
}
