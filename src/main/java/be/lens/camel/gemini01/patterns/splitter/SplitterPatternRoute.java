package be.lens.camel.gemini01.patterns.splitter;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class SplitterPatternRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        from("direct:start-bulk-split")
                .routeId("splitter-pipeline")
                .log("received original bulk payload: ${body}")

                // tokenize is how to split it
                .split(body().tokenize(","))

                        // per split chunk:
                        .log("--- Processing Split Child Item ---")
                        .log("Child Body Content: ${body}")

                        // use split-tracking headers
                        .log("Split Index: ${header.CameSplitIndex} | Is Last Item? -> ${header.CamelSplitComplete}")

                        // route individual chucks
                        .to("file:data/split_output?fileName=item_${header:CamelSplitIndex}.txt")

                // this closes Splitter EIP
                .end()

                .log("Successfully completed splitting and processing all sub-messages");

    }
}
