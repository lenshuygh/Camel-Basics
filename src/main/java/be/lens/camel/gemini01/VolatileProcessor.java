package be.lens.camel.gemini01;

import java.io.IOException;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

public class VolatileProcessor implements Processor {
    private static int counters = 0;

    @Override
    public void process(Exchange exchange) throws Exception {
        counters++;
        // simulate system that fails 2 times before recovering on 3
        if (counters % 3 != 0) {
            throw new IOException("Simulated temp IO network outage");
        }
        exchange.getIn().setBody("Processed clean records data body content payload successfully");
    }

}
