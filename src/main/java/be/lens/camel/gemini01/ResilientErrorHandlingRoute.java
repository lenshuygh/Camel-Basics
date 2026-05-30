package be.lens.camel.gemini01;

import static org.apache.camel.LoggingLevel.WARN;

import java.io.IOException;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class ResilientErrorHandlingRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        // global err handling policy
        // capture specific exceptions accross all routes defined withing this builder
        // context
        onException(IOException.class)

                // retry up to 3 times
                .maximumRedeliveries(3)

                .redeliveryDelay(2000)

                // exponential backing off 2s, 4s, 8s
                .backOffMultiplier(2)
                .retryAttemptedLogLevel(WARN)

                // mark exchange clean after moving to DLC
                .handled(true)

                // send persistent msg payload to Dead Letter Channel folder
                .to("file:data/errors/dlc")
                .log("Failed to process message after retrying, moved safely to error repo.");

        // core app pipeline
        from("file:data/unstable_input?noop=true")
                .routeId("fault-rolerant-pipeline")
                .log("Processing item: ${header.CamelFileName}")

                // inject simulated system disruptions
                .process(new VolatileProcessor())
                .to("file:data/unstable_output");
    }

}
