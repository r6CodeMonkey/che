import message.CheMessage;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * Created by timmytime on 17/01/16.
 */
public class TestSocket {

    public static final int BUFFER_SIZE = 2048;
    //we always connect on localhost  / 8085

    private final Socket socket;
    private final DataInputStream dataInputStream;
    private final DataOutputStream dataOutputStream;

    private ActionListener callback;
    private Thread read;

    public TestSocket(ActionListener callback) throws IOException {

        this.callback = callback;

        socket = new Socket("localhost", 8085);

        dataInputStream = new DataInputStream(socket.getInputStream());
        dataOutputStream = new DataOutputStream(socket.getOutputStream());

        read = new Thread((this::listen));

        read.start();
    }

    public void write(CheMessage message){
        try {
            dataOutputStream.write(message.toString().getBytes("UTF-8"));
        } catch (IOException e) { e.printStackTrace();
         //   configuration.getLogger().error("something bad happened " + e.getMessage());
        }
    }

    private void listen(){
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

                    System.out.println("recd "+object);

                    callback.actionPerformed(new ActionEvent(this, 1, object));

                }

            }

        } catch (IOException e) {
            destroy();
        }


    }

    public void destroy(){
        try {
            dataOutputStream.close();
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
