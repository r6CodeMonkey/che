


/**
 * Created by timmytime on 16/01/16.
 */
public class Setup {

    /*
      fuck the jvm startups...i need a script to start the servers.  in interim can do it manually but fix this tomoz based on source locations. simples.

      what this does, is starts up 1 .. many sockets.

      each socket needs to be able to call netty only (8085).

      each socket needs to maintain the data it gets back.

      so we need a socket class.

      and this socket needs to understand wtf its doing.

      ie : start a connection, save player key.  then set up tests to get them all setting up alliances, sharing alliances, bla bla blah.

      so basically organised madness.  the sockets will need to write some stuff, such as the alliance keys back to a central repo (here).

      additionally need some way of moving our players (sockets) around to test that too.

      and then the real fun starts when we make objects and attack each other.  ha ha.  good sunday (birthday job me thinks).


      possilby @BeforeTest intitiates this...so we can test results etc.  clearly.


     */


 //   private final NettyServer nettyServer;
 //   private final CheControllerServer cheControllerServer;

    /*
      start our servers.
     */

    public Setup() throws Exception {

        //basically needs to run in seperate JVMS lol.
        //doesnt stop me from testing it however.  ie need a script to start each up in seperate jvms...yawn.  stop.

    //    nettyServer = new NettyServer();
    //    cheControllerServer = new CheControllerServer();

        /*
          work my way around this,  if not....not an issue, ie start each service, then use this to test it.  which makes sense....tbh.
         */


    }

    public void start() throws Exception {

   //     nettyServer.run();
    //    HazelcastServer.startServer();
   //     cheControllerServer.run();

    }

    public void stop(){
   //     HazelcastServer.stopServer();
    }

    public static void main(String[] args) throws Exception {

        Setup setup = new Setup();

        try{
            setup.start();
        }catch (Exception e){
            setup.stop();

        }
    }
}
