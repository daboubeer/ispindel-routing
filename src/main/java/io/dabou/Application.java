package io.dabou;

import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.annotations.QuarkusMain;
import org.apache.camel.quarkus.main.CamelMainApplication;
import org.jboss.logging.Logger;

@QuarkusMain
public class Application {

    private static final Logger LOG = Logger.getLogger(Application.class);
    private static String greeting;

    public static void main(String... args) {
        Quarkus.run(CamelMainApplication.class);
    }
}