package core;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import util.Configuration;

/**
 * Created by timmytime on 09/12/15.
 */
public class NettyServer {

    private final Configuration configuration;
    private final NettyChannelInitializer channelInitializer = new NettyChannelInitializer();

    public NettyServer(Configuration configuration) {
        this.configuration = configuration;
        channelInitializer.init(configuration);
    }

    //this is entry point
    public static void main(String[] args) throws Exception {

        //need to load actually configuration at some point.
        Configuration config = new Configuration();

        new NettyServer(config).run();
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
}