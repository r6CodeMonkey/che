package channel.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.nio.charset.Charset;
import java.util.List;

/**
 * Created by timmytime on 10/12/15.
 */
public class JsonFrameDecoder extends ByteToMessageDecoder {

    public static final char OBJECT_START = '{';
    public static final char OBJECT_END = '}';

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf in, List<Object> out) throws Exception {


        Object msg = new String(in.toString(Charset.forName("UTF-8")));

        String message = "";
        int openBracket = 0;
        int closeBracket = 0;

        char[] buffer = msg.toString().toCharArray();
        boolean messageBuffered = false;


        for (int i = 0; i < buffer.length && !messageBuffered; i++) {
            if (buffer[i] == OBJECT_START) {
                openBracket++;
            }
            else if (buffer[i] == OBJECT_END) {
                closeBracket++;
            }

            message = message + String.valueOf(buffer[i]);

            if (openBracket == closeBracket && openBracket != 0) {
                messageBuffered = true;
            }

        }


        if (messageBuffered) {
            in.readBytes(message.getBytes().length); //as every new message has a count in front?
            out.add(message);
        }

    }
}
