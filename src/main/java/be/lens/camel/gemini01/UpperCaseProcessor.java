package be.lens.camel.gemini01;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

public class UpperCaseProcessor implements Processor {

    @Override
    public void process(Exchange exchange) throws Exception {
        // pull input payload out of exchange message body
        String originalBody = exchange.getIn().getBody(String.class);
        String modifiedBody = originalBody.toUpperCase();

        // Push transformed modifications back into exchange payload container
        exchange.getIn().setBody(modifiedBody);
    }

}
