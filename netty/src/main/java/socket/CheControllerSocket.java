package socket;

import io.netty.channel.Channel;
import io.netty.handler.codec.serialization.ObjectEncoderOutputStream;
import model.Core;
import util.Configuration;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by timmytime on 29/12/15.
 */
public class CheControllerSocket {

    public static final int BUFFER_SIZE = 2048;

    private final Configuration configuration;
    private final Channel channel;
    private final Socket socket;
    private final DataInputStream dataInputStream;
    private final ObjectEncoderOutputStream objectOutputStream;

    private static final List<String> pendingSendMessages = new ArrayList<>();


    private Thread read;

    public CheControllerSocket(Configuration configuration, Channel channel, Socket socket) throws IOException {
        this.configuration = configuration;
        this.channel = channel;
        this.socket = socket;
        this.dataInputStream = new DataInputStream(socket.getInputStream());
        this.objectOutputStream = new ObjectEncoderOutputStream(socket.getOutputStream());

        //now start listening to the socket this is ongoing.
        read = new Thread((new Runnable() {
            @Override
            public void run() {
                listen();
            }
        }));

        read.start();
    }

    public void write(Core message){

        try {
             objectOutputStream.writeObject(message);
        } catch (IOException e) {
            configuration.getLogger().error("something bad happened "+e.getMessage());
        }
    }

    private void writeToChannel(String message){

        channel.writeAndFlush(message);

        for(String msg : pendingSendMessages){
            channel.writeAndFlush(msg);
        }
        pendingSendMessages.clear();

    }



    public Channel getChannel() {
        return channel;
    }

    private void listen() {

        try {

            byte[] buffer = new byte[BUFFER_SIZE];
            String partialObject = "";
            int partialOpenBracket = 0;
            int partialCloseBracket = 0;

            int charsRead = 0;

            while ((charsRead = dataInputStream.read(buffer)) != -1) {
                //we need to grab each core message out.
                int openBracket = partialOpenBracket > 0 ? partialOpenBracket : 0;
                int closeBracket = partialCloseBracket > 0 ? partialCloseBracket : 0;
                partialOpenBracket = 0;
                partialCloseBracket = 0;

                //for the given line we need to read all of it
                char[] lineRead = new String(buffer).substring(0, charsRead).toCharArray();
                boolean objectsToRead = true;
                int charPos = 0;

                while (objectsToRead) {

                    boolean objectFound = false;
                    String object = partialObject.trim().isEmpty() ? "" : partialObject;
                    partialObject = "";


                    for (int i = charPos; i < lineRead.length && !objectFound; i++) {
                        if (lineRead[i] == '{') {
                            openBracket++;
                        }
                        if (lineRead[i] == '}') {
                            closeBracket++;
                        }

                        object = object + lineRead[i];

                        if (openBracket == closeBracket) {
                            objectFound = true;
                            charPos = i + 1;
                        }

                        if (i == lineRead.length - 1) {
                            objectsToRead = false;
                        }
                        //if we are partial we need to carry on.
                        if (i == lineRead.length - 1 && !objectFound) {
                            partialObject = object;
                            partialOpenBracket = openBracket;
                            partialCloseBracket = closeBracket;
                        }

                    }

                    //we have a message....so pass it on
                    writeToChannel(object);


                }

            }

        } catch (IOException e) {
            configuration.getLogger().debug("Socket is closed / destroy");
            destroy();
        }

    }

    public void destroy() {
        try {
            objectOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            dataInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        read.interrupt();
        read = null;
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
