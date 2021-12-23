package io.dabou;

import io.quarkus.runtime.annotations.RegisterForReflection;
import org.apache.camel.Exchange;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import java.util.List;
import java.util.Map;

@ApplicationScoped
@Named("logMessagesBean")
@RegisterForReflection
public class LogMessages {
    private static final Logger LOG = Logger.getLogger(LogMessages.class);
    public String list(List<Exchange> body) {
        LOG.info("Method list of logMessagesBean called !");
        for(Exchange msg : body) {
            LOG.info("Topic: " + msg.getIn().getHeader("CamelMqttTopic") + ", " + msg.getIn().getBody(String.class));
        }
        return "";
    }
}
