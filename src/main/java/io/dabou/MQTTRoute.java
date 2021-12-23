package io.dabou;

import javax.enterprise.context.ApplicationScoped;

import org.apache.camel.builder.RouteBuilder;

import static org.apache.camel.LoggingLevel.DEBUG;

@ApplicationScoped
public class MQTTRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        // from("paho-mqtt5:{{consumer.topic}}?brokerUrl={{broker.url}}")
        from("paho:{{consumer.topic}}?brokerUrl={{broker.url}}").log(DEBUG, "Topic: ${header.CamelMqttTopic}, ${body}");
    }

}
