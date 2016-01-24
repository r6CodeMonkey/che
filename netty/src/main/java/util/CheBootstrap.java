package util;

import channel.CheClientHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import message.CheMessage;

import java.io.UnsupportedEncodingException;

/**
 * Created by timmytime on 24/01/16.
 */
public class CheBootstrap {

    private final Configuration configuration;
    private ChannelFuture channelFuture;

    public CheBootstrap(Configuration configuration) throws InterruptedException {
        this.configuration = configuration;
        initBootstrap();
    }


    private void initBootstrap() throws InterruptedException {
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(workerGroup); // (2)
            bootstrap.channel(NioSocketChannel.class); // (3)
            bootstrap.option(ChannelOption.SO_KEEPALIVE, true); // (4)
            bootstrap.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline().addLast(new CheClientHandler(configuration));
                }
            });

            // Start the client.
            channelFuture = bootstrap.connect(configuration.getCheIP(), configuration.getChePort()).sync(); // (5)
        //    channelFuture.channel().writeAndFlush("hello");
            // Wait until the connection is closed.
            channelFuture.channel().closeFuture().sync();

        }finally {
            workerGroup.shutdownGracefully();
        }

    }

    public void write(CheMessage cheMessage) throws UnsupportedEncodingException {
        channelFuture.channel().writeAndFlush(cheMessage.toString().getBytes("UTF-8"));
    }

}
