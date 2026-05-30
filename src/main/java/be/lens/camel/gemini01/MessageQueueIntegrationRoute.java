package be.lens.camel.gemini01;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class MessageQueueIntegrationRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        // pipeline 1 : picking up fs data and submitting to ActiveMQ queue
        from("file:data/queue_inbox?noop=true")
                .routeId("file-to-queue-producer")
                .log("Publishing fileSystem transaction payload over to broker message pipeline queue...")
                .to("activemq:queue:transaction.processing.queue");

        // pipeline 2 : listening async for events arriving on the broker queue
        from("activemq:queue:transaction.processing.queue")
                .routeId("queue-consumer-engine")
                .log("Async received message block off of activemq broker pipeline:")
                .log("Payload data message: ${body}")
                .to("file:data/queue_outbox");
    }

}
