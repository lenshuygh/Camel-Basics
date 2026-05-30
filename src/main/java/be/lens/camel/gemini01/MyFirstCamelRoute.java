package be.lens.camel.gemini01;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class MyFirstCamelRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        /*
         * file:
         * source of file(s)
         * noop=true
         * 'no operation'
         * camel won't delete original file from folder
         * 
         */
        from("file:data/input?noop=true")
                // explicit rout ID for tracking in prod.
                .routeId("file-copy-route")

                // .log() -> a processing step, in this case logging the fileName
                .log("processing file: ${header.CamelFileName}")

                // .to() -> where to send the data (folder)
                .to("file:data/output");
    }

}
