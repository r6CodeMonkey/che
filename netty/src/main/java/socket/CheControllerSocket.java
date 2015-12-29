package socket;

import io.netty.channel.Channel;

import java.io.DataInputStream;
import java.io.IOException;

/**
 * Created by timmytime on 29/12/15.
 */
public class CheControllerSocket {

    public static final int BUFFER_SIZE = 2048;

    private final Channel channel;
    private final DataInputStream dataInputStream;

    private Thread read;

    public CheControllerSocket(Channel channel, DataInputStream dataInputStream){
        this.channel = channel;
        this.dataInputStream = dataInputStream;

        //now start listening to the socket this is ongoing.
        read = new Thread((new Runnable() {
            @Override
            public void run() {
                listen();
            }
        }));

        read.start();
    }


    private void listen(){

        try{

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
                    channel.writeAndFlush(object);

                    System.out.println("received a message and it says "+object.toString());


                   }


            }

        }catch (IOException e){

        }

    }


}
