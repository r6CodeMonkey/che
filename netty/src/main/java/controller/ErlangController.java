package controller;

import com.ericsson.otp.erlang.OtpNode;
import util.Configuration;

import java.io.IOException;

/**
 * Created by timmytime on 23/02/16.
 */
public class ErlangController {

    private OtpNode node;
    private final Configuration configuration;

    public ErlangController(Configuration configuration) {
        this.configuration = configuration;
        initNode();

    }

    private void initNode() {
        //could ping it to test this does same..
        try {
            node = new OtpNode("");
        } catch (IOException e) {
            configuration.getLogger().error("init node failed");
        }

    }

    /*
      we will need to send messages to the node...they will be game messages.

      basically they will be player key, game object, game object rules....simples..

     */

    /*
     fuck this off for time being...doesnt make sense currently.
     */


    /*
     we also need to receive messages back
     */


}
