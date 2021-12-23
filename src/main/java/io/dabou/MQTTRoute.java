package io.dabou;

import javax.enterprise.context.ApplicationScoped;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.processor.aggregate.GroupedExchangeAggregationStrategy;

import static org.apache.camel.LoggingLevel.INFO;

@ApplicationScoped
public class MQTTRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        // from("paho-mqtt5:{{consumer.topic}}?brokerUrl={{broker.url}}")
        from("paho:{{consumer.topic}}?brokerUrl={{broker.url}}")
                // aggregate all the using same expression and group the
                // exchanges to get one single message containing all
                // the others
                .aggregate(new GroupedExchangeAggregationStrategy()).constant(true)
                // wait for 0.5 seconds to aggregate
                .completionTimeout(500L)
                //.to("log:ispindel?showAll=true&multiline=true");
                .log(INFO,"Message: ${body}")
                .bean("logMessagesBean", "list");
    }

}
