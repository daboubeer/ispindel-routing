package io.dabou;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Named;

import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.annotations.QuarkusMain;
import org.apache.camel.quarkus.main.CamelMainApplication;
import org.jboss.logging.Logger;

@QuarkusMain
public class Application {

    private static final Logger LOG = Logger.getLogger(Application.class);
    private static String greeting;

    public static void main(String... args) {

        /* Any custom logic can be implemented here
         * Here, we pass the value of the first argument to Quarkus CDI container so that it can be injected using
         * @Inject @Named("greeting")
         * And we pass the second argument as -durationMaxMessages which is the number of messages that the application
         * will process before terminating */
        List<String> filteredArgs = new ArrayList<>(args.length);
        if (args.length < 2) {
            LOG.warnf(
                    "Expected at least 2 CLI arguments but got %d. Will proceed with default greeting. Refer to the README instructions.",
                    args.length);
            greeting = "Hello";
            filteredArgs.add("-durationMaxMessages");
            filteredArgs.add("2");
        } else {
            int i = 0;
            greeting = args[i++];
            final String repeatTimes = args[i++];
            filteredArgs.add("-durationMaxMessages");
            filteredArgs.add(repeatTimes);

            for (; i < args.length; i++) {
                filteredArgs.add(args[i++]);
            }
        }

        Quarkus.run(CamelMainApplication.class, filteredArgs.toArray(new String[filteredArgs.size()]));
    }

    @ApplicationScoped
    public static class GreetingProducer {
        @Produces
        @Named("greeting")
        public String greeting() {
            return greeting;
        }
    }
}