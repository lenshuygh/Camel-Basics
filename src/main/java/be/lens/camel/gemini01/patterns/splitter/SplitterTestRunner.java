package be.lens.camel.gemini01.patterns.splitter;

import org.apache.camel.ProducerTemplate;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class SplitterTestRunner implements CommandLineRunner {

    ProducerTemplate producerTemplate;

    public SplitterTestRunner(ProducerTemplate producerTemplate) {
        this.producerTemplate = producerTemplate;
    }

    @Override
    public void run(String... args) throws Exception {
        String builkData = "Java,Apache-Camel,Spring-Boot,ActiveMq,Docker";
        producerTemplate.sendBody("direct:start-bulk-split", builkData);
    }

}
