package util;

import model.CheControllerObject;
import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;

/**
 * Created by timmytime on 29/12/15.
 */
public class CheCamelController {

    private CamelContext context = new DefaultCamelContext();
    private ProducerTemplate template;

    public CheCamelController() throws Exception {

        context.addRoutes(new RouteBuilder() {
            //testing only at present.  seems to work.  so next up is to build this up for proper testing.  ie send a core message then connect.
            @Override
            public void configure() throws Exception {
                from("netty:tcp://localhost:8089").to("netty:tcp://localhost:8086").process(exchange -> {
                    //create our object.
                    CheControllerObject cheControllerObject = (CheControllerObject) exchange.getIn().getBody();
                    exchange.getOut().setBody(cheControllerObject);
                });
            }
        });

        template = context.createProducerTemplate();

    }


    public void start() throws Exception {
        context.start();
    }

    public void stop() throws Exception {
        context.stop();
    }

    //primarily for testing
    public CamelContext getContext() {
        return context;
    }

    public ProducerTemplate getTemplate() {
        return template;
    }


}
