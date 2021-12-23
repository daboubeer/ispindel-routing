package io.dabou;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.camel.builder.RouteBuilder;

/**
 * A simple {@link RouteBuilder}.
 */
@ApplicationScoped
public class TimerRoute extends RouteBuilder {
    @Inject
    @Named("greeting")
    String greeting;

    @Override
    public void configure() throws Exception {
        from("timer:foo?period={{timer.period}}").setBody().constant(greeting).to("log:example");
    }
}