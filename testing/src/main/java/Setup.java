import java.util.ArrayList;
import java.util.List;

/**
 * Created by timmytime on 16/01/16.
 */
public class Setup {

    private static final int MAX_SOCKETS = 10000;  //its not that sensible to hammer my own connection....raises questions about netty server doing it
    private List<TestSocketController> socketControllers = new ArrayList<>();

    private List<String> allianceKeys = new ArrayList<>();


/*
  worth thinking about game objects at some point.  and what they will do etc?   or start mobile client.
 */


    public Setup() throws Exception {


    }

    public static void main(String[] args) throws Exception {

        Setup setup = new Setup();

        try {
            setup.start();
        } catch (Exception e) {
            e.printStackTrace();
            setup.stop();

        }
    }

    public void start() throws Exception {


        for (int i = 0; i < MAX_SOCKETS; i++) {
            socketControllers.add(new TestSocketController());
        }


        socketControllers.get(0).createAlliance(e -> {
            allianceKeys.add(e.getActionCommand());
            System.out.println("created alliance " + e.getActionCommand());
            //now we have it...lets try and join
            socketControllers.get(1).joinAlliance(e.getActionCommand());
        });

     /*   EventLoopGroup workerGroup = new NioEventLoopGroup();
        try{
        Bootstrap b = new Bootstrap();
            b.group(workerGroup); // (2)
            b.channel(NioSocketChannel.class); // (3)
            b.option(ChannelOption.SO_KEEPALIVE, true); // (4)
            b.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline().addLast(new CheClientHandler());
                }
            });

            // Start the client.
            ChannelFuture f = b.connect("localhost", 8085).sync(); // (5)


            // Wait until the connection is closed.
            f.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
        }
*/


    }

    public void stop() {
        socketControllers.forEach(TestSocketController::stop);
    }
}
