package be.lens.camel.gemini01;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class TransformationRoute extends RouteBuilder {

	@Override
	public void configure() throws Exception {
		from("file:data/orders?noop=true")
				.routeId("order-processing-pipeline")

				// content base router EIP pattern
				.choice()
				.when(header("CamelFileName").endsWith(".csv"))
				.log("detected CSV file")
				.to("direct:process-csv")

				.when(header("CamelFileName").endsWith(".txt"))
				.log("detected TXT file")
				.process(new UpperCaseProcessor())
				.to("file:data/orders/processed_txt")

				.otherwise()
				.log("Unsupported File Format Type: ${header.CamelFileName}")
				.to("file:data/orders/skipped")

				.end();

		// secondary route segment
		from("direct:process-csv")
				.routeId("csv-to-upperCase")

				// simple data manipulation done inLine
				.transform(body().regexReplaceAll(",", ";"))

				.to("file:data/orders/processed-csv");
	}

}
