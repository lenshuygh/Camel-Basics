package be.lens.camel.gemini01;

import static org.apache.camel.Exchange.HTTP_PATH;
import static org.apache.camel.Exchange.HTTP_RESPONSE_CODE;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class RestIntegrationRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        // SECTION 1 -> expose Rest Endpoint
        restConfiguration().component("servlet");

        rest("/v1/orders")
                .post()
                .description("Receive external client order messages")
                .to("direct:handle-web-order");

        from("direct:handle-web-order")
                .routeId("web-order-receiver")
                .log("HTTP Order Payload Received ${body}")
                .transform().simple("Order Confirmed Processing: ${body}")
                .setHeader(HTTP_RESPONSE_CODE, constant(202));

        // SECTION 2 -> consuming an external rest api
        // polling timer setup that triggers every 30s
        from("timer:weatherTrigger?period=30000")
                .routeId("external-api-poller")
                .log("Fetching curren mock data feed via HTTP...")

                // clear headers prior to outgoing call
                .removeHeader(HTTP_PATH)

                .to("http://jsonplaceholder.typicode.com/posts/1")
                .log("Payload data returned from external server: ${body}")
                .to("file:data");
    }

}
